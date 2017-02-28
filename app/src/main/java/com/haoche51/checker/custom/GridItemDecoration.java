package com.haoche51.checker.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * 网格分隔线
 * Created by wfx on 2016/7/12.
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration{
//    private static final int[] ATTRS = new int[] {android.R.attr.listDivider};
    private Drawable mDivider;


    public GridItemDecoration(Context context, int resId)
    {
//        final TypedArray a = context.obtainStyledAttributes(ATTRS);
//        mDivider = a.getDrawable(0);
//        a.recycle();
         mDivider = context.getResources().getDrawable(resId);
//         mDivider = context.getResources().getDrawable(android.R.attr.listDivider);
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontalDivider(c,parent);
        drawVerticalDivider(c,parent);
    }

    /**
     * 画水平分隔线
     * @param c
     * @param parent
     */
    public void drawHorizontalDivider(Canvas c, RecyclerView parent){
        int childCount = parent.getChildCount();
        for(int i=0; i<childCount; i++){
            View child=parent.getChildAt(i);
            RecyclerView.LayoutParams params= (RecyclerView.LayoutParams) child.getLayoutParams();
            int left=child.getLeft()-params.leftMargin;
            int right = child.getRight()+params.rightMargin+mDivider.getIntrinsicWidth();
            int top = child.getBottom()+params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 画垂直分隔线
     * @param c
     * @param parent
     */
    public void drawVerticalDivider(Canvas c, RecyclerView parent){
        int childCount = parent.getChildCount();
        for(int i=0; i<childCount; i++){
            View child=parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left=child.getRight()+params.rightMargin;
            int right = left+mDivider.getIntrinsicWidth();
            int top = child.getTop()-params.topMargin;
            int bottom = child.getBottom()+params.bottomMargin;
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }



    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        if(isLastRow(parent,itemPosition,spanCount,childCount)){//如果是最后一行，则不需要绘制底部
            outRect.set(0,0,mDivider.getIntrinsicWidth(),0);
        }else if(isLastColum(parent,itemPosition,spanCount,childCount)){//如果是最后一列，则不需要绘制右边
            outRect.set(0,0,0,mDivider.getIntrinsicHeight());
        }else{
            outRect.set(0,0,mDivider.getIntrinsicWidth(),mDivider.getIntrinsicHeight());
        }
    }

    /**
     * 判断是否是最后一列
     * @param parent
     * @param position 位置
     * @param spanCount 列数
     * @param childCount 子控件个数
     * @return
     */
    private boolean isLastColum(RecyclerView parent, int position, int spanCount, int childCount){
        RecyclerView.LayoutManager layoutManager=parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            if((position+1)%spanCount==0){//如果是最后一列，则不需要绘制右边
                return true;
            }
        }else if(layoutManager instanceof StaggeredGridLayoutManager){
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if(orientation==StaggeredGridLayoutManager.VERTICAL){
               if((position+1)%spanCount==0){//如果是最后一列，则不需要绘制右边
                   return true;
               }
            }else{
                childCount=childCount-childCount%spanCount;
                if(position>=childCount){//如果是最后一列，则不需要绘制右边
                   return true;
                }
            }
        }

        return false;
    }


    /**
     * 判断是否是最后一行
     * @param parent
     * @param position
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastRow(RecyclerView parent, int position, int spanCount, int childCount){
        //获取总行数
        int rowCount;
        if(childCount%spanCount==0){
            rowCount=childCount/spanCount;
        }else {
            rowCount=childCount/spanCount+1;
        }
        RecyclerView.LayoutManager layoutManager=parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            if((position+1)> (rowCount-1)*spanCount){//最后一行，则不需绘制底边
               return true;
            }
        }else if(layoutManager instanceof StaggeredGridLayoutManager){
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if(orientation==StaggeredGridLayoutManager.HORIZONTAL){
                if((position+1)> (rowCount-1)*spanCount){//最后一行，则不需绘制底边
                    return true;
                }
            }else{
                if((position+1)%spanCount==0){//最后一行，则不需绘制底边
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 获取列数
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent){
        int spanCount=-1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }else if(layoutManager instanceof StaggeredGridLayoutManager){
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }



}
