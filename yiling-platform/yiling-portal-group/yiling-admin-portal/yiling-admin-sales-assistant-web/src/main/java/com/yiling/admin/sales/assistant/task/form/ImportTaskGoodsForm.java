package com.yiling.admin.sales.assistant.task.form;

import javax.validation.constraints.NotNull;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * 任务以岭品导入
 * @author gxl
 */
@Data
public class ImportTaskGoodsForm implements IExcelModel, IExcelDataModel {

    @Excel(name = "商品ID",orderNum = "0" )
    @NotNull(message = "不能为空")
    private Long gid;

    @Excel(name = "商品名称",orderNum = "2")
    private String name;

    @Excel(name = "售卖规格",orderNum = "3")
    private String specifications;

    @Excel(name = "备注", orderNum = "12")
    private String remark;

    @Excel(name = "错误信息",orderNum = "14")
    private String errorMsg;

    private Integer rowNum;

}
