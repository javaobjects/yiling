package com.yiling.user.agreement.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 返利申请明细表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddRebateApplyDetailRequest extends BaseRequest {


	/**
	 * id
	 */
	private Long id;

	/**
	 * 返利申请表id
	 */
	private Long applyId;

	/**
	 * 申请单号
	 */
	private String applyCode;

	/**
	 * 明细类型 1-协议类型 2-其他
	 */
	private Integer detailType;

	/**
	 * 协议id
	 */
	private Long agreementId;

	/**
	 * 协议版本号
	 */
	private Integer version;

	/**
	 * 订单数量
	 */
	private Integer orderCount;

	/**
	 * 返利金额
	 */
	private BigDecimal amount;

	/**
	 * 销售组织id
	 */
	private Long sellerEid;

	/**
	 * 销售组织名称
	 */
	private String sellerName;

	/**
	 * 销售组织easCode
	 */
	private String sellerCode;

	/**
	 * 返利种类
	 */
	private String rebateCategory;

	/**
	 * 费用科目
	 */
	private String costSubject;

	/**
	 * 费用归属部门
	 */
	private String costDept;

	/**
	 * 执行部门
	 */
	private String executeDept;

	/**
	 * 批复代码
	 */
	private String replyCode;


	/**
	 * 描述
	 */
	private String entryDescribe;

	/**
	 * 申请时间
	 */
	private String applyTime;


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
