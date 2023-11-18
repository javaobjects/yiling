package com.yiling.sjms.flow.vo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/2/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowCollectEnterprisePageVO<T> extends Page<T> {

    /**
     * 经销商数量
     *     1.筛选条件为空时，取客户数量
     *     2.筛选条件仅有 经销商级别、或经销商名称时，取根据此条件查询的客户数量
     *     3.筛选条件有 流向收集方式、对接时间、上次收集时间、状态、流向级别，任何一个条件时，取分页查询到的条数
     */
    @ApiModelProperty(value = "经销商数量")
    private Integer customerCount = 0;

    /**
     * 已部署接口数量
     */
    @ApiModelProperty(value = "已部署接口数量")
    private Integer deployInterfaceCount = 0;

    /**
     * 已部署接口数量占总客户数量比例
     */
    @ApiModelProperty(value = "已部署接口数量占总客户数量比例")
    private BigDecimal deployAndCustomerCountRatio = BigDecimal.ZERO;

    /**
     * 未开启同步数量
     */
    @ApiModelProperty(value = "未开启同步数量")
    private Integer syncStatusOffCount = 0;

    /**
     * 终端未激活数量
     */
    @ApiModelProperty(value = "终端未激活数量")
    private Integer clientStatusOffCount = 0;

    /**
     * 运行中接口数量
     */
    @ApiModelProperty(value = "运行中接口数量")
    private Integer runningCount = 0;

    /**
     * 未上传昨日流向数量
     */
    @ApiModelProperty(value = "未上传昨日流向数量")
    private Integer noDataYesterdayCount = 0;

    /**
     * 超3天未上传流向数量
     */
    @ApiModelProperty(value = "超3天未上传流向数量")
    private Integer noDataMoreThan3DaysCount = 0;

    /**
     * 超7天未上传流向数量
     */
    @ApiModelProperty(value = "超7天未上传流向数量")
    private Integer noDataMoreThan7DaysCount = 0;

}
