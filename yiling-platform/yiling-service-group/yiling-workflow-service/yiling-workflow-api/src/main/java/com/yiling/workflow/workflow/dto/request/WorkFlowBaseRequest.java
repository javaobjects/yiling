package com.yiling.workflow.workflow.dto.request;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/2/25
 */
@Data
@Accessors(chain = true)
public class WorkFlowBaseRequest implements Serializable {
    private static final long serialVersionUID = 5686609116218972251L;
    /**
     * 流程定义id
     */
    private String procDefId;

    /**
     * 业务系统唯一标识（比如团购编号） 必填
     */
    private String businessKey;

    Map<String, Object> variables;

    /**
     * 提交人用户 emp_id 工号
     */
    private String startUserId;

    /**
     * 姓名
     */
    private String empName;
    /**
     * mq消息标签
     */
    private String tag;
    /**
     * form id
     */
    private Long id;

    /**
     * 详情页路径
     */
    private String pagePath;

    /**
     * 表单类型
     */
    private Integer formType;
}