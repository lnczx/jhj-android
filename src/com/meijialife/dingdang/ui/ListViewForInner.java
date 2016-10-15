package com.meijialife.dingdang.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author yejiurui 
 * 
 */
public class ListViewForInner extends ListView {
    public ListViewForInner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
