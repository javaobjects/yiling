package com.yiling.dataflow.gb.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

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
public class GbAppealSubstractMateFlowRequest extends BaseRequest {

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
    private BigDecimal doMatchQuantity;

    /**
     * 处理类型：1-自动 2-人工
     *
     * {@link com.yiling.dataflow.gb.enums.GbExecTypeEnum}
     */
    private Integer execType;

}
