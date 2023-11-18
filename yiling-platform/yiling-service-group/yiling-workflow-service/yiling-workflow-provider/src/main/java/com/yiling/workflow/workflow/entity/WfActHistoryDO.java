package com.yiling.workflow.workflow.entity;

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
 * 流程历史记录表
 * </p>
 *
 * @author xuan.zhou
 * @date 2023-02-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("wf_act_history")
public class WfActHistoryDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 单据ID
     */
    private Long formId;

    /**
     * 操作人工号
     */
    private String fromEmpId;

    /**
     * 操作人姓名
     */
    private String fromEmpName;

    /**
     * 接收人工号（多个以英文逗号分隔）
     */
    private String toEmpIds;

    /**
     * 接收人姓名（多个以英文逗号分隔）
     */
    private String toEmpNames;

    /**
     * 操作类型：1-提交申请 2-驳回 3-审批通过 4-转发 5-批注
     */
    private Integer actType;

    /**
     * 意见/批注
     */
    private String actText;

    /**
     * 操作时间
     */
    private Date actTime;

    /**
     * 批注的转发记录ID
     */
    private Long forwardHistoryId;

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
