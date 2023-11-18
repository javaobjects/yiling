package com.yiling.workflow.workflow.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询代办列表入参
 * @author: gxl
 * @date: 2022/11/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTodoProcessPageRequest  extends QueryPageListRequest {
    private static final long serialVersionUID = 5489956928366214604L;

    private Long userId;

    /**
     * 用户工号
     */
    private String currentUserCode;

    private String businessKey;

    private String category;
}