package com.yiling.admin.data.center.report.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryActivityGoodsForm extends BaseForm {

    /**
     * 商家id
     */
    @NotNull
    @ApiModelProperty("商家id")
    private Long eid;

    /**
     * 商品名称
     */
    @NotBlank
    @ApiModelProperty("商品名称")
    private String goodsName;
}
