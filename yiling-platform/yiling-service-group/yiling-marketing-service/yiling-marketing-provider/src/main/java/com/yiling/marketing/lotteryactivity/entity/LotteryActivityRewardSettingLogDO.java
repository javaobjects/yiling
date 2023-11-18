package com.yiling.marketing.lotteryactivity.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动-奖品设置变更日志表
 * </p>
 *
 * @author lun.yu
 * @date 2022-11-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_lottery_activity_reward_setting_log")
public class LotteryActivityRewardSettingLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 奖品设置ID
     */
    private Long rewardSettingId;

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

    /**
     * 修改前每天最大抽中数量（0为不限制）
     */
    private Integer beforeEveryMaxNumber;

    /**
     * 修改前中奖概率
     */
    private BigDecimal beforeHitProbability;

    /**
     * 修改后每天最大抽中数量（0为不限制）
     */
    private Integer afterEveryMaxNumber;

    /**
     * 修改后中奖概率
     */
    private BigDecimal afterHitProbability;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
