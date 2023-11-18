package com.yiling.sales.assistant.app.userteam.form;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询团队成员 Form
 * 
 * @author lun.yu
 * @date 2021/9/26
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUserTeamForm extends QueryPageListForm {

    /**
     * 姓名或手机号
     */
    @Length(max = 30)
    @ApiModelProperty(value = "姓名或手机号")
    private String nameOrPhone;

}
