package com.yiling.sjms.agency.form;


import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 机构新增修改表单请求参数
 *
 * @author: yong.zhang
 * @date: 2023/2/22 0022
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveRelationShipForm extends BaseForm {

    /**
     * 机构锁定信息表
     */
    private Long agencyFormId;

    /**
     * 机构基本信息id
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
     * 产品组
     */
    @ApiModelProperty("产品组")
    private String productGroup;

    /**
     * 代表编码
     */
    private String representativeCode;

    /**
     * 代表名称
     */
    @ApiModelProperty("代表名称")
    private String representativeName;

    /**
     * 客户编码
     */
    @ApiModelProperty("代表名称工号")
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
    @ApiModelProperty("业务部门")
    private String businessDepartment;

    /**
     * 省区
     */
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
     * 上级主管编码
     */
    private String superiorSupervisorCode;

    /**
     * 上级主管名称
     */
    private String superiorSupervisorName;

    /**
     * 修改三者关系表ID
     */
    private Long srcRelationShipIp;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

}
