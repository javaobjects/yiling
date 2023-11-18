package com.yiling.dataflow.order.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/9/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowSaleExistsRequest extends BaseRequest {

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 售卖规格id
     */
    private Long specificationId;

    private String soBatchNo;

    /**
     * 客户名称（采购企业名称）
     */
    private String enterpriseName;

    /**
     * 销售日期
     */
    private Date startSoTime;

    /**
     * 销售日期
     */
    private Date endSoTime;

}
