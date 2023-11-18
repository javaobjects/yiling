package com.yiling.sjms.wash.vo;

import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/3/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowGoodsBatchSafePageVO <T> extends Page<T> {

    /**
     * 安全库存是否可提交：0-否（无提交时间信息） 1-是
     */
    @ApiModelProperty(value = "安全库存是否可提交：0-否（无提交时间信息） 1-是")
    private Boolean submitFlag;

    /**
     * 所属年月
     */
    @ApiModelProperty(value = "所属年月")
    private Date currentYearMonthTime;

    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private Integer year;

    /**
     * 月份
     */
    @ApiModelProperty(value = "月份")
    private Integer month;


    /**
     * 安全库存上报开始时间
     */
    @ApiModelProperty(value = "安全库存上报开始时间")
    private Date goodsBatchStartTime;

    /**
     * 安全库存上报结束时间
     */
    @ApiModelProperty(value = "安全库存上报结束时间")
    private Date goodsBatchEndTime;
}
