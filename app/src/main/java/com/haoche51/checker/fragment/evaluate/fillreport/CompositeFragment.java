package com.haoche51.checker.fragment.evaluate.fillreport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.haoche51.checker.R;
import com.haoche51.checker.adapter.CompreCheckAdapter;
import com.haoche51.checker.constants.CompreConstants;
import com.haoche51.checker.custom.CheckListView;
import com.haoche51.checker.custom.ComprePopWindow;
import com.haoche51.checker.custom.ItemPopWindow;
import com.haoche51.checker.fragment.BaseReportFragment;
import com.haoche51.checker.item.CheckItem;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航栏—综合按钮对应的界面
 * Created by wfx on 2016/6/30.
 */
public class CompositeFragment extends BaseReportFragment {

    @ViewInject(R.id.compre_minor_accident)
    private CheckListView minorListView;

    @ViewInject(R.id.compre_mechanical_related)
    private CheckListView mechanicalListView;

    @ViewInject(R.id.compre_relevant_formalities)
    private CheckListView relevantListView;

    private CompreCheckAdapter minorAdapter = null;
    private List<CheckItem> minorList = new ArrayList<>();

    private CompreCheckAdapter mechanicalAdapter = null;
    private List<CheckItem> mechanicalList = new ArrayList<>();

    private CompreCheckAdapter relevantAdapter = null;
    private List<CheckItem> relevantList = new ArrayList<>();


    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_composite, null);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
//        loadDBData();
        initListViewItemClick();
    }

    @Override
    protected void loadDBData() {
        if (mCheckReport == null) {
            return;
        }
        initMinorAccidentData();
        initMechanicalData();
        initRelevantData();
    }

    /**
     * 读取Report 机械相关 信息
     *
     * @return
     */
    public void initMechanicalData() {
        if (mCheckReport != null) {
            mechanicalList = new ArrayList<>();
            mechanicalList.add(new CheckItem(CompreConstants.MECHANICAL_RELATED_LIST[0],
                    mCheckReport.getEngine_sound()));
            mechanicalList.add(new CheckItem(CompreConstants.MECHANICAL_RELATED_LIST[1],
                    mCheckReport.getEngine_spill()));
        }
    }

    /**
     * 读取Report手续相关信息
     *
     * @return
     */
    public void initRelevantData() {
        if (mCheckReport != null) {
            relevantList = new ArrayList<>();
            relevantList.add(new CheckItem(CompreConstants.RELEVANT_FORMALITIES_LIST[0],
                    mCheckReport.getMortgage()));
            relevantList.add(new CheckItem(CompreConstants.RELEVANT_FORMALITIES_LIST[1],
                    mCheckReport.getInto_bj()));
            relevantList.add(new CheckItem(CompreConstants.RELEVANT_FORMALITIES_LIST[2],
                    mCheckReport.getCompany_account()));
            relevantList.add(new CheckItem(CompreConstants.RELEVANT_FORMALITIES_LIST[3],
                    mCheckReport.getOperation()));
        }
    }

    /**
     * 读取Report 关于轻微事故 信息
     *
     * @return
     */
    public void initMinorAccidentData() {
        if (mCheckReport == null) {
            return;
        }
        minorList = new ArrayList<>();
        minorList.add(new CheckItem(CompreConstants.MINOR_ACCIDENT_LIST[0],
                mCheckReport.getWater_tank()));
        minorList.add(new CheckItem(CompreConstants.MINOR_ACCIDENT_LIST[1],
                mCheckReport.getMetal_tank()));
        minorList.add(new CheckItem(CompreConstants.MINOR_ACCIDENT_LIST[2],
                mCheckReport.getHeadlight_frame()));
        minorList.add(new CheckItem(CompreConstants.MINOR_ACCIDENT_LIST[3],
                mCheckReport.getFender_liner()));
        minorList.add(new CheckItem(CompreConstants.MINOR_ACCIDENT_LIST[4],
                mCheckReport.getBack_floor()));
        minorList.add(new CheckItem(CompreConstants.MINOR_ACCIDENT_LIST[5],
                mCheckReport.getBack_coaming()));
    }


    /**
     * 存储Report 关于Engine 相关信息
     *
     * @return
     */
    public void saveCompositeData() {
//        CheckReportEntity mCheckReport = CheckReportDAO.getInstance().get(
//                mReportId);
        if (mCheckReport == null || minorList.size() == 0) {
            return;
        }
        mCheckReport.setWater_tank(minorList.get(0).getStatus());
        mCheckReport.setMetal_tank(minorList.get(1).getStatus());
        mCheckReport.setHeadlight_frame(minorList.get(2).getStatus());
        mCheckReport.setFender_liner(minorList.get(3).getStatus());
        mCheckReport.setBack_floor(minorList.get(4).getStatus());
        mCheckReport.setBack_coaming(minorList.get(5).getStatus());

        mCheckReport.setEngine_sound(mechanicalList.get(0).getStatus());
        mCheckReport.setEngine_spill(mechanicalList.get(1).getStatus());

        mCheckReport.setMortgage(relevantList.get(0).getStatus());
        mCheckReport.setInto_bj(relevantList.get(1).getStatus());
        mCheckReport.setCompany_account(relevantList.get(2).getStatus());
        mCheckReport.setOperation(relevantList.get(3).getStatus());

//        CheckReportDAO.getInstance().update(mReportId, mCheckReport);
    }

    private void initListViewItemClick() {
        minorAdapter = new CompreCheckAdapter(getActivity(), minorList);
        minorListView.setAdapter(minorAdapter);

        mechanicalAdapter = new CompreCheckAdapter(getActivity(), mechanicalList);
        mechanicalListView.setAdapter(mechanicalAdapter);

        relevantAdapter = new CompreCheckAdapter(getActivity(), relevantList);
        relevantListView.setAdapter(relevantAdapter);
        /**
         * 轻微事故
         */
        minorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ComprePopWindow comprePop = new ComprePopWindow(
                        getActivity(), minorList.get(position)
                        .getStatus());
                comprePop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

                    @Override
                    public void statusChange(int status, int position) {
                        minorList.get(position).setStatus(status);
                        minorAdapter.notifyDataSetChanged();
                    }

                });
                comprePop.show(view, position);
            }
        });
        /**
         * 机械相关
         */
        mechanicalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ComprePopWindow comprePop = new ComprePopWindow(getActivity(),
                        mechanicalList.get(position).getStatus());
                comprePop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

                    @Override
                    public void statusChange(int status, int position) {
                        mechanicalList.get(position).setStatus(status);
                        mechanicalAdapter.notifyDataSetChanged();
                    }

                });
                comprePop.show(view, position);
            }

        });

        /**
         * 手续相关
         */
        relevantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ComprePopWindow comprePop = new ComprePopWindow(
                        getActivity(), relevantList.get(position)
                        .getStatus());
                comprePop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

                    @Override
                    public void statusChange(int status, int position) {
                        relevantList.get(position).setStatus(status);
                        relevantAdapter.notifyDataSetChanged();
                    }
                });
                comprePop.show(view, position);
            }

        });
    }


    @Override
    public void saveData() {
        saveCompositeData();
    }
}
