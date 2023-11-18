package com.yiling.admin.b2b.promotion.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动-企业
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "PromotionEnterpriseLimitSaveForm", description = "促销活动企业信息")
public class PromotionEnterpriseLimitSaveForm extends BaseForm {

    @ApiModelProperty(value = "企业ID")
    private Long   eid;

    @ApiModelProperty(value = "企业名称")
    private String ename;
}
