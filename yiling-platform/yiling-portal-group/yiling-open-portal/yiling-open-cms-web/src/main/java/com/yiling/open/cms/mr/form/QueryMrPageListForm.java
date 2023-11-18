package com.yiling.open.cms.mr.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询医药代表分页列表 Form
 *
 * @author: xuan.zhou
 * @date: 2022/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMrPageListForm extends QueryPageListForm {

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "企业ID，如果取以岭的医药代表传1", required = true)
    private Long eid;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    @ApiModelProperty("状态：0-全部 1-启用 2-停用")
    private Integer status;

}
