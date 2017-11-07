package com.android.jmaxime.adapters;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.android.jmaxime.factory.ViewHolderFactory;
import com.android.jmaxime.interfaces.IAdapterChanged;
import com.android.jmaxime.interfaces.IBaseCommunication;
import com.android.jmaxime.interfaces.IViewType;
import com.android.jmaxime.interfaces.InitViewHolderDecorator;
import com.android.jmaxime.interfaces.ItemViewTypeStrategy;
import com.android.jmaxime.interfaces.ShowPictureDecorator;
import com.android.jmaxime.viewholder.Container;
import com.android.jmaxime.viewholder.RecyclerViewHolder;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    private static final int DEFAULT_VIEW_TYPE = 0;
    private final String TAG = this.getClass().getSimpleName();
    private List<T> mTList;
    private SparseArray<ViewHolderFactory> mViewHolderFactory;
    private ItemViewTypeStrategy<T> mViewTypeStrategy;
    private IAdapterChanged mAdapterChanged;

    public RecyclerAdapter() {
        mTList = new ArrayList<>();
    }

    public RecyclerAdapter(ViewHolderFactory<T> factory) {
        this(new ArrayList<>(), factory);
    }

    public RecyclerAdapter(Class<? extends RecyclerViewHolder> viewHolderType) {
        this(new ArrayList<>(), viewHolderType, null);
    }

    public RecyclerAdapter(Class<? extends RecyclerViewHolder> viewHolderType, @Nullable IBaseCommunication callback) {
        this(new ArrayList<>(), viewHolderType, callback);
    }

    public RecyclerAdapter(List<T> TList, Class<? extends RecyclerViewHolder> viewHolderType) {
        this(TList, viewHolderType, null);
    }

    public RecyclerAdapter(List<T> TList, Class<? extends RecyclerViewHolder> viewHolderType, @Nullable IBaseCommunication callback) {
        this(TList, new ViewHolderFactory.Builder(viewHolderType).append(callback).build());
    }

    public RecyclerAdapter(List<T> TList, ViewHolderFactory<T> factory) {
        mTList = TList;
        mViewHolderFactory = new SparseArray<>();
        mViewHolderFactory.append(DEFAULT_VIEW_TYPE, factory);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mViewHolderFactory == null || mViewHolderFactory.size() == 0) {
            throw new AccessControlException("mViewHolderFactory is not instancied. thanks use setDefaultFactory() method.");
        }

        RecyclerViewHolder vh = getViewHolderFactory(viewType).createViewHolder(parent);
        /*used for decorator. Sample ButterKnife*/
        vh.initBinding();
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        /*recupération de notre viewModel*/
        Object viewModel = getItem(position);
        if (viewModel instanceof Container) {
            viewModel = ((Container) viewModel).getValue();
        }
        holder.bindingSmart(viewModel);
    }

    @Override
    public int getItemCount() {
        return mTList != null ? mTList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mViewTypeStrategy != null) {
            return mViewTypeStrategy.getItemViewType(getItem(position));
        } else if (getItem(position) != null && getItem(position) instanceof IViewType) {
            return ((IViewType) getItem(position)).getItemViewType();
        }
        if (mViewHolderFactory.size() > 1){
            Log.w(TAG, "----------------------------GENERIC--ADAPTER--WARNING----------------------------------");
            Log.w(TAG, "in getItemViewType: It looks like you forgot to call the method \"setViewTypeStrategy\"");
        }
        return super.getItemViewType(position);
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


    public void setDefaultFactory(ViewHolderFactory factory) {
        mViewHolderFactory.append(DEFAULT_VIEW_TYPE, factory);
    }

    /**
     * Set the strategy for multi item type
     * This strategy its priority on IViewType
     * @param viewTypeStrategy strategy for multi view type holder
     */
    public void setViewTypeStrategy(ItemViewTypeStrategy<T> viewTypeStrategy) {
        mViewTypeStrategy = viewTypeStrategy;
    }

    /**
     * @param viewType                register type
     * @param viewHolder              attache ViewHolder at to viewType
     * @param useDefaultConfiguration add IBaseCommunication, HeaderDecorator and PictureDecorator of Default
     */
    public void putViewType(@IntRange(from = 1) int viewType, @NonNull Class<? extends RecyclerViewHolder> viewHolder, boolean useDefaultConfiguration) {
        if (useDefaultConfiguration) {
            putViewType(viewType, viewHolder,
                    mViewHolderFactory.get(DEFAULT_VIEW_TYPE).getCommunication(),
                    mViewHolderFactory.get(DEFAULT_VIEW_TYPE).getHolderDecorator(),
                    mViewHolderFactory.get(DEFAULT_VIEW_TYPE).getPictureDecorator());
        } else {
            putViewType(viewType, viewHolder, null, null, null);
        }
    }

    public void putViewType(@IntRange(from = 1) int viewType, @NonNull Class<? extends RecyclerViewHolder> viewHolder, @Nullable IBaseCommunication communicationCallback) {
        putViewType(viewType, viewHolder, communicationCallback, null, null);
    }

    public void putViewType(@IntRange(from = 1) int viewType, @NonNull Class<? extends RecyclerViewHolder> viewHolder,
                            @Nullable IBaseCommunication communicationCallback,
                            @Nullable InitViewHolderDecorator holderDecorator,
                            @Nullable ShowPictureDecorator pictureDecorator) {
        if (viewType == 0) {
            throw new IllegalArgumentException("viewType must be greater than 0. Because 0 is reserved for viewHolder by default");
        }
        mViewHolderFactory.append(viewType,
                new ViewHolderFactory.Builder(viewHolder)
                        .append(communicationCallback)
                        .append(holderDecorator)
                        .append(pictureDecorator)
                        .build());
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

    public void setCommunication(@Nullable IBaseCommunication communication) {
        mViewHolderFactory.get(DEFAULT_VIEW_TYPE).setCallback(communication);
        notifyDataSetChanged();
    }

    public void attachInitHolderDecorator(@Nullable InitViewHolderDecorator holderDecorator) {
        mViewHolderFactory.get(DEFAULT_VIEW_TYPE).setHolderDecorator(holderDecorator);
    }

    public void attachShowPictureDecorator(@Nullable ShowPictureDecorator pictureDecorator) {
        mViewHolderFactory.get(DEFAULT_VIEW_TYPE).setPictureDecorator(pictureDecorator);
    }

    /**
     * Inserts the specified element at the specified position in this list (optional operation).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
     *
     * @param item element to be inserted
     */
    public void addItem(@NonNull T item) {
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
    public void addItem(@NonNull T item, @IntRange(from = 0) int position) {
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
    public void removeItem(@NonNull T item) {
        removeItem(getTList().indexOf(item));
    }

    /**
     * Inserts the specified element at the specified position in this list (optional operation).
     * Shifts the element currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
     *
     * @param position the index of the element to be removed
     */
    public void removeItem(@IntRange(from = 0) int position) {
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
     * @param item find item
     * @return
     * @see List#indexOf
     */
    public int indexOf(T item) {
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

    private boolean containsViewType(int viewType) {
        return mViewHolderFactory.indexOfKey(viewType) >= 0;
    }

    private ViewHolderFactory getViewHolderFactory(int viewType) {
        return mViewHolderFactory.get(containsViewType(viewType) ? viewType : DEFAULT_VIEW_TYPE);
    }
}
