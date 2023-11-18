package com.yiling.sjms.workflow.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 提交表单 Request
 *
 * @author: xuan.zhou
 * @date: 2023/2/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SubmitFormBaseRequest extends BaseRequest {


    /**
     * 基础表单ID
     */
    @NotNull
    private Long id;

    /**
     * 工号
     */
    private String empId;
}
