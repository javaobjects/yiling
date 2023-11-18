package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询商品分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-04
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryManufactureGoodsPageListForm extends QueryPageListForm {

    /**
     * 甲方类型：1-工业-生产厂家 2-工业-品牌厂家 3-商业-供应商 4-代理商
     */
    @ApiModelProperty(value = "甲方类型：1-工业生产厂家 2-工业品牌厂家 3-商业供应商 4-代理商（选择供销条款的商品时，此字段必填）")
    private Integer firstType;

    /**
     * 甲方ID
     */
    @Min(1)
    @ApiModelProperty("甲方ID（选择供销条款的商品时，此字段必填）")
    private Long eid;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

}
