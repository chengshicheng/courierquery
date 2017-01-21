package com.chengshicheng.courierquery.QueryAPI;

import com.chengshicheng.courierquery.Utils.LogUtil;
import com.chengshicheng.courierquery.RequestBean.OrderTraceRequestData;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.chengshicheng.courierquery.Utils.StringUtils.AppKey;
import static com.chengshicheng.courierquery.Utils.StringUtils.EBusinessID;
import static com.chengshicheng.courierquery.Utils.StringUtils.encrypt;
import static com.chengshicheng.courierquery.Utils.StringUtils.urlEncoder;
import static com.chengshicheng.courierquery.WebService.sendPost;

/**
 * 及时查询订单轨迹
 * Query order logistics trajectory
 * Created by chengshicheng on 2017/1/13.
 */

public class OrderTraceAPI {

    //请求url
    private static final String ReqURL = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";
//    private String ReqURL = "http://api.kdniao.cc/api/dist";

    /**
     * Json方式 查询订单物流轨迹
     *
     * @throws Exception
     */
    public static String getOrderTracesByJson(String expCode, String expNo) throws Exception {
//        String requestData = "{'OrderCode':'','ShipperCode':'" + expCode + "','LogisticCode':'" + expNo + "'}";
        Gson gson = new Gson();
        String requestData = gson.toJson(new OrderTraceRequestData().getRequestData(expCode, expNo));
        LogUtil.PrintDebug("即时查询RequestData:" + requestData);


        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "1002");
        String dataSign = encrypt(requestData, AppKey, "UTF-8");
        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result = sendPost(ReqURL, params);

        //根据公司业务处理返回的信息......

        return result;
    }
}
