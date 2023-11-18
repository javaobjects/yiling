package com.yiling.user.procrelation.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsByEachOtherRequest extends BaseRequest {

    private static final long serialVersionUID = -6584821938377083048L;
    /**
     * 卖方eid
     */
    private Long sellerEid;

    /**
     * 买方eid
     */
    private Long buyerEid;

    /**
     * 商品名称
     */
    private String goodsName;
}
