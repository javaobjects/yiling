package com.yiling.sales.assistant.app.enterprise.form;


import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询企业成员 Form
 * 
 * @author lun.yu
 * @date 2021/9/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterpriseMemberForm extends QueryPageListForm {

    /**
     * 员工姓名
     */
    @Length(max = 30)
    @ApiModelProperty(value = "员工姓名")
    private String name;

}
