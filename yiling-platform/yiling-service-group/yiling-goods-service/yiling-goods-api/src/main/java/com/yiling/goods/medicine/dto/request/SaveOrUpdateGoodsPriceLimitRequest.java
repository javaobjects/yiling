package com.yiling.goods.medicine.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateGoodsPriceLimitRequest extends BaseRequest {

    private Long id;

    private Long goodsId;

    private Integer customerType;

    /**
     * 省
     */
    private String provinceCode;

    /**
     * 市
     */
    private String cityCode;

    /**
     * 区
     */
    private String regionCode;

    private BigDecimal price;

    public BigDecimal getPrice() {
        if(price!=null) {
            return price.setScale(4,BigDecimal.ROUND_HALF_UP);
        }
        return null;
    }
}
