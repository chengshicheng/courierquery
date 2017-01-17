package com.chengshicheng.courierquery.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.chengshicheng.courierquery.DialogUtils;
import com.chengshicheng.courierquery.LogUtil;
import com.chengshicheng.courierquery.QueryAPI.OrderTraceAPI;
import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.RequestBean.OrderTraceRequestData;
import com.chengshicheng.courierquery.ResposeBean.OrderTraceResponse;
import com.chengshicheng.courierquery.ResposeBean.ShipperBean;
import com.google.gson.Gson;

import static com.chengshicheng.courierquery.R.id.listView;

/*及时查询，显示物流结果
 * Created by chengshicheng on 2017/1/17.
 */

public class TraceResultActivity extends BaseActivity {

    private static TextView textView;
    private static ListView listView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_result);
        initViews();
//        String code = getIntent().getStringExtra("expCode");
//        String no = getIntent().getStringExtra("expNO");
//        doQueryTraceAPI(code, no);

    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
            }
        });
        listView = (ListView) findViewById(R.id.listView);
    }


    /**
     * doQueryAPI("YD", "1000797534377");
     *
     * @param code
     * @param num
     */
    private void doQueryTraceAPI(final String code, final String num) {

        /**
         * 即时查询线程
         */
        Runnable queryTrace = new Runnable() {

            @Override
            public void run() {
                try {
                    String result = OrderTraceAPI.getOrderTracesByJson(code, num);
                    LogUtil.PrintDebug(result);
                    Gson gson = new Gson();
                    OrderTraceResponse response = gson.fromJson(result, OrderTraceResponse.class);
                    if (response.isSuccess()) {
                        //查询成功
                        //// TODO: 2017/1/16
                    } else {
                    }

                } catch (Exception e) {
                    LogUtil.PrintError("doQueryAPI Error", e);
                }
            }
        };
        new Thread(queryTrace).start();
    }
}
