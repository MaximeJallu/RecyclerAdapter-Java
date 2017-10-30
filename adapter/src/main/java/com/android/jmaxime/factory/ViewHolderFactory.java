package com.android.jmaxime.factory;

import android.annotation.SuppressLint;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jmaxime.annotations.BindLayoutRes;
import com.android.jmaxime.interfaces.IBaseCommunication;
import com.android.jmaxime.interfaces.InitViewHolderDecorator;
import com.android.jmaxime.interfaces.ShowPictureDecorator;
import com.android.jmaxime.viewholder.RecyclerViewHolder;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;

public class ViewHolderFactory<T> {

    private static final String TAG = ViewHolderFactory.class.getName();
    private static final int DEFAULT_VIEW_TYPE = 0;

    private WeakReference<ShowPictureDecorator> mDecoratorWeakReference;
    private WeakReference<InitViewHolderDecorator> mInitViewDecoratorWeakReference;
    private SparseArray<ViewTypeContainer<T>> mViewHashMap;

    public ViewHolderFactory() {
        this(null, null);
    }

    @SuppressLint("UseSparseArrays")
    public ViewHolderFactory(Class<? extends RecyclerViewHolder<T>> viewHolderType) {
        this(viewHolderType, null);
    }

    public ViewHolderFactory(Class<? extends RecyclerViewHolder<T>> viewHolderType,
                             @Nullable IBaseCommunication callback) {
        this(viewHolderType, callback, null, null);
    }

    public ViewHolderFactory(Class<? extends RecyclerViewHolder<T>> viewHolderType,
                             @Nullable IBaseCommunication callback,
                             @Nullable InitViewHolderDecorator holderDecorator,
                             @Nullable ShowPictureDecorator pictureDecorator) {
        mViewHashMap = new SparseArray<>();

        mViewHashMap.put(DEFAULT_VIEW_TYPE, new ViewTypeContainer<>(viewHolderType, callback));
        setInitViewDecorator(holderDecorator);
        setShowPictureDecorator(pictureDecorator);
    }

    public final RecyclerViewHolder<T> createVH(ViewGroup view, int viewType) {
        RecyclerViewHolder<T> ret = getInstance(LayoutInflater.from(view.getContext())
                .inflate(getLayoutRes(viewType), view, false), viewType);
        if (ret != null) {
            ret.setCommunication(getInterfaceCallback(viewType));
            ret.setInitViewDecorator(mInitViewDecoratorWeakReference.get());
            ret.setPictureDecorator(mDecoratorWeakReference.get());
        }
        return ret;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private RecyclerViewHolder<T> getInstance(View view, int viewType) {
        RecyclerViewHolder<T> ret = null;
        try {
            /*prévention...*/
            getViewHolderType(viewType).getConstructor(View.class).setAccessible(true);
            ret = getViewHolderType(viewType).getConstructor(View.class).newInstance(view);
        } catch (InstantiationException e) {
            Log.i(TAG, "getInstance: ");
        } catch (IllegalAccessException e) {
            Log.i(TAG, "getInstance: ", e);
        } catch (InvocationTargetException e) {
            Log.i(TAG, "getInstance: ", e);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "not found constructor. La class suivante doit être en static ou ne pas être en inner class : " + getViewHolderType(viewType)
                    .getName(), e);
        }
        return ret;
    }

    protected <I extends IBaseCommunication> I getInterfaceCallback(int viewType) {
        I i = null;
        try {
            //noinspection unchecked
            i = (I) getCommunication(viewType);
        } catch (ClassCastException e) {
            Log.e(TAG, "getInterfaceCallback: ", e);
        }
        return i;
    }

    protected Class<? extends RecyclerViewHolder<T>> getViewHolderType(int viewType) {
        Class<? extends RecyclerViewHolder<T>> vm = mViewHashMap.get(DEFAULT_VIEW_TYPE).mViewHolderType;
        if (containsViewType(viewType)) {
            vm = mViewHashMap.get(viewType).mViewHolderType;
        }
        return vm;
    }

    public final IBaseCommunication getCommunication(int viewType) {
        return containsViewType(viewType) ? mViewHashMap.get(viewType).mCallback : null;
    }

    public void setCommunication(IBaseCommunication communication) {
        mViewHashMap.get(DEFAULT_VIEW_TYPE).mCallback = communication;
    }

    public void setShowPictureDecorator(@Nullable ShowPictureDecorator decoratorWeakReference) {
        mDecoratorWeakReference = new WeakReference<>(decoratorWeakReference);
    }

    public void setInitViewDecorator(@Nullable InitViewHolderDecorator initViewDecoratorWeakReference) {
        mInitViewDecoratorWeakReference = new WeakReference<>(initViewDecoratorWeakReference);
    }

    final @LayoutRes int getLayoutRes(int viewType) {
        return containsViewType(viewType) ? mViewHashMap.get(viewType).mLayoutResId : 0;
    }

    public void putViewType(int viewType, Class<? extends RecyclerViewHolder<T>> viewHolder, boolean setDefaultCommunication){
        putViewType(viewType, viewHolder, setDefaultCommunication ? mViewHashMap.get(DEFAULT_VIEW_TYPE).mCallback : null);
    }

    public void putViewType(int viewType, Class<? extends RecyclerViewHolder<T>> viewHolder, IBaseCommunication communicationCallback) {
        if (viewType == 0){
            throw new IllegalArgumentException("viewType must be greater than 0. Because 0 is reserved for viewHolder by default");
        }
        if (containsViewType(viewType)){
            mViewHashMap.get(viewType).update(viewHolder, communicationCallback);
        }else {
            mViewHashMap.put(viewType, new ViewTypeContainer<>(viewHolder, communicationCallback));
        }
    }

    private boolean containsViewType(int viewType){
        return mViewHashMap.indexOfKey(viewType) >= 0;
    }

    private static class ViewTypeContainer<T> {
        Class<? extends RecyclerViewHolder<T>> mViewHolderType;
        @LayoutRes int mLayoutResId;
        IBaseCommunication mCallback;

        ViewTypeContainer(Class<? extends RecyclerViewHolder<T>> viewHolderType, IBaseCommunication callback) {
            update(viewHolderType, callback);
        }

        void update(Class<? extends RecyclerViewHolder<T>> viewHolderType, IBaseCommunication callback){
            mViewHolderType = viewHolderType;
            /*optimisation reflexion*/
            mLayoutResId = getLayoutResId(viewHolderType);
            mCallback = callback;
        }

        private int getLayoutResId(Class<? extends RecyclerViewHolder<T>> aClass){
            checkViewType(aClass);
            return aClass.getAnnotation(BindLayoutRes.class).value();
        }

        private void checkViewType(Class<? extends RecyclerViewHolder<T>> viewHolder) {
            if (!viewHolder.isAnnotationPresent(BindLayoutRes.class)) {
                throw new IllegalArgumentException(viewHolder.getSimpleName()
                        + "is not annoted by " + BindLayoutRes.class.getSimpleName());
            }
        }
    }
}
