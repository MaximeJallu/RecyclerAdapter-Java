package com.android.jmaxime.sample.viewholders;

import android.view.View;
import android.widget.TextView;

import com.android.jmaxime.annotations.BindLayoutRes;
import com.android.jmaxime.sample.R;
import com.android.jmaxime.sample.models.B;
import com.android.jmaxime.viewholder.RecyclerViewHolder;

@BindLayoutRes(R.layout.item_two_sample)
public class B_ViewHolder extends RecyclerViewHolder<B> {
    private final TextView mTextView;

    public B_ViewHolder(View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.text);
    }

    @Override
    public void bind(B item) {
        mTextView.setText(item.getName());
    }
}
