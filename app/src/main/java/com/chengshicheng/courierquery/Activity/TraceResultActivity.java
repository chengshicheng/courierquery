package com.chengshicheng.courierquery.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.chengshicheng.courierquery.LogUtil;
import com.chengshicheng.courierquery.QueryAPI.OrderTraceAPI;
import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.ResposeBean.OrderTraceResponse;
import com.chengshicheng.courierquery.ResposeBean.OrderTraces;
import com.google.gson.Gson;

import java.util.ArrayList;

/*及时查询，显示物流结果
 * Created by chengshicheng on 2017/1/17.
 */

public class TraceResultActivity extends BaseActivity {

    private static TextView textView;


    private static final int QUERY_FAILED = 0;

    private static final int API_EXCEPTION = 1;

    private static final int QUERY_SUCCESS = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QUERY_SUCCESS:
                    OrderTraceResponse response = (OrderTraceResponse) msg.obj;
                    showTraces(response);
                    break;
                case QUERY_FAILED:
                    OrderTraceResponse response_error = (OrderTraceResponse) msg.obj;
                    textView.setText("查询失败" + response_error.getReason());
                    break;
                case API_EXCEPTION:
                    textView.setText("服务器异常，请重试");
                    break;
            }
        }
    };

    private void showTraces(OrderTraceResponse response) {
        StringBuffer result = new StringBuffer();
        //物流状态：2-在途中,3-签收,4-问题件
//        String state = (null == response.getState()) ? "" : response.getState();
        String state = null;
        ArrayList<OrderTraces> traces = response.getTraces();
        switch (state) {
            case "2":
            case "3":
                if (traces.size() > 0) {
                    result.append("查询成功" + "\r\n" + "\r\n");
                    for (OrderTraces trace : traces) {
                        result.append(trace.getAcceptTime());
                        result.append("\r\n");
                        result.append(trace.getAcceptStation());
                        result.append("\r\n");
                    }
                } else {
                    result.append("查询成功,暂无物流信息" + "\r\n" + "\r\n");
                }
                break;
            case "4":
                result.append("问题件" + "\r\n" + "\r\n");
                break;
            //0
            default:
                result.append("未知状态,无物流信息" + "\r\n" + "\r\n");
                break;
        }

        textView.setText(result.toString());
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_result);
        initViews();
        String expCode = getIntent().getStringExtra("expCode");
        String expNO = getIntent().getStringExtra("expNO");
        doQueryTraceAPI(expCode, expNO);
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
        textView = (TextView) findViewById(R.id.result);
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
            Message message = Message.obtain();

            @Override
            public void run() {
                try {
                    String result = OrderTraceAPI.getOrderTracesByJson(code, num);
                    LogUtil.PrintDebug(result);
                    Gson gson = new Gson();
                    OrderTraceResponse response = gson.fromJson(result, OrderTraceResponse.class);
                    message.obj = response;
                    if (response.isSuccess()) {
                        //查询成功
                        message.what = QUERY_SUCCESS;
                    } else {
                        message.what = QUERY_FAILED;
                    }

                } catch (Exception e) {
                    message.what = API_EXCEPTION;
                    LogUtil.PrintError("doQueryAPI Error", e);
                } finally {
                    handler.sendMessage(message);
                }
            }
        };
        new Thread(queryTrace).start();
    }
}
