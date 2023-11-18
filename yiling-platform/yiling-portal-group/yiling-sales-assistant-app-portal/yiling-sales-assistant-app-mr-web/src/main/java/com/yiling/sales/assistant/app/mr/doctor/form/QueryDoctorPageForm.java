package com.yiling.sales.assistant.app.mr.doctor.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手APP-医生管理-医生列表查询参数
 * @author: benben.jia
 * @date: 2022/3/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDoctorPageForm extends QueryPageListForm {

    @NotNull
    @ApiModelProperty(value = "认证状态 1已认证 2认证中（待审核）3认证未通过 4未认证",required = true)
    private Integer status;

}