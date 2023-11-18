package com.yiling.open.web.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiling.bi.http.dto.request.FineHttpRequest;
import com.yiling.bi.http.enums.FIneRequestCodeEnum;
import com.yiling.bi.resource.api.UploadResourceApi;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpVerifyDataApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.web.redis.factory.RedisServiceFactory;
import com.yiling.open.web.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuan
 */
@RestController
@Slf4j
public class FineHttpRequestController extends OpenBaseController {

    @DubboReference
    public ErpClientApi erpClientApi;

    @DubboReference
    public ErpVerifyDataApi erpVerifyDataApi;

    @DubboReference
    private UploadResourceApi uploadResourceApi;

    private static final RedisService redisService = RedisServiceFactory.getRedisService();

    @PostMapping(path = "/fine/ask")
    public Result<String> fineAsk(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        ErpClientDTO result = this.getOauthClientDetailByAppKey(params.get(OpenConstants.APP_KEY));
        if (result == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(), "参数" + OpenConstants.METHOD + "不存在");
        }
        String body = getDataRequest(request);
        log.info("请求的参数"+body);
        if (StringUtils.isBlank(body)) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + OpenConstants.DATA_PARAM + "不存在");
        }

        FineHttpRequest fineHttpRequest = JSON.parseObject(body, FineHttpRequest.class);
        if (fineHttpRequest == null) {
            return Result.failed(OpenErrorCode.Parameter_Body_Null.getCode(), OpenConstants.DATA_PARAM + "值为空");
        }

        String returnBody="";
        try {
            switch (FIneRequestCodeEnum.getFromCode(fineHttpRequest.getFineRequestCode())){
                case ANALYSIS_EXCEL:
                    returnBody=uploadResourceApi.importInputLsflRecord(fineHttpRequest.getRequestBody());
                    break;
                case REMATCH_B2B_ORDER:
                case REQUEST_TEST:
                    returnBody="测试成功";
                    break;
                default:
            }
            fineHttpRequest.setResponseBody(returnBody);
            fineHttpRequest.setStatus(200);
            fineHttpRequest.setResponseTime(new Date());
        }catch (Exception e){
            fineHttpRequest.setStatus(500);
            fineHttpRequest.setResponseBody(ExceptionUtil.stacktraceToOneLineString(e));
            fineHttpRequest.setResponseTime(new Date());
        }
        return Result.success(JSONObject.toJSONString(fineHttpRequest));
    }




}
