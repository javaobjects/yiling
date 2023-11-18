package com.yiling.open.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.reflect.TypeToken;
import com.yiling.framework.common.annotations.ErpLogAnnotation;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpOrderPushApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpOrderPurchaseDTO;
import com.yiling.open.erp.dto.ErpOrderPurchaseDetailDTO;
import com.yiling.open.erp.dto.ErpOrderPushDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.dto.request.OrderPushErpBuyerEidPageRequest;
import com.yiling.open.erp.dto.request.UpdateErpOrderPushRequest;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.validation.ValidationUtils;
import com.yiling.open.erp.validation.entity.ValidationResult;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderErpApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 采购订单同步
 *
 * @author: houjie.sun
 * @date: 2021/9/10
 */
@Slf4j
@RestController
public class ErpOrderPurchaseController extends OpenBaseController {

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    ErpOrderPushApi erpOrderPushApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderAddressApi orderAddressApi;
    @DubboReference
    OrderErpApi orderErpApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    ErpClientApi erpClientApi;
    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @ErpLogAnnotation
    @PostMapping(path = "/purchase/push")
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

        List<ErpOrderPurchaseDTO> erpOrderList = new ArrayList<>();
        try {
            //转换功能
            composeERPOrderResult(suId, erpOrderList, JsonBody.getInteger("pageSize"));
            log.debug("/purchase/push, erpOrderList -> {}", JSON.toJSONString(erpOrderList));
            //订单验证
            verifyERPOrderResult(erpOrderList);
        } catch (Exception e) {
            log.error("抽取工具获取订单接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        log.debug("/purchase/push, response -> {}", JSONArray.toJSONString(erpOrderList));
        return Result.success(JSONArray.toJSONString(erpOrderList));
    }

    private void composeERPOrderResult(Long suId, List<ErpOrderPurchaseDTO> erpOrderList, Integer pageSize) {
        OrderPushErpBuyerEidPageRequest erpRequest = new OrderPushErpBuyerEidPageRequest();
        erpRequest.setSize(pageSize);
        erpRequest.setPushType(2);
        erpRequest.setStartCreateTime(DateUtil.offsetMonth(DateUtil.parse(DateUtil.today()), -3));
        if (Constants.YILING_EID.equals(suId)) {
            List<Long> list = enterpriseApi.listSubEids(suId);
            erpRequest.setBuyerEids(list);
        } else {
            List<ErpClientDTO> erpClientDTOList = this.selectBySuId(suId);
            erpRequest.setBuyerEids(erpClientDTOList.stream().map(e -> e.getRkSuId()).collect(Collectors.toList()));
        }
        // 订单
        Page<ErpOrderPushDTO> orderPage = erpOrderPushApi.getErpPushBuyerOrder(erpRequest);
        if (orderPage != null && CollectionUtil.isNotEmpty(orderPage.getRecords())) {
            List<Long> idList = orderPage.getRecords().stream().map(o -> o.getOrderId()).collect(Collectors.toList());
            log.debug("/purchase/push, idList -> {}", idList);
            Map<Long, List<OrderDetailChangeDTO>> detailMap = new HashMap<>();
            // 订单明细变更信息
            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(idList);

            for (OrderDetailChangeDTO one : orderDetailChangeList) {
                if (detailMap.containsKey(one.getOrderId())) {
                    List<OrderDetailChangeDTO> orderDetailList = detailMap.get(one.getOrderId());
                    orderDetailList.add(one);
                } else {
                    detailMap.put(one.getOrderId(), new ArrayList<OrderDetailChangeDTO>() {{
                        add(one);
                    }});
                }
            }
            // 商品内码
            Map<Long, String> map = getGoodsInfoYlGoodsIdAndEid(erpRequest.getBuyerEids(), orderDetailChangeList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList()));
            log.debug("/purchase/push, goodsInSn -> {}", JSON.toJSONString(map));

            // 订单收货地址
            List<OrderAddressDTO> orderAddressList = orderAddressApi.getOrderAddressList(idList);
            Map<Long, OrderAddressDTO> addressMap = orderAddressList.stream().collect(Collectors.toMap(OrderAddressDTO::getOrderId, o -> o, (k1, k2) -> k1));
            List<OrderDTO> orderDTOList = orderApi.listByIds(idList);
            Map<Long, OrderDTO> orderDTOMap = orderDTOList.stream().collect(Collectors.toMap(OrderDTO::getId, Function.identity()));

            for (ErpOrderPushDTO one : orderPage.getRecords()) {
                List<OrderDetailChangeDTO> orderDetailList = detailMap.get(one.getOrderId());
                OrderDTO orderDTO=orderDTOMap.get(one.getOrderId());
                List<ErpOrderPurchaseDetailDTO> list = new ArrayList<>();
                for (OrderDetailChangeDTO detailOne : orderDetailList) {
                    ErpOrderPurchaseDetailDTO erpOrderDetail = new ErpOrderPurchaseDetailDTO();
                    erpOrderDetail.setOrderId(detailOne.getOrderId());
                    erpOrderDetail.setOrderNo(String.valueOf(detailOne.getOrderId()));
                    erpOrderDetail.setOrderDetailId(detailOne.getDetailId());
                    erpOrderDetail.setSellerInnerCode(orderDTO.getSellerErpCode());
                    if (orderDTO.getOrderType() == 1) {
                        erpOrderDetail.setGoodsInSn(map.get(detailOne.getGoodsId()));
                    } else if (orderDTO.getOrderType() == 2) {
                        erpOrderDetail.setGoodsInSn(detailOne.getGoodsErpCode());
                    }
                    erpOrderDetail.setGoodsName(detailOne.getGoodsName());
                    erpOrderDetail.setBuyNumber(Long.valueOf(detailOne.getGoodsQuantity()));
                    erpOrderDetail.setGoodsPrice(detailOne.getGoodsPrice());
                    erpOrderDetail.setGoodsAmount(detailOne.getGoodsAmount());
                    erpOrderDetail.setCreateTime(new Date());
                    list.add(erpOrderDetail);
                }

                ErpOrderPurchaseDTO erpOrder = new ErpOrderPurchaseDTO();
                OrderAddressDTO orderAddress = addressMap.get(orderDTO.getId());
                erpOrder.setOrderId(orderDTO.getId());
                erpOrder.setOrderNo(String.valueOf(orderDTO.getId()));
                erpOrder.setOrderSn(orderDTO.getOrderNo());
                erpOrder.setSellerInnerCode(orderDTO.getSellerErpCode());
                erpOrder.setSellerName(orderDTO.getSellerEname());
                erpOrder.setOrderDate(one.getCreateTime());
                if (orderAddress != null) {
                    erpOrder.setReceiveMobile(orderAddress.getMobile());
                    erpOrder.setReceiveUser(orderAddress.getName());
                    erpOrder.setReceiveAddress(orderAddress.getAddress());
                }
                erpOrder.setPaymentMethod(orderDTO.getPaymentMethod());
                erpOrder.setPaymentStatus(orderDTO.getPaymentStatus());
                erpOrder.setRemark(orderDTO.getOrderNote());
                Date date = new Date();
                erpOrder.setCreateTime(date);
                erpOrder.setStatus(0);
                erpOrder.setStatusTime(date);
                erpOrder.setSellerEid(one.getBuyerEid());
                erpOrder.setOrderDetailsList(list);
                erpOrderList.add(erpOrder);
            }
        }
    }

