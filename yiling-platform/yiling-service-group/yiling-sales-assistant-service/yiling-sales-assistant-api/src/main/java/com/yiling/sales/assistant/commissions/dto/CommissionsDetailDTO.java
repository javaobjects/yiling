package com.yiling.sales.assistant.commissions.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

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
public class CommissionsDetailDTO extends BaseDTO {


	private static final long serialVersionUID = -992626920184481175L;

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
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	/**
	 * 修改人
	 */
	private Long updateUser;

	/**
	 * 备注
	 */
	private String remark;


}
