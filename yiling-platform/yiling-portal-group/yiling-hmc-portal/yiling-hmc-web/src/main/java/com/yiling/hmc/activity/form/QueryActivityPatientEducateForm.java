package com.yiling.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 查询患教活动 Form
 *
 * @author: fan.shen
 * @date: 2022-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryActivityPatientEducateForm extends BaseForm {

    /**
     * 活动id 列表
     */
    @ApiModelProperty(value = "活动id 列表", required = true)
    private List<Long> idList;

}
