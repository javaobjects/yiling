package com.yiling.open.web.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpVerifyDataApi;
import com.yiling.open.erp.dto.ClientRequestDTO;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.dto.request.ErpClientSaveRequest;
import com.yiling.open.erp.enums.CommandStatusEnum;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.web.redis.factory.RedisServiceFactory;
import com.yiling.open.web.redis.service.RedisService;
import com.yiling.open.web.redis.util.RedisKey;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@RestController
@Slf4j
public class ErpCommandController extends OpenBaseController {

    @DubboReference
    public ErpClientApi erpClientApi;

    @DubboReference
    public ErpVerifyDataApi erpVerifyDataApi;

    private static final RedisService redisService = RedisServiceFactory.getRedisService();

    @PostMapping(path = "/command/getRedis")
    public Result<String> getRedis(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        ErpClientDTO result = this.getOauthClientDetailByAppKey(params.get(OpenConstants.APP_KEY));
        if (result == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(), "参数" + OpenConstants.METHOD + "不存在");
        }
        //命令心跳
        redisService.set(RedisKey.generate("heart", result.getSuId() + ""), DateUtil.formatDateTime(new Date()));

        if(result.getCommandStatus()!=null&&result.getCommandStatus()==1){
            Long time=System.currentTimeMillis()-result.getCommandTime().getTime();
            if(time>60*60*1000){
                ErpClientSaveRequest erpClientSaveRequest=new ErpClientSaveRequest();
                erpClientSaveRequest.setSuId(result.getSuId());
                erpClientSaveRequest.setCommandStatus(0);
                erpClientSaveRequest.setCommandTime(new Date());
                erpClientApi.updateCommandBySuId(erpClientSaveRequest);
            }
        }else{
            ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
            clientRequestDTO.setSuId(result.getSuId());
            clientRequestDTO.setMessage("没有任务命令休息");
            clientRequestDTO.setStatus(0);
            return Result.success(JSONObject.toJSONString(clientRequestDTO));
        }

        //通知自己的状态
        Map<String, String> map = redisService.hgetAll(RedisKey.generate("erp", result.getSuId() + ""));
        if (MapUtil.isEmpty(map)) {
            return Result.failed("没有任务");
        }
        if (!map.get(OpenConstants.status_flag).equals(CommandStatusEnum.ser_send.getCode())) {
            return Result.failed("没有任务");
        }
        String task = map.get(OpenConstants.task_flag);
        if (StrUtil.isEmpty(task)) {
            return Result.failed("没有任务");
        }
        ClientRequestDTO clientRequestDTO = JSON.parseObject(task, ClientRequestDTO.class);
        return Result.success(JSONObject.toJSONString(clientRequestDTO));
    }

    @PostMapping(path = "/command/updateRedis")
    public Result<String> updateRedis(HttpServletRequest request, HttpServletResponse response) {
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
        if (StringUtils.isBlank(body)) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + OpenConstants.DATA_PARAM + "不存在");
        }

        Map<String,String> map=JSONObject.parseObject(body,Map.class);
        redisService.hmset(RedisKey.generate("erp", result.getSuId() + ""), map);
        redisService.expire(RedisKey.generate("erp", result.getSuId() + ""), 30);
        return Result.success();
    }

    @PostMapping(path = "/command/redisLog")
    public Result<String> redisLog(HttpServletRequest request, HttpServletResponse response) {
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
        if (StringUtils.isBlank(body)) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + OpenConstants.DATA_PARAM + "不存在");
        }
        redisService.lpush(RedisKey.generate("update_log", result.getSuId() + ""),body);
        return Result.success();
    }

    @Deprecated
    @PostMapping(path = "/tool/command")
    public Result<String> findCommand(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        ErpClientDTO result =this.getOauthClientDetailByAppKey(params.get(OpenConstants.APP_KEY));
        if (result == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(), "参数" + OpenConstants.METHOD + "不存在");
        }
        return Result.success(String.valueOf(result.getCommand()));
    }

    @Deprecated
    @PostMapping(path = "/tool/commandback")
    public Result<String> commandback(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        ErpClientDTO result = this.getOauthClientDetailByAppKey(params.get(OpenConstants.APP_KEY));
        if (result == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(), "参数" + OpenConstants.METHOD + "不存在");
        }

        try {
            ErpClientSaveRequest erpClientSaveRequest = new ErpClientSaveRequest();
            erpClientSaveRequest.setSuId(result.getSuId());
            erpClientSaveRequest.setCommand(0);
            erpClientApi.updateCommandBySuId(erpClientSaveRequest);
            return Result.success();
        } catch (Exception e) {
            log.error("[ErpCommandController][commandback] 异常！" + e.getMessage(), e);
            return Result.failed(e.getMessage());
        }
    }

}
