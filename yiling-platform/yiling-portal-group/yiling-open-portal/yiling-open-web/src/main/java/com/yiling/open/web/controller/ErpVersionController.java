package com.yiling.open.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.api.ClientToolVersionApi;
import com.yiling.open.erp.dto.ClientToolVersionDTO;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@RestController
@RefreshScope
@Slf4j
public class ErpVersionController extends OpenBaseController {

    private final static Logger logger = LoggerFactory.getLogger(ErpVersionController.class);

    @DubboReference
    private ClientToolVersionApi clientToolVersionApi;

    @Value("${open.war.version:''}")
    private String version;
    @Value("${open.war.name:''}")
    private String name;
    @Value("${open.war.description:''}")
    private String description;

    @PostMapping(path = "/tool/version")
    public Result<String> getVersion(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        ErpClientDTO erpClient = getOauthClientDetailByAppKey(params.get(OpenConstants.APP_KEY));
        if (erpClient == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(),
                    "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(),
                    "参数" + OpenConstants.METHOD + "不存在");
        }

        String body = getDataRequest(request);
        if (StringUtils.isBlank(body)) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(),
                    "参数" + OpenConstants.DATA_PARAM + "不存在");
        }

        try {
            String packageUrl = "https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/erp/war/".concat(version).concat("/yiling-client-web.war");
            ClientToolVersionDTO clientToolVersion = new ClientToolVersionDTO();
            clientToolVersion.setPackageUrl(packageUrl);
            clientToolVersion.setVersion(version);
            clientToolVersion.setName(name);
            clientToolVersion.setDescription(description);
            if (clientToolVersion != null) {
                return Result.success(JSONObject.toJSONString(clientToolVersion));
            }
        } catch (Exception e) {
            log.error("抽取工具获取版本信息接口报错", e);
            e.printStackTrace();
        }
        return Result.failed("抽取工具获取版本信息失败");
    }
}
