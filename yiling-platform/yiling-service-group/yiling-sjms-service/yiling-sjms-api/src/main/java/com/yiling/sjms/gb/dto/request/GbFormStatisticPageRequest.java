package com.yiling.sjms.gb.dto.request;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;

/**
 * 团购管理统计
 */
@Data
public class GbFormStatisticPageRequest extends QueryPageListRequest {


    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 开始月份时间
     */
    private String startMonthTime;

    /**
     * 开始月份时间
     */
    private Date startMontDate;

    /**
     * 结束月份时间
     */
    private String endMonthTime;

    /**
     * 结束月份时间
     */
    private Date endMonthDate;

    /**
     *省区名称
     */
    private String provinceName;

    /**
     * 产品编号
     */
    private Long code;

    /**
     * 统计维度 1-选产品 2-只选省区 3-只选月份 4-选择产品加省区 5-选择产品加团购月份 6-全选 7-省区和月份 8 全部不选默认
     */
    private Integer type;

    /**
     * 团购月份 0-不勾选 1-勾选
     */
    private Integer monthType;


    /**
     * 省区 0-不勾选 1-勾选
     */
    private Integer provinceType;

    /**
     * 产品 0-不勾选 1-勾选
     */
    private Integer goodsType;


}
