package com.yiling.f2b.admin.agreement.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 三方新协议企业列表vo
 * @author dexi.yao
 * @date 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("三方协议企业列表vo")
public class ThirdAgreementEntListVO extends BaseVO {

	/**
	 * 企业id
	 */
	@ApiModelProperty(value = "企业id")
	private Long eid;

//	/**
//	 * 协议角色 1是乙方 2是丙方
//	 */
//	@ApiModelProperty(value = "协议角色 1是乙方 2是丙方")
//	private Integer   role;

	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称")
	private String   name;

	/**
	 * 所属省份名称
	 */
	@ApiModelProperty(value = "所属省份名称",hidden = true)
	@JsonIgnore
	private String provinceName;

	/**
	 * 所属城市名称
	 */
	@ApiModelProperty(value = "所属城市名称",hidden = true)
	@JsonIgnore
	private String cityName;

	/**
	 * 所属区域名称
	 */
	@ApiModelProperty(value = "所属区域名称",hidden = true)
	@JsonIgnore
	private String regionName;

	/**
	 * 详细地址
	 */
	@ApiModelProperty(value = "详细地址")
	private String address;

	/**
	 * 补充协议个数
	 */
	@ApiModelProperty(value = "补充协议个数")
	private Integer supplementAgreementCount;

	public String getAddress() {
		return provinceName+" "+cityName+" "+regionName+" "+address;
	}
}
