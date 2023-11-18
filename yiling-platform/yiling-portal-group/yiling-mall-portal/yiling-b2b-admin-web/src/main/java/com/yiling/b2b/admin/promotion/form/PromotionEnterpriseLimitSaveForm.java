package com.yiling.b2b.admin.promotion.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionEnterpriseLimitSaveForm extends BaseForm {

    @ApiModelProperty(value = "企业ID")
    private Long eid;

    @ApiModelProperty(value = "企业名称")
    private String ename;
}
