package com.yiling.admin.data.center.enterprise.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReceiptAccountPageListForm extends QueryPageListForm {

	/**
	 * 企业名称（全模糊查询）
	 */
	@ApiModelProperty("企业名称")
	private String name;

	/**
	 * 执业许可证号/社会信用统一代码
	 */
	@ApiModelProperty("社会信用统一代码")
	private String licenseNumber;

	/**
	 * 类型：0-全部 1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
	 */
	@ApiModelProperty("类型")
	private Integer type;

	/**
	 * 账户状态 1-待审核 2-审核成功 3-审核失败
	 */
	@ApiModelProperty("账户状态")
	private Integer status;

	/**
	 * 最小提交时间
	 */
	@ApiModelProperty("最小提交时间")
	private Date minDate;

	/**
	 * 最大提交时间
	 */
	@ApiModelProperty("最大提交时间")
	private Date maxDate;
}
