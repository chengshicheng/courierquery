package com.chengshicheng.courierquery;

import android.content.Context;
import android.widget.Toast;

import static android.R.attr.id;

/**
 * Created by chengshicheng on 2017/1/15.
 */

public class DialogUtils {
    public static void ShowToast(String string) {
        Toast.makeText(CourierApp.getContext(), string, Toast.LENGTH_SHORT).show();
    }
}
