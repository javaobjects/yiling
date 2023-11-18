package com.yiling.sales.assistant.app.agreement.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryYearAgreementPageListForm extends QueryPageListForm {

	@NotNull
	@ApiModelProperty(value = "当前查询企业eid")
	private Long   queryEid;

	@NotNull
	@ApiModelProperty(value = "协议状态：0- 全部 1-进行中 2-未开始 3-已停用 4-已过期 5-进行中&未开始")
	private Integer   agreementStatus;

	/**
	 * 协议id
	 */
	@ApiModelProperty(value = "协议id")
	private Long id;

	/**
	 * 协议主体id
	 */
	@ApiModelProperty(value = "协议主体id")
	private Long eid;

	/**
	 * 履约开始时间
	 */
	@ApiModelProperty(value = "履约开始时间")
	private Date startTime;

	/**
	 * 履约结束时间
	 */
	@ApiModelProperty(value = "履约结束时间")
	private Date endTime;
}
