package com.android.jmaxime.factory;

import android.annotation.SuppressLint;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;

import com.android.jmaxime.adapter.RecyclerViewHolder;
import com.android.jmaxime.annotations.BindLayoutRes;
import com.android.jmaxime.interfaces.IBaseCommunication;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author Maxime Jallu
 * @since 03/05/2017
 * Use this Class for : <br/>
 * ... {DOCUMENTATION}
 */
public class ViewHolderFactory<T> {

    private static final String TAG = ViewHolderFactory.class.getName();
    private WeakReference<IBaseCommunication> mCommunication;
    private Class<? extends RecyclerViewHolder<T>> mViewHolderType;
    private HashMap<Integer, Class<? extends RecyclerViewHolder<T>>> mClassHashMap;

    public ViewHolderFactory() {
        this(null);
    }

    @SuppressLint("UseSparseArrays")
    public ViewHolderFactory(Class<? extends RecyclerViewHolder<T>> viewHolderType) {
        mViewHolderType = viewHolderType;
        mClassHashMap = new HashMap<>();
    }

    public final RecyclerViewHolder<T> createVH(View view, int viewType) {
        RecyclerViewHolder<T> ret = getInstance(view, viewType);
        if (ret != null) {
            ret.setCommunication(getInterfaceCallback());
        }
        return ret;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private RecyclerViewHolder<T> getInstance(View view, int viewType) {
        RecyclerViewHolder<T> ret = null;
        try {
            ret = getViewHolderType(viewType).getConstructor(View.class).newInstance(view);
        } catch (InstantiationException e) {
            Log.i(TAG, "getInstance: ");
        } catch (IllegalAccessException e) {
            Log.i(TAG, "getInstance: ", e);
        } catch (InvocationTargetException e) {
            Log.i(TAG, "getInstance: ", e);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "not found constructor. La class suivante doit être en static ou ne pas être en inner class : " + getViewHolderType(viewType).getName(), e);
        }
        return ret;
    }

    protected <I extends IBaseCommunication> I getInterfaceCallback() {
        I i = null;
        try {
            //noinspection unchecked
            i = (I) getCommunication();
        } catch (ClassCastException e) {
            Log.e(TAG, "getInterfaceCallback: ", e);
        }
        return i;
    }

    protected Class<? extends RecyclerViewHolder<T>> getViewHolderType(int viewType) {
        Class<? extends RecyclerViewHolder<T>> vm = mViewHolderType;
        if (mClassHashMap.containsKey(viewType)) {
            vm = mClassHashMap.get(viewType);
        }
        return vm;
    }

    public final IBaseCommunication getCommunication() {
        return mCommunication.get();
    }

    public void setCommunication(IBaseCommunication communication) {
        mCommunication = new WeakReference<>(communication);
    }

    public final @LayoutRes int getLayoutRes(int viewType) {
        return getViewHolderType(viewType).getAnnotation(BindLayoutRes.class).value();
    }

    public void putViewType(int viewType, Class<? extends RecyclerViewHolder<T>> viewHolder){
        mClassHashMap.put(viewType, viewHolder);
    }
}
