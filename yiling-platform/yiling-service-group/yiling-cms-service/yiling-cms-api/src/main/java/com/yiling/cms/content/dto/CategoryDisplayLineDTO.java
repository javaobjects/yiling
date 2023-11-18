package com.yiling.cms.content.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 栏目引用业务线
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CategoryDisplayLineDTO extends BaseDTO {

    private static final long serialVersionUID = -1748623848495239578L;

    /**
     * cms_category主键
     */
    private Long categoryId;

    /**
     * 栏目名称
     */
    private String categoryName;

    /**
     * 引用业务线id
     */
    private Long lineId;

    /**
     * 业务线名称
     */
    private String lineName;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 模块名称
     */
    private String moduleName;

}
