package com.haoche51.checker.pager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.evaluate.ImageUploadActivity;
import com.haoche51.checker.constants.CameraConstants;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.util.ControlDisplayUtil;
import com.haoche51.checker.util.DisplayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择车身位置标签 主页面
 * Created by mac on 15/9/14.
 */
public class SelectCarPhotoTagPopUpWindowPager implements ImageUploadActivity.OnDeleteOrTagChangeListener {

	private Activity mActivity;
	private List<BaseTagPager> mPagers;
	private TabAdapter adapter;
	private ViewPager mTabPager;
	private Button outer, inner, detail;
	private int tabWidth;
	private ImageView cursor;
	private View rootView;

	public SelectCarPhotoTagPopUpWindowPager(Activity mActivity) {
		this.mActivity = mActivity;
		((ImageUploadActivity) this.mActivity).setOnDeleteOrTagChangeListener(this);
		rootView = initView();
		initData();
	}

	public void initData() {
		if (mPagers == null) {
			mPagers = new ArrayList<>();
			mPagers.add(new CarOutTagPager(this.mActivity));
			mPagers.get(0).setOnTagSelectedListener((BaseTagPager.OnTagSelectedListener) this.mActivity);
			mPagers.add(new CarInnerTagPager(this.mActivity));
			mPagers.get(1).setOnTagSelectedListener((BaseTagPager.OnTagSelectedListener) this.mActivity);
			mPagers.add(new CarDetailTagPager(this.mActivity));
			mPagers.get(2).setOnTagSelectedListener((BaseTagPager.OnTagSelectedListener) this.mActivity);

			int check_appointment_id = ((ImageUploadActivity) this.mActivity).getCheckReport().getCheck_appointment_id();
			List<Map<String, Object>> defaultStrings = GlobalData.mSetting.getDefaultStrings(check_appointment_id + "");

			//初始化外观标签
			List<PhotoEntity> outerPictures = ((ImageUploadActivity) this.mActivity).outerPictures;
			for (int i = 0; i < CameraConstants.OUT_INDEX; i++) {
				Map<String, Object> map = new HashMap<>();
				map.put("tagName", defaultStrings.get(i).get("name"));
				//检测此标签是否已占用
				map.put("isSelected", checkTagIsSeleted(outerPictures, (String) defaultStrings.get(i).get("name")) ? true : false);
				map.put("enumeration", defaultStrings.get(i).get("enumeration"));//设置enumeration
				mPagers.get(0).photoTags.add(map);
			}

			//初始化内饰标签
			List<PhotoEntity> innerPictures = ((ImageUploadActivity) this.mActivity).innerPictures;
			for (int i = CameraConstants.OUT_INDEX; i < CameraConstants.INNER_INDEX; i++) {
				Map<String, Object> map = new HashMap<>();
				map.put("tagName", defaultStrings.get(i).get("name"));
				//检测此标签是否已占用
				map.put("isSelected", checkTagIsSeleted(innerPictures, (String) defaultStrings.get(i).get("name")) ? true : false);
				map.put("enumeration", defaultStrings.get(i).get("enumeration"));//设置enumeration
				mPagers.get(1).photoTags.add(map);
			}

			//初始化细节标签
			List<PhotoEntity> detailPictures = ((ImageUploadActivity) this.mActivity).detailPictures;
			for (int i = CameraConstants.INNER_INDEX; i < defaultStrings.size(); i++) {
				Map<String, Object> map = new HashMap<>();
				map.put("tagName", defaultStrings.get(i).get("name"));
				//检测此标签是否已占用
				map.put("isSelected", checkTagIsSeleted(detailPictures, (String) defaultStrings.get(i).get("name")) ? true : false);
				map.put("enumeration", defaultStrings.get(i).get("enumeration"));//设置enumeration
				mPagers.get(2).photoTags.add(map);
			}
		}

		if (this.adapter == null) {
			this.adapter = new TabAdapter();
			this.mTabPager.setAdapter(this.adapter);
		}
	}

