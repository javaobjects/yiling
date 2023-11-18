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
public class SubstractGbAppealFlowStatisticRequest extends BaseRequest {

    private Long id;
    /**
     * 扣减数量
     */
    private BigDecimal substractQuantity;
    /**
     * 版本号
     */
    private Long flowVersion;
}
