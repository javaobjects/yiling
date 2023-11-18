package com.yiling.open.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.annotations.ErpLogAnnotation;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpOrderPushApi;
import com.yiling.open.erp.dto.EasNotificationDTO;
import com.yiling.open.erp.dto.ErpOrderDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.dto.request.UpdateErpOrderPushRequest;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderErpApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuan
 */
@RestController
@Slf4j
public class ErpOrderController extends OpenBaseController {

    @DubboReference
    OrderApi             orderApi;
    @DubboReference
    OrderErpApi          orderErpApi;
    @DubboReference
    EnterpriseApi        enterpriseApi;
    @DubboReference
    OrderAddressApi      orderAddressApi;
    @DubboReference(timeout = 1000 * 60 * 5)
    ErpOrderPushApi      erpOrderPushApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    ErpClientApi         erpClientApi;
    @DubboReference
    OrderDetailApi       orderDetailApi;
    @DubboReference
    MqMessageSendApi     mqMessageSendApi;

    @ErpLogAnnotation
    @PostMapping(path = "/order/push")
    public Result<String> orderPush(HttpServletRequest request, HttpServletResponse response) {
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

        List<ErpOrderDTO> erpOrderList = null;
        try {
            //转换功能
            erpOrderList = erpOrderPushApi.getOrderSaleBySuId(suId, JsonBody.getInteger("pageSize"));
            //订单验证
            erpOrderList=erpOrderPushApi.verifyERPOrderSaleResult(erpOrderList);
        } catch (Exception e) {
            log.error("抽取工具获取订单接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success(JSONArray.toJSONString(erpOrderList));
    }


    @ErpLogAnnotation
    @PostMapping(path = "/order/back")
    public Result<String> orderBack(HttpServletRequest request, HttpServletResponse response) {
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
                UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
                updateErpOrderDTO.setOrderId(Long.parseLong(orderId));
                updateErpOrderDTO.setPushType(1);
                updateErpOrderDTO.setErpPushStatus(2);
                updateErpOrderRequest.add(updateErpOrderDTO);
            }
            if (!CollectionUtils.isEmpty(updateErpOrderRequest)) {
                erpOrderPushApi.updateErpStatusNotPushToReadSuccess(updateErpOrderRequest);
                if (suId.equals(Constants.YILING_EID)) {
                    for (UpdateErpOrderPushRequest updateErpOrderDTO : updateErpOrderRequest) {
                        //发送mq信息通知对方提取
                        EasNotificationDTO easNotificationDTO = new EasNotificationDTO();
                        easNotificationDTO.setType("1");
                        easNotificationDTO.setJson(String.valueOf(updateErpOrderDTO.getOrderId()));
                        easNotificationDTO.setOrderId(String.valueOf(updateErpOrderDTO.getOrderId()));
                        MqMessageBO mqMessageBO = mqMessageSendApi.prepare(new MqMessageBO("eas_notification", "1", JSON.toJSONString(easNotificationDTO)));
                        mqMessageSendApi.send(mqMessageBO);
                    }
                }
            }
        } catch (Exception e) {
            log.error("抽取工具回些订单状态接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success();
    }

    @ErpLogAnnotation
    @PostMapping(path = "/order/updateErpSn")
    public Result<String> updateErpSn(HttpServletRequest request, HttpServletResponse response) {
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

        try {
            if (StrUtil.isEmpty(JsonBody.getString("third_code")) || StrUtil.isEmpty(JsonBody.getString("order_id"))) {
                return Result.failed(OpenErrorCode.Remote_Service_Error);
            }
            String orderId = JsonBody.getString("order_id");
            String erpSn = JsonBody.getString("third_code");

            OrderDTO orderDTO = orderApi.getOrderInfo(Long.parseLong(orderId));
            if (StrUtil.isEmpty(orderDTO.getErpOrderNo())) {
                UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
                updateErpOrderDTO.setOrderId(Long.parseLong(orderId));
                updateErpOrderDTO.setPushType(1);
                updateErpOrderDTO.setErpPushStatus(4);
                updateErpOrderDTO.setErpOrderNo(erpSn);
                Boolean bool = erpOrderPushApi.updateExtractByOrderId(updateErpOrderDTO);
                if (!bool) {
                    return Result.failed(OpenErrorCode.Remote_Service_Error);
                }
            }
        } catch (Exception e) {
            log.error("抽取工具回些订单状态接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success();
    }

    @ErpLogAnnotation
    @PostMapping(path = "/order/updateSaleNo")
    public Result<String> updateSaleNo(HttpServletRequest request, HttpServletResponse response) {
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
            if (StrUtil.isEmpty(JsonBody.getString("sale_no")) || StrUtil.isEmpty(JsonBody.getString("order_id"))) {
                return Result.failed(OpenErrorCode.Remote_Service_Error);
            }
            String orderId = JsonBody.getString("order_id");
            String saleNo = JsonBody.getString("sale_no");

            OrderDTO orderDTO = orderApi.getOrderInfo(Long.parseLong(orderId));
            if (StrUtil.isEmpty(orderDTO.getErpOrderNo())) {
                UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
                updateErpOrderDTO.setOrderId(Long.parseLong(orderId));
                updateErpOrderDTO.setPushType(1);
                updateErpOrderDTO.setErpFlowNo(saleNo);
                Boolean bool = erpOrderPushApi.updateExtractMessageByOrderId(updateErpOrderDTO);
                if (!bool) {
                    return Result.failed(OpenErrorCode.Remote_Service_Error);
                }
            }
        } catch (Exception e) {
            log.error("抽取工具回些订单状态接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success();
    }
}
