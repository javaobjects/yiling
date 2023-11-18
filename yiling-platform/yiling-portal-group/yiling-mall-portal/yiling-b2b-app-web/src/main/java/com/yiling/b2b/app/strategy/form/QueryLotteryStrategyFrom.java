package com.yiling.b2b.app.strategy.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/10/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryLotteryStrategyFrom extends BaseForm {

    @NotNull(message = "未选中抽奖活动")
    @ApiModelProperty(value = "抽奖活动id")
    private Long lotteryActivityId;
}
