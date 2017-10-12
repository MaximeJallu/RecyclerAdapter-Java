package com.android.jmaxime.interfaces;

public interface ViewCheckableCallback<T> extends IBaseCommunication {
    boolean isChecked(T item);
    boolean isCheckable(T item);
    void put(String key, boolean value);
}
