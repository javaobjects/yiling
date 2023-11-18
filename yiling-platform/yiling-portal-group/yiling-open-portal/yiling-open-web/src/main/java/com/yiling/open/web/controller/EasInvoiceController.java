package com.yiling.open.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.annotations.ErpLogAnnotation;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.mall.order.api.OrderInvoiceApplyProcessApi;
import com.yiling.open.erp.api.ErpDeleteDataApi;
import com.yiling.open.erp.dto.EasBillInvoiceDTO;
import com.yiling.open.erp.dto.ErpTaskInterfaceDTO;
import com.yiling.open.erp.dto.request.SaveErpDeleteDataRequest;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.util.OpenConstants;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDeliveryReceivableApi;
import com.yiling.order.order.api.OrderErpApi;
import com.yiling.order.order.dto.request.CompleteErpOrderInvoiceRequest;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: shuang.zhang
 * @Email: shuang.zhang@rograndec.com
 * @CreateTime: 2019-3-6
 * @Version: 1.0
 */
@RestController
@Slf4j
public class EasInvoiceController extends OpenBaseController {

    private static String flag = "-";

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    @DubboReference
    private OrderErpApi orderErpApi;

    @DubboReference
    private OrderApi orderApi;

    @DubboReference
    private OrderDeliveryReceivableApi orderDeliveryReceivableApi;

    @DubboReference
    private ErpDeleteDataApi erpDeleteDataApi;

    @DubboReference
    private OrderInvoiceApplyProcessApi orderInvoiceApplyProcessApi;

    /**
     * mop.seller.product.sync(商品信息同步)
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ErpLogAnnotation
    @PostMapping(path = "/eas/billInvoice")
    public Result<String> billInvoice(HttpServletRequest request,
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
            EasBillInvoiceDTO easBillInvoiceDTO = JSON.parseObject(body, EasBillInvoiceDTO.class);
            String invoiceNoString = easBillInvoiceDTO.getInvoiceNo();
            String invoiceAmountString = easBillInvoiceDTO.getInvoiceAmount();
            String[] invoiceNoList = invoiceNoString.split("-");
            String[] invoiceAmountList = invoiceAmountString.split("-");
            List<CompleteErpOrderInvoiceRequest> list = new ArrayList<>();
            for (int i = 0; i < invoiceNoList.length; i++) {
                CompleteErpOrderInvoiceRequest completeErpOrderInvoiceRequest = new CompleteErpOrderInvoiceRequest();
                completeErpOrderInvoiceRequest.setErpReceivableNo(easBillInvoiceDTO.getEasBillNumber());
                completeErpOrderInvoiceRequest.setInvoiceNo(invoiceNoList[i]);
                completeErpOrderInvoiceRequest.setInvoiceAmount(new BigDecimal(invoiceAmountList[i]));
                list.add(completeErpOrderInvoiceRequest);
            }
            Boolean bool = orderErpApi.completeErpOrderInvoice(list);
            if (bool) {
                return Result.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(OpenErrorCode.ERP_SYNC_ERROR.getCode(), e.getMessage());
        }
        return Result.failed(OpenErrorCode.ERP_SYNC_ERROR);
    }

    /**
     * mop.seller.product.sync(商品信息同步)
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ErpLogAnnotation
    @PostMapping(path = "/eas/cancelInvoice")
    public Result<String> cancelInvoice(HttpServletRequest request,
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

        log.info("请求数据为{}",body);

        try {
            EasBillInvoiceDTO easBillInvoiceDTO = JSON.parseObject(body, EasBillInvoiceDTO.class);
            if (StrUtil.isNotEmpty(easBillInvoiceDTO.getInvoiceNo())) {
                List<String> invoiceString = Arrays.asList(easBillInvoiceDTO.getInvoiceNo().split(flag));
                invoiceString = invoiceString.stream().filter(e -> StrUtil.isNotEmpty(e)).collect(Collectors.toList());
                Boolean bool = orderInvoiceApplyProcessApi.cancelErpOrderInvoice(invoiceString);
                if (bool) {
                    return Result.success();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed(OpenErrorCode.ERP_SYNC_ERROR.getCode(), e.getMessage());
        }
        return Result.failed(OpenErrorCode.ERP_SYNC_ERROR);
    }

    /**
     * mop.seller.product.sync(商品信息同步)
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ErpLogAnnotation
    @PostMapping(path = "/eas/deleteBill")
    public Result<String> deleteBill(HttpServletRequest request,
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

        log.info("请求数据为{}",body);

        if (StringUtils.isBlank(body)) {
            return Result.failed(OpenErrorCode.Parameter_Parameter.getCode(), "参数" + OpenConstants.DATA_PARAM + "不存在");
        }

        try {
            JSONObject bill = JSONObject.parseObject(body);
            String easBillNumber = bill.getString("eas_bill_number");
            if (StrUtil.isNotEmpty(easBillNumber)) {
                SaveErpDeleteDataRequest request1 = new SaveErpDeleteDataRequest();
                request1.setSuId(suId);
                request1.setTaskNo(ErpTopicName.ErpOrderBill.getMethod());
                String erpDeliveryNoStr = orderDeliveryReceivableApi.getDeliveryNo(easBillNumber);
                if (StrUtil.isNotEmpty(erpDeliveryNoStr)) {
                    String[] erperpDeliveryNoList = erpDeliveryNoStr.split(",");
                    //出库单
                    StringBuffer erpDeliveryNoEas = new StringBuffer();
                    for (String erpDeliveryNo : erperpDeliveryNoList) {
                        erpDeliveryNoEas.append("\"").append(erpDeliveryNo).append("\",");
                    }
                    String str = erpDeliveryNoEas.toString();
                    request1.setDataId(str.substring(0, str.length() - 1));
                    erpDeleteDataApi.saveOrUpdateErpDeleteData(request1);
                    orderInvoiceApplyProcessApi.removeErpReceivableNo(easBillNumber);
                    return Result.success();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.failed(OpenErrorCode.ERP_SYNC_ERROR);
    }
}
