# SDK Version
minSdkVersion = 16

# Gradle 
```
compile "com.github.maximejallu:adapters:1.10.1"
```
    
# RecyclerAdapter (Easy sample method)
CustomerViewHolder.class :
```java
@BindLayoutRes(R.layout.{name_of_your_layout})
public class CustomerViewHolder extends RecyclerViewHolder<Customer> {
    TextView mText;
    CustomerViewHolder(View view){
        super(view);
        mText = view.findViewById(R.id.text1);
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

//# Solution 1 : the object determines are own item view type 
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

public class MyFragment extends Fragment {
    
    public void onCreateView(){
        private RecyclerAdapter<Customer> mAdapter;
        
        mAdapter = new RecyclerAdapter(customerList, CustomerViewHolder1.class/*type par default*/);
        mAdapter.putViewType(Customer.TYPE_STORE, CustomerViewHolder2.class, null /*callback*/);
        mAdapter.putViewType(Customer.TYPE_OTHER, CustomerViewHolder3.class, true /*add default callback*/);
        
        //# Solution 2 : We give the strategy of the IViewType to our adapt it
        mAdapter.setItemViewType(item -> {
            //todo stategy type of item
            return 0;
        });
        mRecyclerView.setAdapter(adapter);
    }

}
```
# SectionDecorator (Recycler with LinearLayout)
precondition : create your RecyclerViewHolder
Sample : 
```java
RecyclerAdapter<Customer> baseAdapter = new RecyclerAdapter<>(...);
RecyclerSectionedAdapter adapter = new RecyclerSectionedAdapter(SectionViewHolder.class, baseAdapter);
```

# SectionDecorator (Recycler with GridLayout)
precondition : create your RecyclerViewHolder
Sample : 
```java
mRecylerView.setLayoutManager(...);
RecyclerAdapter<Customer> baseAdapter = new RecyclerAdapter<>(...);
RecyclerSectionedAdapter<String, Customer> sectionAdapter = new RecyclerSectionedAdapter<>(SectionViewHolder.class, mRecylerView, baseAdapter);

sectionAdapter.addSection(0/*position*/, "Title Section 1");
Customer i = sectionAdapter.getItem(1 /*sectioned position*/);

mRecylerView.setAdapter(sectionAdapter);
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
