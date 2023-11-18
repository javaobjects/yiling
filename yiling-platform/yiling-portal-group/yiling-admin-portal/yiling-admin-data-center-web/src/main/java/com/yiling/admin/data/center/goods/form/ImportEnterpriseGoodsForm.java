package com.yiling.admin.data.center.goods.form;

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
public class ImportEnterpriseGoodsForm implements IExcelModel, IExcelDataModel {

    private static final long serialVersionUID = -4672345329942342009L;

    @Excel(name = "*企业ID",orderNum = "0")
    @NotNull(message = "不能为空")
    private Long eid;

    private Long gid;

    @Excel(name = "*商品名称",orderNum = "1")
    @NotNull(message = "不能为空")
    private String name;

    @Excel(name = "*批准文号/生产许可证号",orderNum = "2")
    private String licenseNo;

    @Excel(name = "*生产厂家",orderNum = "3")
    @NotNull(message = "不能为空")
    private String manufacturer;

    @Excel(name = "生产地址",orderNum = "4")
    private String manufacturerAddress;

    @Excel(name = "*售卖规格",orderNum = "5")
    @NotNull(message = "不能为空")
    private String specifications;

    @Excel(name = "错误信息",orderNum = "6")
    private String errorMsg;

    private Integer rowNum;

}
