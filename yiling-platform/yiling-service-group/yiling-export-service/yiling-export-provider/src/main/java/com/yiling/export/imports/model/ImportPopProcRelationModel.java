package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 导入pop采购关系 Form
 *
 * @author: dexi.yao
 * @date: 2023-06-20
 */
@Data
public class ImportPopProcRelationModel extends BaseImportModel {

    @ExcelShow
    @Excel(name = "*渠道商ID", orderNum = "0")
    @NotNull(message = "不能为空")
    private Long channelPartnerEid;

    @Excel(name = "渠道商", orderNum = "1")
    private String channelPartnerName;

    @ExcelShow
    @Excel(name = "*工业主体ID", orderNum = "2")
    @NotNull(message = "不能为空")
    private Long factoryEid;

    @Excel(name = "*工业主体", orderNum = "3")
    private String factoryName;

    @ExcelShow
    @Excel(name = "*配送商ID", orderNum = "4")
    @NotNull(message = "不能为空")
    private Long deliveryEid;

    @Excel(name = "配送商名称", orderNum = "5")
    private String deliveryName;

    @ExcelShow
    @Excel(name = "*履约开始时间", orderNum = "6")
    @NotEmpty(message = "不能为空")
    private String startTimeStr;

    @ExcelShow
    @Excel(name = "*履约结束时间", orderNum = "7")
    @NotEmpty(message = "不能为空")
    private String endTimeStr;

    @ExcelShow
    @Excel(name = "*商品模板编码", orderNum = "8")
    @NotEmpty(message = "不能为空")
    private String templateNumber;

}
