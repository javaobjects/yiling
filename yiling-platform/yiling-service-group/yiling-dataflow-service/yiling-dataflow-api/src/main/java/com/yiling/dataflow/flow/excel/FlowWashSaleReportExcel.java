package com.yiling.dataflow.flow.excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 月流向清洗，销售报表
 */
@Data
public class FlowWashSaleReportExcel implements Serializable {

    private static final long serialVersionUID = -1L;


    /**
     * 流向主表Id
     */
    @Excel(name = "ID",orderNum = "0")
    private Long flowSaleWashId;

    /**
     * 销售日期
     */
    @Excel(name = "销售日期",orderNum = "1",exportFormat = "yyyy-MM-dd HH:mm:ss",width= 20.0D)
    private Date soTime;

    /**
     * 月份(实际)
     */
    @Excel(name = "月份(实际)",orderNum = "2")
    private String soMonth;

    /**
     * 年份(实际)
     */
    @Excel(name = "年份(实际)",orderNum = "3")
    private String soYear;


    /**
     * 月份（计入）
     */
    @Excel(name = "月份（计入）",orderNum = "4")
    private String month;

    /**
     * 年份（计入）
     */
    @Excel(name = " 年份（计入）",orderNum = "5")
    private String year;

    /**
     * 商业编码
     */
    @Excel(name = "商业编码",orderNum = "6")
    private Long crmId;

    /**
     * 商业名称（商家名称）
     */
    @Excel(name = "商业名称",orderNum = "7",width= 30.0D)
    private String ename;

    /**
     * 商务负责人工号
     */
    @Excel(name = "商务负责人工号",orderNum = "8")
    private String commerceJobNumber;

    /**
     * 商务负责人
     */
    @Excel(name = "商务负责人",orderNum = "9")
    private String commerceLiablePerson;

    /**
     * 流向打取人部门
     */
    @Excel(name = "流向打取人部门",orderNum = "10")
    private String flowDepartment;

    /**
     * 流向打取人工号
     */
    @Excel(name = "流向打取人工号",orderNum = "11")
    private String flowJobNumber;

    /**
     * 流向打取人姓名
     */
    @Excel(name = "流向打取人姓名",orderNum = "12")
    private String flowLiablePerson;

    /**
     * 商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    @Excel(name = "商业级别",orderNum = "13")
    private String supplierLevelStr;

    /**
     * 商业属性 1-城市商业、2-县级商业
     */
    @Excel(name = "商业属性",orderNum = "14")
    private String supplierAttributeStr;

    /**
     * 普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议
     */
    @Excel(name = "普药级别",orderNum = "15",width= 25.0D)
    private String generalMedicineLevelStr;

    /**
     * 商业销售类型 1-分销型、2-纯销型、3-综合型、4-托管型、5-连锁型
     */
    @Excel(name = "商业销售类型",orderNum = "16")
    private String supplierSaleTypeStr;

    /**
     * 基药商信息 1-基药配送商、2-基药促销商
     */
    @Excel(name = "基药商信息",orderNum = "17")
    private String baseSupplierInfoStr;

    /**
     * 是否连锁总部 1是 2否
     */
    @Excel(name = "是否连锁总部",orderNum = "18")
    private String headChainFlagStr;

    /**
     * 连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁
     */
    @Excel(name = "连锁属性",orderNum = "19")
    private String chainAttributeStr;

    /**
     * 连锁KA
     */
    @Excel(name = "连锁是否KA",orderNum = "20")
    private String chainKaFlagStr;

    /**
     * 商业省份
     */
    @Excel(name = "商业省份",orderNum = "21")
    private String supplierProvinceName;

    /**
     * 商业地市
     */
    @Excel(name = "商业地市",orderNum = "22")
    private String supplierCityName;

    /**
     * 商业区县
     */
    @Excel(name = "商业区县",orderNum = "23")
    private String supplierRegionName;

    /**
     * 原始客户名称
     */
    @Excel(name = "原始客户名称",orderNum = "24",width= 25.0D)
    private String originalEnterpriseName;

    /**
     * 机构编码
     */
    @Excel(name = "机构编码",orderNum = "25",width= 25.0D)
    private Long customerCrmId;

    /**
     * 机构名称
     */
    @Excel(name = "机构名称",orderNum = "26",width= 25.0D)
    private String enterpriseName;

    /**
     * 机构是否锁定  1是 2否
     */
    @Excel(name = "机构是否锁定",orderNum = "27")
    private String isChainFlagStr;

    /**
     * 机构部门
     */
    @Excel(name = "机构部门",orderNum = "28",width= 20.0D)
    private String department;

    /**
     * 机构业务部门
     */
    @Excel(name = "机构业务部门",orderNum = "29",width= 25.0D)
    private String businessDepartment;

