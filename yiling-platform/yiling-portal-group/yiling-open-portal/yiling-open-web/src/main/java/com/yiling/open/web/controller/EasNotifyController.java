package com.yiling.open.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.framework.common.annotations.ErpLogAnnotation;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.api.EasNotificationApi;
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
public class EasNotifyController extends OpenBaseController {

    @DubboReference
    private EasNotificationApi easNotificationApi;

    /**
     * mop.seller.product.sync(商品信息同步)
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ErpLogAnnotation
    @PostMapping(path = "/eas/notify")
    public Result<String> easNotify(HttpServletRequest request,
                                    HttpServletResponse response
    ) {
        Map<String, String> params = getRequestParams(request);
        Long suId = getSuIdByAppKey(params.get(OpenConstants.APP_KEY));
        if (suId == null) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "商业公司不存在");
        }

        ErpTaskInterfaceDTO erpTaskInterface = getInterfaceByTaskNo(params.get(OpenConstants.METHOD));
        if (erpTaskInterface == null) {
            return Result.failed(OpenErrorCode.Invalid_Method.getCode(),
                    "参数" + OpenConstants.METHOD + "不存在");
        }

        String body = getDataRequest(request);
        if (StringUtils.isBlank(body)) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + OpenConstants.DATA_PARAM + "不存在");
        }

        try {
            boolean bool = easNotificationApi.executeEas(body);
            if (bool) {
                return Result.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(OpenErrorCode.ERP_SYNC_ERROR.getCode(), e.getMessage());
        }
        return Result.failed(OpenErrorCode.ERP_SYNC_ERROR);
    }

}
