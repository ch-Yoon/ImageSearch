package com.ch.yoon.kakao.pay.imagesearch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ch.yoon.kakao.pay.imagesearch.App;

public class NetworkUtil {

    public static  boolean isNetworkConnecting() {
        Context context = App.getAppContext();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            return networkInfo != null && networkInfo.isConnected();
        }

        return false;
    }

}