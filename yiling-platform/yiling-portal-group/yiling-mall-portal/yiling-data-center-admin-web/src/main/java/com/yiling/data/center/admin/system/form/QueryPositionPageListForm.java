package com.yiling.data.center.admin.system.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询职位分页列表 Form
 *
 * @author: xuan.zhou
 * @date: 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPositionPageListForm extends QueryPageListForm {

    @ApiModelProperty("职位名称")
    private String name;

    @ApiModelProperty("职位状态：0-全部 1-启用 2-停用")
    private Integer status;
}
