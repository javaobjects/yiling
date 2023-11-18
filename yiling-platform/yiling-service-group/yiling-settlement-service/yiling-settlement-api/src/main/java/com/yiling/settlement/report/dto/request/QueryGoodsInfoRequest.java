package com.yiling.settlement.report.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022/2/25
 */
@Data
@Accessors(chain = true)
public class QueryGoodsInfoRequest extends BaseRequest {

    private static final long serialVersionUID = -2751999240331739259L;

    /**
     * 商家id
     */
    private Long eid;

    /**
     * 商业商品内码
     */
    private List<String> goodsInSns;

    /**
     * 活动&阶梯的订单来源：0-全部 1-B2B 2-自建平台 3-三方平台及其他渠道订单
     */
    private Integer orderSource;

}