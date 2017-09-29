package com.android.jmaxime.interfaces

/**
 * @author Maxime Jallu
 * @since 10/05/2017
 * Use this Class for : <br></br>
 * ... {DOCUMENTATION}
 */
interface IBaseCommunication<T> {

    fun onDeleteClicked(position: Int, item : T) {
        /*nothing*/
    }

    fun onEditClicked(position: Int, item: T) {
        /*nothing*/
    }
}
