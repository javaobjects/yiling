package com.yiling.open.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.erp.dto.EasNotificationDTO;
import com.yiling.open.erp.dto.ErpOrderReturnDTO;
import com.yiling.open.erp.dto.ErpReturnDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.api.OrderReturnDetailApi;
import com.yiling.order.order.api.OrderReturnDetailBatchApi;
import com.yiling.order.order.api.OrderReturnDetailErpApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.OrderReturnDetailErpDTO;
import com.yiling.order.order.dto.request.OrderReturnPullErpPageRequest;
import com.yiling.order.order.dto.request.UpdateErpOrderReturnRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@RestController
@Slf4j
public class ErpOrderReturnController extends OpenBaseController {
    //
//    @DubboReference
//    private OrderErpApi orderErpApi;
//
//    @DubboReference
//    private GoodsApi goodsApi;
//
//    @DubboReference
//    private RandomReductionApi randomReductionApi;
    @DubboReference
    OrderApi                  orderApi;
    @DubboReference
    OrderDetailApi            orderDetailApi;
    @DubboReference
    OrderReturnApi            orderReturnApi;
    @DubboReference
    OrderReturnDetailApi      orderReturnDetailApi;
    @DubboReference
    EnterpriseApi             enterpriseApi;
    @DubboReference
    OrderReturnDetailBatchApi orderReturnDetailBatchApi;
    @DubboReference
    OrderReturnDetailErpApi orderReturnDetailErpApi;
    @Autowired(required = false)
    RocketMqProducerService   rocketMqProducerService;

