package com.yiling.order.order.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 出库单关联开票分组信息
 * </p>
 *
 * @author wei.wang
 * @date 2021-10-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderInvoiceDeliveryGroupDTO extends BaseDTO {


    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 申请Id
     */
    private Long applyId;

    /**
     * 关联分组号
     */
    private String groupNo;

    /**
     * 关联的出库单号（多个英文逗号分隔）
     */
    private String groupDeliveryNos;

    /**
     * 开票摘要
     */
    private String invoiceSummary;

    /**
     * ERP推送状态：1-未推送 2-推送成功 3-推送失败
     */
    private Integer erpPushStatus;

    /**
     * ERP推送时间
     */
    private Date erpPushTime;

    /**
     * ERP推送备注
     */
    private String erpPushRemark;

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
