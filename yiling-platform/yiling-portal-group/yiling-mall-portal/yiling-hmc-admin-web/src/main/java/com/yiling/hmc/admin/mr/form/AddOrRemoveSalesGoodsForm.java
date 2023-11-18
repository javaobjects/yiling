package com.yiling.hmc.admin.mr.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加或删除可售商品 Form
 *
 * @author: xuan.zhou
 * @date: 2022/6/6
 */
@Data
public class AddOrRemoveSalesGoodsForm {

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "员工ID", required = true)
    private Long employeeId;

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "商品ID列表", required = true)
    private List<Long> goodsIds;

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "操作类型：1-添加 2-删除", required = true)
    private Integer opType;
}
