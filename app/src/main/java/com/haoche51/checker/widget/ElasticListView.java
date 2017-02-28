package com.haoche51.checker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haoche51.checker.R;

import java.util.Date;

/**
 * 彭祥林 ListView 控件
 *
 */
public class ElasticListView extends ListView implements OnScrollListener {

    private static final String TAG = ElasticListView.class.getSimpleName();

    public final static int RELEASE_To_REFRESH = 0;
    private final static int PULL_To_REFRESH = 1;
    private final static int REFRESHING = 2;
    private final static int DONE = 3;
    private final static int LOADING = 4;
    private final static int RATIO = 3;
    private final int FOOTER_VIEW_LOADING = 1;
    private final int FOOTER_VIEW_RETRY = 2;
    private final int FOOTER_VIEW_GONE = 3;
    private final int FOOTER_VIEW_LOADEND = 4;
    private LayoutInflater inflater;
    //	private LinearLayout headViewTop;
    private LinearLayout headView;
    public View mFooterView;
    private ProgressBar pb_loading;
    private View mFooterLoadingView;
    private View mFooterRetryView;
    private TextView loading_msg;
    private TextView tipsTextview, footer_loader_msg;
    private TextView lastUpdatedTextView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;
    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;
    private boolean isRecored;
    private int headContentWidth;
    private int headContentHeight;
    private int footerContentHeight;
    private int startY;
    private int firstItemIndex;
    public int state;
    private boolean isBack;
    public OnRefreshListener refreshListener;
    public OnLoadMoreListener loadMoreListener;
    private boolean isRefreshable;
    public boolean mEnd = false;

    public ElasticListView(Context context) {
        super(context);
        init(context);
    }

    public ElasticListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setCacheColorHint(context.getResources().getColor(R.color.hc_self_transparent));
        inflater = LayoutInflater.from(context);

//		headViewTop = new LinearLayout(getContext());
//		headViewTop.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
//				LayoutParams.WRAP_CONTENT));

        headView = (LinearLayout) inflater.inflate(
                R.layout.widget_elastic_listview_head, null);

        arrowImageView = (ImageView) headView
                .findViewById(R.id.head_arrowImageView);
        arrowImageView.setMinimumWidth(70);
        arrowImageView.setMinimumHeight(50);
        progressBar = (ProgressBar) headView
                .findViewById(R.id.head_progressBar);
        tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
        lastUpdatedTextView = (TextView) headView
                .findViewById(R.id.head_lastUpdatedTextView);

        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();
        headContentWidth = headView.getMeasuredWidth();


        headView.setPadding(0, -1 * headContentHeight, 0, 0);
        headView.invalidate();

        Log.v("size", "width:" + headContentWidth + " height:"
                + headContentHeight);

//		addHeaderView(headViewTop, null, false);
        addHeaderView(headView, null, false);
        setOnScrollListener(this);

        animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);

        state = DONE;
        isRefreshable = false;

        initFooterView(inflater);
    }

