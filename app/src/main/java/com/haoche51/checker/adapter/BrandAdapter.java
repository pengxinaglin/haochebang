package com.haoche51.checker.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.custom.HCTextView;
import com.haoche51.checker.entity.VehicleBrandEntity;
import com.haoche51.checker.helper.BrandLogoHelper;

import java.util.List;

public class BrandAdapter extends BaseAdapter implements SectionIndexer {
    private List<VehicleBrandEntity> brands;
    private View.OnClickListener mClickListener;

    public BrandAdapter(List<VehicleBrandEntity> brands, View.OnClickListener listener) {
        this.brands = brands;
        this.mClickListener = listener;
    }

    @Override
    public int getCount() {
        return brands.size();
    }

    @Override
    public Object getItem(int position) {
        return brands.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setBrands(List<VehicleBrandEntity> brands) {
        this.brands = brands;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = LayoutInflater.from(GlobalData.mContext).inflate(R.layout.layout_brand_list_item, parent, false);
            mHolder.brandName = (HCTextView) convertView.findViewById(R.id.brand_name);
            mHolder.brandName.hideTriangle();
            mHolder.IndexLetter = (TextView) convertView.findViewById(R.id.brand_letter);
            convertView.setTag(mHolder);
            mHolder.IndexLetter.getPaint().setFakeBoldText(true);
            mHolder.brandName.hideTriangle();

        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        VehicleBrandEntity mBrand = brands.get(position);
        mHolder.brandName.setText(mBrand.getName());
        setUpBrand(mHolder, mBrand);
        setIndexer(mHolder, position, mBrand);
        convertView.setTag(R.id.for_brand_pos, position);
        convertView.setTag(R.id.brand_convert_tag, mBrand);
        convertView.setOnClickListener(mClickListener);

        return convertView;
    }

    private void setIndexer(ViewHolder holder, int position, VehicleBrandEntity brand) {
        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            holder.IndexLetter.setVisibility(View.VISIBLE);
            holder.IndexLetter.setText(brand.getFirst_char());
        } else {
            holder.IndexLetter.setVisibility(View.GONE);
        }
    }

    private void setUpBrand(ViewHolder holder, VehicleBrandEntity brand) {
        int resId;
        if (BrandLogoHelper.BRAND_LOGO.containsKey(brand.getId())) {
            resId = BrandLogoHelper.BRAND_LOGO.get(brand.getId());
        } else {
            resId = R.drawable.empty_brand;
        }
        Drawable logo = GlobalData.mContext.getResources().getDrawable(resId);
        if (logo != null) {
            logo.setBounds(0, 0, logo.getIntrinsicWidth(), logo.getIntrinsicHeight());
            holder.brandName.setCompoundDrawables(logo, null, null, null);
        }

    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public int getPositionForSection(int sectionIndex) {

        for (int i = 0; i < getCount(); i++) {
            char sortChar = brands.get(i).getFirst_char().toUpperCase().charAt(0);
            if (sortChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    @Override
    public int getSectionForPosition(int position) {
        return brands.get(position).getFirst_char().charAt(0);
    }

    private class ViewHolder {
        HCTextView brandName;
        TextView IndexLetter;
    }
}
