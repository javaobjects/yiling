package com.yiling.workflow.workflow.dto.request;

import java.util.Map;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 启动流程入参
 * @author: gxl
 * @date: 2022/11/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StartProcessRequest extends BaseRequest {
    private static final long serialVersionUID = -2997105574806439555L;

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
     * 详情页路径
     */
    private String pagePath;

    /**
     * 表单类型
     */
    private Integer formType;
    /**
     * 消息标签
     */
    private String tag;

    private String title;
    /**
     * form表Id
     */
    private Long id;
}