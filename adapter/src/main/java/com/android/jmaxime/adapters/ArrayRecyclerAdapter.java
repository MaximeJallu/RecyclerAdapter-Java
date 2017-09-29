package com.android.jmaxime.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.android.jmaxime.viewholder.RecyclerViewHolder;

import java.util.List;

/**
 * @author Maxime Jallu
 * @since 30/06/2016
 *
 * Use this Class for : <br/>
 * Crée un adapteur de recycler view de base
 * @param <T> Type d'item de la liste
 * @param <U> Type de ViewHolder doit extends de RecyclerViewHolder
 */
public abstract class ArrayRecyclerAdapter<T, U extends RecyclerViewHolder<T>> extends RecyclerView.Adapter<U> {

    private List<T> mTList;

    /**
     * Constructor
     * @param TList list items for binding views
     */
    public ArrayRecyclerAdapter(@NonNull final List<T> TList) {
        mTList = TList;
    }

    @Override
    public void onBindViewHolder(U holder, int position) {
        holder.setItem(getItem(position));
        holder.setBound(false);
        holder.bind(holder.getItem());
        holder.setBound(true);
    }

    @Override
    public int getItemCount() {
        return mTList != null ? mTList.size() : 0;
    }

    public U onCreateViewHolder(View view){
        return null;
    }

    @Override
    public U onCreateViewHolder(ViewGroup parent, int viewType) {
        if(getLayoutRes(viewType) > 0) {
            return onCreateViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(viewType), parent, false));
        }else{
            throw new UnsupportedOperationException("onCreateViewHolder(ViewGroup parent, int viewType)");
        }
    }

    @LayoutRes
    protected int getLayoutRes(int viewType){
        return 0;
    }

    /**
     * Get Item
     * @param position position founded
     * @return instance to position
     */
    public T getItem(int position){
        return mTList.get(position);
    }

    /**
     * Inserts the specified element at the specified position in this list (optional operation).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
     * @param item element to be inserted
     */
    public void addItem(T item){
        if (mTList != null) {
            mTList.add(item);
            notifyItemInserted(mTList.size());
        }
    }

    /**
     * Inserts the specified element at the specified position in this list (optional operation).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
     * @param item element to be inserted
     * @param position index at which the specified element is to be inserted
     */
    public void addItem(T item, int position){
        if (mTList != null) {
            position = Math.min(position, mTList.size());
            mTList.add(position, item);
            notifyItemInserted(position);
        }
    }

    /**
     * Inserts the specified element at the specified position in this list (optional operation).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
     * @param position the index of the element to be removed
     */
    public void removeItem(int position){
        if (mTList != null) {
            mTList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Set new list items and notifyDataSetChanged()
     * @link notifyDataSetChanged
     * @param list new instance items list for bind views
     */
    public void updateItems(@NonNull List<T> list){
        mTList = list;
        notifyDataSetChanged();
    }

    /**
     *
     * @return instance items list
     */
    public List<T> getTList() {
        return mTList;
    }
}