    @ErpLogAnnotation
    @PostMapping(path = "/return/push")
    public Result<String> returnPush(HttpServletRequest request, HttpServletResponse response) {
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

        List<ErpReturnDTO> erpOrderList = new ArrayList<>();

        try {
            composeERPOrderReturnResult(suId, JsonBody.getInteger("pageSize"), erpOrderList);
        } catch (Exception e) {
            log.error("抽取工具获取订单接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success(JSONArray.toJSONString(erpOrderList));
    }

    private void composeERPOrderReturnResult(Long suId, Integer pageSize, List<ErpReturnDTO> erpOrderList) {
        OrderReturnPullErpPageRequest erpPageRequest = new OrderReturnPullErpPageRequest();
        erpPageRequest.setSize(pageSize);
        erpPageRequest.setStartCreateTime(DateUtil.offsetMonth(DateUtil.parse(DateUtil.today()), -3));
        if (Constants.YILING_EID.equals(suId)) {
            List<Long> list = enterpriseApi.listSubEids(suId);
            list = list.stream().filter(e -> !e.equals(4L)).collect(Collectors.toList());
            erpPageRequest.setSellerEids(list);
        } else {
            erpPageRequest.setSellerEids(new ArrayList<Long>() {{
                add(suId);
            }});
        }
        log.info("推送EAS的请求数据为:[{}]", erpPageRequest);
        Page<OrderReturnDTO> orderReturnDTOPage = orderReturnApi.getERPPullOrderReturn(erpPageRequest);
        if (orderReturnDTOPage != null && CollectionUtil.isNotEmpty(orderReturnDTOPage.getRecords())) {
            List<Long> returnIdList = orderReturnDTOPage.getRecords().stream().map(BaseDO::getId).collect(Collectors.toList());
            List<OrderReturnDetailErpDTO> orderReturnDetailErpDTOList = orderReturnDetailErpApi.listByReturnIds(returnIdList);
            if (CollectionUtils.isEmpty(orderReturnDetailErpDTOList)) {
                return;
            }
            Map<Long, List<OrderReturnDetailErpDTO>> orderReturnDetailErpDTOListMap = orderReturnDetailErpDTOList.stream().collect(Collectors.groupingBy(OrderReturnDetailErpDTO::getReturnId));

            List<Long> orderIdList = orderReturnDTOPage.getRecords().stream().map(OrderReturnDTO::getOrderId).collect(Collectors.toList());
            List<OrderDTO> orderDTOList = orderApi.listByIds(orderIdList);
            Map<Long, OrderDTO> orderDTOMap = orderDTOList.stream().collect(Collectors.toMap(OrderDTO::getId, o -> o, (k1, k2) -> k1));
            List<OrderReturnDTO> orderReturnDTOList = orderReturnDTOPage.getRecords();
            for (OrderReturnDTO orderReturnDTO : orderReturnDTOList) {
                List<OrderReturnDetailErpDTO> orderReturnDetailErpDTOS = orderReturnDetailErpDTOListMap.get(orderReturnDTO.getId());
                if (CollectionUtils.isEmpty(orderReturnDetailErpDTOS)) {
                    continue;
                }
                ErpReturnDTO erpReturnDTO = new ErpReturnDTO();
                erpReturnDTO.setReturnId(orderReturnDTO.getId());
                List<String> easDeliveryNumberList = new ArrayList<>();
                List<ErpOrderReturnDTO> list = new ArrayList<>();
                for (OrderReturnDetailErpDTO orderReturnDetailErpDTO : orderReturnDetailErpDTOS) {
                    OrderDTO orderDTO = orderDTOMap.get(orderReturnDTO.getOrderId());
                    //构造假的主单
                    ErpOrderReturnDTO erpOrderReturnDTO = new ErpOrderReturnDTO();
                    erpOrderReturnDTO.setCreateTime(new Date());
                    erpOrderReturnDTO.setBatchNo(orderReturnDetailErpDTO.getBatchNo());
                    erpOrderReturnDTO.setEasSendOrderId(orderReturnDetailErpDTO.getErpSendOrderId());
                    erpOrderReturnDTO.setEnterpriseInnerCode(orderDTO.getCustomerErpCode());
                    OrderDetailDTO orderDetailDTO = orderDetailApi.getOrderDetailById(orderReturnDetailErpDTO.getDetailId());
                    erpOrderReturnDTO.setGoodsInSn(orderDetailDTO.getGoodsErpCode());
                    erpOrderReturnDTO.setOrderDetailId(orderReturnDetailErpDTO.getDetailId());
                    erpOrderReturnDTO.setOrderId(orderReturnDTO.getOrderId());
                    erpOrderReturnDTO.setReturnNumber(orderReturnDetailErpDTO.getReturnQuantity().longValue());
                    erpOrderReturnDTO.setOrdeReturnId(orderReturnDTO.getId());
                    erpOrderReturnDTO.setSaleInnerCode(orderDTO.getProvinceManagerCode());
                    erpOrderReturnDTO.setSellerInnerCode(orderDTO.getSellerErpCode());
                    erpOrderReturnDTO.setStatus(0);
                    erpOrderReturnDTO.setEasDeliveryNumber(orderReturnDetailErpDTO.getErpDeliveryNo());
                    easDeliveryNumberList.add(orderReturnDetailErpDTO.getErpDeliveryNo());
                    list.add(erpOrderReturnDTO);
                }
                easDeliveryNumberList=easDeliveryNumberList.stream().distinct().collect(Collectors.toList());
                erpReturnDTO.setEasDeliveryNumber(String.join(",", easDeliveryNumberList));
                erpReturnDTO.setSellerEid(orderReturnDTO.getSellerEid());
                erpReturnDTO.setOrderReturnList(list);
                erpOrderList.add(erpReturnDTO);
            }
        }
        log.info("【推送EAS退货单信息】,推送的数据为:[{}]", erpOrderList);
    }

    @ErpLogAnnotation
    @PostMapping(path = "/return/back")
    public Result<String> returnBack(HttpServletRequest request, HttpServletResponse response) {
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
            List<UpdateErpOrderReturnRequest> updateErpOrderReturnRequestList = new ArrayList<>();
            for (Map<String, String> param : paramList) {
                if (param.get("erp_sn") == null || StringUtils.isBlank(param.get("return_id"))) {
                    continue;
                }
                String returnId = param.get("return_id");
                UpdateErpOrderReturnRequest updateErpOrderReturnRequest = new UpdateErpOrderReturnRequest();
                updateErpOrderReturnRequest.setId(Long.parseLong(returnId));
                updateErpOrderReturnRequest.setErpPushStatus(2);
                updateErpOrderReturnRequest.setErpSn(param.get("erp_sn"));
                updateErpOrderReturnRequestList.add(updateErpOrderReturnRequest);
            }
            if (!CollectionUtils.isEmpty(updateErpOrderReturnRequestList)) {
                orderReturnApi.updateERPOrderReturnByOrderId(updateErpOrderReturnRequestList);
                if (suId.equals(Constants.YILING_EID)) {
                    for (UpdateErpOrderReturnRequest updateErpOrderDTO : updateErpOrderReturnRequestList) {
                        //发送mq信息通知对方提取
                        EasNotificationDTO easNotificationDTO = new EasNotificationDTO();
                        easNotificationDTO.setType("3");
                        easNotificationDTO.setJson(updateErpOrderDTO.getErpSn());
                        easNotificationDTO.setOrderId(String.valueOf(updateErpOrderDTO.getId()));
                        rocketMqProducerService.sendSync("eas_notification", "3", DateUtil.formatDate(new Date()), JSON.toJSONString(easNotificationDTO));
                    }
                }
            }
        } catch (Exception e) {
            log.error("抽取工具回些退货单状态接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success();
    }

//    @ErpLogAnnotation
//    @PostMapping(path = "/bill/back")
//    public Result<String> orderBack(HttpServletRequest request, HttpServletResponse response) {
//        Map<String, String> params = getRequestParams(request);
//        ErpClient erpClient = getOauthClientDetailByAppKey(params.get(ErpConstants.APP_KEY));
//        if (erpClient == null) {
//            return DtoResponse.failure(ResultCode.Parameter_Parameter, "商业公司不存在");
//        }
//
//        ErpTaskInterface erpTaskInterface = getInterfaceByTaskNo(params.get(ErpConstants.METHOD));
//        if (erpTaskInterface == null) {
//            return DtoResponse.failure(ResultCode.Invalid_Method,
//                    "参数" + ErpConstants.METHOD + "不存在");
//        }
//
//        String body = getDataRequest(request);
//        if (StringUtils.isBlank(body)) {
//            return DtoResponse.failure(ResultCode.Parameter_Parameter,
//                    "参数" + ErpConstants.DATA_PARAM + "不存在");
//        }
//
//        try {
//            List<Map<String, String>> paramList = JSON.parseObject(body, new TypeToken<List<Map<String, String>>>() {
//            }.getType());
//
//            List<UpdateErpOrderDTO> list = new ArrayList<>();
//            for (Map<String, String> param : paramList) {
//                if (param.get("erp_sn") == null || StringUtils.isBlank(param.get("order_id").toString())
//                        || StringUtils.isBlank(param.get(ErpConstants.ORDER_ID).toString())) {
//                    continue;
//                }
//                String o_id = param.get("order_id");
//                String orderSn = param.get("order_sn");
//                String erpSn = param.get("erp_sn");
//                UpdateErpOrderDTO updateErpOrderDTO = new UpdateErpOrderDTO();
//                updateErpOrderDTO.setOrderId(Integer.parseInt(o_id));
//                updateErpOrderDTO.setOrderSn(orderSn);
//                updateErpOrderDTO.setErpSn(erpSn);
//                list.add(updateErpOrderDTO);
//            }
//            if (!CollectionUtils.isEmpty(list)) {
//                orderErpApi.updateOrderErpInfo(list);
//            }
//        } catch (Exception e) {
//            log.error("抽取工具回些订单状态接口报错", e);
//        }
//        return DtoResponse.success(ErpConstants.SUCCESS);
//    }

//    @ErpLogAnnotation
//    @PostMapping(path = "/order/updateErpSn")
//    public DtoResponse<String> updateErpSn(HttpServletRequest request, HttpServletResponse response) {
//        Map<String, String> params = getRequestParams(request);
//        ErpClient erpClient = getOauthClientDetailByAppKey(params.get(ErpConstants.APP_KEY));
//        if (erpClient == null) {
//            return DtoResponse.failure(ResultCode.Parameter_Parameter, "商业公司不存在");
//        }
//
//        ErpTaskInterface erpTaskInterface = getInterfaceByTaskNo(params.get(ErpConstants.METHOD));
//        if (erpTaskInterface == null) {
//            return DtoResponse.failure(ResultCode.Invalid_Method,
//                    "参数" + ErpConstants.METHOD + "不存在");
//        }
//
//        String body = getDataRequest(request);
//        if (StringUtils.isBlank(body)) {
//            return DtoResponse.failure(ResultCode.Parameter_Parameter,
//                    "参数" + ErpConstants.DATA_PARAM + "不存在");
//        }
//
//        try {
//            List<Map<String, String>> paramList = JSON.parseObject(body, new TypeToken<List<Map<String, String>>>() {
//            }.getType());
//
//            List<UpdateErpOrderDTO> list = new ArrayList<>();
//            for (Map<String, String> param : paramList) {
//                if (param.get("erp_sn") == null || StringUtils.isBlank(param.get("order_id").toString())
//                        || StringUtils.isBlank(param.get(ErpConstants.ORDER_ID).toString())) {
//                    continue;
//                }
//                String o_id = param.get("order_id");
//                String erpSn = param.get("erp_sn");
//
//                UpdateErpOrderDTO updateErpOrderDTO = new UpdateErpOrderDTO();
//                updateErpOrderDTO.setOrderId(Integer.parseInt(o_id));
//                updateErpOrderDTO.setErpSn(erpSn);
//                list.add(updateErpOrderDTO);
//            }
//            if (!CollectionUtils.isEmpty(list)) {
//                orderErpApi.updateOrderErpInfoBySAP(list);
//            }
//        } catch (Exception e) {
//            log.error("抽取工具回些订单状态接口报错", e);
//        }
//        return DtoResponse.success(ErpConstants.SUCCESS);
//    }

//    private List<ErpOrderDTO> tranErpOrder(List<OrderDTO> orderDtoList) {
//        List<ErpOrderDTO> list = new ArrayList<>(orderDtoList.size());
//        for (OrderDTO orderDTO : orderDtoList) {
//            ErpOrderDTO erpOrder = new ErpOrderDTO();
//            erpOrder.setCommissionMoney(orderDTO.getCommissionSubtotal());
//            erpOrder.setOrderId(orderDTO.getOrderId());
//            erpOrder.setCourierName("");// 快递公司名称
//            erpOrder.setCourierNumber("");// 快递单号
//            erpOrder.setCreateTime(new Date());
//            erpOrder.setCreateUser(orderDTO.getBuyerUserName());
//            erpOrder.setEnterpriseCode(orderDTO.getBuyerInnerCode());
//            erpOrder.setEnterpriseName(orderDTO.getBuyerEname());
//            erpOrder.setOrderDate(orderDTO.getCreateTime());
//            erpOrder.setOrderSn(orderDTO.getOrderSn());
//            erpOrder.setOrderStatus(orderDTO.getOrderStatus());
//            erpOrder.setPayType(orderDTO.getPayMethod());
//            erpOrder.setSuId(orderDTO.getSellerEid());
//            erpOrder.setFreight(BigDecimal.ZERO);// 运费
//            erpOrder.setGoodsTotalPrice(orderDTO.getTotalPrice());// 商品总金额
//            erpOrder.setPayTotalPrice(orderDTO.getPayPrice());// 在线支付金额
//
//            erpOrder.setShopTotalCoupon(orderDTO.getSellerCoupon());
//            erpOrder.setPlatformTotalCoupon(orderDTO.getCoupon());
//            erpOrder.setPreferentialTotalMoney(BigDecimal.ZERO);//随机立减
//            erpOrder.setRemark(orderDTO.getNote());
//
//            List<OrderDetailDTO> orderDetailDTOList = orderDTO.getOrderDetailList();
//            List<ErpOrderDetailDTO> erpOrderDetailList = new ArrayList<>();
//            BigDecimal subtotal = BigDecimal.ZERO;
//            BigDecimal platformTotalReduction=BigDecimal.ZERO;
//            for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
//                GoodsFullDTO goodsFullDTO = goodsApi.getFullGoodsById(orderDetailDTO.getGoodsId()).getData();
//                ErpOrderDetailDTO erpOrderDetail = new ErpOrderDetailDTO();
//                erpOrderDetail.setOrderSn(orderDTO.getOrderSn());
//                erpOrderDetail.setSuId(orderDTO.getSellerEid());
//                erpOrderDetail.setOrderId(orderDTO.getOrderId());
//                if (goodsFullDTO != null) {
//                    erpOrderDetail.setGoodsPrice(null);
//                }
//                erpOrderDetail.setGoodsInSn(orderDetailDTO.getGoodsInSn());
//                erpOrderDetail.setBuyNumber(new BigDecimal(orderDetailDTO.getBuyNumber()));
//                erpOrderDetail.setDeliveryNumber(BigDecimal.ZERO);
//                erpOrderDetail.setActualPrice(orderDetailDTO.getUnitPrice());// 实际支付单价（减去所有的优惠）
//                erpOrderDetail.setOrderDetailId(orderDetailDTO.getId());
//                erpOrderDetail.setBuyPrice(orderDetailDTO.getBuyPrice());// 购买单价
//                erpOrderDetail.setGoodsName(orderDetailDTO.getGoodsName());
//                erpOrderDetail.setPlatformCoupon(orderDetailDTO.getCoupon());
//                erpOrderDetail.setShopCoupon(orderDetailDTO.getSellerCoupon());
//                erpOrderDetail.setPreferentialMoney(BigDecimal.ZERO);//随机立减
//
//                if(orderDTO.getReductionId()!=null&&orderDTO.getReductionId()>0) {
//                    //平台承担金额
//                    BigDecimal platformReduction = randomReductionApi.getPlatformReductionByRatio(orderDTO.getReductionId().longValue(), orderDetailDTO.getReduction());
//                    if (platformReduction == null) {
//                        log.error("订单获取平台承担金额随机立减金额异常，订单号为" + orderDTO.getOrderSn());
//                        return null;
//                    }
//                    platformTotalReduction=platformTotalReduction.add(platformReduction);
//                    erpOrderDetail.setPlatformReduction(platformReduction);
//                    erpOrderDetail.setShopCoupon(orderDetailDTO.getReduction().subtract(platformReduction));
//                }
//                subtotal = subtotal.add(orderDetailDTO.getSubtotal());
//                erpOrderDetailList.add(erpOrderDetail);
//            }
//            erpOrder.setOrderDetailsList(erpOrderDetailList);
//            BigDecimal sellerCoupon = BigDecimal.ZERO;
//            BigDecimal coupon = BigDecimal.ZERO;
//            BigDecimal reduction = BigDecimal.ZERO;
//            if (orderDTO.getSellerCoupon() != null) {
//                sellerCoupon = orderDTO.getSellerCoupon();
//            }
//            if (orderDTO.getCoupon() != null) {
//                coupon = orderDTO.getCoupon();
//            }
//            if (orderDTO.getReduction() != null) {
//                reduction = orderDTO.getReduction();
//            }
//            erpOrder.setPlatformTotalReduction(platformTotalReduction);
//            erpOrder.setShopTotalReduction(reduction.subtract(platformTotalReduction));
//            erpOrder.setActualTotalPrice(subtotal.subtract(sellerCoupon).subtract(coupon).subtract(reduction));
//            list.add(erpOrder);
//        }
//        return list;
//    }


}
