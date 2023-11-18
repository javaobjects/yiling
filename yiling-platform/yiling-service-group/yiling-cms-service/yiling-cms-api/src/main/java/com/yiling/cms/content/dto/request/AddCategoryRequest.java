package com.yiling.cms.content.dto.request;


import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加栏目
 * </p>
 *
 * @author gxl
 * @date 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddCategoryRequest extends BaseRequest {

    private static final long serialVersionUID = 6282807579228856247L;

    /**
     * 栏目id
     */
    private Long lineId;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 栏目名称
     */
    private String categoryName;
    //
    // /**
    //  * 业务线关联模块
    //  */
    // private List<CategoryLineModuleRequest> lineModuleList;

}
