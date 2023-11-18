package com.yiling.marketing.paypromotion.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SavePayPromotionRulesActivityRequest extends BaseRequest {

    /**
     * 计算明细id
     */
    private Long id;

    /**
     * 支付促销活动id
     */
    private Long marketingPayId;

    /**
     * 促销规则类型（1-满减券；2-满折券）
     */
    private Integer type;

    /**
     * 门槛金额/件数
     */
    private BigDecimal thresholdValue;

    /**
     * 优惠内容（金额/折扣比例）
     */
    private BigDecimal discountValue;

    /**
     * 最高优惠金额,满折时候使用
     */
    private BigDecimal discountMax;
}
