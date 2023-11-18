package com.yiling.admin.sales.assistant.task.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务选部门-以岭子公司
 * @author: ray
 * @date: 2021/10/12
 */
@Data
public class SubEnterpriseVO {
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "企业id")
    private Long eid;
}