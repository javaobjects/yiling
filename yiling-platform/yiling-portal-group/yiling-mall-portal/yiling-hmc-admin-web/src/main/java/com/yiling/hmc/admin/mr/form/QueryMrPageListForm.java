package com.yiling.hmc.admin.mr.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询医药代表分页列表 Form
 *
 * @author: xuan.zhou
 * @date: 2022/6/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMrPageListForm extends QueryPageListForm {

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
     * 工号
     */
    @ApiModelProperty("工号")
    private String code;

}
