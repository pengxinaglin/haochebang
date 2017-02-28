package com.haoche51.checker.util;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.helper.ResourceHelper;

/**
 * Created by mac on 15/11/6.
 */
public class ControlDisplayUtil {
    private static Context mContext;
    private static ResourceHelper resourceHelper = null;
    private static ControlDisplayUtil mControlDisplayUtil;

    private ControlDisplayUtil() {
    }

    public static ControlDisplayUtil getInstance() {
        synchronized (ControlDisplayUtil.class) {
            if (mControlDisplayUtil == null) {
                mControlDisplayUtil = new ControlDisplayUtil();
                mContext = GlobalData.mContext;
                resourceHelper = new ResourceHelper(mContext);
            }
        }
        return mControlDisplayUtil;
    }

    /**
     * 根据线索状态设置文案及字色
     */
    public void setTextAndColorByCluesStatus(int status, TextView tv_state) {
        //0初始 1处理中 2成功 3无效(提交时判断) 4无效(帮买标记)
        tv_state.setTextColor(mContext.getResources().getColor(R.color.white_B0));
        switch (status) {
            case TaskConstants.CLUESSTATUS_DEFAULT:
                tv_state.setText(resourceHelper.getString(R.string.cluesstatus_default));
                break;
            case TaskConstants.CLUESSTATUS_PROCESSING:
                tv_state.setText(resourceHelper.getString(R.string.cluesstatus_processing));
                break;
            case TaskConstants.CLUESSTATUS_SUCCESS:
                tv_state.setTextColor(mContext.getResources().getColor(R.color.success_green));
                tv_state.setText(resourceHelper.getString(R.string.cluesstatus_success));
                break;
            case TaskConstants.CLUESSTATUS_SUBMIT_INVALID:
            case TaskConstants.CLUESSTATUS_INVALID:
                tv_state.setText(resourceHelper.getString(R.string.cluesstatus_invalid));
                break;
            default:
                break;
        }
    }

    /**
     * 获得毁约状态
     */
    public String getTransCancelStatus(int status) {
        String result = "";
        switch (status) {
            case TaskConstants.CANCEL_TRANS_STATUS_NOTCONFIRM:
                result = resourceHelper.getString(R.string.breach_of_promise_to_confirm);
                break;
            case TaskConstants.CANCEL_TRANS_STATUS_CONFIRMED:
                result = resourceHelper.getString(R.string.already_breach_of_promise);
                break;
        }
        return result;
    }

    /**
     * 获得车源操作状态
     */
    public String getVehicleStatus(int vehicleStatus) {
        //1:待检 2:审核 3: 上线 4: 预定 5:售出 6:公司回购 7:车主售出
        String result = "";
        switch (vehicleStatus) {
            case 1:
                result = resourceHelper.getString(R.string.waiting_check);
                break;
            case 2:
                result = resourceHelper.getString(R.string.audit);
                break;
            case 3:
                result = resourceHelper.getString(R.string.online);
                break;
            case 4:
                result = resourceHelper.getString(R.string.reservation);
                break;
            case 5:
                result = resourceHelper.getString(R.string.sold);
                break;
            case 6:
                result = resourceHelper.getString(R.string.company_repurchase);
                break;
            case 7:
                result = resourceHelper.getString(R.string.seller_sold);
                break;
        }
        return result;
    }

    public float getHCTextSize(int x) {
        if (x == 1)
            return Float.parseFloat(resourceHelper.getString(R.string.hc_activity_image_upload_tab_font_switch_height));
        else
            return Float.parseFloat(resourceHelper.getString(R.string.hc_activity_image_upload_tab_font_switch_low));
    }

    public int getHCColor(int x) {
        if (x == 0)
            return resourceHelper.getColor(R.color.self_empty);
        else
            return resourceHelper.getColor(R.color.self_blue);
    }

    /**
     * 设置验车列表item状态
     */
    public void setCheckStatus(TextView textView, int checkStatus) {
        textView.setVisibility(View.VISIBLE);

        //当前筛选任务的状态任务进行状态. 0待检测 1取消 2检测中 3待上传 4成功检测
        switch (checkStatus) {
            case TaskConstants.CHECK_STATUS_PENDING://待检
                textView.setText(resourceHelper.getString(R.string.to_detected));
                break;
            case TaskConstants.CHECK_STATUS_CANCEL://取消
                textView.setText(resourceHelper.getString(R.string.canceled));
                break;
            case TaskConstants.CHECK_STATUS_ONGOING://检测中
                textView.setText(resourceHelper.getString(R.string.hc_check_status_ongoing));
                break;
            case TaskConstants.CHECK_STATUS_TOUPLOAD://待上传
                textView.setText(resourceHelper.getString(R.string.to_upload));
                break;
            case TaskConstants.CHECK_STATUS_SUCCESS://成功
                textView.setText(resourceHelper.getString(R.string.completed));
                break;
        }
    }


