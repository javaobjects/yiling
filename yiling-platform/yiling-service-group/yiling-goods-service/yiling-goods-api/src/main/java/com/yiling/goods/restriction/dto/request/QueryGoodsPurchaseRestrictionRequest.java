package com.yiling.goods.restriction.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryGoodsPurchaseRestrictionRequest
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsPurchaseRestrictionRequest extends BaseRequest {
    /**
     * 客户企业id
     */
    private Long customerEid;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品id
     */
    private List<Long> goodsIdList;
}
