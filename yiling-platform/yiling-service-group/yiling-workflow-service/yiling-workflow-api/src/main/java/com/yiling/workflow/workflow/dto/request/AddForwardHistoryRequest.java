package com.yiling.workflow.workflow.dto.request;

import java.util.List;

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
public class AddForwardHistoryRequest extends BaseRequest {

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
    private List<String> toEmpIds;

    /**
     * 转发提示语
     */
    private String text;
}
