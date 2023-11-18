package com.yiling.export.imports.model;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelShow;
import com.yiling.framework.common.util.Constants;
import com.yiling.user.enterprise.enums.EmployeeTypeEnum;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 导入员工账号 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/27
 */
@Data
public class ImportStaffModel extends BaseImportModel {

    @NotNull
    @ExcelShow
    @Excel(name = "*企业ID", orderNum = "10")
    private Long eid;

    @NotEmpty
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "手机号格式不正确")
    @ExcelShow
    @Excel(name = "*手机号", orderNum = "20")
    private String mobile;

    @NotEmpty
    @ExcelShow
    @Excel(name = "*姓名", orderNum = "30")
    private String name;

    @ExcelShow
    @Excel(name = "用户类型", orderNum = "40")
    private String employeeTypeStr;

    private EmployeeTypeEnum employeeTypeEnum;

    @NotNull
    @ExcelShow
    @Excel(name = "*角色ID", orderNum = "50")
    private Long roleId;

    @NotNull
    @ExcelShow
    @Excel(name = "*部门ID（多个采用英文逗号分隔）", orderNum = "60")
    private String departmentIdsStr;

    /**
     * 部门ID列表
     */
    private List<Long> departmentIds;

    @ExcelShow
    @Excel(name = "职位ID", orderNum = "70")
    private Long positionId;

    @ExcelShow
    @Excel(name = "员工工号", orderNum = "80")
    private String code;

    @ExcelShow
    @Excel(name = "上下级关系（请填写省区经理工号）", orderNum = "90")
    private String parentCode;

    /**
     * 上级领导ID
     */
    private Long parentId;

}
