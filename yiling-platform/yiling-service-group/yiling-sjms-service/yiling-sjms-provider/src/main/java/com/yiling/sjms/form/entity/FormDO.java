package com.yiling.sjms.form.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 表单基础主表
 * </p>
 *
 * @author wei.wang
 * @date 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("form")
public class FormDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 单据编号
     */
    private String code;

    /**
     * 单据名称
     */
    private String name;

    /**
     * 团购类型:1-团购单据
     */
    private Integer type;
    /**
     *  传输类型：1、上传excel; 2、选择流向
     */
    private Integer transferType;

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
    private String flowId;

    /**
     * 流程版本
     */
    private String flowVersion;

    /**
     * 营销区办名称
     */
    private String bizArea;

    /**
     * 营销部门名称
     */
    private String bizDept;

    /**
     * 营销省区名称
     */
    private String bizProvince;

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
     * 省区
     */
    private String province;

    /**
     * 提交审批时间
     */
    private Date submitTime;

    /**
     * 审批通过时间
     */
    private Date approveTime;

    /**
     * 状态：10-待提交 20-审批中 200-已通过 201-已驳回
     */
    private Integer status;


    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
