package com.yiling.admin.data.center.report.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RevisePurchaseStockStock extends BaseForm {

    /**
     * 商业eid
     */
    @NotNull
    @ApiModelProperty(value = "eid")
    private Long eid;

    /**
     * 商品内码
     */
    @ApiModelProperty(value = "商品内码")
    private String goodsInSn;
}
