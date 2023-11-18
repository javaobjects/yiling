package com.yiling.user.agreement.bo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 双方协议
 *
 * @author dexi.yao
 * @date 2021-06-04
 */
@Data
@Accessors(chain = true)
public class AgreementPageListItemBO implements Serializable {
    private static final long serialVersionUID = -9044217574008714L;
	/**
	 * 协议id
	 */
	private Long   id;

	/**
	 * 乙方id
	 */
	private String secondEid;

	/**
	 * 乙方名称
	 */
	private String secondName;

	/**
	 * 乙方渠道类型
	 */
	private String secondChannelName;

	/**
	 * 丙方eid
	 */
	private String thirdEid;

	/**
	 * 丙方名称
	 */
	private String thirdName;

	/**
	 * 丙方渠道类型名称
	 */
	private String thirdChannelName;

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
	 * 主体名称
	 */
	private String  ename;


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
}
