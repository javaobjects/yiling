package com.yiling.sjms.oa.todo.api;

import com.yiling.sjms.oa.todo.dto.request.ProcessDoneRequest;
import com.yiling.sjms.oa.todo.dto.request.ProcessOverRequest;
import com.yiling.sjms.oa.todo.dto.request.ReceiveTodoRequest;

/**
 * OA 统一待办 API
 *
 * @author: xuan.zhou
 * @date: 2023/1/6
 */
public interface OaTodoApi {

    /**
     * 接收待办流程
     *
     * @param request 请求参数
     * @return boolean
     * @author xuan.zhou
     * @date 2023/1/6
     **/
    boolean receiveTodo(ReceiveTodoRequest request);

    /**
     * 处理待办流程
     *
     * @param request 请求参数
     * @return boolean
     * @author xuan.zhou
     * @date 2023/1/6
     **/
    boolean processDone(ProcessDoneRequest request);

    /**
     * 处理办结流程
     *
     * @param request 请求参数
     * @return boolean
     * @author xuan.zhou
     * @date 2023/1/6
     **/
    boolean processOver(ProcessOverRequest request);
}
