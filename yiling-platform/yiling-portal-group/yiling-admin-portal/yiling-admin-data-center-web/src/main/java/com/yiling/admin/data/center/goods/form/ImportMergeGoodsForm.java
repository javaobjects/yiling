package com.yiling.admin.data.center.goods.form;

import javax.validation.constraints.NotNull;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/4/21
 */
@Data
public class ImportMergeGoodsForm implements IExcelModel, IExcelDataModel {

    @Excel(name = "*标准库ID",orderNum = "0")
    @NotNull(message = "不能为空")
    private Long standardId;

    @Excel(name = "商品名称",orderNum = "1")
    @NotNull(message = "不能为空")
    private String goodsName;

    @Excel(name = "*包装规格ID(删除)",orderNum = "2")
    @NotNull(message = "不能为空")
    private String specificationsId;

    @Excel(name = "包装规格",orderNum = "3")
    private String specifications;

    @Excel(name = "*处理方式",orderNum = "4")
    @NotNull(message = "不能为空")
    private String type;

    @Excel(name = "*合并规格ID(保留)",orderNum = "5")
    @NotNull(message = "不能为空")
    private String mergeSpecificationsId;

    @Excel(name = "合并规格",orderNum = "6")
    private String mergeSpecifications;

    @Excel(name = "错误信息",orderNum = "7")
    private String errorMsg;

    private Integer rowNum;

}
