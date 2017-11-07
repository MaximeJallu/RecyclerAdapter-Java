package com.android.jmaxime.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.android.jmaxime.adapters.RecyclerAdapter;
import com.android.jmaxime.adapters.decorators.SectionedAdapter;
import com.android.jmaxime.sample.models.A;
import com.android.jmaxime.sample.models.B;
import com.android.jmaxime.sample.models.Customer;
import com.android.jmaxime.sample.viewholders.A_Bis_ViewHolder;
import com.android.jmaxime.sample.viewholders.A_ViewHolder;
import com.android.jmaxime.sample.viewholders.B_ViewHolder;
import com.android.jmaxime.sample.viewholders.EmptyViewHolder;
import com.android.jmaxime.sample.viewholders.customers.Customer2ViewHolder;
import com.android.jmaxime.sample.viewholders.customers.CustomerViewHolder;
import com.android.jmaxime.viewholder.Container;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecycler;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setMultiAdapter();
                    return true;
                case R.id.navigation_dashboard:
                    setSimpleMultiAdapter();
                    return true;
                case R.id.navigation_notifications:
                    setSectionAdapter();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecycler = findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Alice"));
        customers.add(new Customer("Béatrice"));
        customers.add(new Customer("Mathilde"));

        RecyclerAdapter<Customer> adapter = new RecyclerAdapter<>(customers, CustomerViewHolder.class);
        adapter.putViewType(1, Customer2ViewHolder.class, false);
        adapter.setViewTypeStrategy(item -> {
            if (item.getName().startsWith("A")){
                return 1;
            }
            return 0;
        });
        mRecycler.setAdapter(adapter);
    }

    private void setMultiAdapter() {
        List<Container> containers = new ArrayList<>();
        containers.add(new Container(new A("Je suis ONE")));
        containers.add(new Container());
        containers.add(new Container(new A("Je suis ONE+")));
        containers.add(new Container(new B("je suis TWO")));
        containers.add(new Container(new A("Je suis ONE++")));
        containers.add(new Container(new B("je suis TWO+")));
        containers.add(new Container(new B("je suis TWO++")));
        containers.add(new Container(new A("Je suis ONE3+")));
        containers.add(new Container(new A("Je suis ONE4+")));
        containers.add(new Container(new B("je suis TWO3+")));
        containers.add(new Container(new B("je suis TWO4+")));
        containers.add(new Container());
        containers.add(new Container());
        containers.add(new Container(new B("je suis TWO5+")));
        containers.add(new Container());

        RecyclerAdapter<Container> multiTypeAdapter = new RecyclerAdapter<>(containers, A_ViewHolder.class);
        multiTypeAdapter.putViewType(2, B_ViewHolder.class, true);
        multiTypeAdapter.putViewType(3, EmptyViewHolder.class, true);
        multiTypeAdapter.setViewTypeStrategy(item -> {
            if (item.getValue() == null) {
                return 3;
            }
            return (item.getValue() instanceof A) ? 1 : 2;
        });
        mRecycler.setAdapter(multiTypeAdapter);
    }

    private void setSimpleMultiAdapter() {
        List<A> list = new ArrayList<>();
        list.add(new A("Alibaba"));
        list.add(new A("Allouette "));

        RecyclerAdapter<A> adapter = new RecyclerAdapter<>(list, A_ViewHolder.class);
        adapter.putViewType(1, A_Bis_ViewHolder.class, true);
        adapter.setViewTypeStrategy(item -> {
            if (item.getName().length() > 7) {
                return 1;
            }
            return 0;
        });
        mRecycler.setAdapter(adapter);
    }


    private void setSectionAdapter(){
        mRecycler.getAdapter().notifyDataSetChanged();
        List<A> list = new ArrayList<>();
        list.add(new A("Alibaba"));
        list.add(new A("Allouette "));

        RecyclerAdapter<A> adapter = new RecyclerAdapter<>(list, A_ViewHolder.class);
        SectionedAdapter<A, RecyclerAdapter> sectionedAdapter = new SectionedAdapter<>(A_Bis_ViewHolder.class, adapter);
        sectionedAdapter.addSection(0, new A("SECTION ONE"));
        sectionedAdapter.addSection(1, new A("SECTION TWO"));
//        mRecycler.setAdapter(sectionedAdapter);
    }
}
