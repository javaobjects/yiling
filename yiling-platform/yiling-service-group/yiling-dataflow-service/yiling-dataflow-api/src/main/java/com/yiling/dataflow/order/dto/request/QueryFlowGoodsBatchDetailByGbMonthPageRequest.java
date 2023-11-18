package com.yiling.dataflow.order.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/4/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowGoodsBatchDetailByGbMonthPageRequest extends QueryPageListRequest {

    private String gbDetailMonth;

    private String gbName;

    private String gbSpecifications;

    private Long crmGoodsCode;
}
