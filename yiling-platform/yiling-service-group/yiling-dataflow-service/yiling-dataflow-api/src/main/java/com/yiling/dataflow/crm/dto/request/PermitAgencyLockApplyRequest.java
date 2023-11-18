package com.yiling.dataflow.crm.dto.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.redis.config.BaseRedisConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PermitAgencyLockApplyRequest extends BaseRequest {

    private static final long serialVersionUID = -4257560043112258852L;

    /**
     * id
     */
    private Long id;

    /**
     * 主表主键
     */
    @NotNull
    private Long crmEnterpriseId;

    /**
     * 机构名称
     */
    @NotBlank
    private String name;

    /**
     * erp供应链角色：1-经销商 2-终端医院 3-终端药店
     */
    @NotNull
    private Integer supplyChainRole;

    /**
     * 商业级别 字典crm_supplier_level 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    private Integer supplierLevel;

    /**
     * 商业属性 字典crm_supplier_attribute 1-城市商业、2-县级商业
     */
    private Integer supplierAttribute;

    /**
     * 普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议
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
     * 医院性质 1公立 2私立 3厂办
     */
    private Integer medicalNature;

    /**
     * 医院类型 1-综合医院、2专科医院、3部队医院、4儿童医院、5县级医院、6厂矿职工医院、7中医医院、8社区卫生服务站、9乡镇卫生院、10村卫生室、11妇保院、12疾控中心、13私人终端、14社区卫生服务中心、15高校医院、16其他
     */
    private Integer medicalType;

    /**
     * 以岭级别 1省级三级、2市级三级、3专科医院三级、4部队三级、5其他二级医院、6县人民医院、7市级一级、8县级其他、9其他厂矿职工高校、10其他私人终端、11乡镇卫生院、12社区卫生服务中心、13社区卫生服务站、14市级其他、15县中医院、16村卫生室、17部队二级、18专科医院二级、19私人终端二级、20厂矿职工高校二级、21私人终端三级、22厂矿职工高校三级、23其他专科医院、24其他部队医院、25县级三级、26乡镇卫生院二级、27社区卫生服务中心二级、28社区卫生服务中心三级
     */
    private Integer ylLevel;

    /**
     * 国家等级 1三级甲等、2三级乙等、3三级丙等、4二级甲等、5二级乙等、6二级丙等、7一级甲等、8三级特等、9一级乙等、10一级丙等、11其他
     */
    private Integer nationalGrade;

    /**
     * 是否基药终端 1是 2否
     */
    private Integer baseMedicineFlag;

    /**
     * 药店属性 1-连锁分店；2-单体药店
     */
    private Integer pharmacyAttribute;

    /**
     * 药店级别 1-A级；2-B级；3-C级
     */
    private Integer pharmacyLevel;

    /**
     * 标签属性 1-社区店；2-商圈店；3-院边店；4-电商店
     */
    private Integer labelAttribute;

    /**
     * 店经理
     */
    private String storeManager;

    /**
     * 是否医保 1-医保药店；2-慢保药店；3-否医保
     */
    private Integer medicalInsurance;

    /**
     * 是否目标 1-是；2-否
     */
    private Integer targetFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 锁定类型 1-打单 2-销售
     */
    private Integer lockType;

    /**
     * 拓展信息类型 1-企业库原有信息 2-锁定机构录入
     */
    private Integer dataType;

    /**
     * 三者关系列表
     */
    @NotNull
    private List<AgencyLockRelationShipRequest> relationList;


    @Data
    public static class AgencyLockRelationShipRequest implements Serializable {

        private static final long serialVersionUID = -546281299612836751L;
        /**
         * 机构基本信息id
         */
        @NotNull
        private Long crmEnterpriseId;

        /**
         * 岗位代码
         */
        private Long postCode;

        /**
         * 岗位名称
         */
        private String postName;

        /**
         * 产品组
         */
        private String productGroup;

        /**
         * 代表编码
         */
        private String representativeCode;

        /**
         * 代表名称
         */
        private String representativeName;

        /**
         * 客户编码
         */
        private String customerCode;

        /**
         * 客户名称
         */
        private String customerName;

        /**
         * 供应链角色
         */
        private String supplyChainRole;

        /**
         * 部门
         */
        private String department;

        /**
         * 业务部门
         */
        private String businessDepartment;

        /**
         * 省区
         */
        private String provincialArea;

        /**
         * 业务省区
         */
        private String businessProvince;

        /**
         * 业务区域代码
         */
        private String businessAreaCode;

        /**
         * 业务区域
         */
        private String businessArea;

        /**
         * 上级主管编码
         */
        private String superiorSupervisorCode;

        /**
         * 上级主管名称
         */
        private String superiorSupervisorName;

        /**
         * 终端类型 供应链类型：1商业公司 2医疗机构 3零售机构
         */
        private Integer supplyChainRoleType;

        /**
         * 产品组id
         */
        private Long productGroupId;

        /**
         * 操作人ID
         */
        private Long opUserId;

    }
}
