package com.yiling.goods.medicine.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsAuditRecordPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品来源
     */
    private Integer source;
}
