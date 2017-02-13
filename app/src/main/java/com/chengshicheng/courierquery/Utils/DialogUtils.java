package com.chengshicheng.courierquery.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.chengshicheng.courierquery.CourierApp;
import com.chengshicheng.courierquery.R;

/**
 * Created by chengshicheng on 2017/1/15.
 */

public class DialogUtils {
    public static void ShowToast(String string) {
        Toast.makeText(CourierApp.getContext(), string, Toast.LENGTH_SHORT).show();
    }

    /**
     * 创建列表对话框
     *
     * @param context     上下文 必填
     * @param title       标题 必填
     * @param itemsString 列表项 必填
     * @return
     */
    public static Dialog createListDialog(Context context, String title, String[] itemsString,
                                          DialogInterface.OnClickListener itemClickListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(itemsString, itemClickListener);
        dialog = builder.create();

        return dialog;
    }

}
