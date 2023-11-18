package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/16 0016
 */
@Data
@Accessors(chain = true)
public class UpdateCrmEnterpriseRelationShipRequest {

    /**
     * 年份
     */
    private String year;

    /**
     * 月份
     */
    private String month;

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
     * 业务区域描述
     */
    private String businessAreaDescription;

    /**
     * 上级主管编码
     */
    private String superiorSupervisorCode;

    /**
     * 上级主管名称
     */
    private String superiorSupervisorName;

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
     * 终端类型
     */
    private String supplyChainRole;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县代码
     */
    private String districtCountyCode;

    /**
     * 区县
     */
    private String districtCounty;

    /**
     * 医院类型/药店类型
     */
    private String hospitalPharmacyType;

    /**
     * 医院以岭级别/药店属性
     */
    private String hospitalPharmacyAttribute;

    /**
     * 国家等级/药店级别
     */
    private String hospitalPharmacyLevel;

    /**
     * 产品组
     */
    private String productGroup;

    /**
     * 是否目标
     */
    private String targetOrNot;

    /**
     * 承包类型
     */
    private String contractingType;

    /**
     * 是否代跑
     */
    private String substituteRunning;

    /**
     * 是否协议
     */
    private String agreementOrNot;

    /**
     * 协议类型
     */
    private String agreementType;

    /**
     * 连锁药店上级编码
     */
    private String chainDrugstoreSuperiorCode;

    /**
     * 连锁药店上级名称
     */
    private String chainDrugstoreSuperiorName;

    /**
     * 协议连锁编码
     */
    private String protocolConcatenationCode;

    /**
     * 协议连锁名称
     */
    private String protocolConcatenationName;

    /**
     * 连锁药店上级是否KA
     */
    private String isKa;

    /**
     * 是否医保
     */
    private String isMedicalInsurance;

    /**
     * 备注/标签属性
     */
    private String remark;

    /**
     * 备注/标签属性
     */
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
     * 终端类型 供应链类型：1商业公司 2医疗机构 3零售机构
     */
    private Integer supplyChainRoleType;

    /**
     * 产品组id
     */
    private Long productGroupId;

    /**
     * 省区经理岗位代码
     */
    private Long provincialManagerPostCode;

    /**
     * 省区经理岗位名称
     */
    private String provincialManagerPostName;

    /**
     * 省区经理工号
     */
    private String provincialManagerCode;

    /**
     * 省区经理姓名
     */
    private String provincialManagerName;

    /**
     * 上级主管岗位代码
     */
    private Long superiorJob;

    /**
     * 上级主管岗位名称
     */
    private String superiorJobName;

    /**
     * 职级编码
     */
    private String dutyGredeId;
}
