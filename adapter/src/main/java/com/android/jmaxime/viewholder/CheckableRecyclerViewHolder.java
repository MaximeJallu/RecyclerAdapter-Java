package com.android.jmaxime.viewholder;

import android.view.View;

import com.android.jmaxime.adapters.decorators.CheckableAdapterDecorator;


public abstract class CheckableRecyclerViewHolder<T> extends RecyclerViewHolder<T> {

    private boolean mIsChecked;
    private boolean mIsCheckable;
    private CheckableAdapterDecorator mDecorator;

    public CheckableRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }

    public void setCheckable(boolean checkable) {
        mIsCheckable = checkable;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public boolean isCheckable() {
        return mIsCheckable;
    }

    public void setDecorator(CheckableAdapterDecorator decorator) {
        mDecorator = decorator;
    }

    public void toogleChecked(){
        if (isBound() && mDecorator != null) {
            if (mIsChecked) {
                mDecorator.unselectedItem(getItem());
            } else if (mIsCheckable){
                mDecorator.selectedItem(getItem());
            }
        }
    }
}