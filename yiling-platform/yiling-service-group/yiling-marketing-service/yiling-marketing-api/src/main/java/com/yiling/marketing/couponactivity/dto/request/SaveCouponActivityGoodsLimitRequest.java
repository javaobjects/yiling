package com.yiling.marketing.couponactivity.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author houjie.sun
 * @date 2021-11-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityGoodsLimitRequest extends BaseRequest {

    /**
     * 商品信息列表
     */
    private List<SaveCouponActivityGoodsLimitDetailRequest> goodsLimitList;

    /**
     * 可用供应商（1-全部供应商可用；2-部分供应商可用）
     */
    private Integer enterpriseLimit;

    /**
     * 业务类型：1-运营后台 2-商家后台
     */
    private Integer businessType;

}
