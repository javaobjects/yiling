package com.yiling.data.center.admin.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

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
public class StandardGoodsInfoForm extends QueryPageListForm {

    private static final long serialVersionUID = -333710304281212221L;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 注册证号
     */
    @ApiModelProperty(value = "注册证号")
    private String licenseNo;

    /**
     * 生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    /**
     * 生产地址
     */
    @ApiModelProperty(value = "生产地址")
    private String manufacturerAddress;


    /**
     * 分类名称1
     */
    @ApiModelProperty(value = "一级分类ID")
    private Long standardCategoryId1;

    /**
     * 分类名称2
     */
    @ApiModelProperty(value = "二级分类ID")
    private Long standardCategoryId2;

    /**
     * 商品类别：1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品
     */
    @ApiModelProperty(value = "商品类别：1-普通药品 2-中药饮片 3-中药材 4-消杀 5-保健食品 6-食品")
    private Integer goodsType;

    /**
     * 处方类型：1处方药 2甲类非处方药 3乙类非处方药 4其他
     */
    @ApiModelProperty(value = "处方类型：1-处方药 2-甲类非处方药 3-乙类非处方药 4-其他")
    private Integer otcType;

    /**
     * 是否医保：1是 2非 3未采集到相关信息
     */
    @ApiModelProperty(value = "是否医保：1-是 2-非 3-未采集到相关信息")
    private Integer isYb;

    /**
     * 管制类型：0非管制 1管制
     */
    @ApiModelProperty(value = "管制类型：1-非管制 2-管制")
    private Integer controlType;


    /**
     * 特殊成分：0不含麻黄碱 1含麻黄碱
     */
    @ApiModelProperty(value = "特殊成分：1-不含麻黄碱 2-含麻黄碱")
    private Integer specialComposition;

    /**
     * 有无图片 1有 2无
     */
    @ApiModelProperty(value = "有无图片：1-有 2-无")
    private Integer pictureFlag;

}
