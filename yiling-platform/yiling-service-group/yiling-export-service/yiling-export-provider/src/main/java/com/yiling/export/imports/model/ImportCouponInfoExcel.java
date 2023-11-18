package com.yiling.export.imports.model;

import javax.validation.constraints.NotNull;

import com.yiling.export.excel.model.BaseImportModel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/5/19
 */
@Data
public class ImportCouponInfoExcel extends BaseImportModel {

    @Excel(name = "*优惠券ID", orderNum = "0")
    @NotNull(message = "不能为空")
    private Long couponActivityId;

    @Excel(name = "*企业ID", orderNum = "1")
    @NotNull(message = "不能为空")
    private Long eid;

    @Excel(name = "*企业名称", orderNum = "2")
    private String name;

    @Excel(name = "*发放数量", orderNum = "3")
    @NotNull(message = "不能为空")
    private Integer num;
}
