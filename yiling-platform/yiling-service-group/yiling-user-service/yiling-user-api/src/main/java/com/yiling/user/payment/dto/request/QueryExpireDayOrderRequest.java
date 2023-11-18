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
public class QueryExpireDayOrderRequest extends QueryPageListRequest {

    /**
     * 当前登录人eid
     */
    private Long currentEid;

    /**
     * 商务联系人ID
     */
    private Long contacterId;

    /**
     * acountId
     */
    private Long acountId;

    /**
     * 采购商名称
     */
    private String buyerEname;

    /**
     * 供应商名称
     */
    private String sellerEname;

    /**
     * 下单开始时间
     */
    private Date startTime;

    /**
     * 下单结束时间
     */
    private Date endTime;
    /**
     * eidlist
     */
    private List<Long> eidList;

    /**
     * 还款状态
     */
    private Integer repaymentStatus;

    /**
     * 商务联系人
     */
    private List<Long> contactUserList;

    /**
     *  发货开始时间
     */
    private Date deliveryStartTime;

    /**
     * 发货结束时间
     */
    private Date deliveryEndTime;

}
