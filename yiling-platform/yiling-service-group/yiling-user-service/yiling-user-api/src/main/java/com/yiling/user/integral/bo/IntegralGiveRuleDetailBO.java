package com.yiling.user.integral.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.user.integral.dto.GenerateMultipleConfigDTO;
import com.yiling.user.integral.dto.IntegralOrderGiveConfigDTO;
import com.yiling.user.integral.dto.IntegralPeriodConfigDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分发放规则详情 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-03
 */
@Data
@Accessors(chain = true)
public class IntegralGiveRuleDetailBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 规则生效时间
     */
    private Date startTime;

    /**
     * 规则失效时间
     */
    private Date endTime;

    /**
     * 规则说明
     */
    private String description;

    /**
     * 行为ID
     */
    private Long behaviorId;

    /**
     * 行为名称
     */
    private String behaviorName;

    /**
     * 签到周期
     */
    private Integer signPeriod;

    /**
     * 签到周期配置集合
     */
    private List<IntegralPeriodConfigDTO> periodConfigList;

    /**
     * 订单送积分配置
     */
    private IntegralOrderGiveConfigBO orderGiveConfig;

    /**
     * 订单送积分倍数配置集合
     */
    private List<GenerateMultipleConfigDTO> orderGiveMultipleConfigList;

}