    private void verifyERPOrderResult(List<ErpOrderPurchaseDTO> erpOrderList) {
        Iterator<ErpOrderPurchaseDTO> it = erpOrderList.iterator();
        List<UpdateErpOrderPushRequest> list = new ArrayList<>();
        while (it.hasNext()) {
            ErpOrderPurchaseDTO erpOrderDTO = it.next();
            ValidationResult validationResult = ValidationUtils.validate(erpOrderDTO);
            if (validationResult.hasError()) {
                it.remove();
                buildUpdateOrderRequest(list, erpOrderDTO, validationResult, "采购订单推送主单校验异常: ");
                continue;
            }
            List<ErpOrderPurchaseDetailDTO> erpOrderDetailDTOList = erpOrderDTO.getOrderDetailsList();
            Boolean flag = true;
            for (ErpOrderPurchaseDetailDTO erpOrderDetailDTO : erpOrderDetailDTOList) {
                validationResult = ValidationUtils.validate(erpOrderDetailDTO);
                if (validationResult.hasError()) {
                    flag = false;
                    buildUpdateOrderRequest(list, erpOrderDTO, validationResult, "采购订单推送明细校验异常: ");
                    break;
                }
            }
            if (!flag) {
                it.remove();
                continue;
            }

            //是否满足订单推送条件
            flag=orderErpApi.checkErpPullBuyerOrder(erpOrderDTO.getOrderId());
            if(!flag){
                it.remove();
                UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
                updateErpOrderDTO.setOrderId(erpOrderDTO.getOrderId());
                updateErpOrderDTO.setPushType(2);
                updateErpOrderDTO.setErpPushStatus(3);
                updateErpOrderDTO.setErpPushRemark("不满足推送条件");
                list.add(updateErpOrderDTO);
                continue;
            }
        }
        if (!CollectionUtils.isEmpty(list)) {
            erpOrderPushApi.updateErpStatusNotPushToReadSuccess(list);
        }
    }

    @ErpLogAnnotation
    @PostMapping(path = "/purchase/back")
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
                UpdateErpOrderPushRequest updateOrderPurchaseRequest = new UpdateErpOrderPushRequest();
                updateOrderPurchaseRequest.setOrderId(Long.parseLong(orderId));
                updateOrderPurchaseRequest.setErpPushStatus(2);
                updateOrderPurchaseRequest.setPushType(2);
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

    @ErpLogAnnotation
    @PostMapping(path = "/order/updatePurchaseNo")
    public Result<String> updatePurchaseNo(HttpServletRequest request, HttpServletResponse response) {
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
            if (StrUtil.isEmpty(JsonBody.getString("purchase_no")) || StrUtil.isEmpty(JsonBody.getString("order_id"))) {
                return Result.failed(OpenErrorCode.Remote_Service_Error);
            }
            String orderId = JsonBody.getString("order_id");
            String purchaseNo = JsonBody.getString("purchase_no");

            UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
            updateErpOrderDTO.setOrderId(Long.parseLong(orderId));
            updateErpOrderDTO.setErpFlowNo(purchaseNo);
            updateErpOrderDTO.setPushType(2);
            Boolean bool = erpOrderPushApi.updateExtractMessageByOrderId(updateErpOrderDTO);
            if (!bool) {
                return Result.failed(OpenErrorCode.Remote_Service_Error);
            }
        } catch (Exception e) {
            log.error("抽取工具回些订单状态接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success();
    }
}
