package com.yiling.sjms.gb.form;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/18
 */
@Data
public class GbAppealFormExecuteEditDetailForm extends BaseForm {

    /**
     * 团购数据处理扣减/增加ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "团购数据处理扣减/增加ID")
    private Long appealAllocationId;

    /**
     * 扣减/增加数量
     */
    @NotNull
    @ApiModelProperty(value = "扣减/增加数量")
    private BigDecimal quantity;

    /**
     * 机构部门ID(用户自己填写部门名称时此id为0)
     */
    @ApiModelProperty(value = "部门ID（orgId）")
    private Long orgId;

    /**
     * 机构部门
     */
    @NotBlank
    @ApiModelProperty(value = "部门（orgName）")
    private String department;

    /**
     * 机构业务部门ID(用户自己填写业务部门名称时此id为0)
     */
    @ApiModelProperty(value = "业务部门ID（orgId）")
    private Long businessOrgId;

    /**
     * 业务部门（orgName）
     */
    @ApiModelProperty(value = "业务部门（orgName）")
    private String businessDepartment;

    /**
     * 业务代表工号
     */
    @ApiModelProperty(value = "业务代表工号")
    private String empId;

    /**
     * 代表姓名
     */
    @ApiModelProperty(value = "业务代表姓名")
    private String empName;

    /**
     * 业务省区
     */
    @ApiModelProperty(value = "业务省区")
    private String yxProvince;

    /**
     * 省区
     */
    @ApiModelProperty("省区")
    private String provinceArea;

    /**
     * 所属部门ID
     */
    @ApiModelProperty(value = "区办ID")
    private Long deptId;

    /**
     * 所属部门ID
     */
    @ApiModelProperty(value = "区办名称")
    private String deptName;

    /**
     * 营销部门名称
     */
    @ApiModelProperty(value = "营销部门名称")
    private String yxDept;

    /**
     * 业务区域（机构区办）
     */
    @ApiModelProperty(value = "业务区域")
    private String yxArea;

    /**
     * 上级主管工号
     */
    @ApiModelProperty(value = "上级主管工号")
    private String superior;

    /**
     * 上级主管姓名
     */
    @ApiModelProperty(value = "上级主管姓名")
    private String superiorName;

    /**
     * 岗位代码
     */
    @ApiModelProperty(value = "岗位代码")
    private Long jobId;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String jobName;

}
