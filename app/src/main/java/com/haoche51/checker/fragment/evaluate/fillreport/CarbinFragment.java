package com.haoche51.checker.fragment.evaluate.fillreport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.haoche51.checker.R;
import com.haoche51.checker.adapter.EngineCheckAdapter;
import com.haoche51.checker.constants.EngineConstants;
import com.haoche51.checker.custom.CheckListView;
import com.haoche51.checker.custom.EnginePopWindow;
import com.haoche51.checker.custom.ItemPopWindow;
import com.haoche51.checker.fragment.BaseReportFragment;
import com.haoche51.checker.item.CheckItem;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航栏—机舱按钮对应的界面
 * Created by wfx on 2016/7/1.
 */
public class CarbinFragment extends BaseReportFragment {
    @ViewInject(R.id.engine_bay_list)
    private CheckListView engineListView;

    private List<CheckItem> checkList = new ArrayList<>();
    private EngineCheckAdapter mAdapter = null;

    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_carbin, null);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUIData();
    }


    /**
     * 读取Report 关于Engine 信息
     *
     * @return
     */
    @Override
    protected void loadDBData() {
        if (mCheckReport == null) {
            return;
        }
        checkList = new ArrayList<>();
        checkList.add(new CheckItem(EngineConstants.ENGINE_CHECK_LIST[0],
                mCheckReport.getEngine_oil()));
        checkList.add(new CheckItem(EngineConstants.ENGINE_CHECK_LIST[1],
                mCheckReport.getCylinder()));
        checkList.add(new CheckItem(EngineConstants.ENGINE_CHECK_LIST[2],
                mCheckReport.getRadiator()));
        checkList.add(new CheckItem(EngineConstants.ENGINE_CHECK_LIST[3],
                mCheckReport.getElectrode_pillar()));
        checkList.add(new CheckItem(EngineConstants.ENGINE_CHECK_LIST[4],
                mCheckReport.getElectrolyte()));
        checkList.add(new CheckItem(EngineConstants.ENGINE_CHECK_LIST[5],
                mCheckReport.getEngine_belt()));
        checkList.add(new CheckItem(EngineConstants.ENGINE_CHECK_LIST[6],
                mCheckReport.getTubing()));
        checkList.add(new CheckItem(EngineConstants.ENGINE_CHECK_LIST[7],
                mCheckReport.getWater_pipe()));
        checkList.add(new CheckItem(EngineConstants.ENGINE_CHECK_LIST[8],
                mCheckReport.getHarness()));
        checkList.add(new CheckItem(EngineConstants.ENGINE_CHECK_LIST[9],
                mCheckReport.getEngine_oil_location()));
        checkList.add(new CheckItem(EngineConstants.ENGINE_CHECK_LIST[10],
                mCheckReport.getBreaker_oil()));
        checkList.add(new CheckItem(EngineConstants.ENGINE_CHECK_LIST[11],
                mCheckReport.getSteering_oil()));

    }

    private void initUIData() {
        if (mCheckReport == null) {
            return;
        }
        mAdapter = new EngineCheckAdapter(getActivity(), checkList);
        engineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EnginePopWindow mPop = new EnginePopWindow(getActivity(), checkList.get(position).getStatus());
                mPop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

                    @Override
                    public void statusChange(int status, int position) {
                        checkList.get(position).setStatus(status);
                        mAdapter.notifyDataSetChanged();
                    }

                });
                mPop.show(view, position);
            }
        });
        engineListView.setAdapter(mAdapter);
    }


    /**
     * 存储Report 关于Engine 相关信息
     *
     * @return
     */
    public void saveEngineData() {
        if (mCheckReport == null || checkList.size() == 0) {
            return;
        }
        mCheckReport.setEngine_oil(checkList.get(0).getStatus());
        mCheckReport.setCylinder(checkList.get(1).getStatus());
        mCheckReport.setRadiator(checkList.get(2).getStatus());
        mCheckReport.setElectrode_pillar(checkList.get(3).getStatus());
        mCheckReport.setElectrolyte(checkList.get(4).getStatus());
        mCheckReport.setEngine_belt(checkList.get(5).getStatus());
        mCheckReport.setTubing(checkList.get(6).getStatus());
        mCheckReport.setWater_pipe(checkList.get(7).getStatus());
        mCheckReport.setHarness(checkList.get(8).getStatus());
        mCheckReport.setEngine_oil_location(checkList.get(9).getStatus());
        mCheckReport.setBreaker_oil(checkList.get(10).getStatus());
        mCheckReport.setSteering_oil(checkList.get(11).getStatus());
    }

    @Override
    public void saveData() {
        saveEngineData();
    }
}
