package com.yiling.sjms.oa.todo.service;

import com.yiling.sjms.oa.todo.dto.request.ProcessDoneRequest;
import com.yiling.sjms.oa.todo.dto.request.ProcessOverRequest;
import com.yiling.sjms.oa.todo.dto.request.ReceiveTodoRequest;

/**
 * OA 统一待办 Service
 *
 * @author: xuan.zhou
 * @date: 2023/1/6
 */
public interface OaTodoService {

    /**
     * 接收待办流程
     *
     * @param request 请求参数
     * @return boolean
     * @author xuan.zhou
     * @date 2023/1/6
     **/
    Boolean receiveTodo(ReceiveTodoRequest request);

    /**
     * 处理待办流程
     *
     * @param request 请求参数
     * @return boolean
     * @author xuan.zhou
     * @date 2023/1/6
     **/
    Boolean processDone(ProcessDoneRequest request);

    /**
     * 处理办结流程
     *
     * @param request 请求参数
     * @return boolean
     * @author xuan.zhou
     * @date 2023/1/6
     **/
    Boolean processOver(ProcessOverRequest request);
}
