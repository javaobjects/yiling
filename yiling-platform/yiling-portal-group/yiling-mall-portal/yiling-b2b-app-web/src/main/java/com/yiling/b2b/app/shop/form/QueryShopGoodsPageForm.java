package com.yiling.b2b.app.shop.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B-店铺商品分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/19
 */
@Data
@ApiModel
public class QueryShopGoodsPageForm extends QueryPageListForm {

    /**
     * 企业ID
     */
    @NotNull
    @ApiModelProperty("企业ID")
    private Long shopEid;

    /**
     * 商品名称
     */
    @Length(max = 20)
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 销量排序（true为倒序，false为正序）
     */
    @ApiModelProperty("销量排序（true为倒序，false为正序）")
    private Boolean saleDesc;

    /**
     * 价格排序（true为倒序，false为正序）
     */
    @ApiModelProperty("价格排序（true为倒序，false为正序）")
    private Boolean priceDesc;

    /**
     * 是否只看有货：0-否，1-是
     */
    @ApiModelProperty("是否只看有货：0-否，1-是")
    private Integer onlyHave;

}
