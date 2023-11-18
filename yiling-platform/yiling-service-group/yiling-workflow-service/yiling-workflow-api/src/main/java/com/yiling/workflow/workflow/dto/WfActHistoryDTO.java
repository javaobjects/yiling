package com.yiling.workflow.workflow.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 流程历史记录 DTO
 *
 * @author xuan.zhou
 * @date 2023-02-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class WfActHistoryDTO extends BaseDTO {

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
     * 是否批注：0-否 1-是
     */
    private Integer commentFlag;

    /**
     * 批注的转发记录ID
     */
    private Long forwardHistoryId;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
