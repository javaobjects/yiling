package com.yiling.open.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.yiling.framework.common.annotations.ErpLogAnnotation;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpOrderPushApi;
import com.yiling.open.erp.dto.ErpOrderPurchaseSendDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.dto.request.UpdateErpOrderPushRequest;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDeliveryErpApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderErpApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 采购发货单同步
 *
 * @author: houjie.sun
 * @date: 2021/9/10
 */
@Slf4j
@RestController
public class ErpOrderPurchaseSendController extends OpenBaseController {

    @DubboReference
    EnterpriseApi       enterpriseApi;
    @DubboReference
    OrderAddressApi     orderAddressApi;
    @DubboReference
    OrderErpApi         orderErpApi;
    @DubboReference
    OrderDetailApi      orderDetailApi;
    @DubboReference
    OrderDeliveryErpApi orderDeliveryErpApi;
    @DubboReference
    ErpClientApi        erpClientApi;
    @DubboReference
    OrderApi            orderApi;
    @DubboReference(timeout = 1000 * 60)
    ErpOrderPushApi     erpOrderPushApi;
    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @ErpLogAnnotation
    @PostMapping(path = "/send/push")
    public Result<String> sendPush(HttpServletRequest request, HttpServletResponse response) {
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

        JSONObject JsonBody = JSON.parseObject(body);
        if (CollectionUtils.isEmpty(JsonBody)) {
            return Result.failed(OpenErrorCode.Parameter_Body_Null.getCode(), OpenConstants.DATA_PARAM + "值为空");
        }

        List<ErpOrderPurchaseSendDTO> erpOrderList = null;
        try {
            //转换功能
            erpOrderList = erpOrderPushApi.getOrderPurchaseSendBySuId(suId, JsonBody.getInteger("pageSize"));
            log.debug("/purchase/push, erpOrderList -> {}", JSON.toJSONString(erpOrderList));
            //订单验证
            erpOrderList=erpOrderPushApi.verifyERPOrderPurchaseSendResult(erpOrderList);
        } catch (Exception e) {
            log.error("抽取工具获取订单接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        log.debug("/purchase/push, response -> {}", JSONArray.toJSONString(erpOrderList));
        return Result.success(JSONArray.toJSONString(erpOrderList));
    }


    @ErpLogAnnotation
    @PostMapping(path = "/send/back")
    public Result<String> sendBack(HttpServletRequest request, HttpServletResponse response) {
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

        try {
            List<Map<String, String>> paramList = JSON.parseObject(body, new TypeToken<List<Map<String, String>>>() {
            }.getType());

            List<UpdateErpOrderPushRequest> updateErpOrderRequest = new ArrayList<>();
            for (Map<String, String> param : paramList) {
                if (param.get("erp_sn") == null || StringUtils.isBlank(param.get("order_id"))) {
                    continue;
                }
                String orderId = param.get("order_id");
                UpdateErpOrderPushRequest updateOrderPurchaseRequest = new UpdateErpOrderPushRequest();
                updateOrderPurchaseRequest.setOrderId(Long.parseLong(orderId));
                updateOrderPurchaseRequest.setErpPushStatus(2);
                updateOrderPurchaseRequest.setPushType(3);
                updateErpOrderRequest.add(updateOrderPurchaseRequest);
            }
            if (!CollectionUtils.isEmpty(updateErpOrderRequest)) {
                erpOrderPushApi.updateErpStatusNotPushToReadSuccess(updateErpOrderRequest);
            }
        } catch (Exception e) {
            log.error("抽取工具回些订单状态接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success();
    }

    /*@ErpLogAnnotation
    @PostMapping(path = "/order/updateErpSn")
    public Result<String> updateErpSn(HttpServletRequest request, HttpServletResponse response) {
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
    
        JSONObject JsonBody = JSON.parseObject(body);
        if (CollectionUtils.isEmpty(JsonBody)) {
            return Result.failed(OpenErrorCode.Parameter_Body_Null.getCode(), OpenConstants.DATA_PARAM + "值为空");
        }
    
        try {
            if (StrUtil.isEmpty(JsonBody.getString("third_code")) || StrUtil.isEmpty(JsonBody.getString("order_id"))) {
                return Result.failed(OpenErrorCode.Remote_Service_Error);
            }
            String orderId = JsonBody.getString("order_id");
            String erpSn = JsonBody.getString("third_code");
    
            UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
            updateErpOrderDTO.setId(Long.parseLong(orderId));
            updateErpOrderDTO.setErpOrderNo(erpSn);
            updateErpOrderDTO.setErpPushStatus(4);
            Boolean bool = orderErpApi.updateExtractMessageByOrderId(updateErpOrderDTO);
            if (!bool) {
                return Result.failed(OpenErrorCode.Remote_Service_Error);
            }
        } catch (Exception e) {
            log.error("抽取工具回些订单状态接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success();
    }*/
}
