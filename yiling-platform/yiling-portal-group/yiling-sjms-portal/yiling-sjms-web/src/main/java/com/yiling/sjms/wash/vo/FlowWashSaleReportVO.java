package com.yiling.sjms.wash.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhigang.guo
 * @date: 2023/3/2
 */
@Data
public class FlowWashSaleReportVO extends BaseVO {

    /**
     * 流向主表Id
     */
    @ApiModelProperty(value = "ID")
    private String flowSaleWashId;

    /**
     * 销售日期
     */
    @ApiModelProperty(value = "销售日期")
    private Date soTime;

    /**
     * 月份(实际)
     */
    @ApiModelProperty(value = "月份(实际)")
    private String soMonth;

    /**
     * 年份(实际)
     */
    @ApiModelProperty(value = "年份(实际)")
    private String soYear;


    /**
     * 月份（计入）
     */
    @ApiModelProperty(value = "月份（计入）")
    private String month;

    /**
     * 年份（计入）
     */
    @ApiModelProperty(value = "年份（计入）")
    private String year;


    /**
     * 商业编码
     */
    @ApiModelProperty(value = "商业编码")
    private Long crmId;

    /**
     * 商业名称（商家名称）
     */
    @ApiModelProperty(value = "商业名称")
    private String ename;

    /**
     * 商务负责人工号
     */
    @ApiModelProperty(value = "商务负责人工号")
    private String commerceJobNumber;

    /**
     * 商务负责人
     */
    @ApiModelProperty(value = "商务负责人姓名")
    private String commerceLiablePerson;

    /**
     * 流向打取人部门
     */
    @ApiModelProperty(value = "流向打取人部门")
    private String flowDepartment;

    /**
     * 流向打取人工号
     */
    @ApiModelProperty(value = "流向打取人工号")
    private String flowJobNumber;

    /**
     * 流向打取人姓名
     */
    @ApiModelProperty(value = "流向打取人姓名")
    private String flowLiablePerson;

    /**
     * 商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    @ApiModelProperty(value = "商业级别(取数据字典)")
    private Integer supplierLevel;

    /**
     * 商业属性 1-城市商业、2-县级商业
     */
    @ApiModelProperty(value = "商业属性 (取数据字典)")
    private Integer supplierAttribute;

    /**
     * 普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议
     */
    @ApiModelProperty(value = "普药级别 (取数据字典)")
    private Integer generalMedicineLevel;

    /**
     * 商业销售类型 1-分销型、2-纯销型、3-综合型、4-托管型、5-连锁型
     */
    @ApiModelProperty(value = "商业销售类型 (取数据字典)")
    private Integer supplierSaleType;

    /**
     * 基药商信息 1-基药配送商、2-基药促销商
     */
    @ApiModelProperty(value = "基药商信息 (取数据字典)")
    private Integer baseSupplierInfo;

    /**
     * 是否连锁总部 1是 2否
     */
    @ApiModelProperty(value = "是否连锁总部 (取数据字典)")
    private Integer headChainFlag;

    /**
     * 连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁
     */
    @ApiModelProperty(value = "连锁属性 (取数据字典)")
    private Integer chainAttribute;

    /**
     * 连锁KA
     */
    @ApiModelProperty(value = "连锁ka (取数据字典)")
    private Integer chainKaFlag;

    /**
     * 商业省份
     */
    @ApiModelProperty(value = "商业省份")
    private String supplierProvinceName;

    /**
     * 商业地市
     */
    @ApiModelProperty(value = "商业地市")
    private String supplierCityName;

    /**
     * 商业区县
     */
    @ApiModelProperty(value = "商业区县")
    private String supplierRegionName;

    /**
     * 原始客户名称
     */
    @ApiModelProperty(value = "原始客户名称")
    private String originalEnterpriseName;

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码")
    private Long customerCrmId;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String enterpriseName;

    /**
     * 机构是否锁定  1是 2否
     */
    @ApiModelProperty(value = "机构是否锁定 (取数据字典)")
    private Integer isChainFlag;

    /**
     * 机构部门
     */
    @ApiModelProperty(value = "机构部门")
    private String department;

    /**
     * 机构业务部门
     */
    @ApiModelProperty(value = "机构业务部门")
    private String businessDepartment;

    /**
     * 机构省区
     */
    @ApiModelProperty(value = "机构省区")
    private String provincialArea;

