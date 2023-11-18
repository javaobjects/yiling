package com.yiling.sjms.agency.vo;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 新增时反显拓展信息
 * @author: gxl
 * @date: 2023/2/22
 */
@Data
public class AgencyExtendVO extends BaseVO {


    /**
     * 主表主键
     */
    @ApiModelProperty(value = "机构eid")
    @NotNull
    private Long crmEnterpriseId;



    /**
     * 商业级别 字典crm_supplier_level 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    @ApiModelProperty(value = "商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商")
    private Integer supplierLevel;

    /**
     * 商业属性 字典crm_supplier_attribute 1-城市商业、2-县级商业
     */
    @ApiModelProperty(value = "商业属性 1-城市商业、2-县级商业")
    private Integer supplierAttribute;

    /**
     * 普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议
     */
    @ApiModelProperty(value = "普药级别 字典crm_supplier_general_medicine_level 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议")
    private Integer generalMedicineLevel;

    /**
     * 是否连锁总部 1是2否
     */
    @ApiModelProperty(value = "是否连锁总部 1是2否")
    private Integer headChainFlag;


    /**
     * 基药商信息 1-基药配送商、2-基药促销商
     */
    @ApiModelProperty(value = "基药商信息 字典crm_supplier_base_supplier_info 1-基药配送商、2-基药促销商")
    private Integer baseSupplierInfo;

    /**
     * 流向打取人业务部门
     */
    @ApiModelProperty(value = "流向打取人业务部门")
    private String department;

    /**
     * 流向打取人业务省区
     */
    @ApiModelProperty(value = "流向打取人业务省区")
    private String provincialArea;

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
     * 商务负责人工号
     */
    @ApiModelProperty(value = "商务负责人工号")
    private String commerceJobNumber;

    /**
     * 商务负责人姓名
     */
    @ApiModelProperty(value = "商务负责人姓名")
    private String commerceLiablePerson;

    /**
     * 医院性质 1公立 2私立 3厂办
     */
    @ApiModelProperty(value = "医院性质 字典crm_hospital_medical_nature 1公立 2私立 3厂办")
    private Integer medicalNature;

    /**
     * 医院类型 1-综合医院、2专科医院、3部队医院、4儿童医院、5县级医院、6厂矿职工医院、7中医医院、8社区卫生服务站、9乡镇卫生院、10村卫生室、11妇保院、12疾控中心、13私人终端、14社区卫生服务中心、15高校医院、16其他
     */
    @ApiModelProperty(value = "医院类型 字典crm_hospital_medical_type 1-综合医院、2专科医院、3部队医院、4儿童医院、5县级医院、6厂矿职工医院、7中医医院、8社区卫生服务站、9乡镇卫生院、10村卫生室、11妇保院、12疾控中心、13私人终端、14社区卫生服务中心、15高校医院、16其他")
    private Integer medicalType;

    /**
     * 以岭级别 1省级三级、2市级三级、3专科医院三级、4部队三级、5其他二级医院、6县人民医院、7市级一级、8县级其他、9其他厂矿职工高校、10其他私人终端、11乡镇卫生院、12社区卫生服务中心、13社区卫生服务站、14市级其他、15县中医院、16村卫生室、17部队二级、18专科医院二级、19私人终端二级、20厂矿职工高校二级、21私人终端三级、22厂矿职工高校三级、23其他专科医院、24其他部队医院、25县级三级、26乡镇卫生院二级、27社区卫生服务中心二级、28社区卫生服务中心三级
     */
    @ApiModelProperty(value = "以岭级别 字典crm_hospital_yl_level 1省级三级、2市级三级、3专科医院三级、4部队三级、5其他二级医院、6县人民医院、7市级一级、8县级其他、9其他厂矿职工高校、10其他私人终端、11乡镇卫生院、12社区卫生服务中心、13社区卫生服务站、14市级其他、15县中医院、16村卫生室、17部队二级、18专科医院二级、19私人终端二级、20厂矿职工高校二级、21私人终端三级、22厂矿职工高校三级、23其他专科医院、24其他部队医院、25县级三级、26乡镇卫生院二级、27社区卫生服务中心二级、28社区卫生服务中心三级")
    private Integer ylLevel;

    /**
     * 国家等级 1三级甲等、2三级乙等、3三级丙等、4二级甲等、5二级乙等、6二级丙等、7一级甲等、8三级特等、9一级乙等、10一级丙等、11其他
     */
    @ApiModelProperty(value = "国家等级 字典crm_hospital_national_grade 1三级甲等、2三级乙等、3三级丙等、4二级甲等、5二级乙等、6二级丙等、7一级甲等、8三级特等、9一级乙等、10一级丙等、11其他")
    private Integer nationalGrade;

    /**
     * 是否基药终端 1是 2否
     */
    @ApiModelProperty(value = "是否基药终端 1是 2否")
    private Integer baseMedicineFlag;

    /**
     * 药店属性 1-连锁分店；2-单体药店
     */
    @ApiModelProperty(value = "药店属性 字典crm_pharmacy_attribute 1-连锁分店；2-单体药店")
    private Integer pharmacyAttribute;

    /**
     * 药店级别 1-A级；2-B级；3-C级
     */
    @ApiModelProperty(value = "药店级别 字典crm_pharmacy_level 1-A级；2-B级；3-C级")
    private Integer pharmacyLevel;

    /**
     * 标签属性 1-社区店；2-商圈店；3-院边店；4-电商店
     */
    @ApiModelProperty(value = "标签属性 字典crm_pharmacy_label_attribute 1-社区店；2-商圈店；3-院边店；4-电商店")
    private Integer labelAttribute;

    /**
     * 店经理
     */
    @ApiModelProperty(value = "店经理")
    private String storeManager;

    /**
     * 是否医保 1-医保药店；2-慢保药店；3-否医保
     */
    @ApiModelProperty(value = "是否医保 字典crm_pharmacy_medical_insurance 1-医保药店；2-慢保药店；3-否医保")
    private Integer medicalInsurance;

    /**
     * 是否目标 1-是；2-否
     */
    @ApiModelProperty(value = "是否目标 1-是；2-否")
    private Integer targetFlag;
    /**
     * 连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁
     */
    @ApiModelProperty(value = "连锁属性crm_supplier_chain_attribute 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁")
    private Integer chainAttribute;

    /**
     * 连锁KA 1是2否
     */
    @ApiModelProperty(value = "连锁KA 1是2否")
    private Integer chainKaFlag;

    /**
     * 上级公司名称
     */
    @ApiModelProperty(value = "上级公司名称")
    private String parentSupplierName;

    /**
     * 上级公司编码
     */
    @ApiModelProperty(value = "上级公司编码")
    private String parentSupplierCode;
}