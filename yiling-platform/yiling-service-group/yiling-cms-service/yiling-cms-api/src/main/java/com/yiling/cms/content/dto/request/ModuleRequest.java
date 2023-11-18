package com.yiling.cms.content.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 模块栏目设置
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-01
 */
@Data
@Accessors(chain = true)
public class ModuleRequest implements java.io.Serializable{

    /**
     * 模块Id
     */
    private Long moduleId;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 栏目list
     */
    private List<ModuleCategoryRequest> moduleCategoryList;

}
