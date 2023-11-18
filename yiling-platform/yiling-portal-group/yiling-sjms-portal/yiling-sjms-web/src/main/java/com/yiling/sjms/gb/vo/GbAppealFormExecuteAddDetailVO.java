package com.yiling.sjms.gb.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/18
 */
@Data
public class GbAppealFormExecuteAddDetailVO {

    /**
     * 团购处理列表ID
     */
    @ApiModelProperty(value = "团购处理列表ID")
    private Long appealFormId;

    /**
     * 团购数量
     */
    @ApiModelProperty(value = "团购数量")
    private BigDecimal gbQuantity;

    /**
     * 团购已扣减数量
     */
    @ApiModelProperty(value = "团购已扣减数量")
    private BigDecimal gbMatchQuantity;

    /**
     * 团购未扣减数量
     */
    @ApiModelProperty(value = "团购未扣减数量")
    private BigDecimal gbUnMatchQuantity;

    /**
     * 原流向ID
     */
    @ApiModelProperty(value = "原流向ID")
    private Long flowWashId;

    /**
     * 原流向数量
     */
    @ApiModelProperty(value = "原流向数量")
    private BigDecimal soQuantity;

    /**
     * 原流向已匹配团购数量
     */
    @ApiModelProperty(value = "原流向已匹配团购数量")
    private BigDecimal soMatchQuantity;

    /**
     * 原流向未匹配团购数量
     */
    @ApiModelProperty(value = "原流向未匹配团购数量")
    private BigDecimal soUnMatchQuantity;

    /**
     * 扣减数量
     */
    @ApiModelProperty(value = "扣减数量")
    private BigDecimal doMatchQuantity;

}
