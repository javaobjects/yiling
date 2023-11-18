package com.yiling.user.integral.dto.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.common.util.bean.After;
import com.yiling.user.common.util.bean.Before;
import com.yiling.user.common.util.bean.In;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询积分兑换订单 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryIntegralExchangeOrderRequest extends BaseRequest {

    /**
     * 积分兑换商品表ID集合
     */
    @In(name = "exchange_goods_id")
    private List<Long> exchangeGoodsId;

    /**
     * 开始兑换提交时间
     */
    @Before(name = "submit_time")
    private Date startSubmitTime;

    /**
     * 结束兑换提交时间
     */
    @After(name = "submit_time")
    private Date endSubmitTime;

}
