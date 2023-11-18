package com.yiling.open.erp.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kingdee.bos.openapi.third.OpenApi;
import com.kingdee.bos.openapi.third.OpenApiFactory;
import com.kingdee.bos.openapi.third.OpenApiInfo;
import com.kingdee.bos.openapi.third.ctx.CommonLogin;
import com.kingdee.bos.openapi.third.exception.BizException;
import com.kingdee.bos.openapi.third.exception.InvokeNetworkException;
import com.kingdee.bos.openapi.third.exception.LoginException;
import com.kingdee.bos.openapi.third.login.EASLoginContext;
import com.yiling.mall.order.api.OrderInvoiceApplyProcessApi;
import com.yiling.open.erp.api.ErpOrderPushApi;
import com.yiling.open.erp.dto.EasNotificationDTO;
import com.yiling.open.erp.dto.request.UpdateErpOrderPushRequest;
import com.yiling.open.webservice.json.EasResultJson;
import com.yiling.order.order.api.OrderErpApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.dto.request.UpdateErpOrderReturnRequest;
import com.yiling.order.order.dto.request.UpdateErpPushStatusRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/12/7
 */
@Slf4j
@Service
public class EasNotificationHandler {

    @DubboReference
    private OrderErpApi orderErpApi;
    @DubboReference
    private ErpOrderPushApi erpOrderPushApi;
    @DubboReference
    private OrderReturnApi orderReturnApi;
    @DubboReference
    private OrderInvoiceApplyProcessApi orderInvoiceApplyProcessApi;

    @Value("${eas.login.webservice.ip}")
    private String ip;
    @Value("${eas.login.webservice.port}")
    private String port;
    @Value("${eas.login.webservice.userName}")
    private String userName;
    @Value("${eas.login.webservice.password}")
    private String password;
    @Value("${eas.login.webservice.language}")
    private String language;
    @Value("${eas.login.webservice.dcName}")
    private String dcName;

