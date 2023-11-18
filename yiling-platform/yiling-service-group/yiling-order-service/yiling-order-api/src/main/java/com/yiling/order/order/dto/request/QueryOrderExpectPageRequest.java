package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderExpectPageRequest extends QueryPageListRequest {
    /**
     * 买家企业ID
     */
    private Long buyerEid;
    /**
     * 供应商名称
     */
    private String distributorEname;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 审核状态： 1-未提交 2-待审核 3-审核通过 4-审核驳回
     */
    private Integer auditStatus;

    /**
     * 下单开始时间
     */
    private Date startCreatTime;

    /**
     * 下单结束时间
     */
    private Date endCreatTime;

    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    private Integer orderType;

    /**
     * 商务联系人
     */
    private List<Long> contacterIdList;

    /**
     * 当前登录人的eid
     */
    private List<Long> sellerEidList;

    /**
     * 订单Id
     */
    private Long id;
}
