package com.yiling.sjms.wash.form;

import java.math.BigDecimal;

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
 * @date: 2023/3/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowTerminalOrderForm extends BaseForm {

    /**
     * 当前保存列表序号
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "当前保存列表序号")
    private Integer itemId;

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

}
