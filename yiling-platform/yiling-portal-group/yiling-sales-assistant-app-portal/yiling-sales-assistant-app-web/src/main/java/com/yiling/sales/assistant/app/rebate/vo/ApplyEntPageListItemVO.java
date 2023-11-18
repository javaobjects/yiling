package com.yiling.sales.assistant.app.rebate.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 返利申请单-企业信息
 * @author dexi.yao
 * @date 2021-09-10
 */
@ApiModel("返利申请单-企业信息")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ApplyEntPageListItemVO extends BaseVO {

	/**
	 * 企业名称
	 */
	@ApiModelProperty("企业名称")
	private String name;

	/**
	 * 联系人
	 */
	@ApiModelProperty("联系人")
	private String contactor;

	/**
	 * 联系人电话
	 */
	@ApiModelProperty("联系人电话")
	private String contactorPhone;

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
	@ApiModelProperty(value = "详细地址",hidden = true)
	@JsonIgnore
	private String address;

	/**
	 * 详细地址
	 */
	@ApiModelProperty("详细地址")
	private String entAddress;

	public String getEntAddress() {
		return provinceName+cityName+regionName+address;
	}
}
