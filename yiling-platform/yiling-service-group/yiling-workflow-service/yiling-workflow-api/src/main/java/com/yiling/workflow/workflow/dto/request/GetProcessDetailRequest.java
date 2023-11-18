package com.yiling.workflow.workflow.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审批记录入参
 * @author: gxl
 * @date: 2022/12/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetProcessDetailRequest extends BaseRequest {

    private static final long serialVersionUID = 8618473952712142183L;

    /**
     * 业务系统唯一标识（比如团购编号）
     */
    private String businessKey;
}