package com.haoche51.checker.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * RecyclerView Item点击监听器
 * Created by 彭祥林 on 2015/9/11.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    GestureDetector mGestureDetector;
    private OnItemClickListener mListener;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            //判断手势为点击
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        View item = recyclerView.findChildViewUnder(event.getX(), event.getY());
        if (item != null && mListener != null && mGestureDetector.onTouchEvent(event)) {
//      int position = recyclerView.getChildPosition(item);
      int position = recyclerView.getChildLayoutPosition(item);
            mListener.onItemClick(item, position);
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
