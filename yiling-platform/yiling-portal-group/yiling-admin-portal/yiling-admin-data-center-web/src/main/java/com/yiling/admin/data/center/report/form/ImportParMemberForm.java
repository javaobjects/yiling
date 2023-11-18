package com.yiling.admin.data.center.report.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.dataflow.order.bo.FlowGoodsRelationBO;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 导入参数 Form
 *
 * @author: dexi.yao
 * @date: 2022/07/14
 */
@Data
public class ImportParMemberForm implements IExcelModel, IExcelDataModel {

    @ExcelShow
    @Excel(name = "*商业ID")
    @NotNull(message ="不能为空")
    private Long  eid;

    @ExcelShow
    @Excel(name = "商业名称")
    private String  entName;

    @NotNull(message ="不能为空")
    @ExcelShow
    @Range(min = 0, message = "返利金额不正确")
    @Excel(name = "*会员金额")
    private BigDecimal thresholdAmount;

    @NotNull(message ="不能为空")
    @ExcelShow
	@Range(min = 2, max = 3, message = "会员来源不正确")
    @Excel(name = "*会员来源（2、b2b，3、销售助手）")
    private Integer memberSource;

    /**
     * 会员id
     */
    @NotNull(message ="不能为空")
    @ExcelShow
    @Range(min = 1, message = "会员id不正确")
    @Excel(name = "*会员id")
    private Long memberId;

    /**
     * 奖奖励金额
     */
    @NotNull(message ="不能为空")
    @ExcelShow
    @Range(min = 0 ,message = "奖励金额不正确")
    @Excel(name = "*奖励金额")
    private BigDecimal rewardValue;

    @NotNull(message ="不能为空")
    @ExcelShow
	@Range(min = 1, max = 2,message = "金额类型不正确")
    @Excel(name = "*金额类型（1、元/个，2、%销售额）")
    private Integer rewardType;

    /**
     * 开始时间
     */
    @NotEmpty
    @ExcelShow
    @Excel(name = "*开始时间（例：2022-7-11）")
    private String startTime;

    /**
     * 结束时间
     */
    @NotEmpty
    @ExcelShow
    @Excel(name = "*结束时间（例：2022-7-11）")
    private String endTime;

    @Excel(name = "错误信息")
    private String  errorMsg;

    private Integer rowNum;
}