    /**
     * 机构省区
     */
    @Excel(name = "机构省区",orderNum = "30")
    private String provincialArea;
    /**
     * 省区经理工号
     */
    @Excel(name = "省区经理工号",orderNum = "31")
    private String provinceManagerCode;


    /**
     * 省区经理姓名
     */
    @Excel(name = "省区经理姓名",orderNum = "32")
    private String provinceManagerName;


    /**
     * 机构业务省区
     */
    @Excel(name = "机构业务省区",orderNum = "33",width= 25.0D)
    private String businessProvince;

    /**
     * 机构区办代码
     */
    @Excel(name = "机构区办代码",orderNum = "34")
    private String districtCountyCode;

    /**
     * 机构区办
     */
    @Excel(name = "机构区办",orderNum = "35")
    private String districtCounty;

    /**
     * 主管工号
     */
    @Excel(name = "主管工号",orderNum = "36")
    private String superiorSupervisorCode;

    /**
     * 主管名称
     */
    @Excel(name = "主管名称",orderNum = "37")
    private String superiorSupervisorName;

    /**
     * 代表工号
     */
    @Excel(name = "代表工号",orderNum = "38")
    private String representativeCode;

    /**
     * 代表姓名
     */
    @Excel(name = "代表姓名",orderNum = "39",width= 25.0D)
    private String representativeName;

    /**
     * 岗位代码
     */
    @Excel(name = "岗位代码",orderNum = "40")
    private Long postCode;

    /**
     * 岗位名称
     */
    @Excel(name = "岗位名称",orderNum = "41",width= 25.0D)
    private String postName;

    /**
     * 省份代码
     */
    @Excel(name = "省份代码",orderNum = "42")
    private String provinceCode;

    /**
     * 机构省份
     */
    @Excel(name = "机构省份",orderNum = "43",width= 25.0D)
    private String provinceName;

    /**
     * 城市代码
     */
    @Excel(name = "城市代码",orderNum = "44")
    private String cityCode;

    /**
     * 机构城市
     */
    @Excel(name = "机构城市",orderNum = "45")
    private String cityName;

    /**
     * 区县代码
     */
    @Excel(name = "区县代码",orderNum = "46")
    private String regionCode;

    /**
     * 机构区县
     */
    @Excel(name = "机构区县",orderNum = "47")
    private String regionName;

    /**
     * 客户商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    @Excel(name = "客户商业级别",orderNum = "48",width= 25.0D)
    private String customerSupplierLevelStr;

    /**
     * 客户普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议
     */
    @Excel(name = "客户普药级别",orderNum = "49",width= 25.0D)
    private String customerGeneralMedicineLevelStr;

    /**
     * 客户商业属性 1-城市商业、2-县级商业
     */
    @Excel(name = "客户商业属性",orderNum = "50")
    private String customerSupplierAttributeStr;

    /**
     * 是否连锁总部 1是 2否
     */
    @Excel(name = "是否连锁总部",orderNum = "51")
    private String customerHeadChainFlagStr;

    /**
     * 连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁
     */
    @Excel(name = "连锁属性",orderNum = "52")
    private String customerChainAttributeStr;

    /**
     * (客户)连锁KA 1是 2否
     */
    @Excel(name = "连锁KA",orderNum = "53")
    private String customerChainKaFlagStr;

    /**
     * 客户上级公司名称
     */
    @Excel(name = "客户上级公司名称",orderNum = "54")
    private String customerParentSupplierName;

    /**
     * 客户上级公司编码
     */
    @Excel(name = "客户上级公司编码",orderNum = "55")
    private String customerParentSupplierCode;

    /**
     * 客户商业销售类型 1-分销型、2-纯销型、3-综合型、4-托管型、5-连锁型
     */
    @Excel(name = "客户商业销售类型",orderNum = "56")
    private String customerSupplierSaleTypeStr;

    /**
     * 客户基药商信息 1-基药配送商、2-基药促销商
     */
    @Excel(name = "客户基药商信息",orderNum = "57")
    private String customerBaseSupplierInfoStr;

    /**
     * 客户供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role
     */
    @Excel(name = "客户供应链角色",orderNum = "58")
    private String customerSupplyChainRoleStr;

    /**
     * 医院性质 1公立 2私立 3厂办
     */
    @Excel(name = "医院性质",orderNum = "59")
    private String customerMedicalNatureStr;

    /**
     * 医院类型 1-综合医院、2专科医院、3部队医院、4儿童医院、5县级医院、6厂矿职工医院、7中医医院、8社区卫生服务站、9乡镇卫生院、10村卫生室、11妇保院、12疾控中心、13私人终端、14社区卫生服务中心、15高校医院、16其他
     */
    @Excel(name = "医院类型",orderNum = "60",width= 25.0D)
    private String customerMedicalTypeStr;

