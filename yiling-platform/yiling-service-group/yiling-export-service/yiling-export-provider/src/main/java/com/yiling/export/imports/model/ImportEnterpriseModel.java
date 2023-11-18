package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelRepet;
import com.yiling.framework.common.annotations.ExcelShow;
import com.yiling.framework.common.util.Constants;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 导入企业 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Data
public class ImportEnterpriseModel extends BaseImportModel {

    @NotEmpty
    @Length(max = 100)
    @ExcelShow
    @Excel(name = "*企业名称", orderNum = "0")
    private String name;

    @NotEmpty
    @ExcelShow
    @Excel(name = "*企业类型", orderNum = "1")
    private String typeName;

    @ExcelShow
    @Excel(name = "所属总店ID", orderNum = "2")
    private Long parentId;

    @NotEmpty
    @Length(max = 50)
    @ExcelRepet
    @ExcelShow
    @Excel(name = "*社会统一信用代码/医疗机构执业许可证", orderNum = "3")
    private String licenseNumber;

    @NotEmpty
    @Length(max = 50)
    @ExcelShow
    @Excel(name = "*所属省份", orderNum = "4")
    private String provinceName;

    @NotEmpty
    @Length(max = 50)
    @ExcelShow
    @Excel(name = "*所属城市", orderNum = "5")
    private String cityName;

    @NotEmpty
    @Length(max = 50)
    @ExcelShow
    @Excel(name = "*所属区县", orderNum = "6")
    private String regionName;

    @NotEmpty
    @Length(max = 100)
    @ExcelShow
    @Excel(name = "*详细地址", orderNum = "7")
    private String address;

    @NotEmpty
    @Length(max = 10)
    @ExcelShow
    @Excel(name = "*企业联系人姓名", orderNum = "8")
    private String contactor;

    @NotEmpty
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "企业联系人电话格式不正确")
    @ExcelShow
    @Excel(name = "*企业联系人电话（该电话将绑定企业管理员账号，后期可换绑。）", orderNum = "9")
    private String contactorPhone;

    @NotEmpty
    @ExcelShow
    @Excel(name = "*开通产品线", orderNum = "10")
    private String products;

    @ExcelShow
    @Excel(name = "渠道类型", orderNum = "11")
    private String channelName;

    @ExcelShow
    @Excel(name = "药+险业务类型", orderNum = "12")
    private String hmcType;

}
