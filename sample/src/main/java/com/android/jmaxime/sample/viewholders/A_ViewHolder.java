package com.android.jmaxime.sample.viewholders;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.jmaxime.annotations.BindLayoutRes;
import com.android.jmaxime.sample.R;
import com.android.jmaxime.sample.models.A;
import com.android.jmaxime.viewholder.RecyclerViewHolder;

@BindLayoutRes(R.layout.item_one_sample)
public class A_ViewHolder extends RecyclerViewHolder<A> {

    private final TextView mTextView;

    public A_ViewHolder(View itemView) {
        super(itemView);
        Log.d("DEBUG", "A_ViewHolder: onCreate()");
        mTextView = itemView.findViewById(R.id.text);
    }

    @Override
    public void bind(A item) {
        Log.d("DEBUG", "A_ViewHolder: bind()");
        mTextView.setText(item.getName());
    }
}
