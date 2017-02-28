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
import android.widget.Toast;

import com.haoche51.checker.R;
import com.haoche51.checker.item.TireData;

public class TirePopWindow extends PopupWindow {
    private EditText lfText;
    private EditText lrText;
    private EditText rfText;
    private EditText rrText;
    private Button mConfirmBtn;
    private Button mCancelBtn;
    private Context mContext;
    private OnTireConfirmListener mListener = null;

    public TirePopWindow(Context context, TireData tire) {
        initView(context, tire);
        mContext = context;
        setWindowLayoutMode(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0));
        setFocusable(true);
        setOutsideTouchable(true);
    }

    private void initView(Context context, TireData tire) {
        View popView = LayoutInflater.from(context).inflate(
                R.layout.pop_tire_layout, null);
        setContentView(popView);
        lfText = (EditText) popView.findViewById(R.id.et_lf);
        lrText = (EditText) popView.findViewById(R.id.et_lr);
        rfText = (EditText) popView.findViewById(R.id.et_rf);
        rrText = (EditText) popView.findViewById(R.id.et_rr);
        mConfirmBtn = (Button) popView.findViewById(R.id.tire_confirm);
        mCancelBtn = (Button) popView.findViewById(R.id.tire_cancel);
        if (tire.getLf() != 0) {
            lfText.setText(String.valueOf(tire.getLf()));
        }
        if (tire.getLr() != 0) {
            lrText.setText(String.valueOf(tire.getLr()));
        }
        if (tire.getRf() != 0) {
            rfText.setText(String.valueOf(tire.getRf()));
        }
        if (tire.getRr() != 0) {
            rrText.setText(String.valueOf(tire.getRr()));
        }
        initClick();
    }

    private void initClick() {
        mConfirmBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!vialationCheck()) {
                    Toast.makeText(mContext, "请将数据填写完整！", Toast.LENGTH_SHORT).show();
                    return;
                }
                TireData mTire = new TireData(Float.valueOf(lfText.getText().toString()),
                        Float.valueOf(lrText.getText().toString()),
                        Float.valueOf(rfText.getText().toString()),
                        Float.valueOf(rrText.getText().toString()));
                if (mListener != null) {
                    mListener.tireConfirm(mTire);

                }
                dismiss();
            }

        });

        mCancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private boolean vialationCheck() {
        if (TextUtils.isEmpty(lfText.getText().toString()) || lfText.getText().toString().equals(".")) {
            lfText.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(lrText.getText().toString()) || lrText.getText().toString().equals(".")) {
            lrText.requestFocus();
            return false;

        }
        if (TextUtils.isEmpty(rfText.getText().toString()) || rfText.getText().toString().equals(".")) {
            rfText.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(rrText.getText().toString()) || rrText.getText().toString().equals(".")) {
            rrText.requestFocus();
            return false;
        }
        return true;
    }

    public void show(View parent) {
        showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public void setOntireConfirmListener(OnTireConfirmListener l) {
        mListener = l;
    }

    public interface OnTireConfirmListener {
        void tireConfirm(TireData mTire);
    }


}
