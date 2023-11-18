package com.yiling.order.order.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:dexi.yao
 * @date:2021/8/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddTicketDiscountRecordRequest extends BaseRequest {

	private static final long serialVersionUID = -4484861173272278875L;
	/**
	 * id
	 */
	private Long id;

    /**
     * 企业ID（销售组织ID）
     */
    private Long eid;

    /**
     * 企业名称（销售组织名称）
     */
    private String ename;

    /**
     * 企业ERP编码（销售组织ERP编码）
     */
    private String sellerErpCode;

    /**
     * 客户ERP编码
     */
    private String customerErpCode;

    /**
     * 票折单号
     */
    private String ticketDiscountNo;

    /**
     * 票折金额
     */
    private BigDecimal totalAmount;

    /**
     * 票折已使用金额
     */
    private BigDecimal usedAmount;

    /**
     * 票折可使用金额
     */
    private BigDecimal availableAmount;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

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
