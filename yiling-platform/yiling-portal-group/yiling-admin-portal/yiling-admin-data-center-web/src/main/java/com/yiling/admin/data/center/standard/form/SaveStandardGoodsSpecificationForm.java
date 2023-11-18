package com.yiling.admin.data.center.standard.form;

import javax.validation.constraints.Size;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveStandardGoodsSpecificationForm extends BaseForm {
    /**
     * 标准库id
     */
    @ApiModelProperty(value = "标准库id")
    private Long standardId;

    /**
     * 单位
     */
    @ApiModelProperty(value = "标准库id")
    @Size(min = 1,max = 30)
    private String unit;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    @Size(min = 1,max = 30)
    private String sellSpecifications;

    /**
     * 条形码
     */
    @ApiModelProperty(value = "条形码")
    private String barcode;


}
