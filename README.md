# Gradle 
```
download last release here : https://github.com/MaximeJallu/RecyclerAdapter-Java/releases
compile ('libs/recycleradapter-{version}.aar', {
        exclude group: 'com.android.support', module: 'support-v4'
        exclude group: 'com.android.support', module: 'design'
        exclude group: 'com.jakewharton', module: 'butterknife'
        exclude group: 'com.squareup.picasso', module: 'picasso'
    })
 ```
    
# RecyclerAdapter (Easy sample method)
CustomerViewHolder.class :
```java
@BindLayoutRes(R.layout.{name_of_your_layout})
public class CustomerViewHolder extends RecyclerViewHolder<Customer> {
    CustomerViewHolder(View view){
        super(view);
    }
    
    void onBind(Customer item){
        //todo implements
    }
}
```

MainFragment.class
```java
public class MainFragment extends Fragment {
...
private RecyclerAdapter<Customer> mAdapter;

void onCreate(...){
    mAdapter = new RecyclerAdapter(customerList, CustomerViewHolder.class);
}
...
}
```
# RecyclerAdapter (Multi cell method)
```java
@BindLayoutRes(R.layout.{name_of_your_layout1})
public class CustomerViewHolder1 extends RecyclerViewHolder<Customer> {
    CustomerViewHolder1(View view){
        super(view);
    }
    
    void onBind(Customer item){
        //todo implements
    }
}

@BindLayoutRes(R.layout.{name_of_your_layout2})
public class CustomerViewHolder2 extends RecyclerViewHolder<Customer> {
    CustomerViewHolder2(View view){
        super(view);
    }
    
    void onBind(Customer item){
        //todo implements
    }
}

public class Customer implements IViewType {
   public static final int TYPE_ON_LINE = 0; /*default*/
   public static final int TYPE_STORE = 1;
   public static final int TYPE_OTHER = 2;
   private int mType;
   
   @Override
    public int getItemViewType() {
        return mType;
    }
}

private RecyclerAdapter<Customer> mAdapter;

mAdapter = new RecyclerAdapter(customerList, CustomerViewHolder1.class/*type par default*/);
mAdapter.putViewType(Customer.TYPE_STORE, CustomerViewHolder2.class);
mAdapter.putViewType(Customer.TYPE_OTHER, CustomerViewHolder3.class);
mRecyclerView.setAdapter(adapter);
```

# ArrayRecyclerAdapter (other method)
Sample : 
```java
public class SampleAdapter extends ArrayRecyclerAdapter<String,SampleAdapter.ItemViewHolder> {

    public SampleAdapter(@NonNull List<String> list) {
        super(list);
    }

    @Override public SampleAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.training_viewholder, parent, false));
    }
    /**
     * Training ViewHolder
     */
    class ItemViewHolder extends RecyclerViewHolder<String> {

        @BindView(R.id.training_note_label)
        TextView mTextView;

        /**
         * This super() auto BindViews with ButterKnife<br/>
         *
         * @param itemView the Views holder
         */
        public ItemViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * Update the view with data
         *
         * @param note data
         */
        @Override
        public void bind(String note) {
            mTextView.setText(note);
        }
    }
}
```

# ItemDecoration
```java
mRecyclerView.addItemDecoration(new FooterDecoration(getContext(), this, R.layout.item_space_80));
```
