package com.yiling.pricing.goods.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户定价查询 form
 * </p>
 *
 * @author lun.yu
 * @date 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsPriceCustomerRequest extends BaseRequest {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 客户企业ID
     */
    private Long customerEid;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 产品线
     */
    private Integer goodsLine;

}
