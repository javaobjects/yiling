package com.yiling.export.imports.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelShow;
import com.yiling.framework.common.util.Constants;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 导入修改企业管理员手机号 Model
 *
 * @author xuan.zhou
 * @date 2022/12/1
 */
@Data
public class ImportUpdateManagerAccountModel extends BaseImportModel {

    @NotNull
    @Min(1)
    @ExcelShow
    @Excel(name = "企业ID", orderNum = "10")
    private Long eid;

    /**
     * 用户ID
     */
    private Long userId;

    @NotEmpty
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^新手机号格式错误")
    @ExcelShow
    @Excel(name = "新手机号", orderNum = "40")
    private String newMobile;

    @NotEmpty
    @Length(min = 1, max = 10)
    @ExcelShow
    @Excel(name = "姓名", orderNum = "50")
    private String name;

}
