package com.yiling.user.procrelation.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-05-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddGoodsForProcRelationRequest extends BaseRequest {

    private static final long serialVersionUID = 745306233004020448L;

    /**
     * 采购关系商品id
     */
    private Long id;

    /**
     * pop采购关系id
     */
    private Long relationId;

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
     * 标准库商品名称
     */
    private String standardGoodsName;

    /**
     * 标准库批准文号
     */
    private String standardLicenseNo;

    /**
     * 售卖规格
     */
    private String sellSpecifications;

    /**
     * 批准文号
     */
    private String licenseNo;

    /**
     * 专利类型 0-全部 1-非专利 2-专利
     */
    private Integer isPatent;

    /**
     * 使用要求金额
     */
    private BigDecimal requirements;

    /**
     * 商品优化折扣，单位为百分比
     */
    private BigDecimal rebate;

}
