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
public class FlowPurchasePageVO extends BaseVO {


    /**
     * 采购时间
     */
    @ApiModelProperty(value = "采购时间")
    private Date poTime;

    /**
     * 采购单号
     */
    @ApiModelProperty(value = "采购单号")
    private String poNo;

    @ApiModelProperty(value = "经销商级别 字典crm_supplier_level")
    private Integer supplierLevel;

    /**
     * 经销商名称
     */
    @ApiModelProperty(value = "经销商名称")
    private String ename;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
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
    private String poSpecifications;

    /**
     * 原始商品生产厂家
     */
    @ApiModelProperty(value = "原始商品生产厂家")
    private String poManufacturer;

    /**
     * 原始商品批准文号
     */
    @ApiModelProperty(value = "原始商品批准文号")
    private String poLicense;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal poQuantity;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String poUnit;

    /**
     * 采购单价
     */
    @ApiModelProperty(value = "采购单价")
    private BigDecimal poPrice;

    /**
     * 采购金额
     */
    @ApiModelProperty(value = "采购金额")
    private BigDecimal poTotalAmount;

    /**
     * 批号
     */
    @ApiModelProperty(value = "批号")
    private String poBatchNo;

    /**
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    @ApiModelProperty(value = "数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除")
    private Integer dataTag;

}
