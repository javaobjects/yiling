package com.yiling.admin.data.center.order.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

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
public class QueryOperatorOrderReturnInfoForm extends QueryPageListForm {

    /**
     * 退货单来源 1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer returnSource;

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 退货单号
     */
    private String orderReturnNo;

    /**
     * 支付方式
     */
    private Long eid;

    /**
     * 供应商名称
     */
    private String sellerEname;
    /**
     * 采购商名称
     */
    private String buyerEname;
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


}
