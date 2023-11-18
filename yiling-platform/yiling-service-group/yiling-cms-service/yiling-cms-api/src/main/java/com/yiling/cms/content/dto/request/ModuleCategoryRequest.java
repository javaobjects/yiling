package com.yiling.cms.content.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 栏目设置
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-01
 */
@Data
@Accessors(chain = true)
public class ModuleCategoryRequest implements java.io.Serializable{

    /**
     * 栏目Id
     */
    private Long categoryId;

    /**
     * 栏目名称
     */
    private String categoryName;

    
}
