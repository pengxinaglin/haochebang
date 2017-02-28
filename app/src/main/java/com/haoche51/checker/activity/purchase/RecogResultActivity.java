/**
 * Project Name:IDCardScanCaller
 * File Name:RecogActivity.java
 * Package Name:com.intsig.idcardscancaller
 * Date:2016年3月15日下午3:58:29
 * Copyright (c) 2016, 上海合合信息 All Rights Reserved.
 */

package com.haoche51.checker.activity.purchase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.intsig.ccrengine.CCREngine;
import com.intsig.ccrengine.ISCardScanActivity;
import com.mobsandgeeks.saripaar.annotation.Required;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class RecogResultActivity extends CommonTitleBaseActivity implements View.OnClickListener {
    /**
     * 银行卡第1个栏位
     */
    @ViewInject(R.id.ed_bank_01)
    @Required(order = 1, message = "银行卡第1个栏位不能为空")
    private EditText ed_bank_01;
    /**
     * 银行卡第2个栏位
     */
    @ViewInject(R.id.ed_bank_02)
    @Required(order = 2, message = "银行卡第2个栏位不能为空")
    private EditText ed_bank_02;

    /**
     * 银行卡第3个栏位
     */
    @ViewInject(R.id.ed_bank_03)
    @Required(order = 3, message = "银行卡第3个栏位不能为空")
    private EditText ed_bank_03;

    /**
     * 银行卡第4个栏位
     */
    @ViewInject(R.id.ed_bank_04)
    @Required(order = 4, message = "银行卡第4个栏位不能为空")
    private EditText ed_bank_04;

    /**
     * 银行卡第5个栏位
     */
    @ViewInject(R.id.ed_bank_05)
    private EditText ed_bank_05;

    @ViewInject(R.id.btn_positive)
    private Button btn_positive;
    private String after_str = "";

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_recog_result, null);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        try {
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        x.view().inject(this);
        btn_positive.setText(getString(R.string.confirm_no_error));
        btn_positive.setOnClickListener(this);
        Intent intent = getIntent();
        CCREngine.ResultData result = (CCREngine.ResultData) intent.getSerializableExtra(ISCardScanActivity.EXTRA_KEY_RESULT);
        // 卡号
        String number = result.getCardNumber();
        if (!TextUtils.isEmpty(number)) {
            after_str = number.replace(" ", "");
            initBankEdit(ed_bank_01);
            initBankEdit(ed_bank_02);
            initBankEdit(ed_bank_03);
            initBankEdit(ed_bank_04);
            initBankEdit(ed_bank_05);
        }

        //获取银行卡卡号截图
        Bitmap bmp = (Bitmap) intent.getParcelableExtra(ISCardScanActivity.EXTRA_KEY_GET_NUMBER_IMG);
        ((ImageView) findViewById(R.id.imageView1)).setImageBitmap(bmp);

        //获取银行卡图片，启动相机模块的时候传入的路径 ISCardScanActivity.EXTRA_KEY_GET_TRIMED_IMG
        Bitmap bigBmp = BitmapFactory.decodeFile("/sdcard/trimedcard.jpg");
        ((ImageView) findViewById(R.id.img_card)).setImageBitmap(bigBmp);
    }


    /**
     * 初始化银行卡号（四位一份）
     *
     * @param ed_bank
     */
    private void initBankEdit(EditText ed_bank) {
        if (TextUtils.isEmpty(after_str) || ed_bank == null) {
            return;
        }
        String temp;
        int endNum = 4;
        if (after_str.length() >= endNum) {
            temp = after_str.substring(0, endNum);
            after_str = after_str.substring(endNum);
        } else {
            temp = after_str.substring(0);
        }
        ed_bank.setText(temp);
        ed_bank.setSelection(temp.length());
    }

    /**
     * 获取银行卡号
     *
     * @return
     */
    private String getBankCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(ed_bank_01.getText());
        sb.append(" ");
        sb.append(ed_bank_02.getText());
        sb.append(" ");
        sb.append(ed_bank_03.getText());
        sb.append(" ");
        sb.append(ed_bank_04.getText());
        sb.append(" ");
        sb.append(ed_bank_05.getText());
        return sb.toString();
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.confirm_bank_code));
    }

    @Override
    public void onValidationSucceeded() {
        super.onValidationSucceeded();
        Intent result = new Intent();
        result.putExtra(TaskConstants.BINDLE_BANK_CODE, getBankCode());
        setResult(RESULT_OK, result);
        finish();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_positive) {
            validator.validate();
        }
    }

}

