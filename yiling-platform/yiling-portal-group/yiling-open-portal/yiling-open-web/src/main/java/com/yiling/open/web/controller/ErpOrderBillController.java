package com.yiling.open.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yiling.mall.order.api.OrderInvoiceApplyProcessApi;
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
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.framework.common.annotations.ErpLogAnnotation;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.open.erp.dto.EasNotificationDTO;
import com.yiling.open.erp.dto.ErpBillDTO;
import com.yiling.open.erp.dto.ErpOrderBillDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.order.order.api.OrderDeliveryErpApi;
import com.yiling.order.order.api.OrderErpApi;
import com.yiling.order.order.api.OrderInvoiceDetailApi;
import com.yiling.order.order.dto.OrderDeliveryErpDTO;
import com.yiling.order.order.dto.OrderInvoiceDetailDTO;
import com.yiling.order.order.dto.OrderInvoicePullErpDTO;
import com.yiling.order.order.dto.request.OrderPullErpPageRequest;
import com.yiling.order.order.dto.request.UpdateErpPushStatusRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@RestController
@Slf4j
public class ErpOrderBillController extends OpenBaseController {
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
    DictApi dictApi;
    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @DubboReference
    OrderErpApi                orderErpApi;
    @DubboReference
    EnterpriseApi              enterpriseApi;
    @DubboReference
    OrderInvoiceDetailApi      orderInvoiceDetailApi;
    @DubboReference
    OrderDeliveryErpApi        orderDeliveryErpApi;
    @DubboReference
    OrderInvoiceApplyProcessApi orderInvoiceApplyProcessApi;

