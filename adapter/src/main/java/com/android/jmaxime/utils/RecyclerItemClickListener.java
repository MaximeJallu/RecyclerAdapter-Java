package com.android.jmaxime.utils;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnChildAttachStateChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

public class RecyclerItemClickListener implements OnChildAttachStateChangeListener {
    private final RecyclerView mRecycler;
    private final OnClickItemListener mClickListener;
    private final OnLongClickItemListener mLongClickListener;

    public RecyclerItemClickListener(RecyclerView recycler, OnClickItemListener clickListener, OnLongClickItemListener longClickListener) {
        mRecycler = recycler;
        mClickListener = clickListener;
        mLongClickListener = longClickListener;
    }

    public static void affectOnClick(RecyclerView recyclerView, OnClickItemListener listener, OnLongClickItemListener longClickListener) {
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerItemClickListener(recyclerView, listener, longClickListener));
    }

    public static void affectOnClick(RecyclerView recyclerView, OnClickItemListener listener) {
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerItemClickListener(recyclerView, listener, null));
    }

    public static void affectOnLongClick(RecyclerView recyclerView, OnLongClickItemListener listener) {
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerItemClickListener(recyclerView, null, listener));
    }

    public void onChildViewAttachedToWindow(View view) {
        if (mClickListener != null) {
            view.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    RecyclerItemClickListener.this.setOnChildAttachedToWindow(v);
                }
            });
        }

        if (mLongClickListener != null) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return setOnLongClickChildAttachedToWindow(view);
                }
            });
        }
    }

    private void setOnChildAttachedToWindow(View v) {
        if (v != null && this.mClickListener != null) {
            int position = this.mRecycler.getChildLayoutPosition(v);
            if (position >= 0) {
                this.mClickListener.onItemClick(position, v);
            }
        }
    }

    private boolean setOnLongClickChildAttachedToWindow(View v) {
        if (v != null && this.mLongClickListener != null) {
            int position = this.mRecycler.getChildLayoutPosition(v);
            if (position >= 0) {
                return this.mLongClickListener.onItemLongClick(position, v);
            }
        }
        return false;
    }

    public void onChildViewDetachedFromWindow(View view) {
        if (view != null) {
            view.setOnClickListener(null);
        }
    }

    public interface OnClickItemListener {
        void onItemClick(int position, View view);
    }

    /**
     * @see <a href=https://developer.android.com/reference/android/view/View.OnLongClickListener.html">Documentation Android</a>
     */
    public interface OnLongClickItemListener {
        /**
         * Called when a view has been clicked and held.
         *
         * @param position position view in adapter
         * @param view     viewHolder that was clicked and held.
         * @return true if the callback consumed the long click, false otherwise.
         */
        boolean onItemLongClick(int position, View view);
    }
}
