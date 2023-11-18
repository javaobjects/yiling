package com.yiling.cms.document.dto.request;


import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文献栏目
 * </p>
 *
 * @author gxl
 * @date 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddDocumentCategoryRequest extends BaseRequest {


    private static final long serialVersionUID = 4055740903533207131L;
    /**
     * 栏目名称
     */
    private String categoryName;

}
