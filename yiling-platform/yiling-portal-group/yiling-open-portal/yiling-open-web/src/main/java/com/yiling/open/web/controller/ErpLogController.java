package com.yiling.open.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/12/7
 */
@RestController
@Slf4j
public class ErpLogController extends OpenBaseController {

    @Value("${env.name}")
    private String envName;

    /**
     * mop.seller.product.sync(商品信息同步)
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @PostMapping(path = "/erp/log")
    public Result<String> erpLog(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = getRequestParams(request);
        ErpClientDTO erpClientDTO = getOauthClientDetailByAppKey(params.get(OpenConstants.APP_KEY));
        if (erpClientDTO == null) {
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
        if (envName.equals("prd")) {
            log.info("[以岭对接工具]-[{}]-[{}]-[{}]:日志{}", erpClientDTO.getSuId(), erpClientDTO.getClientName(), erpClientDTO.getInstallEmployee(), body);
        }
        return Result.success();
    }

}
