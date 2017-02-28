package com.haoche51.checker.activity.evaluate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haoche51.checker.DAO.CheckReportDAO;
import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonBaseFragmentActivity;
import com.haoche51.checker.adapter.FillExamReportPageAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.CheckReportEntity;
import com.haoche51.checker.fragment.BaseReportFragment;
import com.haoche51.checker.fragment.evaluate.fillreport.AskFragment;
import com.haoche51.checker.fragment.evaluate.fillreport.CarbinFragment;
import com.haoche51.checker.fragment.evaluate.fillreport.CompositeFragment;
import com.haoche51.checker.fragment.evaluate.fillreport.DeviceFragment;
import com.haoche51.checker.fragment.evaluate.fillreport.InfoFragment;
import com.haoche51.checker.fragment.evaluate.fillreport.InnerFragment;
import com.haoche51.checker.fragment.evaluate.fillreport.OuterFragment;
import com.haoche51.checker.fragment.evaluate.fillreport.StartUpFragment;
import com.haoche51.checker.util.HCActionUtil;
import com.haoche51.checker.util.IntentExtraMap;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.util.UnixTimeUtil;
import com.haoche51.checker.widget.time.ScreenInfo;
import com.haoche51.checker.widget.time.WheelMain;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * 填写检测报告
 * Created by wfx on 2016/6/28.
 */
public class FillExamReportActivity extends CommonBaseFragmentActivity {

    @ViewInject(R.id.tv_common_back)
    private TextView tv_common_back;
    @ViewInject(R.id.tv_common_title)
    private TextView tv_common_title;
    @ViewInject(R.id.btn_info)
    private TextView btn_info;
    @ViewInject(R.id.btn_composite)
    private TextView btn_composite;
    @ViewInject(R.id.btn_cabin)
    private TextView btn_cabin;
    @ViewInject(R.id.btn_outer)
    private TextView btn_outer;
    @ViewInject(R.id.btn_inner)
    private TextView btn_inner;
    @ViewInject(R.id.btn_device)
    private TextView btn_device;
    @ViewInject(R.id.btn_start)
    private TextView btn_start;
    @ViewInject(R.id.btn_ask)
    private TextView btn_ask;
    @ViewInject(R.id.btn_positive)
    private Button btn_positive;
    @ViewInject(R.id.viewpager)
    private ViewPager mViewpager;
    private List<BaseReportFragment> mFragments;
    private TextView[] mTvs;
    private int reportId = 0;
    private CheckReportEntity mCheckReport;

    public CheckReportEntity getCheckReport() {
        return mCheckReport;
    }


