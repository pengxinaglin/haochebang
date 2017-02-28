package com.haoche51.checker.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * fix listView in scrollview problem,
 * list all item in listview. make listview can not scroll
 *
 * @author xuhaibo
 */
public class CheckListView extends ListView {

    public CheckListView(Context context) {
        super(context);
    }

    public CheckListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
