package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EditAgreementGoodsPurchaseRelationRequest extends BaseRequest {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * pop商品对应可以采购的客户ID聚合
     */
    private String buyerGather;
}
