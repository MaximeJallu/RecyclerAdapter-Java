package com.android.jmaxime.adapters.itemdecorator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Maxime Jallu
 * @since 09/10/2017
 * <p>
 * Use this Class for : <br/>
 * {DOCUMENTATION}
 */
public class FooterDecoration extends BaseItemDecorator {

    public FooterDecoration(Context context, @LayoutRes int resId) {
        super(context, null, resId);
    }

    public FooterDecoration(Context context, RecyclerView parent, @LayoutRes int resId) {
        super(context, parent, resId);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        // layout basically just gets drawn on the reserved space on top of the first view
        mLayout.layout(parent.getLeft(), 0, parent.getRight(), mLayout.getMeasuredHeight());
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            int childAdapterPosition = parent.getChildAdapterPosition(view);
            if (childAdapterPosition == parent.getAdapter().getItemCount() - 1) {
                c.save();
                final int height = mLayout.getMeasuredHeight();
                final int top = view.getTop() - height;
                c.translate(0, top);
                mLayout.draw(c);
                c.restore();
                break;
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (childAdapterPosition == parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, 0, 0, mLayout.getMeasuredHeight());
        } else {
            outRect.setEmpty();
        }
    }
}
