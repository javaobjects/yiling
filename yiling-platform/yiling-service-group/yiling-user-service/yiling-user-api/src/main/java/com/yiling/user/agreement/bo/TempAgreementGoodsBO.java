package com.yiling.user.agreement.bo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/29
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TempAgreementGoodsBO implements Serializable {

    private static final long serialVersionUID = -9044217574008714L;

    /**
     * 以岭商品Id
     */
    private Long goodsId;

    /**
     * 以岭商品对应的协议
     */
    private String agreementIds;

}
