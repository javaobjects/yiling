package com.yiling.order.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2021/10/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodDetailSumQueryPageRequest extends QueryPageListRequest {


    /**
     * 商品ID或者配送商商品ID
     */
    private Long goodId;

    /**
     *  1-以岭商品ID,2-配送商商品iD
     */
    private Integer type;

    /**
     * 买家Eid
     */
    private List<Long> buyerEidList;

}
