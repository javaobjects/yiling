package com.yiling.sjms.oa.todo.feign.request;

import lombok.Data;

/**
 * 接收待办流程 Request
 *
 * @author: xuan.zhou
 * @date: 2023/1/6
 */
@Data
public class ReceiveTodoRequestByJsonRequest implements java.io.Serializable {

    private static final long serialVersionUID = -2034914350723575236L;

    /**
     * 异构系统标识
     */
    private String syscode;

    /**
     * 流程实例id
     */
    private String flowid;

    /**
     * 标题
     */
    private String requestname;

    /**
     * 流程类型名称
     */
    private String workflowname;

    /**
     * 步骤名称（节点名称）
     */
    private String nodename;

    /**
     * PC地址
     */
    private String pcurl;

    /**
     * APP地址
     */
    private String appurl;

    /**
     * 创建人（原值）
     */
    private String creator;

    /**
     * 创建日期时间
     */
    private String createdatetime;

    /**
     * 接收人（原值）
     */
    private String receiver;

    /**
     * 接收日期时间
     */
    private String receivedatetime;

    /**
     * 时间戳字段，客户端使用线程调用接口的时候，根据此字段判断是否需要更新数据，防止后发的请求数据被之前的覆盖
     */
    private String receivets;
}
