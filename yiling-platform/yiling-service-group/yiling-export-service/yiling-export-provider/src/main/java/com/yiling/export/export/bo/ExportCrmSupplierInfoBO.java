package com.yiling.export.export.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商业档案导出信息BO类
 */
@Data
public class ExportCrmSupplierInfoBO  implements java.io.Serializable{
    /**
     * 机构编码
     */
    private Long id;
    /**
     * crm系统对应客户编码
     */
    private String code;

    /**
     * crm系统对应客户名称
     */
    private String name;

    /**
     * 统一信用代码
     */
    private String licenseNumber;

    private String crmNumber;
    private String provinceName;

    private String cityName;

    private String regionName;

    private String address;

    private String province;

    /**
     * 供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role
     */
    private Integer supplyChainRole;
    /**
     * 药品经营许可证编号
     */
    private String distributionLicenseNumber;

    /**
     * 医机构执业许可证
     */
    private String institutionPracticeLicense;

    /**
     * 以岭编码
     */
    private String ylCode;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 备注
     */
    private String businessRemark;

    /**
     * 曾用名
     */
    private String formerName;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 电话
     */
    private String phone;

    /**
     * 传真
     */
    private String fax;

    /**
     * 邮编
     */
    private String postalCode;

    /**
     * 业务状态 1有效 2失效
     */
    private Integer businessCode;

    /**
     * 机构简称
     */
    private String shortName;
    /**
     * 主表主键
     */
    private Long crmEnterpriseId;

    /**
     * 商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    private Integer supplierLevel;

    /**
     * 商业属性 1-城市商业、2-县级商业
     */
    private Integer supplierAttribute;



    /**
     * 普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、5-不签协议
     */
    private Integer generalMedicineLevel;

    /**
     * 是否连锁总部 1是2否
     */
    private Integer headChainFlag;

    /**
     * 连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁
     */
    private Integer chainAttribute;

    /**
     * 连锁KA 1是2否
     */
    private Integer chainKaFlag;

    /**
     * 上级公司名称
     */
    private String parentSupplierName;

    /**
     * 上级公司编码
     */
    private String parentSupplierCode;

    /**
     * 商业销售类型 1-分销型、2-纯销型、3-综合型、4-托管型、5-连锁型
     */
    private Integer supplierSaleType;

    /**
     * 基药商信息 1-基药配送商、2-基药促销商
     */
    private Integer baseSupplierInfo;

    /**
     * 流向打取人业务部门
     */
    private String department;

    /**
     * 流向打取人业务省区
     */
    private String provincialArea;

    /**
     * 流向打取人工号
     */
    private String flowJobNumber;

    /**
     * 流向打取人姓名
     */
    private String flowLiablePerson;

    /**
     * 商务负责人工号
     */
    private String commerceJobNumber;

    /**
     * 商务负责人姓名
     */
    private String commerceLiablePerson;

    /**
     * 是否重点商业1是2否
     */
    private Integer supplierImportFlag;

    /**
     * 专利药协议类型1-全产品一级(六大专利药产品)、2-专利一级(六大专利药产品之一)、OTC直供(连花36粒)、3-全产品二级(六大专利药产品)、4-专利二级(六大专利药产品之一)、5-不签协议
     */
    private Integer patentAgreementType;

    /**
     * 是否捆绑普药 1是2否
     */
    private Integer bindGeneralMedicineFlag;

    /**
     * 普药销售模式 1-分销模式、2-招商模式、3-底价承包模式、3-KA连锁模式
     */
    private Integer generalMedicineSaleType;

    /**
     * 助记码
     */
    private String mnemonicCode;

    /**
     * 是否承包1是2否
     */
    private Integer contractFlag;

    /**
     * 是否有自己的下属连锁1是2否
     */
    private Integer chainSubordinateFlag;

    /**
     * 托管类型: 1-行业托管2-终端托管3-区域托管
     */
    private Integer trusteeshipType;

    /**
     * 商业体系分类: 1-全国体系2-省内体系3-其他体系
     */
    private Integer businessSystemCategory;
    /**
     * 商业体系
     */
    private Integer businessSystem;

    /**
     * 县域客户代码
     */
    private String countyCustomerCode;

    /**
     * 县域客户名称
     */
    private String countyCustomerName;

    /**
     * 收获地址
     */
    private String supplierAddress;
    //businessCodeEnum
    private String businessCodeEnum;
    //商业属性枚举值
    private String supplierAttributeEnum;
    //普药销售模式
    private String generalMedicineSaleTypeEnum;
    //连锁是否KA
    private String chainKaFlagEnum;
    //基药商信息
    private String baseSupplierInfoEnum;
    //商业销售类型
    private String supplierSaleTypeEnum;
    //专利药协议类型
    private String patentAgreementTypeEnum;
    //普药协议类型
    private String generalMedicineLevelEnum;
    //是否捆绑普药
    private String bindGeneralMedicineFlagEnum;
    private String contractFlagEnum;

    private String ae ;
    private String af ;
    private String ag ;
    private String ah ;
    private String ai ;
    private String aj ;
    private String ak ;
    private String al ;
    private String am ;
    private String an ;
    private String ao ;
    private String ap ;
    private String aq ;
    private String ar ;
    private String as ;
    private String at ;
    private String au ;
    private String av ;
    private String aw ;
    private String ax ;
    private String ay ;
    private String az ;
    private String ba ;
    private String bb ;
    private String bc ;
    private String bd ;
}
