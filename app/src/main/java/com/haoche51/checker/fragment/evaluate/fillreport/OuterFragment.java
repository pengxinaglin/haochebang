package com.haoche51.checker.fragment.evaluate.fillreport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.haoche51.checker.R;
import com.haoche51.checker.adapter.CoveringCheckAdapter;
import com.haoche51.checker.adapter.GlassCheckAdapter;
import com.haoche51.checker.adapter.OutCheckAdapter;
import com.haoche51.checker.constants.OutlookConstants;
import com.haoche51.checker.custom.CheckListView;
import com.haoche51.checker.custom.CoverPopWindow;
import com.haoche51.checker.custom.GlassPopWindow;
import com.haoche51.checker.custom.ItemPopWindow;
import com.haoche51.checker.custom.OutPopWindow;
import com.haoche51.checker.fragment.BaseReportFragment;
import com.haoche51.checker.item.CheckItem;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航栏—外观按钮对应的界面
 * Created by wfx on 2016/7/1.
 */
public class OuterFragment extends BaseReportFragment {

    @ViewInject(R.id.covering_list)
    private CheckListView coverListView;

    @ViewInject(R.id.outer_part_list)
    private CheckListView outListView;

    @ViewInject(R.id.glass_list)
    private CheckListView glassListView;

    private CoveringCheckAdapter coverAdapter = null;
    private List<CheckItem> coverList = new ArrayList<>();

    private OutCheckAdapter outAdapter = null;
    private List<CheckItem> outList = new ArrayList<>();

    private GlassCheckAdapter glassAdapter = null;
    private List<CheckItem> glassList = new ArrayList<>();

    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_outer, null);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
