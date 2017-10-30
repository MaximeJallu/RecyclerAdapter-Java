package com.android.jmaxime.sample.models;

import com.android.jmaxime.interfaces.IViewType;

public class Container extends ContainerModel implements IViewType {
    private ObjectOne mOne;
    private ObjectTwo mTwo;

    public Container(ObjectOne one) {
        init(one, null);
    }

    public Container(ObjectTwo two) {
        init(null, two);
    }

    private void init(ObjectOne one, ObjectTwo two){
        mOne = one;
        mTwo = two;
    }

    public ObjectOne getOne() {
        return mOne;
    }

    public ObjectTwo getTwo() {
        return mTwo;
    }

    @Override
    public Object getItem() {
        return getItemViewType() == 1 ? mOne : mTwo;
    }

    @Override
    public int getItemViewType() {
        return mOne != null ? 1 : 2;
    }
}
