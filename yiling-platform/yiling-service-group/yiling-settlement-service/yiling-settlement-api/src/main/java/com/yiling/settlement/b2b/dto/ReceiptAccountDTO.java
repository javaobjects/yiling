package com.yiling.settlement.b2b.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReceiptAccountDTO extends BaseDTO {

	private static final long serialVersionUID = 5518633152029703877L;

	/**
	 * 企业ID
	 */
	private Long eid;

	/**
	 * 企业收款账户名称
	 */
	private String name;

	/**
	 * 银行账户
	 */
	private String account;

	/**
	 * 开户行名称
	 */
	private String bankName;

	/**
	 * 支行名称
	 */
	private String subBankName;

	/**
	 * 总行id
	 */
	private Long bankId;

	/**
	 * 支行id
	 */
	private Long branchBankId;

	/**
	 * 总行编码
	 */
	private String bankCode;

	/**
	 * 支行行号
	 */
	private String branchNum;

	/**
	 * 省份code
	 */
	private String provinceCode;

	/**
	 * 省份name
	 */
	private String provinceName;

	/**
	 * 市code
	 */
	private String cityCode;

	/**
	 * 市name
	 */
	private String cityName;

	/**
	 * 账户状态 1-待审核 2-审核成功 3-审核失败
	 */
	private Integer status;

	/**
	 * 审核时间
	 */
	private Date auditTime;

	/**
	 * 审核描述
	 */
	private String auditRemark;

	/**
	 * 审核人
	 */
	private Long auditUser;

	/**
	 * 失效状态：1-有效 2-失效
	 */
	private Integer invalid;

	/**
	 * 其他证照
	 */
	private String licence;

	/**
	 * 收款账户提交审核时间
	 */
	private Date submitTime;

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
