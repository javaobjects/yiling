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
public class UserSignRecordDetailVO implements Serializable {

    @ApiModelProperty("用户当前积分值")
    private Integer integralValue;

    @ApiModelProperty("当前连续签到天数")
    private Integer continueDays;

    @ApiModelProperty("是否为今日首次签到（如果为首次则要弹窗）")
    private Boolean todaySignFlag;

    @ApiModelProperty("今日签到积分值")
    private Integer todaySignIntegral;

}
