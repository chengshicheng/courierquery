package com.chengshicheng.courierquery.ResposeBean;

/**
 * Created by chengshicheng on 2017/1/15.
 */

public class StringUtils {

    public static boolean isEmpty(String string) {
        if (string == null) {
            return true;
        }
        String result = string.trim();
        if (result.length() == 0) {
            return true;
        } else {
            return false;
        }

    }
}
