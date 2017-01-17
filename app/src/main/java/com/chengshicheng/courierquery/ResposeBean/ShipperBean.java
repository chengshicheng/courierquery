package com.chengshicheng.courierquery.ResposeBean;

/**
 * 单号识别Shipper
 * http://www.kdniao.com/api-recognise
 * Created by chengshicheng on 2017/1/16.
 */

public class ShipperBean {
    /**
     * 快递公司编码
     */
    private String ShipperCode;
    /**
     * 快递公司名称
     */
    private String ShipperName;

    public String getShipperCode() {
        return ShipperCode;
    }

    public String getShipperName() {
        return ShipperName;
    }
}