package com.android.jmaxime.sample;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;

import com.android.jmaxime.adapters.RecyclerAdapter;
import com.android.jmaxime.interfaces.InitViewHolderDecorator;
import com.android.jmaxime.interfaces.ShowPictureDecorator;


public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RecyclerAdapter.Helper()
                .append(new InitViewHolderDecorator() {
                    @Override
                    public void initBinding(Object target, View view) {
//                        ButterKnife.bind(target, view);
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
