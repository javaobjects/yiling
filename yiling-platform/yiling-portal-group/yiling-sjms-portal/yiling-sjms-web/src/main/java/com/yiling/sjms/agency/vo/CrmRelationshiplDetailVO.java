package com.yiling.sjms.agency.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 零售机构档案扩展表
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmRelationshiplDetailVO extends BaseVO {

    /**
     * 医院性质 1公立 2私立 3厂办
     */
    private Integer medicalNature;
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
    private String businessAreaCode;

    /**
     * 业务区域
     */
    @ApiModelProperty("业务区域")
    private String businessArea;

    /**
     * 业务区域描述
     */
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
     * 代表编码
     */
    @ApiModelProperty("业务代表工号")
    private String representativeCode;

    /**
     * 代表名称
     */
    @ApiModelProperty("业务代表工号")
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
    @ApiModelProperty("产品组")
    private String productGroup;

    /**
     * 产品组
     */
    @ApiModelProperty("产品组id")
    private Long productGroupId;

    /**
     * 辖区ID
     */
    @ApiModelProperty("辖区ID")
    private Long manorId;
    @ApiModelProperty("辖区名称")
    private String manorName;
    /**
     * 品类ID
     */
    @ApiModelProperty("品种名称")
    private String categoryName;
    @ApiModelProperty("品种分类ID")
    private String categoryId;


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
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 主表关联字段
     */
    @ApiModelProperty("主表关联字段")
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

    private Date createTime;

    /**
     * 省区经理岗位代码
     */
    @ApiModelProperty("岗位名称")
    private Long provincialManagerPostCode;

    /**
     * 省区经理岗位名称
     */
    @ApiModelProperty("省区经理岗位名称")
    private String provincialManagerPostName;

    /**
     * 省区经理工号
     */
    @ApiModelProperty("省区经理工号")
    private String provincialManagerCode;

    /**
     * 省区经理姓名
     */
    @ApiModelProperty("省区经理姓名")
    private String provincialManagerName;

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
}
