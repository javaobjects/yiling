package com.yiling.admin.sales.assistant.banner.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * @author dexi.yao
 * @date 2021-09-26
 */
@Data
public class ImportCommissionsForm implements IExcelModel, IExcelDataModel {

	/**
	 * 佣金明细id
	 */
	@ExcelRepet
	@ExcelShow
	@Excel(name = "佣金明细ID")
	@NotNull
	private Long id;


	/**
	 * 任务名称
	 */
	@ExcelShow
	@Excel(name = "任务名称")
	private String taskName;


	/**
	 * 用户任务id
	 */
	@ExcelShow
	@Excel(name = "任务ID")
	private Long userTaskId;

	/**
	 * 订单编号
	 */
	@ExcelShow
	@Excel(name = "订单编号")
	private String orderCode;

	/**
	 * 拉新企业id
	 */
	@ExcelShow
	@Excel(name = "邀户ID")
	private Long newEntId;

	/**
	 * 拉新用户id
	 */
	@ExcelShow
	@Excel(name = "邀人ID")
	private Long newUserId;

	/**
	 * 任务类型
	 */
	@ExcelShow
	@Excel(name = "任务类型")
	private String finishTypeName;

	/**
	 * 开始时间
	 */
	@ExcelShow
	@Excel(name = "开始时间")
	private String startTime;

	/**
	 * 结束时间
	 */
	@ExcelShow
	@Excel(name = "结束时间")
	private String endTime;

//	/**
//	 * 佣金金额
//	 */
//	@ExcelShow
//	@Excel(name = "发放金额")
//	@NotNull
//	private BigDecimal subAmount;

	/**
	 * 用户名子
	 */
	@ExcelShow
	@Excel(name = "获佣人")
	private String userName;

	@Excel(name = "错误信息")
	private String errorMsg;

	private Integer rowNum;
}
