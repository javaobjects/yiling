package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author shichen
 * @类名 ImportFlowPurchaseChannelModel
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Data
public class ImportFlowPurchaseChannelModel extends BaseImportModel {

    /**
     * 机构编码
     */
    @ExcelShow
    @Excel(name = "*机构编码", orderNum = "0")
    private Long crmOrgId;

    /**
     * 机构名称
     */
    @NotEmpty(message = "不能为空")
    @ExcelShow
    @Excel(name = "*机构名称", orderNum = "1")
    private String orgName;

    /**
     * 省份
     */
    @Excel(name = "省", orderNum = "2")
    private String province;

    /**
     * 省份代码
     */
    private String provinceCode;

    /**
     * 市
     */
    @Excel(name = "市", orderNum = "3")
    private String city;

    /**
     * 市代码
     */
    private String cityCode;

    /**
     * 区
     */
    @Excel(name = "区", orderNum = "4")
    private String region;

    /**
     * 区代码
     */
    private String regionCode;

    /**
     * 采购渠道机构编码
     */
    @ExcelShow
    @Excel(name = "*采购渠道机构编码", orderNum = "5")
    private Long crmPurchaseOrgId;

    /**
     * 采购渠道机构名称
     */
    @NotEmpty(message = "不能为空")
    @ExcelShow
    @Excel(name = "*采购渠道机构名称", orderNum = "6")
    private String purchaseOrgName;

}
