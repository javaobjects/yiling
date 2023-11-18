package com.yiling.user.agreement.bo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SupplementaryAgreementGoodsBO
 * @描述
 * @创建时间 2023/3/30
 * @修改人 shichen
 * @修改时间 2023/3/30
 **/
@Data
@Accessors(chain = true)
public class SupplementaryAgreementGoodsBO implements Serializable {

    /**
     * 以岭商品Id
     */
    private Long goodsId;

    /**
     * 以岭商品对应的协议
     */
    private String agreementIds;
}
