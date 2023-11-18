package com.yiling.b2b.app.integral.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户签到日历明细 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-07
 */
@Data
@Accessors(chain = true)
public class UserSignRecordCalendarVO implements Serializable {

    /**
     * 当前页的年份
     */
    @ApiModelProperty("当前页的年份")
    private Integer year;

    /**
     * 当前页的月份
     */
    @ApiModelProperty("当前页的月份")
    private Integer month;

    /**
     * 用户签到记录日历
     */
    @ApiModelProperty("用户签到记录日历")
    private List<GenerateUserSignRecordVO> userSignRecordList;

}
