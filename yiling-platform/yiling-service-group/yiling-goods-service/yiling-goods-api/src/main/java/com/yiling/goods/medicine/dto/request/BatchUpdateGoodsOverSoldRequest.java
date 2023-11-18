package com.yiling.goods.medicine.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: chen.shi
 * @date: 2021/12/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BatchUpdateGoodsOverSoldRequest extends BaseRequest {
    /**
     * 商品ID集合
     */
    private List<Long> goodsIds;
    /**
     * 超卖type 0-非超卖 1-超卖
     */
    private Integer overSoldType;
}
