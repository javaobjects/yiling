package com.yiling.admin.b2b.goods.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/5/19
 */
@Data
public class ImportGoodsForm implements IExcelModel, IExcelDataModel {

    private static final long serialVersionUID = -4672345329942342009L;

    private Long eid;

    @Excel(name = "*商品ID",orderNum = "0" )
    @NotNull(message = "不能为空")
    private Long gid;

    @Excel(name = "*企业名称",orderNum = "1" )
    private String ename;

    @Excel(name = "*商品名称",orderNum = "2")
    @NotNull(message = "不能为空")
    private String name;

    @Excel(name = "*售卖规格",orderNum = "3")
    @NotNull(message = "不能为空")
    private String specifications;

    @Excel(name = "*批准文号/生产许可证号",orderNum = "4")
    @NotNull(message = "不能为空")
    private String licenseNo;

    @Excel(name = "*生产厂家",orderNum = "5")
    @NotNull(message = "不能为空")
    private String manufacturer;

    @Excel(name = "生产地址",orderNum = "6")
    private String manufacturerAddress;

    @Excel(name = "*售价",orderNum = "7")
    @NotNull(message = "不能为空")
    private BigDecimal price;

    @Excel(name = "*销售包装",orderNum = "8")
    @NotNull(message = "不能为空")
    private Integer middlePackage;

    @Excel(name = "*库存",orderNum = "9")
    @NotNull(message = "不能为空")
    private Long qty;

    @Excel(name = "商品内码",orderNum = "10")
    private String inSn;

    @Excel(name = "商品编码",orderNum = "11")
    private String sn;

    @Excel(name = "备注", orderNum = "12")
    private String remark;

    @Excel(name = "商品状态", replace = {"待设置_3","下架_2", "上架_1"},orderNum = "13")
    private Integer goodsStatus;

    @Excel(name = "错误信息",orderNum = "14")
    private String errorMsg;

    private Integer rowNum;

}
