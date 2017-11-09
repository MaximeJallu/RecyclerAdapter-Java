# Status

![alt text](https://travis-ci.org/MaximeJallu/RecyclerAdapter-Java.svg?branch=develop) [![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)


[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.maximejallu/adapters/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.github.maximejallu/adapters)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RecyclerAdapter--Java-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/6400)

# Generic-Adapter:

This tool allows you to no longer worry about adapters. Now you will only create your ViewHolder.
Communication management between your Views and your ViewHolders is possible.
Creating sections is now very easily.
Enjoy.

# Download [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.maximejallu/adapters/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.github.maximejallu/adapters)
buildtool used is 27
use {exclude group: 'com.android.support'} only if you have problems
```groovy
dependencies {
    ...
    implementation ('com.github.maximejallu:adapters:{version}')
    ...
}
```

# Decorators for : [ButterKnife][1] - [Picasso][2] - [Glide][3] ...
```java
public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RecyclerAdapter.Helper()
                .append(new InitViewHolderDecorator() {
                    @Override
                    public void initBinding(Object target, View view) {
                        ButterKnife.bind(target, view);
                    }
                })
                .append(new ShowPictureDecorator() {
                    @Override
                    public void showPicture(ImageView picture, String url) {
                        //use Picasso, Glide... Other
                    }
                })
                .init();
    }
}
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
- create your necessary ViewHolder (here only one example)
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
# SectionDecorator (Recycler with LinearLayout or GridLayout)
precondition : create your RecyclerViewHolder
Sample : 
```java
mRecylerView.setLayoutManager(...);
/*create Adapter*/
RecyclerAdapter<Customer> baseAdapter = new RecyclerAdapter<>(...);
/*create sectioned adapter. the Adapter type can be RecyclerView.Adapter*/
SectionedAdapter<String, RecyclerAdapter> adapter = new SectionedAdapter<>(SectionViewHolder.class, baseAdapter);
/*add your sections*/
sectionAdapter.addSection(0/*position*/, "Title Section 1");
/*attach Adapter to RecyclerView*/
mRecylerView.setAdapter(sectionAdapter);
```

# ItemDecoration
```java
mRecyclerView.addItemDecoration(new FooterDecoration(getContext(), this, R.layout.item_space_80));
```

[1]: https://github.com/JakeWharton/butterknife
[2]: https://github.com/square/picasso
[3]: https://github.com/bumptech/glide