package com.yiling.open.cms.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 查询患教活动 Form
 *
 * @author: fan.shen
 * @date: 2022-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryActivityDocPatientForm extends BaseForm {

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id", required = true)
    private Long id;

}
