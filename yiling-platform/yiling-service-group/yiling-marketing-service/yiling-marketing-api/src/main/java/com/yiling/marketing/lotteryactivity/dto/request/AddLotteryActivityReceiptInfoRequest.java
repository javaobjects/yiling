package com.yiling.marketing.lotteryactivity.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加抽奖活动收货信息 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddLotteryActivityReceiptInfoRequest extends BaseRequest {

    /**
     * 参与抽奖明细ID（我的奖品列表的ID）
     */
    @NotNull
    private Long joinDetailId;

    /**
     * 收货地址ID
     */
    @NotNull
    private Long deliveryAddressId;

}
