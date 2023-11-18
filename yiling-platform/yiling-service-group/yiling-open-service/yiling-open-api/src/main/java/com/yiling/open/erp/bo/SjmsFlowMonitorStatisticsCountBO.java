package com.yiling.open.erp.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/2/6
 */
@Data
@Accessors(chain = true)
public class SjmsFlowMonitorStatisticsCountBO implements java.io.Serializable {

    /**
     * 已部署接口数量
     */
    private Integer deployInterfaceCount;

    /**
     * 未开启同步数量
     */
    private Integer syncStatusOffCount;

    /**
     * 终端未激活数量
     */
    private Integer clientStatusOffCount;

    /**
     * 运行中接口数量
     */
    private Integer runningCount;

    /**
     * 未上传昨日流向数量
     */
    private Integer noDataYesterdayCount;

    /**
     * 超3天未上传流向数量
     */
    private Integer noDataMoreThan3DaysCount;

    /**
     * 超7天未上传流向数量
     */
    private Integer noDataMoreThan7DaysCount;

    public SjmsFlowMonitorStatisticsCountBO (){
        this.deployInterfaceCount = 0;
        this.syncStatusOffCount = 0;
        this.clientStatusOffCount = 0;
        this.runningCount = 0;
        this.noDataYesterdayCount = 0;
        this.noDataMoreThan3DaysCount = 0;
        this.noDataMoreThan7DaysCount = 0;
    }

}
