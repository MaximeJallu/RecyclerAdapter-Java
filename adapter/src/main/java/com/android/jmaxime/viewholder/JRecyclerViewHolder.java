package com.android.jmaxime.viewholder;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.jmaxime.adapter.R;
import com.android.jmaxime.interfaces.IBaseCommunication;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

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
 * make it easier ViewHolder com.android.jmaxime.adapter recyclerView, define T type of item
 * Must to use in ArrayRecyclerAdapter
 */
public abstract class JRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    private T mItem;
    private boolean isBound;
    private IBaseCommunication<T> mCommunication;

    /**
     * This super() auto BindViews with ButterKnife<br/>
     * <code>
     * ButterKnife.bind(this, itemView);
     * </code>
     *
     * @param itemView the Views holder
     */
    @SuppressLint("NewApi")
    public JRecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void bind(final T item);

    protected Context getContext() {
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

    protected final void showPicture(ImageView picture, String url) {
        Picasso.with(picture.getContext())
               .load(url)
               .placeholder(R.drawable.no_image)
               .error(R.drawable.no_image)
               .fit()
               .centerInside()
               .into(picture);
    }

    protected boolean isBound() {
        return isBound;
    }

    public void setBound(boolean bound) {
        isBound = bound;
    }

    public T getItem() {
        return mItem;
    }

    public void setItem(T item) {
        mItem = item;
    }

    protected final void onClickRemoveItem() {
        getDispatcher().onDeleteClicked(getAdapterPosition(), getItem());
    }

    protected final void onClickEditItem() {
        getDispatcher().onEditClicked(getAdapterPosition(), getItem());
    }

    protected <I extends IBaseCommunication<T>> I getDispatcher() {
        I i = null;
        try {
            //noinspection unchecked
            i = (I) mCommunication;
        } catch (ClassCastException e) {
            Log.e("ViewHolderFactory", "getInterfaceCallback: ", e);
        }
        return i;
    }

    public void setCommunication(IBaseCommunication<T> interfaceCallback) {
        mCommunication = interfaceCallback;
    }
}