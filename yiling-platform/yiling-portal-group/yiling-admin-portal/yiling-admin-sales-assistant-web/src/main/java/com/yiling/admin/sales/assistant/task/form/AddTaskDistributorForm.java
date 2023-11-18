package com.yiling.admin.sales.assistant.task.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: ray
 * @date: 2021/10/18
 */
@Data
@ApiModel(value = "配送商")
public class AddTaskDistributorForm {
    /**
     * 配送商
     */
    @ApiModelProperty(value = "配送商id",required = true)
    private Long distributorEid;
    @ApiModelProperty(value = "配送商名称",required = true)
    private String name;
    /**
     * 类型：1-云仓 2-非云仓
     */
    @ApiModelProperty(value = "1-云仓 2-非云仓",required = true)
    private Integer type;
}