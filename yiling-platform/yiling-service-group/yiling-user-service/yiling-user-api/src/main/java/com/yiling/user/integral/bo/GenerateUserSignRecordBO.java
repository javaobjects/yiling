package com.yiling.user.integral.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户签到记录详情 BO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-31
 */
@Data
@Accessors(chain = true)
public class GenerateUserSignRecordBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 签到日期
     */
    private Date signTime;

    /**
     * 该天签到状态：1-未签到 2-已签到 3-待签到
     */
    private Integer signFlag;

    /**
     * 当前连续签到天数
     */
    private Integer continueDays;

    /**
     * 签到积分
     */
    private Integer signIntegral;

    /**
     * 连续签到奖励积分
     */
    private Integer continueSignIntegral;

}
