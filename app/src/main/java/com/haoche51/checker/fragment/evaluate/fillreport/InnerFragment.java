package com.haoche51.checker.fragment.evaluate.fillreport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.adapter.TrimCheckAdapter;
import com.haoche51.checker.constants.TrimConstants;
import com.haoche51.checker.custom.CheckListView;
import com.haoche51.checker.custom.FeaturePopWindow;
import com.haoche51.checker.custom.ItemPopWindow;
import com.haoche51.checker.custom.SmellPopWindow;
import com.haoche51.checker.custom.TrimPopWindow;
import com.haoche51.checker.fragment.BaseReportFragment;
import com.haoche51.checker.item.CheckItem;
import com.haoche51.checker.item.ECheckItem;
import com.haoche51.checker.util.StatusInfoUtils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航栏—内饰按钮对应的界面
 * Created by wfx on 2016/7/1.
 */
public class InnerFragment extends BaseReportFragment {
    @ViewInject(R.id.central_list)
    private CheckListView centralListView;

    @ViewInject(R.id.vehicle_roof_list)
    private CheckListView vehicleRoofListView;

    @ViewInject(R.id.seat_list)
    private CheckListView seatListView;

    @ViewInject(R.id.door_list)
    private CheckListView doorListView;

    @ViewInject(R.id.pillar_list)
    private CheckListView pillarListView;

    @ViewInject(R.id.trunk_label)
    private TextView trunk_label;

    @ViewInject(R.id.trunk_status)
    private TextView trunk_status;

    private int trunkStatus = 0;

    @ViewInject(R.id.rubberstrip_label)
    private TextView rubber_strip_label;

    @ViewInject(R.id.rubberstrip_status)
    private TextView rubber_strip_status;

    private ECheckItem rubberItem = null;

    @ViewInject(R.id.vehicle_smell_label)
    private TextView smell_label;

    @ViewInject(R.id.vehicle_smell_status)
    private TextView smell_status;

    private int smellStatus = 0;

    private List<CheckItem> centralList = new ArrayList<>();
    private List<CheckItem> roofList = new ArrayList<>();
    private List<CheckItem> seatList = new ArrayList<>();
    private List<CheckItem> doorList = new ArrayList<>();
    private List<CheckItem> pillarList = new ArrayList<>();


