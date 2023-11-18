package com.yiling.sjms.oa.todo.dto.request;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 接收待办流程 Request
 *
 * @author: xuan.zhou
 * @date: 2023/1/6
 */
@Data
public class ReceiveTodoRequest implements java.io.Serializable {

    private static final long serialVersionUID = 4870029915471538404L;

    /**
     * 业务ID（推荐：工作流任务ID）
     */
    @NotEmpty
    private String bizId;

    /**
     * 业务单号（form表code字段值）
     */
    private String formCode;

    /**
     * 标题（推荐：form表name字段值）
     */
    @NotEmpty
    private String title;

    /**
     * 工作流名称（推荐：FormTypeEnum.getName值）
     */
    @NotEmpty
    private String workflowName;

    /**
     * 当前节点名称
     */
    private String nodeName;

    /**
     * PC端业务处理跳转地址
     */
    @NotEmpty
    private String pcUrl;

    /**
     * APP端业务处理跳转地址
     */
    private String appUrl;

    /**
     * 创建人OA工号
     */
    @NotEmpty
    private String createrCode;

    /**
     * 创建时间
     */
    @NotNull
    private Date createTime;

    /**
     * 接收人OA工号
     */
    @NotEmpty
    private String receiverCode;

    /**
     * 接收时间
     */
    @NotNull
    private Date receiveTime;

    /**
     * 是否自动变为已办（发起人和接收人一样时直接变为已办）
     */
    private Boolean autoDone = false;
}
