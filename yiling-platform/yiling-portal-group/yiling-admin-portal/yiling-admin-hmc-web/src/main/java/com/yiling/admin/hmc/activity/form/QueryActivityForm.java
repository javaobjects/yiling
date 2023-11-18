package com.yiling.admin.hmc.activity.form;

import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询患教活动 Form
 *
 * @author: fan.shen
 * @date: 2022/9/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryActivityForm extends QueryPageListForm {

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

}
