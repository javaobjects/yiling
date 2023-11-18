package com.yiling.open.erp.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/7/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpOrderPushDTO extends BaseDTO {

    private Long orderId;
    private String orderNo;
    private Long sellerEid;
    private Long buyerEid;
    private Integer pushType;
    private Integer erpPushStatus;
    private Date erpPushTime;
    private String erpPushRemark;
    private String erpOrderNo;
    private Date createTime;
}
