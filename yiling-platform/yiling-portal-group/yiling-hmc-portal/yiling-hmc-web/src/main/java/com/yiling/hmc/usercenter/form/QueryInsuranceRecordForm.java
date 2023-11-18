package com.yiling.hmc.usercenter.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询我的参保 Form
 *
 * @author: fan.shen
 * @date: 2022/4/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryInsuranceRecordForm extends QueryPageListForm {

    /**
     * 人名或者手机号
     */
    @ApiModelProperty("人名或者手机号")
    private String nameOrPhone;

}
