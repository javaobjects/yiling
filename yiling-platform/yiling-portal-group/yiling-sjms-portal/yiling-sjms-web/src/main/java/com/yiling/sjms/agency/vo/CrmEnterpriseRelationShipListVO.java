package com.yiling.sjms.agency.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
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
public class CrmEnterpriseRelationShipListVO extends BaseVO {

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
     * 代表编码
     */
    @ApiModelProperty("代表编码")
    private String representativeCode;

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
    @ApiModelProperty("是否代跑 1否 2是")
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
     * 终端类型 供应链类型：1商业公司 2医疗机构 3零售机构
     */
    @ApiModelProperty("终端类型 供应链类型：1商业公司 2医疗机构 3零售机构")
    private Integer supplyChainRoleType;

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

    @ApiModelProperty("机构所属省")
    private String provinceName;
    @ApiModelProperty("机构所属市")
    private String cityName;
    @ApiModelProperty("机构所属区")
    private String regionName;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateUserName;

    /**
     * 产品组id
     */
    @ApiModelProperty("产品组id")
    private Long productGroupId;

    /**
     * 省区经理岗位代码
     */
    @ApiModelProperty("省区经理岗位代码")
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

    /**
     * 职级编码
     */
    @ApiModelProperty("职级编码")
    private String dutyGredeId;

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
    private Long categoryId;
}
