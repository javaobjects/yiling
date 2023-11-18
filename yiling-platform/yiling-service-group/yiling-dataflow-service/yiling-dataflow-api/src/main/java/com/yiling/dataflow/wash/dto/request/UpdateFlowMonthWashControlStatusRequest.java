package com.yiling.dataflow.wash.dto.request;

import com.yiling.dataflow.wash.enums.FlowMonthWashControlMappingStatusEnum;
import com.yiling.dataflow.wash.enums.FlowMonthWashControlTypeEnum;
import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/3/3
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpdateFlowMonthWashControlStatusRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * FlowMonthWashControlTypeEnum
     */
    private FlowMonthWashControlTypeEnum flowMonthWashControlTypeEnum;

    /**
     * FlowMonthWashControlMappingStatusEnum
     */
    private FlowMonthWashControlMappingStatusEnum flowMonthWashControlMappingStatusEnum;
}
