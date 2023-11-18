package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBuyerOrderReturnInfoRequest extends QueryPageListRequest {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 退货单号
     */
    private String orderReturnNo;

    /**
     * 采购商eid
     */
    private Long eid;
    /**
     * 供应商名称
     */
    private String sellerEname;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 1-供应商退货单（驳回退货单） 2-破损退货单 3-采购退货单
     */
    private Integer returnType;

    /**
     * 订单状态 1-待审核 2-审核通过 3-审核驳回
     */
    private Integer returnStatus;
    /**
     * eidlist
     */
    private List<Long> eidList;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否是以岭普通人员，需要使用userId查询
     */
    private Boolean yiLingOrdinary;


}
