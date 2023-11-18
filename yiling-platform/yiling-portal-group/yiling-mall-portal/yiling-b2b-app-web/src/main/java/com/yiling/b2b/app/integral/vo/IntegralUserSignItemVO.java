package com.yiling.b2b.app.integral.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户签到记录 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-30
 */
@Data
@Accessors(chain = true)
public class IntegralUserSignItemVO {

    @ApiModelProperty("签到日期")
    private Date signTime;

    @ApiModelProperty("该天是否签到")
    private Boolean signFlag;

    @ApiModelProperty("签到积分（连续签到时包含了连签奖励的积分）")
    private Integer signIntegral;

}
