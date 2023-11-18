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
 * 销售助手佣金明细表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_commissions_detail")
public class CommissionsDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;

	/**
	 * 佣金记录id
	 */
	private Long commissionsId;

	/**
	 * 兑付佣金记录id
	 */
	private Long paidCommissionsId;

	/**
	 * 用户id
	 */
	private Long userId;

    /**
	 * 任务id
	 */
	private Long taskId;

    /**
	 * 用户任务id
	 */
	private Long userTaskId;

    /**
	 * 订单id
	 */
	private Long orderId;

	/**
	 * 订单编号
	 */
	private String orderCode;

	/**
	 * 拉新企业id
	 */
	private Long newEntId;

	/**
	 * 拉新企业名称
	 */
	private String newEntName;

	/**
	 * 拉新用户id
	 */
	private Long newUserId;

	/**
	 * 拉新用户名
	 */
	private String newUserName;

	/**
	 * 购买会员企业id
	 */
	private Long buyMemberEid;

	/**
	 * 购买会员企业名称
	 */
	private String buyMemberName;

	/**
	 * 拉新时间
	 */
	private Date newTime;

	/**
	 * 第一次资料上传时间
	 */
	private Date firstUploadTime;

	/**
	 * 佣金金额
	 */
	private BigDecimal subAmount;

	/**
	 * 兑付状态 1-未兑付 2-以兑付
	 */
	private Integer status;

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
