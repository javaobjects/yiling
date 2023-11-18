package com.yiling.dataflow.sale.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/4/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySaleDepartmentTargetPageRequest extends QueryPageListRequest {
    private static final long serialVersionUID = 2060026446740800618L;
    /**
     * 指标编号
     */
    private String targetNo;

    private String name;

    private String departName;
    /**
     * 部门ID
     */
    private Long departId;


    /**
      * 状态 1-未配置 2-已配置 3-配置中
     */
    private Integer configStatus;
}