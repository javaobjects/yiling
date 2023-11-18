package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 商家品与以岭品对应关系导入 Model
 *
 * @author: houjie.sun
 * @date: 2022/12/29
 */
@Data
public class ImportFlowGoodsRelationModel extends BaseImportModel {

    @ExcelShow
    @Excel(name = "*商业ID", orderNum = "1")
    private Long eid;

    @ExcelShow
    @Excel(name = "商业名称", orderNum = "2")
    private String ename;

    @ExcelShow
    @Excel(name = "商品名称", orderNum = "3")
    private String goodsName;

    @ExcelShow
    @Excel(name = "商品规格", orderNum = "4")
    private String goodsSpecifications;

    @ExcelShow
    @Excel(name = "*商品内码", orderNum = "5")
    private String goodsInSn;

    @ExcelShow
    @Excel(name = "生产厂家", orderNum = "6")
    private String goodsManufacturer;

    @ExcelShow
    @Excel(name = "*商品关系标签(1-以岭品,2-非以岭品,3-中药饮片)", orderNum = "7")
    private Integer goodsRelationLabel;

    @ExcelShow
    @Excel(name = "*以岭商品ID(商品关系标签为1时必填)", orderNum = "8")
    private Long ylGoodsId;

    @ExcelShow
    @Excel(name = "以岭商品名称", orderNum = "9")
    private String ylGoodsName;

    @ExcelShow
    @Excel(name = "以岭商品规格", orderNum = "10")
    private String ylGoodsSpecifications;

    private Long id;

}