    @Override
    protected void initView() {
        setContentView(R.layout.activity_fill_exam_report);
        x.view().inject(this);
        reportId = getIntent().getIntExtra("id", 0);
        tv_common_title.setText(getString(R.string.fill_exam_report));
        btn_positive.setText(getString(R.string.save_and_next));
        //返回时，保存当前页的数据
        tv_common_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBackBtn();
            }
        });
    }

    /**
     * 当点击返回按钮时
     */
    private void onClickBackBtn() {

        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (mCheckReport == null || mFragments==null) {
                    return;
                }

                BaseReportFragment fragment;
                for (int i = 0; i < mFragments.size(); i++) {
                    fragment = mFragments.get(i);
                    fragment.saveData();
                }

                CheckReportDAO.getInstance().update(reportId, mCheckReport);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        });
    }


    @Override
    protected void initData() {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                mCheckReport = CheckReportDAO.getInstance().get(reportId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initHeaderTvs();
                        initFragments();
                        initViewPager();
                    }
                });
            }
        });

    }

    private void initViewPager() {
        mViewpager.setAdapter(new FillExamReportPageAdapter(getSupportFragmentManager(), mFragments));
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeBtnsStyle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化所有的操作按钮
     */
    private void initHeaderTvs() {
        mTvs = new TextView[8];
        mTvs[0] = btn_info;
        mTvs[1] = btn_composite;
        mTvs[2] = btn_cabin;
        mTvs[3] = btn_outer;
        mTvs[4] = btn_inner;
        mTvs[5] = btn_device;
        mTvs[6] = btn_start;
        mTvs[7] = btn_ask;
        btn_info.setOnClickListener(new MyBtnOnClickListener());
        btn_composite.setOnClickListener(new MyBtnOnClickListener());
        btn_cabin.setOnClickListener(new MyBtnOnClickListener());
        btn_outer.setOnClickListener(new MyBtnOnClickListener());
        btn_inner.setOnClickListener(new MyBtnOnClickListener());
        btn_device.setOnClickListener(new MyBtnOnClickListener());
        btn_start.setOnClickListener(new MyBtnOnClickListener());
        btn_ask.setOnClickListener(new MyBtnOnClickListener());
    }

    /**
     * 初始化fragment
     */
    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(new InfoFragment());
        mFragments.add(new CompositeFragment());
        mFragments.add(new CarbinFragment());
        mFragments.add(new OuterFragment());
        mFragments.add(new InnerFragment());
        mFragments.add(new DeviceFragment());
        mFragments.add(new StartUpFragment());
        mFragments.add(new AskFragment());
    }

    /**
     * 修改按钮样式
     *
     * @param position
     */
    private void changeBtnsStyle(int position) {
        TextView btn;
        for (int i = 0; i < mTvs.length; i++) {
            btn = mTvs[i];
            if (i == position) {
                btn.setTextColor(getResources().getColor(R.color.hc_indicator));
                btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                btn.setBackgroundResource(R.drawable.bottom_line_light_blue);
            } else {
                btn.setTextColor(getResources().getColor(R.color.self_black));
                btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                btn.setBackgroundResource(R.color.hc_self_white);
            }
        }
    }

    /**
     * 选择登记/年检/交强险/商业险时间
     */
    public void selectTime(View v) {
        final TextView textView = (TextView) v;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_time));
        final View timerView = LayoutInflater.from(this).inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(this);
        final WheelMain wheelMain = new WheelMain(timerView, true);
        int currYear=UnixTimeUtil.getCurrYear();
        //选择登记时间 时间范围在07-16年之间 （当前年份）
        if (v.getId() == R.id.regist_time) {
            WheelMain.setSTART_YEAR(2007);
            WheelMain.setEND_YEAR(currYear);
        } else if (v.getId() == R.id.transfer_year) {
            //选择过户时间 时间范围04年截止到当前年份）
            WheelMain.setSTART_YEAR(2004);
            WheelMain.setEND_YEAR(currYear);
        } else {
            //选择其他时间 时间范围在07-18年之间（截止到当前年份+2）
            WheelMain.setSTART_YEAR(2007);
            WheelMain.setEND_YEAR(currYear+2);
        }
        wheelMain.screenheight = screenInfo.getHeight();
        Log.e("select_time", UnixTimeUtil.format(UnixTimeUtil.getUnixTime(textView.getText().toString(), "yyyy-MM")) + "");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if (!TextUtils.isEmpty(textView.getText().toString())) {
            year = UnixTimeUtil.getYear(UnixTimeUtil.getUnixTime(textView.getText().toString(), "yyyy-MM"));
            month = UnixTimeUtil.getMonth(UnixTimeUtil.getUnixTime(textView.getText().toString(), "yyyy-MM")) - 1;
        }
        wheelMain.initDateTimePicker(year, month);
        builder.setView(timerView);
        builder.setNegativeButton(getString(R.string.soft_update_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(textView.getText())) {
                    textView.setError("请填写日期");
                }
            }
        });
        builder.setPositiveButton(getString(R.string.button_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int transferTime = UnixTimeUtil.getUnixTime(wheelMain.getTime());
                textView.setText(UnixTimeUtil.format(transferTime, "yyyy-MM"));
                textView.setError(null);
            }
        });
        builder.show();
    }

    @Event(R.id.btn_positive)
    private void saveAndNext(View view) {
        int totalErrNum = 0;
        BaseReportFragment fragment;
        for (int i = 0; i < mFragments.size(); i++) {
            fragment = mFragments.get(i);
            fragment.saveData();
            if (i == 0 || i == mFragments.size() - 1) {
                fragment.setErrNum(0);
                fragment.setCheckReport(mCheckReport);
                if (!fragment.validateCheck()) {
                    mTvs[i].setError("");
                    fragment.setTouchValidate(true);
                    totalErrNum += fragment.getErrNum();
                } else {
                    mTvs[i].setError(null);
                }
            }
        }

        if (totalErrNum > 0) {
            ToastUtil.showInfo("请将红色叹号处的错误全部纠正后，再提交");
        } else {
            updateReportAndNext();
        }
    }

    /**
     * 保存报告
     */
    private void saveReport(){
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (mCheckReport == null || mFragments==null) {
                    return;
                }
               try{
                   BaseReportFragment fragment;
                   for (int i = 0; i < mFragments.size(); i++) {
                       fragment = mFragments.get(i);
                       fragment.saveData();
                   }

                   CheckReportDAO.getInstance().update(reportId, mCheckReport);
               }catch (Exception e){
                   e.printStackTrace();
               }

            }
        });
    }

    @Override
    protected void onStop() {
        saveReport();
        super.onStop();
    }

    /**
     * 确认报告完成时，更新报告的标记
     */
    private void updateReportAndNext() {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if (mCheckReport == null) {
                    return;
                }
                //本地标识为已完成了报告基本信息的录入
                mCheckReport.setComplete_check(TaskConstants.CHECK_TASK_ACHIEVE);
                final int result = CheckReportDAO.getInstance().update(reportId, mCheckReport);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result > 0) {
                            HCActionUtil.launchActivity(GlobalData.context, ImageUploadActivity.class, IntentExtraMap.putId(reportId));
                        } else {
                            ToastUtil.showInfo(getString(R.string.save_report_error));
                        }
                    }
                });
            }
        });
    }

    /**
     * 导航栏按钮点击事件
     */
    private class MyBtnOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            int position = 0;
            switch (v.getId()) {
                case R.id.btn_info:
                    position = 0;
                    break;
                case R.id.btn_composite:
                    position = 1;
                    break;
                case R.id.btn_cabin:
                    position = 2;
                    break;
                case R.id.btn_outer:
                    position = 3;
                    break;
                case R.id.btn_inner:
                    position = 4;
                    break;
                case R.id.btn_device:
                    position = 5;
                    break;
                case R.id.btn_start:
                    position = 6;
                    break;
                case R.id.btn_ask:
                    position = 7;
                    break;
            }
            mViewpager.setCurrentItem(position);
        }
    }

}
