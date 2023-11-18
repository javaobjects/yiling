package com.yiling.open.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.yiling.framework.common.annotations.ErpLogAnnotation;
import com.yiling.framework.common.pojo.Result;
import com.yiling.open.erp.api.ErpDeleteDataApi;
import com.yiling.open.erp.dto.ErpDeleteDataDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.dto.request.SaveErpDeleteDataRequest;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: shuang.zhang
 * @Email: shuang.zhang@rograndec.com
 * @CreateTime: 2019-3-6
 * @Version: 1.0
 */
@RestController
@Slf4j
public class ErpDeleteDataController extends OpenBaseController {

    @DubboReference
    private ErpDeleteDataApi erpDeleteDataApi;

    /**
     * mop.seller.product.sync(商品信息同步)
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ErpLogAnnotation
    @PostMapping(path = "/delete/push")
    public Result<String> deletePush(HttpServletRequest request,
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
            JSONObject jsonObject = JSONObject.parseObject(body);
            String taskNo = jsonObject.getString("taskNo");
            if (taskNo!=null) {
                SaveErpDeleteDataRequest request1 = new SaveErpDeleteDataRequest();
                request1.setSuId(suId);
                request1.setTaskNo(ErpTopicName.ErpOrderBill.getMethod());
                request1.setStatus(0);
                List<ErpDeleteDataDTO> erpDeleteDataList = erpDeleteDataApi.findErpDeleteBySuIdAndTaskNo(suId, taskNo);
                return Result.success(JSONArray.toJSONString(erpDeleteDataList));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.failed(OpenErrorCode.ERP_SYNC_ERROR);
    }

    @ErpLogAnnotation
    @PostMapping(path = "/delete/back")
    public Result<String> deleteBack(HttpServletRequest request, HttpServletResponse response) {
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
            List<Map<String, String>> paramList = JSON.parseObject(body, new TypeToken<List<Map<String, String>>>() {
            }.getType());

            for (Map<String, String> param : paramList) {
                if (StringUtils.isBlank(param.get("erp_sn"))|| StringUtils.isBlank(param.get("order_id"))) {
                    continue;
                }
                String orderId = param.get("order_id");
                String erpSn = param.get("erp_sn");
                SaveErpDeleteDataRequest saveErpDeleteDataRequest = new SaveErpDeleteDataRequest();
                saveErpDeleteDataRequest.setDataId(orderId);
                saveErpDeleteDataRequest.setTaskNo(erpSn);
                saveErpDeleteDataRequest.setSuId(suId);
                erpDeleteDataApi.updateErpDeleteData(saveErpDeleteDataRequest);
            }
        } catch (Exception e) {
            log.error("抽取工具回些订单状态接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success();
    }
}
