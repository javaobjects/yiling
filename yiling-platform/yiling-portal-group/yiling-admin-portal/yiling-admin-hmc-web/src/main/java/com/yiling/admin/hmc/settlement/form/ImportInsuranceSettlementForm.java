package com.yiling.admin.hmc.settlement.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/4/20
 */
@Data
public class ImportInsuranceSettlementForm implements IExcelModel, IExcelDataModel {

    @ExcelShow
    @Excel(name = "客户姓名", orderNum = "0")
    @NotBlank(message = "不能为空")
    private String issueName;

    @Excel(name = "保单号", orderNum = "1")
    @NotBlank(message = "不能为空")
    private String policyNo;

    @Excel(name = "打款时间", orderNum = "2")
    @NotNull(message = "不能为空")
    private String payTime;

    @Excel(name = "打款金额", orderNum = "3")
    @NotBlank(message = "不能为空")
    private String payAmount;

    @Excel(name = "收款账号", orderNum = "4")
    @NotBlank(message = "不能为空")
    private String accountNo;

    @Excel(name = "服务商ID", orderNum = "5")
    @NotNull(message = "不能为空")
    private Long insuranceCompanyId;

    @Excel(name = "对应第三方打款流水号", orderNum = "6")
    private String thirdPayNo;

    @Excel(name = "对应药品订单号", orderNum = "7")
    @NotBlank(message = "不能为空")
    private String orderNoStr;


    @Excel(name = "错误信息", orderNum = "8")
    private String errorMsg;

    private Integer rowNum;
}
