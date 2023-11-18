package com.yiling.admin.b2b.promotion.form;

import java.math.BigDecimal;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动-商品
 *
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PromotionCombinationPackageSaveForm", description = "组合包基本信息")
public class PromotionCombinationPackageSaveForm extends BaseForm {

    @ApiModelProperty(value = "组合包名称")
    private String packageName;

    @ApiModelProperty(value = "组合包起购数量")
    private Integer initialNum;

    @ApiModelProperty(value = "退货要求")
    private String returnRequirement;

    @ApiModelProperty(value = "组合包商品简称")
    private String packageShortName;

    /**
     * 总数量
     */
    @ApiModelProperty(value = "总数量")
    private Integer totalNum=0;

    /**
     * 每人最大数量
     */
    @ApiModelProperty(value = "每人最大数量")
    private Integer perPersonNum=0;

    /**
     * 每人每天数量
     */
    @ApiModelProperty(value = "每人每天数量")
    private Integer perDayNum=0;

    @ApiModelProperty(value = "活动图片")
    private String pic;

    @ApiModelProperty(value = "组合包与其他营销活动说明")
    private String descriptionOfOtherActivity;
}
