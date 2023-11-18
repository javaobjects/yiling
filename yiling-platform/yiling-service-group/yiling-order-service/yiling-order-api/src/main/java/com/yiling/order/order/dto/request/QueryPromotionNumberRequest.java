package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;

/** 描述促销活动累计数量查询
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2022/2/7
 */
@Data
public class QueryPromotionNumberRequest extends BaseRequest {

    /**
     * 卖家EID
     */
    private Long buyerEid;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 配送商商品ID
     */
    private Long distributorGoodsId;
}
