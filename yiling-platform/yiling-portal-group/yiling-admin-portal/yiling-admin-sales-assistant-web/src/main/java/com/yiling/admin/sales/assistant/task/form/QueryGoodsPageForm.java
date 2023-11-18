package com.yiling.admin.sales.assistant.task.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryGoodsPageForm extends QueryPageListForm {
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value = "0-平台任务1-企业任务",required = true)
    @NotNull
    private Integer taskType;

/*
    @ApiModelProperty(value = "所选配送商企业id",required = true)
    private List<Long> distributorEidList;
*/
}