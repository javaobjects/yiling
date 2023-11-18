package com.yiling.admin.b2b.presale.vo;

import java.math.BigDecimal;
import java.util.Date;
import com.yiling.framework.common.base.BaseVO;

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
public class PresaleGoodsLimitVO extends BaseVO {

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
     * 生产厂家
     */
    @ApiModelProperty("生产厂家")
    private String manufacturer;

    /**
     * 商品类型，字典：standard_goods_type（1-普通药品；2-中药饮片；3-中药材；4-消杀；5-健食品；6-食品）
     */
    @ApiModelProperty("商品类型，字典：standard_goods_type（1-普通药品；2-中药饮片；3-中药材；4-消杀；5-健食品；6-食品）")
    private Integer goodsType;

    /**
     * 销售规格
     */
    @ApiModelProperty("销售规格")
    private String sellSpecifications;

    /**
     * 商品基价
     */
    @ApiModelProperty("商品基价")
    private BigDecimal price;

    /**
     * 库存数量
     */
    @ApiModelProperty("库存数量")
    private Long goodsInventory;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String sellUnit;

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

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