    @ErpLogAnnotation
    @PostMapping(path = "/bill/push")
    public Result<String> billPush(HttpServletRequest request, HttpServletResponse response) {
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

        List<ErpBillDTO> erpOrderList = new ArrayList<>();
        try {
            OrderPullErpPageRequest erpRequest = new OrderPullErpPageRequest();
            erpRequest.setStartCreateTime(DateUtil.offsetMonth(DateUtil.parse(DateUtil.today()), -3));
            if (Constants.YILING_EID.equals(suId)) {
                List<Long> list = enterpriseApi.listSubEids(suId);
                List<Long> longs = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.INDUSTRY_DIRECT);
                if (CollectionUtil.isNotEmpty(list) && (CollectionUtil.isNotEmpty(longs))) {
                    list.addAll(longs);
                }
                erpRequest.setSellerEids(list);
            } else {
                erpRequest.setSellerEids(new ArrayList<Long>() {{
                    add(suId);
                }});
            }
            erpRequest.setSize(JsonBody.getInteger("pageSize"));

            Page<OrderInvoicePullErpDTO> erpBillPage = orderErpApi.getErpPullOrderInvoice(erpRequest);

            if (erpBillPage != null && CollectionUtil.isNotEmpty(erpBillPage.getRecords())) {
                List<Long> ids = erpBillPage.getRecords().stream().map(OrderInvoicePullErpDTO::getId).collect(Collectors.toList());
                List<OrderInvoiceDetailDTO> orderInvoiceDetailList = orderInvoiceDetailApi.listByOrderIds(ids);
                Map<String, List<OrderInvoiceDetailDTO>> detailMap = new HashMap<>();
                for (OrderInvoiceDetailDTO one : orderInvoiceDetailList) {
                    if (detailMap.containsKey(one.getGroupNo())) {
                        List<OrderInvoiceDetailDTO> orderDetailList = detailMap.get(one.getGroupNo());
                        orderDetailList.add(one);
                    } else {
                        detailMap.put(one.getGroupNo(), new ArrayList<OrderInvoiceDetailDTO>() {{
                            add(one);
                        }});
                    }
                }

                Map<String, List<OrderInvoiceDetailDTO>> invoiceDetailMap = orderInvoiceDetailList.stream().collect(Collectors.groupingBy(s -> s.getGroupNo() + "_" + s.getDetailId() + "_" + s.getErpDeliveryNo()));

                Map<String, List<OrderDeliveryErpDTO>> mapResult = getDetailDeliveryErpMap(ids);

                for(OrderInvoicePullErpDTO one : erpBillPage.getRecords()) {
                    ErpBillDTO erpBill = new ErpBillDTO();
                    erpBill.setOrdeId(one.getId());
                    erpBill.setOrderBillId(one.getGroupNo());
                    List<String> erpDeliveryNoList = Arrays.asList(one.getGroupDeliveryNos().split(","));
                    if (CollectionUtil.isNotEmpty(erpDeliveryNoList)) {
                        erpBill.setEasDeliveryNumber("\"" + String.join("\",\"", erpDeliveryNoList) + "\"");
                    }
                    List<OrderInvoiceDetailDTO> orderDetailList = detailMap.get(one.getGroupNo());
                    List<ErpOrderBillDTO> list = new ArrayList<>();

                    Map<String, BigDecimal> map = new HashMap<>();
                    for (OrderInvoiceDetailDTO detailOne : orderDetailList) {
                        //旧数据处理
                        if(!map.containsKey(detailOne.getGroupNo() + "_" + detailOne.getDetailId() + "_" + detailOne.getErpDeliveryNo())){
                            if (StringUtils.isBlank(detailOne.getBatchNo()) && detailOne.getInvoiceQuantity().compareTo(0) == 0) {
                                BigDecimal discountAmount = detailOne.getTicketDiscountAmount().add(detailOne.getCashDiscountAmount());
                                BigDecimal amountAll = BigDecimal.ZERO;
                                BigDecimal rateAll = BigDecimal.ZERO;
                                List<OrderDeliveryErpDTO> orderDeliveryErpDTOList = mapResult.get(detailOne.getDetailId() + "_" + detailOne.getErpDeliveryNo());
                                BigDecimal rate = discountAmount.multiply(BigDecimal.valueOf(100)).divide(detailOne.getGoodsAmount(), 2, BigDecimal.ROUND_HALF_UP);
                                for (int i = 0; i < orderDeliveryErpDTOList.size(); i++) {
                                    ErpOrderBillDTO erpOrderBill = new ErpOrderBillDTO();
                                    if (i != orderDeliveryErpDTOList.size() - 1) {
                                        Integer deliveryQuantity = orderDeliveryErpDTOList.get(i).getDeliveryQuantity();
                                        BigDecimal amount = BigDecimal.valueOf(deliveryQuantity).multiply(discountAmount).divide(BigDecimal.valueOf(detailOne.getGoodsQuantity()), 2, BigDecimal.ROUND_DOWN);
                                        erpOrderBill.setDiscountAmount(amount);
                                        erpOrderBill.setBillQuantity(deliveryQuantity);

                              /*  BigDecimal rate = amount.multiply(BigDecimal.valueOf(100))
                                        .divide((detailOne.getGoodsPrice().multiply(BigDecimal.valueOf(deliveryQuantity))), 2, BigDecimal.ROUND_DOWN);*/
                                        erpOrderBill.setDiscountRate(rate);

                                        amountAll = amountAll.add(amount);
                                        rateAll = rateAll.add(rate);
                                    } else {
                                        BigDecimal amount = discountAmount.subtract(amountAll);
                                        Integer deliveryQuantity = orderDeliveryErpDTOList.get(i).getDeliveryQuantity();
                               /* BigDecimal rate = amount.multiply(BigDecimal.valueOf(100))
                                        .divide((detailOne.getGoodsPrice().multiply(BigDecimal.valueOf(deliveryQuantity))), 2, BigDecimal.ROUND_DOWN);*/
                                        erpOrderBill.setDiscountAmount(amount);
                                        erpOrderBill.setBillQuantity(deliveryQuantity);
                                        erpOrderBill.setDiscountRate(rate);
                                    }
                                    erpOrderBill.setEasDeliveryNumber(orderDeliveryErpDTOList.get(i).getErpDeliveryNo());
                                    erpOrderBill.setCreateTime(new Date());
                                    erpOrderBill.setEasBillNumber("");
                                    erpOrderBill.setTicketType(one.getTransitionRuleCode());
                                    erpOrderBill.setGoodsInSn(detailOne.getGoodsErpCode());
                                    //需要开票申请主键
                                    erpOrderBill.setOrderBillId(one.getGroupNo());
                                    erpOrderBill.setOrderDetailId(detailOne.getDetailId());
                                    //需要要获取销售主体名字
                                    erpOrderBill.setFnumber(transformFnumber(one.getSellerEname(), one.getTransitionRuleCode()));
                                    //需要获取订单摘要信息
                                    erpOrderBill.setBillRemark(one.getInvoiceSummary());
                                    //需要获取批次号信息
                                    erpOrderBill.setBatchNo(orderDeliveryErpDTOList.get(i).getBatchNo());
                                    erpOrderBill.setEasSendOrderId(orderDeliveryErpDTOList.get(i).getErpSendOrderId());
                                    //需要获取电子邮箱
                                    erpOrderBill.setEmail(one.getInvoiceEmail());
                                    erpOrderBill.setOrdeId(one.getId());
                                    erpOrderBill.setStatus(0);
                                    list.add(erpOrderBill);
                                }
                            } else {
                                List<OrderInvoiceDetailDTO> detailList = invoiceDetailMap.get(detailOne.getGroupNo() + "_" + detailOne.getDetailId() + "_" + detailOne.getErpDeliveryNo());
                                //折扣总金额
                                BigDecimal discountAmount = BigDecimal.ZERO;
                                //开票小计
                                BigDecimal invoiceAmountSum = BigDecimal.ZERO;
                                for (OrderInvoiceDetailDTO orderInvoiceDetailOne : detailList) {
                                    discountAmount = discountAmount.add(orderInvoiceDetailOne.getTicketDiscountAmount()).add(orderInvoiceDetailOne.getCashDiscountAmount());
                                    invoiceAmountSum = invoiceAmountSum.add(orderInvoiceDetailOne.getGoodsPrice().multiply(BigDecimal.valueOf(orderInvoiceDetailOne.getInvoiceQuantity())));
                                }
                                log.info("开票使用折扣总金额discountAmount:{},开票小计invoiceAmountSum:{}", discountAmount, invoiceAmountSum);
                                //折扣比率
                                BigDecimal rate = discountAmount.multiply(BigDecimal.valueOf(100)).divide(invoiceAmountSum, 2, BigDecimal.ROUND_HALF_UP);
                                for (OrderInvoiceDetailDTO orderInvoiceDetailOne : detailList) {
                                    ErpOrderBillDTO erpOrderBill = new ErpOrderBillDTO();
                                    erpOrderBill.setDiscountRate(rate);
                                    erpOrderBill.setBillQuantity(orderInvoiceDetailOne.getInvoiceQuantity());
                                    erpOrderBill.setDiscountAmount(orderInvoiceDetailOne.getCashDiscountAmount().add(orderInvoiceDetailOne.getTicketDiscountAmount()));
                                    erpOrderBill.setEasDeliveryNumber(orderInvoiceDetailOne.getErpDeliveryNo());
                                    erpOrderBill.setCreateTime(new Date());
                                    erpOrderBill.setEasBillNumber("");
                                    erpOrderBill.setTicketType(one.getTransitionRuleCode());
                                    erpOrderBill.setGoodsInSn(detailOne.getGoodsErpCode());
                                    //需要开票申请主键
                                    erpOrderBill.setOrderBillId(one.getGroupNo());
                                    erpOrderBill.setOrderDetailId(detailOne.getDetailId());
                                    //需要要获取销售主体名字
                                    erpOrderBill.setFnumber(transformFnumber(one.getSellerEname(), one.getTransitionRuleCode()));
                                    //需要获取订单摘要信息
                                    erpOrderBill.setBillRemark(one.getInvoiceSummary());
                                    //需要获取批次号信息
                                    erpOrderBill.setBatchNo(orderInvoiceDetailOne.getBatchNo());
                                    erpOrderBill.setEasSendOrderId(orderInvoiceDetailOne.getErpSendOrderId());
                                    //需要获取电子邮箱
                                    erpOrderBill.setEmail(one.getInvoiceEmail());
                                    erpOrderBill.setOrdeId(one.getId());
                                    erpOrderBill.setStatus(0);
                                    list.add(erpOrderBill);
                                }
                            }
                            map.put(detailOne.getGroupNo() + "_" + detailOne.getDetailId() + "_" + detailOne.getErpDeliveryNo(),BigDecimal.ONE);
                        }

                    }
                    //需要开票申请主键
                    log.info("开票存入中间表数量");
                    erpBill.setOrderBillList(list);
                    erpOrderList.add(erpBill);
                }
            }

        } catch (Exception e) {
            log.error("抽取工具获取订单接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success(JSONArray.toJSONString(erpOrderList));
    }

    //转换开票终端,如果AR00004标识电子发票

    /**
     * wyzp001	纸票	万洋衡水制药有限公司
     * 102001ZZ	纸票	北京以岭药业有限公司本部
     * maofeng01	纸票	故城县茂丰农业科技开发有限公司
     * HS-02	电票	衡水以岭药业有限公司
     * HS-01	纸票	衡水以岭药业有限公司
     * txlk02	电票	通心络科（河北）科技有限公司
     * txlk01	纸票	通心络科（河北）科技有限公司
     * YP-DP	电票	石家庄以岭中药饮片有限公司
     * YPZP	纸票	石家庄以岭中药饮片有限公司
     * YLYY-ZP	纸票	石家庄以岭药业股份有限公司本部
     * YLYY-DP	电票	石家庄以岭药业股份有限公司本部
     * jckzp	纸票	以岭万洋河北进出口贸易有限公司
     * jckdp	电票	以岭万洋河北进出口贸易有限公司
     * ylwzdp001	电票	以岭万洲国际制药有限公司
     * ylwzzp001	纸票	以岭万洲国际制药有限公司
     * dyhdzfp	电票	河北大运河医药物流有限公司
     * dyhzzp	纸票	河北大运河医药物流有限公司
     *
     * @param ename
     * @param ticketType
     * @return
     */
    public String transformFnumber(String ename, String ticketType) {
        List<DictBO> list = dictApi.getEnabledList();
        List<DictBO.DictData> dictDataList = null;
        for (DictBO dictBO : list) {
            if (dictBO.getName().equals("fnumber_type")) {
                dictDataList = dictBO.getDataList();
                break;
            }
        }
        String type = null;
        if (ticketType.equals("AR00004")) {
            type = "电票";
        } else {
            type = "纸票";
        }
        String strEname = type +"-"+ ename;
        if (CollUtil.isNotEmpty(dictDataList)) {
            for (DictBO.DictData dictData : dictDataList) {
                if (strEname.equals(dictData.getLabel())) {
                    return dictData.getValue();
                }
            }
        }
        return "";
    }

    private Map<String, List<OrderDeliveryErpDTO>> getDetailDeliveryErpMap(List<Long> ids) {
        List<OrderDeliveryErpDTO> orderDeliveryErpList = orderDeliveryErpApi.listByOrderIds(ids);
        Map<String, List<OrderDeliveryErpDTO>> map = new HashMap<>();
        for (OrderDeliveryErpDTO one : orderDeliveryErpList) {
            if (map.containsKey(one.getDetailId()+"_"+one.getErpDeliveryNo())) {
                List<OrderDeliveryErpDTO> orderDeliveryErp = map.get(one.getDetailId()+"_"+one.getErpDeliveryNo());
                orderDeliveryErp.add(one);
            } else {
                map.put(one.getDetailId()+"_"+one.getErpDeliveryNo(), new ArrayList<OrderDeliveryErpDTO>() {{
                    add(one);
                }});
            }
        }

        return map;
    }

    @ErpLogAnnotation
    @PostMapping(path = "/bill/back")
    public Result<String> orderBack(HttpServletRequest request, HttpServletResponse response) {
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

            List<UpdateErpPushStatusRequest> list = new ArrayList<>();
            for (Map<String, String> param : paramList) {
                if (param.get("erp_sn") == null || StringUtils.isBlank(param.get("order_id"))) {
                    continue;
                }
                String o_id = param.get("order_id");
                String orderSn = param.get("erp_sn");
                UpdateErpPushStatusRequest updateErpPushStatusRequest = new UpdateErpPushStatusRequest();
                updateErpPushStatusRequest.setGroupNo(o_id);
                updateErpPushStatusRequest.setErpPushStatus(2);
                updateErpPushStatusRequest.setOrderSn(orderSn);
                list.add(updateErpPushStatusRequest);
            }
            if (!CollUtil.isEmpty(list)) {
                orderErpApi.updateErpOrderInvoicePushStatus(list);
                for (UpdateErpPushStatusRequest updateErpOrderDTO : list) {
                    //发送mq信息通知对方提取
                    EasNotificationDTO easNotificationDTO = new EasNotificationDTO();
                    easNotificationDTO.setType("2");
                    easNotificationDTO.setJson(updateErpOrderDTO.getOrderSn());
                    easNotificationDTO.setOrderId(String.valueOf(updateErpOrderDTO.getGroupNo()));
                    rocketMqProducerService.sendSync("eas_notification", "2", DateUtil.formatDate(new Date()), JSON.toJSONString(easNotificationDTO));
                }
            }
        } catch (Exception e) {
            log.error("抽取工具回些订单状态接口报错", e);
            return Result.failed(OpenErrorCode.Remote_Service_Error);
        }
        return Result.success();
    }

    @ErpLogAnnotation
    @PostMapping(path = "/bill/updateErpSn")
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

        log.info("回写应收单号body"+body);
        JSONObject JsonBody = JSON.parseObject(body);
        if (CollectionUtils.isEmpty(JsonBody)) {
            return Result.failed(OpenErrorCode.Parameter_Body_Null.getCode(), OpenConstants.DATA_PARAM + "值为空");
        }

        try {
            if (StrUtil.isEmpty(JsonBody.getString("eas_delivery_number")) || StrUtil.isEmpty(JsonBody.getString("eas_bill_number"))) {
                return Result.failed(OpenErrorCode.Remote_Service_Error);
            }
            String easBillNumber = JsonBody.getString("eas_bill_number");
            String erpSn = JsonBody.getString("eas_delivery_number");
            Boolean bool = orderInvoiceApplyProcessApi.updateErpReceivableNoByDeliveryNo(erpSn, easBillNumber);
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
