package com.yiling.user.payment.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:tingwei.chen
 * @description:
 * @date: Created in 17:34 2021/7/2
 * @modified By:
 */
@Data
@Accessors(chain = true)
public class ReduceQuotaRequest extends BaseRequest {

    private static final long serialVersionUID = -3360334557002774069L;

    /**
     * 供应商id
     */
    private Long eid;
    /**
     * 采购商id
     */
    private Long customerEid;
    /**
     * 订单Id
     */
    private Long orderId;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 使用金额
     */
    private BigDecimal useAmount;
    /**
     * 下单时间
     */
    private Date time;

    /**
     * 平台类型
     */
    private PlatformEnum platformEnum;

}
