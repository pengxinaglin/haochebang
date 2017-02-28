package com.haoche51.checker.activity.purchase;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.vehicle.VehicleBrandActivity;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.PurchasePostPhotoAdapter;
import com.haoche51.checker.adapter.PurchaseSuccessAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.CGridView;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.entity.PurchaseSuccessEntity;
import com.haoche51.checker.entity.PurchaseTaskEntity;
import com.haoche51.checker.entity.VehicleSourceEntity;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.HCArithUtil;
import com.haoche51.checker.util.HCDialogUtil;
import com.haoche51.checker.util.HCLogUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.QiNiuUploadUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * 收车成功
 * Created by wufx on 2016/1/13.
 */
public class PurchaseSuccessActivity extends CommonTitleBaseActivity {

    /**
     * VIN码
     */
    @ViewInject(R.id.ed_vehicle_vin)
    @Required(order = 1, message = "VIN码不能为空")
    private EditText ed_vehicle_vin;

    /**
     * 车辆款型
     */
    @ViewInject(R.id.tv_vehicle_type)
    @Required(order = 2, message = "请补全车辆款型")
    private TextView tv_vehicle_type;

    /**
     * 上牌时间
     */
    @ViewInject(R.id.tv_registration_time)
    @Required(order = 3, message = "上牌时间不能为空")
    private TextView tv_registration_time;
    /**
     * 表显里程
     */
    @ViewInject(R.id.ed_show_mile)
    @Required(order = 4, message = "表显里程不能为空")
    private EditText ed_show_mile;
    /**
     * 过户次数
     */
    @ViewInject(R.id.ed_transfer_times)
    @Required(order = 5, message = "过户次数不能为空")
    private EditText ed_transfer_times;

    /**
     * 退保收益
     */
    @ViewInject(R.id.rg_tbsy)
    private RadioGroup rg_tbsy;


    /**
     * 保险到期时间
     */
    @ViewInject(R.id.tv_bxdq_time)
    private TextView tv_bxdq_time;

    /**
     * 保险到期时间线性布局
     */
    @ViewInject(R.id.ll_bxdq_time)
    private LinearLayout ll_bxdq_time;

    /**
     * 收购价格
     */
    @ViewInject(R.id.ed_purchase_price)
    @Required(order = 6, message = "收购价格不能为空")
    private EditText ed_purchase_price;

    /**
     * 过户费
     */
    @ViewInject(R.id.ed_transfer_fee)
    @Required(order = 7, message = "过户费不能为空")
    private EditText ed_transfer_fee;

    /**
     * 介绍费
     */
    @ViewInject(R.id.ed_referral_fee)
    @Required(order = 8, message = "介绍费不能为空")
    private EditText ed_referral_fee;

    /**
     * 车辆报价
     */
    @ViewInject(R.id.ed_offer_price)
    @Required(order = 9, message = "车辆报价不能为空")
    private EditText ed_offer_price;

    /**
     * 车辆底价
     */
    @ViewInject(R.id.tv_low_price)
    @Required(order = 10, message = "车辆底价不能为空")
    private TextView tv_low_price;

    /**
     * 备注
     */
    @ViewInject(R.id.ed_remark)
    private EditText ed_remark;


    /**
     * 交易照片 GridView
     */
    @ViewInject(R.id.cgv_deal_photo)
    private CGridView cgv_deal_photo;


    /**
     * 发帖照片 GridView
     */
    @ViewInject(R.id.cgv_post_photo)
    private CGridView cgv_post_photo;


    /**
     * 收车任务，进行中详情界面传过来的
     */
    private PurchaseTaskEntity purchaseTask;
    /**
     * 车源信息，进行中详情界面传过来的
     */
    private VehicleSourceEntity vehicleSource;
    private List<PhotoEntity> dealPhotoList;
    private List<PhotoEntity> postPhotoList;

    /**
     * 当前点击的PhotoEntity
     */
    private PhotoEntity curPhotoEntity;
    private PurchaseSuccessAdapter mDealPhotoAdapter;
    private PurchasePostPhotoAdapter mPostPhotoAdapter;
    private QiNiuUploadUtil qiniuUploadUtil;
    private int photoLimit = 10;//发帖照片限制的张数

