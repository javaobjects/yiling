package com.yiling.user.integral.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分兑换订单 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateIntegralExchangeOrderRequest extends BaseRequest {

    /**
     * 兑付类型：1-兑付全部 2-兑付当前页 3-单个兑付
     */
    @NotNull
    private Integer exchangeType;

    /**
     * 兑付订单ID（单个兑付）
     */
    private Long id;

    /**
     * 兑付订单集合（兑付当前页）
     */
    private List<Long> idList;

    /**
     * 收货信息（真实物品兑付时使用）
     */
    private UpdateReceiptInfoRequest orderReceiptInfo;

}
