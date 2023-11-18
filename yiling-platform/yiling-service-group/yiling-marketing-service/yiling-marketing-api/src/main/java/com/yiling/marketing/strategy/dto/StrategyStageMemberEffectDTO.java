package com.yiling.marketing.strategy.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 策略满赠购买会员方案表
 * </p>
 *
 * @author zhangy
 * @date 2022-09-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StrategyStageMemberEffectDTO extends BaseDTO {

    /**
     * 营销活动id
     */
    private Long marketingStrategyId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

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
