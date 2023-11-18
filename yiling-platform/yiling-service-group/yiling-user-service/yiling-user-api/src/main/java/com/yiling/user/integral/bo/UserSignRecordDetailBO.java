package com.yiling.user.integral.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
public class UserSignRecordDetailBO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户当前积分值
     */
    private Integer integralValue;

    /**
     * 当前连续签到天数
     */
    private Integer continueDays;

    /**
     * 是否为今日首次签到
     */
    private Boolean todaySignFlag;

    /**
     * 今日签到积分值
     */
    private Integer todaySignIntegral;

}
