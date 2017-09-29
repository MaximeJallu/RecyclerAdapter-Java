package com.android.jmaxime.interfaces;


public interface IBaseCommunication<T> {

    default void onDeleteClicked(int position, T item) {
        /*nothing*/
    }

    default void onEditClicked(int position, T item) {
        /*nothing*/
    }
}
