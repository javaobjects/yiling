package com.yiling.f2b.admin.agreement.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-22
 */
@Data
@Accessors(chain = true)
@ApiModel("协议状态VO")
@AllArgsConstructor
@NoArgsConstructor
public class AgreementStatusCountVO {


	@ApiModelProperty(value = "进行中")
	private Integer start;

	@ApiModelProperty(value = "未开始")
	private Integer unStart;

	@ApiModelProperty(value = "已停用")
	private Integer stop;

	@ApiModelProperty(value = "已过期")
	private Integer expire;

}
