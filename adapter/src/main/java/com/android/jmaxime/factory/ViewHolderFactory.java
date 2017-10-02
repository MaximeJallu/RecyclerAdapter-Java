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

/**
 * @author Maxime Jallu
 * @since 03/05/2017
 * Use this Class for : <br/>
 * ... {DOCUMENTATION}
 */
public class ViewHolderFactory<T> {

    private static final String TAG = ViewHolderFactory.class.getName();
    private WeakReference<IBaseCommunication> mCommunication;
    private WeakReference<ShowPictureDecorator> mDecoratorWeakReference;
    private WeakReference<InitViewHolderDecorator> mInitViewDecoratorWeakReference;
    private Class<? extends RecyclerViewHolder<T>> mViewHolderType;
    private SparseArray<Class<? extends RecyclerViewHolder<T>>> mClassHashMap;

    public ViewHolderFactory() {
        this(null, null);
    }

    @SuppressLint("UseSparseArrays")
    public ViewHolderFactory(Class<? extends RecyclerViewHolder<T>> viewHolderType) {
        this(viewHolderType, null);
    }

    public ViewHolderFactory(Class<? extends RecyclerViewHolder<T>> viewHolderType, @Nullable IBaseCommunication callback) {
        this(viewHolderType, callback, null, null);
    }

    public ViewHolderFactory(Class<? extends RecyclerViewHolder<T>> viewHolderType,
            @Nullable IBaseCommunication callback, @Nullable InitViewHolderDecorator holderDecorator,
            @Nullable ShowPictureDecorator pictureDecorator) {
        mViewHolderType = viewHolderType;
        mClassHashMap = new SparseArray<>();
        setCommunication(callback);
        setInitViewDecorator(holderDecorator);
        setShowPictureDecorator(pictureDecorator);
    }

    public final RecyclerViewHolder<T> createVH(ViewGroup view, int viewType) {
        RecyclerViewHolder<T> ret = getInstance(LayoutInflater.from(view.getContext())
                                                              .inflate(getLayoutRes(viewType), view, false), viewType);
        if (ret != null) {
            ret.setCommunication(getInterfaceCallback());
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
        if (mClassHashMap.indexOfKey(viewType) > -1) {
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

    public void setShowPictureDecorator(@Nullable ShowPictureDecorator decoratorWeakReference) {
        mDecoratorWeakReference = new WeakReference<>(decoratorWeakReference);
    }

    public void setInitViewDecorator(@Nullable InitViewHolderDecorator initViewDecoratorWeakReference) {
        mInitViewDecoratorWeakReference = new WeakReference<>(initViewDecoratorWeakReference);
    }

    final @LayoutRes int getLayoutRes(int viewType) {
        return getViewHolderType(viewType).getAnnotation(BindLayoutRes.class).value();
    }

    public void putViewType(int viewType, Class<? extends RecyclerViewHolder<T>> viewHolder) {
        mClassHashMap.put(viewType, viewHolder);
    }
}
