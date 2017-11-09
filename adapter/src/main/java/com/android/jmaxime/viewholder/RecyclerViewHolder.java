package com.android.jmaxime.viewholder;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.jmaxime.interfaces.IBaseCommunication;
import com.android.jmaxime.interfaces.InitViewHolderDecorator;
import com.android.jmaxime.interfaces.ShowPictureDecorator;

/**
 * @author Maxime Jallu
 * @link ArrayRecyclerAdapter
 * <p>
 * Tools this class:<br/>
 * <p>
 * getContext()
 * getColor(@ColorRes res)
 * getDrawable(@DrawableRes res)
 * @since 30/06/2016
 * <p>
 * Create for CubeInStore - Android (Decathlon)
 * <p>
 * Use this Class for : <br/>
 * make it easier ViewHolder adapter recyclerView, define T type of item
 * Must to use in ArrayRecyclerAdapter
 */
public abstract class RecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    private T mItem;
    private boolean isBound;
    private InitViewHolderDecorator mDecorator;
    private ShowPictureDecorator mPictureDecorator;
    private IBaseCommunication mCommunication;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void initBinding() {
        if (mDecorator != null) {
            mDecorator.initBinding(this, itemView);
        }
    }

    public final void bindingSmart(final T item){
        /*change state : binding in progress*/
        setBound(false);
        /*set viewModel*/
        setItem(item);
        /*update view*/
        if (this instanceof EmptyRecyclerViewHolder) {
            ((EmptyRecyclerViewHolder) this).bind();
        } else {
            bind(item);
        }
        /*change state : binding finish*/
        setBound(true);
    }

    @UiThread
    public abstract void bind(final T item);

    protected final Context getContext() {
        return itemView.getContext();
    }

    protected final String getString(@StringRes int stringRes) {
        return getContext().getString(stringRes);
    }

    protected final String getString(@StringRes int stringRes, String string) {
        return getContext().getResources().getString(stringRes, string);
    }

    protected final String getQuantityString(@PluralsRes int pluralRes, int quantity) {
        return getContext().getResources().getQuantityString(pluralRes, quantity, quantity);
    }

    protected final String getQuantityStringFormat(@PluralsRes int pluralRes, int quantity) {
        return getContext().getResources().getQuantityString(pluralRes, quantity, quantity);
    }

    protected final int getColor(@ColorRes int colorResId) {
        return ContextCompat.getColor(getContext(), colorResId);
    }

    protected final Drawable getDrawable(@DrawableRes int drawableResId) {
        return ContextCompat.getDrawable(getContext(), drawableResId);
    }

    protected final Drawable getDrawable(@DrawableRes int drawableId, @ColorRes int colorId){
        Drawable drawable = getDrawable(drawableId);
        DrawableCompat.setTint(drawable.mutate(), getColor(colorId));
        return drawable;
    }

    @UiThread
    protected final void showPicture(ImageView picture, String url) {
        if (mPictureDecorator != null) {
            mPictureDecorator.showPicture(picture, url);
        }
    }

    protected final boolean isBound() {
        return isBound;
    }

    private void setBound(boolean bound) {
        isBound = bound;
    }

    public final void setInitViewDecorator(InitViewHolderDecorator decorator) {
        mDecorator = decorator;
    }

    public final void setPictureDecorator(ShowPictureDecorator pictureDecorator) {
        mPictureDecorator = pictureDecorator;
    }

    public final T getItem() {
        return mItem;
    }

    public final void setItem(T item) {
        mItem = item;
    }

    protected final <I extends IBaseCommunication> I getDispatcher() {
        I i = null;
        try {
            //noinspection unchecked
            i = (I) mCommunication;
        } catch (ClassCastException e) {
            Log.e("ViewHolderFactory", "getInterfaceCallback: ", e);
        }
        return i;
    }

    public final void setCommunication(IBaseCommunication interfaceCallback) {
        mCommunication = interfaceCallback;
    }
}

