package com.yiling.f2b.admin.enterprise.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 移动分组客户 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/30
 */
@Data
public class MoveGroupCustomersForm {

    /**
     * 原分组ID
     */
    @NotNull
    @ApiModelProperty(value = "原分组ID", required = true)
    private Long originalGroupId;

    /**
     * 目标分组ID
     */
    @NotNull
    @ApiModelProperty(value = "目标分组ID", required = true)
    private Long targetGroupId;
}
