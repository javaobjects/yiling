package com.yiling.marketing.couponactivityautogive.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityAutoGiveGoodsLimitRequest extends BaseRequest {

    /**
     * 操作人id
     */
    @NotNull
    private Long userId;

    /**
     * 商品信息列表
     */
    @NotNull
    private List<SaveCouponActivityAutoGiveGoodsLimitDetailRequest> goodsLimitList;
}