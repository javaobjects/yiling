package com.yiling.admin.b2b.promotion.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * <p>
 * 满赠活动商品导入
 * </p>
 *
 * @author yong.zhang
 * @date 2021-11-24
 */
@Data
public class ImportPromotionGoodsForm implements IExcelModel, IExcelDataModel {

    @ExcelRepet
    @ExcelShow
    @Excel(name = "商品ID", orderNum = "0")
    @NotNull(message = "不能为空")
    private Long goodsId;

    @Excel(name = "商品名称", orderNum = "1")
    @NotNull(message = "不能为空")
    private String name;

    @Excel(name = "包装规格", orderNum = "2")
    private String specifications;

    @Excel(name = "错误信息", orderNum = "3")
    private String errorMsg;

    private Integer rowNum;
}
