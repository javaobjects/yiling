package com.yiling.admin.data.center.report.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2022/7/4
 */
@Data
public class FlowPurchaseMonthVO {

    @ApiModelProperty("采购商Id")
    private Long purchaseEnterpriseId;

    @ApiModelProperty("采购商名称")
    private String purchaseEnterpriseName;

    @ApiModelProperty("采购商渠道类型Id")
    private Integer purchaseChannelId;

    @ApiModelProperty("采购商渠道类型")
    private String purchaseChannelDesc;

    @ApiModelProperty("采购商业名称")
    private Long supplierEnterpriseId;

    @ApiModelProperty("采购商业名称")
    private String supplierEnterpriseName;

    @ApiModelProperty("供应商渠道类型Id")
    private Integer supplierChannelId;

    @ApiModelProperty("供应商渠道类型")
    private String supplierChannelDesc;

    @ApiModelProperty("各月份统计数据")
    private Map<String, StorageInfoVO> storageInfoMap;

    @ApiModelProperty("一行金额统计")
    private BigDecimal storageMoneySum;

    @ApiModelProperty("一行数量统计")
    private BigDecimal storageQuantitySum;

}
