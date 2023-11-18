package com.yiling.cms.content.dto.request;


import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 栏目
 * </p>
 *
 * @author gxl
 * @date 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCategoryRequest extends BaseRequest {


    private static final long serialVersionUID = -3777589507779567445L;

    private Long id;
    /**
     * 栏目名称
     */
    private String categoryName;

    // /**
    //  * 引用业务线
    //  */
    // private List<Long> displayLines;

    /**
     * 0-禁用 1启用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer categorySort;

    /**
     * 栏目id
     */
    private Long lineId;

    /**
     * 模块id
     */
    private Long moduleId;


}
