package com.yiling.b2b.app.integral.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户签到明细 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-31
 */
@Data
@Accessors(chain = true)
public class GenerateUserSignRecordVO implements Serializable {

    /**
     * 签到日期
     */
    @ApiModelProperty("签到日期")
    private Date signTime;

    /**
     * 该天签到状态：1-未签到 2-已签到 3-待签到
     */
    @ApiModelProperty("该天签到状态：1-未签到 2-已签到 3-待签到")
    private Integer signFlag;

    /**
     * 当前连续签到天数
     */
    @ApiModelProperty("当前连续签到天数")
    private Integer continueDays;

    /**
     * 签到积分
     */
    @ApiModelProperty("签到积分")
    private Integer signIntegral;

    /**
     * 连续签到奖励积分
     */
    @ApiModelProperty("连续签到奖励积分")
    private Integer continueSignIntegral;

    /**
     * 当天总积分
     */
    @ApiModelProperty("当天总积分")
    private Integer sumIntegral;

}
