package com.chengshicheng.courierquery;

import android.app.Application;
import android.content.Context;

/**
 * Created by chengshicheng on 2017/1/15.
 */

public class CourierApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
    }

    public static Context getContext() {
        return context;
    }
}
