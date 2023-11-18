package com.yiling.sjms.oa.todo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.exception.ServiceException;
import com.yiling.sjms.config.OaConfig;
import com.yiling.sjms.form.entity.FormDO;
import com.yiling.sjms.form.service.FormService;
import com.yiling.sjms.oa.todo.dto.request.ProcessDoneRequest;
import com.yiling.sjms.oa.todo.dto.request.ProcessOverRequest;
import com.yiling.sjms.oa.todo.dto.request.ReceiveTodoRequest;
import com.yiling.sjms.oa.todo.feign.ApiResult;
import com.yiling.sjms.oa.todo.feign.OaTodoFeignClient;
import com.yiling.sjms.oa.todo.feign.request.ProcessDoneRequestByJsonRequest;
import com.yiling.sjms.oa.todo.feign.request.ProcessOverRequestByJsonRequest;
import com.yiling.sjms.oa.todo.feign.request.ReceiveTodoRequestByJsonRequest;
import com.yiling.sjms.oa.todo.service.OaTodoService;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * OA 统一待办 Service 实现
 *
 * @author: xuan.zhou
 * @date: 2023/1/6
 */
@Slf4j
@Service
public class OaTodoServiceImpl implements OaTodoService {

    public static final String SYS_CODE = "DIH";

    @Autowired
    OaConfig oaConfig;
    @Autowired
    OaTodoFeignClient oaTodoFeignClient;
    @Autowired
    FormService formService;

    @Override
    public Boolean receiveTodo(ReceiveTodoRequest request) {
        String formCode = request.getFormCode();
        FormDO formDO = formService.getByCode(formCode);
        if (formDO == null) {
            log.error("基础表单信息不存在 -> code={}", formCode);
            throw new ServiceException("基础表单信息不存在");
        }
        ReceiveTodoRequestByJsonRequest request1 = new ReceiveTodoRequestByJsonRequest();
        request1.setSyscode(SYS_CODE);
        request1.setFlowid(request.getBizId());
        request1.setRequestname(request.getTitle());
        request1.setWorkflowname(request.getWorkflowName());
        request1.setNodename(Convert.toStr(request.getNodeName(), "审批"));
        request1.setPcurl(request.getPcUrl());
        if (oaConfig.getApp().getTodoFormTypeCodes().contains(formDO.getType())) {
            request1.setAppurl(request.getAppUrl());
        }
        request1.setCreator(request.getCreaterCode());
        request1.setCreatedatetime(DateUtil.format(request.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN));
        request1.setReceiver(request.getReceiverCode());
        request1.setReceivedatetime(DateUtil.format(request.getCreateTime(), DatePattern.NORM_DATETIME_PATTERN));
        request1.setReceivets(String.valueOf(DateUtil.current(true)));

        log.info("接收待办流程参数：{}", JSONUtil.toJsonStr(request1));
        ApiResult result = oaTodoFeignClient.receiveTodoRequestByJson(request1);
        log.info("接收待办流程结果：{}", JSONUtil.toJsonStr(result));

        return result.isSuccessful();
    }

    @Override
    public Boolean processDone(ProcessDoneRequest request) {
        ProcessDoneRequestByJsonRequest request1 = new ProcessDoneRequestByJsonRequest();
        request1.setSyscode(SYS_CODE);
        request1.setFlowid(request.getBizId());
        request1.setRequestname(request.getTitle());
        request1.setWorkflowname(request.getWorkflowName());
        request1.setNodename(Convert.toStr(request.getNodeName(), "审批"));
        request1.setReceiver(request.getReceiverCode());
        request1.setReceivets(String.valueOf(DateUtil.current(true)));

        log.info("处理已办流程参数：{}", JSONUtil.toJsonStr(request1));
        ApiResult result = oaTodoFeignClient.processDoneRequestByJson(request1);
        log.info("处理已办流程结果：{}", JSONUtil.toJsonStr(result));

        return result.isSuccessful();
    }

    @Override
    public Boolean processOver(ProcessOverRequest request) {
        ProcessOverRequestByJsonRequest request1 = new ProcessOverRequestByJsonRequest();
        request1.setSyscode(SYS_CODE);
        request1.setFlowid(request.getBizId());
        request1.setRequestname(request.getTitle());
        request1.setWorkflowname(request.getWorkflowName());
        request1.setNodename(Convert.toStr(request.getNodeName(), "审批"));
        request1.setReceiver(request.getReceiverCode());
        request1.setReceivets(String.valueOf(DateUtil.current(true)));

        log.info("处理办结流程参数：{}", JSONUtil.toJsonStr(request1));
        ApiResult result = oaTodoFeignClient.processOverRequestByJson(request1);
        log.info("处理办结流程结果：{}", JSONUtil.toJsonStr(result));

        return result.isSuccessful();
    }
}
