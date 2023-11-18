package com.yiling.sjms.oa.todo.feign;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.BaseTest;
import com.yiling.sjms.oa.todo.feign.request.ProcessDoneRequestByJsonRequest;
import com.yiling.sjms.oa.todo.feign.request.ProcessOverRequestByJsonRequest;
import com.yiling.sjms.oa.todo.feign.request.ReceiveTodoRequestByJsonRequest;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2023/1/6
 */
@Slf4j
public class OaTodoFeignClientTest extends BaseTest {

    private static final String FLOW_ID = "1003";
    private static final String TITLE = "团购提报-计井一-2023-01-09（TG20230109105300）";
    private static final String CREATOR = "YX02913";
    private static final String RECEIVER = "24154";

    @Autowired
    OaTodoFeignClient oaTodoFeignClient;

    @Test
    public void receiveTodoRequestByJson() {
        ReceiveTodoRequestByJsonRequest request = new ReceiveTodoRequestByJsonRequest();
        request.setSyscode("DIH");
        request.setFlowid(FLOW_ID);
        request.setRequestname(TITLE);
        request.setWorkflowname("团购提报");
        request.setNodename("待办");
        request.setPcurl("https://sjms-api-dev.59yi.com/sjms/api/v1/sso/callback?redirectUrl=https%3A%2F%2Fsjms-dev.59yi.com%2F%23%2Fprocess_center%2Fprocess_center_agenda");
        request.setAppurl("");
        request.setCreator(CREATOR);
        request.setCreatedatetime("2023-01-09 09:48:21");
        request.setReceiver(RECEIVER);
        request.setReceivedatetime("2023-01-09 09:11:03");
        request.setReceivets("" + DateUtil.current(true));

        ApiResult result = oaTodoFeignClient.receiveTodoRequestByJson(request);
        log.info("result = {}", JSONUtil.toJsonStr(result));
    }
    
    @Test
    public void processDoneRequestByJson() {
        ProcessDoneRequestByJsonRequest request = new ProcessDoneRequestByJsonRequest();
        request.setSyscode("DIH");
        request.setFlowid(FLOW_ID);
        request.setRequestname(TITLE);
        request.setWorkflowname("团购提报");
        request.setNodename("已办");
        request.setReceiver(RECEIVER);
        request.setReceivets("" + DateUtil.current(true));

        ApiResult result = oaTodoFeignClient.processDoneRequestByJson(request);
        log.info("result = {}", JSONUtil.toJsonStr(result));
    }

    @Test
    public void processOverRequestByJson() {
        ProcessOverRequestByJsonRequest request = new ProcessOverRequestByJsonRequest();
        request.setSyscode("DIH");
        request.setFlowid(FLOW_ID);
        request.setRequestname(TITLE);
        request.setWorkflowname("团购提报");
        request.setNodename("办结");
        request.setReceiver(CREATOR);
        request.setReceivets("" + DateUtil.current(true));

        ApiResult result = oaTodoFeignClient.processOverRequestByJson(request);
        log.info("result = {}", JSONUtil.toJsonStr(result));
    }
}
