package com.yiling.hmc.insurance.bo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/7/5
 */
@Data
public class TkOrderDetailBO implements Serializable {

    /**
     * 渠道方订单号
     */
    private String channelOrderId;

    /**
     * 渠道方编码
     */
    private String channelCode;

    /**
     * 是否为中药    0否 1是
     */
    private Integer isTraditional;

    //    /**
    //     * 收货人
    //     */
    //    private String receiver;
    //
    //    /**
    //     * 联系电话
    //     */
    //    private String contactNumber;
    //
    //    /**
    //     * 收货省份
    //     */
    //    private String province;
    //
    //    /**
    //     * 收货省份编码
    //     */
    //    private String provinceCode;
    //
    //    /**
    //     * 收货城市
    //     */
    //    private String city;
    //
    //    /**
    //     * 收货城市编码
    //     */
    //    private String cityCode;
    //
    //    /**
    //     * 收货区县
    //     */
    //    private String county;
    //
    //    /**
    //     * 收货区县编码
    //     */
    //    private String countyCode;
    //
    //    /**
    //     * 收货详细地址
    //     */
    //    private String receiveAddress;

    /**
     * 取药方式 0-到店自提1-送药上门
     */
    private Integer receiveWay;

    /**
     * 0-到店自提;1-同城配送;2-快递配送
     */
    private Integer deliverway;

    /**
     * 药店名称
     */
    private String drugstoreName;

    /**
     * 药店地址
     */
    private String drugstoreAddress;

    /**
     * 购药订单状态   0新订单, 11待支付,12支付成功,13支付失败,14支付取消,15支付退款,21待发货,22待收货,23签收成功,24发货失败,25拒收,26退货
     */
    private Integer orderStatus;

    /**
     * 订单药品市场总价 （元）
     */
    private String orderTotalPrices;

    /**
     * 药品信息集合
     */
    private List<TkDrugBO> drugList;
}
