package com.yiling.dataflow.statistics;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/10/19
 */
@Data
public class FlowSaleSummaryExcel implements Serializable {
    private static final long serialVersionUID = -4672345329942342009L;

    @Excel(name = "销售日期", orderNum = "0")
    protected String     soTime;
    @Excel(name = "年份(实际)", orderNum = "1")
    protected String     year;
    @Excel(name = "月份(实际)", orderNum = "2")
    protected String     month;
    //---- 商业信息 -----
    @Excel(name = "商业编码", orderNum = "3")
    protected String     businessCode;
    @Excel(name = "商业名称", orderNum = "4")
    protected String     businessName;
    @Excel(name = "商务负责省区", orderNum = "5")
    protected String     businessProvince;
    @Excel(name = "商务负责人工号", orderNum = "6")
    protected String     commerceJobNumber;
    @Excel(name = "商务负责人姓名", orderNum = "7")
    protected String     commerceLiablePerson;
    @Excel(name = "流向打取人部门", orderNum = "8")
    protected String     flowObtainDepartment;
    @Excel(name = "流向打取人工号", orderNum = "9")
    protected String     flowJobNumber;
    @Excel(name = "流向打取人姓名", orderNum = "10")
    protected String     flowLiablePerson;
    @Excel(name = "协议级别", orderNum = "11")
    protected String     agreementLevel;
    @Excel(name = "商业属性", orderNum = "12")
    protected String     commercialAttributes;
    @Excel(name = "连锁协议类型", orderNum = "13")
    protected String     chainAgreementType;
    @Excel(name = "销售类型", orderNum = "14")
    protected String     salesType;
    @Excel(name = "商业省份", orderNum = "15")
    protected String     commercialProvince;
    @Excel(name = "商业地市", orderNum = "16")
    protected String     commercialCity;
    @Excel(name = "商业区县", orderNum = "17")
    protected String     commercialCounty;
    //---- 终端信息 -----
    @Excel(name = "原始机构名称", orderNum = "18")
    protected String     originalOrganizationName;
    @Excel(name = "机构编码", orderNum = "19")
    protected String     organizationCode;
    @Excel(name = "机构名称", orderNum = "20")
    protected String     organizationName;
    @Excel(name = "机构是否锁定", orderNum = "21")
    protected String     isLocked;

    @Excel(name = "机构部门", orderNum = "22")
    protected String     organizationDepartment;
    @Excel(name = "机构业务部门", orderNum = "23")
    protected String     organizationBusinessDepartment;
    @Excel(name = "机构省区", orderNum = "24")
    protected String     organizationProvince; //板块省区
    @Excel(name = "机构业务省区", orderNum = "25")
    protected String     organizationbusinessProvince;
    @Excel(name = "机构区办代码", orderNum = "26")
    protected String     organizationAreaCode;
    @Excel(name = "机构区办", orderNum = "27")
    protected String     organizationArea;
    @Excel(name = "主管工号", orderNum = "28")
    protected String     managerCode;
    @Excel(name = "主管姓名", orderNum = "29")
    protected String     managerName;
    @Excel(name = "代表工号", orderNum = "30")
    protected String     representativeCode;
    @Excel(name = "代表姓名", orderNum = "31")
    protected String     representativeName;
    @Excel(name = "岗位代码", orderNum = "32")
    protected String     postCode;
    @Excel(name = "岗位名称", orderNum = "33")
    protected String     postName;
    @Excel(name = "机构省份", orderNum = "34")
    protected String     institutionalProvince;
    @Excel(name = "城市代码", orderNum = "35")
    protected String     institutionalCityCode;
    @Excel(name = "机构地市", orderNum = "36")
    protected String     institutionalCity;
    @Excel(name = "区县代码", orderNum = "37")
    protected String     institutionalCountyCode;
    @Excel(name = "机构区县", orderNum = "38")
    protected String     institutionalCounty;
    @Excel(name = "终端类型", orderNum = "39")
    protected String     terminalType;
    @Excel(name = "终端类型二", orderNum = "40")
    protected String     terminalTypeTwo;
    @Excel(name = "机构级别", orderNum = "41")
    protected String     organizationLevel;
    @Excel(name = "机构国家级别", orderNum = "42")
    protected String     institutionalCountryLevel;
    @Excel(name = "机构是否连锁", orderNum = "43")
    protected String     isChain;
    @Excel(name = "机构连锁属性", orderNum = "44")
    protected String     chainAttribute;
    //------产品信息-------
    @Excel(name = "原始产品品名", orderNum = "45")
    protected String     originalProductName;
    @Excel(name = "原始产品品规", orderNum = "46")
    protected String     originalProductSpecification;
    @Excel(name = "品种", orderNum = "47")
    protected String     varieties;
    @Excel(name = "产品(SKU)编码", orderNum = "48")
    protected Long     crmGoodsCode;
    @Excel(name = "产品品名", orderNum = "49")
    protected String     crmGoodsName;
    @Excel(name = "产品品规", orderNum = "50")
    protected String     crmGoodsSpec;
    @Excel(name = "产品单价", orderNum = "51")
    protected BigDecimal price;
    @Excel(name = "原始数量", orderNum = "52")
    protected BigDecimal originalQuantity;
    @Excel(name = "原始单位", orderNum = "53")
    protected String     originalUnit;
    @Excel(name = "金额/元", orderNum = "54")
    protected BigDecimal amountPrice;
    @Excel(name = "批号", orderNum = "55")
    protected String     batchNo;
    @Excel(name = "原始单价", orderNum = "56")
    protected BigDecimal originalPrice;

}
