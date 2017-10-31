package com.android.jmaxime.adapters.decorators;

import android.util.SparseBooleanArray;

import com.android.jmaxime.adapters.RecyclerAdapter;
import com.android.jmaxime.interfaces.CheckableViewHolder;
import com.android.jmaxime.viewholder.CheckableRecyclerViewHolder;
import com.android.jmaxime.viewholder.RecyclerViewHolder;


public class CheckableAdapterDecorator<T> extends RecyclerAdapter<T> implements CheckableViewHolder {

    private SparseBooleanArray mMap = new SparseBooleanArray();
    private RecyclerAdapter mAdapter;
    private boolean isLockable = false;
    private int mSelectedMax = -1;

    public CheckableAdapterDecorator(RecyclerAdapter adapter) {
        mAdapter = adapter;
    }

    @Override public boolean isCheckable(Object item) {
        return isChecked(item) || mMap.indexOfValue(true) < 0 || !isLockable && isLock();
    }

    @Override public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        ((CheckableRecyclerViewHolder)holder).setChecked(isChecked(mAdapter.getItem(position)));
        ((CheckableRecyclerViewHolder)holder).setCheckable(isCheckable(mAdapter.getItem(position)));
        ((CheckableRecyclerViewHolder)holder).setDecorator(this);
        super.onBindViewHolder(holder, position);
    }


    public void setSelectedMax(int selectedMax) {
        mSelectedMax = selectedMax;
    }

    public void setLockable(boolean lockable) {
        isLockable = lockable;
    }

    @Override public boolean isChecked(Object item) {
        return mMap.get(getHashCode(item));
    }

    @Override public void selectedItem(Object item) {
        if (mSelectedMax == 1) {
            mMap.clear();
        }
        put(getHashCode(item), true);
    }

    @Override public void unselectedItem(Object item) {
        put(getHashCode(item), false);
    }

    @SuppressWarnings("unchecked")
    private int getHashCode(Object o) {
        if (mAdapter.indexOf(o) > -1) {
            return mAdapter.getItem(mAdapter.indexOf(o)).hashCode();
        }
        return -1;
    }

    private boolean isLock() {
        int count = 0;
        for (int i = 0; i < mMap.size(); i++) {
            if (mMap.valueAt(i)){
                count++;
            }
        }
        return mSelectedMax <= 1 || (mSelectedMax > 0 && mSelectedMax < count);
    }

    private void put(int hashCode, boolean value) {
        if (hashCode > 0) {
            mMap.put(hashCode, value);
        }
    }
}