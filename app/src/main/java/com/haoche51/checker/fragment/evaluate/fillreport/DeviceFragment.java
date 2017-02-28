package com.haoche51.checker.fragment.evaluate.fillreport;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.haoche51.checker.R;
import com.haoche51.checker.adapter.ECheckAdapter;
import com.haoche51.checker.constants.EquipmentConstants;
import com.haoche51.checker.custom.CheckListView;
import com.haoche51.checker.custom.FeaturePopWindow;
import com.haoche51.checker.custom.ItemPopWindow;
import com.haoche51.checker.custom.TirePopWindow;
import com.haoche51.checker.custom.TireStatPopWindow;
import com.haoche51.checker.fragment.BaseReportFragment;
import com.haoche51.checker.item.ECheckItem;
import com.haoche51.checker.item.TireData;
import com.haoche51.checker.util.StatusInfoUtils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航栏—设备按钮对应的界面
 * Created by wfx on 2016/7/2.
 */
public class DeviceFragment extends BaseReportFragment {
    @ViewInject(R.id.feature_list)
    private CheckListView fListView;

    @ViewInject(R.id.safty_list)
    private CheckListView sListView;

    @ViewInject(R.id.light_list)
    private CheckListView lListView;

    @ViewInject(R.id.tire_uniformity_label)
    private TextView tireUniLabel;

    @ViewInject(R.id.tire_uniformity)
    private TextView tireUniStatus;

    @ViewInject(R.id.tread)
    private TextView treadText;

    @ViewInject(R.id.tire_status_label)
    private TextView tire_status_label;

    @ViewInject(R.id.tire_status)
    private TextView tire_status;

    private ECheckItem eCheckItem = null;
    private TireData mBreakPad = null;
    private TireData mTread = null;
    private int spareTire = 0;

