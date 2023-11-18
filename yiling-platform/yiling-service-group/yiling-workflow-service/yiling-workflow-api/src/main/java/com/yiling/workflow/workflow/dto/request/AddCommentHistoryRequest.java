package com.yiling.workflow.workflow.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 添加转发记录 request
 *
 * @author: xuan.zhou
 * @date: 2023/2/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddCommentHistoryRequest extends BaseRequest {

    /**
     * 转发历史记录ID
     */
    private Long forwardHistoryId;

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
     * 意见/批注
     */
    @NotEmpty
    private String text;
}
