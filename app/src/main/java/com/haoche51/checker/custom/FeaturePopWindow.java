package com.haoche51.checker.custom;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.haoche51.checker.R;
import com.haoche51.checker.constants.EquipmentConstants;

public class FeaturePopWindow extends PopupWindow {
    private RadioGroup statusGroup;
    private EditText mDesc;
    private Button cancel_btn;
    private Button confirmBtn;
    private OnFeatureChangeListener mListener;
    private Context mContext;
    private TextView mTitle;

    public FeaturePopWindow(Context context, int status, String desc, String title) {
        initView(context, status, desc, title);
        mContext = context;
        setWindowLayoutMode(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0));
        setFocusable(true);
        setOutsideTouchable(true);
    }


    protected void initView(Context context, int status, String desc, String title) {
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_check_layout, null);
        setContentView(popView);
        statusGroup = (RadioGroup) popView.findViewById(R.id.feature_status);
        mDesc = (EditText) popView.findViewById(R.id.exception_desc);
        mTitle = (TextView) popView.findViewById(R.id.feature_title);
        cancel_btn = (Button) popView.findViewById(R.id.cancel_btn);
        confirmBtn = (Button) popView.findViewById(R.id.confirm_btn);
        mTitle.setText(title);
        setUpStatus(status);
        setUpDescription(desc);
        setUpClick();
    }

    protected void setUpClick() {
        confirmBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int status;
                String desc;
                if (statusGroup.getCheckedRadioButtonId() == R.id.feature_normal) {
                    status = EquipmentConstants.EQUIPMENT_NORMAL;
                    mDesc.setHint("");
                    mDesc.setText("");
                } else {
                    status = EquipmentConstants.EQUIPMENT_ABNORMAL;
                    mDesc.setHint("请描述一下异常原因");
                    if (TextUtils.isEmpty(mDesc.getText().toString())) {
                        Toast.makeText(mContext, "请填写异常说明", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                desc = mDesc.getText().toString();

                if (mListener != null) {
                    mListener.onFeatureChange(status, desc);
                }
                dismiss();
            }

        });
        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    protected void setUpStatus(int status) {
        switch (status) {
            case EquipmentConstants.EQUIPMENT_NORMAL:
                statusGroup.check(R.id.feature_normal);
                break;
            case EquipmentConstants.EQUIPMENT_ABNORMAL:
                statusGroup.check(R.id.feature_exception);
                break;
        }
    }

    protected void setUpDescription(String desc) {
        if (desc != null) {
            mDesc.setText(desc);
        }
    }

    public void show(View parent) {
        showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public void setOnFeatureChangeListener(OnFeatureChangeListener l) {
        mListener = l;
    }

    public interface OnFeatureChangeListener {
        void onFeatureChange(int status, String desc);
    }
}