//        loadDBData();
        initUIData();
    }

    /**
     * 加载数据库数据
     */
    @Override
    protected void loadDBData() {
        if (mCheckReport == null) {
            return;
        }
        coverList = new ArrayList<>();
        coverList.add(new CheckItem(OutlookConstants.COVERING_PART[0],
                mCheckReport.getEngine_hood()));
        coverList.add(new CheckItem(OutlookConstants.COVERING_PART[1],
                mCheckReport.getRf_fender()));
        coverList.add(new CheckItem(OutlookConstants.COVERING_PART[2],
                mCheckReport.getRf_door()));
        coverList.add(new CheckItem(OutlookConstants.COVERING_PART[3],
                mCheckReport.getRr_door()));
        coverList.add(new CheckItem(OutlookConstants.COVERING_PART[4],
                mCheckReport.getRr_fender()));
        coverList.add(new CheckItem(OutlookConstants.COVERING_PART[5],
                mCheckReport.getDecklid()));
        coverList.add(new CheckItem(OutlookConstants.COVERING_PART[6],
                mCheckReport.getLr_fender()));
        coverList.add(new CheckItem(OutlookConstants.COVERING_PART[7],
                mCheckReport.getLr_door()));
        coverList.add(new CheckItem(OutlookConstants.COVERING_PART[8],
                mCheckReport.getLf_door()));
        coverList.add(new CheckItem(OutlookConstants.COVERING_PART[9],
                mCheckReport.getLf_fender()));
        coverList.add(new CheckItem(OutlookConstants.COVERING_PART[10],
                mCheckReport.getVehicle_roof()));

        // 外观件
        outList = new ArrayList<>();
        outList.add(new CheckItem(OutlookConstants.OUTER_PART[0], mCheckReport
                .getF_bumper()));
        outList.add(new CheckItem(OutlookConstants.OUTER_PART[1], mCheckReport
                .getR_bumper()));
        outList.add(new CheckItem(OutlookConstants.OUTER_PART[2], mCheckReport
                .getL_mirror()));
        outList.add(new CheckItem(OutlookConstants.OUTER_PART[3], mCheckReport
                .getR_mirror()));
        outList.add(new CheckItem(OutlookConstants.OUTER_PART[4], mCheckReport
                .getLf_headlight()));
        outList.add(new CheckItem(OutlookConstants.OUTER_PART[5], mCheckReport
                .getRf_headlight()));
        outList.add(new CheckItem(OutlookConstants.OUTER_PART[6], mCheckReport
                .getL_taillight()));
        outList.add(new CheckItem(OutlookConstants.OUTER_PART[7], mCheckReport
                .getR_taillight()));
        // 玻璃件
        glassList = new ArrayList<>();
        glassList.add(new CheckItem(OutlookConstants.GLASS_PART[0], mCheckReport
                .getF_windshield()));
        glassList.add(new CheckItem(OutlookConstants.GLASS_PART[1], mCheckReport
                .getR_windshield()));
        glassList.add(new CheckItem(OutlookConstants.GLASS_PART[2], mCheckReport
                .getLf_glass()));
        glassList.add(new CheckItem(OutlookConstants.GLASS_PART[3], mCheckReport
                .getLr_glass()));
        glassList.add(new CheckItem(OutlookConstants.GLASS_PART[4], mCheckReport
                .getRf_glass()));
        glassList.add(new CheckItem(OutlookConstants.GLASS_PART[5], mCheckReport
                .getRr_glass()));

    }

    /**
     * 初始化界面数据
     */
    private void initUIData() {
        if (mCheckReport == null) {
            return;
        }
        coverAdapter = new CoveringCheckAdapter(getActivity(), coverList);
        coverListView.setAdapter(coverAdapter);
        outAdapter = new OutCheckAdapter(getActivity(), outList);
        outListView.setAdapter(outAdapter);
        glassAdapter = new GlassCheckAdapter(getActivity(), glassList);
        glassListView.setAdapter(glassAdapter);

        /**
         * 覆盖件Click 事件
         */
        coverListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                CoverPopWindow coverPop = new CoverPopWindow(
                        getActivity(), coverList.get(position)
                        .getStatus());
                coverPop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

                    @Override
                    public void statusChange(int status, int position) {
                        coverList.get(position).setStatus(status);
                        coverAdapter.notifyDataSetChanged();
                    }

                });
                coverPop.show(view, position);
            }
        });
        /**
         * 外观件事件
         */
        outListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                OutPopWindow outPop = new OutPopWindow(getActivity(),
                        outList.get(position).getStatus());
                outPop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

                    @Override
                    public void statusChange(int status, int position) {
                        outList.get(position).setStatus(status);
                        outAdapter.notifyDataSetChanged();
                    }

                });
                outPop.show(view, position);
            }

        });

        /**
         * 玻璃事件
         */
        glassListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                GlassPopWindow glassPop = new GlassPopWindow(
                        getActivity(), glassList.get(position)
                        .getStatus());
                glassPop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

                    @Override
                    public void statusChange(int status, int position) {
                        glassList.get(position).setStatus(status);
                        glassAdapter.notifyDataSetChanged();
                    }
                });
                glassPop.show(view, position);
            }

        });
    }

    private void saveOutlookData() {
        if (mCheckReport == null || coverList.size() == 0) {
            return;
        }
        mCheckReport.setEngine_hood(coverList.get(0).getStatus());
        mCheckReport.setRf_fender(coverList.get(1).getStatus());
        mCheckReport.setRf_door(coverList.get(2).getStatus());
        mCheckReport.setRr_door(coverList.get(3).getStatus());
        mCheckReport.setRr_fender(coverList.get(4).getStatus());
        mCheckReport.setDecklid(coverList.get(5).getStatus());
        mCheckReport.setLr_fender(coverList.get(6).getStatus());
        mCheckReport.setLr_door(coverList.get(7).getStatus());
        mCheckReport.setLf_door(coverList.get(8).getStatus());
        mCheckReport.setLf_fender(coverList.get(9).getStatus());
        mCheckReport.setVehicle_roof(coverList.get(10).getStatus());
        // 保存外观
        mCheckReport.setF_bumper(outList.get(0).getStatus());
        mCheckReport.setR_bumper(outList.get(1).getStatus());
        mCheckReport.setL_mirror(outList.get(2).getStatus());
        mCheckReport.setR_mirror(outList.get(3).getStatus());
        mCheckReport.setLf_headlight(outList.get(4).getStatus());
        mCheckReport.setRf_headlight(outList.get(5).getStatus());
        mCheckReport.setL_taillight(outList.get(6).getStatus());
        mCheckReport.setR_taillight(outList.get(7).getStatus());
        // 保存玻璃件
        mCheckReport.setF_windshield(glassList.get(0).getStatus());
        mCheckReport.setR_windshield(glassList.get(1).getStatus());
        mCheckReport.setLf_glass(glassList.get(2).getStatus());
        mCheckReport.setLr_glass(glassList.get(3).getStatus());
        mCheckReport.setRf_glass(glassList.get(4).getStatus());
        mCheckReport.setRr_glass(glassList.get(5).getStatus());

    }

    @Override
    public void saveData() {
        saveOutlookData();
    }
}
