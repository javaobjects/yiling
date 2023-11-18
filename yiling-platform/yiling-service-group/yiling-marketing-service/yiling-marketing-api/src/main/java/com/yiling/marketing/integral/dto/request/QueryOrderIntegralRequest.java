package com.yiling.marketing.integral.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询订单确认收货送积分的订单信息 Request
 *
 * @author: lun.yu
 * @date: 2023-02-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderIntegralRequest extends BaseRequest {

    /**
     * 订单号
     */
    @NotEmpty
    private String orderNo;

}
