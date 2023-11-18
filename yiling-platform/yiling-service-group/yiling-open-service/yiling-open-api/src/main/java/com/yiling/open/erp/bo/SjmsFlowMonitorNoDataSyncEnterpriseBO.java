package com.yiling.open.erp.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/2/6
 */
@Data
@Accessors(chain = true)
public class SjmsFlowMonitorNoDataSyncEnterpriseBO implements java.io.Serializable {


    /**
     * 企业id
     */
    private Long rkSuId;

    /**
     * 企业名称
     */
    private String clientName;

    /**
     * 流向收集方式
     */
    private Integer flowMode;

    /**
     * 未上传流向天数
     */
    private Integer noDataSyncDaysCount;

}
