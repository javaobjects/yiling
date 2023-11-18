package com.yiling.sjms.agency.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
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
public class CrmEnterpriseRelationPinchRunnerVO extends BaseVO {

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
    @ApiModelProperty("供应链角色：1-商业公司 2-医疗机构 3-零售机构。数据字典：crm_supply_chain_role")
    private Integer crmSupplyChainRole;

    /**
     * 产品组id
     */
    @ApiModelProperty("产品组")
    private String productGroupId;

    /**
     * 产品组名称
     */
    @ApiModelProperty("产品组名称")
    private String productGroupName;

    /**
     * 品种ID
     */
    @ApiModelProperty("品种ID")
    private Long categoryId;


    /**
     * 品种
     */
    @ApiModelProperty("品种")
    private String categoryName;

    /**
     * 辖区ID
     */
    @ApiModelProperty("辖区ID")
    private Long manorId;

    /**
     * 辖区
     */
    @ApiModelProperty("辖区")
    private String manorName;

    /**
     * 业务代表工号
     */
    @ApiModelProperty("业务代表工号")
    private String representativeCode;

    /**
     * 业务代表姓名
     */
    @ApiModelProperty("业务代表姓名")
    private String representativeName;

    /**
     * 业务代表岗位代码
     */
    @ApiModelProperty("业务代表岗位代码")
    private Long representativePostCode;

    /**
     * 业务代表岗位名称
     */
    @ApiModelProperty("业务代表岗位名称")
    private String representativePostName;

    /**
     * 业务代表部门
     */
    @ApiModelProperty("业务代表部门")
    private String representativeDepartment;

    /**
     * 业务代表省区
     */
    @ApiModelProperty("业务代表省区")
    private String representativeProvincialArea;

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
     * 销量计入部门
     */
    @ApiModelProperty("销量计入部门")
    private String superiorDepartment;

    /**
     * 销量计入省区
     */
    @ApiModelProperty("销量计入省区")
    private String superiorProvincialArea;

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
     * 三者关系ID
     */
    private Long enterpriseCersId;

    /**
     * 备注
     */
    private String remark;

}
