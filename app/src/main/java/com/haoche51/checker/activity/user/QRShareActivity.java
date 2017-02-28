package com.haoche51.checker.activity.user;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.haoche51.checker.Checker;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonBaseActivity;
import com.haoche51.checker.helper.ImageLoaderHelper;
import com.haoche51.checker.item.ShareStat;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.BitmapUtil;
import com.haoche51.checker.util.CodeCreatorUtil;
import com.haoche51.checker.util.SharedPreferencesUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * Created by yangming on 2015/11/25.
 */
public class QRShareActivity extends CommonBaseActivity {

  /**
   * 二维码
   */
  @ViewInject(R.id.iv_qrcode_app_download)
  private ImageView iv_qrcode_app_download;
  @ViewInject(R.id.iv_qrcode_introduction)
  private ImageView iv_qrcode_introduction;
  private String myQRCodePath = Environment.getExternalStorageDirectory() + "/myqrcode.png";


  @Override
  protected void initView() {
    super.initView();
    setContentView(R.layout.activity_qrcode_share);
    registerTitleBack();
    setScreenTitle(R.string.hc_qrcode_share_title);
    x.view().inject(this);
  }

  @Override
  protected void initData() {
    super.initData();
    //    setUpUserShareStat();
    showQRCode();
  }


  private void setUpUserShareStat() {

    // TODO:设置分享次数和排名信息,和排名前三的评估师姓名

    /**
     * 获取个人排名
     *
     */
    int checker_id = GlobalData.userDataHelper.getChecker().getId();
    OKHttpManager.getInstance().post(HCHttpRequestParam.getSelfShareStat(checker_id), this, 0);
    /**
     * 获取前三名
     */
    OKHttpManager.getInstance().post(HCHttpRequestParam.getShareStat(), this, 0);
  }

  private void showQRCode() {
    if (isCanUse()) {
      ImageLoaderHelper.displayImage("file://" + myQRCodePath, iv_qrcode_app_download);
      ImageLoaderHelper.displayImage("file://" + myQRCodePath, iv_qrcode_introduction);
    } else {
      Checker checker = GlobalData.userDataHelper.getChecker();
      String url = GlobalData.resourceHelper.getString(R.string.myqrcode) + checker.getId();
      try {
        Bitmap bitmap = CodeCreatorUtil.createQRCode(url);
        iv_qrcode_app_download.setImageBitmap(bitmap);
        iv_qrcode_introduction.setImageBitmap(bitmap);
        BitmapUtil.saveBitmapToFile(bitmap, myQRCodePath);
        SharedPreferencesUtils.saveBoolean("hc_share_qrcode", true);
      } catch (WriterException e) {
        iv_qrcode_app_download.setVisibility(View.GONE);
        iv_qrcode_introduction.setVisibility(View.GONE);
        e.printStackTrace();
      }
    }
  }

  private boolean isCanUse() {
    return SharedPreferencesUtils.getBoolean("hc_share_qrcode", false) && new File(myQRCodePath).exists();
  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    super.onHttpComplete(action, requestId, response, error);
    if (action.equals(HttpConstants.ACTION_GET_SELF_SHARE_STAT)) {// 获取个人分享数据
      onSelfShareStateResult(response);
    }
//    if (action.equals(HttpConstants.ACTION_GET_SHARE_STAT)) {//获取分享前三
////      onShareTopResult(response);
//    }
  }

  /**
   * 获取分享前三
   */
//  private void onShareTopResult(HCHttpResponse response) {
//    switch (response.getErrno()) {
//      case 0:
//        List<ShareStat> statList = HCJsonParse.getInstance().parseShareStat(response.getData());
//
//        if (statList != null) { //
//          int size = statList.size();
//          if (size > 0) {
//            mRank1Name.setText(statList.get(0).getCheck_user_name());
//          }
//          if (size > 1) {
//            mRank2Name.setText(statList.get(1).getCheck_user_name());
//          }
//          if (size > 2) {
//            mRank3Name.setText(statList.get(1).getCheck_user_name());
//          }
//        }
//        break;
//      default:
//        Toast.makeText(getApplicationContext(), response.getErrmsg(), Toast.LENGTH_SHORT).show();
//        break;
//    }
//  }


  /**
   * 获取个人分享数据
   */
  private void onSelfShareStateResult(HCHttpResponse response) {
    switch (response.getErrno()) {
      case 0:
//        ShareStat shareStat = new HCJsonParse().parseSelfShareStat(response.getData());
        ShareStat shareStat = JsonParseUtil.fromJsonObject(response.getData(), ShareStat.class);
//        if (shareStat != null) {
////          mShareRankInfoTv.setText("本月分享：" + shareStat.getShare_num() + "当前排名:" + shareStat.getRank());
//        }
        break;
      default:
        Toast.makeText(getApplicationContext(), response.getErrmsg(), Toast.LENGTH_SHORT).show();
        break;
    }
  }

}
