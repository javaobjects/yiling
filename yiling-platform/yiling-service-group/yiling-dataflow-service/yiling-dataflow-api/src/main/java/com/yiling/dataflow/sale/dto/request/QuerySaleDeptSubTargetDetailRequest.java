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
public class QuerySaleDeptSubTargetDetailRequest extends QueryPageListRequest {
    private static final long serialVersionUID = 4625116450195078466L;
    /**
     * 部门ID
     */
    private Long departId;

    /**
     * 指标ID
     */
    private Long saleTargetId;

    /**
     * 标签为省区的部门ID(业务省区)
     */
    private Long departProvinceId;

    /**
     * 标签为区办的部门ID(区办)
     */
    private Long departRegionId;

    /**
     * 品类ID（品种）
     */
    private Long categoryId;

    private String departProvinceName;
    /**
     * 标签为区办的部门名称
     */
    private String departRegionName;
    /**
     * 品类名称
     */
    private String categoryName;
}