package com.yiling.b2b.admin.goods.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

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
public class ImportGoodsGroupPriceForm implements IExcelModel, IExcelDataModel {

    private static final long serialVersionUID = -4672345329942342009L;

    private Long eid;

    @Excel(name = "商品库ID",orderNum = "0")
    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

    /**
     * 定价规则：1-浮动点位 2-具体价格
     */
    private Integer priceRule;

    private BigDecimal priceValue;

    @Excel(name = "药品名称",orderNum = "1")
    private String name;

    @Excel(name = "批准文号/生产许可证号",orderNum = "2")
    private String licenseNo;

    @Excel(name = "生产厂家",orderNum = "3")
    private String manufacturer;

    @Excel(name = "生产地址",orderNum = "4")
    private String manufacturerAddress;

    @Excel(name = "售卖规格",orderNum = "5")
    private String specifications;

    @Excel(name = "库存",orderNum = "6")
    private Long qty;

    @Excel(name = "挂网价（售价）",orderNum = "7")
    private BigDecimal price;

    @Excel(name = "售卖包装",orderNum = "8")
    private Long bigPackage;

    @Excel(name = "客户分组ID",orderNum = "9")
    @NotNull(message = "客户分组ID不能为空")
    private Long customerGroupId;

    @Excel(name = "客户分组名称",orderNum = "10")
    private String customerGroupName;

    @Excel(name = "设置具体价格",orderNum = "11")
    private BigDecimal specificPriceValue;

    @Excel(name = "设置浮点价格",orderNum = "12")
    private BigDecimal floatPriceValue;

    @Excel(name = "错误信息")
    private String errorMsg;

    private Integer rowNum;

}
