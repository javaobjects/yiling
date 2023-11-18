package com.yiling.admin.cms.content.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class ModuleCategoryVO {

    /**
     * 栏目Id
     */
    @ApiModelProperty(value = "栏目Id")
    private Long categoryId;

    /**
     * 栏目名称
     */
    @ApiModelProperty(value = "栏目名称")
    private String categoryName;
    
}
