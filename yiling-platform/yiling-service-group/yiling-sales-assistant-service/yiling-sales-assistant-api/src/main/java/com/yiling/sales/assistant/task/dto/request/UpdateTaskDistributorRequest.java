package com.yiling.sales.assistant.task.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: ray
 * @date: 2021/10/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateTaskDistributorRequest extends BaseRequest {
    private static final long serialVersionUID = 7820191950303737708L;
    /**
     * 配送商
     */
    private Long distributorEid;

    /**
     * 类型：1-云仓 2-非云仓
     */
    private Integer type;

    private String name;
}