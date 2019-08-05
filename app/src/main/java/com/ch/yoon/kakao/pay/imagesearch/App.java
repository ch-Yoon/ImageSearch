package com.ch.yoon.kakao.pay.imagesearch;

import android.app.Application;
import android.content.Context;

import com.ch.yoon.kakao.pay.imagesearch.repository.local.ImageDatabase;

public class App extends Application {

    private static volatile App instance = null;

    public static App getAppContext() {
        if(instance == null) {
            throw new IllegalStateException("this application does not extends Application.class");
        }

        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        instance = null;
    }

}