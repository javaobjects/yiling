package com.yiling.b2b.app.order.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/** 预售活动信息
 * @author zhigang.guo
 * @date: 2022/11/18
 */
@Data
public class PresaleActivityInfoVO {


    @ApiModelProperty("定金金额")
    private BigDecimal depositAmount = BigDecimal.ZERO;

    @ApiModelProperty("尾款金额")
    private BigDecimal balanceAmount = BigDecimal.ZERO;

    /**
     * 预售类型（1-定金预售；2-全款预售）
     */
    @ApiModelProperty("预售类型（1-定金预售；2-全款预售）")
    private Integer presaleType;

    /**
     * 支付尾款开始时间
     */
    @ApiModelProperty("支付尾款开始时间")
    private Date finalPayBeginTime;

    /**
     * 支付尾款结束时间
     */
    @ApiModelProperty("支付尾款结束时间")
    private Date finalPayEndTime;

    /**
     * 促销方式：0-无 1-定金膨胀 2-尾款立减
     */
    @ApiModelProperty("促销方式：0-无 1-定金膨胀 2-尾款立减")
    private Integer goodsPresaleType;

    /**
     * 定金膨胀倍数
     */
    @ApiModelProperty("定金膨胀倍数")
    private BigDecimal expansionMultiplier;

    /**
     * 尾款立减金额
     */
    @ApiModelProperty("尾款立减金额")
    private BigDecimal finalPayDiscountAmount;
}
