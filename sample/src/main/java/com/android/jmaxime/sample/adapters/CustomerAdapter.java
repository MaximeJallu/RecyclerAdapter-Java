package com.android.jmaxime.sample.adapters;

import com.android.jmaxime.adapters.RecyclerAdapter;
import com.android.jmaxime.sample.models.Customer;
import com.android.jmaxime.sample.viewholders.customers.Customer2ViewHolder;
import com.android.jmaxime.sample.viewholders.customers.CustomerViewHolder;

import java.util.List;

@SuppressWarnings("unused")
public class CustomerAdapter extends RecyclerAdapter<Customer> {

    public CustomerAdapter(List<Customer> TList) {
        super(TList, CustomerViewHolder.class);
        putViewType(1, Customer2ViewHolder.class, true);
    }

    @Override
    public int getItemViewType(int position) {
        Customer customer = getItem(position);
        if (customer.getName().startsWith("A")){
            return 1;
        }
        return super.getItemViewType(position);
    }
}
