package com.android.jmaxime.sample.viewholders.customers;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.android.jmaxime.annotations.BindLayoutRes;
import com.android.jmaxime.sample.R;
import com.android.jmaxime.sample.models.Customer;
import com.android.jmaxime.viewholder.RecyclerViewHolder;

@BindLayoutRes(R.layout.item_customer_simple)
public class CustomerViewHolder extends RecyclerViewHolder<Customer> {

    private TextView mTextView;

    public CustomerViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.text);
    }

    @Override
    public void bind(Customer item) {
        mTextView.setText(item.getName());
    }
}
