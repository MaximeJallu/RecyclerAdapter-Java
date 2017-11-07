package com.android.jmaxime.sample.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jmaxime.sample.R;
import com.android.jmaxime.sample.models.A;
import com.android.jmaxime.sample.models.B;

import java.util.List;

public class MultiOldAdapter extends RecyclerView.Adapter {
    private final List<A> mAList;
    private final List<B> mBList;

    public MultiOldAdapter(List<A> aList, List<B> bList) {
        mAList = aList;
        mBList = bList;
    }
    //prend pas les null ...

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_one_sample, parent, false));
            default: //super.getItemViewType(position) = 0
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_two_sample, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 1:
                ((ViewHolder) holder).update(mAList.get(position));
                break;
            default:
                ((ViewHolder) holder).update(mBList.get(position - mAList.size()));
                break;

        }
    }

    @Override
    public int getItemCount() {
        return mAList.size() + mBList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mAList.size()) {
            return 1;
        }
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        void update(Object item) {

        }
    }
}
