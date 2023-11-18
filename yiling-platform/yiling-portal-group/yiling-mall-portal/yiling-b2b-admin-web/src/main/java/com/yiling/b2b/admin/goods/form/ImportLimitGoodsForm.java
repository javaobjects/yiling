package com.yiling.b2b.admin.goods.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.annotations.ExcelRepet;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * 商品定价信息导入 Form
 * @author: yuecheng.chen
 * @date: 2021/6/22
 */
@Data
public class ImportLimitGoodsForm implements IExcelModel, IExcelDataModel {

    private static final long serialVersionUID = -4672345329942342009L;

    @ExcelRepet
    @Excel(name = "*商品ID",orderNum = "0")
    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

    @Excel(name = "*商品名称",orderNum = "1")
    @NotNull(message = "商品名称不能为空")
    private String name;

    @Excel(name = "*批准文号",orderNum = "2")
    private String licenseNo;

    @Excel(name = "*包装规格",orderNum = "3")
    private String specifications;

    @Excel(name = "*生产厂家",orderNum = "4")
    private String manufacturer;

    @Excel(name = "错误信息")
    private String errorMsg;

    private Integer rowNum;

}
