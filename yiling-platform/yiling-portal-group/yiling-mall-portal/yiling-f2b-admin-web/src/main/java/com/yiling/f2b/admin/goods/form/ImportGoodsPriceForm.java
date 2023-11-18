package com.yiling.f2b.admin.goods.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * 商品定价信息导入 Form
 * @author: yuecheng.chen
 * @date: 2021/6/22
 */
@Data
public class ImportGoodsPriceForm implements IExcelModel, IExcelDataModel {

    private static final long serialVersionUID = -4672345329942342009L;

    @Excel(name = "企业ID（从用户信息获取）")
    private Long eid;

    @Excel(name = "商品ID")
    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

    @Excel(name = "客户ID")
    private Long customerEid;

    @Excel(name = "客户名称")
    private Long customerName;

    @Excel(name = "客户分组ID")
    private Long customerGroupId;

    @Excel(name = "客户分组名称")
    private Long customerGroupName;

    /**
     * 定价规则：1-浮动点位 2-具体价格
     */
    private Integer priceRule;

    private BigDecimal priceValue;

    @Excel(name = "设置具体价格")
    private BigDecimal specificPriceValue;

    @Excel(name = "设置浮点价格")
    private BigDecimal floatPriceValue;

    @ExcelShow
    @ExcelRepet
    @Excel(name = "商品名称")
    private String name;

    @ExcelShow
    @ExcelRepet
    @Excel(name = "批准文号/生产许可证号")
    private String licenseNo;

    @ExcelShow
    @Excel(name = "生产厂家")
    private String manufacturer;

    @Excel(name = "生产地址")
    private String manufacturerAddress;

    @Excel(name = "包装规格")
    private String specifications;

    @Excel(name = "价格")
    private BigDecimal price;

    @Excel(name = "错误信息")
    private String errorMsg;

    private Integer rowNum;

}
