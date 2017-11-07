package com.android.jmaxime.sample.models;

import com.android.jmaxime.interfaces.IViewType;

public class Customer implements IViewType{
    private final String mName;

    public Customer(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    @Override
    public int getItemViewType() {
        if (getName().startsWith("A")) {
            return 1;
        }
        return 0;
    }
}
