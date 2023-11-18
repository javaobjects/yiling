package com.yiling.sjms.oa.todo.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.oa.todo.api.OaTodoApi;
import com.yiling.sjms.oa.todo.dto.request.ProcessDoneRequest;
import com.yiling.sjms.oa.todo.dto.request.ProcessOverRequest;
import com.yiling.sjms.oa.todo.dto.request.ReceiveTodoRequest;
import com.yiling.sjms.oa.todo.service.OaTodoService;

/**
 * OA 统一待办 API 实现
 *
 * @author: xuan.zhou
 * @date: 2023/1/6
 */
@DubboService
public class OaTodoApiImpl implements OaTodoApi {

    @Autowired
    OaTodoService oaTodoService;

    @Override
    public boolean receiveTodo(ReceiveTodoRequest request) {
        return oaTodoService.receiveTodo(request);
    }

    @Override
    public boolean processDone(ProcessDoneRequest request) {
        return oaTodoService.processDone(request);
    }

    @Override
    public boolean processOver(ProcessOverRequest request) {
        return oaTodoService.processOver(request);
    }
}
