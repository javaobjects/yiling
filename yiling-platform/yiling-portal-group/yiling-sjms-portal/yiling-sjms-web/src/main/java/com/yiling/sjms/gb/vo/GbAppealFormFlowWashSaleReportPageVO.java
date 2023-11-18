package com.yiling.sjms.gb.vo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GbAppealFormFlowWashSaleReportPageVO<T> extends Page<T> {

    /**
     * 合计数量
     */
    @ApiModelProperty(value = "合计数量")
    private BigDecimal totalQuantity;

    /**
     * 已匹配团购数量合计
     */
    @ApiModelProperty(value = "已匹配团购数量合计")
    private BigDecimal totalMatchQuantity;

    /**
     * 未匹配团购数量合计
     */
    @ApiModelProperty(value = "未匹配团购数量合计")
    private BigDecimal totalUnMatchQuantity;

    public GbAppealFormFlowWashSaleReportPageVO (){
        this.totalQuantity = BigDecimal.ZERO;
        this.totalMatchQuantity = BigDecimal.ZERO;
        this.totalUnMatchQuantity = BigDecimal.ZERO;
    }

}
