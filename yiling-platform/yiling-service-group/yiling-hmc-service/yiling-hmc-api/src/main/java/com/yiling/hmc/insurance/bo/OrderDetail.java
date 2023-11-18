package com.yiling.hmc.insurance.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {

    /**
     * 渠道方订单号
     */
    private String channelOrderId;

    /**
     * 渠道方编码
     */
    private String channelCode;

    /**
     * 是否为中药 0否 1是
     */
    private Integer isTraditional;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 收货省份
     */
    private String province;

    /**
     * 收货省份编码
     */
    private String provinceCode;

    /**
     * 收货城市
     */
    private String city;

    /**
     * 收货城市编码
     */
    private String cityCode;

    /**
     * 收货区县
     */
    private String county;

    /**
     * 收货区县编码
     */
    private String countyCode;

    /**
     * 收货详细地址
     */
    private String receiveAddress;

    /**
     * 取药方式 0-到店自提1-送药上门
     */
    private Integer receiveWay;

    /**
     * 配送方式 0-到店自提;1-同城配送;2-快递配送
     */
    private Integer deliverway;

    /**
     * 配送方式为2-快递配送为必填
     */
    private String expressName;

    /**
     * 物流单号 配送方式为2-快递配送为必填
     */
    private String expressNo;

    /**
     * 药店名称
     */
    private String drugstoreName;

    /**
     * 药店地址
     */
    private String drugstoreAddress;

    /**
     * 购药订单状态 0新订单
     */
    private Integer orderStatus;

    /**
     * 订单药品市场总价（元）
     */
    private String orderTotalPrices;

    /**
     * 订单药品折后总价（元）
     */
    private String orderDiscountPrices;

    /**
     * 订单备注
     */
    private String note;

    /**
     * 订单备注
     */
    private List<DrugDto> drugList;

}

