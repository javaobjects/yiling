package com.yiling.sjms.flee.bo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/3/13 0013
 */
@Data
public class FleeingFormBO implements Serializable {

    /**
     * ID
     */
    private Long id;

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
     * 创建时间
     */
    private Date createTime;


    // ================================================================

    /**
     * 申报类型 1-电商 2-非电商
     */
    private Integer reportType;

    /**
     * 确认状态：1-待确认 2-生成中 3-生成成功 4-生成失败
     */
    private Integer confirmStatus;

    /**
     * 确认人id
     */
    private String confirmUserId;

    /**
     * 生成流向表单时间(提交清洗)
     */
    private Date submitWashTime;

}
