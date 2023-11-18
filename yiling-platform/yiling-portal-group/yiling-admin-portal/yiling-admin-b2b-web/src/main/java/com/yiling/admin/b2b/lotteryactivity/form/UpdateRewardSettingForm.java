package com.yiling.admin.b2b.lotteryactivity.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 修改奖品设置信息 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateRewardSettingForm extends BaseForm {

    /**
     * 抽奖活动奖品设置集合
     */
    @NotEmpty
    @ApiModelProperty(value = "抽奖活动奖品设置集合", required = true)
    private List<@Valid SaveRewardSettingForm> activityRewardSettingList;

    @Data
    public static class SaveRewardSettingForm {

        /**
         * ID
         */
        @NotNull
        @ApiModelProperty(value = "主键ID", required = true)
        private Long id;

        /**
         * 每天最大抽中数量（0为不限制）
         */
        @NotNull
        @ApiModelProperty(value = "每天最大抽中数量（0为不限制）", required = true)
        private Integer everyMaxNumber;

        /**
         * 中奖概率
         */
        @NotNull
        @ApiModelProperty(value = "中奖概率", required = true)
        private BigDecimal hitProbability;
    }
}
