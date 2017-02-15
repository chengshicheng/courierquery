package com.chengshicheng.courierquery.Activity;

/**
 * 服务器回调接口
 * Created by chengshicheng on 2017/2/15.
 */

public interface WebCallBackListener {

    void onSuccess(String result);

    void onFailed();
}
