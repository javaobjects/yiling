package com.yiling.sales.assistant.commissions.entity;

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
 * 销售助手佣金记录表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_commissions")
public class CommissionsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 任务id
	 */
	private Long taskId;

    /**
     * 任务的属企业eid
     */
    private Long taskOwnershipEid;

    /**
     * 任务的人的用户类型：1-以岭人员 2-小三元 3-自然人
     */
    private Integer taskUserType;

    /**
     * 获佣人的属企业eid
     */
    private Long ownershipEid;

    /**
     * 获佣人的用户类型：1-以岭人员 2-小三元 3-自然人
     */
    private Integer userType;

    /**
     * 下线用户id
     */
    private Long subordinateUserId;

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 用户任务id
	 */
	private Long userTaskId;

	/**
	 * 佣金金额
	 */
	private BigDecimal amount;

	/**
	 * 以结算金额
	 */
	private BigDecimal paidAmount;

	/**
	 * 待结算金额
	 */
	private BigDecimal surplusAmount;

	/**
	 * 佣金来源 1-任务收益 2-下线推广
	 */
	private Integer sources;

	/**
	 * 佣金类型 1-入账 2-出账
	 */
	private Integer type;

	/**
	 * 佣金是否有效 1-无效 2-有效
	 */
	private Integer effectStatus;

	/**
	 * 任务类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买
	 */
	private Integer finishType;

	/**
	 * 结算状态 1-未结清 2-已结清
	 */
	private Integer status;

	/**
	 * 生效时间
	 */
	private Date effectTime;

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
