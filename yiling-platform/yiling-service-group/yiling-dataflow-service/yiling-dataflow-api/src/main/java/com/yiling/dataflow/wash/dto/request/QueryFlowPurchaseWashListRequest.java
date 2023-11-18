package com.yiling.dataflow.wash.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowPurchaseWashListRequest extends BaseRequest {

    private static final long serialVersionUID = 7147478323486501772L;

    private Integer year;

    private Integer month;

    private String goodsName;

    private String poSpecifications;

    private Long crmGoodsCode;
}
