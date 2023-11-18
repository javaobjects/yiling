package com.yiling.export.imports.model;

import javax.validation.constraints.NotEmpty;

import com.yiling.export.excel.model.BaseImportModel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author shichen
 * @类名 ImportErpShopMappingModel
 * @描述
 * @创建时间 2023/3/21
 * @修改人 shichen
 * @修改时间 2023/3/21
 **/
@Data
public class ImportErpShopMappingModel extends BaseImportModel {
    /**
     * 总店企业id
     */
    @Excel(name = "*总店企业ID", orderNum = "0")
    private Long mainShopEid;

    /**
     * 总店名称
     */
    @Excel(name = "总店企业名称", orderNum = "1")
    private String mainShopName;

    /**
     * 门店企业id
     */
    @Excel(name = "*门店企业ID", orderNum = "2")
    private Long shopEid;

    /**
     * 门店名称
     */
    @Excel(name = "门店企业名称", orderNum = "3")
    private String shopName;

    /**
     * 门店编码
     */
    @NotEmpty(message = "不能为空")
    @Excel(name = "*门店编码", orderNum = "4")
    private String shopCode;

    /**
     * 同步状态 0：关闭 1：开启
     */
    @Excel(name = "*同步状态（0，关闭；1，开启）", orderNum = "5")
    private Integer syncStatus;
}
