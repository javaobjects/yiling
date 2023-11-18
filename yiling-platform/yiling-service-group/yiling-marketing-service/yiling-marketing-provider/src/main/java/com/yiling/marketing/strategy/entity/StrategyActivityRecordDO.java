package com.yiling.marketing.strategy.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 营销活动记录表
 * </p>
 *
 * @author zhangy
 * @date 2022-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_strategy_activity_record")
public class StrategyActivityRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 活动名称
     */
    private String marketingStrategyName;

    /**
     * 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
     */
    private Integer strategyType;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 活动阶梯id
     */
    private Long ladderId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 是否赠送成功：1-是 2-否
     */
    private Integer sendStatus;

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
