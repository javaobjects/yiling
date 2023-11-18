package com.yiling.admin.b2b.promotion.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动保存form
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PromotionSpecialEnterpriseSaveForm")
public class PromotionSpecialEnterpriseSaveForm extends BaseForm {

    @ApiModelProperty(value = "专场活动id")
    private Long specialActivityId;

    @ApiModelProperty(value = "企业id")
    private Long eid;

    @ApiModelProperty(value = "营销活动id")
    private Long promotionActivityId;

    @ApiModelProperty(value = "专场活动图片")
    private String pic;
}
