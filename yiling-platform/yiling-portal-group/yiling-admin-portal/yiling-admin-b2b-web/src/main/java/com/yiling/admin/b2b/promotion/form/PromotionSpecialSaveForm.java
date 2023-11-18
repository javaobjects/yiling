package com.yiling.admin.b2b.promotion.form;

import java.util.List;

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
@ApiModel(value = "PromotionSaveForm", description = "专场活动保存form")
public class PromotionSpecialSaveForm extends BaseForm {

    @ApiModelProperty(value = "专场活动id-编辑必传")
    private Long id;

    @ApiModelProperty(value = "专场活动基本信息")
    private PromotionSpecialActivitySaveForm promotionSpecialActivitySave;

    @ApiModelProperty(value = "参与活动的企业信息")
    private List<PromotionSpecialEnterpriseSaveForm> enterpriseSaves;
}
