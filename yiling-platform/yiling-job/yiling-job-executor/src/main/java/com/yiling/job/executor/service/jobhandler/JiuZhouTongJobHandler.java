package com.yiling.job.executor.service.jobhandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.api.ErpOrderPushApi;
import com.yiling.open.erp.bo.JiuZhouTongPurchaseJson;
import com.yiling.open.erp.bo.JiuZhouTongSaleJson;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.ErpOrderDTO;
import com.yiling.open.erp.dto.ErpOrderDetailDTO;
import com.yiling.open.erp.dto.ErpOrderPurchaseSendDTO;
import com.yiling.open.erp.dto.ErpOrderPurchaseSendDetailDTO;
import com.yiling.open.erp.dto.request.UpdateErpOrderPushRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author: shuang.zhang
 * @date: 2023/3/28
 */
@Component
@Slf4j
public class JiuZhouTongJobHandler {

    private static final String client_id     = "6bf2f78c86fc4dc69f11779730775adf";
    private static final String client_secret = "f92b67dd4ff34c0192946362b2f15a77";
    private static final String grant_type    = "client_credentials";


    @DubboReference
    private ErpOrderPushApi erpOrderPushApi;
    @DubboReference
    private ErpClientApi    erpClientApi;

    /**
     * 每10分钟调度推送采购单数据给九州通
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("pushPurchaseOrderHandleer")
    public ReturnT<String> pushPurchaseOrderHandleer(String param) throws Exception {
        log.info("任务开始：推送以岭发货单给九州通任务");
        List<ErpOrderPurchaseSendDTO> erpOrderList = null;
        try {
            //转换功能
            erpOrderList = erpOrderPushApi.getOrderPurchaseSendBySuId(1235L, 5);
            log.debug("/purchase/push, erpOrderList -> {}", JSON.toJSONString(erpOrderList));
            //订单验证
            erpOrderPushApi.verifyERPOrderPurchaseSendResult(erpOrderList);
            for (ErpOrderPurchaseSendDTO erpOrderPurchaseSendDTO : erpOrderList) {
                ErpClientDTO erpClientDTO = erpClientApi.selectByRkSuId(erpOrderPurchaseSendDTO.getBuyerEid());
                if (erpClientDTO == null) {
                    continue;
                }
                JiuZhouTongPurchaseJson json = new JiuZhouTongPurchaseJson();
                json.setBranchId(erpClientDTO.getSuDeptNo());
                json.setZytBillId(erpOrderPurchaseSendDTO.getOrderSn());
                json.setCustId(erpOrderPurchaseSendDTO.getSellerInnerCode());
                json.setOpId(erpOrderPurchaseSendDTO.getPurchasePeopleCode());
                json.setOpName(erpOrderPurchaseSendDTO.getPurchasePeopleName());
                List<JiuZhouTongPurchaseJson.PurPlanDet> purPlanDetList = new ArrayList<>();
                for (ErpOrderPurchaseSendDetailDTO erpOrderPurchaseSendDetailDTO : erpOrderPurchaseSendDTO.getOrderDetailsList()) {
                    JiuZhouTongPurchaseJson.PurPlanDet purPlanDet = new JiuZhouTongPurchaseJson.PurPlanDet();
                    purPlanDet.setProdId(erpOrderPurchaseSendDetailDTO.getGoodsInSn());
                    purPlanDet.setProdNo(erpOrderPurchaseSendDetailDTO.getGoodsInSn());
                    purPlanDet.setPrice(erpOrderPurchaseSendDetailDTO.getGoodsPrice().toPlainString());
                    purPlanDet.setQuantity(erpOrderPurchaseSendDetailDTO.getDeliveryNumber());
                    purPlanDetList.add(purPlanDet);
                }
                json.setPurPlanDetList(purPlanDetList);
                String token = getToken();
                String request = HttpRequest.post("https://openapi-ams.jztweb.com/ziy00172416/iaspm/V1/eip/api/open/startProcess/eip_1681890016509").header(Header.CONTENT_TYPE, "application/json").header(Header.AUTHORIZATION, token).body(JSON.toJSONString(json)).execute().body();
                JSONObject jsonObject = JSONObject.parseObject(request);
                if (jsonObject.getString("isSuccess").equals("1")) {
                    List<UpdateErpOrderPushRequest> updateErpOrderRequest = new ArrayList<>();
                    UpdateErpOrderPushRequest updateOrderPurchaseRequest = new UpdateErpOrderPushRequest();
                    updateOrderPurchaseRequest.setOrderId(erpOrderPurchaseSendDTO.getOrdeId());
                    updateOrderPurchaseRequest.setErpPushStatus(2);
                    updateOrderPurchaseRequest.setPushType(3);
                    updateErpOrderRequest.add(updateOrderPurchaseRequest);
                    erpOrderPushApi.updateErpStatusNotPushToReadSuccess(updateErpOrderRequest);
                }
            }
        } catch (Exception e) {
            log.error("抽取工具获取订单接口报错", e);
        }
        log.info("任务结束：推送以岭发货单给九州通任务");
        return ReturnT.SUCCESS;
    }


    /**
     * 每10分钟调度推送销售单数据给九州通
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("pushSaleOrderHandleer")
    public ReturnT<String> pushSaleOrderHandleer(String param) throws Exception {
        log.info("任务开始：推送以岭销售单给九州通任务");
        List<ErpOrderDTO> erpOrderList = null;
        try {
            //转换功能
            erpOrderList = erpOrderPushApi.getOrderSaleBySuId(1235L, 5);
            log.debug("/purchase/push, erpOrderList -> {}", JSON.toJSONString(erpOrderList));
            //订单验证
            erpOrderPushApi.verifyERPOrderSaleResult(erpOrderList);
            for (ErpOrderDTO erpOrderDTO : erpOrderList) {
                ErpClientDTO erpClientDTO = erpClientApi.selectByRkSuId(erpOrderDTO.getSellerEid());
                if (erpClientDTO == null) {
                    continue;
                }
                JiuZhouTongSaleJson json = new JiuZhouTongSaleJson();
                json.setBranchId(erpClientDTO.getSuDeptNo());
                json.setBillGuid(erpOrderDTO.getOrderSn());
                json.setOrderDate(DateUtil.formatDateTime(erpOrderDTO.getCreateTime()));
                json.setOrderSource("ylpop");
                json.setCustID("");
                json.setCustNo(erpOrderDTO.getEnterpriseInnerCode());
                List<JiuZhouTongSaleJson.PlatOrderDet> purPlanDetList = new ArrayList<>();
                for (ErpOrderDetailDTO erpOrderDetailDTO : erpOrderDTO.getOrderDetailsList()) {
                    JiuZhouTongSaleJson.PlatOrderDet purPlanDet = new JiuZhouTongSaleJson.PlatOrderDet();
                    purPlanDet.setOrderSourceId(erpOrderDTO.getOrderSn());
                    purPlanDet.setProdID("");
                    purPlanDet.setPrice(erpOrderDetailDTO.getTruthPrice().toPlainString());
                    purPlanDet.setQty(String.valueOf(erpOrderDetailDTO.getBuyNumber()));
                    purPlanDet.setBranchID(erpClientDTO.getSuDeptNo());
                    purPlanDetList.add(purPlanDet);
                }
                json.setShoppingCartDetList(purPlanDetList);
                String token = getToken();
                String request = HttpRequest.post("https://openapi-ams.jztweb.com/ziy00172416/iaspm/V1/eip/api/open/startProcess/eip_1681890090102\n").header(Header.CONTENT_TYPE, "application/json").header(Header.AUTHORIZATION, token).body(JSON.toJSONString(json)).execute().body();
                JSONObject jsonObject = JSONObject.parseObject(request);
                if (jsonObject.getString("isSuccess").equals("1")) {
                    List<UpdateErpOrderPushRequest> updateErpOrderRequest = new ArrayList<>();
                    UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
                    updateErpOrderDTO.setOrderId(erpOrderDTO.getOrdeId());
                    updateErpOrderDTO.setPushType(1);
                    updateErpOrderDTO.setErpPushStatus(2);
                    updateErpOrderRequest.add(updateErpOrderDTO);
                    erpOrderPushApi.updateErpStatusNotPushToReadSuccess(updateErpOrderRequest);
                }
            }
        } catch (Exception e) {
            log.error("抽取工具获取订单接口报错", e);
        }
        log.info("任务结束：推送以岭发货单给九州通任务");
        return ReturnT.SUCCESS;
    }


    public String getToken() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("client_id", client_id);
        paramMap.put("client_secret", client_secret);
        paramMap.put("grant_type", grant_type);
        String msg = HttpUtil.post("https://sandbox-openapi-ams.jztweb.com/IMS/002/oauth2/token", paramMap);
        if (StrUtil.isNotEmpty(msg)) {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            String token = jsonObject.getString("access_token");
            return token;
        }
        return null;
    }

    public static void main(String[] args) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("client_id", "052f8be4d9384a30b20f34f3cc357af7");
        paramMap.put("client_secret", "4cc87dbf5f5241b299fd4a56e5cbb5a9");
        paramMap.put("grant_type", "client_credentials");
        String msg = HttpUtil.post("https://sandbox-openapi-ams.jztweb.com/IMS/002/oauth2/token", paramMap);
        if (StrUtil.isNotEmpty(msg)) {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            String token = jsonObject.getString("access_token");
            if (StrUtil.isNotEmpty(token)) {
//                JiuZhouTongPurchaseJson json = new JiuZhouTongPurchaseJson();
//                json.setZytBillId("11112");
//                json.setBranchId("FDG");
//                json.setOpId("ZIY00003410");
//                json.setOpName("湖北管理员");
//                json.setCustId("P360423002X001HA");
//                List<JiuZhouTongPurchaseJson.PurPlanDet> list = new ArrayList<>();
//                JiuZhouTongPurchaseJson.PurPlanDet purPlanDetList = new JiuZhouTongPurchaseJson.PurPlanDet();
//                purPlanDetList.setProdId("HQS004020C");
//                purPlanDetList.setProdNo("HQS004020C");
//                purPlanDetList.setPrice("2");
//                purPlanDetList.setQuantity("1000");
//                list.add(purPlanDetList);
//                json.setPurPlanDetList(list);
                JiuZhouTongSaleJson json=new JiuZhouTongSaleJson();
                json.setBillGuid("1112");
                json.setBranchId("FDG");
                json.setCustID("");
                json.setCustNo("C420113039X001HA");
                json.setOrderDate("2023-02-29");
                json.setOrderSource("ylpop");
                List<JiuZhouTongSaleJson.PlatOrderDet> platOrderDetList=new ArrayList<>();
                JiuZhouTongSaleJson.PlatOrderDet platOrderDet=new JiuZhouTongSaleJson.PlatOrderDet();
                platOrderDet.setBranchID("FDG");
                platOrderDet.setOrderSourceId("1112");
                platOrderDet.setProdID("");
                platOrderDet.setProdNo("CAD311002G");
                platOrderDet.setPrice("1");
                platOrderDet.setQty("2");
                platOrderDetList.add(platOrderDet);
                json.setShoppingCartDetList(platOrderDetList);
                String request = HttpRequest.post("https://sandbox-openapi-ams.jztweb.com/ZIY00172416/31pw4/V1/eip/api/open/startProcess/eip_1679552666637").header(Header.CONTENT_TYPE, "application/json").header(Header.AUTHORIZATION, token).body(JSON.toJSONString(json)).execute().body();
                System.out.println(request);
            }
        }
    }


//    /**
//     * 每10分钟调度推送销售单数据给九州通
//     *
//     * @param param
//     * @return
//     * @throws Exception
//     */
//    @JobLog
//    @XxlJob("pushSaleOrderHandleer")
//    public ReturnT<String> pushPurchaseOrderHandleer(String param) throws Exception {
//        log.info("任务开始：推送以岭发货单给九州通任务");
//
//        log.info("任务结束：推送以岭发货单给九州通任务");
//        return ReturnT.SUCCESS;
//    }
}
