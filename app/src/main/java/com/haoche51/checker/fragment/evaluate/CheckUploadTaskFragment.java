package com.haoche51.checker.fragment.evaluate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.evaluate.UploadTaskDetailActivity;
import com.haoche51.checker.adapter.CheckUploadTaskAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.fragment.CommonBaseFragment;
import com.haoche51.checker.helper.UploadServiceHelper;

/**
 * 上传中
 */
public class CheckUploadTaskFragment extends CommonBaseFragment implements AdapterView.OnItemClickListener{

    private ListView mListView;
    private TextView mNoDataView;
    private CheckUploadTaskAdapter mAdapter;

    public CheckUploadTaskFragment() {
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_check_upload;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_upload, null);
        mListView = (ListView) view.findViewById(R.id.lv_check_upload);
        mListView.setOnItemClickListener(this);
        mNoDataView = (TextView) view.findViewById(R.id.tv_no_data);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter == null) {
            mAdapter = new CheckUploadTaskAdapter(getActivity());
            mListView.setAdapter(mAdapter);
            UploadServiceHelper.getInstance().setAdapter(mAdapter);
        } else {
            UploadServiceHelper.getInstance().setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        if (UploadServiceHelper.mUploadList == null || UploadServiceHelper.mUploadList.size() == 0) {
            mNoDataView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        } else {
            mNoDataView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (UploadServiceHelper.mUploadList != null && UploadServiceHelper.mUploadList.size() > 0) {
            int taskId = UploadServiceHelper.mUploadList.get(position).getCheckTaskId();
            Intent intent = new Intent(GlobalData.context, UploadTaskDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(TaskConstants.INTENT_EXTRA_ID, taskId);
            intent.putExtra(TaskConstants.INTENT_EXTRA_POSITION, position);
            GlobalData.context.startActivity(intent);
        }
    }

    @Override
    public void onStop() {
        UploadServiceHelper.getInstance().setAdapter(null);
        super.onStop();
    }
}
