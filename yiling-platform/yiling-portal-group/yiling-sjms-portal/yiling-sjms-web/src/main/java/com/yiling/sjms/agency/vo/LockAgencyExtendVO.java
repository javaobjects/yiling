package com.yiling.sjms.agency.vo;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.BaseVO;

import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 机构拓展信息及三者信息
 *
 * @author: dexi.yao
 * @date: 2023/2/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LockAgencyExtendVO extends BaseVO {

    /**
     * 机构信息
     */
    @ApiModelProperty("机构信息")
    CrmEnterpriseInfoVO crmEnterpriseInfo;

    /**
     * 拓展信息类型 1-企业库原有信息 2-锁定机构录入
     */
    @ApiModelProperty("拓展信息类型 1-企业库原有信息 2-锁定机构录入")
    private Integer dataType;

    /**
     * 主表主键
     */
    @ApiModelProperty(value = "机构eid")
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
     * 连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁
     */
    @ApiModelProperty(value = "连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁")
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

    /**
     * 商业销售类型 1-分销型、2-纯销型、3-综合型、4-托管型、5-连锁型
     */
    @ApiModelProperty(value = "商业销售类型 1-分销型、2-纯销型、3-综合型、4-托管型、5-连锁型")
    private Integer supplierSaleType;

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
     * 锁定类型 1-打单 2-销售
     */
    @ApiModelProperty(value = "锁定类型 1-打单 2-销售")
    private Integer lockType;

    /**
     * 三者关系列表
     */
    @ApiModelProperty(value = "三者关系列表--注意isIgnoreSubmit字段为true时，三者关系不可修改且提交时忽略相关记录")
    private List<CrmEnterpriseRelationShipVO> relationList;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    @Data
    public static class CrmEnterpriseInfoVO {

        /**
         * ID
         */
        @Ignore
        @ApiModelProperty(value = "crmEnterpriseId",hidden = true)
        private Long id;

        /**
         * crmEnterpriseId
         */
        @ApiModelProperty("crmEnterpriseId")
        private Long crmEnterpriseId;

        /**
         * 机构名称
         */
        @ApiModelProperty("机构名称")
        private String name;
        /**
         * 统一社会信用代码
         */
        @ApiModelProperty("统一社会信用代码")
        private String licenseNumber;

        /**
         * 药品经营许可证编号
         */
        @ApiModelProperty("药品经营许可证编号")
        private String distributionLicenseNumber;

        /**
         * 医机构执业许可证
         */
        @ApiModelProperty("医机构执业许可证")
        private String institutionPracticeLicense;

        /**
         * 所属省份编码
         */
        @ApiModelProperty("所属省份编码")
        private String provinceCode;

        /**
         * 所属城市编码
         */
        @ApiModelProperty("所属城市编码")
        private String cityCode;

        /**
         * 所属区域编码
         */
        @ApiModelProperty("所属区域编码")
        private String regionCode;

        /**
         * 所属省份名称
         */
        @ApiModelProperty("所属省份名称")
        private String provinceName;

        /**
         * 所属城市名称
         */
        @ApiModelProperty("所属城市名称")
        private String cityName;

        /**
         * 所属区区域名称
         */
        @ApiModelProperty("所属区区域名称")
        private String regionName;

        /**
         * 供应链角色：1-商业公司 2-医院 3-零售药店。数据字典：erp_crm_supply_chain_role
         */
        @ApiModelProperty("供应链角色：1-商业公司 2-医院 3-零售药店。数据字典：erp_crm_supply_chain_role")
        private Integer supplyChainRole;

        public Long getCrmEnterpriseId() {
            return id;
        }
    }

    @Data
    public static class CrmEnterpriseRelationShipVO {

        /**
         * 产品组id
         */
        @ApiModelProperty("产品组id")
        private Long productGroupId;

        @Ignore
        @ApiModelProperty(value = "id", hidden = true)
        private Long id;

        /**
         * 代表编码
         */
        @ApiModelProperty("代表编码")
        private String representativeCode;

        /**
         * 职级编码
         */
        @ApiModelProperty("职级编码")
        private String dutyGredeId;

        /**
         * 年份
         */
        @ApiModelProperty("年份")
        private String year;

        /**
         * 月份
         */
        @ApiModelProperty("月份")
        private String month;

        /**
         * 部门
         */
        @ApiModelProperty("部门")
        private String department;

        /**
         * 业务部门
         */
        @ApiModelProperty("业务部门")
        private String businessDepartment;

        /**
         * 省区
         */
        @ApiModelProperty("省区")
        private String provincialArea;

        /**
         * 业务省区
         */
        @ApiModelProperty("业务省区")
        private String businessProvince;

        /**
         * 业务区域代码
         */
        @ApiModelProperty("业务区域代码")
        private String businessAreaCode;

        /**
         * 业务区域
         */
        @ApiModelProperty("业务区域")
        private String businessArea;

        /**
         * 业务区域描述
         */
        @ApiModelProperty("业务区域描述")
        private String businessAreaDescription;

        /**
         * 上级主管编码
         */
        @ApiModelProperty("上级主管编码")
        private String superiorSupervisorCode;

        /**
         * 上级主管名称
         */
        @ApiModelProperty("上级主管名称")
        private String superiorSupervisorName;

        /**
         * 代表名称
         */
        @ApiModelProperty("代表名称")
        private String representativeName;

        /**
         * 客户编码
         */
        @ApiModelProperty("客户编码")
        private String customerCode;

        /**
         * 客户名称
         */
        @ApiModelProperty("客户名称")
        private String customerName;

        /**
         * 供应链角色
         */
        @ApiModelProperty("供应链角色")
        private String supplyChainRole;

        /**
         * 省份
         */
        @ApiModelProperty("省份")
        private String province;

        /**
         * 城市代码
         */
        @ApiModelProperty("城市代码")
        private String cityCode;

        /**
         * 城市
         */
        @ApiModelProperty("城市")
        private String city;

        /**
         * 区县代码
         */
        @ApiModelProperty("区县代码")
        private String districtCountyCode;

        /**
         * 区县
         */
        @ApiModelProperty("区县")
        private String districtCounty;

        /**
         * 医院类型/药店类型
         */
        @ApiModelProperty("医院类型/药店类型")
        private String hospitalPharmacyType;

        /**
         * 医院以岭级别/药店属性
         */
        @ApiModelProperty("医院以岭级别/药店属性")
        private String hospitalPharmacyAttribute;

        /**
         * 国家等级/药店级别
         */
        @ApiModelProperty("国家等级/药店级别")
        private String hospitalPharmacyLevel;

        /**
         * 产品组
         */
        @ApiModelProperty("产品组")
        private String productGroup;

        /**
         * 是否目标
         */
        @ApiModelProperty("是否目标")
        private String targetOrNot;

        /**
         * 承包类型
         */
        @ApiModelProperty("承包类型")
        private String contractingType;

        /**
         * 是否代跑
         */
        @ApiModelProperty("是否代跑")
        private Integer substituteRunning;

        /**
         * 是否协议
         */
        @ApiModelProperty("是否协议")
        private String agreementOrNot;

        /**
         * 协议类型
         */
        @ApiModelProperty("协议类型")
        private String agreementType;

        /**
         * 连锁药店上级编码
         */
        @ApiModelProperty("连锁药店上级编码")
        private String chainDrugstoreSuperiorCode;

        /**
         * 连锁药店上级名称
         */
        @ApiModelProperty("连锁药店上级名称")
        private String chainDrugstoreSuperiorName;

        /**
         * 协议连锁编码
         */
        @ApiModelProperty("协议连锁编码")
        private String protocolConcatenationCode;

        /**
         * 协议连锁名称
         */
        @ApiModelProperty("协议连锁名称")
        private String protocolConcatenationName;

        /**
         * 连锁药店上级是否KA
         */
        @ApiModelProperty("连锁药店上级是否KA")
        private String isKa;

        /**
         * 是否医保
         */
        @ApiModelProperty("是否医保")
        private String isMedicalInsurance;

        /**
         * 备注/标签属性
         */
        @ApiModelProperty("备注/标签属性")
        private String remark;

        /**
         * 备注/标签属性
         */
        @ApiModelProperty("机构基本信息id")
        private Long crmEnterpriseId;

        /**
         * 岗位代码
         */
        @ApiModelProperty("岗位代码")
        private Long postCode;

        /**
         * 岗位名称
         */
        @ApiModelProperty("岗位名称")
        private String postName;

        /**
         * 客户名称
         */
        @ApiModelProperty("客户名称")
        private String customerArea;

        /**
         * 产品组名称
         */
        @ApiModelProperty("产品组名称")
        private String productGroupName;

        /**
         * 机构所属省
         */
        @ApiModelProperty("机构所属省")
        private String provinceName;

        /**
         * 机构所属市
         */
        @ApiModelProperty("机构所属市")
        private String cityName;

        /**
         * 机构所属区
         */
        @ApiModelProperty("机构所属区")
        private String regionName;

        /**
         * 是否忽略提交
         */
        @ApiModelProperty("是否忽略提交 0-否 1-是")
        private Integer isIgnoreSubmit;

        /**
         * 可选产品组
         */
        @ApiModelProperty("可选产品组")
        private List<ProductGroupVO> groupList;

        /**
         * 上级主管岗位代码
         */
        @ApiModelProperty("上级主管岗位代码")
        private Long superiorJob;

        /**
         * 上级主管岗位名称
         */
        @ApiModelProperty("上级主管岗位名称")
        private String superiorJobName;

        public Integer getIsIgnoreSubmit() {
            if (id != null && id != 0L) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    @Data
    public static class  ProductGroupVO{

        /**
         * ID
         */
        @ApiModelProperty("id")
        private Long id;

        /**
         * 产品组
         */
        @ApiModelProperty("产品组")
        private String productGroup;

        /**
         * 产品组id
         */
        @ApiModelProperty("产品组id")
        private Long productGroupId;
    }


}