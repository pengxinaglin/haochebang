package com.haoche51.checker.custom;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.haoche51.checker.R;

public class ItemPopWindow extends PopupWindow implements OnCheckedChangeListener {
    protected RadioGroup mpopGroup = null;
    protected OnStatusChangeListener mListener;
    protected int position = 0;

    public ItemPopWindow(Context context, int status) {

        View popView = LayoutInflater.from(context).inflate(getRes(), null);
        initView(popView, status);
        setContentView(popView);
        setWindowLayoutMode(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0));
        setAnimationStyle(R.style.PopMenuAnimation);
        setFocusable(true);
        setOutsideTouchable(true);
        if (mpopGroup != null) {
            mpopGroup.setOnCheckedChangeListener(this);
        }
    }


    /**
     * 响应事件
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
    }

    /**
     * 设置弹出窗口
     *
     * @param popView
     */
    protected void initView(View popView, int status) {

    }

    /**
     * 设置当前resource
     *
     * @return
     */
    protected int getRes() {
        return 0;
    }

    /**
     * 弹出
     *
     * @param parent
     * @param position
     */
    public void show(View parent, int position) {
        this.position = position;
        int[] arrayOfInt = new int[2];
        parent.getLocationOnScreen(arrayOfInt);
        int x = arrayOfInt[0];
        int y = arrayOfInt[1];
        showAtLocation(parent, 0, x, y);
    }

    public void setOnStatusChangeListener(OnStatusChangeListener listener) {
        mListener = listener;
    }

    /**
     * 弹出回调接口
     *
     * @author xuhaibo
     */
    public interface OnStatusChangeListener {
        void statusChange(int status, int position);
    }


}
