package com.yiling.pricing.goods.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存或更新客户分组定价 request
 * @author: yuecheng.chen
 * @date: 2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateGoodsPriceCustomerGroupRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 供应商企业ID
     */
    private Long eid;

    /**
     * 产品线
     */
    private Integer goodsLine;

    /**
     * 客户分组ID
     */
    private Long customerGroupId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 定价规则：1-浮动点位 2-具体价格
     */
    private Integer priceRule;

    /**
     * 浮动点位/价格
     */
    private BigDecimal priceValue;

    public BigDecimal getPriceValue() {
        if(priceValue!=null) {
            return priceValue.setScale(4,BigDecimal.ROUND_HALF_UP);
        }
        return null;
    }

}
