package com.chengshicheng.courierquery.Web.QueryAPI;

import com.chengshicheng.courierquery.Utils.LogUtil;

import java.util.HashMap;
import java.util.Map;

import static com.chengshicheng.courierquery.Utils.StringUtils.AppKey;
import static com.chengshicheng.courierquery.Utils.StringUtils.EBusinessID;
import static com.chengshicheng.courierquery.Utils.StringUtils.encrypt;
import static com.chengshicheng.courierquery.Utils.StringUtils.urlEncoder;
import static com.chengshicheng.courierquery.Web.WebService.sendPost;

/**
 * Created by chengshicheng on 2017/1/16.
 */

public class OrderDistinguishAPI {
    //    API测试地址：http://testapi.kdniao.cc:8081/Ebusiness/EbusinessOrderHandle.aspx
//    API正式地址：http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx
    //测试地址返回shipper为空  2017-01-16，采用正式地址
    //请求url
    private static final String ReqURL = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";

    /**
     * 单号识别
     *
     * @throws Exception
     */
    public static String getCompanyByJson(String expNo) throws Exception {
        String requestData = "{'LogisticCode':'" + expNo + "'}";
        LogUtil.PrintDebug("单号识别RequestData:" + requestData);

        Map<String, String> params = new HashMap<String, String>();
        params.put("RequestData", urlEncoder(requestData, "UTF-8"));
        params.put("EBusinessID", EBusinessID);
        params.put("RequestType", "2002");
        String dataSign = encrypt(requestData, AppKey, "UTF-8");
        params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
        params.put("DataType", "2");

        String result = sendPost(ReqURL, params);

        //根据公司业务处理返回的信息......

        return result;
    }
}
