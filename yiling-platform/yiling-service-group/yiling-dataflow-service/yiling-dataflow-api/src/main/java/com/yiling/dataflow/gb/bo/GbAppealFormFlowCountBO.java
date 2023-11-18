package com.yiling.dataflow.gb.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/17
 */
@Data
public class GbAppealFormFlowCountBO implements Serializable {

    private static final long serialVersionUID = 1646349692224352286L;

    /**
     * 源流向Id
     */
    private Long appealFormId;

    /**
     * 合计数量
     */
    private BigDecimal totalQuantity;

    /**
     * 已匹配团购数量合计
     */
    private BigDecimal totalMatchQuantity;

    /**
     * 未匹配团购数量合计
     */
    private BigDecimal totalUnMatchQuantity;

    public GbAppealFormFlowCountBO(){
        this.totalQuantity = BigDecimal.ZERO;
        this.totalMatchQuantity = BigDecimal.ZERO;
        this.totalUnMatchQuantity = BigDecimal.ZERO;
    }

}
