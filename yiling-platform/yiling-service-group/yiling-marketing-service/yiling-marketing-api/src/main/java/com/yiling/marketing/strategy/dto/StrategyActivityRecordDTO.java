package com.yiling.marketing.strategy.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

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
public class StrategyActivityRecordDTO extends BaseDTO {

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
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
