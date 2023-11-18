package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yiling.export.excel.model.EasyExcelBaseModel;
import com.yiling.framework.common.annotations.ExcelShow;

import lombok.Data;

/**
 * 窜货申报确认上传
 *
 * @author: yong.zhang
 * @date: 2023/3/15 0015
 */
@Data
public class ImportConfirmFleeingGoodsModel extends EasyExcelBaseModel {

    @ExcelShow
    @ExcelProperty(value = "销售时间", index = 0)
    private String soTimeStr;

    //    @ExcelShow
    //    @Excel(name = "经销商名称", orderNum = "1")
    //    @NotEmpty(message = "不能为空")
    //    private String name;

    @ExcelShow
    @ExcelProperty(value = "客户名称", index = 1)
    @NotEmpty(message = "不能为空")
    private String enterpriseName;

    @ExcelShow
    @ExcelProperty(value = "原始产品名称", index = 2)
    @NotEmpty(message = "不能为空")
    private String goodsName;

    @ExcelShow
    @ExcelProperty(value = "原始产品规格", index = 3)
    @NotEmpty(message = "不能为空")
    private String soSpecifications;

    @ExcelShow
    @ExcelProperty(value = "数量（盒）", index = 4)
    @NotEmpty(message = "不能为空")
    private String soQuantity;

    @ExcelProperty(value = "单位", index = 5)
    private String soUnit;

    @ExcelProperty(value = "单价", index = 6)
    private String soPrice;

    @ExcelProperty(value = "金额", index = 7)
    private String soTotalAmount;

    @ExcelProperty(value = "业务代表工号", index = 8)
    private String representativeCode;

    @ExcelProperty(value = "业务代表姓名", index = 9)
    private String representativeName;

    @ExcelProperty(value = "业务部门", index = 10)
    private String deptName;

    @ExcelProperty(value = "备注", index = 11)
    private String notes;
}
