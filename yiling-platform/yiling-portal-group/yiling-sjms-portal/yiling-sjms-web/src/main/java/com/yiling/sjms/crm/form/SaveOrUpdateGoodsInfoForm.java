package com.yiling.sjms.crm.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveOrUpdateGoodsInfoForm
 * @描述
 * @创建时间 2023/4/11
 * @修改人 shichen
 * @修改时间 2023/4/11
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateGoodsInfoForm extends BaseForm {

    @ApiModelProperty(value = "id")
    private Long id;
    /**
     * crm商品编码
     */
    @NotNull(message = "商品编码不能为空")
    @ApiModelProperty(value = "crm商品编码")
    private Long goodsCode;

    /**
     * 产品分类
     */
    @ApiModelProperty(value = "产品分类")
    private String goodsGroup;

    /**
     * 产品名称
     */
    @NotEmpty(message = "产品名称不能为空")
    @ApiModelProperty(value = "产品名称")
    private String goodsName;

    /**
     * 规格
     */
    @NotEmpty(message = "规格不能为空")
    @ApiModelProperty(value = "规格")
    private String goodsSpec;

    /**
     * 缩写
     */
    @ApiModelProperty(value = "缩写")
    private String goodsNameAbbr;

    /**
     * 小包装
     */
    @ApiModelProperty(value = "小包装")
    private String packing;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌")
    private String brand;

    /**
     * 计量单位
     */
    @ApiModelProperty(value = "计量单位")
    private String unit;

    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    private BigDecimal supplyPrice;

    /**
     * 剂型
     */
    @NotEmpty(message = "剂型不能为空")
    @ApiModelProperty(value = "剂型")
    private String dosageForm;

    /**
     * 是否处方药
     */
    @ApiModelProperty(value = "是否处方药")
    private Integer isPrescription;

    /**
     * 换算因子
     */
    @ApiModelProperty(value = "换算因子")
    private String conversionFactor;

    /**
     * 是否指标产品
     */
    @ApiModelProperty(value = "是否指标产品")
    private String isTarget;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    /**
     * 出厂价
     */
    @ApiModelProperty(value = "出厂价")
    private BigDecimal exFactoryPrice;

    /**
     * 采购价
     */
    @ApiModelProperty(value = "采购价")
    private BigDecimal purchasePrice;

    /**
     * 销售价
     */
    @ApiModelProperty(value = "销售价")
    private BigDecimal salesPrice;

    /**
     * 考核价
     */
    @ApiModelProperty(value = "考核价")
    private BigDecimal assessmentPrice;

    /**
     * 品种类型
     */
    @ApiModelProperty(value = "品种类型")
    private String varietyType;

    /**
     * 是否团购 0：否 1：是
     */
    @NotNull(message = "是否团购不能为空")
    @ApiModelProperty(value = "是否团购 0：否 1：是")
    private Integer isGroupPurchase;

    /**
     * 品类id
     */
    @ApiModelProperty(value = "品类id")
    private Long categoryId;

    /**
     * 0:有效，1：失效
     */
    @ApiModelProperty(value = "0:有效，1：失效")
    private Integer status;

    @ApiModelProperty(value = "非锁标签列表")
    private List<Long> notLockTagIdList;

    @ApiModelProperty(value = "团购标签列表")
    private List<Long> groupPurchaseTagIdList;
}
