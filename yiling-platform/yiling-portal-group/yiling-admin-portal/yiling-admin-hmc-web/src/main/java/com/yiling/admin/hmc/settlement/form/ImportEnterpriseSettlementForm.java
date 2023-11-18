package com.yiling.admin.hmc.settlement.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.annotations.ExcelRepet;
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
public class ImportEnterpriseSettlementForm implements IExcelModel, IExcelDataModel {

    @ExcelShow
    @Excel(name = "商品名称", orderNum = "0")
    private String goodsName;

    @Excel(name = "售卖数量（真实给到用户货的退的不算）", orderNum = "1")
    private Long count;

    @Excel(name = "以岭给终端结算单价", orderNum = "2")
    private String price;

    @Excel(name = "合计结算额", orderNum = "3")
    private String goodsAmount;

    @ExcelRepet
    @Excel(name = "订单明细编号", orderNum = "4")
    @NotNull(message = "不能为空")
    private Long detailId;

    @Excel(name = "订单编号", orderNum = "5")
    @NotBlank(message = "不能为空")
    private String orderNo;

    @Excel(name = "创建日期", orderNum = "6")
    private String createTime;

    @Excel(name = "订单完成日期", orderNum = "7")
    private String finishTime;

    @Excel(name = "管控渠道", orderNum = "8")
    private String channelName;

    @Excel(name = "保司结算状态", orderNum = "9")
    @NotBlank(message = "不能为空")
    private String insuranceSettlementStatus;

    @Excel(name = "订单状态", orderNum = "10")
    @NotBlank(message = "不能为空")
    private String orderStatus;

    @Excel(name = "药品服务终端名称", orderNum = "11")
    @NotBlank(message = "不能为空")
    private String ename;

    @Excel(name = "药品服务终端ID", orderNum = "12")
    @NotNull(message = "不能为空")
    private Long eid;

    @Excel(name = "结账金额", orderNum = "13")
    @NotNull(message = "不能为空")
    private String settlementAmount;

    @Excel(name = "对账执行时间", orderNum = "14")
    @NotNull(message = "不能为空")
    private String executionTime;

    @Excel(name = "结算完成时间", orderNum = "15")
    @NotNull(message = "不能为空")
    private String settlementTime;


    @Excel(name = "错误信息", orderNum = "16")
    private String errorMsg;

    private Integer rowNum;
}
