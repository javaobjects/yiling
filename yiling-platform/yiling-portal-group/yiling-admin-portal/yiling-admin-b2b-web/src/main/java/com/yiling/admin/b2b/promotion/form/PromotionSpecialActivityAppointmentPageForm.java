package com.yiling.admin.b2b.promotion.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionSpecialActivityAppointmentPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "专场活动id")
    private Long id;

    @ApiModelProperty(value = "专场活动名称")
    private String specialActivityName;

    @ApiModelProperty(value = "活动类型0全部 1-满赠，2-特价，3-秒杀, 4-组合包")
    private Integer type;

    @ApiModelProperty(value = "活动状态（0-全部，1-启用；2-停用；）")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "企业名称")
    private String eName;

    @ApiModelProperty(value = "预约开始时间")
    private Date appointmentStartTime;

    @ApiModelProperty(value = "预约结束时间")
    private Date appointmentEndTime;

    @ApiModelProperty(value = "预约人姓名")
    private String appointmentUserName;

    @ApiModelProperty(value = "营销活动参与企业")
    private String specialActivityEnterpriseName;

    public Date getBeginTime() {
        if (null != startTime) {
            return DateUtil.beginOfDay(startTime);
        }
        return null;
    }

    public Date getEndTime() {
        if (null != endTime) {
            return DateUtil.endOfDay(endTime);
        }
        return null;
    }
}