    /**
     * 机构业务省区
     */
    @ApiModelProperty(value = "机构业务省区")
    private String businessProvince;

    /**
     * 机构区办代码
     */
    @ApiModelProperty(value = "机构区办代码")
    private String districtCountyCode;

    /**
     * 机构区办
     */
    @ApiModelProperty(value = "机构区办")
    private String districtCounty;

    /**
     * 主管工号
     */
    @ApiModelProperty(value = "主管工号")
    private String superiorSupervisorCode;

    /**
     * 主管名称
     */
    @ApiModelProperty(value = "主管名称")
    private String superiorSupervisorName;

    /**
     * 代表工号
     */
    @ApiModelProperty(value = "代表工号")
    private String representativeCode;

    /**
     * 代表姓名
     */
    @ApiModelProperty(value = "代表姓名")
    private String representativeName;

    /**
     * 岗位代码
     */
    @ApiModelProperty(value = "岗位代码")
    private Long postCode;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postName;

    /**
     * 省份代码
     */
    @ApiModelProperty(value = "省份代码")
    private String provinceCode;

    /**
     * 机构省份
     */
    @ApiModelProperty(value = "机构省份")
    private String provinceName;

    /**
     * 城市代码
     */
    @ApiModelProperty(value = "城市代码")
    private String cityCode;

    /**
     * 机构城市
     */
    @ApiModelProperty(value = "机构城市")
    private String cityName;

    /**
     * 区县代码
     */
    @ApiModelProperty(value = "区县代码")
    private String regionCode;

    /**
     * 机构区县
     */
    @ApiModelProperty(value = "机构区县")
    private String regionName;

    /**
     * 客户商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    @ApiModelProperty(value = "客户商业级别 (取数据字典)")
    private Integer customerSupplierLevel;

    /**
     * 客户普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议
     */
    @ApiModelProperty(value = "客户普药级别 (取数据字典)")
    private Integer customerGeneralMedicineLevel;

    /**
     * 客户商业属性 1-城市商业、2-县级商业
     */
    @ApiModelProperty(value = "客户商业属性 (取数据字典)")
    private Integer customerSupplierAttribute;

    /**
     * 是否连锁总部 1是 2否
     */
    @ApiModelProperty(value = "客户是否连锁总部 (取数据字典)")
    private Integer customerHeadChainFlag;

    /**
     * 连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁
     */
    @ApiModelProperty(value = "连锁属性 (取数据字典)")
    private Integer customerChainAttribute;

    /**
     * (客户)连锁KA 1是 2否
     */
    @ApiModelProperty(value = "(客户)连锁KA (取数据字典)")
    private Integer customerChainKaFlag;

    /**
     * 客户上级公司名称
     */
    @ApiModelProperty(value = "客户上级公司名称")
    private String customerParentSupplierName;

    /**
     * 客户上级公司编码
     */
    @ApiModelProperty(value = "客户上级公司编码")
    private String customerParentSupplierCode;

    /**
     * 客户商业销售类型 1-分销型、2-纯销型、3-综合型、4-托管型、5-连锁型
     */
    @ApiModelProperty(value = "客户商业销售类型 (取数据字典)")
    private Integer customerSupplierSaleType;

    /**
     * 客户基药商信息 1-基药配送商、2-基药促销商
     */
    @ApiModelProperty(value = "客户基药商信息 (取数据字典)")
    private Integer customerBaseSupplierInfo;

    /**
     * 客户供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role
     */
    @ApiModelProperty(value = "终端类型 (取数据字典)")
    private Integer customerSupplyChainRole;

    /**
     * 医院性质 1公立 2私立 3厂办
     */
    @ApiModelProperty(value = "医院性质 (取数据字典)")
    private Integer customerMedicalNature;

    /**
     * 医院类型 1-综合医院、2专科医院、3部队医院、4儿童医院、5县级医院、6厂矿职工医院、7中医医院、8社区卫生服务站、9乡镇卫生院、10村卫生室、11妇保院、12疾控中心、13私人终端、14社区卫生服务中心、15高校医院、16其他
     */
    @ApiModelProperty(value = "医院类型 (取数据字典)")
    private Integer customerMedicalType;

    /**
     * 以岭级别
     */
    @ApiModelProperty(value = "以岭级别 (取数据字典)")
    private Integer customerYlLevel;

