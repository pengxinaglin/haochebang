package com.haoche51.checker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.custom.RoundImageView;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.helper.ImageLoaderHelper;

import java.util.List;

public class PurchaseSuccessAdapter extends HCCommonAdapter<PhotoEntity> {

  public PurchaseSuccessAdapter(Context context, List<PhotoEntity> data, int layoutId) {
    super(context, data, layoutId);
  }

  @Override
  public void fillViewData(HCCommonViewHolder holder, int position) {
    //    View view = LayoutInflater.from(context).inflate(R.layout.item_purchase_success, parent, false);
    RoundImageView iv_photo = holder.findTheView(R.id.riv_purchase_photo);
    TextView tv_photo_tag = holder.findTheView(R.id.tv_photo_tag);
    TextView tv_photo_tag_new = holder.findTheView(R.id.tv_photo_tag_new);
    final PhotoEntity temPhoto = mList.get(position);
    if (!TextUtils.isEmpty(temPhoto.getPath())) {
      tv_photo_tag.setVisibility(View.GONE);
      tv_photo_tag_new.setVisibility(View.VISIBLE);
      tv_photo_tag_new.setText(temPhoto.getName());
      //清除imageview的背景
      iv_photo.setBackgroundResource(0);
      ImageLoaderHelper.displayImage("file://" + temPhoto.getPath(), iv_photo);
    } else {
      tv_photo_tag.setVisibility(View.VISIBLE);
      tv_photo_tag_new.setVisibility(View.INVISIBLE);
      tv_photo_tag.setText(temPhoto.getName());
    }
  }

}
