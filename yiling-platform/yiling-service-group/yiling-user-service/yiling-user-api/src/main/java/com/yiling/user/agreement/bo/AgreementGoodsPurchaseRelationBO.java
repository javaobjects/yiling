package com.yiling.user.agreement.bo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/11
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AgreementGoodsPurchaseRelationBO implements Serializable {

    private static final long serialVersionUID = -9044217574008714L;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 可购买的渠道商ID集合
     */
    private String buyerGather;

}
