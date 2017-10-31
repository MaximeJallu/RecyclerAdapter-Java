package com.android.jmaxime.sample.models;

import com.android.jmaxime.interfaces.IViewType;
import com.android.jmaxime.viewholder.ContainerViewModel;

public class Container extends ContainerViewModel implements IViewType {

    public Container() {
        super();
    }

    public Container(A a) {
        super(a);
    }

    public Container(B b) {
        super(b);
    }

    public A getA(){
        return (A) getValue();
    }

    @Override
    public int getItemViewType() {
        if (isEmpty()){
            return 3;
        }
        return (getValue() instanceof A) ? 1 : 2;
    }
}