    /**
     * 执行eas通知程序
     *
     * @param msg
     * @return
     * @throws LoginException
     * @throws BizException
     * @throws InvokeNetworkException
     */
    public boolean executeEas(String msg) throws LoginException, BizException, InvokeNetworkException {
        EASLoginContext loginCtx = new EASLoginContext.Builder(ip, Integer.parseInt(port), new CommonLogin.Builder(userName, password, dcName, language).build()).https(false).build();

        EasNotificationDTO easNotificationDTO = JSON.parseObject(msg, EasNotificationDTO.class);
        OpenApiInfo info = new OpenApiInfo();
        //调用api的方法名
        info.setApi("PopToEasWebserviceFacade-Receivemessage");
        if ("1".equals(easNotificationDTO.getType()) || "2".equals(easNotificationDTO.getType())) {
            //调用参数，格式是数组形式
            info.setData("[" + easNotificationDTO.getType() + ",\"" + easNotificationDTO.getJson().replace('\"', '\'') + "\"]");
            OpenApi openApi = OpenApiFactory.getService(loginCtx);
            //返回数据
            String result = openApi.invoke(info);
            log.info("eas返回数据{}", result);
            EasResultJson easResultJson = JSON.parseObject(result, EasResultJson.class);
            if (easResultJson != null) {
                if ("1".equals(easNotificationDTO.getType())) {
                    UpdateErpOrderPushRequest updateErpOrderDTO = new UpdateErpOrderPushRequest();
                    updateErpOrderDTO.setOrderId(Long.parseLong(easNotificationDTO.getJson()));
                    updateErpOrderDTO.setPushType(1);
                    updateErpOrderDTO.setErpPushRemark(easResultJson.getDescription());
                    if (200 != easResultJson.getResultCode() && 4001 != easResultJson.getResultCode()) {
                        updateErpOrderDTO.setErpPushStatus(5);
                        erpOrderPushApi.updateExtractByOrderId(updateErpOrderDTO);
                        log.info("msg信息{},EAS接口订单提取报错{}", msg, JSON.toJSONString(easResultJson));
                    } else {
                        JSONArray jsonArray = JSONArray.parseArray(easResultJson.getBody());
                        if (jsonArray != null && jsonArray.size() > 0) {
                            JSONObject jSONObject = jsonArray.getJSONObject(0);
                            updateErpOrderDTO.setErpPushStatus(4);
                            updateErpOrderDTO.setErpOrderNo(jSONObject.getString("number"));
                            erpOrderPushApi.updateExtractByOrderId(updateErpOrderDTO);
                        } else {
                            log.info("msg信息{},EAS接口订单返回信息{}", msg, JSON.toJSONString(easResultJson));
                        }
                    }
                } else if ("2".equals(easNotificationDTO.getType())) {
                    UpdateErpPushStatusRequest updateErpPushStatusRequest = new UpdateErpPushStatusRequest();
                    updateErpPushStatusRequest.setGroupNo(easNotificationDTO.getOrderId());
                    updateErpPushStatusRequest.setErpPushRemark(easResultJson.getDescription());
                    if (200 != easResultJson.getResultCode()&& 4001 != easResultJson.getResultCode()) {
                        updateErpPushStatusRequest.setErpPushStatus(5);
                        List<UpdateErpPushStatusRequest> pushStatusList = new ArrayList<>();
                        pushStatusList.add(updateErpPushStatusRequest);
                        orderErpApi.updateErpOrderInvoicePushStatus(pushStatusList);
                        log.info("msg信息{},EAS接口订单提取报错{}", msg, JSON.toJSONString(easResultJson));
                    } else {
                        JSONArray jsonArray = JSONArray.parseArray(easResultJson.getBody());
                        if (jsonArray != null && jsonArray.size() > 0) {
                            List<UpdateErpPushStatusRequest> pushStatusList = new ArrayList<>();
                            pushStatusList.add(updateErpPushStatusRequest);
                            JSONObject jSONObject = jsonArray.getJSONObject(0);
                            updateErpPushStatusRequest.setErpPushStatus(4);
                            updateErpPushStatusRequest.setErpPushRemark(jSONObject.getString("number"));
                            orderErpApi.updateErpOrderInvoicePushStatus(pushStatusList);
                            String[] strs=easNotificationDTO.getJson().split(",");
                            for(String str:strs) {
                                str=str.replace("\"", "");
                                log.info("回写应收单号{},{}", str, jSONObject.getString("number"));
                                orderInvoiceApplyProcessApi.updateErpReceivableNoByDeliveryNo(str, jSONObject.getString("number"));
                            }
                        } else {
                            log.info("msg信息{},EAS接口订单返回信息{}", msg, JSON.toJSONString(easResultJson));
                        }
                    }
                }
            }
        } else if("3".equals(easNotificationDTO.getType())) {
            for (String easNo : easNotificationDTO.getJson().split(",")) {
                info.setData("[" + easNotificationDTO.getType() + ",\"{saleOrderNum:'" + easNo + "',order_return_id:'" + easNotificationDTO.getOrderId() + "'}\"]");
                OpenApi openApi = OpenApiFactory.getService(loginCtx);
                //返回数据
                String result = openApi.invoke(info);
                log.info("eas返回数据{}", result);
                EasResultJson easResultJson = JSON.parseObject(result, EasResultJson.class);
                if (easResultJson != null) {
                    List<UpdateErpOrderReturnRequest> request = new ArrayList<>();
                    UpdateErpOrderReturnRequest updateErpPushStatusRequest = new UpdateErpOrderReturnRequest();
                    updateErpPushStatusRequest.setId(Long.parseLong(easNotificationDTO.getOrderId()));
                    updateErpPushStatusRequest.setErpPushRemark("出库单号:" + easNo + "|" + easResultJson.getDescription());
                    if (200 != easResultJson.getResultCode()) {
                        updateErpPushStatusRequest.setErpPushStatus(5);
                        request.add(updateErpPushStatusRequest);
                        orderReturnApi.updateERPOrderReturnByOrderId(request);
                        log.info("msg信息{},EAS接口订单提取报错{}", msg, JSON.toJSONString(easResultJson));
                        break;
                    } else {
                        updateErpPushStatusRequest.setErpPushStatus(4);
                        request.add(updateErpPushStatusRequest);
                        orderReturnApi.updateERPOrderReturnByOrderId(request);
                    }
                } else {
                    List<UpdateErpOrderReturnRequest> request = new ArrayList<>();
                    UpdateErpOrderReturnRequest updateErpPushStatusRequest = new UpdateErpOrderReturnRequest();
                    updateErpPushStatusRequest.setId(Long.parseLong(easNotificationDTO.getOrderId()));
                    updateErpPushStatusRequest.setErpPushRemark("出库单号:" + easNo + "|没有返回值");
                    updateErpPushStatusRequest.setErpPushStatus(5);
                    request.add(updateErpPushStatusRequest);
                    orderReturnApi.updateERPOrderReturnByOrderId(request);
                    log.info("msg信息{},EAS接口订单提取没有返回值", msg);
                    break;
                }
            }
        }else if("4".equals(easNotificationDTO.getType())) {
            //重新推送出库单转应收
            for (String easNo : easNotificationDTO.getJson().split(",")) {
                orderInvoiceApplyProcessApi.updateErpReceivableNoByDeliveryNo(easNo, easNotificationDTO.getOrderId());
            }
        }
        return true;
    }
}
