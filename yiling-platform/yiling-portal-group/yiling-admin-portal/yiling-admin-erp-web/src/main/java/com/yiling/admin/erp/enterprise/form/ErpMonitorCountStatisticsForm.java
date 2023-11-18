package com.yiling.admin.erp.enterprise.form;

import java.util.Date;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/2/10
 */@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpMonitorCountStatisticsForm extends BaseForm {

    ///**
    // * 超过请求次数关闭对接数量，类型（0：关闭统计 1：开启统计）
    // */
    //@NotNull
    //@Min(0)
    //@Max(1)
    //@ApiModelProperty(value = "超过请求次数关闭对接数量，统计类型（0：关闭统计 1：开启统计）")
    //private Integer clientStatus;
    //
    ///**
    // * 1小时内无心跳对接数量，类型（0：关闭统计 1：开启统计）
    // */
    //@NotNull
    //@Min(0)
    //@Max(1)
    //@ApiModelProperty(value = "1小时内无心跳对接数量，统计类型（0：关闭统计 1：开启统计）")
    //private Integer hartBeatType;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date    endTime;

}