    /**
     * 车辆底价
     */
    private double basePrice;
    private String tag = "PurchaseSuccessActivity";

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_purchase_success, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this);
        initUI();
        initPhotoList();
        mDealPhotoAdapter = new PurchaseSuccessAdapter(this, dealPhotoList, R.layout.item_purchase_success);
        cgv_deal_photo.setAdapter(mDealPhotoAdapter);
        cgv_deal_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curPhotoEntity = dealPhotoList.get(position);
                openPhotoPicker(TaskConstants.REQUEST_SELECT_DEAL_PHOTO);
            }
        });

        mPostPhotoAdapter = new PurchasePostPhotoAdapter(this, postPhotoList, TaskConstants.POST_PHOTO_TAGS.length, photoLimit);
        cgv_post_photo.setAdapter(mPostPhotoAdapter);
        cgv_post_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curPhotoEntity = postPhotoList.get(position);
                if (position == postPhotoList.size() - 1 && postPhotoList.size() < photoLimit) {
                    openMorePhotoPicker(TaskConstants.REQUEST_SELECT_MORE_POST_PHOTO);
                } else {
                    openPhotoPicker(TaskConstants.REQUEST_SELECT_POST_PHOTO);
                }

            }
        });
    }

    /**
     * 初始化标题栏
     *
     * @param mReturn
     * @param mTitle        标题
     * @param mRightFaction
     */
    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.hc_purchase_success));
        ed_remark.setHint(getString(R.string.hc_purchase_success_remark));
    }

    /**
     * 初始化照片数据
     */
    private void initPhotoList() {
        this.dealPhotoList = new ArrayList<>();
        PhotoEntity photoEntity;
        for (int i = 0; i < TaskConstants.DEAL_PHOTO_TAGS.length; i++) {
            photoEntity = new PhotoEntity();
            photoEntity.setName(getString(TaskConstants.DEAL_PHOTO_TAGS[i]));
            dealPhotoList.add(photoEntity);
        }

        postPhotoList = new ArrayList<>();
        for (int i = 0; i < TaskConstants.POST_PHOTO_TAGS.length; i++) {
            photoEntity = new PhotoEntity();
            photoEntity.setName(getString(TaskConstants.POST_PHOTO_TAGS[i]));
            postPhotoList.add(photoEntity);
        }
        postPhotoList.add(new PhotoEntity());

    }

    /**
     * 初始化界面点击事件及数据
     */
    private void initUI() {
        purchaseTask = getIntent().getParcelableExtra("purchaseTask");

        //设置初始化值
        if (purchaseTask != null) {
            //设置车辆款型
            if (!TextUtils.isEmpty(purchaseTask.getTitle())) {
                if (!TextUtils.isEmpty(purchaseTask.getVehicle_name())) {
                    tv_vehicle_type.setText(purchaseTask.getTitle());
                } else {
                    tv_vehicle_type.setHint(purchaseTask.getTitle());
                }
            }

            //设置默认的vin码
            ed_vehicle_vin.setText(purchaseTask.getVin_code());
        }

        //底价=报价-还价空间
        ed_offer_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    changeBasePrice();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //退保收益
        rg_tbsy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_has) {//有退保收益，就显示保险到期时间
                    ll_bxdq_time.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.rb_none) {
                    ll_bxdq_time.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 修改底价
     */
    private void changeBasePrice() {
        if (TextUtils.isEmpty(ed_offer_price.getText())) {
            tv_low_price.setText("");
            onValidateFailed(ed_offer_price, "车辆报价不能为空");
            return;
        }

        //车辆报价 元
        double offerPrice = HCArithUtil.mul(Double.valueOf(ed_offer_price.getText().toString()), 10000);
        if (offerPrice <= 0) {
            onValidateFailed(ed_offer_price, "车辆报价必须大于0");
            return;
        }

        double tmpPrice;
        int diffPrice;
        if (offerPrice < 50000) {
            diffPrice = 1000;
        } else if (offerPrice < 80000) {
            diffPrice = 2000;
        } else if (offerPrice < 100000) {
            diffPrice = 3000;
        } else if (offerPrice < 150000) {
            diffPrice = 4000;
        } else {
            diffPrice = 5000;
        }
        tmpPrice = HCArithUtil.sub(offerPrice, diffPrice);
        basePrice = HCArithUtil.div(tmpPrice, 10000);
        basePrice = basePrice < 0 ? 0 : basePrice;
        tv_low_price.setText(String.valueOf(basePrice));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getIntent() == null) {
            return;
        }

        vehicleSource = getIntent().getParcelableExtra("vehicleSource");
        if (vehicleSource == null) {
            return;
        }

        String fullName = vehicleSource.getFull_name().trim();
        if (!TextUtils.isEmpty(vehicleSource.getBrand_name()) && !TextUtils.isEmpty(fullName)) {
            tv_vehicle_type.setText(fullName);
        }
    }

    /**
     * 提交收车信息
     */
    @Event(R.id.btn_commit)
    private void commit(View v) {
        validator.validate();
    }

    /**
     * 上牌时间点击事件
     */
    @Event(R.id.tv_registration_time)
    private void clickTimeWhell(View v) {
        //初始化时间滚轮
        DisplayUtils.displayYearAndMonthWhellNoControl(this, tv_registration_time, R.string.select_regist_time);
    }

    /**
     * 保险到期时间点击事件
     */
    @Event(R.id.tv_bxdq_time)
    private void bxdqTimeWhell(View v) {
        //初始化时间滚轮
        DisplayUtils.displayYearAndMonthWhellNoControl(this, tv_bxdq_time, R.string.select_bxdq_time);
    }

    /**
     * 补全车源信息
     *
     * @param view
     */
    @Event((R.id.tv_vehicle_type))
    private void completeVehicleSource(View view) {

        VehicleSourceEntity vehicleSource = new VehicleSourceEntity();
        //来源于收车成功界面
        vehicleSource.setJump_source(PurchaseSuccessActivity.class.getName());
        Intent intent = new Intent();
        intent.putExtra("vehicleSource", vehicleSource);
        intent.setClass(this, VehicleBrandActivity.class);
        startActivity(intent);
    }

    /**
     * 打开照片选择器
     */
    private void openPhotoPicker(int requestCode) {
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        //设置每次只可以选择1张图片
        intent.setPhotoCount(1);
        intent.setShowCamera(false);
        startActivityForResult(intent, requestCode);
    }


    /**
     * 打开照片选择器“更多”
     */
    private void openMorePhotoPicker(int requestCode) {
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        //设置每次只可以选择1张图片
        intent.setPhotoCount(photoLimit - postPhotoList.size() + 1);
        intent.setShowCamera(false);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode != RESULT_OK || curPhotoEntity == null) {
            return;
        }
        ArrayList<String> photoPaths = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
        if (photoPaths != null && photoPaths.size() > 0) {
            curPhotoEntity.setPath(photoPaths.get(0));
            switch (requestCode) {
                case TaskConstants.REQUEST_SELECT_DEAL_PHOTO://交易照片

                    mDealPhotoAdapter.notifyDataSetChanged();
                    break;
                case TaskConstants.REQUEST_SELECT_POST_PHOTO://发帖照片
                    mPostPhotoAdapter.notifyDataSetChanged();
                    break;
                case TaskConstants.REQUEST_SELECT_MORE_POST_PHOTO://发帖照片（更多）
                    PhotoEntity temPhoto;
                    for (int i = 1; i < photoPaths.size(); i++) {
                        if (postPhotoList.size() < photoLimit) {
                            temPhoto = new PhotoEntity();
                            temPhoto.setPath(photoPaths.get(i));
                            postPhotoList.add(temPhoto);
                        }
                    }
                    if (postPhotoList.size() < photoLimit) {
                        postPhotoList.add(new PhotoEntity());
                    }
                    mPostPhotoAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    /**
     * 校验成功
     */
    @Override
    public void onValidationSucceeded() {
        super.onValidationSucceeded();
        try {
            if (ed_vehicle_vin.getText().toString().trim().length() != 17) {
                onValidateFailed(ed_vehicle_vin, "VIN码必须为17位");
                return;
            }

            //退保收益
            if (rg_tbsy.getCheckedRadioButtonId() == R.id.rb_has) {
                if (TextUtils.isEmpty(tv_bxdq_time.getText())) {
                    onValidateFailed(tv_bxdq_time, "保险到期时间不能为空");
                    ToastUtil.showInfo("保险到期不能为空！");
                    return;
                }
            }

            //收购价格
            BigDecimal purchasePrice = new BigDecimal(ed_purchase_price.getText().toString());
            double purchasePriceDouble = purchasePrice.doubleValue();
            if (purchasePriceDouble < 0 || purchasePriceDouble > 100) {
                onValidateFailed(ed_purchase_price, "收购价格必须介于0和100万之间");
                return;
            }

            //车辆报价 元
            BigDecimal unitWan = new BigDecimal(10000);
            BigDecimal offerPrice = new BigDecimal(ed_offer_price.getText().toString()).multiply(unitWan);
            if (offerPrice.compareTo(new BigDecimal(0)) <= 0) {
                onValidateFailed(ed_offer_price, "车辆报价必须大于0");
                return;
            }

            //检查发帖照片
            if (!checkPostPhoto()) {
                ToastUtil.showInfo("左前45度照片不能为空！");
                return;
            }

            //上传图片到七牛
            uploadImageToQiniu();
        } catch (Exception e) {
            HCLogUtil.e(tag, e.getMessage());
            ToastUtil.showInfo("提交收车失败，请检查数据无误后，再次提交");
        }
    }

    /**
     * 发帖照片中一旦选择了照片，提交时需判断：左前45°不能为空
     *
     * @return
     */
    private boolean checkPostPhoto() {
        if (postPhotoList != null && postPhotoList.size() > 0) {
            PhotoEntity photoEntity;
            PhotoEntity firstPhoto = postPhotoList.get(0);//左前45度照片
            boolean flag = TextUtils.isEmpty(firstPhoto.getPath());
            for (int i = 1; i < postPhotoList.size(); i++) {
                photoEntity = postPhotoList.get(i);
                if (flag && !TextUtils.isEmpty(photoEntity.getPath())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 获取需要上传的所有图片列表
     *
     * @return
     */
    private List<PhotoEntity> getNeedUploadPhotoList() {
        List<PhotoEntity> photoList = new ArrayList<>();
        //交易照片
        getTotalPhotoList(dealPhotoList, photoList);
        //发帖照片
        getTotalPhotoList(postPhotoList, photoList);
        return photoList;
    }

    /**
     * 将图片列表整合到一起
     *
     * @return
     */
    private void getTotalPhotoList(List<PhotoEntity> photoList, List<PhotoEntity> totalList) {
        if (photoList != null && photoList.size() > 0) {
            for (PhotoEntity photoEntity : photoList) {
                totalList.add(photoEntity);
            }
        }
    }

    /**
     * 上传图片到七牛
     */
    private void uploadImageToQiniu() {
        try {
            List<PhotoEntity> totalPhotoList = getNeedUploadPhotoList();
            if (totalPhotoList.size() == 0) {
                //上传完成，提交确认信息
                commitPurchaseSuccess(totalPhotoList);
                return;
            }
            qiniuUploadUtil = new QiNiuUploadUtil(this, totalPhotoList, null);
            qiniuUploadUtil.startUpload(new QiNiuUploadUtil.QiniuUploadListener() {
                @Override
                public void onSuccess(List<String> photoUrlList, List<PhotoEntity> photoList) {
                    //上传完成，提交确认信息
                    commitPurchaseSuccess(photoList);
                }
            });
        } catch (Exception e) {
            HCLogUtil.e("PurchaseSuccessActivity", e.getMessage());
        }
    }

    /**
     * 显示校验失败消息
     *
     * @param failedView
     * @param message
     */
    private void onValidateFailed(TextView failedView, String message) {
        failedView.requestFocus();
        failedView.setError(message);
    }


    /**
     * 提交收车成功信息至服务器
     */
    private void commitPurchaseSuccess(List<PhotoEntity> photoList) {
        PurchaseSuccessEntity purchaseSuccessEntity = new PurchaseSuccessEntity();
        purchaseSuccessEntity.setTaskId(purchaseTask.getTask_id());
        //设置VIN码
        purchaseSuccessEntity.setVin(ed_vehicle_vin.getText().toString().trim());
        //设置品牌和车系、年款、车型
        if (vehicleSource == null) {
            vehicleSource = new VehicleSourceEntity();
            vehicleSource.setBrand_id(purchaseTask.getBrand_id());
            vehicleSource.setBrand_name(purchaseTask.getBrand_name());
            vehicleSource.setSeries_id(purchaseTask.getClass_id());
            vehicleSource.setSeries_name(purchaseTask.getClass_name());
            vehicleSource.setYear(purchaseTask.getYear());
            vehicleSource.setModel_id(purchaseTask.getVehicle_id());
            vehicleSource.setModel_name(purchaseTask.getVehicle_name());
        }
        purchaseSuccessEntity.setVehicleSource(vehicleSource);

        //上牌时间
        String regist_time_str = tv_registration_time.getText().toString().trim();
        if (!TextUtils.isEmpty(regist_time_str)) {
            purchaseSuccessEntity.setRegistTime(UnixTimeUtil.getUnixTime(regist_time_str, UnixTimeUtil.YEAR_MONTH_PATTERN));
        }

        //表显里程（单位万公里）
        purchaseSuccessEntity.setShowMile(Float.valueOf(ed_show_mile.getText().toString()));

        //过户次数
        purchaseSuccessEntity.setTransferTimes(Integer.parseInt(ed_transfer_times.getText().toString()));

        //退保收益
        if (rg_tbsy.getCheckedRadioButtonId() == R.id.rb_has) {
            purchaseSuccessEntity.setHasBxsy(1);//有
            if (!TextUtils.isEmpty(tv_bxdq_time.getText())) {
                String bxdqTimeStr = tv_bxdq_time.getText().toString().trim();
                purchaseSuccessEntity.setBxdqTime(UnixTimeUtil.getUnixTime(bxdqTimeStr, UnixTimeUtil.YEAR_MONTH_PATTERN));
            }
        }else{
            purchaseSuccessEntity.setHasBxsy(2);//无
        }

        //收购价格 元
        BigDecimal unitWan = new BigDecimal(10000);
        BigDecimal purchasePrice = new BigDecimal(ed_purchase_price.getText().toString()).multiply(unitWan);
        purchaseSuccessEntity.setPurchasePrice(purchasePrice.intValue());

        //过户费
        purchaseSuccessEntity.setTransferFee(Integer.parseInt(ed_transfer_fee.getText().toString()));

        //介绍费
        purchaseSuccessEntity.setReferralFee(Integer.parseInt(ed_referral_fee.getText().toString()));

        //车辆报价 元
        BigDecimal offerPrice = new BigDecimal(ed_offer_price.getText().toString()).multiply(unitWan);
        purchaseSuccessEntity.setOfferPrice(offerPrice.intValue());

        //备注说明
        if (!TextUtils.isEmpty(ed_remark.getText())) {
            purchaseSuccessEntity.setRemark(ed_remark.getText().toString().trim());
        }

        //如果所有图片都已经上传完，就提交收车信息
        if (isAllPhotoUpload(photoList)) {
            purchaseSuccessEntity.setPhotoList(photoList);
            //显示对话框
            HCDialogUtil.showProgressDialog(this);
            OKHttpManager.getInstance().post(HCHttpRequestParam.commitPurchaseSuccess(purchaseSuccessEntity), this, 0);
        }
    }

    /**
     * 判断是否所有图片都已经上传了
     *
     * @return
     */
    private boolean isAllPhotoUpload(List<PhotoEntity> photoList) {
        PhotoEntity tempPhoto;
        if (photoList != null && photoList.size() > 0) {
            for (int i = 0; i < photoList.size(); i++) {
                tempPhoto = photoList.get(i);
                if (!TextUtils.isEmpty(tempPhoto.getPath()) && TextUtils.isEmpty(tempPhoto.getUrl())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        HCDialogUtil.dismissProgressDialog();
        if (response == null) {
            ToastUtil.showInfo("响应结果为空！");
            return;
        }
        //提交收车收车信息
        if (HttpConstants.ACTION_COMMIT_PURCHASE_SUCCESS.equals(action)) {
            switch (response.getErrno()) {
                case 0:
                    ToastUtil.showInfo("提交收车信息成功");
                    setResult(RESULT_OK);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(TaskConstants.BINDLE_TASK_SUCCESS, true);
                    HCTasksWatched.getInstance().notifyWatchers(bundle);
                    Intent intent = new Intent(PurchaseSuccessActivity.this, PurchaseBuyBackDetailActivity.class);
                    intent.putExtra("task_id", purchaseTask.getTask_id());
                    startActivity(intent);
                    finish();
                    break;
                default:
                    ToastUtil.showInfo("提交收车信息失败：" + response.getErrmsg());
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        HCDialogUtil.dismissProgressDialog();
        ProgressDialogUtil.closeProgressDialog();
        super.onDestroy();
    }


}