package com.yiling.bi.resource.excel;

import java.math.BigDecimal;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * <p>
 * 零售部计算返利对应的备案表
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-09-22
 */
@Data
public class InputLsflCoverExcel implements IExcelModel, IExcelDataModel {

    private static final long serialVersionUID = -4672345329942342009L;


    /**
     * 省区
     */
    private String province;

    /**
     * 标准编码
     */
    private String bzCode;

    /**
     * 标准名称
     */
    private String bzName;

    /**
     * 客户类型
     */
    private String customerType;

    /**
     * 连锁总部
     */
    private String nkaZb;

    /**
     * 协议类型
     */
    private String xyType;

    /**
     * 签订类型
     */
    private String qdType;



    /**
     * 产品分类
     */
    private String wlType;

    /**
     * 品种
     */
    private String wlBreed;

    /**
     * 门店家数
     */
    private BigDecimal storesNum;

    /**
     * 一季度覆盖家数
     */
    private BigDecimal quarter1CoverNum;

    /**
     * 一季度覆盖率
     */
    private BigDecimal quarter1CoverRate;

    /**
     * 二季度覆盖家数
     */
    private BigDecimal quarter2CoverNum;

    /**
     * 二季度覆盖率
     */
    private BigDecimal quarter2CoverRate;

    /**
     * 三季度覆盖家数
     */
    private BigDecimal quarter3CoverNum;

    /**
     * 三季度覆盖率
     */
    private BigDecimal quarter3CoverRate;

    /**
     * 四季度覆盖家数
     */
    private BigDecimal quarter4CoverNum;

    /**
     * 四季度覆盖率
     */
    private BigDecimal quarter4CoverRate;

    @Excel(name = "错误信息")
    private String errorMsg;

    private Integer rowNum;

}
