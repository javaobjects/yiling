package com.yiling.sjms.oa.todo.dto.request;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 处理待办流程 Request
 *
 * @author: xuan.zhou
 * @date: 2023/1/6
 */
@Data
@Accessors(chain = true)
public class ProcessDoneRequest implements java.io.Serializable {

    private static final long serialVersionUID = -1664664643992047758L;

    /**
     * 业务ID（推荐：工作流任务ID）
     */
    @NotEmpty
    private String bizId;

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
     * 接收人OA工号
     */
    @NotEmpty
    private String receiverCode;

}
