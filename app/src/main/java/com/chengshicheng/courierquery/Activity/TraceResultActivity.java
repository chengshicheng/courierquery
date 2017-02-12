package com.chengshicheng.courierquery.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.chengshicheng.courierquery.GreenDao.GreenDaoHelper;
import com.chengshicheng.courierquery.GreenDao.OrderQuery;
import com.chengshicheng.courierquery.Utils.LogUtil;
import com.chengshicheng.courierquery.R;
import com.chengshicheng.courierquery.QueryAPI.OrderTraceAPI;
import com.chengshicheng.courierquery.ResposeBean.OrderTraceResponse;
import com.chengshicheng.courierquery.ResposeBean.OrderTrace;
import com.chengshicheng.greendao.gen.OrderQueryDao;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 及时查询，显示物流结果
 * Created by chengshicheng on 2017/1/17.
 */

public class TraceResultActivity extends BaseActivity {
    private TextView tvCompany, tvOrderNum, tvOrdrrState;
    private String expCode, expName, expNO;
    private static String state;
    private static ListView lvTrace;
    private TraceResultAdapter resultAdapter;
    private ArrayList<OrderTrace> tracesList = new ArrayList<OrderTrace>();
    private OrderQueryDao mOrderDao;


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
                    tvOrdrrState.setText("查询失败：" + response_error.getReason());
                    tvOrdrrState.setTextColor(getResources().getColor(R.color.tabBlue));
                    break;
                case API_EXCEPTION:
                    tvOrdrrState.setText("查询失败,请稍后查询");
                    tvOrdrrState.setTextColor(getResources().getColor(R.color.tabBlue));
                    break;
            }
        }
    };

    /**
     * 保存到数据库
     *
     * @param response
     */
    private void insertToDataBase(OrderTraceResponse response) {
        mOrderDao = GreenDaoHelper.getDaoSession().getOrderQueryDao();
        OrderQuery save = new OrderQuery();
        save.setOrderNum(Long.valueOf(response.getLogisticCode()));
        save.setOrderCode(response.getShipperCode());
        save.setOrderName(expName);
        save.setLastQueryTime(System.currentTimeMillis());
        save.setIsSuccess(response.isSuccess());
        save.setState(response.getState());
        save.setRemark("我是备注");

        Gson gson = new Gson();
        String traces = gson.toJson(response.getTraces());
        LogUtil.PrintDebug("-----------------" + traces);
        save.setTraces2Json(traces);
        mOrderDao.insertOrReplace(save);

        OrderQuery query = mOrderDao.queryBuilder().where(OrderQueryDao.Properties.OrderCode.eq("YD")).unique();
        if (query != null) {
            LogUtil.PrintDebug(query.getOrderNum() + "");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_result);
        expCode = getIntent().getStringExtra("expCode");
        expName = getIntent().getStringExtra("expName");
        expNO = getIntent().getStringExtra("expNO");
        initViews();
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

        tvCompany = (TextView) findViewById(R.id.tvOrderCompany);
        tvOrderNum = (TextView) findViewById(R.id.tvOrderNumber);
        tvOrdrrState = (TextView) findViewById(R.id.tvOrderState);
        tvCompany.setText("快递公司：" + expName);
        tvOrderNum.setText("运单号码：" + expNO);
        lvTrace = (ListView) findViewById(R.id.lvTrace);
        resultAdapter = new TraceResultAdapter(this, tracesList);
        lvTrace.setAdapter(resultAdapter);
    }


    private void showTraces(OrderTraceResponse response) {
        //物流状态：2-在途中,3-签收,4-问题件,0 暂无物流轨迹
        state = (null == response.getState()) ? "" : response.getState();
        ArrayList<OrderTrace> traces = response.getTraces();
        switch (state) {
            case "0":
                tvOrdrrState.setText("暂无物流信息");
                insertToDataBase(response);
                break;
            case "2":
                tvOrdrrState.setText("在途中");
                insertToDataBase(response);
                break;
            case "3":
                tvOrdrrState.setText("已签收");
                insertToDataBase(response);
                break;
            case "4":
                tvOrdrrState.setText("问题件");
                insertToDataBase(response);
                break;
            default:
                tvOrdrrState.setText("没有物流信息，请检查单号");
                break;
        }
        tvOrdrrState.setTextColor(getResources().getColor(R.color.colorAccent));
        Collections.reverse(traces);
        tracesList.addAll(traces);
        resultAdapter.notifyDataSetChanged();


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

    @Override
    public void onBackPressed() {
        if (state.isEmpty())
            setResult(RESULT_CANCELED);
        else {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }
}
