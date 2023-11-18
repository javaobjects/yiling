package com.yiling.open.erp.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/7/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpPurchaseFlowPageRequest extends QueryPageListRequest {

    /**
     * 采购时间-开始
     */
    private String poTimeStart;

    /**
     * 采购时间-结束
     */
    private String poTimeEnd;

}
