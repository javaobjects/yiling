package com.yiling.admin.data.center.report.form;

import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/7/25
 */
@Data
public class ImportFlowGoodsRelationForm implements IExcelModel, IExcelDataModel {

    @ExcelShow
    @Excel(name = "*商业ID", orderNum = "10")
    private Long eid;

    @ExcelShow
    @Excel(name = "商业名称", orderNum = "20")
    private String ename;

    @ExcelShow
    @Excel(name = "商品名称", orderNum = "30")
    private String goodsName;

    @ExcelShow
    @Excel(name = "商品规格", orderNum = "40")
    private String goodsSpecifications;

    @ExcelShow
    @Excel(name = "*商品内码", orderNum = "50")
    private String goodsInSn;

    @ExcelShow
    @Excel(name = "*以岭商品ID", orderNum = "60")
    private Long ylGoodsId;

    @ExcelShow
    @Excel(name = "以岭商品名称", orderNum = "70")
    private String ylGoodsName;

    @ExcelShow
    @Excel(name = "以岭商品规格", orderNum = "80")
    private String ylGoodsSpecifications;

    @ExcelShow
    @Excel(name = "商品关系标签：0-无标签 1-以岭品 2-非以岭品 3-中药饮片", orderNum = "80")
    private Integer goodsRelationLabel;

    private Long id;

    @Excel(name = "错误信息", orderNum = "99999")
    private String errorMsg;

    private Integer rowNum;
}
