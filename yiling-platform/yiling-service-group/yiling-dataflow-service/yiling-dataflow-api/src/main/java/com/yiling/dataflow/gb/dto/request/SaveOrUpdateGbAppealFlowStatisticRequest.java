package com.yiling.dataflow.gb.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/5/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateGbAppealFlowStatisticRequest extends BaseRequest {
    /**
     * 源流向Id
     */
    private Long flowWashId;

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
}
