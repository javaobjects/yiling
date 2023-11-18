package com.yiling.open.web.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@RestController
@Slf4j
public class OpenApiIndexController extends OpenBaseController {

    @ApiOperation(value = "erp对接工具对外接口")
    @RequestMapping(path = "/router/rest")
    public Result<String> restIndex(HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {

        Map<String, String> allRequestParams = getRequestParams(request);
        //错误集合
        Result<String> errorResponse = null;
        // 1. 验证公共请求参数是否为空
        errorResponse = this.checkSysParams(allRequestParams);
        if (errorResponse != null) {
            log.warn("公共请求参数为空allRequestParams="+JSON.toJSONString(allRequestParams));
            return errorResponse;
        }

        // 2. 验证appKey有效性
        String appKey = allRequestParams.get(OpenConstants.APP_KEY);
        ErpClientDTO erpClient = this.getOauthClientDetailByAppKey(appKey);
        if (erpClient == null) {
//            log.warn("appKey不存在或者已经停用allRequestParams="+JSON.toJSONString(allRequestParams));
            return Result.failed(OpenErrorCode.Invalid_AppKey);
        }

        String appSecret = erpClient.getClientSecret();
        if (StrUtil.isEmpty(appSecret)) {
            log.error("appSecret不正确allRequestParams="+JSON.toJSONString(allRequestParams));
            return Result.failed(OpenErrorCode.Invalid_AppSecret);
        }

        // 3.验证签名是否有效
        boolean checkSign = validateSign(allRequestParams, appSecret);
        if (!checkSign) {
            log.error("checkSign不正确allRequestParams="+JSON.toJSONString(allRequestParams));
            return Result.failed(OpenErrorCode.Invalid_Signature);
        }

        //4.验证任务方式是否有效
        String method = allRequestParams.get(OpenConstants.METHOD);
        ErpTaskInterfaceDTO erpTaskInterface=getInterfaceByTaskNo(method);
        if (erpTaskInterface == null) {
            log.error("method参数不正确allRequestParams="+JSON.toJSONString(allRequestParams));
            return Result.failed(OpenErrorCode.Invalid_Method);
        }
        //5.限流
        if(!method.equals(ErpTopicName.RedisCommandGet.getMethod())) {
            boolean bool = rateLimiterUtil.isAllowExecution(erpClient.getRkSuId() + "_" + method, 100, 60, TimeUnit.SECONDS);
            if (!bool) {
                log.warn("数据请求次数频繁allRequestParams=" + JSON.toJSONString(allRequestParams));
                return Result.failed(OpenErrorCode.Limit_Request);
            }
        }

        String mappingUri = erpTaskInterface.getMappingUri();
        request.getRequestDispatcher(mappingUri).forward(request, response);
        return null;
    }

    /**
     * 验证API HTTP请求参数
     * @param requestMap
     * @return
     */
    public Result<String> checkSysParams(Map<String, String> requestMap) {

        log.debug("http传入的参数为："+ JSON.toJSONString(requestMap));
        String subMsg = null;
        String method = requestMap.get(OpenConstants.METHOD);
        if (StrUtil.isEmpty(method)) {
            subMsg = "http传入的参数需加入" + OpenConstants.METHOD + "字段";
            return Result.failed(OpenErrorCode.Missing_Method.getCode(), subMsg);
        }

        String appKey = requestMap.get(OpenConstants.APP_KEY);
        if (StrUtil.isEmpty(appKey)) {
            subMsg = "http传入的参数需加入" + OpenConstants.APP_KEY + "字段";
            return Result.failed(OpenErrorCode.Missing_AppKey.getCode(), subMsg);
        }

        String timestamp = requestMap.get(OpenConstants.TIMESTAMP);
        if (StrUtil.isEmpty(timestamp)) {
            subMsg = "http传入的参数需加入" + OpenConstants.TIMESTAMP + "字段";
            return Result.failed(OpenErrorCode.Missing_Timestamp.getCode(), subMsg);
        } else {
            long requestDate=Long.parseLong(timestamp);
                Date currentDate = Calendar.getInstance().getTime();
                if (Math.abs(requestDate - currentDate.getTime()) > (1000 * 60 * 60 *24)) {
                    subMsg = OpenConstants.TIMESTAMP + "字段的值与服务器时间相差为24小时";
                    return Result.failed(OpenErrorCode.Invalid_Timestamp.getCode(), subMsg);
                }
        }

        String version = requestMap.get(OpenConstants.VERSION);
        if (StrUtil.isEmpty(version)) {
            subMsg = "http传入的参数加入" + OpenConstants.VERSION + "字段";
            return Result.failed(OpenErrorCode.Missing_Version.getCode(), subMsg);
        } else if (!version.equals(OpenConstants.SDK_VERSION)) {
            subMsg = OpenConstants.VERSION + "字段的值只能为" + OpenConstants.SDK_VERSION;
            return Result.failed(OpenErrorCode.Invalid_Version.getCode(), subMsg);
        }

        String sign = requestMap.get(OpenConstants.SIGN);
        if (StrUtil.isEmpty(sign)) {
            subMsg = "http传入的参数加入" + OpenConstants.SIGN + "字段";
            return Result.failed(OpenErrorCode.Missing_Signature.getCode(), subMsg);
        }

        return null;
    }

}
