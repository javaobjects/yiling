package com.yiling.sales.assistant.app.agreement.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("年度协议列表VO")
public class YearAgreementPageListItemVO extends BaseVO {

	/**
	 * 主体名称
	 */
	@ApiModelProperty(value = "主体名称")
	private String  ename;

	/**
	 * 乙方渠道类型名称
	 */
	@ApiModelProperty(value = "乙方渠道类型名称")
	private String  secondChannelName;

	/**
	 * 补充协议个数
	 */
	@ApiModelProperty(value = "补充协议个数")
	private Integer tempCount;

	/**
	 * 协议下的商品数
	 */
	@ApiModelProperty(value = "协议下的商品数")
	private Integer goodsCount;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	private Date endTime;


}
