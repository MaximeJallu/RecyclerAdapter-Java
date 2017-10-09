package com.android.jmaxime.adapters.decorators;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.android.jmaxime.adapters.RecyclerAdapter;
import com.android.jmaxime.viewholder.RecyclerViewHolder;

/**
 *
 * @param <S> Section Type
 * @param <I> Item type for using with RecyclerAdapter<I>
 */
public class RecyclerSectionedAdapter<S, I> extends SectionedAdapter<S, RecyclerAdapter<I>> {

    public RecyclerSectionedAdapter(@NonNull Class<? extends RecyclerViewHolder<S>> viewType, @NonNull RecyclerAdapter<I> iRecyclerAdapter) {
        super(viewType, iRecyclerAdapter);
    }

    public RecyclerSectionedAdapter(@NonNull Class<? extends RecyclerViewHolder<S>> viewType, @Nullable RecyclerView r, @NonNull RecyclerAdapter<I> iRecyclerAdapter) {
        super(viewType, r, iRecyclerAdapter);
    }

    /**
     * Calcule la position reél de l'item en fonction de l'offset des sections
     * @param sectionedPosition current sectioned position
     * @return Item in the base adapter list
     */
    public I getItem(int sectionedPosition){
        return getBaseAdapter().getItem(positionToSectionedPosition(sectionedPosition));
    }
}
