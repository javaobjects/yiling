package com.yiling.hmc.remind.vo;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.hmc.remind.dto.MedsRemindTaskDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * 今日提醒
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/6/9
 */
@Data
@ToString
@ApiModel(value = "TodayMedsRemindTaskVO", description = "今日提醒")
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodayMedsRemindTaskVO {

    private static final long serialVersionUID = -7722430332896313642L;

    /**
     * 提醒时间
     */
    @ApiModelProperty(value = "提醒时间")
    private String remindTime;

    /**
     * 时间icon标签
     */
    @ApiModelProperty(value = "时间icon标签")
    private Integer timeIconFlag;

    /**
     * 提醒明细
     */
    @ApiModelProperty(value = "提醒明细")
    private List<MedsRemindTaskDetailVO> remindTaskDetailVOList;

}
