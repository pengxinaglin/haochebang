package com.haoche51.checker.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.haoche51.checker.Checker;
import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.BaseActivity;
import com.haoche51.checker.activity.HomePageActivity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.DeviceInfoUtil;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.concurrent.Executors;

public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.login)
    private Button loginButton;

    @ViewInject(R.id.user_name)
    @Required(order = 1, message = "用户名不能为空")
    private EditText userEditText;

    @ViewInject(R.id.password)
    @Required(order = 2, message = "密码不能为空")
    private EditText pwdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView() {
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        userEditText.setText(GlobalData.userDataHelper.getLoginName());
        pwdEditText.setText(GlobalData.userDataHelper.getLoginPwd());
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        pwdEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null) && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    validator.validate();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        super.onValidationSucceeded();
        String username = userEditText.getText().toString();
        String password = pwdEditText.getText().toString();
        GlobalData.userDataHelper.setLoginName(username)
                .setLoginPwd(password)
                .commit();
        ProgressDialogUtil.showProgressDialog(LoginActivity.this, getString(R.string.loging));
        OKHttpManager.getInstance().post(HCHttpRequestParam.loginForEncode(username, password
                , DeviceInfoUtil.getDeviceUniqueId(), DeviceInfoUtil.getPhoneType(), DeviceInfoUtil.getOSVersion()
                , DeviceInfoUtil.getAppVersion()), LoginActivity.this, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("LoginActivity");
        MobclickAgent.onResume(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("LoginActivity");
        MobclickAgent.onPause(getApplicationContext());
    }

    /**
     * 不同用户登录时，清除之前用户填写的报告
     */
    private void clearLocalReport() {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                CheckReportDAO.getInstance().clear();
            }
        });
    }

    /**
     * 网络请求结束,请求服务器成功，请求服务器失败
     *
     * @param action   当前请求action
     * @param response hc 请求结果
     * @param error    网络问题造成failed 的error
     */
    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        ProgressDialogUtil.closeProgressDialog();
        if (HttpConstants.ACTION_LOGIN_ENCODE.equals(action)) {
            doGetLoginRequest(response);
        }
    }

    private void doGetLoginRequest(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
                int lastCheckerId = GlobalData.userDataHelper.getLastCheckerId();

                Checker checker = JsonParseUtil.fromJsonObject(response.getData(), Checker.class);

                if (checker == null) {
                    GlobalData.userDataHelper.clearLogin().clearChecker().commit();
                    Toast.makeText(LoginActivity.this, getString(R.string.server_no_response), Toast.LENGTH_SHORT).show();
                    return;
                }
                GlobalData.userDataHelper.setChecker(checker).setLogin().setLastCheckerId(checker.getId()).commit();
                //如果此次登陆用户和上次登录用户不同，
                if (lastCheckerId != checker.getId()) {
                    clearLocalReport();
                }
                loadHomeUI();
                break;
            case -7:
                showErrorMsg(userEditText, response.getErrmsg());
                GlobalData.userDataHelper.clearLogin().clearChecker().commit();
                break;
            default:
                ToastUtil.showInfo(response.getErrmsg());
                GlobalData.userDataHelper.clearLogin().clearChecker().commit();
                break;
        }
    }

    /**
     * 加载主页
     */
    private void loadHomeUI() {
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }


}
