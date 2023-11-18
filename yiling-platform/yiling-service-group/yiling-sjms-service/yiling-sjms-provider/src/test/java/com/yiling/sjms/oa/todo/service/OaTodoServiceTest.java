package com.yiling.sjms.oa.todo.service;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.BaseTest;
import com.yiling.sjms.oa.todo.dto.request.ProcessDoneRequest;
import com.yiling.sjms.oa.todo.dto.request.ProcessOverRequest;
import com.yiling.sjms.oa.todo.dto.request.ReceiveTodoRequest;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2023/1/9
 */
@Slf4j
public class OaTodoServiceTest extends BaseTest {

    private static final String FLOW_ID = "1004";
    private static final String FORM_CODE = "TG20230109113200";
    private static final String TITLE = "团购提报-计井一-2023-01-09（TG20230109113200）";
    private static final String CREATOR = "YX02913";
    private static final String RECEIVER = "24154";

    @Autowired
    OaTodoService oaTodoService;

    @Test
    public void receiveTodo() {
        ReceiveTodoRequest request = new ReceiveTodoRequest();
        request.setBizId(FLOW_ID);
        request.setFormCode(FORM_CODE);
        request.setTitle(TITLE);
        request.setWorkflowName("团购提报");
        request.setPcUrl("https://sjms-api-dev.59yi.com/sjms/api/v1/sso/callback?redirectUrl=https%3A%2F%2Fsjms-dev.59yi.com%2F%23%2Fprocess_center%2Fprocess_center_agenda");
        request.setAppUrl("");
        request.setCreaterCode(CREATOR);
        request.setCreateTime(new Date());
        request.setReceiverCode(RECEIVER);
        request.setReceiveTime(new Date());

        Boolean result = oaTodoService.receiveTodo(request);
        log.info("result = {}", JSONUtil.toJsonStr(result));
    }

    @Test
    public void processDone() {
        ProcessDoneRequest request = new ProcessDoneRequest();
        request.setBizId(FLOW_ID);
        request.setTitle(TITLE);
        request.setWorkflowName("团购提报");
        request.setReceiverCode(RECEIVER);

        Boolean result = oaTodoService.processDone(request);
        log.info("result = {}", JSONUtil.toJsonStr(result));
    }

    @Test
    public void processOver() {
        ProcessOverRequest request = new ProcessOverRequest();
        request.setBizId(FLOW_ID);
        request.setTitle(TITLE);
        request.setWorkflowName("团购提报");
        request.setReceiverCode(CREATOR);

        Boolean result = oaTodoService.processOver(request);
        log.info("result = {}", JSONUtil.toJsonStr(result));
    }
}
