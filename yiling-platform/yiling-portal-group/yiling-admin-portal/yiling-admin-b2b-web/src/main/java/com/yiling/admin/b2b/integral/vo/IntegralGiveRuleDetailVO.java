package com.yiling.admin.b2b.integral.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.user.integral.bo.IntegralOrderGiveConfigBO;
import com.yiling.user.integral.dto.GenerateMultipleConfigDTO;
import com.yiling.user.integral.dto.IntegralPeriodConfigDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分发放规则详情 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-03
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class IntegralGiveRuleDetailVO extends BaseVO {

    /**
     * 规则名称
     */
    @ApiModelProperty("规则名称")
    private String name;

    /**
     * 规则生效时间
     */
    @ApiModelProperty("规则生效时间")
    private Date startTime;

    /**
     * 规则失效时间
     */
    @ApiModelProperty("规则失效时间")
    private Date endTime;

    /**
     * 规则说明
     */
    @ApiModelProperty("规则说明")
    private String description;

    /**
     * 行为ID
     */
    @ApiModelProperty("行为ID")
    private Long behaviorId;

    /**
     * 行为名称
     */
    @ApiModelProperty("行为名称")
    private String behaviorName;

    /**
     * 签到周期
     */
    @ApiModelProperty("签到周期")
    private Integer signPeriod;

    /**
     * 签到周期配置集合
     */
    @ApiModelProperty("签到周期配置集合")
    private List<IntegralPeriodConfigVO> periodConfigList;

    /**
     * 订单送积分配置
     */
    @ApiModelProperty("订单送积分配置")
    private IntegralOrderGiveConfigVO orderGiveConfig;

    /**
     * 订单送积分倍数配置集合
     */
    @ApiModelProperty("订单送积分倍数配置集合")
    private List<GenerateMultipleConfigVO> orderGiveMultipleConfigList;

}
