package com.android.jmaxime.utils;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnChildAttachStateChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

public class RecyclerItemClickListener implements OnChildAttachStateChangeListener {
    private final RecyclerView mRecycler;
    private final RecyclerItemClickListener.OnRecyclerOnItemClickListener mListener;

    public RecyclerItemClickListener(RecyclerView recyclerView, RecyclerItemClickListener.OnRecyclerOnItemClickListener listener) {
        this.mRecycler = recyclerView;
        this.mListener = listener;
    }

    public static void affectOnItemClick(RecyclerView recyclerView, RecyclerItemClickListener.OnRecyclerOnItemClickListener listener) {
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerItemClickListener(recyclerView, listener));
    }

    public void onChildViewAttachedToWindow(View view) {
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RecyclerItemClickListener.this.setOnChildAttachedToWindow(v);
            }
        });
    }

    private void setOnChildAttachedToWindow(View v) {
        if(v != null && this.mListener != null) {
            int position = this.mRecycler.getChildLayoutPosition(v);
            if(position >= 0) {
                this.mListener.onItemClick(position, v);
            }
        }

    }

    public void onChildViewDetachedFromWindow(View view) {
        if(view != null) {
            view.setOnClickListener((OnClickListener)null);
        }
    }

    public interface OnRecyclerOnItemClickListener {
        void onItemClick(int var1, View var2);
    }
}
