package com.yiling.settlement.report.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-09-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReportGoodsRequest extends BaseRequest {

    /**
     *eid
     */
    private Long eid;

    /**
     *以岭品id
     */
    private Long ylGoodsId;

    /**
     *内码
     */
    private String goodsInSn;
}
