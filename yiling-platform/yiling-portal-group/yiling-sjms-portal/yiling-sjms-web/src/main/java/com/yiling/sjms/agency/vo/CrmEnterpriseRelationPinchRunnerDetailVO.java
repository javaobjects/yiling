package com.yiling.sjms.agency.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmEnterpriseRelationPinchRunnerDetailVO extends BaseVO {

    // 机构信息
    /**
     * 机构ID
     */
    @ApiModelProperty("机构编码")
    private Long crmEnterpriseId;

    /**
     * 机构名称
     */
    @ApiModelProperty("机构名称")
    private String crmEnterpriseName;

    /**
     * 供应链角色：1-商业公司 2-医疗机构 3-零售机构。数据字典：crm_supply_chain_role
     */
    @ApiModelProperty("供应链角色")
    private Integer crmSupplyChainRole;

    /**
     * 机构所属省份名称
     */
    @ApiModelProperty("机构所属省份名称")
    private String crmProvinceName;

    /**
     * 机构所属省份编码
     */
    @ApiModelProperty("机构所属省份编码")
    private String crmProvinceCode;

    /**
     * 机构所属城市名称
     */
    @ApiModelProperty("机构所属城市名称")
    private String crmCityName;

    /**
     * 机构所属城市编码
     */
    @ApiModelProperty("机构所属城市编码")
    private String crmCityCode;

    /**
     * 机构所属区域名称
     */
    @ApiModelProperty("机构所属区域名称")
    private String crmRegionName;

    /**
     * 机构所属区域编码
     */
    @ApiModelProperty("机构所属区域编码")
    private String crmRegionCode;

    /**
     * 三者关系ID
     */
    @ApiModelProperty("三者关系ID")
    private Long enterpriseCersId;

    // 代跑三者关系信息

    /**
     * 销量计入主管岗位代码
     */
    @ApiModelProperty("销量计入主管岗位代码")
    private Long businessSuperiorPostCode;

    /**
     * 销量计入主管岗位名称
     */
    @ApiModelProperty("销量计入主管岗位名称")
    private String businessSuperiorPostName;

    /**
     * 销量计入主管工号
     */
    @ApiModelProperty("销量计入主管工号")
    private String businessSuperiorCode;

    /**
     * 销量计入主管姓名
     */
    @ApiModelProperty("销量计入主管姓名")
    private String businessSuperiorName;

    /**
     * 销量计入业务部门
     */
    @ApiModelProperty("销量计入业务部门")
    private String businessSuperiorDepartment;

    /**
     * 销量计入业务省区
     */
    @ApiModelProperty("销量计入业务省区")
    private String businessSuperiorProvince;

    /**
     * 销量计入业务区域代码
     */
    private String businessSuperiorAreaCode;

    /**
     * 销量计入业务区域
     */
    @ApiModelProperty("销量计入业务区域")
    private String businessSuperiorArea;

    /**
     * 销量计入部门
     */
    @ApiModelProperty("销量计入部门")
    private String superiorDepartment;

    /**
     * 销量计入省区
     */
    @ApiModelProperty("销量计入省区")
    private String superiorProvincial;

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
     * 最后操作时间
     */
    @ApiModelProperty(value = "最后操作时间")
    private Date opTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String opUser;

    /**
     * 备注
     */
    private String remark;

    // 三者关系信息
    /**
     * 三者关系
     */
    List<CrmEnterpriseRelationPinchRunnerShipVO> crmEnterpriseRelationShipList;

}
