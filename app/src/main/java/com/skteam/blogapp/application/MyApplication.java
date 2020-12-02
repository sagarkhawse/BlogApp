/*
 * Copyright (c) Ishant Sharma
 * Android Developer
 * ishant.sharma1947@gmail.com
 * 7732993378
 */

package com.skteam.blogapp.application;

import android.app.Application;
import android.content.IntentFilter;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.skteam.blogapp.brodcastReceivers.ConnectionReceiver;
import com.skteam.blogapp.prefrences.SharedPre;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;


public class MyApplication extends Application implements LifeCycleDelegate {

    private static MyApplication mInstance;
    private ConnectionReceiver connectionReceiver;
    private IntentFilter networkintentFilter = new IntentFilter();
    private CookieManager cookieManager;

    @Override
    public void onCreate() {
        super.onCreate();
        connectionReceiver = new ConnectionReceiver();
        cookieManager=new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        HttpLoggingInterceptor logInter = new HttpLoggingInterceptor();
        logInter.setLevel(HttpLoggingInterceptor.Level.BODY);
        networkintentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(logInter)
                .readTimeout(30, TimeUnit.SECONDS)
                . writeTimeout(60, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(this,okHttpClient);
        AppLifecycleHandler lifeCycleHandler = new AppLifecycleHandler(this);
        registerLifecycleHandler(lifeCycleHandler);
        registerReceiver(connectionReceiver, networkintentFilter);
        mInstance = this;
        VMExceptionHandler.install();
    }


    public void setConnectionListener(ConnectionReceiver.ConnectionReceiverListener listener) {
        ConnectionReceiver.connectionReceiverListener = listener;
    }


    private void registerLifecycleHandler(AppLifecycleHandler lifeCycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler);
        registerComponentCallbacks(lifeCycleHandler);
    }

    @Override
    public void onAppBackgrounded() {
        SharedPre.getInstance(this).setIsAppBackground(true);
//        unregisterReceiver(connectionReceiver);
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(connectionReceiver);

    }

    @Override
    public void onAppForegrounded() {
        SharedPre.getInstance(this).setIsAppBackground(false);
    }
}
