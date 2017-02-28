package com.haoche51.checker.fragment.evaluate.fillreport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.haoche51.checker.R;
import com.haoche51.checker.adapter.StartupAdapter;
import com.haoche51.checker.constants.StartupConstants;
import com.haoche51.checker.custom.CheckListView;
import com.haoche51.checker.custom.ItemPopWindow;
import com.haoche51.checker.custom.StartupPopWindow;
import com.haoche51.checker.fragment.BaseReportFragment;
import com.haoche51.checker.item.CheckItem;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航栏—启动按钮对应的界面
 * Created by wfx on 2016/7/1.
 */
public class StartUpFragment extends BaseReportFragment {

    @ViewInject(R.id.startup_list)
    private CheckListView mListView;

    private List<CheckItem> checkList = new ArrayList<>();
    private StartupAdapter mAdapter = null;

    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_startup, null);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
//        loadDBData();
        initUIData();
    }

    /**
     * 初始化ListView
     */
    private void initUIData() {
        if (mCheckReport == null) {
            return;
        }
        mAdapter = new StartupAdapter(getActivity(), checkList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StartupPopWindow startupPop = new StartupPopWindow(
                        getActivity(), checkList.get(position).getStatus());
                startupPop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

                    @Override
                    public void statusChange(int status, int position) {
                        checkList.get(position).setStatus(status);
                        mAdapter.notifyDataSetChanged();

                    }

                });
                startupPop.show(view, position);
            }
        });
    }

    /**
     * 初始化启动项数据
     *
     * @return
     */
    @Override
    protected void loadDBData() {
        if (mCheckReport == null) {
            return;
        }
        checkList = new ArrayList<>();
        checkList.add(new CheckItem(StartupConstants.STARTUP_PART[0],
                mCheckReport.getVehicle_start()));
        checkList.add(new CheckItem(StartupConstants.STARTUP_PART[1],
                mCheckReport.getEngine_staility()));
        checkList.add(new CheckItem(StartupConstants.STARTUP_PART[2],
                mCheckReport.getIdle_state()));
        checkList.add(new CheckItem(StartupConstants.STARTUP_PART[3],
                mCheckReport.getNeutral_state()));
        checkList.add(new CheckItem(StartupConstants.STARTUP_PART[4],
                mCheckReport.getExhaust_color()));
        checkList.add(new CheckItem(StartupConstants.STARTUP_PART[5],
                mCheckReport.getFault_gear()));
    }

    @Override
    public void saveData() {
        saveStartupData();
    }


    /**
     * 存储启动项相关数据
     *
     * @return
     */
    public void saveStartupData() {
        if (mCheckReport == null || checkList.size() == 0) {
            return;
        }
        mCheckReport.setVehicle_start(checkList.get(0).getStatus());
        mCheckReport.setEngine_staility(checkList.get(1).getStatus());
        mCheckReport.setIdle_state(checkList.get(2).getStatus());
        mCheckReport.setNeutral_state(checkList.get(3).getStatus());
        mCheckReport.setExhaust_color(checkList.get(4).getStatus());
        mCheckReport.setFault_gear(checkList.get(5).getStatus());
    }

}
