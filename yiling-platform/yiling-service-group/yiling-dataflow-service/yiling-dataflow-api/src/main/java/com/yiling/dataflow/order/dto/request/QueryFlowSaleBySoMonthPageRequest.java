package com.yiling.dataflow.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
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
public class QueryFlowSaleBySoMonthPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -5454390596585583576L;

    private String soMonth;

    private String goodsName;

    private String soSpecifications;

    private Long crmGoodsCode;

    private String enterpriseName;

    private String enterpriseCrmCode;
}
