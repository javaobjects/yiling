package com.yiling.admin.b2b.strategy.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/9/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryStrategyActivityRecordPageForm extends QueryPageListForm {

    @ApiModelProperty("营销活动id")
    @NotNull(message = "未选中策略满赠活动")
    private Long marketingStrategyId;

    @ApiModelProperty("策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员")
    private Integer strategyType;
}
