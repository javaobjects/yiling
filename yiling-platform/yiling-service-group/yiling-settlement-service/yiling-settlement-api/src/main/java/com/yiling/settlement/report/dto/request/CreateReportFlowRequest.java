package com.yiling.settlement.report.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateReportFlowRequest extends QueryPageListRequest {

    /**
     * 卖家eid
     */
    private Long eid;

    /**
     * 报表状态
     */
    private List<Integer> reportStatusList;

    /**
     * 下单时间
     */
    private Date startSoTime;

    /**
     * 下单时间
     */
    private Date endSoTime;

    /**
     * 订单来源，字典：erp_sale_flow_source，1-大运河销售 2-自建平台销售 3-其它渠道销售
     */
    private List<String> soSourceList;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    private Integer identificationStatus;

}
