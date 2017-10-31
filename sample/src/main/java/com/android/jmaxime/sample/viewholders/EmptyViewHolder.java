package com.android.jmaxime.sample.viewholders;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.android.jmaxime.annotations.BindLayoutRes;
import com.android.jmaxime.sample.R;
import com.android.jmaxime.viewholder.EmptyRecyclerViewHolder;

import java.util.Locale;

@BindLayoutRes(R.layout.item_empty_holder)
public class EmptyViewHolder extends EmptyRecyclerViewHolder {

    private TextView mTextView;
    private int mCountBindView = 1;

    public EmptyViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.text_compteur);
    }

    @Override
    public void bind() {
        mTextView.setText(String.format(Locale.getDefault(), "I'm : %d\n" +
                "This view has been loaded : %d", itemView.hashCode(), mCountBindView++));
    }
}
