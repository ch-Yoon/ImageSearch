package com.ch.yoon.kakao.pay.imagesearch;

import android.app.Application;

public class App extends Application {

    private static App INSTANCE = null;

    public static App getAppContext() {
        if(INSTANCE == null) {
            throw new IllegalStateException("this application does not extends Application.class");
        }

        return INSTANCE;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        INSTANCE = null;
    }

}