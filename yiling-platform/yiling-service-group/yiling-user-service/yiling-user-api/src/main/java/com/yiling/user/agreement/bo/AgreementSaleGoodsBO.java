package com.yiling.user.agreement.bo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: zhigang.guo
 * @date: 2021/09/14
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AgreementSaleGoodsBO implements Serializable {


    /**
     * 协议主键
     */
    private Long agreementId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 标准库ID
     */
    private Long standardId;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 售卖规格
     */
    private String sellSpecifications;

    /**
     * 批准文号
     */
    private String licenseNo;

    /**
     * 专利类型 1-非专利 2-专利
     */
    private Integer isPatent;

    /**
     * 配送商ID
     */
    private Integer distributionEid;

    /**
     * 可采企业ID
     */
    private Integer purchaseEid;
}
