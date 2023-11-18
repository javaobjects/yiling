package com.yiling.dataflow.check.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/10/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowPurchaseCheckTaskPageRequest extends QueryPageListRequest {

    /**
     * 采购商企业ID
     */
    private Long eid;

    /**
     * 采购商企业名称
     */
    private String ename;

    /**
     * 采购时间，开始
     */
    private Date poTimeStart;

    /**
     * 采购时间，结束
     */
    private Date poTimeEnd;

}
