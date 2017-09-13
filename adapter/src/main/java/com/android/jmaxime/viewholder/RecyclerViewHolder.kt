package com.android.jmaxime.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import butterknife.ButterKnife
import com.android.jmaxime.interfaces.IBaseCommunication
import com.squareup.picasso.Picasso

/**
 * @author Maxime Jallu
 * *
 * @link ArrayRecyclerAdapter
 * * Tools this class:<br></br>
 * *
 * * getContext()
 * * getColor(@ColorRes res)
 * * getDrawable(@DrawableRes res)
 * *
 * @since 30/06/2016
 * *
 * * Create for CubeInStore - Android (Decathlon)
 * *
 * * Use this Class for : <br></br>
 * * make it easier ViewHolder adapter recyclerView, define T type of item
 * * Must to use in RecyclerAdapter
 */
abstract class RecyclerViewHolder<T> constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var item: T? = null
    var isBound: Boolean = false
    private var mCommunication: IBaseCommunication<T>? = null

    init {
        ButterKnife.bind(this, itemView)
    }

    abstract fun bind(item: T)

    protected val context: Context get() = itemView.context

    protected fun getString(@StringRes stringRes: Int): String {
        return context.getString(stringRes)
    }

    protected fun getString(@StringRes stringRes: Int, string: String): String {
        return context.resources.getString(stringRes, string)
    }

    protected fun getQuantityString(@PluralsRes pluralRes: Int, quantity: Int): String {
        return context.resources.getQuantityString(pluralRes, quantity, quantity)
    }

    protected fun getQuantityStringFormat(@PluralsRes pluralRes: Int, quantity: Int): String {
        return context.resources.getQuantityString(pluralRes, quantity, quantity)
    }

    protected fun getColor(@ColorRes colorResId: Int): Int {
        return ContextCompat.getColor(context, colorResId)
    }

    protected fun getDrawable(@DrawableRes drawableResId: Int): Drawable {
        return ContextCompat.getDrawable(context, drawableResId)
    }

    fun showPicture(picture: ImageView, url: String) {
        Picasso.with(picture.context)
                .load(url)
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_image_black_24dp)
                .fit()
                .centerInside()
                .into(picture)
    }

    protected fun onClickRemoveItem() {
        getDispatcher<IBaseCommunication<T>>()?.onDeleteClicked(adapterPosition, item!!)
    }

    protected fun onClickEditItem() {
        getDispatcher<IBaseCommunication<T>>()?.onEditClicked(adapterPosition, item!!)
    }

    protected fun <I : IBaseCommunication<T>> getDispatcher(): I? {
        var i: I? = null
        try {
            i = mCommunication as I?
        } catch (e: ClassCastException) {
            Log.e("ViewHolderFactory", "getInterfaceCallback: ", e)
        }

        return i
    }

    fun setCommunication(interfaceCallback: IBaseCommunication<T>?) {
        mCommunication = interfaceCallback
    }
}
