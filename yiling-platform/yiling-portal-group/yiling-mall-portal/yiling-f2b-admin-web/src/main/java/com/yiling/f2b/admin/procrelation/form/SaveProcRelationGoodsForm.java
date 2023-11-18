package com.yiling.f2b.admin.procrelation.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveProcRelationGoodsForm extends BaseForm {

    /**
     * pop采购关系id
     */
    @Min(1)
    @NotNull
    @ApiModelProperty(value = "pop采购关系id")
    private Long relationId;

    @Valid
    @NotEmpty
    @ApiModelProperty(value = "商品列表")
    List<GoodsInfo> goodsInfoList;

    @Data
    public static class GoodsInfo{

        /**
         * 采购关系商品id
         */
        @ApiModelProperty(value = "商品列表页id---主要用于编辑时批量设置优惠折扣，新增的商品无需此字段")
        private Long id;

        /**
         * 商品id
         */
        @NotNull
        @ApiModelProperty(value = "商品id")
        private Long goodsId;

        /**
         * 标准库ID
         */
        @NotNull
        @ApiModelProperty(value = "标准库ID")
        private Long standardId;

        /**
         * 售卖规格ID
         */
        @NotNull
        @ApiModelProperty(value = "售卖规格ID")
        private Long sellSpecificationsId;

        /**
         * 商品名称
         */
        @NotBlank
        @ApiModelProperty(value = "商品名称")
        private String goodsName;

        /**
         * 标准库商品名称
         */
        @ApiModelProperty(value = "标准库商品名称")
        private String standardGoodsName;

        /**
         * 标准库批准文号
         */
        @ApiModelProperty(value = "标准库批准文号")
        private String standardLicenseNo;

        /**
         * 售卖规格
         */
        @NotBlank
        @ApiModelProperty(value = "售卖规格")
        private String sellSpecifications;

        /**
         * 批准文号
         */
        @ApiModelProperty(value = "批准文号")
        private String licenseNo;

        /**
         * 专利类型 0-全部 1-非专利 2-专利
         */
        @NotNull
        @ApiModelProperty(value = "专利类型 0-全部 1-非专利 2-专利")
        private Integer isPatent;

        /**
         * 商品优化折扣，单位为百分比
         */
        @ApiModelProperty(value = "商品优化折扣，单位为百分比")
        private BigDecimal rebate;
    }

}
