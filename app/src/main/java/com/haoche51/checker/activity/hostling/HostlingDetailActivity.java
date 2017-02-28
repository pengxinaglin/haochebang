package com.haoche51.checker.activity.hostling;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonStateActivity;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.HostlingTaskEntity;
import com.haoche51.checker.fragment.hostling.HostlingUnderWayFragment;
import com.haoche51.checker.helper.ImageLoaderHelper;
import com.haoche51.checker.listener.HCTasksWatched;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * 整备详情
 * Created by mac on 16/01/18.
 */
public class HostlingDetailActivity extends CommonStateActivity {

    public static String INTENT_PUSH_SEND = "push_send";
    //提交整备、完成整备
    private final int  ADD_REPAIR = 30, REPAIR_OVER = 40, RESORTING = 50;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.tv_status)
    private TextView tv_status;
    @ViewInject(R.id.tv_worker_name)
    private TextView tv_worker_name;
    @ViewInject(R.id.tv_worker_phone)
    private TextView tv_worker_phone;
    @ViewInject(R.id.tv_task_num)
    private TextView tv_task_num;
    @ViewInject(R.id.tv_back_price)
    private TextView tv_back_price;
    @ViewInject(R.id.tv_sold_price)
    private TextView tv_sold_price;
    @ViewInject(R.id.tv_cheap_price)
    private TextView tv_cheap_price;
    @ViewInject(R.id.tv_plate_time)
    private TextView tv_plate_time;
    @ViewInject(R.id.tv_mile)
    private TextView tv_mile;
    @ViewInject(R.id.tv_transfer_count)
    private TextView tv_transfer_count;
    @ViewInject(R.id.tv_real_repair_free)
    private TextView tv_real_repair_free;
    @ViewInject(R.id.tv_repair_free)
    private TextView tv_repair_free;
    @ViewInject(R.id.ll_hostling_layout)
    private LinearLayout ll_hostling_layout;
    @ViewInject(R.id.ll_hostling_picture_layout)
    private LinearLayout ll_hostling_picture_layout;
    @ViewInject(R.id.ll_progress)
    private LinearLayout ll_progress;//任务进度
    @ViewInject(R.id.btn_negative)
    private Button btn_negative;
    @ViewInject(R.id.btn_positive)
    private Button btn_positive;
    private HostlingTaskEntity mTask;
    private int taskId;//根据任务Id去请求任务详情

    @Override
    protected int getContentView() {
        return R.layout.activity_hostling_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        setScreenTitle(R.string.hc_purchasetask_detail_title);
    }

    @Override
    protected void initData() {
        super.initData();
        //获取查询的任务id
        taskId = getIntent().getIntExtra("id", 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (taskId > 0) {
            ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
            //从推送进入此页面
            if (getIntent().getBooleanExtra(INTENT_PUSH_SEND, false))
                OKHttpManager.getInstance().post(HCHttpRequestParam.getBackRepair(0, taskId), this, 0);
            else//从整备列表进入此页面
                OKHttpManager.getInstance().post(HCHttpRequestParam.getBackRepair(taskId, 0), this, 0);
        }
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        if (!isFinishing())
            ProgressDialogUtil.closeProgressDialog();
         try{
             if (action.equals(HttpConstants.ACTION_GET_BACK_REPAIR)) {
                 responseTask(response);
             } else if (action.equals(HttpConstants.ACTION_BACK_NO_NEED_REPAIR)) {
                 responseNoNeedRepair(response);
             }
         }catch (Exception e){
             e.printStackTrace();
         }

    }

    /**
     * 处理请求任务详情
     */
    private void responseTask(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
//                mTask = new HCJsonParse().parseHostlingEntity(response.getData());
                mTask = JsonParseUtil.fromJsonObject(response.getData(), HostlingTaskEntity.class);
                if (mTask != null) {
                    setData(mTask);
                }
                break;
            default:
                ToastUtil.showInfo(response.getErrmsg());
                break;
        }
    }

    /**
     * 处理请求无需整备
     */
    private void responseNoNeedRepair(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
                ToastUtil.showInfo(getString(R.string.successful));
                Bundle bundle = new Bundle();
                bundle.putBoolean(TaskConstants.BINDLE_NO_HOSTLING, true);
                HCTasksWatched.getInstance().notifyWatchers(bundle);
                onStart();//重新刷新页面
                break;
            default:
                ToastUtil.showInfo(response.getErrmsg());
                break;
        }
    }

    /**
     * 设置UI界面数据
     */
    private void setData(HostlingTaskEntity data) {
        tv_title.setText(data.getTitle());
        tv_status.setText(data.getRepair_status_text());
        tv_status.setVisibility(View.VISIBLE);
        tv_worker_name.setText(data.getWorker_name() + "（地收）");
        tv_worker_phone.setText(data.getWorker_phone());
        tv_task_num.setText(data.getTask_num());
        tv_back_price.setText(data.getBack_price() + "元");
        tv_sold_price.setText(data.getSold_price() + "元");
        tv_cheap_price.setText(data.getCheap_price() + "元");
        tv_plate_time.setText(data.getPlate_time());
        tv_mile.setText(data.getMile() + "万公里");
        tv_transfer_count.setText(data.getTransfer_count() + "");

        switch (data.getRepair_status()) {
            case TaskConstants.REPAIR_STATUS_WAIT://待整备
                ll_hostling_layout.setVisibility(View.GONE);
                btn_negative.setText(getString(R.string.hc_hostling_noneed_repair));
                btn_negative.setVisibility(View.VISIBLE);
                btn_positive.setVisibility(View.VISIBLE);
                btn_positive.setText(getString(R.string.hc_hostling_add_repair));
                break;
            case TaskConstants.REPAIR_STATUS_NONE://无需整备
                ll_hostling_layout.setVisibility(View.GONE);
                btn_negative.setVisibility(View.GONE);
                btn_positive.setVisibility(View.VISIBLE);
                btn_positive.setText(getString(R.string.hc_hostling_resorting));
                break;
            case TaskConstants.REPAIR_STATUS_ING://整备中
                ll_hostling_layout.setVisibility(View.VISIBLE);
                //预计整备费用
                tv_real_repair_free.setText("预计整备费用总计");
                tv_repair_free.setText(data.getRepair_free() + "");
                btn_positive.setVisibility(View.VISIBLE);
                btn_positive.setText(getString(R.string.hc_hostling_complete));
                setProject(data);
                break;

            case TaskConstants.REPAIR_STATUS_OVER://整备完成
                ll_hostling_layout.setVisibility(View.VISIBLE);
                //实际整备费用
                tv_real_repair_free.setText("实际整备费用总计");
                tv_repair_free.setText(data.getReal_repair_free() + "");
                btn_positive.setVisibility(View.VISIBLE);
                btn_positive.setText(getString(R.string.hc_hostling_resorting));
                setProject(data);
                break;
            case TaskConstants.REPAIR_STATUS_WAIT_FIRST_CHECK://待初审
            case TaskConstants.REPAIR_STATUS_WAIT_NEXT_CHECK://待复审
                //预计整备费用
                tv_real_repair_free.setText("预计整备费用总计");
                tv_repair_free.setText(data.getRepair_free() + "");
                btn_negative.setVisibility(View.GONE);
                btn_positive.setVisibility(View.GONE);
                setProject(data);
                break;
        }

        //显示进度条
        List<HostlingTaskEntity.FlowChartEntity> flow_chart = data.getFlow_chart();
        if (flow_chart == null || flow_chart.isEmpty()) {
            //没有进度信息 不显示进度条
            ll_progress.setVisibility(View.GONE);
        } else {
            //去掉所有的进度，防止重复
            ll_progress.removeAllViews();
            ll_progress.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            for (int i = 0; i < flow_chart.size(); i++) {
                HostlingTaskEntity.FlowChartEntity entity = flow_chart.get(i);

                //创建一个进度
                LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.layout_recycling_progress_item, null);
                RadioButton radioButton = (RadioButton) linearLayout.findViewById(R.id.rb_progress_name);
                //设置进度名称
                radioButton.setText(entity.getText());
                //是否已完成
                setProgressColor(radioButton, entity.getIs_over());
                View v1 = linearLayout.findViewById(R.id.v_1);
                View v2 = linearLayout.findViewById(R.id.v_2);
                if (i < flow_chart.size() - 1) {
                    HostlingTaskEntity.FlowChartEntity nextEntity = flow_chart.get(i + 1);
                    if (entity.getIs_over() == 1) {
                        v1.setBackgroundColor(getResources().getColor(R.color.hc_task_fullpay));
                    } else {
                        v1.setBackgroundColor(getResources().getColor(R.color.hc_self_gray));
                    }

                    if (nextEntity.getIs_over() == 1) {
                        v2.setBackgroundColor(getResources().getColor(R.color.hc_task_fullpay));
                    } else {
                        v2.setBackgroundColor(getResources().getColor(R.color.hc_self_gray));
                    }
                    linearLayout.setLayoutParams(params);
                }
                //最后一个进度不显示进度条
                else if (i == flow_chart.size() - 1) {
                    v1.setVisibility(View.GONE);
                    v2.setVisibility(View.GONE);
                }
                //将创建的进度加到屏幕上
                ll_progress.addView(linearLayout);
            }
        }


    }

    /**
     * 设置进度节点的颜色
     *
     * @param radioButton
     * @param isOver
     */
    private void setProgressColor(RadioButton radioButton, int isOver) {
        switch (isOver) {
            case 0://未操作
                radioButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_unfinished, 0, 0);
                radioButton.setTextColor(getResources().getColor(R.color.hc_self_black));
                break;
            case 1://操作通过
                radioButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_pass, 0, 0);
                radioButton.setTextColor(getResources().getColor(R.color.hc_task_fullpay));
                break;
            case 2://操作失败
                radioButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_no_pass, 0, 0);
                radioButton.setTextColor(getResources().getColor(R.color.hc_recycling_finished));
                break;
        }
    }

    /**
     * 展示整备项目
     */
    private void setProject(HostlingTaskEntity data) {
        ll_hostling_picture_layout.removeAllViews();
        //获取屏幕宽度
        int width = getWindowManager().getDefaultDisplay().getWidth();
        List<HostlingTaskEntity.ProjectEntity> project = data.getProject();
        if (project != null && !project.isEmpty()) {
            for (int i = 0; i < project.size(); i++) {
                HostlingTaskEntity.ProjectEntity projectEntity = project.get(i);
                LinearLayout item = (LinearLayout) View.inflate(this, R.layout.layout_hostling_project, null);
                TextView tv_position = (TextView) item.findViewById(R.id.tv_position);
                tv_position.setText((i + 1) + "");
                TextView tv_project_name = (TextView) item.findViewById(R.id.tv_project_name);
                TextView tv_project_fee = (TextView) item.findViewById(R.id.tv_project_fee);
                //已经整备完成
                if (projectEntity.getIs_over() == 1) {
                    //实际整备费用
                    tv_project_fee.setText(projectEntity.getReal_price() + "元");
                    //如果是整备中展示预计费用
                    if (mTask.getRepair_status() == TaskConstants.REPAIR_STATUS_ING)
                        tv_project_fee.setText(projectEntity.getExpect_price() + "元");
                    tv_project_name.setText(projectEntity.getName() + "（" + (projectEntity.getNo_repair() == 0 ? "已整备" : "未整备") + "）");
                }
                //未提交整备完成
                else {
                    //预计整备费用
                    tv_project_fee.setText(projectEntity.getExpect_price() + "元");
                    tv_project_name.setText(projectEntity.getName() + "（未整备）");
                }
                //整备前图片
                List<String> pre_image = projectEntity.getPre_image();
                if (pre_image != null && !pre_image.isEmpty()) {
                    LinearLayout ll_photos = (LinearLayout) item.findViewById(R.id.ll_photos);
                    LinearLayout pothosLayout = new LinearLayout(this);
                    ll_photos.addView(pothosLayout);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    int photoWidth = (width - DisplayUtils.dip2px(this, 40)) / 3;
                    int photoHeight = (int) (photoWidth * 0.75);
                    LinearLayout.LayoutParams photoParams = new LinearLayout.LayoutParams(photoWidth - 30, photoHeight);
                    photoParams.setMargins(15, 15, 15, 15);
                    for (int j = 1; j <= pre_image.size(); j++) {
                        ImageView iv = new ImageView(this);
                        iv.setLayoutParams(photoParams);
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);
                        if (!TextUtils.isEmpty(pre_image.get(j - 1))) {
                            ImageLoaderHelper.displayImage(pre_image.get(j - 1), iv);
                        }
                        pothosLayout.addView(iv);
                        if (j % 3 == 0) {
                            pothosLayout = new LinearLayout(this);
                            pothosLayout.setOrientation(LinearLayout.HORIZONTAL);
                            pothosLayout.setLayoutParams(params);
                            ll_photos.addView(pothosLayout);
                        }
                    }
                }
                //整备后图片
                List<String> after_image = projectEntity.getAfter_image();
                if (after_image != null && !after_image.isEmpty()) {
                    //增加一条分割线
                    View splitLine = new View(this);
                    splitLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3));
                    splitLine.setBackgroundColor(getResources().getColor(R.color.hc_self_gray));
                    LinearLayout ll_photos = (LinearLayout) item.findViewById(R.id.ll_photos);
                    LinearLayout pothosLayout = new LinearLayout(this);
                    ll_photos.addView(splitLine);
                    ll_photos.addView(pothosLayout);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    int photoWidth = (width - DisplayUtils.dip2px(this, 40)) / 3;
                    int photoHeight = (int) (photoWidth * 0.75);
                    LinearLayout.LayoutParams photoParams = new LinearLayout.LayoutParams(photoWidth - 30, photoHeight);
                    photoParams.setMargins(15, 15, 15, 15);
                    for (int j = 1; j <= after_image.size(); j++) {
                        ImageView iv = new ImageView(this);
                        iv.setLayoutParams(photoParams);
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);
                        if (!TextUtils.isEmpty(after_image.get(j - 1))) {
                            ImageLoaderHelper.displayImage(after_image.get(j - 1), iv);
                        }
                        pothosLayout.addView(iv);
                        if (j % 3 == 0) {
                            pothosLayout = new LinearLayout(this);
                            pothosLayout.setOrientation(LinearLayout.HORIZONTAL);
                            pothosLayout.setLayoutParams(params);
                            ll_photos.addView(pothosLayout);
                        }
                    }
                }
                ll_hostling_picture_layout.addView(item);
            }
        }
    }

    /**
     * 无需整备
     */
    @Event(R.id.btn_negative)
    private void btn_negative(View v) {
        if (mTask == null) return;
        AlertDialogUtil.showStandardTitleMessageDialog(this, "提示", "确定无需整备吗？", "取消", "确定", new AlertDialogUtil.OnDismissListener() {
            @Override
            public void onDismiss(Bundle data) {
                //无需整备
                ProgressDialogUtil.showProgressDialog(HostlingDetailActivity.this, getString(R.string.later));
                OKHttpManager.getInstance().post(HCHttpRequestParam.backNoNeedRepair(mTask.getStock_id()), HostlingDetailActivity.this, 0);
            }
        });
    }

    /**
     * 提交整备/完成整备//再次整备
     */
    @Event(R.id.btn_positive)
    private void btn_positive(View v) {
        if (mTask == null) return;
        Intent intent;
        switch (mTask.getRepair_status()) {
            case TaskConstants.REPAIR_STATUS_WAIT:
                //提交整备
                intent = new Intent(this, SubmitHostlingActivity.class);
                intent.putExtra("taskId", mTask.getStock_id());
                intent.putExtra(SubmitHostlingActivity.INTENTY_REPAIR, mTask);
                startActivityForResult(intent, ADD_REPAIR);
                break;
            case TaskConstants.REPAIR_STATUS_ING://整备中
                //完成整备
                intent = new Intent(this, HostlingCompleteActivity.class);
                intent.putExtra(TaskConstants.INTENTY_REPAIR, mTask);
                startActivityForResult(intent, REPAIR_OVER);
                break;
            case TaskConstants.REPAIR_STATUS_OVER:
            case TaskConstants.REPAIR_STATUS_NONE:
                //再次整备
                intent = new Intent(this, SubmitHostlingActivity.class);
                intent.putExtra("taskId", mTask.getStock_id());
                intent.putExtra(SubmitHostlingActivity.INTENTY_REPAIR, mTask);
                //传递标记为再次整备
                intent.putExtra(SubmitHostlingActivity.INTENT_RESORTING, true);
                startActivityForResult(intent, RESORTING);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = new Bundle();
            switch (requestCode) {
                case ADD_REPAIR://提交整备
                    bundle.putBoolean(TaskConstants.BINDLE_COMMIT_HOSTLING, true);
                    HCTasksWatched.getInstance().notifyWatchers(bundle);
                    finish();
                    break;
                case REPAIR_OVER://完成整备
                    bundle.putBoolean(HostlingUnderWayFragment.BINDLE_PURCHASE_TASK, true);
                    HCTasksWatched.getInstance().notifyWatchers(bundle);
                    btn_positive.setVisibility(View.GONE);
                    break;
                case RESORTING://再次整备
                    bundle.putBoolean(TaskConstants.BINDLE_RESORTING_TASK, true);
                    HCTasksWatched.getInstance().notifyWatchers(bundle);
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.closeProgressDialog();
        super.onDestroy();
    }
}