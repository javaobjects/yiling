package com.yiling.user.integral.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分参与抽奖活动配置 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralLotteryConfigDTO extends BaseDTO {

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
