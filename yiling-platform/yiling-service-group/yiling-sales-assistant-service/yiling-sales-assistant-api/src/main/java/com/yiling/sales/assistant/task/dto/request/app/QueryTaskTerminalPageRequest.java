package com.yiling.sales.assistant.task.dto.request.app;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 选择终端入参
 * @author: ray
 * @date: 2021/9/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTaskTerminalPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 669527254461959526L;

    /**
     * 企业名称或联系电话
     */
    private String nameOrPhone;



    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 任务id
     */
   private Long taskId;
}