package com.yiling.open.erp.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/2/10
 */
@Data
@Accessors(chain = true)
public class ErpMonitorCountStatisticsBO implements java.io.Serializable {

    /**
     * 超过请求次数关闭对接数量
     */
    private Long closedCount;

    /**
     * 1小时内无心跳对接数量
     */
    private Long noHeartCount;

    /**
     * 当月未上传销售企业数量
     */
    private Long noFlowSaleCount;

    /**
     * 销售异常数据数量
     */
    private Long saleExceptionCount;

    /**
     * 采购异常数据数量
     */
    private Long purchaseExceptionCount;

    public ErpMonitorCountStatisticsBO(){
        this.closedCount = 0L;
        this.noHeartCount = 0L;
        this.noFlowSaleCount = 0L;
        this.saleExceptionCount = 0L;
        this.purchaseExceptionCount = 0L;
    }

}
