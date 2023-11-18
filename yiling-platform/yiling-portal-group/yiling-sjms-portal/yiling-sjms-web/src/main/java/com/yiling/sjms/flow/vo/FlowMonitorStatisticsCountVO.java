package com.yiling.sjms.flow.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/2/6
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel("流向接口监控-统计数量信息VO")
public class FlowMonitorStatisticsCountVO extends BaseVO {

    /**
     * 我的客户数量
     */
    @ApiModelProperty(value = "我的客户数量")
    private Integer customerCount;

    /**
     * 已部署接口数量
     */
    @ApiModelProperty(value = "已部署接口数量")
    private Integer deployInterfaceCount;

    /**
     * 已部署接口数量占总客户数量比例
     */
    @ApiModelProperty(value = "已部署接口数量占总客户数量比例")
    private BigDecimal deployAndCustomerCountRatio;

    /**
     * 未开启同步数量
     */
    @ApiModelProperty(value = "未开启同步数量")
    private Integer syncStatusOffCount;

    /**
     * 终端未激活数量
     */
    @ApiModelProperty(value = "终端未激活数量")
    private Integer clientStatusOffCount;

    /**
     * 运行中接口数量
     */
    @ApiModelProperty(value = "运行中接口数量")
    private Integer runningCount;

    /**
     * 未上传昨日流向数量
     */
    @ApiModelProperty(value = "未上传昨日流向数量")
    private Integer noDataYesterdayCount;

    /**
     * 超3天未上传流向数量
     */
    @ApiModelProperty(value = "超3天未上传流向数量")
    private Integer noDataMoreThan3DaysCount;

    /**
     * 超7天未上传流向数量
     */
    @ApiModelProperty(value = "超7天未上传流向数量")
    private Integer noDataMoreThan7DaysCount;

    public FlowMonitorStatisticsCountVO(){
        this.customerCount = 0;
        this.deployInterfaceCount = 0;
        this.deployAndCustomerCountRatio = BigDecimal.ZERO;
        this.syncStatusOffCount = 0;
        this.clientStatusOffCount = 0;
        this.noDataMoreThan3DaysCount = 0;
        this.noDataMoreThan7DaysCount = 0;
    }
}
