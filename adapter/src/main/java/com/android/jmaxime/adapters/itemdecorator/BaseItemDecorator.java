package com.android.jmaxime.adapters.itemdecorator;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

public class BaseItemDecorator extends RecyclerView.ItemDecoration {

    protected View mLayout;

    public BaseItemDecorator(final Context context, RecyclerView parent, @LayoutRes int resId) {
        // inflate and measure the layout
        mLayout = LayoutInflater.from(context).inflate(resId, parent, false);
        mLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }
}
