package com.yiling.dataflow.wash.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/4/4
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpdateFlowMonthWashTaskStatusRequest extends BaseRequest {

    private static final long serialVersionUID = 2792993311036831509L;

    private Integer year;

    private Integer month;

    private Integer washStatus;
}

