package com.yiling.dataflow.gb.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GbAppealFlowRelatedDTO extends BaseDTO {

    /**
     * 流向业务主键
     */
    private String flowKey;

    /**
     * 申诉申请Id
     */
    private Long appealFormId;

    /**
     * 源流向Id
     */
    private Long flowWashId;

    /**
     * 匹配数量
     */
    private BigDecimal matchQuantity;

}
