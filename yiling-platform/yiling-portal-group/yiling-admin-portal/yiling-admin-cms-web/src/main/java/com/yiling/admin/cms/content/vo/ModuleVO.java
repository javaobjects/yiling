package com.yiling.admin.cms.content.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class ModuleVO {

    /**
     * 模块Id
     */
    @ApiModelProperty(value = "模块Id")
    private Long moduleId;

    /**
     * 栏目list
     */
    @ApiModelProperty(value = "栏目list")
    private List<ModuleCategoryVO> moduleCategoryList;

}
