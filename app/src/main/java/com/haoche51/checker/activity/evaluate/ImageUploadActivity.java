package com.haoche51.checker.activity.evaluate;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.DAO.CompressedPhotoDAO;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.media.AudioPickerActivity;
import com.haoche51.checker.activity.media.VideoPickerActivity;
import com.haoche51.checker.activity.widget.CommonBaseActivity;
import com.haoche51.checker.constants.PictureConstants;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.SelectCarPhotoTagPopUpWindow;
import com.haoche51.checker.custom.SelectDefectsPhotoPositionPopUpWindow;
import com.haoche51.checker.entity.CheckReportEntity;
import com.haoche51.checker.entity.MediaEntity;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.fragment.evaluate.CheckTaskFragment;
import com.haoche51.checker.helper.UploadTaskPhotoHelper;
import com.haoche51.checker.helper.UserDataHelper;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.pager.BaseTagPager;
import com.haoche51.checker.pager.CarPhotoPager;
import com.haoche51.checker.pager.DefectsPhotoPager;
import com.haoche51.checker.pager.MediaFilesPager;
import com.haoche51.checker.pager.TabBasePhotoPager;
import com.haoche51.checker.util.DefectPhotoComparator;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.HCCheckTaskUtil;
import com.haoche51.checker.util.PhotoComparator;
import com.haoche51.checker.util.PreferenceUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import me.iwf.photopicker.PhotoPickerActivity;

import static com.haoche51.checker.constants.PictureConstants.SELECT_AUDIO;
import static com.haoche51.checker.constants.PictureConstants.SELECT_CAR_PICTURE;
import static com.haoche51.checker.constants.PictureConstants.SELECT_DEFECT_PICTURE;
import static com.haoche51.checker.constants.PictureConstants.SELECT_VIDEO;


