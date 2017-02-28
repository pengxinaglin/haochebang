package com.haoche51.checker.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

public class ThumbnailImageView extends ImageView implements Checkable {
	
	private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };
	private boolean mChecked = false;
	public ThumbnailImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ThumbnailImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ThumbnailImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setChecked(boolean checked) {
		mChecked = checked;
		refreshDrawableState(); // 根据状态刷新显示
	}

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void toggle() {
		setChecked(!mChecked);
	}

	@Override
	public int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace+1);
		if (isChecked()) {
			mergeDrawableStates(drawableState,CHECKED_STATE_SET);
		}
		return drawableState;
	}

}
