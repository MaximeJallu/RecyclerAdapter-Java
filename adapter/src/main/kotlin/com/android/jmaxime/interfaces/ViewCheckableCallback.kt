package com.android.jmaxime.interfaces

import android.annotation.SuppressLint

/**
 * @author  Maxime Jallu
 * @since   26/09/2017
 *
 * Use this Class for : <br/>
 * {DOCUMENTATION}
 */
interface ViewCheckableCallback<T> : IBaseCommunication<T> {
    abstract fun isChecked(item: T): Boolean
    @SuppressLint("NewApi")
    fun isCheckable(item: T): Boolean {
        return true
    }
    fun put(key: String, value: Boolean)
}