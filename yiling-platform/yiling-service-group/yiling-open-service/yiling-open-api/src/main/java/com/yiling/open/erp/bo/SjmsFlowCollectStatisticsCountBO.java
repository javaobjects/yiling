package com.yiling.open.erp.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/2/22
 */
@Data
@Accessors(chain = true)
public class SjmsFlowCollectStatisticsCountBO implements java.io.Serializable  {

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

    public SjmsFlowCollectStatisticsCountBO (){
        this.runningCount = 0;
        this.noDataYesterdayCount = 0;
        this.noDataMoreThan3DaysCount = 0;
        this.noDataMoreThan7DaysCount = 0;
    }
}
