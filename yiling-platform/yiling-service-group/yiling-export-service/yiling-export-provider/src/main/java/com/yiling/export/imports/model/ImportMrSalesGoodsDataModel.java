package com.yiling.export.imports.model;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 导入医药代表可售药品数据 Form
 *
 * @author: xuan.zhou
 * @date: 2022/6/7
 */
@Data
public class ImportMrSalesGoodsDataModel extends BaseImportModel {

    /**
     * 员工ID
     */
    @NotNull(message = "未找到员工ID对应的信息")
    @Min(1L)
    @ExcelShow
    @Excel(name = "员工ID", orderNum = "10")
    private Long employeeId;

    @ExcelShow
    @Excel(name = "员工工号", orderNum = "20")
    private String code;

    @ExcelShow
    @Excel(name = "员工姓名", orderNum = "30")
    private String name;

    @ExcelShow
    @Excel(name = "员工手机号", orderNum = "40")
    private String mobile;

    @ExcelShow
    @Excel(name = "可售药品ID", orderNum = "50")
    private String salesGoodsIds;

    /**
     * 可售药品ID集合
     */
    private List<Long> goodsIds;
}
