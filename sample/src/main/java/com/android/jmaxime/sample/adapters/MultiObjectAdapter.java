package com.android.jmaxime.sample.adapters;

import com.android.jmaxime.adapters.RecyclerAdapter;
import com.android.jmaxime.sample.models.ObjectOne;
import com.android.jmaxime.sample.models.ObjectTwo;
import com.android.jmaxime.sample.viewholders.OneViewHolder;
import com.android.jmaxime.sample.viewholders.TwoViewHolder;

import java.util.List;


public class MultiObjectAdapter extends RecyclerAdapter {

    public MultiObjectAdapter(List TList) {
        super(TList, OneViewHolder.class);
        putViewType(1, TwoViewHolder.class, true);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        if (item instanceof ObjectOne){
            return 1;
        }else if (item instanceof ObjectTwo){
            return 2;
        }
        return super.getItemViewType(position);
    }
}
