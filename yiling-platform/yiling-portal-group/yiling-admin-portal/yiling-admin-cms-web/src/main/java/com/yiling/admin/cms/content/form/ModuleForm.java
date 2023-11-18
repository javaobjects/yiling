package com.yiling.admin.cms.content.form;

import com.yiling.admin.cms.content.vo.ModuleCategoryVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 业务线模块设置
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Data
@Accessors(chain = true)
public class ModuleForm {

    /**
     * 模块Id
     */
    @ApiModelProperty(value = "模块Id")
    private Long moduleId;

    /**
     * 模块名称
     */
    @ApiModelProperty(value = "模块名称")
    private String moduleName;

    /**
     * 栏目list
     */
    @ApiModelProperty(value = "栏目list")
    private List<ModuleCategoryForm> moduleCategoryList;

}
