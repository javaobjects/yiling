package com.yiling.export.imports.model;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 终端库存导入 Model
 *
 * @author: houjie.sun
 * @date: 2023/3/10
 */
@Data
public class ImportFlowGoodsBatchTerminalModel extends BaseImportModel {

    @ExcelShow
    @Excel(name = "*经销商编码/机构编码（请填写数据洞察系统中的编码）", orderNum = "1")
    private Long crmEnterpriseId;

    @ExcelShow
    @Excel(name = "*原始商品名称（系统根据对照关系转换）", orderNum = "2")
    private String gbName;

    @ExcelShow
    @Excel(name = "*原始商品规格（系统根据对照关系转换）", orderNum = "3")
    private String gbSpecifications;

    @ExcelShow
    @Excel(name = "*标准产品编码（原始商品信息和标准产品编码至少填写一个）", orderNum = "4")
    private Long crmGoodsCode;

    @ExcelShow
    @Excel(name = "批号", orderNum = "5")
    private String gbBatchNo;

    @ExcelShow
    @Excel(name = "*库存数量", orderNum = "6")
    private BigDecimal gbNumber;

    // 冗余字段

    /**
     * 所属月份
     */
    private String gbDetailMonth;

    /**
     * 经销商名称
     */
    private String name;

    /**
     * crm商品名称
     */
    private String crmGoodsName;

    /**
     * crm商品规格
     */
    private String crmGoodsSpecifications;

    /**
     * 库存类型：1-在途订单库存 2-终端库存
     */
    private Integer goodsBatchType;

    /**
     * 经销商三者关系ID
     */
    private Long enterpriseCersId;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 转换单位：1-乘 2-除
     */
    private Integer convertUnit;

    /**
     * 转换系数
     */
    private BigDecimal convertNumber;

}
