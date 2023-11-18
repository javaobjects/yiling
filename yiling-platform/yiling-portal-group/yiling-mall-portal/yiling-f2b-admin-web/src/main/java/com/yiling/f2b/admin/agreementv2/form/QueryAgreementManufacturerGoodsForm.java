package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 厂家商品列表查询 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/24
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgreementManufacturerGoodsForm extends QueryPageListForm {

    /**
     * 厂家ID
     */
    @Min(1)
    @NotNull
    @ApiModelProperty("厂家ID")
    private Long manufacturerId;

    /**
     * 商品ID
     */
    @Min(1)
    @ApiModelProperty("商品ID")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

}
