package com.yiling.admin.data.center.report.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * 导入参数 Form
 *
 * @author: dexi.yao
 * @date: 2022/07/14
 */
@Data
public class ImportOrderIdentForm implements IExcelModel, IExcelDataModel {

    @ExcelShow
    @Excel(name = "*订单编号")
    @NotEmpty(message ="不能为空")
    private String  orderCode;


    @ExcelShow
    @Excel(name = "*订单详情编号")
    @NotNull(message ="不能为空")
    private Long  id;

    @ExcelShow
    @Excel(name = "采购商ID")
    private Long  buyerId;

    @ExcelShow
    @Excel(name = "采购商名称")
    private String  buyerName;

    @ExcelShow
    @Excel(name = "商业名称")
    private String  entName;

    @ExcelShow
    @Excel(name = "供应商ID")
    private Long  sellerId;

    @ExcelShow
    @Excel(name = "供应商名称")
    private String  sellerName;

    @ExcelShow
    @Excel(name = "商品ID")
    private Long  goodsId;

    @ExcelShow
    @Excel(name = "商品内码")
    private String  goodsErpCode;

    @ExcelShow
    @Excel(name = "商品名称")
    private String  goodsName;

    @ExcelShow
    @Excel(name = "商品规格")
    private String  specification;

    @ExcelShow
    @Excel(name = "以岭品ID")
    private Long  ylGoodsId;

    @ExcelShow
    @Excel(name = "以岭品名称")
    private String  ylGoodsName;

    @ExcelShow
    @Excel(name = "以岭品规格")
    private String  ylGoodsSpecification;

    @NotNull(message ="不能为空")
    @ExcelShow
	@Range(min = 1, max = 3, message = "标识状态不正确")
    @Excel(name = "*标识状态（1.正常订单；2.无效订单；3.异常订单）")
    private Integer identificationStatus;

    @ExcelShow
	@Range(min = 1, max = 5, message = "标识状态不正确")
    @Excel(name = "*异常情况（标识状态为异常订单时必填，1.打单商业；2.锁定终端；3.疑似商业；4.库存不足；5.其它）")
    private Integer abnormalReason;

    @ExcelShow
    @Excel(name = "*其它异常场景（异常情况为其它时必填）")
    private String abnormalDescribed;


    @Excel(name = "错误信息")
    private String  errorMsg;

    private Integer rowNum;
}
