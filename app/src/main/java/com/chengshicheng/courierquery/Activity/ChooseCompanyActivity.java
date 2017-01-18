package com.chengshicheng.courierquery.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chengshicheng.courierquery.DialogUtils;
import com.chengshicheng.courierquery.LogUtil;
import com.chengshicheng.courierquery.QueryAPI.OrderDistinguishAPI;
import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.ResposeBean.OrderDistinguishResponse;
import com.chengshicheng.courierquery.ResposeBean.ShipperBean;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * Created by chengshicheng on 2017/1/12.
 */

public class ChooseCompanyActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static int requestCode;

    /**
     * 快递公司编号
     */
    private static String expCode = "YD";
    /**
     * 快递单号
     */
    private static String expNO = "";

    private static ArrayList<ShipperBean> shippers = new ArrayList<ShipperBean>();//适配器数据源
    private static ArrayList<ShipperBean> scanShippers;//单号识别返回的的结果
    private ListView listView;
    private static ChooseCompanyAdapter adapter;
    /**
     * 单号查询失败
     */
    private static final int QUERY_FAILED = 0;
    /**
     * 单号查询成功，但是shipper为空
     */
    private static final int EMPTY_SHIPPER = 1;
    /**
     * 单号查询成功
     */
    private static final int QUERY_SUCCESS = 2;
    /**
     * 系统错误：服务器接口异常等
     */
    private static final int SYSTEM_ERROR = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QUERY_FAILED:
                    int errCode = (int) (msg.obj);
                    break;
                case EMPTY_SHIPPER:
                    break;
                case QUERY_SUCCESS:
                    scanShippers = (ArrayList<ShipperBean>) msg.obj;
                    LogUtil.PrintDebug("ShipperBean size = " + scanShippers.size());
                    refreshListView();
                    break;
                default:
                    break;
            }
        }
    };

    private void refreshListView() {
        shippers.clear();
        shippers.addAll(scanShippers);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_company);
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
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        listView.setDivider(new ColorDrawable(Color.GRAY));
        listView.setDividerHeight(1);
        adapter = new ChooseCompanyAdapter(ChooseCompanyActivity.this, shippers);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DialogUtils.ShowToast("点击" + position);
        Intent intent = new Intent();
        intent.setClass(this, TraceResultActivity.class);
        intent.putExtra("expCode", scanShippers.get(position).getShipperCode());
        intent.putExtra("expNO", expNO);
        startActivity(intent);
    }

    private void doRequest(Intent intent) {
        requestCode = intent.getIntExtra("requestType", 0);
        LogUtil.PrintDebug("requestType = " + requestCode);
        if (1 == requestCode) {
            expNO = intent.getStringExtra("requestNumber");
            LogUtil.PrintDebug("requestNumber = " + expNO);
            //// TODO: 2017/1/16  查询完毕后刷新界面
            //查询快递公司
            doQueryCompany(expNO);
        } else if (2 == requestCode) {
            toCustomScanActivity();
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


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.searchButton://开始即时查询
                //// TODO: 2017/1/16 跳转到查询结果界面
//                doQueryTraceAPI(expCode, expNO);
                break;
        }
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
                finish();

            } else {
                expNO = result.getContents();
                LogUtil.PrintDebug("Scan Succeed : " + expNO);
                doQueryCompany(expNO);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 单号识别，根据单号查询物流公司
     *
     * @param num
     */
    private void doQueryCompany(final String num) {

        /**
         * 即时查询线程
         */
        Runnable queryTrace = new Runnable() {
            Message message = Message.obtain();

            @Override
            public void run() {
                try {
                    String result = OrderDistinguishAPI.getCompanyByJson(num);
                    LogUtil.PrintDebug(result);
                    Gson gson = new Gson();
                    OrderDistinguishResponse response = gson.fromJson(result, OrderDistinguishResponse.class);

                    if (response.isSuccess()) {
                        //查询成功
                        ArrayList<ShipperBean> shippers = response.getShippers();
                        if (shippers.size() == 0) {
                            message.what = EMPTY_SHIPPER;
                        } else {
                            message.what = QUERY_SUCCESS;
                            message.obj = shippers;
                        }
                    } else {
                        message.what = QUERY_FAILED;
                        message.obj = response.getCode();//失败错误码
                    }

                } catch (Exception e) {
                    LogUtil.PrintError("OrderDistinguishAPI Error", e);
                    message.what = SYSTEM_ERROR;
                } finally {
                    handler.sendMessage(message);
                }
            }
        };
        new Thread(queryTrace).start();
    }


}
