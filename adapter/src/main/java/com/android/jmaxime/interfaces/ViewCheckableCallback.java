package com.android.jmaxime.interfaces;

import android.annotation.SuppressLint;

/**
 * @author Maxime Jallu
 * @since 29/09/2017
 * <p>
 * Use this Class for : <br/>
 * {DOCUMENTATION}
 */
@SuppressLint("NewApi")
public interface ViewCheckableCallback<T> extends IBaseCommunication<T> {
    boolean isChecked(T item);
    default boolean isCheckable(T item) {
        return true;
    }
    void put(String key, boolean value);
}
