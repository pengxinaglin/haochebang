package com.haoche51.checker.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.haoche51.checker.R;
import com.haoche51.checker.util.DisplayUtils;

/**
 * 1、清除按钮在输入框中有内容时出现
 * 2、清除按钮必须出现在输入框内
 * 3、点击清除按钮，清除输入框中的所有内容
 * 4、用户只能输入数字，每输入4个字符，用空格自动隔开
 * 5、用户点击键盘的删除键时，自动删除掉上边的每个字符。空格跳过，自动消除
 * Created by wfx on 2016/10/27.
 */

public class ClearableEditText extends EditText implements View.OnFocusChangeListener, TextWatcher, View.OnKeyListener {
    private Drawable mClearTextIcon;

    private OnClearBtnChangeListener mOnClearBtnChangeListener;

    public ClearableEditText(Context context) {
        super(context);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!TextUtils.isEmpty(getText())) {
                String str = getText().toString().replace(" ", "");
                if (str.length() >= 1) {
                    setText(str.substring(0, str.length() - 1));
                } else {
                    setText("");
                }

            }
            return true;
        }
        return false;
    }

    public void setOnClearBtnChangeListener(OnClearBtnChangeListener onClearBtnChangeListener) {
        this.mOnClearBtnChangeListener = onClearBtnChangeListener;
    }

    /**
     * 创建drawable，并为其加入Touch、Focus事件处理
     * 加入TextChangedListener，监听EditText内容变化
     *
     * @param context
     */
    private void init(final Context context) {
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_clear);
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable); //Wrap the drawable so that it can be tinted pre Lollipop
        DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        mClearTextIcon = wrappedDrawable;
        mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicHeight(), mClearTextIcon.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        setOnKeyListener(this);
        addTextChangedListener(this);
    }

    /**
     * 我们默认使用setClearIconVisible(false)隐藏了清除按钮，在输入文本时才会显示
     *
     * @param visible
     */
    private void setClearIconVisible(final boolean visible) {
        if (mOnClearBtnChangeListener != null) {
            mOnClearBtnChangeListener.onClearBtnChange(visible);
        }
        setPadding(0, 0, visible ? DisplayUtils.dip2px(this.getContext(), 10f) : DisplayUtils.dip2px(this.getContext(), 40f), 0);
        mClearTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible ? mClearTextIcon : null,
                compoundDrawables[3]);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 判断输入框中的字数，大于0则显示清除按钮，否则隐藏。
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public final void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        if (isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    /**
     * 用户输入卡号时，自动每四位用空格进行分隔
     */
    private void resetAccountText() {
        if (!TextUtils.isEmpty(getText())) {
            String content = getText().toString();
            content = content.replace(" ", "");
            StringBuilder sb = new StringBuilder();
            int size = content.length();
            if (size <= 0) {
                return;
            }
            int endNum = 4;
            int count = size / endNum;
            int startIndex;

            for (int i = 0; i <= count; i++) {
                startIndex = i * endNum;
                if (i == count && size % endNum > 0) {
                    sb.append(content.substring(startIndex));
                } else if (i < count) {
                    sb.append(content.substring(startIndex, endNum + startIndex));
                    sb.append(" ");
                }
            }
            removeTextChangedListener(this);
            setText(sb.toString());
            setSelection(sb.toString().length());
            addTextChangedListener(this);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        resetAccountText();
    }

    /**
     * 在获取焦点时，判断输入框中内容是否大于0，有内容则显示清除按钮。
     *
     * @param view
     * @param hasFocus
     */
    @Override
    public void onFocusChange(final View view, final boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (getCompoundDrawables()[2] != null) {
//                //getTotalPaddingRight()图标左边缘至控件右边缘的距离
//                //getWidth() - getTotalPaddingRight()表示从最左边到图标左边缘的位置
//                //getWidth() - getPaddingRight()表示最左边到图标右边缘的位置
//                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
//                        && (event.getX() < ((getWidth() - getPaddingRight())));
//
//                if (touchable) {
//                    this.setText("");
//                }
//            }
//        }
        final int x = (int) event.getX();
        if (mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                setText("");
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

//    /**
//     * 在这里，我们首先检查了清除按钮是否为显示状态，然后判断点击的范围是否在清除按钮内，
//     如果在范围内的话，在ACTION_UP时清空输入框内容，否则执行mOnTouchListener的
//     onTouch方法。
//     * @param view
//     * @param motionEvent
//     * @return
//     */
//    @Override
//    public boolean onTouch(final View view, final MotionEvent motionEvent) {
//        final int x = (int) motionEvent.getX();
//        if (mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()) {
//            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                setText("");
//            }
//            return true;
//        }
//        return false;
//    }

    public interface OnClearBtnChangeListener {
        void onClearBtnChange(boolean isClearVisible);
    }
}
