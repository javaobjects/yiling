package com.yiling.admin.erp.enterprise.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/2/10
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@ApiModel("erp监控数量统计信息VO")
public class ErpMonitorCountStatisticsVo extends BaseVO {

    /**
     * 超过请求次数关闭对接数量
     */
    @ApiModelProperty(value = "超过请求次数关闭对接数量")
    private Integer closedCount;

    /**
     * 1小时内无心跳对接数量
     */
    @ApiModelProperty(value = "1小时内无心跳对接数量")
    private Integer noHeartCount;


    /**
     * 当月未上传销售企业数量
     */
    @ApiModelProperty(value = "当月未上传销售企业数量")
    private Long noFlowSaleCount;

    /**
     * 销售异常数据数量
     */
    @ApiModelProperty(value = "销售异常数据数量")
    private Long saleExceptionCount;

    /**
     * 采购异常数据数量
     */
    @ApiModelProperty(value = "采购异常数据数量")
    private Long purchaseExceptionCount;

}
