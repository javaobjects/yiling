package com.yiling.dataflow.crm.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmEnterpriseRelationPinchRunnerDTO extends BaseDTO {

    /**
     * 机构ID
     */
    private Long crmEnterpriseId;

    /**
     * 机构名称
     */
    private String crmEnterpriseName;

    /**
     * 机构所属省份名称
     */
    private String crmProvinceName;

    /**
     * 机构所属省份编码
     */
    private String crmProvinceCode;

    /**
     * 机构所属城市名称
     */
    private String crmCityName;

    /**
     * 机构所属城市编码
     */
    private String crmCityCode;

    /**
     * 机构所属区域名称
     */
    private String crmRegionName;

    /**
     * 机构所属区域编码
     */
    private String crmRegionCode;

    /**
     * 供应链类型：1-商业公司 2-医疗机构 3-零售机构。数据字典：crm_supply_chain_role
     */
    private Integer crmSupplyChainRole;

    /**
     * 三者关系ID
     */
    private Long enterpriseCersId;

    /**
     * 辖区ID
     */
    private Long manorId;

    /**
     * 品种分类ID
     */
    private Long categoryId;

    /**
     * 销量计入主管岗位代码
     */
    private Long businessSuperiorPostCode;

    /**
     * 销量计入主管岗位名称
     */
    private String businessSuperiorPostName;

    /**
     * 销量计入主管工号
     */
    private String businessSuperiorCode;

    /**
     * 销量计入主管姓名
     */
    private String businessSuperiorName;

    /**
     * 销量计入部门
     */
    private String superiorDepartment;

    /**
     * 销量计入省区
     */
    private String superiorProvincial;

    /**
     * 销量计入业务部门
     */
    private String businessSuperiorDepartment;

    /**
     * 销量计入业务省区
     */
    private String businessSuperiorProvince;

    /**
     * 销量计入业务区域代码
     */
    private String businessSuperiorAreaCode;

    /**
     * 销量计入业务区域
     */
    private String businessSuperiorArea;

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
     * 备注
     */
    private String remark;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;
}
