package com.yiling.user.agreement.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EntPageListItemDTO extends BaseDTO {

	private static final long serialVersionUID = 1748048735972390469L;

	/**
	 * 企业名称
	 */
	private String name;

	/**
	 * 联系人
	 */
	private String contactor;

	/**
	 * 联系人电话
	 */
	private String contactorPhone;

	/**
	 * 所属省份名称
	 */
	private String provinceName;

	/**
	 * 所属城市名称
	 */
	private String cityName;

	/**
	 * 所属区域名称
	 */
	private String regionName;

	/**
	 * 详细地址
	 */
	private String address;

	/**
	 * 年度协议数量
	 */
	private Integer agreementCount;

}
