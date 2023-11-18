package com.yiling.sjms.flow.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/2/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowSalePageVO extends BaseVO {

    /**
     * 销售时间
     */
    @ApiModelProperty(value = "销售时间")
    private Date soTime;

    /**
     * 销售单号
     */
    @ApiModelProperty(value = "销售单号")
    private String soNo;

    @ApiModelProperty(value = "经销商级别 字典crm_supplier_level")
    private Integer supplierLevel;

    /**
     * 经销商名称
     */
    @ApiModelProperty(value = "经销商名称")
    private String ename;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String enterpriseName;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "原始商品名称")
    private String goodsName;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "原始商品规格")
    private String soSpecifications;

    /**
     * 原始商品生产厂家
     */
    @ApiModelProperty(value = "原始商品生产厂家")
    private String soManufacturer;

    /**
     * 原始商品批准文号
     */
    @ApiModelProperty(value = "原始商品批准文号")
    private String soLicense;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal soQuantity;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String soUnit;

    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal soPrice;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal soTotalAmount;

    /**
     * 批号
     */
    @ApiModelProperty(value = "批号")
    private String soBatchNo;

    /**
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    @ApiModelProperty(value = "数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除")
    private Integer dataTag;
}
