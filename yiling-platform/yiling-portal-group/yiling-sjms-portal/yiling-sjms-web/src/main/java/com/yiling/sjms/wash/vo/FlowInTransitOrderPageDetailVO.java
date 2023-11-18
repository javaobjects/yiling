package com.yiling.sjms.wash.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/3/6
 */
@Data
public class FlowInTransitOrderPageDetailVO extends BaseVO {

    /**
     * 所属月份
     */
    @ApiModelProperty(value = "所属年月")
    private Date gbDetailMonth;

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码")
    private Long crmEnterpriseId;

    /**
     * 经销商名称
     */
    @ApiModelProperty(value = "经销商名称")
    private String name;

    /**
     * 采购渠道机构编码
     */
    @ApiModelProperty(value = "采购渠道机构编码")
    private Long supplyCrmEnterpriseId;

    /**
     * 采购渠道机构名称
     */
    @ApiModelProperty(value = "采购渠道机构名称")
    private String supplyName;

    /**
     * crm商品编码
     */
    @ApiModelProperty(value = "标准产品编码")
    private Long crmGoodsCode;

    /**
     * crm商品名称
     */
    @ApiModelProperty(value = "标准产品名称")
    private String crmGoodsName;

    /**
     * crm商品规格
     */
    @ApiModelProperty(value = "标准产品规格")
    private String crmGoodsSpecifications;

    /**
     * 商品批次号
     */
    @ApiModelProperty(value = "批号")
    private String gbBatchNo;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "在途数量")
    private BigDecimal gbNumber;

    /**
     * 商品单位
     */
    @ApiModelProperty(value = "单位")
    private String gbUnit;

    /**
     * 采购时间
     */
    @ApiModelProperty(value = "采购时间")
    private Date gbPurchaseTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String opUser;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "最后操作时间")
    private Date opTime;

}
