package com.yiling.user.agreement.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议申请订单关联表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_apply_order")
public class ApplyOrderDO extends BaseDO {

	private static final long serialVersionUID = 1L;

	/**
	 * 协议申请id
	 */
	private Long applyId;

	/**
	 * 协议申请单号
	 */
	private String applyCode;

	/**
	 * 销售主体Eid
	 */
	private Long eid;

	/**
	 * 采购方eid
	 */
	private Long secondEid;

	/**
	 * 协议Id
	 */
	private Long agreementId;

	/**
	 * 协议条件Id
	 */
	private Long agreementConditionId;

	/**
	 * 版本号
	 */
	@Version
	private Integer version;

	/**
	 * EAS企业账号
	 */
	private String easCode;

	/**
	 * 订单Id
	 */
	private Long orderId;

	/**
	 * 返利订单表Id
	 */
	private Long rebateOrderId;

	/**
	 * 订单编号
	 */
	private String orderCode;

	/**
	 * 单据类型 1- 订单 2-退货单 3-结算单
	 */
	private Integer type;

	/**
	 * 返利订单明细表id
	 */
	private String rebateOrderDetailId;

	/**
	 * 协议返还金额
	 */
	private BigDecimal discountAmount;

	/**
	 * 是否删除：0-否 1-是
	 */
	@TableLogic
	private Integer delFlag;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long createUser;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updateUser;

	/**
	 * 备注
	 */
	private String remark;


}
