package com.yiling.user.agreement.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TempAgreementListItemDTO extends BaseDTO {

	private static final long serialVersionUID = -1464928790425610421L;
	/**
	 * 协议名称
	 */
	private String name;

	/**
	 * 协议描述
	 */
	private String content;

	/**
	 * 协议分类：1-主协议 2-临时协议
	 */
	private Integer category;

	/**
	 * 任务类型：1-采购类  （等产品完善说明再补充）2-其他
	 */
	private Integer taskType;

	/**
	 * 采购的类型：1- 采购额 2- 采购量 3- 采购频次 4-数据直连
	 */
	private Integer taskPurchaseType;

	/**
	 * 开始时间
	 */
	private Date startTime;

	/**
	 * 结束时间
	 */
	private Date endTime;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 创建人名称
	 */
	private String createUserName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 修改人
	 */
	private Long updateUser;
	/**
	 * 修改人名称
	 */
	private String updateUserName;

	/**
	 * 修改时间
	 */
	private Date updateTime;
}
