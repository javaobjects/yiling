package com.yiling.admin.b2b.strategy.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CopyStrategyForm extends BaseForm {

    @ApiModelProperty("策略满赠活动id")
    @NotNull(message = "未选中策略满赠活动")
    private Long id;
}
