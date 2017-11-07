package com.android.jmaxime.viewholder;

@SuppressWarnings("unchecked")
public class Container {
    private final Object mT;

    public Container() {
        this(null);
    }

    public Container(Object t) {
        mT = t;
    }

    public boolean isEmpty(){
        return mT == null;
    }

    public final <CAST> CAST getValue(){
        return (CAST) mT;
    }
}
