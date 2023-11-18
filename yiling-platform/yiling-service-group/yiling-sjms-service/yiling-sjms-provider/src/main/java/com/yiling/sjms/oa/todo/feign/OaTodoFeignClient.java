package com.yiling.sjms.oa.todo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.yiling.sjms.oa.todo.feign.request.ProcessDoneRequestByJsonRequest;
import com.yiling.sjms.oa.todo.feign.request.ProcessOverRequestByJsonRequest;
import com.yiling.sjms.oa.todo.feign.request.ReceiveTodoRequestByJsonRequest;

/**
 * OA 统一待办接口调用 <br/>
 * 基于《统一待办中心集成数据接口说明v5——E9版》<br/>
 *
 * @author: xuan.zhou
 * @date: 2023/1/6
 */
@FeignClient(name = "oaTodoFeignClient", url = "${oa.service.baseUrl}")
public interface OaTodoFeignClient {

    /**
     * 2.2.5、接收待办流程(json格式)
     *
     * @param request 请求参数
     * @return com.yiling.sjms.oa.todo.feign.ApiResult
     * @author xuan.zhou
     * @date 2023/1/6
     **/
    @PostMapping("/rest/ofs/ReceiveTodoRequestByJson")
    ApiResult receiveTodoRequestByJson(@RequestBody ReceiveTodoRequestByJsonRequest request);

    /**
     * 2.2.6、处理待办流程（变为已办）(json格式)
     *
     * @param request 请求参数
     * @return com.yiling.sjms.oa.todo.feign.ApiResult
     * @author xuan.zhou
     * @date 2023/1/6
     **/
    @PostMapping("/rest/ofs/ProcessDoneRequestByJson")
    ApiResult processDoneRequestByJson(@RequestBody ProcessDoneRequestByJsonRequest request);

    /**
     * 2.2.7、处理办结流程（变为办结）(json格式)
     *
     * @param request 请求参数
     * @return com.yiling.sjms.oa.todo.feign.ApiResult
     * @author xuan.zhou
     * @date 2023/1/6
     **/
    @PostMapping("/rest/ofs/ProcessOverRequestByJson")
    ApiResult processOverRequestByJson(@RequestBody ProcessOverRequestByJsonRequest request);
}