public class ImageUploadActivity extends CommonBaseActivity implements TabBasePhotoPager.OnPhotoListChangedListener,
        PopupWindow.OnDismissListener, BaseTagPager.OnTagSelectedListener, SelectDefectsPhotoPositionPopUpWindow.OnFlawSelectListener,
        UploadTaskPhotoHelper.OnCompressPhotoSuccessedListener {

  //外观图、内视图、细节图、瑕疵图
  public List<PhotoEntity> outerPictures, innerPictures, detailPictures, defectPictures;
  /**
   * 设置图片标签
   */
  public int currentClickPhotoIndex;
  public CheckReportEntity mCheckReport;

  /**
   * 上传报告
   */
  public boolean click = false;
  /**
   * 上传报告
   */
  @ViewInject(R.id.tabpager)
  ViewPager tabpager;

  @ViewInject(R.id.tv_common_back)
  TextView tv_common_back;

  @ViewInject(R.id.tv_delete)
  TextView tv_delete;
  /**
   * 删除照片
   */
  int deleteStatus = 0;
  @ViewInject(R.id.tv_car)
  private TextView car;
  @ViewInject(R.id.tv_defect)
  private TextView defect;
  @ViewInject(R.id.tv_media)
  private TextView media;
  @ViewInject(R.id.cb_channel)
  private CheckBox cb_channel;//渠寄车源
  private List<TabBasePhotoPager> mPagers;
  private TabAdapter mTabAdapter;
  private SelectCarPhotoTagPopUpWindow carPhotoTagPopUpWindow;
  private SelectDefectsPhotoPositionPopUpWindow defectsPhotoPositionPopUpWindow;
  private int mTaskId;
  private int mReportId = 0;
  private UploadTaskPhotoHelper helper;
  private OnDeleteOrTagChangeListener onDeleteOrTagChangeListener;
  private TextView[] mTvs;
  /**
   * 广播接收器--接收删除以前选择上传图片记录的广播
   */
  private BroadcastReceiver clearRecordReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      try {
        CompressedPhotoDAO.getInstance().truncate();
      } catch (Exception e) {
      }
    }
  };

  @Override
  protected void initView() {
    super.initView();
    setContentView(R.layout.activity_image_upload_activity);
    x.view().inject(this);
    initUI();
    //注册广播，用于退出时关闭页面
    IntentFilter finishIntent = new IntentFilter();
    finishIntent.addAction("com.haoche51.clearRecordReceiver.action");
    registerReceiver(clearRecordReceiver, finishIntent);
  }


  /**
   * 初始化所有的操作按钮
   */
  private void initHeaderTvs() {
    mTvs = new TextView[3];
    mTvs[0] = car;
    mTvs[1] = defect;
    mTvs[2] = media;
    this.car.setOnClickListener(new MyOnClickListener(0));
    this.defect.setOnClickListener(new MyOnClickListener(1));
    this.media.setOnClickListener(new MyOnClickListener(2));
  }


  @Override
  protected void initData() {
    /** 获取报告信息. */
    this.mReportId = getIntent().getIntExtra("id", 0);
    this.mCheckReport = CheckReportDAO.getInstance().get(this.mReportId);
    if (this.mCheckReport == null)
      return;
    /** 获取任务信息 */
    this.mTaskId = this.mCheckReport.getCheck_appointment_id();
    cb_channel.setChecked(mCheckReport.getIs_channel() == 1 ? true : false);
    //拿到此任务的拍摄图片
    Map<String, List<PhotoEntity>> images = HCCheckTaskUtil.getTaskPhotoList(mCheckReport);
    this.outerPictures = images.get(PictureConstants.OUTER_PICTURE_TYPE);
    this.mPagers.get(0).photoPaths.addAll(this.outerPictures);
    this.innerPictures = images.get(PictureConstants.INNER_PICTURE_TYPE);
    this.mPagers.get(0).photoPaths.addAll(this.innerPictures);
    this.detailPictures = images.get(PictureConstants.DETAIL_PICTURE_TYPE);
    this.mPagers.get(0).photoPaths.addAll(this.detailPictures);
    this.defectPictures = images.get(PictureConstants.DEFECT_PICTURE_TYPE);
    this.mPagers.get(1).photoPaths.addAll(this.defectPictures);
    this.mPagers.get(2).videoEntity = HCCheckTaskUtil.getTaskVideoEntity(mCheckReport);
    this.mPagers.get(2).audioEntity = HCCheckTaskUtil.getTaskAudioEntity(mCheckReport);
    this.mPagers.get(2).initData();
    this.onChange();

    initPopUpWindows();
  }

  private void initUI() {
    initHeaderTvs();
    tv_common_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    this.tabpager.setOffscreenPageLimit(0);
    this.tabpager.setOnPageChangeListener(new MyOnPageChangeListener());

    this.mPagers = new ArrayList<>();
    this.mPagers.add(new CarPhotoPager(this));
    this.mPagers.get(0).setOnPhotoListChangedListener(this);
    this.mPagers.add(new DefectsPhotoPager(this));
    this.mPagers.get(1).setOnPhotoListChangedListener(this);
    this.mPagers.add(new MediaFilesPager(this));
    this.mTabAdapter = new TabAdapter();
    this.tabpager.setAdapter(this.mTabAdapter);
  }

  @Override
  public void onChange() {
    List<PhotoEntity> allPhotos = new ArrayList<>();
    for (TabBasePhotoPager e : mPagers) {
      allPhotos.addAll(e.getPhotoPaths());
    }

    //图片总数>0 && 删除状态=DELETESTATUS_CANCEL，说明原状态为“删除图片”，变为“完成”状态 || 正在删除中状态
    if (allPhotos.size() > 0 && (deleteStatus == PictureConstants.DELETESTATUS_CANCEL || deleteStatus == PictureConstants.DELETESTATUS_FINISH)) {
      tv_delete.setText(getString(R.string.p_finish));
      tv_delete.setVisibility(View.VISIBLE);
      deleteStatus = PictureConstants.DELETESTATUS_FINISH;
    }
    //有图可删
    else if (allPhotos.size() > 0) {
      tv_delete.setText(getString(R.string.delete_image));
      tv_delete.setVisibility(View.VISIBLE);
      deleteStatus = PictureConstants.DELETESTATUS_DELTE;
    } else {//无图
      tv_delete.setText("");
      tv_delete.setVisibility(View.GONE);
      deleteStatus = PictureConstants.DELETESTATUS_NULL;
      //没有图片了，认为是删除完成
      mPagers.get(0).deletePhotoFinish();
      mPagers.get(1).deletePhotoFinish();
    }
  }

  public void setOnDeleteOrTagChangeListener(OnDeleteOrTagChangeListener onDeleteOrTagChangeListener) {
    this.onDeleteOrTagChangeListener = onDeleteOrTagChangeListener;
  }

  @Override
  public void onDeleteOrTagChange(String tagName) {
    if (this.onDeleteOrTagChangeListener != null && !TextUtils.isEmpty(tagName))
      this.onDeleteOrTagChangeListener.onChange(tagName);
  }

  public void deletePhotos(View v) {
    List<PhotoEntity> allPhotos = new ArrayList<>();
    allPhotos.addAll(mPagers.get(0).getPhotoPaths());
    allPhotos.addAll(mPagers.get(1).getPhotoPaths());

    //删除1 完成2 取消3 “”没有图片4
    switch (deleteStatus) {
      case PictureConstants.DELETESTATUS_DELTE:
        mPagers.get(0).deletePhoto();
        mPagers.get(1).deletePhoto();
        tv_delete.setText(getString(R.string.p_cancel));
        deleteStatus = PictureConstants.DELETESTATUS_CANCEL;
        break;
      case PictureConstants.DELETESTATUS_FINISH:
        if (allPhotos.size() > 0) {
          tv_delete.setText(getString(R.string.delete_image));
          tv_delete.setVisibility(View.VISIBLE);
          deleteStatus = PictureConstants.DELETESTATUS_DELTE;
        } else {
          tv_delete.setText("");
          tv_delete.setVisibility(View.GONE);
          deleteStatus = PictureConstants.DELETESTATUS_NULL;
        }
        mPagers.get(0).deletePhotoFinish();
        mPagers.get(1).deletePhotoFinish();
        break;
      case PictureConstants.DELETESTATUS_CANCEL:
        tv_delete.setText(getString(R.string.delete_image));
        mPagers.get(0).cancelDeletePhoto();
        mPagers.get(1).cancelDeletePhoto();
        deleteStatus = PictureConstants.DELETESTATUS_DELTE;
        break;
      case PictureConstants.DELETESTATUS_NULL:
        //没有图片可以删除
        break;
      default:
        if (allPhotos.size() > 0) {
          tv_delete.setText(getString(R.string.delete_image));
          tv_delete.setVisibility(View.VISIBLE);
          deleteStatus = PictureConstants.DELETESTATUS_DELTE;
        } else {
          tv_delete.setText("");
          tv_delete.setVisibility(View.GONE);
          deleteStatus = PictureConstants.DELETESTATUS_NULL;
        }
        break;
    }
  }

  /**
   * 防止已进入页面后删除照片，造成标签不对应，所以先进行初始化
   */
  private void initPopUpWindows() {
    if (carPhotoTagPopUpWindow == null) {
      carPhotoTagPopUpWindow = new SelectCarPhotoTagPopUpWindow(this);
      carPhotoTagPopUpWindow.setOnDismissListener(this);
    }

    if (defectsPhotoPositionPopUpWindow == null) {
      defectsPhotoPositionPopUpWindow = new SelectDefectsPhotoPositionPopUpWindow(this);
      defectsPhotoPositionPopUpWindow.setOnFlawSelectListener(this);
      defectsPhotoPositionPopUpWindow.setOnDismissListener(this);
    }
  }

  public void setPhotoTag(int type, View view) {
    //让屏幕变暗
    DisplayUtils.setActivityBackgroundAlpha(this, 1);
    currentClickPhotoIndex = (int) view.getTag();
    //汽车照片，弹出选择标签窗
    if (type == SELECT_CAR_PICTURE) {
      carPhotoTagPopUpWindow.showPopupWindow(view);
    }
    //瑕疵照片，选择瑕疵位置
    else if (type == SELECT_DEFECT_PICTURE) {
      if (currentClickPhotoIndex > -1) {
        PhotoEntity photoEntity = mPagers.get(1).getPhotoPaths().get(currentClickPhotoIndex);
        defectsPhotoPositionPopUpWindow.showPopupWindow(view, currentClickPhotoIndex + 1, photoEntity.getPosition_x(), photoEntity.getPosition_y());
      }
    }
  }

  /**
   * 车身位置标签选择回调
   */
  @Override
  public void onTagSelected(String tagName, int type, int index, int enumeration) {
    try {
      if (carPhotoTagPopUpWindow != null)
        carPhotoTagPopUpWindow.dismissPopUpWindow();
      PhotoEntity checkSelectTag = checkSelectTag(tagName);
      if (checkSelectTag == null) {//这个模板有没有使用 null 未使用
        onDeleteOrTagChange(mPagers.get(0).getPhotoPaths().get(currentClickPhotoIndex).getName());
        mPagers.get(0).getPhotoPaths().get(currentClickPhotoIndex).setName(tagName);
        mPagers.get(0).getPhotoPaths().get(currentClickPhotoIndex).setType(type);
        mPagers.get(0).getPhotoPaths().get(currentClickPhotoIndex).setIndex(index);
        mPagers.get(0).getPhotoPaths().get(currentClickPhotoIndex).setEnumeration(enumeration);

        mPagers.get(0).mAdapter.notifyDataSetChanged();
      } else {
        //检查他是否是一个图片重复选择了此标签
        String currentPhoto = mPagers.get(0).getPhotoPaths().get(currentClickPhotoIndex).getName();
        if (!TextUtils.isEmpty(currentPhoto) && currentPhoto.equals(tagName))
          ToastUtil.showInfo(getString(R.string.dont_choose_same_tag));
        else
          //这个模板已经使用了
          isResetTagName(checkSelectTag, tagName, type, index, enumeration);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 瑕疵位置选择回调
   */
  @Override
  public void selected(float positionX, float positionY, int position) {
    if (defectsPhotoPositionPopUpWindow != null)
      defectsPhotoPositionPopUpWindow.dismissPopUpWindow();
    PhotoEntity photoEntity = mPagers.get(1).getPhotoPaths().get(currentClickPhotoIndex);
    photoEntity.setPosition_x(positionX);
    photoEntity.setPosition_y(positionY);
    mPagers.get(1).mAdapter.notifyDataSetChanged();
  }

  /**
   * 瑕疵位置取消回调
   */
  @Override
  public void cancel(int position) {
    if (defectsPhotoPositionPopUpWindow != null)
      defectsPhotoPositionPopUpWindow.dismissPopUpWindow();
  }

  /**
   * 检测此标签是否已使用
   */
  public PhotoEntity checkSelectTag(String tagName) {
    List<PhotoEntity> photoPaths = mPagers.get(0).getPhotoPaths();
    for (PhotoEntity e : photoPaths) {
      if (e.getName() != null && e.getName().equals(tagName))
        return e;
    }
    return null;
  }

  /**
   * 是否重新设置标签
   */
  public void isResetTagName(final PhotoEntity checkSelectTag, final String tagName, final int type, final int index, final int enumeration) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(getString(R.string.whether_to_replace));
    builder.setTitle(getString(R.string.tips));
    builder.setPositiveButton(getString(R.string.p_ok), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        //重新设置
        onDeleteOrTagChange(mPagers.get(0).getPhotoPaths().get(currentClickPhotoIndex).getName());
        checkSelectTag.setName("");//原来占用的图片名字置为空
        checkSelectTag.setType(0);//原来占用的图片type置为0
        checkSelectTag.setIndex(0);//原来的index置为0
        checkSelectTag.setEnumeration(0);//TODO 设置原来的图片Enumeration为0

        mPagers.get(0).getPhotoPaths().get(currentClickPhotoIndex).setName(tagName);
        mPagers.get(0).getPhotoPaths().get(currentClickPhotoIndex).setType(type);
        mPagers.get(0).getPhotoPaths().get(currentClickPhotoIndex).setIndex(index);
        //TODO
        mPagers.get(0).getPhotoPaths().get(currentClickPhotoIndex).setEnumeration(enumeration);

        mPagers.get(0).mAdapter.notifyDataSetChanged();
        dialogInterface.dismiss();
      }
    });
    builder.setNegativeButton(getString(R.string.soft_update_cancel), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
      }
    });
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }

  /**
   * 监听popupwindow关闭
   */
  @Override
  public void onDismiss() {
    DisplayUtils.setActivityBackgroundAlpha(this, 1);
  }

  public void previewPhoto(Intent intent) {
    startActivity(intent);
  }

  public int getReportId() {
    return mReportId;
  }

  public CheckReportEntity getCheckReport() {
    return mCheckReport;
  }

  public void finish(View v) {
    this.finish();
  }

  /**
   * 上传报告
   */
  public void uploadReport(final View v) {
    //正在删除图片
    if (click || deleteStatus == PictureConstants.DELETESTATUS_FINISH || deleteStatus == PictureConstants.DELETESTATUS_CANCEL) {
      ToastUtil.showInfo(getString(R.string.other_operating));
      return;
    }
    click = true;
    //请求服务器将此任务状态置为待上传
    OKHttpManager.getInstance().post(HCHttpRequestParam.completeCheckReport(this.mTaskId), null, 0);
    Log.e("uploadReport", "click+++++++++++++");
    //对图片重新排序，按标准顺序排列
    reSplitPhotoByType();
    //汽车图
    PhotoComparator comparator = new PhotoComparator();
    Collections.sort(this.outerPictures, comparator);
    Collections.sort(this.innerPictures, comparator);
    Collections.sort(this.detailPictures, comparator);
    //瑕疵图片
    DefectPhotoComparator defectPhotoComparator = new DefectPhotoComparator();
    Collections.sort(this.defectPictures, defectPhotoComparator);
    //保存是否是渠寄车源
    CheckReportEntity checkReport = CheckReportDAO.getInstance().get(mReportId);
    if (checkReport != null) {
      //保存音视频信息
      checkReport.setVideo_url(mPagers.get(2).videoEntity == null ? "" : mPagers.get(2).videoEntity.toJson());
      checkReport.setAudio_url(mPagers.get(2).audioEntity == null ? "" : mPagers.get(2).audioEntity.toJson());
      checkReport.setIs_channel(cb_channel.isChecked() ? 1 : 0);
      CheckReportDAO.getInstance().update(mReportId, checkReport);
    }
    //上传
    Map<String, List<PhotoEntity>> images = new HashMap<>();
    images.put(PictureConstants.OUTER_PICTURE_TYPE, this.outerPictures);
    images.put(PictureConstants.INNER_PICTURE_TYPE, this.innerPictures);
    images.put(PictureConstants.DETAIL_PICTURE_TYPE, this.detailPictures);
    images.put(PictureConstants.DEFECT_PICTURE_TYPE, this.defectPictures);
    helper = new UploadTaskPhotoHelper(images, ((MediaFilesPager) mPagers.get(2)).audioEntity, ((MediaFilesPager) mPagers.get(2)).videoEntity, this.mTaskId, this.mReportId, this);
    helper.setOnCompressPhotoSuccessedListener(this);
    helper.startUpload(cb_channel.isChecked());//调用上传
  }

  @Override
  public void onCompressPhotoSuccess() {
    Intent backIntent = new Intent(this, CheckMainActivity.class);
    backIntent.putExtra(TaskConstants.BINDLE_FRAGMENT_INDEX, TaskConstants.FRAGMENT_UPLOAD_TASK);
    startActivity(backIntent);
    finish();
  }

  /**
   * 重新case图片种类
   */
  public void reSplitPhotoByType() {
    //汽车图
    List<PhotoEntity> carPhotos = mPagers.get(0).getPhotoPaths();
    outerPictures.clear();
    innerPictures.clear();
    detailPictures.clear();
    for (PhotoEntity e : carPhotos) {
      switch (e.getType()) {
        case PictureConstants.OUTER_PICTURE_CHOSE:
          outerPictures.add(e);
          break;
        case PictureConstants.INNER_PICTURE_CHOSE:
          innerPictures.add(e);
          break;
        case PictureConstants.DETAIL_PICTURE_CHOSE:
          detailPictures.add(e);
          break;
        default://没有图片类型，默认是细节图片
          e.setType(PictureConstants.DETAIL_PICTURE_CHOSE);
          detailPictures.add(e);
          break;
      }
    }
    //瑕疵图
    defectPictures.clear();
    defectPictures.addAll(mPagers.get(1).getPhotoPaths());
  }

  /**
   * 保存报告
   */
  public void saveReport(View v) {
    //正在删除图片
    if (deleteStatus == PictureConstants.DELETESTATUS_FINISH || deleteStatus == PictureConstants.DELETESTATUS_CANCEL) {
      ToastUtil.showInfo(getString(R.string.other_operating));
      return;
    }

    Executors.newCachedThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        CheckReportEntity checkReport = CheckReportDAO.getInstance().get(mReportId);
        if (checkReport == null)
          return;
        Gson gson = new Gson();
        reSplitPhotoByType();
        checkReport.setOut_pics(gson.toJson(outerPictures));
        checkReport.setInner_pics(gson.toJson(innerPictures));
        checkReport.setDetail_pics(gson.toJson(detailPictures));
        checkReport.setDefect_pics(gson.toJson(defectPictures));
        checkReport.setVideo_url(mPagers.get(2).videoEntity == null ? "" : mPagers.get(2).videoEntity.toJson());
        checkReport.setAudio_url(mPagers.get(2).audioEntity == null ? "" : mPagers.get(2).audioEntity.toJson());
        checkReport.setIs_channel(cb_channel.isChecked() ? 1 : 0);
        //保存一个标示，表示用户已经保存过报过了
        PreferenceUtil.putBoolean(ImageUploadActivity.this, UserDataHelper.CHECKER_SAVED_REPORT + mCheckReport.getId(), true);

        final int ret = CheckReportDAO.getInstance().update(mReportId, checkReport);
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            if (ret > 0) {
              ProgressDialogUtil.showProgressDialog(ImageUploadActivity.this, getString(R.string.later));
              //请求服务器将此任务状态置为待上传
              OKHttpManager.getInstance().post(HCHttpRequestParam.completeCheckReport(mTaskId), ImageUploadActivity.this, 0);
            } else {
              ToastUtil.showInfo(getString(R.string.save_report_error));
            }
          }
        });

      }
    });

  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    if (!isFinishing())
      ProgressDialogUtil.closeProgressDialog();

    if (action.equals(HttpConstants.ACTION_COMPLETE_CHECK_REPORT)) {
      responseCompleteTask(response);
    }
  }


  /**
   * 处理请求网络完成验车 待上传
   */
  private void responseCompleteTask(HCHttpResponse response) {
    switch (response.getErrno()) {
      case 0:
        Bundle data = new Bundle();
        data.putBoolean(CheckTaskFragment.ONCHANGED_COMPLETECHECK, true);
        HCTasksWatched.getInstance().notifyWatchers(data);
        Intent intent = new Intent(this, CheckMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        break;
      default:
        ToastUtil.showInfo(response.getErrmsg());
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    List<String> photos = null;
    MediaEntity audioEntity = null;
    MediaEntity videoEntity = null;
    if (resultCode == RESULT_OK) {
      if (data != null) {
        photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
        audioEntity = data.getParcelableExtra(AudioPickerActivity.KEY_SELECTED_AUDIO);
        videoEntity = data.getParcelableExtra(VideoPickerActivity.KEY_SELECTED_VIDEO);
      }

      TabBasePhotoPager pager = null;
      switch (requestCode) {
        case SELECT_CAR_PICTURE://汽车图
          pager = mPagers.get(0);
          break;
        case SELECT_DEFECT_PICTURE://瑕疵图
          pager = mPagers.get(1);
          break;
        case SELECT_VIDEO://视频
          pager = mPagers.get(2);
          pager.chooseVideo(videoEntity);
          return;
        case SELECT_AUDIO://音频
          pager = mPagers.get(2);
          pager.chooseAudio(audioEntity);
          return;
      }

      if (pager!=null && photos != null && photos.size() > 0) {
        pager.notifyDataChanged(photos);
        onChange();
      }
    }
  }

  @Override
  public void finish() {
    //当Activity退出的时候把dialog关闭，避免报error
    ProgressDialogUtil.closeProgressDialog();
    super.finish();
  }

  /**
   * 修改按钮样式
   *
   * @param position
   */
  private void changeBtnsStyle(int position) {
    TextView btn;
    for (int i = 0; i < mTvs.length; i++) {
      btn = mTvs[i];
      if (i == position) {
        btn.setTextColor(getResources().getColor(R.color.hc_indicator));
        btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        btn.setBackgroundResource(R.drawable.bottom_line_light_blue);
      } else {
        btn.setTextColor(getResources().getColor(R.color.self_black));
        btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        btn.setBackgroundResource(R.color.hc_self_white);
      }
    }
  }

  @Override
  protected void onDestroy() {
    ProgressDialogUtil.closeProgressDialog();

    if (clearRecordReceiver != null) {
      unregisterReceiver(clearRecordReceiver);
      clearRecordReceiver = null;
    }
    //停止正在运行的线程
    if (helper != null) {
      helper.enableCompress(false);
      helper.disAlertDialog();
    }
    mPagers.clear();
    super.onDestroy();
  }

  public interface OnDeleteOrTagChangeListener {
    void onChange(String tagName);
  }

  public class MyOnClickListener implements View.OnClickListener {
    private int index = 0;

    public MyOnClickListener(int i) {
      index = i;
    }

    @Override
    public void onClick(View v) {
      tabpager.setCurrentItem(index);
    }
  }

  public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
    @Override
    public void onPageSelected(int position) {
      changeBtnsStyle(position);
      mPagers.get(position).initUI();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
      if (position == mPagers.size())
        return;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
      if (arg0 == mPagers.size())
        return;
    }
  }

  private class TabAdapter extends PagerAdapter {
    @Override
    public int getCount() {
      return mPagers.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
      return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      View view = mPagers.get(position).getRootView();
      container.addView(view);
      if (position == 0) {
        mPagers.get(position).initUI();
      }
      return view;
    }
  }
}
