package com.yiling.user.agreement.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementPurchaseGoodsRequest extends BaseRequest {

    /**
     * 采购商eid
     */
    private Long purchaseEid;

    /**
     * 配送eid
     */
    private Long distributionEid;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品id
     */
    private List<Long> goodsIds;

    /**
     * 返利周期
     */
    private Integer rebateCycle;
}
