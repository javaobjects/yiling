package com.yiling.f2b.web.payment.controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.f2b.web.payment.form.PaymentForm;
import com.yiling.f2b.web.payment.form.QueryReceiptDeskOrderListForm;
import com.yiling.f2b.web.payment.form.SelectOrderPaymentMethodForm;
import com.yiling.f2b.web.payment.vo.OrderDiscountAmountVO;
import com.yiling.f2b.web.payment.vo.ReceiptDeskPageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.mall.payment.api.PaymentApi;
import com.yiling.mall.payment.dto.request.PaymentRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.payment.enums.PaymentErrorCode;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysCompanyDTO;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.PaymentMethodDTO;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/6/28
 */
@RestController
@RequestMapping("/payment")
@Api(tags = "支付模块接口")
@Slf4j
public class PaymentController extends BaseController {

    @DubboReference
    OrderApi                orderApi;
    @DubboReference
    PaymentApi              paymentApi;
    @DubboReference
    PaymentMethodApi        paymentMethodApi;
    @DubboReference
    PaymentDaysAccountApi   paymentDaysAccountApi;
    @DubboReference
    DataPermissionsApi      dataPermissionsApi;

    @ApiOperation(value = "收营台订单列表")
    @PostMapping("/receiptDeskOrderList")
    @UserAccessAuthentication
    public Result<ReceiptDeskPageVO> receiptDeskOrderList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryReceiptDeskOrderListForm form) {
        // 订单列表
        List<OrderDTO> orderDTOList = orderApi.listByOrderNos(form.getOrderNos());
        if (CollUtil.isEmpty(orderDTOList)) {
            return Result.failed("未找到相关订单信息");
        }
        // 校验订单状态是否发生变更
        boolean orderStatusChange = orderDTOList.stream().filter(e -> OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(e.getOrderStatus())).findAny().isPresent();
        if (orderStatusChange) {
            return Result.failed("订单已取消,请重新下单!");
        }
        List<Long> contacterList = Optional.ofNullable(
                dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId()))
                .orElse(ListUtil.empty());
        orderDTOList = orderDTOList.stream().filter(
                t -> ObjectUtil.equal(t.getBuyerEid(),staffInfo.getCurrentEid()) || contacterList.contains(t.getContacterId())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(orderDTOList)) {
            return Result.failed("未查询到当前登录人的相关订单!");
        }

        ReceiptDeskPageVO receiptDeskPageVO = new ReceiptDeskPageVO();
        receiptDeskPageVO.setOrderNum(orderDTOList.size());
        receiptDeskPageVO.setTotalAmount(orderDTOList.stream().map(OrderDTO::getTotalAmount).reduce(BigDecimal::add).get());
        receiptDeskPageVO.setSelectedAmount(BigDecimal.ZERO);
        receiptDeskPageVO.setDiscountAmount(BigDecimal.ZERO);
        receiptDeskPageVO.setPaymentAmount(orderDTOList.stream().map(OrderDTO::getPaymentAmount).reduce(BigDecimal::add).get());
        List<ReceiptDeskPageVO.OrderListItemVO> orderListVO = CollUtil.newArrayList();
        orderDTOList.forEach(orderInfo -> {
            ReceiptDeskPageVO.OrderListItemVO orderListItemVO = new ReceiptDeskPageVO.OrderListItemVO();
            orderListItemVO.setOrderId(orderInfo.getId());
            orderListItemVO.setOrderNo(orderInfo.getOrderNo());
            orderListItemVO.setDistributorEid(orderInfo.getDistributorEid());
            orderListItemVO.setDistributorEname(orderInfo.getDistributorEname());
            orderListItemVO.setTotalAmount(orderInfo.getTotalAmount());
            orderListItemVO.setDiscountAmount(BigDecimal.ZERO);
            orderListItemVO.setContractNumber(orderInfo.getContractNumber());
            List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodApi.listByEidAndCustomerEid(orderInfo.getSellerEid(), orderInfo.getBuyerEid(), PlatformEnum.POP);
            if (CollUtil.isEmpty(paymentMethodDTOList)) {
                orderListItemVO.setPaymentTypeList(ListUtil.empty());
                orderListItemVO.setSelectEnabled(false);
            } else {
                Map<Integer, List<PaymentMethodDTO>> customerPaymentMethodDTOMap = paymentMethodDTOList.stream().collect(Collectors.groupingBy(PaymentMethodDTO::getType));

                List<ReceiptDeskPageVO.PaymentTypeVO> paymentTypeVOList = CollUtil.newArrayList();
                for (Integer type : customerPaymentMethodDTOMap.keySet()) {
                    ReceiptDeskPageVO.PaymentTypeVO paymentTypeVO = new ReceiptDeskPageVO.PaymentTypeVO();
                    paymentTypeVO.setId(type);

                    List<ReceiptDeskPageVO.PaymentMethodVO> paymentMethodVOList = CollUtil.newArrayList();
                    paymentMethodDTOList.forEach(paymentMethod -> {
                        ReceiptDeskPageVO.PaymentMethodVO paymentMethodVO = new ReceiptDeskPageVO.PaymentMethodVO();
                        paymentMethodVO.setId(paymentMethod.getCode());
                        paymentMethodVO.setName(paymentMethod.getName());
                        paymentMethodVO.setEnabled(true);
                        paymentMethodVOList.add(paymentMethodVO);
                    });
                    paymentTypeVO.setPaymentMethodList(paymentMethodVOList);

                    paymentTypeVOList.add(paymentTypeVO);
                }
                orderListItemVO.setPaymentTypeList(paymentTypeVOList);
            }

            orderListVO.add(orderListItemVO);
        });
        receiptDeskPageVO.setOrderList(orderListVO);

        // 设置selectEnabled字段
        receiptDeskPageVO.getOrderList().forEach(orderInfo -> {
            if (orderInfo.getSelectEnabled() == null) {
                List<ReceiptDeskPageVO.PaymentMethodVO> orderPaymentMethodList = orderInfo.getPaymentTypeList().stream().map(ReceiptDeskPageVO.PaymentTypeVO::getPaymentMethodList).flatMap(Collection::stream).collect(Collectors.toList());
                Optional optional = orderPaymentMethodList.stream().filter(e -> e.getEnabled()).findAny();
                orderInfo.setSelectEnabled(optional.isPresent());
                orderInfo.setSelected(false);
            }
        });

        return Result.success(receiptDeskPageVO);
    }

    @ApiOperation(value = "选择订单支付方式")
    @PostMapping("/selectOrderPaymentMethod")
    @UserAccessAuthentication
    public Result<OrderDiscountAmountVO> selectOrderPaymentMethod(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SelectOrderPaymentMethodForm form) {
        Long orderId = form.getOrderId();
        OrderDTO orderDTO = orderApi.getOrderInfo(orderId);
        if (orderDTO == null) {
            return Result.failed("订单信息不存在");
        }

        // 判断订单是否已支付
        if (orderDTO.getPaymentMethod() != 0) {
            return Result.failed("该订单已支付过，请勿重复支付");
        }

        // 校验支付方式
        List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodApi.listByEidAndCustomerEid(orderDTO.getSellerEid(), orderDTO.getBuyerEid(), PlatformEnum.POP);
        if (CollUtil.isEmpty(paymentMethodDTOList)) {
            throw new BusinessException(PaymentErrorCode.NO_PAYMENT_METHOD);
        }
        List<Long> paymentMethodIds = paymentMethodDTOList.stream().map(PaymentMethodDTO::getCode).collect(Collectors.toList());
        if (!paymentMethodIds.contains(form.getPaymentMethodId())) {
            throw new BusinessException(PaymentErrorCode.PAYMENT_METHOD_UNUSABLE);
        }

        Long sellerEid = orderDTO.getSellerEid();

        PaymentRequest.OrderPaymentRequest request = new PaymentRequest.OrderPaymentRequest();
        request.setOrderId(orderId);
        request.setPaymentMethodId(form.getPaymentMethodId());
        // 计算现折金额
        BigDecimal orderCashDiscountAmount = paymentApi.calculateOrderCashDiscountAmount(request);

        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.getByCode(form.getPaymentMethodId());
        if (paymentMethodEnum == PaymentMethodEnum.PAYMENT_DAYS) {
            // 账期可用额度
            BigDecimal paymentDaysAvailableAmount = paymentDaysAccountApi.getAvailableAmountByCustomerEid(sellerEid, orderDTO.getBuyerEid());
            // 订单应付金额
            BigDecimal orderPaymentAmount = NumberUtil.sub(orderDTO.getTotalAmount(), orderDTO.getFreightAmount(), orderCashDiscountAmount);
            if (paymentDaysAvailableAmount.compareTo(orderPaymentAmount) < 0) {
                throw new BusinessException(UserErrorCode.PAYMENT_DAYS_AMOUNT_LESS);
            }
            //获取集团可用账期，校验集团账期是否足够
            PaymentDaysCompanyDTO paymentDaysCompanyDTO = paymentDaysAccountApi.get();
            BigDecimal availableAmount = paymentDaysCompanyDTO.getTotalAmount().subtract(paymentDaysCompanyDTO.getUsedAmount()).add(paymentDaysCompanyDTO.getRepaymentAmount());
            if (availableAmount.compareTo(orderPaymentAmount) < 0) {
                throw new BusinessException(UserErrorCode.PAYMENT_DAYS_COMPANY_ERROR);
            }
        }

        return Result.success(new OrderDiscountAmountVO(orderId, orderCashDiscountAmount));
    }

    @ApiOperation(value = "支付")
    @PostMapping("/pay")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<BoolObject> pay(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid PaymentForm form) {
        PaymentRequest request = PojoUtils.map(form, PaymentRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOrderType(OrderTypeEnum.POP.getCode());

        boolean result = paymentApi.pay(request);
        return Result.success(new BoolObject(result));
    }

}