    /**
     * 国家等级 1三级甲等、2三级乙等、3三级丙等、4二级甲等、5二级乙等、6二级丙等、7一级甲等、8三级特等、9一级乙等、10一级丙等、11其他
     */
    @ApiModelProperty(value = "国家等级 (取数据字典)")
    private Integer customerNationalGrade;

    /**
     * 是否基药终端 1是 2否
     */
    @ApiModelProperty(value = "是否基药终端 (取数据字典)")
    private Integer baseMedicineFlag;

    /**
     * 药店属性 1-连锁分店；2-单体药店
     */
    @ApiModelProperty(value = "药店属性 (取数据字典)")
    private Integer pharmacyAttribute;

    /**
     * 药店类型 1-直营；2-加盟
     */
    @ApiModelProperty(value = "药店类型 (取数据字典)")
    private Integer pharmacyType;

    /**
     * 药店级别 1-A级；2-B级；3-C级
     */
    @ApiModelProperty(value = "药店级别 (取数据字典)")
    private Integer pharmacyLevel;

    /**
     * 连锁属性
     */
    @ApiModelProperty(value = "连锁属性 (取数据字典)")
    private Integer pharmacyChainAttribute;

    /**
     * 标签属性 1-社区店；2-商圈店；3-院边店；4-电商店
     */
    @ApiModelProperty(value = "标签属性 (取数据字典)")
    private Integer labelAttribute;

    /**
     * 是否目标 1-是；2-否
     */
    @ApiModelProperty(value = "是否目标 (取数据字典)")
    private Integer targetFlag;

    /**
     * 原始商品名称
     */
    @ApiModelProperty(value = "原始商品名称")
    private String soGoodsName;

    /**
     * 原始商品规格
     */
    @ApiModelProperty(value = "原始商品规格")
    private String soSpecifications;

    /**
     * 品种类型
     */
    @ApiModelProperty(value = "品种")
    private String varietyType;

    /**
     * 产品(sku)编码
     */
    @ApiModelProperty(value = "产品(sku)编码")
    private Long goodsCode;

    /**
     * 产品品名
     */
    @ApiModelProperty(value = "产品品名")
    private String goodsName;

    /**
     * 产品品规
     */
    @ApiModelProperty(value = "产品品规")
    private String goodsSpec;

    /**
     * 原始数量
     */
    @ApiModelProperty(value = "原始数量")
    private BigDecimal soQuantity;

    /**
     * 原始单位
     */
    @ApiModelProperty(value = "原始单位")
    private String soUnit;

    /**
     * 原始产品价格
     */
    @ApiModelProperty(value = "原始产品价格")
    private BigDecimal soPrice;

    /**
     * 产品单价
     */
    @ApiModelProperty(value = "产品价格")
    private BigDecimal salesPrice;

    /**
     * 最终数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal finalQuantity;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal soTotalAmount;

    /**
     * 批号
     */
    @ApiModelProperty(value = "批号")
    private String soBatchNo;

    /**
     * 流向分类 1-正常 2-销量申诉 3-流向补传 4-团购处理
     *  {@link com.yiling.dataflow.wash.enums.FlowClassifyEnum}
     */
    @ApiModelProperty(value = "流向类型 (取数据字典)")
    private Integer flowClassify;

    /**
     * 收集方式 0-excel导入 1-工具 2-以岭接口 3-第三方接口 4-ftp
     */
    @ApiModelProperty(value = "收集方式 (取数据字典)")
    private Integer collectionMethod;

    /**
     * 流向清理时间
     */
    @ApiModelProperty(value = "流向清理时间")
    private Date washTime;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * 销量申诉类型
     */
    @ApiModelProperty(value = "销量申诉类型 (取数据字典)")
    private Integer complainType;

    /**
     * 首次锁定时间
     */
    @ApiModelProperty(value = "首次锁定时间")
    private Date lockTime;


    /**
     * 最后解锁时间
     */
    @ApiModelProperty(value = "最后解锁时间")
    private Date lastUnLockTime;

    /**
     * 省区经理工号
     */
    @ApiModelProperty(value = "省区经理工号")
    private String provinceManagerCode;

    /**
     * 省区经理编号
     */
    @ApiModelProperty(value = "省区经理工号")
    private String provinceManagerName;

    /**
     * 流向是否锁定 1:是 2否
     */
    @ApiModelProperty(value = "流向是否锁定 1:是 2否")
    private Integer isLockFlag;

    /**
     * 流向KeyId
     */
    private String flowKey;

}