    private List<ECheckItem> featureList = new ArrayList<>();
    private List<ECheckItem> safetyList = new ArrayList<>();
    private List<ECheckItem> lightList = new ArrayList<>();
    private ECheckAdapter fAdapter = null;
    private ECheckAdapter sAdapter = null;
    private ECheckAdapter lAdapter = null;

    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_device, null);
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
        featureList = new ArrayList<>();
        featureList.add(getSingleFeature(mCheckReport.getDashboard(), false));
        featureList.add(getSingleFeature(mCheckReport.getReading_lamp(), false));
        featureList.add(getSingleFeature(mCheckReport.getWiper(), false));
        featureList.add(getSingleFeature(mCheckReport.getWindow_lifter(), false));
        featureList.add(getSingleFeature(mCheckReport.getAir_condition(), false));
        featureList.add(getSingleFeature(mCheckReport.getSound_system(), false));
        featureList.add(getSingleFeature(mCheckReport.getHandbrake(), false));
        featureList.add(getSingleFeature(mCheckReport.getCentral_lock(), false));
        featureList.add(getSingleFeature(mCheckReport.getSkylight(), true));
        featureList.add(getSingleFeature(mCheckReport.getCentral_dispaly(), true));
        featureList.add(getSingleFeature(mCheckReport.getGps(), true));
        featureList.add(getSingleFeature(mCheckReport.getReverse_sensor(),
                true));
        featureList.add(getSingleFeature(mCheckReport.getM_electric_seat(),
                true));
        featureList.add(getSingleFeature(mCheckReport.getA_electric_seat(),
                true));

        safetyList = new ArrayList<>();
        safetyList.add(getSingleFeature(mCheckReport.getDashboard_light(),
                false));
        safetyList.add(getSingleFeature(mCheckReport.getAbs(), true));
        safetyList.add(getSingleFeature(mCheckReport.getAir_bag(), true));
        safetyList
                .add(getSingleFeature(mCheckReport.getSafty_belt(), false));
        safetyList.add(getSingleFeature(mCheckReport.getJack(), false));
        safetyList.add(getSingleFeature(mCheckReport.getFire_extinguiher(),
                false));
        safetyList
                .add(getSingleFeature(mCheckReport.getMark_delta(), false));

        lightList = new ArrayList<>();
        lightList.add(getSingleFeature(mCheckReport.getHigh_beams(), false));
        lightList
                .add(getSingleFeature(mCheckReport.getDipped_beams(), false));
        lightList.add(getSingleFeature(mCheckReport.getFog_light(), false));
        lightList.add(getSingleFeature(mCheckReport.getSteering_lamp(),
                false));
        lightList.add(getSingleFeature(mCheckReport.getTaillight(), false));
        lightList.add(getSingleFeature(mCheckReport.getBreak_lamp(), false));
        lightList.add(getSingleFeature(mCheckReport.getDriving_light(), true));

        //轮胎
        eCheckItem = getSingleFeature(mCheckReport.getTire_uniformity(), false);
        mBreakPad = getSingleTire(mCheckReport.getBreakpads_thickness());
        mTread = getSingleTire(mCheckReport.getTread());
        spareTire = mCheckReport.getSpare_tire();
    }


    /**
     * 初始化界面数据
     */
    private void initUIData() {
        if (mCheckReport == null) {
            return;
        }

        fAdapter = new ECheckAdapter(getActivity(), featureList, 0);
        sAdapter = new ECheckAdapter(getActivity(), safetyList, 1);
        lAdapter = new ECheckAdapter(getActivity(), lightList, 2);

        fListView.setAdapter(fAdapter);
        sListView.setAdapter(sAdapter);
        lListView.setAdapter(lAdapter);

        tireUniStatus.setText(StatusInfoUtils.getEquipmentStatus(eCheckItem.getStatus()));
        changeStatusStyle(tireUniLabel, tireUniStatus, eCheckItem.getStatus());

        treadText.setText(StatusInfoUtils.getTireData(mTread));


        tire_status.setText(StatusInfoUtils.getTireStatus(spareTire));
        changeStatusStyle(tire_status_label, tire_status, spareTire);
    }


    @Event(R.id.ll_tire_uni)
    private void tireUniClick(View view) {
        FeaturePopWindow mPop = new FeaturePopWindow(getActivity(), eCheckItem.getStatus(),
                eCheckItem.getDesc(), getString(R.string.tire_consistency));
        mPop.setOnFeatureChangeListener(new FeaturePopWindow.OnFeatureChangeListener() {

            @Override
            public void onFeatureChange(int status, String desc) {
                eCheckItem.setStatus(status);
                eCheckItem.setDesc(desc);
                tireUniStatus.setText(StatusInfoUtils.getEquipmentStatus(status));
                changeStatusStyle(tireUniLabel, tireUniStatus, status);
            }

        });
        mPop.show(view);


    }

    @Event(R.id.ll_tread)
    private void treadClick(View view) {
        TirePopWindow mPop = new TirePopWindow(getActivity(), mTread);
        mPop.setOntireConfirmListener(new TirePopWindow.OnTireConfirmListener() {

            @Override
            public void tireConfirm(TireData mTire) {
                mTread = mTire;
                treadText.setText(StatusInfoUtils.getTireData(mTread));
            }

        });
        mPop.show(view);
    }

    /**
     * 获取刹车片数值
     *
     * @param data
     * @return
     */
    public TireData getSingleTire(String data) {
        Gson mGson = new Gson();
        TireData mTire;
        if (!TextUtils.isDigitsOnly(data)) {
            mTire = mGson.fromJson(data, TireData.class);
        } else {
            mTire = new TireData(0, 0, 0, 0);
        }
        return mTire;
    }

    @Event(value = R.id.feature_list, type = AdapterView.OnItemClickListener.class)
    private void onFeatureItemClick(AdapterView<?> parent, View view,
                                   final int position, long id) {
        FeaturePopWindow mPop = new FeaturePopWindow(getActivity(),
                featureList.get(position).getStatus(), featureList.get(position).getDesc(),
                EquipmentConstants.EQUIPMENT_PART[position]);
        mPop.setOnFeatureChangeListener(new FeaturePopWindow.OnFeatureChangeListener() {

            @Override
            public void onFeatureChange(int status, String desc) {
                featureList.get(position).setStatus(status);
                featureList.get(position).setDesc(desc);
                fAdapter.notifyDataSetChanged();
            }

        });
        mPop.show(parent);
    }

    @Event(value = R.id.safty_list, type = AdapterView.OnItemClickListener.class)
    private void onSaftyItemClick(AdapterView<?> parent, View view,
                                 final int position, long id) {
//        boolean extra = false;
//        if (position == 1 || position == 2) {
//            extra = true;
//        }
        FeaturePopWindow mPop = new FeaturePopWindow(getActivity(),
                safetyList.get(position).getStatus(), safetyList.get(position)
                .getDesc(), EquipmentConstants.SAFTY_PART[position]);
        mPop.setOnFeatureChangeListener(new FeaturePopWindow.OnFeatureChangeListener() {

            @Override
            public void onFeatureChange(int status, String desc) {
                safetyList.get(position).setStatus(status);
                safetyList.get(position).setDesc(desc);
                sAdapter.notifyDataSetChanged();
            }

        });
        mPop.show(parent);
    }

    @Event(value = R.id.light_list,type = AdapterView.OnItemClickListener.class)
    private void onLightItemClick(AdapterView<?> parent, View view,
                                 final int position, long id) {
        FeaturePopWindow mPop = new FeaturePopWindow(getActivity(),
                lightList.get(position).getStatus(), lightList.get(position).getDesc(),
                EquipmentConstants.LIGHT_PART[position]);
        mPop.setOnFeatureChangeListener(new FeaturePopWindow.OnFeatureChangeListener() {

            @Override
            public void onFeatureChange(int status, String desc) {
                lightList.get(position).setStatus(status);
                lightList.get(position).setDesc(desc);
                lAdapter.notifyDataSetChanged();
            }

        });
        mPop.show(parent);

    }


    @Event(R.id.ll_tire_stat)
    private void showTireStat(View view) {
        TireStatPopWindow tireStatPop = new TireStatPopWindow(
                getActivity(), spareTire);
        tireStatPop.setOnStatusChangeListener(new ItemPopWindow.OnStatusChangeListener() {

            @Override
            public void statusChange(int status, int position) {
                spareTire = status;
                tire_status.setText(StatusInfoUtils.getTireStatus(spareTire));
                changeStatusStyle(tire_status_label, tire_status, spareTire);
            }

        });
        tireStatPop.show(view, 0);
    }

    /**
     * 存储配置数据
     *
     * @return
     */
    public void saveDeviceData() {
        if (mCheckReport == null || featureList.size() == 0) {
            return;
        }

        mCheckReport.setDashboard(saveSingleFeature(featureList.get(0)));
        mCheckReport.setReading_lamp(saveSingleFeature(featureList.get(1)));
        mCheckReport.setWiper(saveSingleFeature(featureList.get(2)));
        mCheckReport.setWindow_lifter(saveSingleFeature(featureList.get(3)));
        mCheckReport.setAir_condition(saveSingleFeature(featureList.get(4)));
        mCheckReport.setSound_system(saveSingleFeature(featureList.get(5)));
        mCheckReport.setHandbrake(saveSingleFeature(featureList.get(6)));
        mCheckReport.setCentral_lock(saveSingleFeature(featureList.get(7)));
        mCheckReport.setSkylight(saveSingleFeature(featureList.get(8)));
        mCheckReport
                .setCentral_dispaly(saveSingleFeature(featureList.get(9)));
        mCheckReport.setGps(saveSingleFeature(featureList.get(10)));
        mCheckReport
                .setReverse_sensor(saveSingleFeature(featureList.get(11)));
        mCheckReport.setM_electric_seat(saveSingleFeature(featureList
                .get(12)));
        mCheckReport.setA_electric_seat(saveSingleFeature(featureList
                .get(13)));//

        mCheckReport
                .setDashboard_light(saveSingleFeature(safetyList.get(0)));
        mCheckReport.setAbs(saveSingleFeature(safetyList.get(1)));
        mCheckReport.setAir_bag(saveSingleFeature(safetyList.get(2)));
        mCheckReport.setSafty_belt(saveSingleFeature(safetyList.get(3)));
        mCheckReport.setJack(saveSingleFeature(safetyList.get(4)));
        mCheckReport
                .setFire_extinguiher(saveSingleFeature(safetyList.get(5)));
        mCheckReport.setMark_delta(saveSingleFeature(safetyList.get(6)));

        mCheckReport.setHigh_beams(saveSingleFeature(lightList.get(0)));
        mCheckReport.setDipped_beams(saveSingleFeature(lightList.get(1)));
        mCheckReport.setFog_light(saveSingleFeature(lightList.get(2)));
        mCheckReport.setSteering_lamp(saveSingleFeature(lightList.get(3)));
        mCheckReport.setTaillight(saveSingleFeature(lightList.get(4)));
        mCheckReport.setBreak_lamp(saveSingleFeature(lightList.get(5)));
        mCheckReport.setDriving_light(saveSingleFeature(lightList.get(6)));
        //保存轮胎信息
        Gson mGson = new Gson();
        mCheckReport.setTire_uniformity(mGson.toJson(eCheckItem));
        mCheckReport.setBreakpads_thickness(mGson.toJson(mBreakPad));
        mCheckReport.setTread(mGson.toJson(mTread));
        mCheckReport.setSpare_tire(spareTire);
    }


    @Override
    public void saveData() {
        saveDeviceData();
    }
}
