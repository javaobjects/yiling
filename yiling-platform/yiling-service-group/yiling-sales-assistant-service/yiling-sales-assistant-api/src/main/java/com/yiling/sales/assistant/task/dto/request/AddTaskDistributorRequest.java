package com.yiling.sales.assistant.task.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * @author: ray
 * @date: 2021/10/18
 */
@Data
public class AddTaskDistributorRequest extends BaseRequest {
    private static final long serialVersionUID = -3034781961192610674L;
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