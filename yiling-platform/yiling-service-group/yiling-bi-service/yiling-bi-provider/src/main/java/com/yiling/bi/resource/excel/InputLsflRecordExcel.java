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
public class InputLsflRecordExcel implements IExcelModel, IExcelDataModel {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * 省份
     */
    @Excel(name = "省区")
    private String province;

    /**
     * 标准编码
     */
    @Excel(name = "连锁编码")
    private String bzCode;

    /**
     * 标准名称
     */
    @Excel(name = "连锁名称")
    private String bzName;

    /**
     * 客户类型-新
     */
    @Excel(name = "客户类型")
    private String customerType;

    /**
     * NKA总部
     */
    @Excel(name = "NKA总部")
    private String nkaZb;

    /**
     * 签订类型
     */
    @Excel(name = "签订类型")
    private String qdType;

    /**
     * 协议类型
     */
    @Excel(name = "协议类别")
    private String xyType;

    /**
     * 产品分类
     */
    @Excel(name = "产品分类")
    private String wlType;

    /**
     * 品种
     */
    @Excel(name = "品种")
    private String wlBreed;

    /**
     * 产品编码
     */
    @Excel(name = "产品编码")
    private String wlCode;

    /**
     * 产品名称
     */
    @Excel(name = "产品名称")
    private String wlName;

    /**
     * 核算价
     */
    @Excel(name = "核算价")
    private BigDecimal accountPrice;

    /**
     * 基础费-小计
     */
    @Excel(name = "基础费-小计",type = 10,numFormat="0.00")
    private BigDecimal basicNum;

    /**
     * 项目费-覆盖
     */
    @Excel(name = "项目费-覆盖",type = 10,numFormat="0.00")
    private BigDecimal projectFg;

    /**
     * 项目费-其他
     */
    @Excel(name = "项目费-其他",type = 10,numFormat="0.00")
    private BigDecimal projectOther;

    /**
     * 目标达成奖励（元/盒）
     */
    @Excel(name = "目标达成奖励（元/盒）",type = 10,numFormat="0.00")
    private BigDecimal targetNum;

    /**
     * 2022年规划目标
     */
    @Excel(name = "2022年规划目标",type = 10,numFormat="0.00")
    private BigDecimal yearTarget;

    /**
     * 1月份
     */
    @Excel(name = "1月份",type = 10,numFormat="0.00")
    private BigDecimal month1Num;

    /**
     * 2月份
     */
    @Excel(name = "2月份",type = 10,numFormat="0.00")
    private BigDecimal month2Num;

    /**
     * 3月份
     */
    @Excel(name = "3月份",type = 10,numFormat="0.00")
    private BigDecimal month3Num;

    /**
     * 4月份
     */
    @Excel(name = "4月份",type = 10,numFormat="0.00")
    private BigDecimal month4Num;

    /**
     * 5月份
     */
    @Excel(name = "5月份",type = 10,numFormat="0.00")
    private BigDecimal month5Num;

    /**
     * 6月份
     */
    @Excel(name = "6月份",type = 10,numFormat="0.00")
    private BigDecimal month6Num;

    /**
     * 7月份
     */
    @Excel(name = "7月份",type = 10,numFormat="0.00")
    private BigDecimal month7Num;

    /**
     * 8月份
     */
    @Excel(name = "8月份",type = 10,numFormat="0.00")
    private BigDecimal month8Num;

    /**
     * 9月份
     */
    @Excel(name = "9月份",type = 10,numFormat="0.00")
    private BigDecimal month9Num;

    /**
     * 10月份
     */
    @Excel(name = "10月份",type = 10,numFormat="0.00")
    private BigDecimal month10Num;

    /**
     * 11月份
     */
    @Excel(name = "11月份",type = 10,numFormat="0.00")
    private BigDecimal month11Num;

    /**
     * 12月份
     */
    @Excel(name = "12月份",type = 10,numFormat="0.00")
    private BigDecimal month12Num;

    @Excel(name = "错误信息")
    private String errorMsg;

    private Integer rowNum;

}
