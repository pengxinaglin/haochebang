package com.haoche51.checker.adapter;

import android.content.Context;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.entity.MessageEntity;
import com.haoche51.checker.util.UnixTimeUtil;

import java.util.List;

/**
 * Created by xuhaibo on 15/9/12.
 */
public class PushMessageAdapter extends HCCommonAdapter<MessageEntity> {
	public PushMessageAdapter(Context context, List<MessageEntity> data, int layoutId) {
		super(context, data, layoutId);
	}

	@Override
	public void fillViewData(HCCommonViewHolder holder, int position) {
		MessageEntity  entity = getItem(position);
		TextView message_title = holder.findTheView(R.id.message_title);
		if (entity.getStatus() == 0) {
			message_title.setTextColor(mContext.getResources().getColor(R.color.hc_indicator));
		}
		holder.setTextViewText(R.id.message_title, entity.getTitle());
		holder.setTextViewText(R.id.message_detail, entity.getDescription());
		String create_time = UnixTimeUtil.relativeStyle(entity.getCreate_time());
		holder.setTextViewText(R.id.message_date, create_time);
	}
}
