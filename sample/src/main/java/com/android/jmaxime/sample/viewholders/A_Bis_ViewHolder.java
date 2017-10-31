package com.android.jmaxime.sample.viewholders;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.jmaxime.annotations.BindLayoutRes;
import com.android.jmaxime.sample.R;
import com.android.jmaxime.sample.models.A;
import com.android.jmaxime.viewholder.RecyclerViewHolder;


@BindLayoutRes(R.layout.item_one_sample)
public class A_Bis_ViewHolder extends RecyclerViewHolder<A> {

    private final TextView mTextView;

    public A_Bis_ViewHolder(View itemView) {
        super(itemView);
        Log.d("DEBUG", "A_Bis_ViewHolder: onCreate()");
        mTextView = itemView.findViewById(R.id.text);
    }

    @Override
    public void bind(A item) {
        Log.d("DEBUG", "A_Bis_ViewHolder: bind()");
        mTextView.setTextSize(18);
        mTextView.setText("New type : " + item.getName());
    }
}
