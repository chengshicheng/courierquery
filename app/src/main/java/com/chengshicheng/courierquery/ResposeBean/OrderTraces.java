package com.chengshicheng.courierquery.ResposeBean;

/**
 * 及时查询物流跟踪
 * Traces
 * Created by chengshicheng on 2017/1/15.
 * http://www.kdniao.com/api-track
 */
public class OrderTraces {
    /**
     * 时间
     */
    private String AcceptTime;
    /**
     * 描述
     */
    private String AcceptStation;
    /**
     * 备注
     */
    private String Remark;

    public String getAcceptStation() {
        return AcceptStation;
    }

    public String getAcceptTime() {
        return AcceptTime;
    }

    public String getRemark() {
        return Remark;
    }
}
