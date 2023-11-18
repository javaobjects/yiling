package com.yiling.dataflow.sale.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询分解模板
 * @author: gxl
 * @date: 2023/4/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryResolveDetailPageRequest extends QueryPageListRequest {
    private static final long serialVersionUID = -3325795495223548328L;

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

    /**
     * 月份目标值分解状态 1未分解（任意月份没填） 2已分解
     */
    private Integer resolveStatus;


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