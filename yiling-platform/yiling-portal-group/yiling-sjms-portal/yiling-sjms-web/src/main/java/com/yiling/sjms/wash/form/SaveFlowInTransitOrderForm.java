package com.yiling.sjms.wash.form;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/3/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowInTransitOrderForm extends BaseForm {

    /**
     * 当前保存列表序号
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "当前保存列表序号")
    private Integer itemId;

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
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "crm商品编码")
    private Long crmGoodsCode;

    /**
     * crm商品名称
     */
    @NotBlank
    @ApiModelProperty(value = "crm商品名称")
    private String crmGoodsName;

    /**
     * crm商品规格
     */
    @NotBlank
    @ApiModelProperty(value = "crm商品规格")
    private String crmGoodsSpecifications;

    /**
     * 商品批次号
     */
    @ApiModelProperty(value = "商品批次号")
    private String gbBatchNo;

    /**
     * 库存数量
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "库存数量")
    private BigDecimal gbNumber;

    /**
     * 采购时间
     */
    @ApiModelProperty(value = "采购时间，天")
    private Date gbPurchaseTime;

}
