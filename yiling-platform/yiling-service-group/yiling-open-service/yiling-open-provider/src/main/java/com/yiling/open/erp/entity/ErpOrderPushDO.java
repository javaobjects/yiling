package com.yiling.open.erp.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * erp订单推送表
 *
 * @author shuan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("erp_order_push")
public class ErpOrderPushDO extends BaseDO {

    private Long orderId;
    private String orderNo;
    private Long sellerEid;
    private Long buyerEid;
    private Integer pushType;
    private Integer erpPushStatus;
    private Date erpPushTime;
    private String erpPushRemark;
    private String erpOrderNo;
    private String erpFlowNo;
    private Date createTime;
}