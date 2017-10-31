package com.android.jmaxime.viewholder;

@SuppressWarnings("unchecked")
public class ContainerViewModel<T>  {
    private final T mT;

    public ContainerViewModel() {
        this(null);
    }

    public ContainerViewModel(T t) {
        mT = t;
    }

    public boolean isEmpty(){
        return mT == null;
    }

    public final <CAST> CAST getValue(){
        return (CAST) mT;
    }
}
