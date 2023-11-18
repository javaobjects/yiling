package com.yiling.dataflow.gb.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubstractGbAppealFlowRelatedRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 团购处理申请ID
     */
    private Long appealFormId;

    /**
     * 源流向Id
     */
    private Long flowWashId;

    /**
     * 扣减数量
     */
    private BigDecimal substractQuantity;

}
