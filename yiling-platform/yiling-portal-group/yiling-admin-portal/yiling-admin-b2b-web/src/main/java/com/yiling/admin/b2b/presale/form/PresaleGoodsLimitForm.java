package com.yiling.admin.b2b.presale.form;

import java.math.BigDecimal;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shixing.sun
 * @date: 2022/10/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PresaleGoodsLimitForm extends BaseForm {
    /**
     * 预售活动id
     */
    @ApiModelProperty("预售活动id")
    private Long marketingStrategyId;

    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;


    /**
     * 预售价
     */
    @ApiModelProperty("预售价")
    private BigDecimal presaleAmount;

    /**
     * 定金比例
     */
    @ApiModelProperty("定金比例")
    private BigDecimal depositRatio;

    /**
     * 促销方式：0-无 1-定金膨胀 2-尾款立减
     */
    @ApiModelProperty("促销方式：0-无 1-定金膨胀 2-尾款立减")
    private Integer presaleType;

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

    /**
     * 每人最小预定量
     */
    @ApiModelProperty("每人最小预定量")
    private Integer minNum;

    /**
     * 每人最大预定量
     */
    @ApiModelProperty("每人最大预定量")
    private Integer maxNum;

    /**
     * 合计最大预定量
     */
    @ApiModelProperty("合计最大预定量")
    private Integer allNum;
}
