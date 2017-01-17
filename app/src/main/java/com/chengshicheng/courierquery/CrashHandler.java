package com.chengshicheng.courierquery;

/**
 * Created by chengshicheng on 2017/1/18.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //处理异常
        LogUtil.PrintError("程序崩溃了", ex);
        //发送到服务器
        //dialog提醒
    }
}
