package com.yiling.goods.medicine.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddOrDeleteCustomerGoodsLimitRequest extends BaseRequest {

    /**
     * 限价id
     */
    private Long customerEid;

    /**
     * 限价id
     */
    private Long eid;

    /**
     * 商品Id
     */
    private List<Long> goodsIds;


}
