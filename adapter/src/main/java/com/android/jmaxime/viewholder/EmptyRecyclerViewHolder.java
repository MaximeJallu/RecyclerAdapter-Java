package com.android.jmaxime.viewholder;

import android.support.annotation.NonNull;
import android.view.View;

public abstract class EmptyRecyclerViewHolder extends RecyclerViewHolder<Void> {
    public EmptyRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public final void bind(Void item) {
        throw new RuntimeException("The method on bind (Object item) should not be called for this type of viewHolder");
    }

    public abstract void bind();
}
