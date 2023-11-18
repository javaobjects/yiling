package com.yiling.workflow.workflow.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加流程操作记录 request
 *
 * @author: xuan.zhou
 * @date: 2023/2/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddWfActHistoryRequest extends BaseRequest {

    /**
     * 单据ID
     */
    @NotNull
    private Long formId;

    /**
     * 操作人工号
     */
    @NotEmpty
    private String fromEmpId;

    /**
     * 接收人工号列表
     */
    @NotEmpty
    private String toEmpId;

    /**
     * 操作类型：1-提交申请 2-驳回 3-审批通过
     */
    @NotNull
    private Integer type;

    /**
     * 审批意见
     */
    private String text;
}
