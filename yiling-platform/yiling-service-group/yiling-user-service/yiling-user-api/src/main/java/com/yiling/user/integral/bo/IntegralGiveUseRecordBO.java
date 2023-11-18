package com.yiling.user.integral.bo;

import java.io.Serializable;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分发放/扣减记录 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Data
@Accessors(chain = true)
public class IntegralGiveUseRecordBO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 规则ID字符串（导出）
     */
    private String ruleIdStr;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 积分值
     */
    private Integer integralValue;

    /**
     * 发放/扣减时间
     */
    private Date operTime;

    /**
     * 类型：1-订单送积分 2-签到送积分 3-参与活动消耗 4-兑换消耗 5-退货扣减 6-过期作废
     */
    private Integer changeType;

    /**
     * 类型：1-发放 2-扣减
     */
    private Integer type;

    /**
     * 行为ID
     */
    private Long behaviorId;

    /**
     * 行为名称
     */
    private String behaviorName;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户名称
     */
    private String uname;

    /**
     * 操作备注
     */
    private String opRemark;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 提交人手机号
     */
    private String mobile;

}
