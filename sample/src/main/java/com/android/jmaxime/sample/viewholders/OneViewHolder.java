package com.android.jmaxime.sample.viewholders;

import android.view.View;
import android.widget.TextView;

import com.android.jmaxime.annotations.BindLayoutRes;
import com.android.jmaxime.sample.R;
import com.android.jmaxime.sample.models.ObjectOne;
import com.android.jmaxime.viewholder.RecyclerViewHolder;

@BindLayoutRes(R.layout.item_one_sample)
public class OneViewHolder extends RecyclerViewHolder<ObjectOne> {

    private final TextView mTextView;

    public OneViewHolder(View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.text);
    }

    @Override
    public void bind(ObjectOne item) {
        mTextView.setText(item.getName());
    }
}
