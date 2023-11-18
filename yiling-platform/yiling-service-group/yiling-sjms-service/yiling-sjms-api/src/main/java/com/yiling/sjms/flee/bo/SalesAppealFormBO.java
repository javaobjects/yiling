package com.yiling.sjms.flee.bo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author: shixing.sun
 * @date: 2023/3/13 0013
 */
@Data
public class SalesAppealFormBO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 确认人
     */
    private String confirmUser;

    /**
     * 单据编号
     */
    private String code;

    /**
     * 团购类型:1-团购单据
     */
    private Integer type;

    /**
     * 状态：10-待提交 20-审批中 200-已通过 201-已驳回
     */
    private Integer status;

    /**
     * 发起人ID
     */
    private String empId;

    /**
     * 发起人姓名
     */
    private String empName;

    /**
     * 发起人部门ID
     */
    private Long deptId;

    /**
     * 营销部门名称
     */
    private String bizDept;

    /**
     * 发起人部门名称
     */
    private String deptName;

    /**
     * 事业部ID
     */
    private Long bdDeptId;

    /**
     * 事业部名称
     */
    private String bdDeptName;

    /**
     * 提交审批时间
     */
    private Date submitTime;

    /**
     * 审批通过时间
     */
    private Date approveTime;

    /**
     * 提交清洗时间
     */
    private Date submitWashTime;

    /**
     * 创建时间
     */
    private Date createTime;


    // ================================================================

    /**
     * 申报类型 1-电商 2-非电商
     */
    private Integer reportType;


    /**
     * 申诉类型 1补传月流向、2调整月流向、3代表终端对应错误、4终端类型申诉、5其他
     */
    private Integer appealType;


    /**
     * 确认状态：1-待确认 2-生成中 3-生成成功 4-生成失败
     */
    private Integer confirmStatus;

    /**
     * 确认人
     */
    private String confirmUserName;

}
