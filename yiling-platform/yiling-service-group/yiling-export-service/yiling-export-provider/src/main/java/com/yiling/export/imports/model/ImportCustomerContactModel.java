package com.yiling.export.imports.model;

import javax.validation.constraints.NotNull;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 导入企业客户商务联系人 Form
 *
 * @author: lun.yu
 * @date: 2023-06-05
 */
@Data
public class ImportCustomerContactModel extends BaseImportModel {

    @ExcelRepet(groupName = "customerEid_contactUserId")
    @ExcelShow
    @Excel(name = "*渠道商ID", orderNum = "0")
    @NotNull(message = "不能为空")
    private Long customerEid;

    @Excel(name = "渠道商名称", orderNum = "1")
    private String customerName;

    @ExcelRepet(groupName = "customerEid_contactUserId")
    @ExcelShow
    @Excel(name = "*业务员ID", orderNum = "2")
    @NotNull(message = "不能为空")
    private Long contactUserId;

    @Excel(name = "业务员姓名", orderNum = "3")
    private String userName;

}
