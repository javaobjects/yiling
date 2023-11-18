package com.yiling.user.integral.entity;

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
 * 积分参与抽奖活动配置表
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("integral_lottery_config")
public class IntegralLotteryConfigDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 消耗规则ID
     */
    private Long useRuleId;

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

    /**
     * 消耗积分值
     */
    private Integer useIntegralValue;

    /**
     * 消耗总次数限制
     */
    private Integer useSumTimes;

    /**
     * 单用户每天消耗总次数限制
     */
    private Integer everyDayTimes;

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