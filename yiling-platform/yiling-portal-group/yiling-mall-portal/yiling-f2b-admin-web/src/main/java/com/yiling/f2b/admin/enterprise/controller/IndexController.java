package com.yiling.f2b.admin.enterprise.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.f2b.admin.enterprise.vo.IndexOrderNumberVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * POP首页接口
 *
 * @author:wei.wang
 * @date:2021/8/10
 */

@RestController
@RequestMapping("/index")
@Api(tags = "POP后台首页")
@Slf4j
public class IndexController extends BaseController {

    @DubboReference
    OrderApi       orderApi;
    @DubboReference
    OrderReturnApi orderReturnApi;
    @DubboReference
    EnterpriseApi  enterpriseApi;

    @ApiOperation(value = "获取采购销售处理事物接口")
    @GetMapping("/get/order/number")
    public Result<IndexOrderNumberVO> getOrderNumber(@CurrentUser CurrentStaffInfo staffInfo) {
        IndexOrderNumberVO result = new IndexOrderNumberVO();
        EnterpriseChannelEnum enterpriseChannel = staffInfo.getEnterpriseChannel();
        if(staffInfo.getYilingAdminFlag()){
            List<Long> list = enterpriseApi.listSubEids(Constants.YILING_EID);
            Integer sellerUnDeliveryNum = orderApi.getSellerOrderNumberByStatus(OrderStatusEnum.UNDELIVERED.getCode(),
                    list,1,staffInfo.getCurrentUserId());
            Integer sellerDeliveryNum = orderApi.getSellerOrderNumberByStatus(OrderStatusEnum.DELIVERED.getCode(),
                    list,1,staffInfo.getCurrentUserId());
            Integer sellerReturningNum = orderReturnApi.getSellerOrderReturnNumberByReturnStatus(OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode(),
                    list,1,staffInfo.getCurrentUserId());
            result.setSellerDeliveryNum(sellerDeliveryNum);
            result.setSellerReturningNum(sellerReturningNum);
            result.setSellerUnDeliveryNum(sellerUnDeliveryNum);
        } else if(staffInfo.getYilingFlag()){
            List<Long> list = enterpriseApi.listSubEids(Constants.YILING_EID);
            Integer sellerUnDeliveryNum = orderApi.getSellerOrderNumberByStatus(OrderStatusEnum.UNDELIVERED.getCode(),
                    list,2,staffInfo.getCurrentUserId());
            Integer sellerDeliveryNum = orderApi.getSellerOrderNumberByStatus(OrderStatusEnum.DELIVERED.getCode(),
                    list,2,staffInfo.getCurrentUserId());
            Integer sellerReturningNum = orderReturnApi.getSellerOrderReturnNumberByReturnStatus(OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode(),
                    list,2,staffInfo.getCurrentUserId());
            result.setSellerDeliveryNum(sellerDeliveryNum);
            result.setSellerReturningNum(sellerReturningNum);
            result.setSellerUnDeliveryNum(sellerUnDeliveryNum);
        } else if(EnterpriseChannelEnum.getByCode(EnterpriseChannelEnum.KA.getCode()) == enterpriseChannel){
            List<Long> list = new ArrayList<>();
            list.add(staffInfo.getCurrentEid());
            Integer buyerUnPaymentNum = orderApi.getBuyerOrderNumberByPaymentStatus(PaymentStatusEnum.UNPAID.getCode(), list);
            Integer buyerUnDeliveryNum = orderApi.getBuyerOrderNumberByStatus(OrderStatusEnum.UNDELIVERED.getCode(), list);
            Integer buyerUnReceiveNum = orderApi.getBuyerOrderNumberByStatus(OrderStatusEnum.DELIVERED.getCode(), list);
            result.setBuyerUnPaymentNum(buyerUnPaymentNum);
            result.setBuyerUnDeliveryNum(buyerUnDeliveryNum);
            result.setBuyerUnReceiveNum(buyerUnReceiveNum);
        }else{
            List<Long> list = new ArrayList<>();
            list.add(staffInfo.getCurrentEid());
            Integer sellerUnDeliveryNum = orderApi.getSellerOrderNumberByStatus(OrderStatusEnum.UNDELIVERED.getCode(), list,3,staffInfo.getCurrentUserId());
            Integer sellerDeliveryNum = orderApi.getSellerOrderNumberByStatus(OrderStatusEnum.DELIVERED.getCode(), list,3,staffInfo.getCurrentUserId());
            Integer sellerReturningNum = orderReturnApi.getSellerOrderReturnNumberByReturnStatus(OrderReturnStatusEnum.ORDER_RETURN_PENDING.getCode(), list,3,staffInfo.getCurrentUserId());
            Integer buyerUnPaymentNum = orderApi.getBuyerOrderNumberByPaymentStatus(PaymentStatusEnum.UNPAID.getCode(), list);
            Integer buyerUnDeliveryNum = orderApi.getBuyerOrderNumberByStatus(OrderStatusEnum.UNDELIVERED.getCode(), list);
            Integer buyerUnReceiveNum = orderApi.getBuyerOrderNumberByStatus(OrderStatusEnum.DELIVERED.getCode(), list);

            result.setSellerDeliveryNum(sellerDeliveryNum);
            result.setSellerReturningNum(sellerReturningNum);
            result.setSellerUnDeliveryNum(sellerUnDeliveryNum);
            result.setBuyerUnPaymentNum(buyerUnPaymentNum);
            result.setBuyerUnDeliveryNum(buyerUnDeliveryNum);
            result.setBuyerUnReceiveNum(buyerUnReceiveNum);
        }
        return Result.success(result);
    }
}
