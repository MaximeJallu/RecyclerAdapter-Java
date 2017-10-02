package com.android.jmaxime.interfaces;

/**
 * @author Maxime Jallu
 * @since 02/10/2017
 * <p>
 * Use this Class for : <br/>
 * {DOCUMENTATION}
 */
public interface CheckableViewHolder<T> extends IBaseCommunication<T> {
    boolean isChecked(T item);
    boolean isCheckable(T item);
    void selectedItem(T item);
    void unselectedItem(T item);
}
