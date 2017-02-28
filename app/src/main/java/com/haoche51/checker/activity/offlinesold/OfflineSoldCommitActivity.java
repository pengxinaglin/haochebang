package com.haoche51.checker.activity.offlinesold;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.SaleChannelAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.OfflineSoldEntity;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.entity.SaleChannelEntity;
import com.haoche51.checker.helper.ImageLoaderHelper;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.HCLogUtil;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.QiNiuUploadUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.settlement.cashiers.SettlementIntent;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * 线下售出——提交售出页面
 */
public class OfflineSoldCommitActivity extends CommonTitleBaseActivity {

    private static final int[] PHOTO_TAGS = {R.string.hc_buyer_id_card, R.string.hc_contract, R.string.hc_three_people};
    private final int PAY_REQUEST_CODE = 3000;
    /**
     * 买家姓名
     */
    @ViewInject(R.id.ed_offline_buyer_name)
    @Required(order = 1, message = "买家姓名不能为空")
    private EditText ed_offline_buyer_name;
    /**
     * 买家电话
     */
    @ViewInject(R.id.ed_offline_buyer_phone)
    private EditText ed_offline_buyer_phone;
    /**
     * 售出价格
     */
    @ViewInject(R.id.ed_offline_sold_price)
    @Required(order = 2, message = "售出价格不能为空")
    private EditText ed_offline_sold_price;
    /**
     * 哪方付费
     */
    @ViewInject(R.id.rg_which_pay)
    private RadioGroup rg_which_pay;
    /**
     * 过户费用线性布局
     */
    @ViewInject(R.id.ll_transfer_fee)
    private LinearLayout ll_transfer_fee;
    /**
     * 过户费用
     */
    @ViewInject(R.id.ed_transfer_fee)
    private EditText ed_transfer_fee;
    /**
     * 备注
     */
    @ViewInject(R.id.ed_offline_remarks)
    private EditText ed_offline_remarks;
    /**
     * 买家身份证
     */
    @ViewInject(R.id.rl_buyer_id_card)
    private RelativeLayout rl_buyer_id_card;
    /**
     * 买家身份证
     */
    @ViewInject(R.id.rl_buyer_id_card_new)
    private RelativeLayout rl_buyer_id_card_new;
    /**
     * 买家身份证
     */
    @ViewInject(R.id.iv_buyer_id_card_new)
    private ImageView iv_buyer_id_card_new;
    /**
     * 合同
     */
    @ViewInject(R.id.iv_contract_new)
    private ImageView iv_contract_new;
    /**
     * 合同
     */
    @ViewInject(R.id.rl_contract)
    private RelativeLayout rl_contract;
    /**
     * 合同
     */
    @ViewInject(R.id.rl_contract_new)
    private RelativeLayout rl_contract_new;
    /**
     * 三人合影
     */
    @ViewInject(R.id.rl_three_people)
    private RelativeLayout rl_three_people;
    /**
     * 三人合影
     */
    @ViewInject(R.id.rl_three_people_new)
    private RelativeLayout rl_three_people_new;
    /**
     * 三人合影
     */
    @ViewInject(R.id.iv_three_people_new)
    private ImageView iv_three_people_new;
    /**
     * 售出渠道
     */
    @ViewInject(R.id.sp_sale_channel)
    private Spinner sp_sale_channel;

    private SaleChannelAdapter adapter;
    /**
     * 库存id
     */
    private int stockId;
    /**
     * 当前选中的照片
     */
    private PhotoEntity curPhotoEntity;
    private List<PhotoEntity> photoList;
    private QiNiuUploadUtil qiniuUploadUtil;
    //线下售出实体类
    private OfflineSoldEntity offlineEntity;
    private List<SaleChannelEntity> channelList = new LinkedList<>();
//    private List<String> channeNameList=new ArrayList<>();

