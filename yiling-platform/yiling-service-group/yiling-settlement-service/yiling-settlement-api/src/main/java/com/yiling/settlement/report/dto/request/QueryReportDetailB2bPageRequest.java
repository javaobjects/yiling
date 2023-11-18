package com.yiling.settlement.report.dto.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReportDetailB2bPageRequest extends QueryPageListRequest {

    /**
     * 报表id
     */
    @NotNull
    private  Long reportId;

    /**
     * 订单id
     */
    private  Long orderId;

    /**
     * 采购方id
     */
    private List<Long> buyerEidList;

    /**
     * 开始下单时间
     */
    private Date startCreateOrderTime;

    /**
     * 结束下单时间
     */
    private Date endCreateOrderTime;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    private Integer identificationStatus;

    /**
     * 返利状态：1-待返利 2-已返利 3-部分返利
     */
    private Integer rebateStatus;


}