    /**
     * 以岭级别
     */
    @Excel(name = "以岭级别",orderNum = "61")
    private String customerYlLevelStr;

    /**
     * 国家等级 1三级甲等、2三级乙等、3三级丙等、4二级甲等、5二级乙等、6二级丙等、7一级甲等、8三级特等、9一级乙等、10一级丙等、11其他
     */
    @Excel(name = "国家等级",orderNum = "62",width= 25.0D)
    private String customerNationalGradeStr;

    /**
     * 是否基药终端 1是 2否
     */
    @Excel(name = "是否基药终端",orderNum = "63")
    private String baseMedicineFlagStr;

    /**
     * 药店属性 1-连锁分店；2-单体药店
     */
    @Excel(name = "药店属性",orderNum = "64")
    private Integer pharmacyAttribute;

    /**
     * 药店类型 1-直营；2-加盟
     */
    @Excel(name = "药店类型",orderNum = "65")
    private String pharmacyTypeStr;

    /**
     * 药店级别 1-A级；2-B级；3-C级
     */
    @Excel(name = "药店级别",orderNum = "66")
    private String pharmacyLevelStr;

    /**
     * 连锁属性
     */
    @Excel(name = "连锁属性",orderNum = "67")
    private String pharmacyChainAttributeStr;

    /**
     * 标签属性 1-社区店；2-商圈店；3-院边店；4-电商店
     */
    @Excel(name = "标签属性",orderNum = "68")
    private String labelAttributeStr;

    /**
     * 是否目标 1-是；2-否
     */
    @Excel(name = "是否目标",orderNum = "69")
    private String targetFlagStr;

    /**
     * 原始商品名称
     */
    @Excel(name = "原始商品名称",orderNum = "70",width= 25.0D)
    private String soGoodsName;

    /**
     * 原始商品规格
     */
    @Excel(name = "原始商品规格",orderNum = "71",width= 25.0D)
    private String soSpecifications;

    /**
     * 品种类型
     */
    @Excel(name = "品种类型",orderNum = "72")
    private String varietyType;

    /**
     * 产品(sku)编码
     */
    @Excel(name = "产品(sku)编码",orderNum = "73")
    private Long goodsCode;

    /**
     * 产品品名
     */
    @Excel(name = "产品品名",orderNum = "74",width= 25.0D)
    private String goodsName;

    /**
     * 产品品规
     */
    @Excel(name = "产品品规",orderNum = "75",width= 25.0D)
    private String goodsSpec;

    /**
     * 原始数量
     */
    @Excel(name = "原始数量",orderNum = "76")
    private BigDecimal soQuantity;

    /**
     * 原始单位
     */
    @Excel(name = "原始单位",orderNum = "77")
    private String soUnit;

    /**
     * 原始产品价格
     */
    @Excel(name = "原始产品价格",orderNum = "78" ,numFormat = "#.###")
    private BigDecimal soPrice;

    /**
     * 产品单价
     */
    @Excel(name = "产品单价",orderNum = "79",numFormat = "#.###")
    private BigDecimal salesPrice;

    /**
     * 最终数量
     */
    @Excel(name = "最终数量",orderNum = "80",numFormat = "#.###")
    private BigDecimal finalQuantity;

    /**
     * 金额
     */
    @Excel(name = "金额",orderNum = "81",numFormat = "#.###")
    private BigDecimal soTotalAmount;

    /**
     * 批号
     */
    @Excel(name = "批号",orderNum = "82")
    private String soBatchNo;

    /**
     * 流向分类 0-正常 1-销量申诉 2-流向补传
     */
    @Excel(name = "流向分类",orderNum = "83")
    private String flowClassifyStr;

    /**
     * 收集方式 0-excel导入 1-工具 2-以岭接口 3-第三方接口 4-ftp
     */
    @Excel(name = "收集方式",orderNum = "84")
    private String collectionMethodStr;

    /**
     * 流向清理时间
     */
    @Excel(name = "流向清理时间",orderNum = "85",exportFormat = "yyyy-MM-dd HH:mm:ss",width= 25.0D)
    private Date washTime;

    /**
     * 文件名称
     */
    @Excel(name = "文件名称",orderNum = "86",width= 40.0D)
    private String fileName;

    /**
     * 销量申诉类型
     */
    @Excel(name = "销量申诉类型",orderNum = "87",width= 20.0D)
    private String complainTypeStr;

    /**
     * 机构首次锁定时间
     */
    @Excel(name = "机构首次锁定时间",orderNum = "88",width= 20.0D)
    private Date lockTime;

    /**
     * 机构最后一次解锁时间
     */
    @Excel(name = "机构最后一次解锁时间",orderNum = "89",width= 20.0D)
    private Date lastUnLockTime;
}
