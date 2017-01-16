package com.chengshicheng.courierquery.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.chengshicheng.courierquery.DialogUtils;
import com.chengshicheng.courierquery.LogUtil;
import com.chengshicheng.courierquery.QueryAPI.QueryOrderTraceAPI;
import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.ResposeBean.OrderTraceResponse;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by chengshicheng on 2017/1/12.
 */

public class ChooseCompanyActivity extends BaseActivity {

    private static int requestCode;
    private static String expCode = "YD";
    private static String expNO = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initViews();
        Intent intent = getIntent();
        doRequest(intent);
    }


    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.ShowToast("返回");
            }
        });


    }

    private void doRequest(Intent intent) {
        requestCode = intent.getIntExtra("requestType", 0);
        LogUtil.PrintDebug("requestType = " + requestCode);
        if (1 == requestCode) {
            expNO = intent.getStringExtra("requestNumber");
            //TODO
            //查询快递公司
            LogUtil.PrintDebug("requestNumber = " + expNO);
        } else if (2 == requestCode) {
            toCustomScanActivity();
        } else {

        }
    }

    /**
     * 跳转到扫描二维码界面
     * https://github.com/journeyapps/zxing-android-embedded
     */
    private void toCustomScanActivity() {
//         zxinging自带的扫描界面
//        IntentIntegrator.forSupportFragment(Fragment1.this).initiateScan();

        //重定义zxing的扫描界面 CustomScanActivity
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.setPrompt("");
        integrator.setCaptureActivity(CustomScanActivity.class);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    /**
     * 获取扫描结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                LogUtil.PrintDebug("Scan Cancelled");
            } else {
                LogUtil.PrintDebug("Scan Succeed : " + result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.drawable.back:
                this.finish();
                break;
            case R.id.scanImage:
                toCustomScanActivity();
                break;
            case R.id.searchButton:
                doQueryAPI(expCode, expNO);
                break;

        }
    }

    private void doQueryAPI(final String code, final String num) {

        /**
         * 即时查询线程
         */
        Runnable downloadRun = new Runnable() {

            @Override
            public void run() {
                QueryOrderTraceAPI api = new QueryOrderTraceAPI();
                try {
                    String result = api.getOrderTracesByJson(code, num);
                    LogUtil.PrintDebug(result);
                    Gson gson = new Gson();
                    OrderTraceResponse response = gson.fromJson(result, OrderTraceResponse.class);
                    Looper.prepare();
                    if (response.isSuccess()) {
                        //查询成功
                        DialogUtils.ShowToast("查询成功:" + response.getTraces().toString());


                    } else {
                        DialogUtils.ShowToast("查询失败:" + response.getReason());
                    }

                } catch (Exception e) {
                    LogUtil.PrintError("doQueryAPI Error", e);
                }
            }
        };
        new Thread(downloadRun).start();

    }

}
