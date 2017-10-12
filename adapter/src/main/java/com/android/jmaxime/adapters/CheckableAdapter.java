package com.android.jmaxime.adapters;

import com.android.jmaxime.factory.ViewHolderFactory;
import com.android.jmaxime.interfaces.ViewCheckableCallback;
import com.android.jmaxime.viewholder.RecyclerViewHolder;

import java.util.HashMap;
import java.util.List;


public class CheckableAdapter<T> extends RecyclerAdapter<T> implements ViewCheckableCallback<T> {

    private HashMap<String, Boolean> mCheckedMap = new HashMap<>();

    public CheckableAdapter(Class<? extends RecyclerViewHolder<T>> viewHolderType) {
        super(viewHolderType);
        setCommunication(this);
    }

    public CheckableAdapter(List<T> TList, ViewHolderFactory<T> factory) {
        super(TList, factory);
        setCommunication(this);
    }

    /**
     * Clear map and add item to selected
     * add selected position to sparseArray
     *
     * @param position
     */
    public void setSelected(int position) {
        /*si plusieurs cellules sont checked alors on fera un notify complet*/
        mCheckedMap.clear();
        put(String.valueOf(getItem(position).hashCode()), true);
        notifyDataSetChanged();
    }

    @Override public boolean isChecked(T item) {
        String hashString = String.valueOf(item.hashCode());
        return mCheckedMap.containsKey(hashString) ? mCheckedMap.get(hashString) : false;
    }

    @Override public boolean isCheckable(T item) {
        return true;
    }

    @Override public void put(String key, boolean value) {
        mCheckedMap.put(key, value);
    }
}
