package com.haoche51.checker.activity.evaluate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.adapter.LocalCityCheckerAdapter;
import com.haoche51.checker.entity.LocalCheckerEntity;
import com.haoche51.checker.net.HCHttpCallback;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.ProgressDialogUtil;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.widget.SideBar;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 从本地评估师列表中选择评估师
 * Created by wufx on 16/3/2.
 */
public abstract class GetCheckerActivity extends Activity implements AdapterView.OnItemClickListener,
  SearchView.OnQueryTextListener, SearchView.OnCloseListener, HCHttpCallback {
  protected TextView mReturn;

  protected TextView mTitle;

  protected TextView mRightFaction;

  protected ListView mListView;

  protected TextView select_letter;

  protected SideBar side_bar;

  protected SearchView search_box;

  protected List<LocalCheckerEntity> mLocalCityCheckerlist = new ArrayList<>();//本地的评估师 用于存放所有的评估师
  protected LocalCityCheckerAdapter mAdapter;
  protected LocalCheckerEntity chooseChecker;//当前选中的评估师
  protected Handler handler = new Handler();
  private Call mCall;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_get_checker);
    //标题栏的view
    mReturn = (TextView) findViewById(R.id.tv_common_back);
    mTitle = (TextView) findViewById(R.id.tv_common_title);
    mRightFaction = (TextView) findViewById(R.id.tv_right_fuction);
    select_letter = (TextView) findViewById(R.id.select_letter);
    side_bar = (SideBar) findViewById(R.id.side_bar);
    search_box = (SearchView) findViewById(R.id.search_box);
    mListView = (ListView) findViewById(R.id.checker_list);

    initContentView();
    initTitleBar();
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

  public void initContentView() {

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
    mAdapter = new LocalCityCheckerAdapter(this
      , mLocalCityCheckerlist, R.layout.item_checker);
    mListView.setAdapter(mAdapter);
    mListView.setOnItemClickListener(this);

    ProgressDialogUtil.showProgressDialog(this, getString(R.string.later));
    mCall = loadData();
  }


  public void initTitleBar() {
    mRightFaction.setCompoundDrawables(null, null, null, null);
    mRightFaction.setVisibility(View.VISIBLE);
    mRightFaction.setText(R.string.common_title_commit);
    mRightFaction.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //确定按钮点击事件
        onConfirmClick(chooseChecker);
      }
    });

    //返回按钮
    mReturn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }


  @Override
  public void onHttpStart(String action, int requestId) {

  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    ProgressDialogUtil.closeProgressDialog();
  }

  @Override
  public void onHttpProgress(String action, int requestId, long bytesWritten, long totalSize) {

  }

  @Override
  public void onHttpRetry(String action, int requestId, int retryNo) {

  }

  @Override
  public void onHttpFinish(String action, int requestId) {

  }


  /**
   * 解析员工列表信息
   */
  protected void responseGetCheckerList(HCHttpResponse response) {
    switch (response.getErrno()) {
      case 0:
        if (!TextUtils.isEmpty(response.getData())) {
          //解析评估师list
//          List<LocalCheckerEntity> entities = new HCJsonParse().parseCheckerList(response.getData());
          List<LocalCheckerEntity> entities = JsonParseUtil.fromJsonArray(response.getData(),LocalCheckerEntity.class);
          if (entities != null){
            mLocalCityCheckerlist.addAll(entities);
            mAdapter.notifyDataSetChanged();
          }
        }
        break;
      default:
        ToastUtil.showInfo(response.getErrmsg());
        break;
    }
  }


  /**
   * 响应操作
   * @param response
   */
  protected  void responseOperate(HCHttpResponse response){
    switch (response.getErrno()) {
      case 0:
        ToastUtil.showInfo(getString(R.string.successful));
        finish();
        break;
      default:
        ToastUtil.showInfo(response.getErrmsg());
        break;
    }
  }

  @Override
  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    //获得当前adapter中的list数据
    List<LocalCheckerEntity> entities = ((LocalCityCheckerAdapter) adapterView.getAdapter()).getmList();
    //先将其与同事全部置为false 未选择
    for (LocalCheckerEntity entity : entities) {
      entity.setIsChoose(false);
    }
    //设置当前点击项为已选中
    entities.get(i).setIsChoose(true);
    mAdapter.notifyDataSetChanged();
    chooseChecker = entities.get(i);
  }

  @Override
  public boolean onQueryTextSubmit(String s) {
    chooseChecker = null;//将已选置为空
    mAdapter.filter(s, mLocalCityCheckerlist);
    return true;
  }

  @Override
  public boolean onQueryTextChange(String s) {
    chooseChecker = null;//将已选置为空
    mAdapter.filter(s, mLocalCityCheckerlist);
    return true;
  }

  @Override
  public boolean onClose() {
    mAdapter.setmList(mLocalCityCheckerlist);
    mAdapter.notifyDataSetChanged();
    return true;
  }

  /**
   * 点击确定按钮
   *
   * @param chooseChecker 选中的评估师
   */
  protected abstract void onConfirmClick(LocalCheckerEntity chooseChecker);

  /**
   * 加载数据
   *
   * @return
   */
  protected abstract Call loadData();

  @Override
  protected void onDestroy() {
    ProgressDialogUtil.closeProgressDialog();
    //如果请求还没完成，界面就被关闭了，那么就取消请求
    OKHttpManager.getInstance().cancelRequest(mCall);
    super.onDestroy();
  }
}
