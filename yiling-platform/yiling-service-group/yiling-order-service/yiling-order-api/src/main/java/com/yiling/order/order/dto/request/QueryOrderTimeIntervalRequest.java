package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 获取一段时间内的订单数据
 * @author:wei.wang
 * @date:2022/09/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderTimeIntervalRequest extends BaseRequest {


    /**
     * 开始下单时间
     */
    private Date startCreateTime;

    /**
     * 结束下单时间
     */
    private Date endCreateTime;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;

    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    private Integer orderType;

    /**
     *  订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    private List<Integer> orderStatusList;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private List<Integer> paymentMethodList;

    /**
     * 买家eid
     */
    private List<Long> buyerEidList;

    /**
     * 买家eid
     */
    private List<Long> sellerEidList;

    /**
     * com.yiling.order.order.enums.CustomerConfirmEnum
     * 客户确认状态-20-未转发,-30-待客户确认,-40-已确认
     */
    private Integer customerConfirmStatus;


}