    private SaleChannelEntity saleChannelEntity;

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_offlinesold_commit, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        stockId = getIntent().getIntExtra("stockId", 0);
        initPhotoList();
        x.view().inject(this);
        ed_offline_buyer_name.requestFocus();
        //支付方式变化
        rg_which_pay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_company) {
                    ll_transfer_fee.setVisibility(View.VISIBLE);
                } else {
                    ll_transfer_fee.setVisibility(View.GONE);
                    ed_transfer_fee.setText("");
                }
            }
        });
        OKHttpManager.getInstance().post(HCHttpRequestParam.getSaleChannelList(), this, 0);

    }

    /**
     * 初始化照片列表
     */
    private void initPhotoList() {
        photoList = new ArrayList<>();
        PhotoEntity photoEntity;
        for (int i = 0; i < PHOTO_TAGS.length; i++) {
            photoEntity = new PhotoEntity();
            photoEntity.setName(getString(PHOTO_TAGS[i]));
            photoList.add(photoEntity);
        }
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.hc_offlinesold_commit));
    }

    /**
     * 显示校验失败消息
     *
     * @param failedView
     * @param message
     */
    private void onValidateFailed(EditText failedView, String message) {
        failedView.requestFocus();
        failedView.setError(message);
    }

    /**
     * 提交售出按钮点击事件
     */
    @Event(R.id.btn_commit)
    private void commitOfflineSold(View v) {
        //检查必输项
        validator.validate();
    }

    /**
     * 买家身份证点击事件
     *
     * @param view
     */
    @Event(R.id.fl_buyer_id_card)
    private void clickBuyerIdCard(View view) {
        curPhotoEntity = photoList.get(0);
        openPhotoPicker(TaskConstants.REQUEST_BUYER_ID_CARD);
    }

    /**
     * 合同点击事件
     *
     * @param view
     */
    @Event(R.id.fl_contract)
    private void clickContract(View view) {
        curPhotoEntity = photoList.get(1);
        openPhotoPicker(TaskConstants.REQUEST_CONTRACT);
    }

    /**
     * 三人合影点击事件
     *
     * @param view
     */
    @Event(R.id.fl_three_people)
    private void clickThreePeople(View view) {
        curPhotoEntity = photoList.get(2);
        openPhotoPicker(TaskConstants.REQUEST_THREE_PEOPLE);
    }

    /**
     * 打开照片选择器
     *
     * @param requestCode 请求码
     */
    private void openPhotoPicker(int requestCode) {
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        //设置每次只可以选择1张图片
        intent.setPhotoCount(1);
        intent.setShowCamera(false);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 重置买家身份证
     */
    private void setBuyerIDCard() {
        ImageLoaderHelper.displayImage("file://" + photoList.get(0).getPath(), iv_buyer_id_card_new);
        rl_buyer_id_card.setVisibility(View.GONE);
        rl_buyer_id_card_new.setVisibility(View.VISIBLE);
    }

    /**
     * 重置合同
     */
    private void setContract() {
        ImageLoaderHelper.displayImage("file://" + photoList.get(1).getPath(), iv_contract_new);
        rl_contract.setVisibility(View.GONE);
        rl_contract_new.setVisibility(View.VISIBLE);
    }

    /**
     * 重置三人合影
     */
    private void setThreePeople() {
        ImageLoaderHelper.displayImage("file://" + photoList.get(2).getPath(), iv_three_people_new);
        rl_three_people.setVisibility(View.GONE);
        rl_three_people_new.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case TaskConstants.REQUEST_BUYER_ID_CARD:
                if (checkResult(data))
                    setBuyerIDCard();
                break;
            case TaskConstants.REQUEST_CONTRACT:
                if (checkResult(data))
                    setContract();
                break;
            case TaskConstants.REQUEST_THREE_PEOPLE:
                if (checkResult(data))
                    setThreePeople();
                break;
            case PAY_REQUEST_CODE:
                if (offlineEntity != null) {
                    ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
                    OKHttpManager.getInstance().post(HCHttpRequestParam.commitOfflineSold(offlineEntity), this, 0);
                }
                break;
        }
    }

    private boolean checkResult(Intent data) {
        if (data == null || curPhotoEntity == null) {
            return false;
        }
        ArrayList<String> photoPaths = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
        if (photoPaths == null || photoPaths.size() == 0) {
            return false;
        }
        curPhotoEntity.setPath(photoPaths.get(0));
        return true;
    }

    /**
     * 校验成功
     */
    @Override
    public void onValidationSucceeded() {
        if (!TextUtils.isEmpty(ed_offline_buyer_phone.getText())) {
            String phoneNumber = ed_offline_buyer_phone.getText().toString().trim();

//			if (!PhoneNumberUtil.isPhoneNumberValid(phoneNumber)) {
//				onValidateFailed(ed_offline_buyer_phone, "买家电话不正确");
//				return;
//			}
            if (phoneNumber.length() != 11) {
                onValidateFailed(ed_offline_buyer_phone, "买家电话不正确");
                return;
            }
        }

        if (rg_which_pay.getCheckedRadioButtonId() == R.id.rb_company && TextUtils.isEmpty(ed_transfer_fee.getText())) {
            onValidateFailed(ed_transfer_fee, "过户费用不能为空");
            return;
        }

        if (new BigDecimal(ed_offline_sold_price.getText().toString()).floatValue() <= 0) {
            onValidateFailed(ed_offline_sold_price, "售出价格必须大于0");
            return;
        }

        if (new BigDecimal(ed_offline_sold_price.getText().toString()).floatValue() > 500) {
            onValidateFailed(ed_offline_sold_price, "售出价格必须小于500万元");
            return;
        }

        if (saleChannelEntity == null || saleChannelEntity.getKey() == 0) {
//			onValidateFailed(sp_sale_channel, "售出渠道不能为空！");
            ToastUtil.showInfo("售出渠道不能为空！");
            return;
        }

        //上传图片到七牛服务器
        uploadImageToQiniu();
    }

    /**
     * 上传图片到七牛
     */
    private void uploadImageToQiniu() {

        qiniuUploadUtil = new QiNiuUploadUtil(this, this.photoList, null);
        qiniuUploadUtil.startUpload(new QiNiuUploadUtil.QiniuUploadListener() {
            @Override
            public void onSuccess(List<String> photoUrlList, List<PhotoEntity> photoList) {
                //提交线下售出信息
                commitOfflineSold(photoList);
            }
        });
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        try {
            if (!isFinishing())
                ProgressDialogUtil.closeProgressDialog();

            if (response == null) {
                ToastUtil.showInfo("响应结果为空！");
                return;
            }
            //提交线下售出
            if (HttpConstants.ACTION_COMMIT_OFFLINE_SOLD.equals(action)) {
                switch (response.getErrno()) {
                    case 0:
                        ToastUtil.showInfo("提交线下售出信息成功");
                        setResult(RESULT_OK);
                        finish();
                        break;
                    default:
                        ToastUtil.showInfo("提交线下售出信息失败：" + response.getErrmsg());
                        break;
                }
            } else if (HttpConstants.ACTION_GET_SOLD_CHANNEL.equals(action)) {
                switch (response.getErrno()) {
                    case 0:
                        channelList = JsonParseUtil.fromJsonArray(response.getData(), SaleChannelEntity.class);
                        initChannelSpinner();
                        break;
                    default:
                        ToastUtil.showInfo("获取售出渠道失败：" + response.getErrmsg());
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 初始化售出渠道列表
     */
    private void initChannelSpinner() {
        if(channelList!=null && channelList.size()>0){
            channelList.add(0, new SaleChannelEntity(0, "请选择"));
        }
        adapter = new SaleChannelAdapter(this, channelList, R.layout.item_spinner);
        sp_sale_channel.setAdapter(adapter);
        //添加Spinner事件监听
        sp_sale_channel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(channelList!=null && channelList.size()>0){
                    saleChannelEntity = channelList.get(position);
                    HCLogUtil.e(this.getClass().getName(), "saleChannelEntity========================" + saleChannelEntity.getKey());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * 提交线下售出信息
     *
     * @param photoEntities
     */
    private void commitOfflineSold(List<PhotoEntity> photoEntities) {
        offlineEntity = new OfflineSoldEntity();
        offlineEntity.setStock_id(stockId);
        offlineEntity.setTrans_type(getIntent().getIntExtra("trans_type", 0));
        offlineEntity.setTask_num(getIntent().getStringExtra("task_num"));
        offlineEntity.setBuyer_name(ed_offline_buyer_name.getText().toString().trim());
        if (!TextUtils.isEmpty(ed_offline_buyer_phone.getText())) {
            offlineEntity.setBuyer_phone(ed_offline_buyer_phone.getText().toString().trim());
        }
        BigDecimal unitWan = new BigDecimal(10000);
        BigDecimal sellPrice = new BigDecimal(ed_offline_sold_price.getText().toString()).multiply(unitWan);
        offlineEntity.setSold_price(sellPrice.intValue());
        //需要过户
        offlineEntity.setIs_transfer(TaskConstants.YES);

        //哪方付费（0：没选择、1：公司、2：车主)
        if (rg_which_pay.getCheckedRadioButtonId() == R.id.rb_company) {
            offlineEntity.setTransfer_free_payer(1);
            //过户费用
            BigDecimal transferFee = new BigDecimal(ed_transfer_fee.getText().toString());
            offlineEntity.setTransfer_free(transferFee.intValue());
        } else if (rg_which_pay.getCheckedRadioButtonId() == R.id.rb_owner) {
            offlineEntity.setTransfer_free_payer(2);
            offlineEntity.setTransfer_free(0);
        }
        offlineEntity.setSold_remark(ed_offline_remarks.getText().toString());

        offlineEntity.setPhotoList(photoEntities);
        offlineEntity.setSale_channel(saleChannelEntity.getKey());
        //如果需要上传的图片都已上传
        if (checkAllUrl()) {
            AlertDialogUtil.showConfirmPhoneMoneyDialog(this, offlineEntity.getBuyer_phone(), offlineEntity.getSold_price(), offlineEntity.getTransfer_free(),
                    getString(com.haoche51.settlement.R.string.again_input), getString(com.haoche51.settlement.R.string.confirm_no_error), new AlertDialogUtil.OnDismissListener() {
                        @Override
                        public void onDismiss(Bundle data) {
                            //调用结算
                            SettlementIntent settlementIntent = new SettlementIntent(OfflineSoldCommitActivity.this);
                            settlementIntent.setAppToken(GlobalData.userDataHelper.getChecker().getApp_token());//必传，网络请求的appToken
                            settlementIntent.setCrmUserId(GlobalData.userDataHelper.getChecker().getId() + "");// 必传，crm_user_id
                            settlementIntent.setCrmUserName(GlobalData.userDataHelper.getChecker().getName());//必传，crm_user_name
                            settlementIntent.setCustomerName(offlineEntity.getBuyer_name());//必传 客户电话号码
                            settlementIntent.setCustomerPhone(offlineEntity.getBuyer_phone());//必传 客户电话号码
                            settlementIntent.setPrice(offlineEntity.getSold_price() + "");// 应收金额，单位元
                            settlementIntent.setTaskId(offlineEntity.getTask_num());// 非必传 业务订单号，没有不传
                            settlementIntent.setTaskType(offlineEntity.getTrans_type());// 任务类型，1c2c交易 2回购 3金融
                            settlementIntent.setFromBusiness(true);//是否从业务方调用
                            settlementIntent.setCashEnable(true);//是否可以使用现金收款，不传默认false，不使用现金
                            startActivityForResult(settlementIntent, PAY_REQUEST_CODE);
                        }
                    });
        }
    }

    /**
     * 检查是否都已上传完毕
     *
     * @return
     */
    private boolean checkAllUrl() {
        PhotoEntity temPhoto;
        for (int i = 0; i < photoList.size(); i++) {
            temPhoto = photoList.get(i);
            if (!TextUtils.isEmpty(temPhoto.getPath()) && TextUtils.isEmpty(temPhoto.getUrl())) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.closeProgressDialog();
        if (qiniuUploadUtil != null) {
            qiniuUploadUtil.stopUpload();
            qiniuUploadUtil = null;
        }
        super.onDestroy();
    }
}
