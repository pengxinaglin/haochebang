package com.haoche51.checker.activity.purchase;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.activity.widget.CommonTitleBaseActivity;
import com.haoche51.checker.adapter.LocalCityWorkerAdapter;
import com.haoche51.checker.entity.SameCityWorkerEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.AlertDialogUtil;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.widget.SideBar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 收车转单
 * Created by mac on 16/01/21.
 */
public class TransmitPurchaseTaskActivity extends CommonTitleBaseActivity implements AdapterView.OnItemClickListener,
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    public static String BINDLE_OLD_CRM_USER_ID = "bindle_old_crm_user_id", BINDLE_OLD_CRM_USER_NAME = "bindle_old_crm_user_name";
    @ViewInject(R.id.checker_list)
    private ListView mListView;
    @ViewInject(R.id.select_letter)
    private TextView select_letter;
    @ViewInject(R.id.side_bar)
    private SideBar side_bar;
    @ViewInject(R.id.search_box)
    private SearchView search_box;
    private int taskId;
    private List<SameCityWorkerEntity> mLocalCityCheckerlist = new ArrayList<>();//本地的地收 用于存放所有的地收人员
    private LocalCityWorkerAdapter mAdapter;
    private SameCityWorkerEntity chooseWorker;//当前选中的地收
    private int old_crm_userid;
    private String old_crm_username;
    /**
     * 是否是分配
     */
    private boolean isAssign;
    private Handler handler = new Handler();

    @Override
    public View getHCContentView() {
        return View.inflate(this, R.layout.activity_transmit_checktask, null);
    }

    private void showIndex(String word) {
        select_letter.setVisibility(View.VISIBLE);
        select_letter.setText(word);
        //每次显示前先取出让它消失的任务
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                select_letter.setVisibility(View.GONE);
            }
        }, 1500);
    }

    @Override
    public void initContentView(Bundle saveInstanceState) {
        x.view().inject(this);
        search_box.setOnQueryTextListener(this);
        search_box.setOnCloseListener(this);
        side_bar.setTextView(select_letter);
        side_bar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                showIndex(s);//显示当前触摸滑动的位置
                for (int i = 0; i < mLocalCityCheckerlist.size(); i++) {
                    String firstWord = mLocalCityCheckerlist.get(i).getFirst_char();
                    if (firstWord.equals(s)) {
                        mListView.setSelection(i);//只需要第一个首字母为当前触摸字母的索引
                        break;
                    }
                }
            }
        });
        mAdapter = new LocalCityWorkerAdapter(this
                , mLocalCityCheckerlist, R.layout.item_checker);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        taskId = getIntent().getIntExtra("taskId", 0);
        old_crm_userid = getIntent().getIntExtra(BINDLE_OLD_CRM_USER_ID, 0);
        old_crm_username = getIntent().getStringExtra(BINDLE_OLD_CRM_USER_NAME);
        isAssign = getIntent().getBooleanExtra("isAssign", false);
        if (taskId == 0) {
            ToastUtil.showInfo("缺少参数");
            return;
        }

        //如果非待分配，则检查参数
        if (!isAssign && (old_crm_userid == 0 || TextUtils.isEmpty(old_crm_username))) {
            ToastUtil.showInfo("缺少参数");
            return;
        }
        ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
        //请求获取本地评估师
        OKHttpManager.getInstance().post(HCHttpRequestParam.getBackSameCityWorker(), this, 0);
    }

    @Override
    public void initTitleBar(TextView mReturn, TextView mTitle, TextView mRightFaction) {
        mTitle.setText(getString(R.string.worker_list));
        mRightFaction.setCompoundDrawables(null, null, null, null);
        mRightFaction.setVisibility(View.VISIBLE);
        mRightFaction.setText(R.string.hc_common_save);
        mRightFaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否已选择
                if (chooseWorker == null) {
                    ToastUtil.showInfo(getString(R.string.choose_acceptor));
                    return;
                }
                //二次确认
                if (isAssign) {
                    //分配确认
                    reConfirm(getString(R.string.assign_ok), getString(R.string.assign_to_choose_checker, chooseWorker.getName()));
                } else {
                    if(old_crm_userid==chooseWorker.getId()){
                        ToastUtil.showInfo("不能转给自己，请重新选择");
                        return;
                    }
                    //转单确认
                    reConfirm(getString(R.string.transmit_ok), getString(R.string.transmit_to_choose_checker, chooseWorker.getName()));
                }

            }
        });
    }

    /**
     * 二次确认
     *
     * @param title   标题
     * @param content 内容
     */
    private void reConfirm(String title, String content) {
        AlertDialogUtil.showStandardTitleMessageDialog(TransmitPurchaseTaskActivity.this, title,
                content, getString(R.string.transmit_cancel),
                getString(R.string.transmit_confirm), new AlertDialogUtil.OnDismissListener() {
                    @Override
                    public void onDismiss(Bundle data) {
                        //确定转单
                        OKHttpManager.getInstance().post(HCHttpRequestParam.changeBackWorker(taskId, old_crm_userid, old_crm_username, chooseWorker.getId(), chooseWorker.getName()), TransmitPurchaseTaskActivity.this, 0);
                    }
                });
    }

    @Override
    public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
        if (action.equals(HttpConstants.ACTION_GET_BACK_SAME_CITY_WORKER)) {
            responseBackWorkerList(response);
        } else if (action.equals(HttpConstants.ACTION_CHANGE_BACK_WORKER)) {
            responseChangeBackWorker(response);
        }
        ProgressDialogUtil.closeProgressDialog();
    }

    /**
     * 处理请求转单
     */
    private void responseChangeBackWorker(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
                ToastUtil.showInfo(getString(R.string.successful));
                setResult(RESULT_OK);
                finish();
                break;
            default:
                ToastUtil.showInfo(response.getErrmsg());
                break;
        }
    }

    /**
     * 解析员工列表信息
     */
    private void responseBackWorkerList(HCHttpResponse response) {
        switch (response.getErrno()) {
            case 0:
                if (!TextUtils.isEmpty(response.getData())) {
                    //解析地收list
//                    List<SameCityWorkerEntity> entities = new HCJsonParse().parseSameCityWorkertEntitys(response.getData());
                    List<SameCityWorkerEntity> entities = JsonParseUtil.fromJsonArray(response.getData(), SameCityWorkerEntity.class);
                    if (entities != null)
                        mLocalCityCheckerlist.addAll(entities);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            default:
                ToastUtil.showInfo(response.getErrmsg());
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //获得当前adapter中的list数据
        List<SameCityWorkerEntity> entities = ((LocalCityWorkerAdapter) adapterView.getAdapter()).getmList();
        //先将其与同事全部置为false 未选择
        for (SameCityWorkerEntity entity : entities) {
            entity.setChoose(false);
        }
        //设置当前点击项为已选中
        entities.get(i).setChoose(true);
        mAdapter.notifyDataSetChanged();
        chooseWorker = entities.get(i);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        chooseWorker = null;//将已选置为空
        mAdapter.filter(s, mLocalCityCheckerlist);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        chooseWorker = null;//将已选置为空
        mAdapter.filter(s, mLocalCityCheckerlist);
        return true;
    }

    @Override
    public boolean onClose() {
        mAdapter.setmList(mLocalCityCheckerlist);
        mAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    protected void onDestroy() {
        ProgressDialogUtil.closeProgressDialog();
        super.onDestroy();
    }
}
