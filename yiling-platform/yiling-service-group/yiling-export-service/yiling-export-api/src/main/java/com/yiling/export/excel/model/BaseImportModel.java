package com.yiling.export.excel.model;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * 导入模型基类
 *
 * @author: xuan.zhou
 * @date: 2022/12/1
 */
@Data
public abstract class BaseImportModel implements IExcelModel, IExcelDataModel, IExcelStatusModel {

    @Excel(name = "*状态", orderNum = "99998")
    private String status;

    @Excel(name = "错误信息", orderNum = "99999")
    private String  errorMsg;

    private Integer rowNum;
}
