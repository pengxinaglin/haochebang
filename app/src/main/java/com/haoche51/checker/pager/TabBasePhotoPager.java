package com.haoche51.checker.pager;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haoche51.checker.R;
import com.haoche51.checker.adapter.PhotoAdapter;
import com.haoche51.checker.entity.MediaEntity;
import com.haoche51.checker.entity.PhotoEntity;
import com.haoche51.checker.listener.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mac on 15/9/11.
 */
public abstract class TabBasePhotoPager implements RecyclerItemClickListener.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    public Activity mActivity;
    public TextView title;
    public SwipeRefreshLayout refresh;
    public RecyclerView recycler;
    public RecyclerItemClickListener itemClickListener;
    public LinearLayout emptyView;
    public TextView photoType;
    public Button openAlbum;
    public List<PhotoEntity> photoPaths = new ArrayList<>();
    public MediaEntity audioEntity;
    public MediaEntity videoEntity;
    public PhotoAdapter mAdapter;
    public int REQUEST_CODE;
    public OnPhotoListChangedListener onPhotoListChangedListener;
    protected View rootView;

    public TabBasePhotoPager(Activity mActivity) {
        this.mActivity = mActivity;
        rootView = initView();
        initData();
    }

    public View initView() {
        View view = View.inflate(this.mActivity, R.layout.tab_base_selectphoto_pager, null);
        this.refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        this.refresh.setOnRefreshListener(this);
        this.refresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        this.recycler = (RecyclerView) view.findViewById(R.id.recycler_contract_photo);
        initRecyclerView();

        this.title = (TextView) view.findViewById(R.id.title);
        this.emptyView = (LinearLayout) view.findViewById(R.id.emptyView);
        this.photoType = (TextView) view.findViewById(R.id.photoType);
        this.openAlbum = (Button) view.findViewById(R.id.openAlbum);
        this.openAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlbum(view);
            }
        });
        return view;
    }

    public int getREQUEST_CODE() {
        return REQUEST_CODE;
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this.mActivity, 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.recycler.setLayoutManager(layoutManager);
        this.recycler.setHasFixedSize(true);
        this.itemClickListener = new RecyclerItemClickListener(this.mActivity, this);
        this.recycler.addOnItemTouchListener(this.itemClickListener);
    }

    public View getRootView() {
        return this.rootView;
    }

    public void initData() {
    }

    public void initUI() {
    }

    /**
     * 打开相册
     */
    public abstract void openAlbum(View v);

    /**
     * 删除
     */
    public void deletePhoto() {
    }

    /**
     * 取消删除
     */
    public void cancelDeletePhoto() {
    }

    /**
     * 删除完成
     */
    public void deletePhotoFinish() {
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public void onRefresh() {
        refresh.setRefreshing(false);//TODO 暂时不需要刷新功能
    }

    public void notifyDataChanged(List<String> photos) {
    }

    public void chooseAudio(MediaEntity audioInfo) {
    }

    /**
     * 选择音频
     *
     * @param videoEntity
     */
    public void chooseVideo(MediaEntity videoEntity) {
    }

    public OnPhotoListChangedListener getOnPhotoListChangedListener() {
        return onPhotoListChangedListener;
    }

    public void setOnPhotoListChangedListener(OnPhotoListChangedListener onPhotoListChangedListener) {
        this.onPhotoListChangedListener = onPhotoListChangedListener;
    }

    public List<PhotoEntity> getPhotoPaths() {
        return photoPaths;
    }

    public void addOnitemClickListener() {
        recycler.addOnItemTouchListener(this.itemClickListener);
    }

    public void removeOnItemTouchListener() {
        recycler.removeOnItemTouchListener(this.itemClickListener);
    }

    public interface OnPhotoListChangedListener {
        void onChange();

        void onDeleteOrTagChange(String tagName);
    }
}
