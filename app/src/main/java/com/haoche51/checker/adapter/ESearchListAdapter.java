package com.haoche51.checker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.entity.CheckTaskEntity;
import com.haoche51.checker.item.EvaluateTasksFilter;
import com.haoche51.checker.util.UnixTimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部tab 验车  adapter
 */
public class ESearchListAdapter extends BaseAdapter implements Filterable {


	public List<CheckTaskEntity> getAdapterTask() {
		return adapterTask;
	}

	public void setAdapterTask(List<CheckTaskEntity> adapterTask) {
		this.adapterTask = adapterTask;
	}

	private LayoutInflater mInflater;
	private Context sContext;
	private List<CheckTaskEntity> adapterTask = new ArrayList<CheckTaskEntity>();


	private class ViewHolder {
		TextView taskTitle;
		TextView taskDTime;
		TextView taskBTime;
		TextView taskAddress;
		TextView taskStatus;
	}

	public ESearchListAdapter(Context context, List<CheckTaskEntity> adapterTask) {
		sContext = context;
		this.adapterTask = adapterTask;
		this.mInflater = LayoutInflater.from(sContext);
	}

	@Override
	public int getCount() {
		if (adapterTask == null) return 0;
		if (adapterTask.size() == 0) {
			return 1;
		}
		return adapterTask.size();
	}

	@Override
	public Object getItem(int position) {
		return adapterTask.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (adapterTask.size() == 0) {
			TextView textView = new TextView(sContext);
			textView.setText(GlobalData.resourceHelper.getString(R.string.task_list_empty));
			textView.setTextSize(GlobalData.resourceHelper.getFontSize(R.dimen.px_18));
			textView.setTextColor(Color.BLACK);
			textView.setHeight(GlobalData.resourceHelper.getDimenPx(R.dimen.px_120));
			textView.setWidth(-1);
			textView.setGravity(0x11);
			return textView;
		}
		CheckTaskEntity eTask = (CheckTaskEntity) getItem(position);
		ViewHolder mHolder;
		if (convertView == null || TextView.class.isInstance(convertView)) {
			convertView = mInflater.inflate(R.layout.task_item, parent, false);
			mHolder = new ViewHolder();
			mHolder.taskStatus = (TextView) convertView.findViewById(R.id.task_status);
			mHolder.taskTitle = (TextView) convertView.findViewById(R.id.tasks_title);
			mHolder.taskDTime = (TextView) convertView.findViewById(R.id.tasks_date);
			mHolder.taskAddress = (TextView) convertView.findViewById(R.id.taks_apaddress);
			mHolder.taskBTime = (TextView) convertView.findViewById(R.id.task_apdate);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		initItemData(mHolder, eTask);
		return convertView;
	}

	private void initItemData(ViewHolder mHolder, CheckTaskEntity task) {
		mHolder.taskStatus.setVisibility(View.GONE);
//		ControlDisplayUtil.getInstance().setTextAndColorCheckStatus(mHolder.taskStatus, mHolder.taskDTime, task);
		//状态
		mHolder.taskTitle.setText(task.getSeller_name() + "|" + task.getVehicle_name());
		mHolder.taskBTime.setText(UnixTimeUtil.formatEvalTime(task.getAppointment_starttime()) + task.getStarttime_comment());
		mHolder.taskAddress.setText(task.getAppointment_place());
	}

	@Override
	public Filter getFilter() {
		return new EvaluateTasksFilter(adapterTask, this);
	}
}
