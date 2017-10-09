package com.android.jmaxime.adapters.itemdecorator;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class HeaderDecoration extends BaseItemDecorator {

    public HeaderDecoration(Context context, RecyclerView parent, @LayoutRes int resId) {
        super(context, parent, resId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.set(0, mLayout.getMeasuredHeight(), 0, 0);
        } else {
            outRect.setEmpty();
        }
    }
}