    private TrimCheckAdapter centralAdapter = null;
    private TrimCheckAdapter roofAdapter = null;
    private TrimCheckAdapter seatAdapter = null;
    private TrimCheckAdapter doorAdapter = null;
    private TrimCheckAdapter pillarAdapter = null;


    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_inner, null);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUIData();
    }

    @Override
    protected void loadDBData() {
        if (mCheckReport == null) {
            return;
        }
        centralList = new ArrayList<>();
        centralList.add(new CheckItem(TrimConstants.CENTRAL_PART[0],
                mCheckReport.getDashboard_platform()));
        centralList.add(new CheckItem(TrimConstants.CENTRAL_PART[1],
                mCheckReport.getCenter_control()));
        centralList.add(new CheckItem(TrimConstants.CENTRAL_PART[2],
                mCheckReport.getSteering_wheel()));
        centralList.add(new CheckItem(TrimConstants.CENTRAL_PART[3],
                mCheckReport.getGear_handler()));
        centralList.add(new CheckItem(TrimConstants.CENTRAL_PART[4],
                mCheckReport.getCigar_lighter()));
        centralList.add(new CheckItem(TrimConstants.CENTRAL_PART[5],
                mCheckReport.getGlove_box()));
        roofList = new ArrayList<>();
        roofList.add(new CheckItem(TrimConstants.VEHECLE_ROOF_PART[0],
                mCheckReport.getVehicle_roof_inner()));
        roofList.add(new CheckItem(TrimConstants.VEHECLE_ROOF_PART[1],
                mCheckReport.getM_sunblind()));
        roofList.add(new CheckItem(TrimConstants.VEHECLE_ROOF_PART[2],
                mCheckReport.getA_subblind()));
        roofList.add(new CheckItem(TrimConstants.VEHECLE_ROOF_PART[3],
                mCheckReport.getRear_view()));
        seatList = new ArrayList<>();
        seatList.add(new CheckItem(TrimConstants.SEAT_PART[0], mCheckReport
                .getDriver_seat()));
        seatList.add(new CheckItem(TrimConstants.SEAT_PART[1], mCheckReport
                .getCopilot_seat()));
        seatList.add(new CheckItem(TrimConstants.SEAT_PART[2], mCheckReport
                .getRear_seat()));
        seatList.add(new CheckItem(TrimConstants.SEAT_PART[3], mCheckReport
                .getCenter_armrest()));
        doorList = new ArrayList<>();
        doorList.add(new CheckItem(TrimConstants.DOOR_PART[0], mCheckReport
                .getLf_trim_plate()));
        doorList.add(new CheckItem(TrimConstants.DOOR_PART[1], mCheckReport
                .getRf_trim_plate()));
        doorList.add(new CheckItem(TrimConstants.DOOR_PART[2], mCheckReport
                .getLr_trim_plate()));
        doorList.add(new CheckItem(TrimConstants.DOOR_PART[3], mCheckReport
                .getRr_trim_plate()));
        doorList.add(new CheckItem(TrimConstants.DOOR_PART[4], mCheckReport
                .getLf_armrest()));
        doorList.add(new CheckItem(TrimConstants.DOOR_PART[5], mCheckReport
                .getLr_armrest()));
        doorList.add(new CheckItem(TrimConstants.DOOR_PART[6], mCheckReport
                .getRf_armrest()));
        doorList.add(new CheckItem(TrimConstants.DOOR_PART[7], mCheckReport
                .getRl_armrest()));

        pillarList = new ArrayList<>();
        pillarList.add(new CheckItem(TrimConstants.PILLAR_PART[0], mCheckReport
                .getA_trim_plate()));
        pillarList.add(new CheckItem(TrimConstants.PILLAR_PART[1], mCheckReport
                .getB_trim_plate()));
        pillarList.add(new CheckItem(TrimConstants.PILLAR_PART[2], mCheckReport
                .getC_trim_plate()));
        //其他
        trunkStatus = mCheckReport.getTrunk();
        rubberItem = getSingleFeature(mCheckReport.getRubber_strip(), false);
        smellStatus = mCheckReport.getVehicle_smell();
    }

    private void initUIData() {
        if (mCheckReport == null) {
            return;
        }

        trunk_status.setText(StatusInfoUtils.getTrimStatus(trunkStatus));
        changeStatusStyle(trunk_label, trunk_status, trunkStatus);

        rubber_strip_status.setText(StatusInfoUtils.getOtherStatus(rubberItem.getStatus()));
        changeStatusStyle(rubber_strip_label, rubber_strip_status, rubberItem.getStatus());

        smell_status.setText(StatusInfoUtils.getSmellStatus(smellStatus));
        changeStatusStyle(smell_label, smell_status, smellStatus);

        centralAdapter = new TrimCheckAdapter(getActivity(), centralList);
        centralListView.setAdapter(centralAdapter);
        roofAdapter = new TrimCheckAdapter(getActivity(), roofList);
        vehicleRoofListView.setAdapter(roofAdapter);
        seatAdapter = new TrimCheckAdapter(getActivity(), seatList);
        seatListView.setAdapter(seatAdapter);
        doorAdapter = new TrimCheckAdapter(getActivity(), doorList);
        doorListView.setAdapter(doorAdapter);
        pillarAdapter = new TrimCheckAdapter(getActivity(), pillarList);
        pillarListView.setAdapter(pillarAdapter);
    }

    protected void saveInnerData() {
        if (mCheckReport == null || centralList.size() == 0) {
            return;
        }
        mCheckReport.setDashboard_platform(centralList.get(0).getStatus());
        mCheckReport.setCenter_control(centralList.get(1).getStatus());
        mCheckReport.setSteering_wheel(centralList.get(2).getStatus());
        mCheckReport.setGear_handler(centralList.get(3).getStatus());
        mCheckReport.setCigar_lighter(centralList.get(4).getStatus());
        mCheckReport.setGlove_box(centralList.get(5).getStatus());

        mCheckReport.setVehicle_roof_inner(roofList.get(0).getStatus());
        mCheckReport.setM_sunblind(roofList.get(1).getStatus());
        mCheckReport.setA_subblind(roofList.get(2).getStatus());
        mCheckReport.setRear_view(roofList.get(3).getStatus());

        mCheckReport.setDriver_seat(seatList.get(0).getStatus());
        mCheckReport.setCopilot_seat(seatList.get(1).getStatus());
        mCheckReport.setRear_seat(seatList.get(2).getStatus());
        mCheckReport.setCenter_armrest(seatList.get(3).getStatus());

        mCheckReport.setLf_trim_plate(doorList.get(0).getStatus());
        mCheckReport.setRf_trim_plate(doorList.get(1).getStatus());
        mCheckReport.setLr_trim_plate(doorList.get(2).getStatus());
        mCheckReport.setRr_trim_plate(doorList.get(3).getStatus());
        mCheckReport.setLf_armrest(doorList.get(4).getStatus());
        mCheckReport.setLr_armrest(doorList.get(5).getStatus());
        mCheckReport.setRf_armrest(doorList.get(6).getStatus());
        mCheckReport.setRl_armrest(doorList.get(7).getStatus());

        mCheckReport.setA_trim_plate(pillarList.get(0).getStatus());
        mCheckReport.setB_trim_plate(pillarList.get(1).getStatus());
        mCheckReport.setC_trim_plate(pillarList.get(2).getStatus());
        mCheckReport.setTrunk(trunkStatus);
        mCheckReport.setRubber_strip(saveSingleFeature(rubberItem));
        mCheckReport.setVehicle_smell(smellStatus);
    }


    @Event(value = R.id.central_list,type= AdapterView.OnItemClickListener.class)
    private void centralItemClick(AdapterView<?> parent, View view, final int position,
                                 long id) {
        TrimPopWindow centralPop = new TrimPopWindow(getActivity(), centralList.get(position).getStatus());
        centralPop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

            @Override
            public void statusChange(int status, int position) {
                centralList.get(position).setStatus(status);
                centralAdapter.notifyDataSetChanged();
            }
        });
        centralPop.show(view, position);

    }

    @Event(value = R.id.vehicle_roof_list,type=AdapterView.OnItemClickListener.class)
    private void roofItemClick(AdapterView<?> parent, View view, final int position,
                              long id) {
        TrimPopWindow roofPop = new TrimPopWindow(getActivity(), roofList.get(position).getStatus());
        roofPop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

            @Override
            public void statusChange(int status, int position) {
                roofList.get(position).setStatus(status);
                roofAdapter.notifyDataSetChanged();
            }
        });
        roofPop.show(view, position);

    }

    @Event(value = R.id.seat_list,type=AdapterView.OnItemClickListener.class)
    private void seatItemClick(AdapterView<?> parent, View view, final int position,
                              long id) {
        TrimPopWindow seatPop = new TrimPopWindow(getActivity(), seatList.get(position).getStatus());
        seatPop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

            @Override
            public void statusChange(int status, int position) {
                seatList.get(position).setStatus(status);
                seatAdapter.notifyDataSetChanged();
            }
        });
        seatPop.show(view, position);

    }

    @Event(value = R.id.door_list,type=AdapterView.OnItemClickListener.class)
    private void doorItemClick(AdapterView<?> parent, View view, final int position,
                              long id) {
        TrimPopWindow doorPop = new TrimPopWindow(getActivity(), doorList.get(position).getStatus());
        doorPop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

            @Override
            public void statusChange(int status, int position) {
                doorList.get(position).setStatus(status);
                doorAdapter.notifyDataSetChanged();
            }
        });
        doorPop.show(view, position);
    }

    @Event(value = R.id.pillar_list,type=AdapterView.OnItemClickListener.class)
    private void pillorItemClick(AdapterView<?> parent, View view, final int position,
                                long id) {
        TrimPopWindow pillarPop = new TrimPopWindow(getActivity(), pillarList.get(position).getStatus());
        pillarPop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

            @Override
            public void statusChange(int status, int position) {
                pillarList.get(position).setStatus(status);
                pillarAdapter.notifyDataSetChanged();
            }
        });
        pillarPop.show(view, position);

    }


    @Event(R.id.ll_trunk)
    private void trunkClick(View view) {
        TrimPopWindow pillarPop = new TrimPopWindow(getActivity(), trunkStatus);
        pillarPop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

            @Override
            public void statusChange(int status, int position) {
                trunkStatus = status;
                trunk_status.setText(StatusInfoUtils.getTrimStatus(status));
                changeStatusStyle(trunk_label, trunk_status, trunkStatus);
            }
        });
        pillarPop.show(view, 0);

    }

    @Event(R.id.ll_rubber_strip)
    private void rubberStripClick(View view) {
        FeaturePopWindow mPop = new FeaturePopWindow(getActivity(), rubberItem.getStatus(), rubberItem.getDesc(), "全车胶条");
        mPop.setOnFeatureChangeListener(new FeaturePopWindow.OnFeatureChangeListener() {

            @Override
            public void onFeatureChange(int status, String desc) {
                rubberItem.setStatus(status);
                rubberItem.setDesc(desc);
                rubber_strip_status.setText(StatusInfoUtils.getOtherStatus(status));
                changeStatusStyle(rubber_strip_label, rubber_strip_status, rubberItem.getStatus());
            }

        });
        mPop.show(view);

    }

    @Event(R.id.ll_vehicle_smell)
    private void smellClick(View view) {
        //TODO
        SmellPopWindow mPop = new SmellPopWindow(getActivity(), smellStatus);
        mPop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

            @Override
            public void statusChange(int status, int position) {
                smellStatus = status;
                smell_status.setText(StatusInfoUtils.getSmellStatus(status));
                changeStatusStyle(smell_label, smell_status, smellStatus);
            }

        });
        mPop.show(view, 0);
    }


    @Override
    public void saveData() {
        saveInnerData();
    }
}
