package com.yiling.user.payment.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 账户临时额度 Form
 *
 * @author: tingwei.chen
 * @date: 2021/7/5
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryQuotaOrderRequest extends QueryPageListRequest {

    /**
     * 账户id
     */
    private Long accountId;
    /**
     * 供应商name
     */
    private String ename;
    /**
     * 还款状态
     */
    private Integer status;

    /**
     * 菜单类型：1.已使用订单，2.已还款订单 3.待还款订单
     */
    private Integer type;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * eidlist
     */
    private List<Long> eidList;

    /**
     * 账期平台类型：1-POP账期 2-B2B账期
     */
    private Integer paymentDaysType;
}
