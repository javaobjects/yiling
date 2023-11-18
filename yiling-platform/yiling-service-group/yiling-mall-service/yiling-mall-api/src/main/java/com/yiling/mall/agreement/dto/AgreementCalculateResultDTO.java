package com.yiling.mall.agreement.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/7/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementCalculateResultDTO extends BaseRequest {

    /**
     * 协议Id
     */
    private Long agreementId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * 协议政策
     */
    private BigDecimal policyValue;

    /**
     * 商品小计
     */
    private BigDecimal goodsAmount;

    /**
     * 政策返利金额
     */
    private BigDecimal discountAmount;
}
