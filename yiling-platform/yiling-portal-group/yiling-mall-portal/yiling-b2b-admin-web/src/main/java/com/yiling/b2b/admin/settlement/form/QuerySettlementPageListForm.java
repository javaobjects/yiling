package com.yiling.b2b.admin.settlement.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
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
public class QuerySettlementPageListForm extends QueryPageListForm {

	@ApiModelProperty("结算单号")
	private String  code;

	@ApiModelProperty("开始生成结算单最小时间")
	private Date minTime;

	@ApiModelProperty("开始生成结算单最大时间")
	private Date  maxTime;

	@ApiModelProperty("结算单类型 1-货款结算单 2-促销结算单 3-预售违约金结算单")
	private Integer  type;

	@ApiModelProperty("结算单状态 1-待结算 2-银行处理中 3-已结算 4-银行处理失败")
	private Integer  status;
}
