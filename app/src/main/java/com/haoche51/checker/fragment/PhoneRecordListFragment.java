package com.haoche51.checker.fragment;

import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.haoche51.checker.GlobalData;
import com.haoche51.checker.R;
import com.haoche51.checker.adapter.ListenRecordAdapter;
import com.haoche51.checker.constants.TaskConstants;
import com.haoche51.checker.entity.RecordEntity;
import com.haoche51.checker.net.HCHttpRequestParam;
import com.haoche51.checker.net.HCHttpResponse;
import com.haoche51.checker.net.HttpConstants;
import com.haoche51.checker.util.JsonParseUtil;
import com.haoche51.checker.net.OKHttpManager;
import com.haoche51.checker.util.DisplayUtils;
import com.haoche51.checker.util.ToastUtil;
import com.haoche51.checker.widget.ElasticListView;
import com.haoche51.checker.widget.PlayerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 车主录音、买家录音
 * Created by mac on 15/11/11.
 */
public class PhoneRecordListFragment extends CommonBaseFragment implements ElasticListView.OnRefreshListener,
  ElasticListView.OnLoadMoreListener {

  private ElasticListView mListView;
  private PlayerView playerView;

  private List<RecordEntity> mList;
  private ListenRecordAdapter mAdapter;
  private boolean mLoadEnd = false;
  private int pageIndex = 0;//当前页面索引
  private String phone;//根据电话查询
  private String taskId;//根据电话查询

  private int taskType  ;
  public PhoneRecordListFragment() {
  }

  public static PhoneRecordListFragment newInstance(String phone, String taskId,int taskType) {
    PhoneRecordListFragment fragment = new PhoneRecordListFragment();
    fragment.setPhoneNumber(phone);
    fragment.setTaskId(taskId);
    fragment.setTaskType(taskType);
    return fragment;
  }

  public void setPhoneNumber(String phone) {
    this.phone = phone;
  }

  public String getPhoneNumber() {
    return this.phone;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }
  public int getTaskType() {
    return taskType;
  }

  public void setTaskType(int taskType) {
    this.taskType = taskType;
  }

  @Override
  protected int getContentView() {
    return R.layout.fragment_phone_record_list;
  }

  @Override
  protected void initView(View view) {
    super.initView(view);
    if (TextUtils.isEmpty(phone)) {
      ToastUtil.showInfo(getString(R.string.lack_parameter));
    }
    mListView = (ElasticListView) view.findViewById(R.id.mElasticListView);
    playerView = (PlayerView) view.findViewById(R.id.play_view_activity_listen_record);
    mListView.setDivider(new ColorDrawable(getResources().getColor(R.color.hc_self_gray_bg)));
    mListView.setDividerHeight(DisplayUtils.dip2px(getActivity(), 10));
    mListView.setonRefreshListener(this);
    mListView.setonLoadMoreListener(this);
    if (mList == null)
      mList = new ArrayList<>();
    if (mAdapter == null) {
      mAdapter = new ListenRecordAdapter(GlobalData.mContext, mList, new ListenRecordAdapter.PlayListener() {
        @Override
        public void play(String url) {
          playerView.playUrl(url);
          playerView.setVisibility(View.VISIBLE);
        }
      });
      mListView.setAdapter(mAdapter);

    }
    //获取数据
    onRefreshComplete();
    onLoadMoreComplete();
    loadData();

    //初始化播放器
    playerView.setMediaPlayer(new MediaPlayer());
  }

  @Override
  protected void initData(Bundle savedInstanceState) {
    super.initData(savedInstanceState);
  }

  /**
   * 加载list数据
   */
  private void loadData() {
    new Handler().post(new Runnable() {
      @Override
      public void run() {
        dismissResultView();
        showLoadingView(true);
      }
    });
    pageIndex = 0;
    mLoadEnd = false;
    OKHttpManager.getInstance().post(HCHttpRequestParam.getPhoneRecordList(pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT,
      phone, null), this, HttpConstants.GET_LIST_REFRESH);
  }

  /**
   * 加载下一页list数据
   */
  private void loadMore() {
    new Handler().post(new Runnable() {
      @Override
      public void run() {
        dismissResultView();
        showLoadingView(true);
      }
    });
    //当前页面索引值++
    pageIndex += 1;
    mLoadEnd = true;
    mListView.setFooterLoading();
    OKHttpManager.getInstance().post(HCHttpRequestParam.getPhoneRecordList(pageIndex, TaskConstants.DEFAULT_SHOWTASK_COUNT,
      phone, null), this, HttpConstants.GET_LIST_LOADMORE);
  }

  @Override
  public void onRefresh() {
    if (!TextUtils.isEmpty(phone)) {
      //条件判断通过，允许刷新，重置参数，请求刷新；
      loadData();
    } else {
      onRefreshComplete();
    }
    //重置录音播放器
    playerView.stop();
    playerView.setVisibility(View.GONE);
  }

  @Override
  public void onLoadMore() {
    if (mLoadEnd || mList == null || mList.size() < TaskConstants.DEFAULT_SHOWTASK_COUNT)
      return;
    loadMore();
  }

  private void onRefreshComplete() {
    mListView.onRefreshComplete();
    dismissLoadingView();
  }

  private void onLoadMoreComplete() {
    mListView.onLoadMoreComplete();
    dismissLoadingView();
  }

  @Override
  public void onHttpComplete(String action, int requestId, HCHttpResponse response, Throwable error) {
    try {
      if (requestId == HttpConstants.GET_LIST_REFRESH) {
        onRefreshComplete();
      } else {
        onLoadMoreComplete();
      }

      switch (response.getErrno()) {
        case 0:
          //刷新
          if (requestId == HttpConstants.GET_LIST_REFRESH) {
//            List<RecordEntity> entities = new HCJsonParse().parseRecordLists(response.getData());
            List<RecordEntity> entities = JsonParseUtil.fromJsonArray(response.getData(), RecordEntity.class);
            mList.clear();
            if (entities != null) {
              mList.addAll(entities);
            }
            if (mList.size() == 0) {
              showNoDataView(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  loadData();
                }
              });
              return;
            }
            //有数据包，但数据包解析出来的数据为null或不够10条
            if (mList == null || mList.size() < TaskConstants.DEFAULT_SHOWTASK_COUNT) {//如果小于10条
              mListView.setFooterInvisible();//不显示加载更多了
            }
          } else {
            //加载更多
            //把后来加载的添加进来
//            List<RecordEntity> list = new HCJsonParse().parseRecordLists(response.getData());//不缓存
            List<RecordEntity> list = JsonParseUtil.fromJsonArray(response.getData(), RecordEntity.class);//不缓存
            if (list != null)
              mList.addAll(list);
            //没有数据或没有更多了
            if (list == null || list.size() < TaskConstants.DEFAULT_SHOWTASK_COUNT) {//如果小于10条
              mListView.setFooterLoadEnd();
              mLoadEnd = true;
            } else
              mLoadEnd = false;
          }
          mAdapter.notifyDataSetChanged();
          break;
        default:
          ToastUtil.showInfo(response.getErrmsg());
          if (requestId == HttpConstants.GET_LIST_REFRESH) {
            showErrorView(true, "网络异常", new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                loadData();
              }
            });
          }
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void pausePlay() {
    if (playerView != null) {
      playerView.pause();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (playerView != null) {
      playerView.playerDestory();
    }
  }
}
