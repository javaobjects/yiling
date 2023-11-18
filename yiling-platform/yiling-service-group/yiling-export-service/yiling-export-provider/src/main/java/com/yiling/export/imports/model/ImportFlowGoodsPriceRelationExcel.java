package com.yiling.export.imports.model;

import com.yiling.export.excel.model.BaseImportModel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author shichen
 * @类名 ImportFlowGoodsPriceRelationExcel
 * @描述
 * @创建时间 2023/2/22
 * @修改人 shichen
 * @修改时间 2023/2/22
 **/
@Data
public class ImportFlowGoodsPriceRelationExcel extends BaseImportModel {

    @Excel(name = "原始产品名称", orderNum = "0")
    private String oldGoodsName;

    @Excel(name = "原始产品规格", orderNum = "1")
    private String spec;

    @Excel(name = "对应产品代码", orderNum = "2")
    private Long goodsCode;

    @Excel(name = "对应产品名称", orderNum = "3")
    private String goodsName;

    @Excel(name = "经销商代码", orderNum = "4")
    private String customerCode;

    @Excel(name = "经销商名称", orderNum = "5")
    private String customer;
}
