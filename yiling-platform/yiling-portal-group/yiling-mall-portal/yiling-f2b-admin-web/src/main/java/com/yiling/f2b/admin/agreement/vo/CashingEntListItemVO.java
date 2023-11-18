package com.yiling.f2b.admin.agreement.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CashingEntListItemVO extends BaseVO {

	/**
	 * 企业id
	 */
	@ApiModelProperty(value = "企业id")
	private Long eid;

	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称")
	private String name;

	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "一级商编码列表")
	private List<EasCode> easAccountList;


	@Data
	public static class EasCode {

		/**
		 * 企业账号
		 */
		@ApiModelProperty(value = "一级商编码")
		private String easCode;
	}
}
