package com.yiling.dataflow.gb.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GbAppealSubstractResultRequest extends BaseRequest {

    /**
     * 流向团购统计ID
     */
    private Long appealFlowStatisticId;

    /**
     * 流向团购关联ID
     */
    private Long gafrId;

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

    /**
     * 处理类型：1自动2人工
     */
    private Integer execType;

}
