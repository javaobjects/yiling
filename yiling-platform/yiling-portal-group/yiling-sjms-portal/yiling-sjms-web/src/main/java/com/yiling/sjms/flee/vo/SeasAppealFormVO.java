package com.yiling.sjms.flee.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.sjms.flee.form.SubmitSalesAppealForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础表单信息 VO
 *
 * @author: xuan.zhou
 * @date: 2023/3/1
 */
@Data
public class SeasAppealFormVO {

    @ApiModelProperty("表单ID")
    private Long id;

    /**
     * 单据编号
     */
    @ApiModelProperty("单据编号")
    private String code;

    /**
     * 单据名称
     */
    @ApiModelProperty("单据名称")
    private String name;

    /**
     * 单据类型
     */
    @ApiModelProperty("单据类型（参见字典）")
    private Integer type;

    /**
     * 流程实例ID
     */
    @ApiModelProperty("流程实例ID")
    private String flowId;

    /**
     * 营销区办名称
     */
    @ApiModelProperty("营销区办名称")
    private String bizArea;

    /**
     * 营销部门名称
     */
    @ApiModelProperty("营销部门名称（业务部门）")
    private String bizDept;

    /**
     * 营销省区名称
     */
    @ApiModelProperty("营销省区名称（业务省区）")
    private String bizProvince;

    /**
     * 发起人ID
     */
    @ApiModelProperty("发起人ID")
    private String empId;

    /**
     * 发起人姓名
     */
    @ApiModelProperty("发起人姓名")
    private String empName;

    /**
     * 发起人部门ID
     */
    @ApiModelProperty("发起人部门ID")
    private Long deptId;

    /**
     * 发起人部门名称
     */
    @ApiModelProperty("发起人部门名称")
    private String deptName;

    /**
     * 事业部ID
     */
    @ApiModelProperty("事业部ID")
    private Long bdDeptId;

    /**
     * 事业部名称
     */
    @ApiModelProperty("事业部名称（对应页面的部门字段）")
    private String bdDeptName;

    /**
     * 省区
     */
    @ApiModelProperty("省区")
    private String province;

    /**
     * 提交审批时间
     */
    @ApiModelProperty("提交审批时间")
    private Date submitTime;

    /**
     * 审批通过时间
     */
    @ApiModelProperty("审批通过时间")
    private Date approveTime;

    /**
     * 状态：10-待提交 20-审批中 200-已通过 201-已驳回
     */
    @ApiModelProperty("状态（参见字典）")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;


    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private BigDecimal appealAmount;

    /**
     * 申诉描述
     */
    @ApiModelProperty("申诉描述")
    private String appealDescribe;

    /**
     * 确认时的备注意见
     */
    @ApiModelProperty("确认时的备注意见")
    private String confirmRemark;

    /**
     * 确认状态：1-待确认 2-确认提交
     */
    @ApiModelProperty("确认状态：1-待确认 2-确认提交")
    private Integer ConfirmStatus;


    @ApiModelProperty(value = "申诉类型 1补传月流向、2调整月流向、3代表终端对应错误、4终端类型申诉、5其他")
    private Integer appealType;

    /**
     * 调整月流向对应的调整事项 1漏做客户关系对照 2未备案商业销售到锁定终端3医院分院以总院名头进货4 医院的院内外药店进货5医联体、医共体共用进货名头6互联网医院无法体现医院名字7药店子公司以总部名头进货
     */
    @ApiModelProperty(value = "调整月流向对应的调整事项 1漏做客户关系对照 2未备案商业销售到锁定终端3医院分院以总院名头进货4 医院的院内外药店进货5医联体、医共体共用进货名头6互联网医院无法体现医院名字7药店子公司以总部名头进货")
    private Integer monthAppealType;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private List<AppendixDetailVO> appendixList;

    /**
     *  传输类型：1、上传excel; 2、选择流向
     */
    @ApiModelProperty(value = "传输类型：1、上传excel; 2、选择流向")
    private Integer transferType;
}
