package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 导入开通会员 Form
 *
 * @author: lun.yu
 * @date: 2022-09-30
 */
@Data
public class ImportOpenMemberModel extends BaseImportModel {

    @ExcelRepet(groupName = "eid_memberId")
    @ExcelShow
    @Excel(name = "*终端ID", orderNum = "0")
    @NotNull(message = "不能为空")
    private Long eid;

    @Excel(name = "终端名称", orderNum = "1")
    private String ename;

    @ExcelRepet(groupName = "eid_memberId")
    @ExcelShow
    @Excel(name = "*会员ID", orderNum = "2")
    @NotNull(message = "不能为空")
    private Long memberId;

    @Excel(name = "会员名称", orderNum = "3")
    private String memberName;

    @Excel(name = "*购买规则", orderNum = "4")
    @NotEmpty(message = "不能为空")
    private String buyRule;

    @ExcelShow
    @Excel(name = "推广方ID", orderNum = "5")
    private Long promoterId;

    @Excel(name = "推广方名称", orderNum = "6")
    private String promoterName;

    @ExcelShow
    @Excel(name = "推广人ID", orderNum = "7")
    private Long promoterUserId;

    @Excel(name = "推广人名称", orderNum = "8")
    private String promoterUserName;

    @Excel(name = "导入类型", orderNum = "9")
    @NotEmpty(message = "不能为空")
    private String sourceName;

}
