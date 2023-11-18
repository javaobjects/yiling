package com.yiling.order.order.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * erp应收单和出库单关系表
 * @author:wei.wang
 * @date:2021/9/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDeliveryReceivableDTO extends BaseDTO {
    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 申请id
     */
    private Long applyId;

    /**
     * erp出库单号
     */
    private String erpDeliveryNo;

    /**
     * erp应收单号
     */
    private String erpReceivableNo;

    /**
     * 作废应收单 0-否 1-是
     */
    private Integer erpReceivableFlag;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
