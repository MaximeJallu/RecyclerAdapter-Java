package com.android.jmaxime.adapter

import android.support.v7.widget.RecyclerView

/**
 * @author Maxime Jallu
 * @since 30/06/2016
 *
 * Create for CubeInStore - Android
 *
 * Use this Class for : <br></br>
 * Crée un adapteur de recycler view de base
 * @param <T> Type d'item de la liste
 * @param <U> Type de ViewHolder doit extends de RecyclerViewHolder<T>
 **/
abstract class ArrayRecyclerAdapter<T, U : KRecyclerViewHolder<T>>(TList: List<T>) : RecyclerView.Adapter<U>() {

    var tList: List<T>? = null
        private set

    init {
        tList = TList
    }

    override fun onBindViewHolder(holder: U, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return tList!!.size
    }

    /**
     * Get Item
     * @param position position founded
     * @return instance to position
     */
    fun getItem(position: Int): T {
        return tList!![position]
    }

    /**
     * Set new list items and notifyDataSetChanged()
     * @link notifyDataSetChanged
     * @param list new instance items list for bind views
     */
    fun updateItems(list: List<T>) {
        tList = list
        notifyDataSetChanged()
    }
}
