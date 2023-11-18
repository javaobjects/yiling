package com.yiling.open.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpHeartApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.dto.HeartParamDTO;
import com.yiling.open.erp.dto.SysHeartBeatDTO;
import com.yiling.open.erp.dto.request.UpdateHeartBeatTimeRequest;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@RestController
@Slf4j
public class ErpHeartController extends OpenBaseController {

    @DubboReference
    private ErpHeartApi erpHeartApi;
    @DubboReference
    private ErpClientApi erpClientApi;
    @Value("${env.name}")
    private String envName;
    @Value("${aliyun.oss.endpoint:xxxxx}")
    private String endpoint;
    @Value("${aliyun.oss.protocol:http}")
    private String protocol;
    @Value("${aliyun.oss.domain:xxxxx}")
    private String domain;
    @Value("${aliyun.oss.url-expires:xxxxx}")
    private Long expires;
    @Value("${aliyun.oss.access-key:xxxxx}")
    private String accessKey;
    @Value("${aliyun.oss.accessKeySecret:xxxxx}")
    private String accessKeySecret;

    @PostMapping(path = "/sys/heart")
    public Result<String> sysHeart(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        Long suId = getSuIdByAppKey(params.get(OpenConstants.APP_KEY));
        if (suId == null) {
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

        ErpClientDTO erpClientDTO = getOauthClientDetailByAppKey(params.get(OpenConstants.APP_KEY));

        SysHeartBeatDTO sysHeartBeat;
        String encodeJson = "";
        try {
            HeartParamDTO heartParamDTO = new HeartParamDTO();
            heartParamDTO.setDomain(domain);
            heartParamDTO.setEndpoint(endpoint);
            heartParamDTO.setEnvName(envName);
            heartParamDTO.setExpires(expires);
            heartParamDTO.setProtocol(protocol);
            heartParamDTO.setAccessKey(accessKey);
            heartParamDTO.setAccessKeySecret(accessKeySecret);
            heartParamDTO.setRedisAddr("redis1.prd.yl.local:25001,redis2.prd.yl.local:25001");
            heartParamDTO.setRedisPassWord("0PmInwufGTIV8h4PmoyG");
            String ossJson = JSON.toJSONString(heartParamDTO);
            encodeJson = Base64.encode(ossJson);

            sysHeartBeat = JSON.parseObject(body, SysHeartBeatDTO.class);
            sysHeartBeat.setSuId(erpClientDTO.getSuId());
            sysHeartBeat.setSuName(erpClientDTO.getClientName());
            erpHeartApi.insertErpHeart(sysHeartBeat);
            // 记录心跳最后时间
            UpdateHeartBeatTimeRequest erpClientRequest = new UpdateHeartBeatTimeRequest();
            erpClientRequest.setSuId(erpClientDTO.getSuId());
            erpClientRequest.setOpUserId(0L);
            erpClientRequest.setVersions(sysHeartBeat.getVersions());
            erpClientApi.updateHeartBeatTimeBySuid(erpClientRequest);
        } catch (Exception e) {
            log.error("心跳信息插入失败", e);
        }
        return Result.success(erpClientDTO.getClientName().concat(",").concat(erpClientDTO.getSuId().toString()).concat(",").concat(envName).concat(",").concat(encodeJson));
    }
}
