package com.yiling.sjms.flee.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/16 0016
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateFleeFlowRequest extends BaseRequest {

    /**
     * 确认人工号
     */
    private String empId;

    /**
     * form表主键
     */
    private Long formId;

    /**
     * 确认时候备注
     */
    private String confirmDescribe;
}
