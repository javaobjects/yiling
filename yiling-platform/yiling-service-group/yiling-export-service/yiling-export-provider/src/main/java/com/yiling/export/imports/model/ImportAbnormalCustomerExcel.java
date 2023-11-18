package com.yiling.export.imports.model;

import com.yiling.export.excel.model.BaseImportModel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author shichen
 * @类名 ImportAbnormalCustomerExcel
 * @描述
 * @创建时间 2022/12/22
 * @修改人 shichen
 * @修改时间 2022/12/22
 **/
@Data
public class ImportAbnormalCustomerExcel extends BaseImportModel {
    @Excel(name = "*ID（不可修改）", orderNum = "0")
    private Long id;

    /**
     * 企业名称
     */
    @Excel(name = "*企业名称", orderNum = "1")
    private String name;

    /**
     * 营业执照号
     */
    @Excel(name = "*统一社会信用代码/医疗机构许可证", orderNum = "2")
    private String licenseNo;

    /**
     * 企业类型
     */
    @Excel(name = "*企业类型", orderNum = "3")
    private String customerType;

    /**
     * 联系人
     */
    @Excel(name = "*联系人", orderNum = "4")
    private String contact;

    /**
     * 区号-号码
     */
    @Excel(name = "*联系电话", orderNum = "5")
    private String phone;

    /**
     * 省份
     */
    @Excel(name = "*省", orderNum = "6")
    private String province;

    /**
     * 市
     */
    @Excel(name = "*市", orderNum = "7")
    private String city;

    /**
     * 区域
     */
    @Excel(name = "*区", orderNum = "8")
    private String region;

    /**
     * 详细地址
     */
    @Excel(name = "*详细地址", orderNum = "9")
    private String address;
}
