package com.haoche51.checker.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haoche51.checker.Checker;
import com.haoche51.checker.CheckerApplication;
import com.haoche51.checker.Debug;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.approve.ApproveMainActivity;
import com.haoche51.checker.activity.channel.ChannelMainActivity;
import com.haoche51.checker.activity.evaluate.CheckMainActivity;
import com.haoche51.checker.activity.hostling.HostlingMainActivity;
import com.haoche51.checker.activity.notice.PushMessageActivity;
import com.haoche51.checker.activity.offerrefer.OfferReferActivity;
import com.haoche51.checker.activity.offlinesold.OfflineSoldMainActivity;
import com.haoche51.checker.activity.purchase.PurchaseMainActivity;
import com.haoche51.checker.activity.recommend.RecommendMainActivity;
import com.haoche51.checker.activity.user.LoginActivity;
import com.haoche51.checker.activity.user.SettingActivity;
import com.haoche51.checker.activity.vehicle.QueryReportActivity;
import com.haoche51.checker.activity.widget.CommonBaseActivity;
import com.haoche51.checker.adapter.HomePageAdapter;
import com.haoche51.checker.custom.GridItemDecoration;
import com.haoche51.checker.entity.UserRightEntity;
import com.haoche51.checker.entity.UserRightShortEntity;
import com.haoche51.checker.listener.RecyclerItemClickListener;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.service.AutoUpdateVersionService;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.HCLogUtil;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.NetInfoUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ThirdPartInjector;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.settlement.onlinepay.OnlinePayIntent;
import com.zbar.lib.CaptureActivity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class HomePageActivity extends CommonBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RecyclerItemClickListener.OnItemClickListener, View.OnClickListener {

    public RecyclerItemClickListener itemClickListener;
    /**
     * 欢迎语
     */
    @ViewInject(R.id.tv_layout_user_welcome_name)
    private TextView textVieName;
    private PopupWindow popupWindow;
    @ViewInject(R.id.swipe_layout)
    private SwipeRefreshLayout swipe_layout;
    @ViewInject(R.id.my_recycler_view)
    private RecyclerView mRecyclerView;
    private ArrayList<UserRightShortEntity> mDataList = new ArrayList<>();
    private HomePageAdapter mHomePageAdapter;
    private List<UserRightEntity> mUserRights;
    private String tag = "HomePageActivity";

    @Override
    protected void initView() {
        if (!GlobalData.userDataHelper.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activity_home_page);
        x.view().inject(this);
        //初始化右上角菜单悬浮窗
        initRecycleView();
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewPopupWindow = layoutInflater.inflate(R.layout.layout_mainpager_popup_menu, null);
        popupWindow = new PopupWindow(viewPopupWindow, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //右上角菜单
        TextView tv_right_fuction = (TextView) findViewById(R.id.tv_right_fuction);
        tv_right_fuction.setOnClickListener(this);
        //车源估价
        TextView textViewAppraisal = (TextView) viewPopupWindow.findViewById(R.id.tv_popup_menu_appraisal);
        textViewAppraisal.setOnClickListener(this);
        //线上收款
        TextView textViewReceipt = (TextView) viewPopupWindow.findViewById(R.id.tv_popup_menu_receipt);
        textViewReceipt.setOnClickListener(this);
        //出险记录
        TextView textDangerRecord = (TextView) viewPopupWindow.findViewById(R.id.tv_in_danger_record);
        textDangerRecord.setOnClickListener(this);
        //设置
        TextView textViewSetting = (TextView) viewPopupWindow.findViewById(R.id.tv_popup_menu_setting);
        textViewSetting.setOnClickListener(this);
        //初始化主界面数据
        initHomeData();
    }

    @Override
    public void onClick(View v) {
        if (popupWindow == null) {
            return;
        }
        Map params;
        switch (v.getId()) {
            case R.id.tv_right_fuction://右上角菜单
                if (!popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(v, 0, 0);
                }
                break;
            case R.id.tv_popup_menu_appraisal://车源估价
                params = new HashMap();
                params.put("url", "http://m.haoche51.com/vehicle_valuation?platform=checker_app");
                params.put(HCWebViewActivity.KEY_INTENT_EXTRA_VEHICLE_SOURCE_ESTIMATE, true);
                HCActionUtil.launchActivity(HomePageActivity.this, HCWebViewActivity.class, params);
                popupWindow.dismiss();
                break;

            case R.id.tv_popup_menu_receipt://线上收款
                OnlinePayIntent intent = new OnlinePayIntent(HomePageActivity.this);
                Checker checker = GlobalData.userDataHelper.getChecker();
                intent.setCrmUserId(String.valueOf(checker.getId()));
                intent.setCrmUserName(checker.getName());
                intent.setAppToken(checker.getApp_token());
                startActivity(intent);
                popupWindow.dismiss();
                break;
            case R.id.tv_in_danger_record://出险记录
                params = new HashMap();
                params.put("url", "http://app.open.chengniu.com/query-policy/index.html?channel=d085cd6fce5fe0e628fb939ed8e05ea0&clientId=" + GlobalData.userDataHelper.getChecker().getId());
                params.put(HCWebViewActivity.KEY_INTENT_EXTRA_DANGER, true);
                HCActionUtil.launchActivity(HomePageActivity.this, HCWebViewActivity.class, params);
                popupWindow.dismiss();
                break;
            case R.id.tv_popup_menu_setting://设置
                HCActionUtil.launchActivity(HomePageActivity.this, SettingActivity.class, null);
                popupWindow.dismiss();
                break;
        }
    }


    /**
     * 初始化主界面数据
     */
    protected void initHomeData() {
        //如果网络可用，联网请求
        if (NetInfoUtil.isNetAvaliable()) {
            ProgressDialogUtil.showProgressDialog(HomePageActivity.this, getString(R.string.hc_loading));
            //请求用户权限
            OKHttpManager.getInstance().post(HCHttpRequestParam.getUserRight(), HomePageActivity.this, 0);
        } else {//不可用，读取本地缓存
            //获取缓存权限
            mUserRights = GlobalData.userDataHelper.getUserRight();
            //设置用户权限
            loadUserRights();
        }
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //add tingyun analytics
//                ThirdPartInjector.startTingyun(HomePageActivity.this);
                //add umeng online update
                //start baidu location
                ThirdPartInjector.startBaiduLocation();
                //start baidu push
                ThirdPartInjector.startBaiduPush();
                //check update
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        HCLogUtil.e(tag, "onStart ==========================");
        startUpdateService();
    }

    /**
     * 启动更新服务
     */
    private void startUpdateService() {
        HCLogUtil.e(tag, "startUpdateService  service start==========================");
        Intent intent = new Intent(this, AutoUpdateVersionService.class);
        startService(intent);
    }

    /**
     * 设置用户权限视图
     */
    private void initRecycleView() {
        DisplayUtils.getInstance().setWelcome(textVieName);
        if (Debug.EVIROMENT == 1) {
            setScreenTitle("线上测试");
        } else if (Debug.EVIROMENT == 2) {
            setScreenTitle("线下测试");
        } else if (Debug.EVIROMENT == 3) {
            setScreenTitle("内部测试");
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //添加分隔线
        mRecyclerView.addItemDecoration(new GridItemDecoration(this, R.drawable.shape_divider));
        mHomePageAdapter = new HomePageAdapter(mDataList);
        mRecyclerView.setAdapter(mHomePageAdapter);
        itemClickListener = new RecyclerItemClickListener(this, this);
        mRecyclerView.addOnItemTouchListener(itemClickListener);
        swipe_layout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipe_layout.setOnRefreshListener(this);
    }


    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        if (!NetInfoUtil.isNetAvaliable()) {
            swipe_layout.setRefreshing(false);
            ToastUtil.showInfo("网络不给力，刷新失败!");
            return;
        }
        OKHttpManager.getInstance().post(HCHttpRequestParam.getUserRight(), HomePageActivity.this, 0);
    }

    /**
     * 加载主页权限界面
     */
    private void loadUserRights() {
        //清空原有的数据
        mDataList.clear();

        //根据权限设置页面规则
        //权限编码：101，验车，201: 过户,301: 车源推荐,401: 我的客户(看车), 501: 我的推荐, 601消息,701:收车 801 整备。901 回购车辆
        UserRightShortEntity userRightShort;
        if (mUserRights == null || mUserRights.size() <= 0) {
            userRightShort = new UserRightShortEntity(0, "保养记录", R.drawable.ic_maintenance_records, QueryReportActivity.class.getName());
            mDataList.add(userRightShort);
            notifyRecycleView();
            return;
        }
        Bundle params;
        for (UserRightEntity userRight : mUserRights) {
            if (userRight.getRight() < 1) {
                continue;
            }
            switch (userRight.getCode()) {
                case 101://验车
                    userRightShort = new UserRightShortEntity(userRight.getCode(), "验车", R.drawable.ic_vehicle_check, CheckMainActivity.class.getName());
                    mDataList.add(userRightShort);
                    break;
                case 501://推荐买家
//                    userRightShort = new UserRightShortEntity(userRight.getCode(), "推荐买家", R.drawable.ic_buyer_recommend, BuyerLeadTaskActivity.class.getName());
                    userRightShort = new UserRightShortEntity(userRight.getCode(), "我的推荐", R.drawable.ic_buyer_recommend, RecommendMainActivity.class.getName());
                    mDataList.add(userRightShort);
                    break;
                case 601://消息
                    userRightShort = new UserRightShortEntity(userRight.getCode(), "消息", R.drawable.ic_message, PushMessageActivity.class.getName());
                    mDataList.add(userRightShort);
                    break;
                case 701://收车
                    GlobalData.userDataHelper.setPurchaseRight(userRight.getRight());
                    userRightShort = new UserRightShortEntity(userRight.getCode(), "收车", R.drawable.ic_vehicle_purchase, PurchaseMainActivity.class.getName());
                    mDataList.add(userRightShort);
                    break;
                case 801://整备
                    userRightShort = new UserRightShortEntity(userRight.getCode(), "整备", R.drawable.ic_hostling, HostlingMainActivity.class.getName());
                    mDataList.add(userRightShort);
                    break;
                case 901://回购库存
                    userRightShort = new UserRightShortEntity(userRight.getCode(), "回购库存", R.drawable.ic_offlinesold, OfflineSoldMainActivity.class.getName());
                    mDataList.add(userRightShort);
                    break;
                case 1001://渠寄
                    GlobalData.userDataHelper.setChannelRight(userRight.getRight());
                    userRightShort = new UserRightShortEntity(userRight.getCode(), "渠寄", R.drawable.ic_channel, ChannelMainActivity.class.getName());
                    mDataList.add(userRightShort);
                    break;
//                case 1011://车源推荐
//                    userRightShort = new UserRightShortEntity(userRight.getCode(), "车源推荐", R.drawable.ic_recom, VehicleRecommendActivity.class.getName());
//                    mDataList.add(userRightShort);
//                    break;
                case 1021://库存盘点
                    userRightShort = new UserRightShortEntity(userRight.getCode(), "库存盘点", R.drawable.ic_stocking, CaptureActivity.class.getName());
                    mDataList.add(userRightShort);
                    break;
                case 1031://审批
                    userRightShort = new UserRightShortEntity(userRight.getCode(), "审批", R.drawable.ic_approve, ApproveMainActivity.class.getName());
                    mDataList.add(userRightShort);
                    break;
                case 1041://报价参考
                    userRightShort = new UserRightShortEntity(userRight.getCode(), "报价参考", R.drawable.ic_refer, OfferReferActivity.class.getName());
                    mDataList.add(userRightShort);
                    break;
                case 1051://回购线索
                    GlobalData.userDataHelper.setPurchaseClueRight(userRight.getRight());
                    break;
                case 1061://车源回访
                    userRightShort = new UserRightShortEntity(userRight.getCode(), "车源回访", R.drawable.ic_car_revisit, CRMWebViewActivity.class.getName());
                    params = new Bundle();
//                    HCLogUtil.e(tag, "http://crm.haoche51.com/VehicleVisitWap/index?token="+ GlobalData.userDataHelper.getChecker().getApp_token());
                    params.putBoolean(CRMWebViewActivity.KEY_INTENT_EXTRA_CAR_REVISIT, true);
                    userRightShort.setParams(params);
                    mDataList.add(userRightShort);
                    break;
                case 1071://工资明细
                    userRightShort = new UserRightShortEntity(userRight.getCode(), "工资明细", R.drawable.ic_salary, CRMWebViewActivity.class.getName());
                    params = new Bundle();
//                    params.putString("url", "http://crm.haoche51.com/subsidyWap/checker?token=" + GlobalData.userDataHelper.getChecker().getApp_token());
                    params.putBoolean(CRMWebViewActivity.KEY_INTENT_EXTRA_SALARY, true);
                    userRightShort.setParams(params);
                    mDataList.add(userRightShort);
                    break;
                case 1081://工作简报
                    userRightShort = new UserRightShortEntity(userRight.getCode(), "工作简报", R.drawable.ic_work_report, CRMWebViewActivity.class.getName());
                    params = new Bundle();
//                    HCLogUtil.e(tag, "http://crm.haoche51.com/VehicleVisitWap/index?token="+ GlobalData.userDataHelper.getChecker().getApp_token());
                    params.putBoolean(CRMWebViewActivity.KEY_INTENT_EXTRA_REPORT, true);
                    userRightShort.setParams(params);
                    mDataList.add(userRightShort);
                    break;
            }

        }

        Collections.sort(mDataList, new Comparator<UserRightShortEntity>() {
            @Override
            public int compare(UserRightShortEntity lhs, UserRightShortEntity rhs) {
                return lhs.getCode() - rhs.getCode();
            }
        });
        userRightShort = new UserRightShortEntity(0, "保养记录", R.drawable.ic_maintenance_records, QueryReportActivity.class.getName());
        mDataList.add(userRightShort);
        notifyRecycleView();
    }

    /**
     * 通知并刷新RecycleView
     */
    private void notifyRecycleView() {
        if (mHomePageAdapter != null) {
            mHomePageAdapter.notifyDataSetChanged();
        } else {
            mHomePageAdapter = new HomePageAdapter(mDataList);
            mRecyclerView.setAdapter(mHomePageAdapter);
        }
    }

    /**
     * HTTP 请求结果
     *
     * @param action    当前请求action
     * @param requestId
     * @param response  hc 请求结果
     * @param error     网络问题造成failed 的error
     */
    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        super.onHttpComplete(action, requestId, response, error);
        ProgressDialogUtil.closeProgressDialog();
        swipe_layout.setRefreshing(false);
        if (HttpConstants.ACTION_GET_USER_RIGHT.equals(action)) {
            switch (response.getErrno()) {
                case 0:
                    //网络请求成功
                    onResponseSuccess(response);
                    break;
                case -7:
                    CheckerApplication.logout();
                    break;
                default:
                    ToastUtil.showInfo(response.getErrmsg());
                    mUserRights = GlobalData.userDataHelper.getUserRight();
                    loadUserRights();
                    break;
            }
        }
    }

    /**
     * 获取用户权限成功
     *
     * @param response
     */
    private void onResponseSuccess(HCHttpResponse response) {
        try {
            mUserRights = JsonParseUtil.fromJsonArray(response.getData(), UserRightEntity.class);
            //将新获取的权限缓存起来
            GlobalData.userDataHelper.setUserRight(response.getData()).commit();
        } catch (Exception e) {
            mUserRights = GlobalData.userDataHelper.getUserRight();
            HCLogUtil.e(tag, e.getMessage());
        } finally {
            loadUserRights();
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        try {
            Intent intent = new Intent();
            UserRightShortEntity entity = mDataList.get(position);
            intent.setClassName(getPackageName(), entity.getClassName());
            if (entity.getParams() != null && entity.getParams().size() > 0) {
                intent.putExtras(entity.getParams());
            }
            startActivity(intent);
        } catch (Exception e) {
            HCLogUtil.e(tag, e.getMessage());
            ToastUtil.showInfo(e.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        if (swipe_layout != null && swipe_layout.isRefreshing()) {
            swipe_layout.setRefreshing(false);
        }

        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }

        super.onDestroy();
    }


}
