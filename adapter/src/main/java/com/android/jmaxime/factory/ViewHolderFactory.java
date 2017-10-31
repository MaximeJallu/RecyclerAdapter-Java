package com.android.jmaxime.factory;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jmaxime.annotations.BindLayoutRes;
import com.android.jmaxime.interfaces.IBaseCommunication;
import com.android.jmaxime.interfaces.InitViewHolderDecorator;
import com.android.jmaxime.interfaces.ShowPictureDecorator;
import com.android.jmaxime.viewholder.RecyclerViewHolder;

import java.lang.reflect.InvocationTargetException;

public class ViewHolderFactory<T> {

    private static final String TAG = ViewHolderFactory.class.getName();

    private Class<? extends RecyclerViewHolder<T>> mViewHolderType;
    private @LayoutRes int mLayoutResId;
    private IBaseCommunication mCallback;
    private InitViewHolderDecorator mHolderDecorator;
    private ShowPictureDecorator mPictureDecorator;

    ViewHolderFactory(Class<? extends RecyclerViewHolder<T>> viewHolderType,
                             @Nullable IBaseCommunication callback,
                             @Nullable InitViewHolderDecorator holderDecorator,
                             @Nullable ShowPictureDecorator pictureDecorator) {
        update(viewHolderType, callback);
        mHolderDecorator = holderDecorator;
        mPictureDecorator = pictureDecorator;
    }

    public final RecyclerViewHolder<T> createVH(@NonNull ViewGroup view) {
        RecyclerViewHolder<T> viewHolder = getInstance(LayoutInflater.from(view.getContext())
                .inflate(mLayoutResId, view, false));
        if (viewHolder != null) {
            viewHolder.setCommunication(mCallback);
            viewHolder.setInitViewDecorator(mHolderDecorator);
            viewHolder.setPictureDecorator(mPictureDecorator);
        }
        return viewHolder;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private RecyclerViewHolder<T> getInstance(@NonNull View view) {
        RecyclerViewHolder<T> viewHolder = null;
        try {
            /*HACK : prévention...*/
            mViewHolderType.getConstructor(View.class).setAccessible(true);
            viewHolder = mViewHolderType.getConstructor(View.class).newInstance(view);
        } catch (InstantiationException e) {
            Log.i(TAG, "getInstance: ");
        } catch (IllegalAccessException e) {
            Log.i(TAG, "getInstance: ", e);
        } catch (InvocationTargetException e) {
            Log.i(TAG, "getInstance: ", e);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "not found constructor. La class suivante doit être en static ou ne pas être en inner class : " +
                    mViewHolderType.getName(), e);
        }
        return viewHolder;
    }

    @Nullable
    protected <I extends IBaseCommunication> I getInterfaceCallback() {
        I communication = null;
        try {
            //noinspection unchecked
            communication = (I) mCallback;
        } catch (ClassCastException e) {
            Log.e(TAG, "getInterfaceCallback: ", e);
        }
        return communication;
    }

    @Nullable
    public InitViewHolderDecorator getHolderDecorator() {
        return mHolderDecorator;
    }

    public void setHolderDecorator(InitViewHolderDecorator holderDecorator) {
        mHolderDecorator = holderDecorator;
    }

    @Nullable
    public ShowPictureDecorator getPictureDecorator() {
        return mPictureDecorator;
    }

    public void setPictureDecorator(ShowPictureDecorator pictureDecorator) {
        mPictureDecorator = pictureDecorator;
    }

    public IBaseCommunication getCommunication() {
        return mCallback;
    }

    public void setCallback(IBaseCommunication callback) {
        mCallback = callback;
    }

    void update(Class<? extends RecyclerViewHolder<T>> viewHolderType, IBaseCommunication callback) {
        mViewHolderType = viewHolderType;
            /*optimisation reflexion*/
        mLayoutResId = getLayoutResId(viewHolderType);
        mCallback = callback;
    }

    private int getLayoutResId(Class<? extends RecyclerViewHolder<T>> aClass) {
        checkViewType(aClass);
        return aClass.getAnnotation(BindLayoutRes.class).value();
    }

    private void checkViewType(Class<? extends RecyclerViewHolder<T>> viewHolder) {
        if (!viewHolder.isAnnotationPresent(BindLayoutRes.class)) {
            throw new IllegalArgumentException(viewHolder.getSimpleName()
                    + "is not annoted by " + BindLayoutRes.class.getSimpleName());
        }
    }

    public static class Builder<T> {
        private Class<? extends RecyclerViewHolder<T>> mViewHolderType;
        private IBaseCommunication mCallback;
        private InitViewHolderDecorator mHolderDecorator;
        private ShowPictureDecorator mPictureDecorator;

        public Builder(Class<? extends RecyclerViewHolder<T>> viewHolderType) {
            mViewHolderType = viewHolderType;
        }

        public Builder<T> append(IBaseCommunication communication) {
            this.mCallback = communication;
            return this;
        }

        public Builder<T> append(InitViewHolderDecorator holderDecorator) {
            this.mHolderDecorator = holderDecorator;
            return this;
        }

        public Builder<T> append(ShowPictureDecorator pictureDecorator) {
            this.mPictureDecorator = pictureDecorator;
            return this;
        }

        public ViewHolderFactory<T> build() {
            return new ViewHolderFactory<>(mViewHolderType, mCallback, mHolderDecorator, mPictureDecorator);
        }
    }
}
