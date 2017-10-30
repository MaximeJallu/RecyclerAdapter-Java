package com.android.jmaxime.sample.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.android.jmaxime.sample.models.ContainerModel;
import com.android.jmaxime.viewholder.RecyclerViewHolder;

import java.util.List;

public class MultiTypeAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<T> mTList;

    public MultiTypeAdapter(List<T> TList) {
        mTList = TList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (mTList.get(position) instanceof ContainerModel) {
            holder.bind(((ContainerModel)mTList.get(position)).getItem());
        }
    }

    @Override
    public int getItemCount() {
        return mTList.size();
    }
}
