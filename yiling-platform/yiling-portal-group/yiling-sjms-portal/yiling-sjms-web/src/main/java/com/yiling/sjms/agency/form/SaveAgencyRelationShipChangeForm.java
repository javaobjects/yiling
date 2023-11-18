package com.yiling.sjms.agency.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class SaveAgencyRelationShipChangeForm extends BaseForm {

    /**
     * 三者关系修改form表id
     */
    private Long changeRelationShipId;

    /**
     * 机构基本信息id
     */
    private Long crmEnterpriseId;

    /**
     * 产品组
     */
    private String productGroup;

    /**
     * 产品组id
     */
    private Long productGroupId;


    /**
     * 岗位代码
     */
    private Long postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 代表编码
     */
    private String representativeCode;

    /**
     * 代表名称
     */
    private String representativeName;

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
     * 业务区域
     */
    private String businessRemark;

    /**
     * 供应链角色
     */
    private String supplyChainRole;

    /**
     * 修改三者关系表ID
     */
    private Long srcRelationShipIp;

    /**
     * 上级主管岗位id
     */
    @ApiModelProperty("上级主管岗位id")
    private Long superiorJob;

    /**
     * 职级编码
     */
    @ApiModelProperty("职级编码")
    private String dutyGredeId;
}