//	/** 其实这里就是你要添加头的方法 */
//	public void addHeaderView(View v) {
//		this.headViewTop.addView(v);
//		this.headViewTop.invalidate();
//	}

    private void initFooterView(LayoutInflater inflater) {
        mFooterView = inflater.inflate(R.layout.asset_list_footer, null);
        pb_loading = (ProgressBar) mFooterView.findViewById(R.id.pb_loading);
        loading_msg = (TextView) mFooterView.findViewById(R.id.loading_msg);
        mFooterLoadingView = mFooterView.findViewById(R.id.loading_layout);
        mFooterRetryView = mFooterView.findViewById(R.id.footer_retry_view);
        measureView(mFooterView);
        footerContentHeight = mFooterView.getMeasuredHeight();

        mFooterRetryView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setFooterViewState(FOOTER_VIEW_LOADING);
            }
        });
        addFooterView(mFooterView);
    }

    public void setFooterLoading() {
        setFooterViewState(FOOTER_VIEW_LOADING);
    }


    public void setFooterInvisible() {
        setFooterViewState(FOOTER_VIEW_GONE);
    }

    public void removeFooterView() {
        removeFooterView(mFooterView);
    }

    public void setFooterLoadEnd() {
        mFooterView.setVisibility(View.VISIBLE);
        loading_msg.setText("没有更多了");
        pb_loading.setVisibility(GONE);
        mFooterLoadingView.setVisibility(View.VISIBLE);
        mFooterRetryView.setVisibility(View.GONE);
    }

    private void setFooterViewState(int state) {

        if (mFooterView == null || mFooterLoadingView == null
                || mFooterRetryView == null)
            return;

        switch (state) {
            case FOOTER_VIEW_LOADING:
                loading_msg.setText("请稍后");
                mFooterView.setVisibility(View.VISIBLE);
                pb_loading.setVisibility(VISIBLE);
                mFooterLoadingView.setVisibility(View.VISIBLE);
                mFooterRetryView.setVisibility(View.GONE);
                break;
            case FOOTER_VIEW_RETRY:
                mFooterView.setVisibility(View.VISIBLE);
                mFooterLoadingView.setVisibility(View.GONE);
                mFooterRetryView.setVisibility(View.VISIBLE);
                break;
            case FOOTER_VIEW_GONE:
                mFooterView.setVisibility(View.GONE);
                mFooterLoadingView.setVisibility(GONE);
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        firstItemIndex = firstVisibleItem;

        if (getCount() - view.getLastVisiblePosition() <= 2) {
            onLoadMore();
        }

//        if (getCount() > TaskConstants.DEFAULT_SHOWTASK_COUNT && mEnd) {
//            mFooterView.setVisibility(View.VISIBLE);
//            mFooterLoadingView.setVisibility(View.VISIBLE);
//            mFooterRetryView.setVisibility(View.GONE);
//            InternalHandler.sendEmptyMessageDelayed(1, 3000);
//
//            invalidate();
//        }
    }

//     Handler InternalHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
////            mEnd = true;
////            setFooterInvisible();
//            setFooterInvisible();
//            invalidate();
//        }
//    };
//
//    InternalHandler handler;

    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        switch (scrollState) {
//            // 当不滚动时
//            case OnScrollListener.SCROLL_STATE_IDLE:
//                // 判断滚动到底部 && 没有了更多
//                if (getLastVisiblePosition() == getCount() - 1 && mEnd) {
//
//                    if (handler == null)
//                        handler = new InternalHandler();
//
//                    mEnd = false;
//                    setFooterLoading();
//                    handler.sendEmptyMessageDelayed(1, 3000);
//                }
//                break;
//        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (isRefreshable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (firstItemIndex == 0 && !isRecored) {
                        isRecored = true;
                        startY = (int) event.getY();
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (state != REFRESHING && state != LOADING) {
//                        if (state == DONE) {
//                        }
                        if (state == PULL_To_REFRESH) {
                            state = DONE;
                            changeHeaderViewByState();
                        }
                        if (state == RELEASE_To_REFRESH) {
                            state = REFRESHING;
                            changeHeaderViewByState();
                            onRefresh();
                        }
                    }

                    isRecored = false;
                    isBack = false;

                    break;

                case MotionEvent.ACTION_MOVE:

                    int tempY = (int) event.getY();

                    if (!isRecored && firstItemIndex == 0) {
                        isRecored = true;
                        startY = tempY;
                    }

                    if (state != REFRESHING && isRecored && state != LOADING) {

                        if (state == RELEASE_To_REFRESH) {

                            setSelection(0);

                            if (((tempY - startY) / RATIO < headContentHeight)
                                    && (tempY - startY) > 0) {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                            } else if (tempY - startY <= 0) {
                                state = DONE;
                                changeHeaderViewByState();
                            }
                        }
                        if (state == PULL_To_REFRESH) {

                            setSelection(0);

                            if ((tempY - startY) / RATIO >= headContentHeight) {
                                state = RELEASE_To_REFRESH;
                                isBack = true;
                                changeHeaderViewByState();
                            } else if (tempY - startY <= 0) {
                                state = DONE;
                                changeHeaderViewByState();
                            }
                        }

                        if (state == DONE) {
                            // YYH-552 getFirstVisiblePosition() == 0
                            if (tempY - startY > 0
                                    && getFirstVisiblePosition() == 0) {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                            }
                        }

                        if (state == PULL_To_REFRESH) {
                            headView.setPadding(0, -1 * headContentHeight
                                    + (tempY - startY) / RATIO, 0, 0);
                        }

                        if (state == RELEASE_To_REFRESH) {
                            headView.setPadding(0, (tempY - startY) / RATIO
                                    - headContentHeight, 0, 0);
                        }


                    }

                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    public void changeHeaderViewByState() {
        switch (state) {
            case RELEASE_To_REFRESH:
                arrowImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);
                tipsTextview.setText("松开可以刷新");
                break;
            case PULL_To_REFRESH:
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.VISIBLE);
                if (isBack) {
                    isBack = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(reverseAnimation);

                    tipsTextview.setText("下拉可以刷新");
                } else {
                    tipsTextview.setText("下拉可以刷新");
                }
                break;

            case REFRESHING:
                headView.setPadding(0, 0 + headContentHeight / 4, 0, 0);
                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
                tipsTextview.setText("加载中...");
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                break;
            case DONE:
                headView.setPadding(0, -1 * headContentHeight, 0, 0);

                progressBar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(R.drawable.ic_arrow_down);
                tipsTextview.setText("刷新完毕");
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setonRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        isRefreshable = true;
    }

    public void setonLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnUpOrCancelMotionEvent {
        void onMotionEvent(int MotionEvent);
    }


    public void onRefreshComplete() {
        state = DONE;
        lastUpdatedTextView.setText("更新于：" + new Date().toLocaleString());
        changeHeaderViewByState();
    }

    public void onLoadMoreComplete() {
        setFooterViewState(FOOTER_VIEW_GONE);

    }

    private void onRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    private void onLoadMore() {
        if (loadMoreListener != null) {
            loadMoreListener.onLoadMore();
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setAdapter(BaseAdapter adapter) {
        lastUpdatedTextView.setText("更新于：" + new Date().toLocaleString());
        super.setAdapter(adapter);
    }

    public void setRefreshable(boolean isRefreshable) {
        this.isRefreshable = isRefreshable;
    }
}