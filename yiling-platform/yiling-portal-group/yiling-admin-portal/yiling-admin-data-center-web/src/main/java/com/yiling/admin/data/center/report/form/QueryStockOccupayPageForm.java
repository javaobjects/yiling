package com.yiling.admin.data.center.report.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;

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
public class QueryStockOccupayPageForm extends QueryPageListForm {

    /**
     * 商业eid
     */
    @NotNull
    @ApiModelProperty(value = "eid")
    private Long eid;

    /**
     * 以岭品id
     */
    @Min(1)
    @ApiModelProperty(value = "以岭品id")
    private Long ylGoodsId;

    /**
     * 商品内码
     */
    @NotBlank
    @ApiModelProperty(value = "商品内码")
    private String goodsInSn;

    /**
     * 采购渠道：1-大运河采购 2-京东采购
     */
    @Range(min = 1,max = 2)
    @ApiModelProperty("采购渠道：1-大运河采购 2-京东采购")
    private Integer purchaseChannel;

}
