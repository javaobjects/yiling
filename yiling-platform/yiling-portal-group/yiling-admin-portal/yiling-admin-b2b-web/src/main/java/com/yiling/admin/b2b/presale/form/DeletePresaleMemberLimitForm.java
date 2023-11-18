package com.yiling.admin.b2b.presale.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeletePresaleMemberLimitForm extends BaseForm {

    @ApiModelProperty("营销活动id")
    @NotNull(message = "未选中策略满赠活动")
    private Long marketingPresaleId;

    @ApiModelProperty("会员id-单独删除时使用")
    @NotNull(message = "未选中会员")
    private Long memberId;
}