    /**
     * 设置底部按钮状态
     * @param btn_negative
     * @param btn_cancel
     * @param btn_positive
     * @param check_source
     * @param check_status
     * @param help_check_status
     */
    public void setCheckBtnStatus(Button btn_negative,Button btn_cancel, Button btn_positive, int check_source, int check_status, int help_check_status) {
        btn_cancel.setVisibility(View.GONE);
        switch (check_source) {
            case TaskConstants.CHECK_TASK_ASSISTANCE://帮检
                //帮检状态:0待检 1帮检失败 2完成帮检 3完成帮检并成交(在该状态下需要质保，因此需要上传报告)
                switch (help_check_status) {
                    case TaskConstants.HELP_CHECK_PENDING://待检
                        //任务没有被取消
                        if (check_status != TaskConstants.CHECK_STATUS_CANCEL) {
                            btn_positive.setText(resourceHelper.getString(R.string.p_helpcheck_result));
                            btn_positive.setVisibility(View.VISIBLE);
                            btn_negative.setVisibility(View.GONE);
                        }
                        break;
                    case TaskConstants.HELP_CHECK_FAILED:// 取消
                    case TaskConstants.HELP_CHECK_SUCCESS://完成
                        btn_positive.setVisibility(View.GONE);
                        btn_negative.setVisibility(View.GONE);
                        break;
                    case TaskConstants.HELP_CHECK_DEAL://完成帮检并成交 需要写报告
                        setOrdinaryCheckTaskBtnStatus(btn_negative, btn_cancel, btn_positive, check_status);
                        break;
                }
                if (check_status == TaskConstants.CHECK_STATUS_SUCCESS)
                    setOrdinaryCheckTaskBtnStatus(btn_negative, btn_cancel,btn_positive, check_status);
                break;
            default://普通验车任务
                setOrdinaryCheckTaskBtnStatus(btn_negative,btn_cancel,btn_positive,check_status);
                break;
        }
    }


    private void setOrdinaryCheckTaskBtnStatus(Button btn_negative,Button btn_cancel, Button btn_positive, int check_status) {
        //已经验车成功了 或者 已取消任务
        if (check_status == TaskConstants.CHECK_STATUS_SUCCESS || check_status == TaskConstants.CHECK_STATUS_CANCEL) {
            btn_positive.setVisibility(View.GONE);
            btn_negative.setVisibility(View.GONE);
            return;
        }

        btn_negative.setVisibility(View.VISIBLE);
        btn_positive.setVisibility(View.VISIBLE);
        //检测中
        if (check_status == TaskConstants.CHECK_STATUS_ONGOING) {
            btn_negative.setText(resourceHelper.getString(R.string.p_cancel_evaluation));
            btn_positive.setText(resourceHelper.getString(R.string.hc_check_write_report));
            return;
        }
        //待上传
        if (check_status == TaskConstants.CHECK_STATUS_TOUPLOAD) {
            btn_negative.setText(resourceHelper.getString(R.string.hc_check_modify_report));
            btn_cancel.setVisibility(View.VISIBLE);
            btn_positive.setText(resourceHelper.getString(R.string.hc_check_upload_report));
            return;
        }
        //初始状态
        btn_negative.setText(resourceHelper.getString(R.string.p_cancel_evaluation));
        btn_positive.setText(resourceHelper.getString(R.string.hc_check_start_report));

    }


    /**
     * 获取验车任务来源：1主站 2爬虫 3帮检 4收车复检 5渠道寄售 6展厅
     * 只显示 「渠寄」「回购」「帮检」
     */
    public void setCheckSource(TextView textView, int check_source) {
        switch (check_source) {
            case TaskConstants.CHECK_TASK_ASSISTANCE:
            case TaskConstants.CHECK_TASK_RECHECK:
            case TaskConstants.CHECK_TASK_CHANNEL:
                textView.setVisibility(View.VISIBLE);
                textView.setText(getCheckSourceText(check_source));
                break;
            case TaskConstants.CHECK_TASK_MAINSITE:
            case TaskConstants.CHECK_TASK_EXHIBITION:
            case TaskConstants.CHECK_TASK_SPIDER:
            default:
                textView.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 获取验车任务来源：1主站 2爬虫 3帮检 4收车复检 5渠道寄售 6展厅
     * 只显示 「渠寄」「回购」「帮检」
     */
    public String getCheckSourceText(int check_source) {
        switch (check_source) {
            case TaskConstants.CHECK_TASK_MAINSITE:
                return TaskConstants.CHECK_SOURCE_NORMAL;
            case TaskConstants.CHECK_TASK_SPIDER:
                return TaskConstants.CHECK_SOURCE_NORMAL;
            case TaskConstants.CHECK_TASK_ASSISTANCE:
                return TaskConstants.CHECK_SOURCE_ASSISTANCE;
            case TaskConstants.CHECK_TASK_RECHECK:
                return TaskConstants.CHECK_SOURCE_RECHECK;
            case TaskConstants.CHECK_TASK_CHANNEL:
                return TaskConstants.CHECK_SOURCE_CHANNEL;
            case TaskConstants.CHECK_TASK_EXHIBITION:
                return TaskConstants.CHECK_SOURCE_NORMAL;
            default:
                return TaskConstants.CHECK_SOURCE_NORMAL;
        }
    }

    /**
     * 根据付款状态改变界面展示
     *
     * @param status
     * @param iv_pay
     * @param tv_pay_stat
     * @param tv_reject_reason
     */
    public void changeDisplayStyle(int status, ImageView iv_pay, TextView tv_pay_stat, TextView tv_reject_reason) {
        switch (status) {
            case TaskConstants.APPLY_PAY_STATUS_APLYING:
                iv_pay.setBackgroundResource(R.drawable.wait);
                tv_pay_stat.setTextColor(GlobalData.mContext.getResources().getColor(R.color.ic_blue));
                break;
            case TaskConstants.APPLY_PAY_STATUS_REJECT:
                iv_pay.setBackgroundResource(R.drawable.wrong);
                tv_pay_stat.setTextColor(GlobalData.mContext.getResources().getColor(R.color.red));
                tv_reject_reason.setTextColor(GlobalData.mContext.getResources().getColor(R.color.red));
                break;
            case TaskConstants.APPLY_PAY_STATUS_PAYED:
                iv_pay.setBackgroundResource(R.drawable.right);
                tv_pay_stat.setTextColor(GlobalData.mContext.getResources().getColor(R.color.ic_green));
                break;
        }
    }
}
