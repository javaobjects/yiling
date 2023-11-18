package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 零售机构档案excel导出模板
 *
 * @author: yong.zhang
 * @date: 2023/2/17 0017
 */
@Data
public class ExportCrmPharmacyBO {

    /**
     * 客户代码
     */
    private String code;
    /**
     * CRM编码
     */
    private String crmCode;
    /**
     * 以岭编码
     */
    private String ylCode;
    /**
     * 客户名称
     */
    private String name;
    /**
     * 状态名称
     */
    private String businessCode;
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 区县名称
     */
    private String regionName;
    /**
     * 地址
     */
    private String address;
    /**
     * 邮编
     */
    private String postalCode;
    /**
     * 办公电话
     */
    private String phone;
    /**
     * 传真
     */
    private String fax;
    /**
     * 标签属性
     */
    private String labelAttribute;
    /**
     * 店经理
     */
    private String storeManager;
    /**
     * 药店营业额
     */
    private BigDecimal turnover;
    /**
     * 以岭年销售额
     */
    private BigDecimal ylAnnualSales;
    /**
     * 营业面积
     */
    private BigDecimal businessArea;
    /**
     * 是否协议
     */
    private String agreement;
    /**
     * 客户属性
     */
    private String pharmacyAttribute;
    /**
     * 药店类型 1-直营；2-加盟
     */
    private String pharmacyType;
    /**
     * 药店级别
     */
    private String pharmacyLevel;
    /**
     * 上级公司编码
     */
    private String parentCompanyCode;
    /**
     * 上级公司名称
     */
    private String parentCompanyName;
    /**
     * 县域客户编码
     */
    private String countyCustomerCode;
    /**
     * 县域客户名称
     */
    private String countyCustomerName;
    /**
     * 是否平价
     */
    private String parity;
    /**
     * 是否医保
     */
    private String medicalInsurance;
    /**
     * 是否基药终端
     */
    private String baseMedicineFlag;
    /**
     * 助记码
     */
    private String mnemonicCode;
    /**
     * 是否承包
     */
    private String contractFlag;
    /**
     * 流向打取人工号
     */
    private String flowJobNumber;
    /**
     * 流向打取人姓名
     */
    private String flowLiablePerson;
    /**
     * 驻商专员工号
     */
    private String residentCommissionerCode;
    /**
     * 驻商专员姓名
     */
    private String residentCommissionerName;

    /**
     * 零售部标准产品组 -- 是否目标
     */
    private String ag;
    /**
     * 零售部标准产品组 -- 业务区域
     */
    private String ah;
    /**
     * 零售部标准产品组 -- 业务代表编码
     */
    private String ai;
    /**
     * 零售部标准产品组 -- 业务代表姓名
     */
    private String aj;
    /**
     * 零售部标准产品组 -- 承包类型
     */
    private String ak;
    /**
     * 零售部标准产品组(不含双花) -- 是否目标
     */
    private String al;
    /**
     * 零售部标准产品组(不含双花) -- 业务区域
     */
    private String am;
    /**
     * 零售部标准产品组(不含双花) -- 业务代表编码
     */
    private String an;
    /**
     * 零售部标准产品组(不含双花) -- 业务代表姓名
     */
    private String ao;
    /**
     * 零售部标准产品组(不含双花) -- 承包类型
     */
    private String ap;
    /**
     * 零售部连花颗粒产品组 -- 是否目标
     */
    private String aq;
    /**
     * 零售部连花颗粒产品组 -- 业务区域
     */
    private String ar;
    /**
     * 零售部连花颗粒产品组 -- 业务代表编码
     */
    private String as;
    /**
     * 零售部连花颗粒产品组 -- 业务代表姓名
     */
    private String at;
    /**
     * 零售部连花颗粒产品组 -- 承包类型
     */
    private String au;
    /**
     * 零售部连花清咳产品组 -- 是否目标
     */
    private String av;
    /**
     * 零售部连花清咳产品组 -- 业务区域
     */
    private String aw;
    /**
     * 零售部连花清咳产品组 -- 业务代表编码
     */
    private String ax;
    /**
     * 零售部连花清咳产品组 -- 业务代表姓名
     */
    private String ay;
    /**
     * 零售部连花清咳产品组 -- 承包类型
     */
    private String az;
    /**
     * 数字化药店终端产品组 -- 是否目标
     */
    private String ba;
    /**
     * 数字化药店终端产品组 -- 业务区域
     */
    private String bb;
    /**
     * 数字化药店终端产品组 -- 业务代表编码
     */
    private String bc;
    /**
     * 数字化药店终端产品组 -- 业务代表姓名
     */
    private String bd;
    /**
     * 数字化药店终端产品组 -- 承包类型
     */
    private String be;
}
