package com.android.jmaxime.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.android.jmaxime.factory.ViewHolderFactory;
import com.android.jmaxime.interfaces.IAdapterChanged;
import com.android.jmaxime.interfaces.IBaseCommunication;
import com.android.jmaxime.interfaces.IItemViewType;
import com.android.jmaxime.interfaces.IViewType;
import com.android.jmaxime.interfaces.InitViewHolderDecorator;
import com.android.jmaxime.interfaces.ShowPictureDecorator;
import com.android.jmaxime.viewholder.RecyclerViewHolder;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder<T>> {

    private List<T> mTList;
    private ViewHolderFactory<T> mViewHolderFactory;
    private IItemViewType<T> mViewTypeStrategy;
    private IAdapterChanged mAdapterChanged;

    public RecyclerAdapter() {
        mTList = new ArrayList<>();
    }

    public RecyclerAdapter(ViewHolderFactory<T> factory) {
        this(new ArrayList<>(), factory);
    }

    public RecyclerAdapter(Class<? extends RecyclerViewHolder<T>> viewHolderType) {
        this(new ArrayList<>(), viewHolderType, null);
    }

    public RecyclerAdapter(Class<? extends RecyclerViewHolder<T>> viewHolderType, @Nullable IBaseCommunication callback) {
        this(new ArrayList<>(), viewHolderType, callback);
    }

    public RecyclerAdapter(List<T> TList, Class<? extends RecyclerViewHolder<T>> viewHolderType) {
        this(TList, viewHolderType, null);
    }

    public RecyclerAdapter(List<T> TList, Class<? extends RecyclerViewHolder<T>> viewHolderType, @Nullable IBaseCommunication callback) {
        this(TList, new ViewHolderFactory<>(viewHolderType, callback));
    }

    public RecyclerAdapter(List<T> TList, ViewHolderFactory<T> factory) {
        mTList = TList;
        mViewHolderFactory = factory;
    }

    public void setFactory(ViewHolderFactory<T> factory) {
        mViewHolderFactory = factory;
    }

    @Override
    public RecyclerViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mViewHolderFactory == null) {
            throw new AccessControlException("mViewHolderFactory is not instancied. thanks use setFactory() method.");
        }

        RecyclerViewHolder<T> vh = mViewHolderFactory.createVH(parent, viewType);
        /*used for decorator. Sample ButterKnife*/
        vh.initBinding();
        return vh;
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder<T> holder, int position) {
        holder.setItem(getItem(position));
        holder.setBound(false);
        holder.bind(holder.getItem());
        holder.setBound(true);
    }

    @Override
    public int getItemCount() {
        return mTList != null ? mTList.size() : 0;
    }

    /**
     * Get Item
     *
     * @param position position founded
     * @return instance to position
     */
    public T getItem(int position) {
        return mTList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (mViewTypeStrategy != null){
            return mViewTypeStrategy.getItemViewType(getItem(position));
        }else if (getItem(position) != null && getItem(position) instanceof IViewType) {
            return ((IViewType) getItem(position)).getItemViewType();
        }
        return super.getItemViewType(position);
    }

    public void setViewTypeFactory(IItemViewType<T> viewTypeStrategy) {
        mViewTypeStrategy = viewTypeStrategy;
    }

    public void putViewType(int viewType, Class<? extends RecyclerViewHolder<T>> viewHolder, boolean setDefaultCom){
        mViewHolderFactory.putViewType(viewType, viewHolder, setDefaultCom);
    }

    public void putViewType(int viewType, Class<? extends RecyclerViewHolder<T>> viewHolder, IBaseCommunication callback){
        mViewHolderFactory.putViewType(viewType, viewHolder, callback);
    }

    public boolean contains(final T obj) {
        return mTList.contains(obj);
    }

    protected void callChangedListener() {
        if (mAdapterChanged != null) {
            mAdapterChanged.onItemCountChange(getItemCount());
        }
    }

    public void setOnAdapterChangedListener(IAdapterChanged adapterChanged) {
        mAdapterChanged = adapterChanged;
    }

    public void setCommunication(IBaseCommunication communication) {
        mViewHolderFactory.setCommunication(communication);
        notifyDataSetChanged();
    }

    public void attachInitHolderDecorator(InitViewHolderDecorator holderDecorator) {
        mViewHolderFactory.setInitViewDecorator(holderDecorator);
    }

    public void attachShowPictureDecorator(ShowPictureDecorator pictureDecorator) {
        mViewHolderFactory.setShowPictureDecorator(pictureDecorator);
    }

    /**
     * Inserts the specified element at the specified position in this list (optional operation).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
     *
     * @param item element to be inserted
     */
    public void addItem(T item) {
        if (mTList != null) {
            mTList.add(item);
            notifyItemInserted(mTList.size());
        }
    }

    /**
     * Inserts the specified element at the specified position in this list (optional operation).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
     *
     * @param item     element to be inserted
     * @param position index at which the specified element is to be inserted
     */
    public void addItem(T item, int position) {
        if (mTList != null) {
            position = Math.min(position, mTList.size());
            mTList.add(position, item);
            notifyItemInserted(position);
        }
    }

    /**
     * Inserts the specified element at the specified position in this list (optional operation).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
     *
     * @param collection elements to be inserted
     */
    public void addAll(List<T> collection) {
        if (mTList != null) {
            mTList.addAll(collection);
            int start = Math.max(0, (mTList.size() - collection.size()) - 1);
            notifyItemRangeInserted(start, collection.size());
        }
    }

    /**
     * Inserts the specified element at the specified position in this list (optional operation).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
     *
     * @param item the element to be removed
     */
    public void removeItem(T item) {
        removeItem(getTList().indexOf(item));
    }

    /**
     * Inserts the specified element at the specified position in this list (optional operation).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
     *
     * @param position the index of the element to be removed
     */
    public void removeItem(int position) {
        if (mTList != null && position > -1 && position < mTList.size()) {
            mTList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Set new list items and notifyDataSetChanged()
     *
     * @param list new instance items list for bind views
     * @link notifyDataSetChanged
     */
    public void updateItems(@NonNull List<T> list) {
        mTList = list;
        notifyDataSetChanged();
    }

    /**
     * @see List#indexOf
     * @param item find item
     * @return
     */
    public int indexOf(T item){
        return getTList() != null ? getTList().indexOf(item) : -1;
    }


    /**
     * @return instance items list
     */
    public List<T> getTList() {
        return mTList;
    }

    public boolean isEmpty() {
        return mTList == null || mTList.isEmpty();
    }
}
