package com.chengshicheng.courierquery;

import android.widget.Toast;

/**
 * Created by chengshicheng on 2017/1/15.
 */

public class DialogUtils {
    public static void ShowToast(String string) {
        Toast.makeText(CourierApp.getContext(), string, Toast.LENGTH_SHORT).show();
    }
}
