package com.yiling.sjms.agency.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 表单基础主表
 * </p>
 *
 * @author gxl
 * @date 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FormVO extends BaseVO {


    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    private String code;

    /**
     * 单据类型 form_type
     */
    @ApiModelProperty(value = "单据类型 字典form_type")
    private Integer type;

    /**
     * 流程模板ID
     */
    private String flowTplId;

    /**
     * 流程模板名称
     */
    private String flowTplName;

    /**
     * 流程实例ID
     */
    @ApiModelProperty(value = "流程实例ID")
    private String flowId;


    /**
     * 发起人姓名
     */
    @ApiModelProperty(value = "发起人姓名")
    private String empName;


    /**
     * 发起人部门名称
     */
    @ApiModelProperty(value = "发起人部门名称")
    private String deptName;
    /**
     * 营销区办名称
     */
    @ApiModelProperty(value = "营销区办名称")
    private String bizArea;

    /**
     * 营销省区名称
     */
    @ApiModelProperty(value = "营销省区名称")
    private String bizProvince;

    /**
     * 提交审批时间
     */
    @ApiModelProperty(value = "提交审批时间")
    private Date submitTime;


    /**
     * 状态：10-待提交 20-审批中 200-已通过 201-已驳回
     */
    @ApiModelProperty(value = "状态 ：字典gb_form_status")
    private Integer status;

    @ApiModelProperty(value = "调整原因")
    @JsonProperty(value = "adjustReason")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
