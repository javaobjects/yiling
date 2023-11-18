package com.yiling.cms.document.dto.request;


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
public class UpdateDocumentCategoryRequest extends BaseRequest {


    private static final long serialVersionUID = 2879827213186687877L;
    private Long id;
    /**
     * 栏目名称
     */
    private String categoryName;


    /**
     * 0-禁用 1启用
     */
    private Integer status;
}
