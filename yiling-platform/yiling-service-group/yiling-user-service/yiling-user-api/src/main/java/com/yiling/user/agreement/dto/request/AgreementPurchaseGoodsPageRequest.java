package com.yiling.user.agreement.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class AgreementPurchaseGoodsPageRequest extends QueryPageListRequest {

    /**
     * 采购商eid
     */
    private Long purchaseEid;

}
