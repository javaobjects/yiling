package com.yiling.sales.assistant.app.userteam.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询成员的所有订单 Form
 * 
 * @author lun.yu
 * @date 2021/9/28
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMemberOrderForm extends QueryPageListForm {

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "用户ID")
    private Long userId;


}
