package com.yiling.order.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 票折信息
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ticket_discount_record")
public class TicketDiscountRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
	 * 执行人工号
	 */
	private String executeUserCode;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