	/**
	 * 检测这个标签是否已使用
	 */
	public boolean checkTagIsSeleted(List<PhotoEntity> list, String tagName) {
		for (PhotoEntity e : list) {
			if (!TextUtils.isEmpty(e.getName()) && e.getName().equals(tagName))
				return true;
		}
		return false;
	}

	public View initView() {
		View view = View.inflate(mActivity, R.layout.popupwindow_select_carphototag, null);

		mTabPager = (ViewPager) view.findViewById(R.id.tabpager);
		mTabPager.setOffscreenPageLimit(0);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());

		outer = (Button) view.findViewById(R.id.outer);
		inner = (Button) view.findViewById(R.id.inner);
		detail = (Button) view.findViewById(R.id.detail);

		cursor = (ImageView) view.findViewById(R.id.tab_lines);
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		tabWidth = dm.widthPixels / 3;

		Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(),
			R.drawable.cursor);
		Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, tabWidth,
			DisplayUtils.dip2px(mActivity, 2));// 设置tab的宽度和高度
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(tabWidth,
			DisplayUtils.dip2px(mActivity, 3));
		cursor.setLayoutParams(lp);
		cursor.setImageBitmap(b);

		outer.setOnClickListener(new MyOnClickListener(0));
		inner.setOnClickListener(new MyOnClickListener(1));
		detail.setOnClickListener(new MyOnClickListener(2));

		setAllDefault();
		outer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ControlDisplayUtil.getInstance().getHCTextSize(1));
		return view;
	}

	public void setAllDefault() {
		outer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ControlDisplayUtil.getInstance().getHCTextSize(0));
		inner.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ControlDisplayUtil.getInstance().getHCTextSize(0));
		detail.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ControlDisplayUtil.getInstance().getHCTextSize(0));
	}

	public View getRootView() {
		return rootView;
	}

	@Override
	public void onChange(String tagName) {
		for (BaseTagPager e : mPagers) {
			e.onChange(tagName);
		}
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {

			setAllDefault();
			switch (position) {
				case 0:
					inner.setTextColor(ControlDisplayUtil.getInstance().getHCColor(0));
					detail.setTextColor(ControlDisplayUtil.getInstance().getHCColor(0));

					outer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ControlDisplayUtil.getInstance().getHCTextSize(1));
					outer.setTextColor(ControlDisplayUtil.getInstance().getHCColor(1));
					break;
				case 1:
					outer.setTextColor(ControlDisplayUtil.getInstance().getHCColor(0));
					detail.setTextColor(ControlDisplayUtil.getInstance().getHCColor(0));

					inner.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ControlDisplayUtil.getInstance().getHCTextSize(1));
					inner.setTextColor(ControlDisplayUtil.getInstance().getHCColor(1));
					break;
				case 2:
					outer.setTextColor(ControlDisplayUtil.getInstance().getHCColor(0));
					inner.setTextColor(ControlDisplayUtil.getInstance().getHCColor(0));

					detail.setTextSize(TypedValue.COMPLEX_UNIT_DIP, ControlDisplayUtil.getInstance().getHCTextSize(1));
					detail.setTextColor(ControlDisplayUtil.getInstance().getHCColor(1));
					break;
			}

			mPagers.get(position).initData();
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
		                           int positionOffsetPixels) {
			if (position == mPagers.size())
				return;
			float leftMargin = tabWidth * (position + positionOffset);
			FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) cursor
				.getLayoutParams();
			lp.leftMargin = (int) leftMargin;
			cursor.setLayoutParams(lp);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (arg0 == mPagers.size())
				return;
		}
	}

	private class TabAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = mPagers.get(position).getRootView();
			container.addView(view);
			if (position == 0) {
				mPagers.get(position).initData();
			}
			return view;
		}
	}
}