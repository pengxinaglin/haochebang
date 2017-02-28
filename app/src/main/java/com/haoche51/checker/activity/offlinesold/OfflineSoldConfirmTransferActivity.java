package com.haoche51.checker.activity.offlinesold;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.OfflineSoldConfirmAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.custom.CGridView;
import com.haoche51.checker.entity.OfflineSoldEntity;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.HCDialogUtil;
import com.haoche51.checker.util.QiNiuUploadUtil;
import com.haoche51.checker.util.ToastUtil;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * 线下售出——确定转账页面
 */
public class OfflineSoldConfirmTransferActivity extends CommonTitleBaseActivity {
	private List<String> imagePathList = new ArrayList<>();
	/**
	 * 转账备注
	 */
	@ViewInject(R.id.ed_transfer_remark)
	@Required(order = 1, message = "转账备注不能为空")
	private EditText ed_transfer_remark;


	/**
	 * 默认图片
	 */
	@ViewInject(R.id.ll_photo_default)
	private LinearLayout ll_photo_default;

	/**
	 * 转账照片
	 */
	@ViewInject(R.id.cgv_photo)
	private CGridView mGridView;

	/**
	 * 库存id
	 */
	private int stockId;

	private OfflineSoldConfirmAdapter adapter;
	private QiNiuUploadUtil qiniuUploadUtil;

	@Override
	public View getHCContentView() {
		return View.inflate(this, R.layout.activity_offlinesold_confirm_transfer, null);
	}

	@Override
	public void initContentView(Bundle saveInstanceState) {
		stockId = getIntent().getIntExtra("stockId", 0);
		x.view().inject(this);
		ed_transfer_remark.requestFocus();
		adapter = new OfflineSoldConfirmAdapter(this, imagePathList, R.layout.item_offlinesold_confirm_photo);
		mGridView.setAdapter(adapter);
	}

	@Override
	public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
		mTitle.setText(getString(R.string.hc_offlinesold_transfer_confirm));
	}

	/**
	 * 确认转账
	 */
	@Event(R.id.btn_commit)
	private void commit(View v) {
		validator.validate();
	}

	@Event(R.id.ll_photo_add)
	private void addPhoto(View view) {
		openPhotoPicker();
	}

	/**
	 * 打开选择相片界面
	 */
	private void openPhotoPicker() {
		PhotoPickerIntent intent = new PhotoPickerIntent(this);
		intent.setPhotoCount(10);//一次最多只能选10张图
		intent.setShowCamera(false);
		startActivityForResult(intent, TaskConstants.REQUEST_SELECT_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null || resultCode != RESULT_OK || requestCode != TaskConstants.REQUEST_SELECT_PHOTO) {
			return;
		}
//    ll_photo_default.setVisibility(View.GONE);
		mGridView.setVisibility(View.VISIBLE);
		ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
		if (list != null) {
			imagePathList.addAll(list);
			adapter.setmList(imagePathList);
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 校验成功
	 */
	@Override
	public void onValidationSucceeded() {
		super.onValidationSucceeded();
		//上传到七牛服务器
		uploadImageToQiniu();
	}

	/**
	 * 上传图片到七牛
	 */
	private void uploadImageToQiniu() {
		//没有选择图片，显示提示
		if (this.imagePathList == null || this.imagePathList.size() == 0) {
			ll_photo_default.setVisibility(View.VISIBLE);
			mGridView.setVisibility(View.GONE);
			ToastUtil.showInfo("请至少上传一张转账图片");
			return;
		}

		qiniuUploadUtil = new QiNiuUploadUtil(this, null, this.imagePathList);
		qiniuUploadUtil.startUpload(new QiNiuUploadUtil.QiniuUploadListener() {
			@Override
			public void onSuccess(List<String> photoUrlList, List<PhotoEntity> photoList) {
				//上传完成，提交确认信息
				commitOfflineSold(photoUrlList);
			}
		});
	}

	/**
	 * 提交确认转账信息
	 */
	private void commitOfflineSold(List<String> photoPathList) {
		//显示对话框
		HCDialogUtil.showProgressDialog(this);
		OfflineSoldEntity offlineSold = new OfflineSoldEntity();
		offlineSold.setStock_id(stockId);
		offlineSold.setTrans_way(ed_transfer_remark.getText().toString().trim());
		offlineSold.setPhotoPathList(photoPathList);
		OKHttpManager.getInstance().post(HCHttpRequestParam.confirmOfflineSold(offlineSold), this, 0);
	}

	@Override
	public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
		HCDialogUtil.dismissProgressDialog();
		if (response == null) {
			ToastUtil.showInfo("响应结果为空！");
			return;
		}

		//提交线下售出
		if (HttpConstants.ACTION_CONFIRM_OFFLINE_SOLD.equals(action)) {
			switch (response.getErrno()) {
				case 0:
					ToastUtil.showInfo("确认成功");
					setResult(RESULT_OK);
					finish();
					break;
				default:
					ToastUtil.showInfo("确认失败：" + response.getErrmsg());
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
