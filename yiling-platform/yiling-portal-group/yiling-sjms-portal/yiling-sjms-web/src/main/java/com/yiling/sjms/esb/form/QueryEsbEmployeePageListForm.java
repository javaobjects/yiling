package com.yiling.sjms.esb.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询ESB员工分页列表
 *
 * @author: xuan.zhou
 * @date: 2023/2/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEsbEmployeePageListForm extends QueryPageListForm {

    @ApiModelProperty("员工工号或姓名")
    private String empIdOrName;
}
