package com.yiling.cms.content.dto.request;


import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 业务线关联模块
 * </p>
 *
 * @author gxl
 * @date 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CategoryLineModuleRequest extends BaseRequest {

    private static final long serialVersionUID = 6282807579228856247L;

    /**
     * 栏目id
     */
    private Long lineId;

    /**
     * 栏目名称
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
