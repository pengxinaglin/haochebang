package com.haoche51.checker.activity.hostling;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.HostlingCompleteAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.HostlingTaskEntity;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.HCDialogUtil;
import com.haoche51.checker.util.QiNiuUploadUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;

/**
 * 完成整备
 * Created by wufx on 16/01/18.
 */
public class HostlingCompleteActivity extends CommonTitleBaseActivity {
  /**
   * 当前选中的整备项目的位置
   */
  public int currPosition;
  @ViewInject(R.id.lv_hostling_complete)
  private ListView mListView;
  @ViewInject(R.id.rl_no_data)
  private RelativeLayout rl_no_data;
  @ViewInject(R.id.sv_hostling_complete)
  private ScrollView sv_hostling_complete;
  @ViewInject(R.id.tv_no_data)
  private TextView tv_no_data;
  private HostlingCompleteAdapter adapter;
  private List<HostlingTaskEntity.ProjectEntity> projectEntityList;
  private HostlingTaskEntity mTask;
  private HostlingTaskEntity.ProjectEntity currProjectEntity;
  private QiNiuUploadUtil qiniuUploadUtil;

  @Override
  public View getHCContentView() {
    return View.inflate(this, R.layout.activity_hostling_complete, null);
  }

  @Override
  public void initContentView(Bundle saveInstanceState) {
    x.view().inject(this);
    mTask = (HostlingTaskEntity) getIntent().getSerializableExtra(TaskConstants.INTENTY_REPAIR);
    //初始化整备项目
    initHostlingProject();
  }

  @Override
  public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
    mTitle.setText(getString(R.string.hc_hostling_complete));
  }

  /**
   * 初始化整备项目
   */
  private void initHostlingProject() {
    if (mTask == null || mTask.getProject() == null || mTask.getProject().size() == 0) {
      tv_no_data.setCompoundDrawablesWithIntrinsicBounds(0,
        R.drawable.common_button_nodata, 0, 0);
      rl_no_data.setVisibility(View.VISIBLE);
      sv_hostling_complete.setVisibility(View.GONE);
      return;
    }
    //保存整备过的项目
    List<HostlingTaskEntity.ProjectEntity> needRemoveProjects = new ArrayList<>();
    for (HostlingTaskEntity.ProjectEntity projectEntity : mTask.getProject()) {
      //已经整备过
      if (projectEntity.getIs_over() == 1) {
        needRemoveProjects.add(projectEntity);
      }
    }
    //移除整备过的项目
    mTask.getProject().removeAll(needRemoveProjects);
    projectEntityList = mTask.getProject();
    adapter = new HostlingCompleteAdapter(this, projectEntityList, needRemoveProjects.size());
    mListView.setAdapter(adapter);
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (data == null || resultCode != RESULT_OK || requestCode != TaskConstants.REQUEST_SELECT_PHOTO) {
      return;
    }
    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
    if (list != null) {
      uploadImageToQiniu(list);
    }
  }

  /**
   * 完成整备按钮
   */
  @Event(R.id.btn_commit)
  private void commit(View v) {
    //检查所有项目的合法性
    if (!checkAllProject()) {
      return;
    }
    //提交完成整备的信息
    commitHostlingComplete();
  }


  /**
   * 检查所有已完成整备的项目，其金额不能为空，图片至少有一张
   *
   * @return
   */
  private boolean checkAllProject() {
    if (projectEntityList != null && projectEntityList.size() > 0) {
      for (int i = 0; i < projectEntityList.size(); i++) {
        HostlingTaskEntity.ProjectEntity projectEntity = projectEntityList.get(i);
        if (projectEntity.getNo_repair() == 0) {//已完成整备
          if (TextUtils.isEmpty(projectEntity.getReal_price())) {
            ToastUtil.showInfo("编号为" + (i + 1) + "的项目金额不能为空");
            return false;
          }
          BigDecimal realPrice;
          try {
            realPrice=new BigDecimal(projectEntity.getReal_price());
          }catch (Exception e){
            realPrice=new BigDecimal(0);
          }

          if (realPrice.intValue() > 2000000 || realPrice.intValue() < 0) {
            ToastUtil.showInfo("编号为" + (i + 1) + "的项目金额不能为空，且不能超过200万");
            return false;
          }

          if (projectEntity.getAfter_image() == null || projectEntity.getAfter_image().size() == 0) {
            ToastUtil.showInfo("编号为" + (i + 1) + "的项目至少有一张图片");
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * 上传图片到七牛
   */
  private void uploadImageToQiniu(List<String> photoPathList) {
    //没有选择图片，显示提示
    if (photoPathList.size() == 0) {
      return;
    }
    qiniuUploadUtil = new QiNiuUploadUtil(this, null, photoPathList);
    qiniuUploadUtil.startUpload(new QiNiuUploadUtil.QiniuUploadListener() {
      @Override
      public void onSuccess(List<String> photoUrlList, List<PhotoEntity> photoList) {
        currProjectEntity = projectEntityList.get(currPosition);
        currProjectEntity.getAfter_image().addAll(photoUrlList);
        adapter.notifyDataSetChanged();
      }
    });
  }

  /**
   * 提交完成整备信息
   */
  private void commitHostlingComplete() {
    //显示对话框
    HCDialogUtil.showProgressDialog(this);
    OKHttpManager.getInstance().post(HCHttpRequestParam.completeHostling(mTask), this, 0);
  }


  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    HCDialogUtil.dismissProgressDialog();
    if (response == null) {
      ToastUtil.showInfo("响应结果为空！");
      return;
    }

    //完成整备
    if (HttpConstants.ACTION_COMPLETE_HOSTLING.equals(action)) {
      switch (response.getErrno()) {
        case 0:
          ToastUtil.showInfo("提交成功");
          setResult(RESULT_OK);
          finish();
          break;
        default:
          ToastUtil.showInfo("提交失败：" + response.getErrmsg());
          break;
      }
    }
  }

  @Override
  protected void onDestroy() {
    //关闭对话框
    HCDialogUtil.dismissProgressDialog();
    if (qiniuUploadUtil != null) {
      qiniuUploadUtil.stopUpload();
      qiniuUploadUtil = null;
    }
    super.onDestroy();
  }
}