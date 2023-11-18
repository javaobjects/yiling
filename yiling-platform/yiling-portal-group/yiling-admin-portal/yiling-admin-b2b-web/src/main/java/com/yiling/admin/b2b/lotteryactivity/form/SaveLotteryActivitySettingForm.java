package com.yiling.admin.b2b.lotteryactivity.form;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存抽奖活动设置信息 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-14
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveLotteryActivitySettingForm extends BaseForm {

    /**
     * 抽奖活动ID
     */
    @NotNull
    @ApiModelProperty(value = "抽奖活动ID", required = true)
    private Long lotteryActivityId;

    /**
     * 抽奖活动赠送范围
     */
    @ApiModelProperty("抽奖活动赠送范围（B端使用规则）")
    private SaveLotteryActivityGiveScopeForm activityGiveScope;

    /**
     * C端参与规则
     */
    @ApiModelProperty("C端参与规则（C端使用规则）")
    private SaveLotteryActivityJoinRuleForm activityJoinRule;

    /**
     * 抽奖活动奖品设置集合
     */
    @NotEmpty
    @ApiModelProperty(value = "抽奖活动奖品设置集合", required = true)
    private List<@Valid AddLotteryActivityRewardSettingForm> activityRewardSettingList;

    /**
     * 预算金额
     */
    @NotNull
    @ApiModelProperty("预算金额")
    @DecimalMin(value = "0.00",message = "预算金额不能小于0")
    @Digits(integer = 8, fraction = 2,message = "预算金额不在有效范围")
    private BigDecimal budgetAmount;

    /**
     * 活动规则说明
     */
    @ApiModelProperty("活动规则说明")
    private String content;

    /**
     * 分享背景图
     */
    @ApiModelProperty("分享背景图")
    private String bgPicture;

}
