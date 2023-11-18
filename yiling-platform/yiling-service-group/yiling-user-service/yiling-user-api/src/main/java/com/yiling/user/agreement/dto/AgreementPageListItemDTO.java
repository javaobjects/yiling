package com.yiling.user.agreement.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 主协议列表dto
 * @author dexi.yao
 * @date 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementPageListItemDTO extends BaseDTO {


	private static final long serialVersionUID = 3083121379171571938L;

	/**
	 * 乙方id
	 */
	private Long secondEid;

	/**
	 * 乙方渠道类型名称
	 */
	private String  secondChannelName;

	/**
	 * 协议名称
	 */
	private String name;

	/**
	 * 协议描述
	 */
	private String content;

	/**
	 * 主体名称
	 */
	private String  ename;


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
