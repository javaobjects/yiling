package com.yiling.dataflow.gb.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/17
 */
@Data
public class GbAppealFormFlowStatisticBO implements Serializable {

    private static final long serialVersionUID = -5050453861176598118L;

    /**
     * 流向业务主键
     */
    private String flowKey;

    /**
     * 团购申诉申请ID
     */
    private Long appealFormId;

    /**
     * 源流向Id
     */
    private Long flowWashId;

    /**
     * 当前团购占用数量
     */
    private BigDecimal gbMatchQuantity;

    /**
     * 数量
     */
    private BigDecimal soQuantity;

    /**
     * 匹配数量
     */
    private BigDecimal matchQuantity;

    /**
     * 未匹配数量
     */
    private BigDecimal unMatchQuantity;

    public GbAppealFormFlowStatisticBO() {
        this.gbMatchQuantity = BigDecimal.ZERO;
        this.soQuantity = BigDecimal.ZERO;
        this.matchQuantity = BigDecimal.ZERO;
        this.unMatchQuantity = BigDecimal.ZERO;
    }

}
