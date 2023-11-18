package com.yiling.common.web.goods.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 PresaleInfoVO
 * @描述
 * @创建时间 2022/10/13
 * @修改人 shichen
 * @修改时间 2022/10/13
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PresaleInfoVO extends BaseVO {
    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String name;

    /**
     * 活动分类（1-平台活动；2-商家活动；）
     */
    @ApiModelProperty("活动分类（1-平台活动；2-商家活动；）")
    private Integer sponsorType;

    /**
     * 生效开始时间
     */
    @ApiModelProperty("生效开始时间")
    private Date beginTime;

    /**
     * 生效结束时间
     */
    @ApiModelProperty("生效结束时间")
    private Date endTime;


    /**
     * 其他(1-新客适用,多个值用逗号隔开)
     */
    @ApiModelProperty("其他(1-新客适用,多个值用逗号隔开)")
    private String conditionOther;

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
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private Long goodsId;

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long eid;


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
     * 定金
     */
    @ApiModelProperty("定金")
    private BigDecimal  deposit;

    /**
     * 尾款
     */
    @ApiModelProperty("尾款")
    private BigDecimal  finalPayment;

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
