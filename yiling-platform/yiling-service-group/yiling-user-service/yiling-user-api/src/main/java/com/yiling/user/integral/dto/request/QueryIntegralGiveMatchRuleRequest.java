package com.yiling.user.integral.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.Eq;
import com.yiling.user.common.util.bean.Like;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分发放匹配规则 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralGiveMatchRuleRequest extends BaseRequest {

    /**
     * 商家企业ID
     */
    private Long eid;

    /**
     * 当前企业ID
     */
    private Long uid;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品标准库ID
     */
    private Long standardId;

    /**
     * 商品规格ID
     */
    private Long specificationId;

    /**
     * 支付方式：1-线上支付 2-线下支付 3-账期支付
     */
    private Integer paymentMethod;

    /**
     * 订单号（非必填）
     */
    private String orderNO;

}
