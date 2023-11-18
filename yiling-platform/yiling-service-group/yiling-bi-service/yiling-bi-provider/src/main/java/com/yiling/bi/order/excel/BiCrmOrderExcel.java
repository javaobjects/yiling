package com.yiling.bi.order.excel;

import java.math.BigDecimal;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2022/9/20
 */
@Data
public class BiCrmOrderExcel implements IExcelModel, IExcelDataModel {

    private static final long serialVersionUID = 1L;

    /**
     * 销售日期
     */
    @Excel(name = "销售日期", importFormat = "yyyy/MM/dd")
    private String soTime;

    /**
     * 销售年份
     */
    @Excel(name = "年份")
    private String soYear;

    /**
     * 销售月份
     */
    @Excel(name = "月份")
    private String soMonth;

    /**
     * 配送商业代码
     */
    @Excel(name = "配送商业代码")
    private String sellerEcode;

    /**
     * 配送商业id
     */
    @Excel(name = "商业ID")
    private String sellerEid;

    /**
     * 配送商业名称
     */
    @Excel(name = "配送商业名称")
    private String sellerEname;

    /**
     * 流向客户名称
     */
    @Excel(name = "流向客户名称")
    private String buyerEname;

    /**
     * 终端/商业省区
     */
    @Excel(name = "终端/商业省区")
    private String terminalArea;

    /**
     * 终端/商业部门
     */
    @Excel(name = "终端/商业部门")
    private String terminalDept;

    /**
     * 业务类型
     */
    @Excel(name = "业务类型")
    private String businessType;

    /**
     * 主管工号
     */
    @Excel(name = "主管工号")
    private String satrapNo;

    /**
     * 主管姓名
     */
    @Excel(name = "主管姓名")
    private String satrapName;

    /**
     * 代表工号
     */
    @Excel(name = "代表工号")
    private String representativeNo;

    /**
     * 代表姓名
     */
    @Excel(name = "代表姓名")
    private String representativeName;

    /**
     * 供应链角色
     */
    @Excel(name = "供应链角色")
    private String chainRole;

    /**
     * 省份
     */
    @Excel(name = "省份")
    private String province;

    /**
     * 城市
     */
    @Excel(name = "城市")
    private String city;

    /**
     * 区县
     */
    @Excel(name = "区县")
    private String county;

    /**
     * 品种
     */
    @Excel(name = "品种")
    private String variety;

    /**
     * 产品代码
     */
    @Excel(name = "产品代码")
    private String goodsId;

    /**
     * 产品名称
     */
    @Excel(name = "产品名称")
    private String goodsName;

    /**
     * 产品规格
     */
    @Excel(name = "产品规格")
    private String goodsSpec;

    /**
     * 产品单价
     */
    @Excel(name = "产品单价")
    private String goodsPrice;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private String quantity;

    /**
     * 商销量
     */
    @Excel(name = "商销量(购进)/元")
    private String salesVolume;

    /**
     * 规格
     */
    @Excel(name = "规格")
    private String spec;

    /**
     * 批号
     */
    @Excel(name = "批号")
    private String batchNo;

    /**
     * 流向类型
     */
    @Excel(name = "流向类型")
    private String flowType;

    /**
     * 当月非锁判断
     */
    @Excel(name = "当月非锁判断")
    private String lockFlag;

    /**
     * 判断备注
     */
    @Excel(name = "判断备注")
    private String remark;


    @Excel(name = "错误信息")
    private String errorMsg;

    private Integer rowNum;
}
