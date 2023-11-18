package com.yiling.admin.b2b.promotion.form;

import java.math.BigDecimal;
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
public class PromotionSpecialActivityPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "专场活动id")
    private Long id;

    @ApiModelProperty(value = "专场活动名称")
    private String specialActivityName;

    @ApiModelProperty(value = "活动类型0全部 1-满赠，2-特价，3-秒杀, 4-组合包")
    private Integer type;

    @ApiModelProperty(value = "活动状态（0全部 1-未开始；2-进行中；3-已结束）")
    private Integer status;

    @ApiModelProperty(value = "开始-开始时间")
    private Date beginStartTime;

    @ApiModelProperty(value = "开始-结束时间")
    private Date beginEndTime;

    @ApiModelProperty(value = "结束-开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束-结束时间")
    private Date endTime;

    @ApiModelProperty(value = "企业名称")
    private String eName;

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
