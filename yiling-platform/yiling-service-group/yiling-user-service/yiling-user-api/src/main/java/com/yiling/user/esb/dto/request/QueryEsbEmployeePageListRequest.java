package com.yiling.user.esb.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询ESB员工信息列表 request
 *
 * @author: xuan.zhou
 * @date: 2023/2/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEsbEmployeePageListRequest extends QueryPageListRequest {

    /**
     * 工号或姓名
     */
    private String empIdOrName;
}
