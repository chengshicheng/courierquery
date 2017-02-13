package com.chengshicheng.courierquery;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.chengshicheng.courierquery.GreenDao.GreenDaoHelper;

/**
 * Created by chengshicheng on 2017/1/15.
 */

public class CourierApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        CrashHandler handler = CrashHandler.getInstance();
        handler.init(this);
        GreenDaoHelper.initDatabase();
    }

    public static Context getContext() {
        return context;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
