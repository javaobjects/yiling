package com.yiling.sjms.form.dto.request;

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
public class SubmitFormRequest extends BaseRequest {

    private static final long serialVersionUID = -6265497967480319297L;

    /**
     * 基础表单ID
     */
    @NotNull
    private Long id;

    /**
     * 流程模板ID
     */
    private String flowTplId;

    /**
     * 流程模板名称
     */
    private String flowTplName;

    /**
     * 流程版本
     */
    private String flowVersion;

    /**
     * 流程实例ID
     */
    @NotNull
    private String flowId;
}
