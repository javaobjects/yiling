package com.yiling.dataflow.agency.dto.request;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/15 0015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCrmEnterpriseRelationShipRequest extends BaseRequest {
    private Long id;

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
     * crmenterprise的code
     */
    private String customerCode;

    /**
     * 客户名称 crmenterprise的name
     */
    private String customerName;



    /**
     * 经销商   终端医院   终端药店
     */
    private String supplyChainRole;

    /**
     * 终端类型 供应链类型：1商业公司 2医疗机构 3零售机构
     */
    private Integer supplyChainRoleType;

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
     * 产品组id
     */
    private Long productGroupId;

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
    private Integer substituteRunning;

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
     * 机构主键
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

    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

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
     * 上级主管岗位id
     */
    private Long superiorJob;

}
