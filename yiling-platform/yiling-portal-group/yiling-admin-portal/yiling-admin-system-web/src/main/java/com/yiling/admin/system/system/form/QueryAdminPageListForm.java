package com.yiling.admin.system.system.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询用户分页列表 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryAdminPageListForm extends QueryPageListForm {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "状态：0-全部 1-启用 2-停用")
    private Integer status;

    @ApiModelProperty(value = "创建时间-起")
    private Date beginTime;

    @ApiModelProperty(value = "创建时间-止")
    private Date endTime;
}
