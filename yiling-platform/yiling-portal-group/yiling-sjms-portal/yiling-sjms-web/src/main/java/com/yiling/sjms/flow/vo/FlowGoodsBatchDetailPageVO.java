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
public class FlowGoodsBatchDetailPageVO extends BaseVO {

    /**
     * 库存时间
     */
    @ApiModelProperty(value = "库存时间")
    private Date gbDetailTime;

    /**
     * 经销商名称
     */
    @ApiModelProperty(value = "经销商名称")
    private String ename;

    @ApiModelProperty(value = "经销商级别 字典crm_supplier_level")
    private Integer supplierLevel;

    /**
     * 原始商品名称
     */
    @ApiModelProperty(value = "原始商品名称")
    private String gbName;

    /**
     * 原始商品规格
     */
    @ApiModelProperty(value = "原始商品规格")
    private String gbSpecifications;

    /**
     * 原始商品生产厂家
     */
    @ApiModelProperty(value = "原始商品生产厂家")
    private String gbManufacturer;

    /**
     * 原始商品批准文号
     */
    @ApiModelProperty(value = "原始商品批准文号")
    private String gbLicense;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal gbNumber;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String gbUnit;

    /**
     * 批号
     */
    @ApiModelProperty(value = "批号")
    private String gbBatchNo;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期")
    private String gbProduceTime;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private String gbEndTime;
}
