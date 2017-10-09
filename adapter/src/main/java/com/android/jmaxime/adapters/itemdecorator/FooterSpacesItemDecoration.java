package com.android.jmaxime.adapters.itemdecorator;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.jmaxime.adapter.R;


public class FooterSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;

    public FooterSpacesItemDecoration(Context context){
        this((int) context.getResources().getDimension(R.dimen.default_footer_space));
    }

    public FooterSpacesItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        // Add top margin only for the last item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == parent.getChildCount() -1) {
            outRect.top = mSpace;
        }
    }
}