package com.yiling.b2b.admin.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.yiling.b2b.admin.order.form.UpdateTransportInfoForm;
import com.yiling.b2b.admin.order.vo.OrderNumberInfoPage;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.order.order.api.PresaleOrderApi;
import com.yiling.order.order.dto.PresaleOrderDTO;
import com.yiling.order.order.dto.request.UpdateTransportInfoRequest;
import com.yiling.order.order.enums.OrderCategoryEnum;
import com.yiling.order.order.enums.OrderErpPushStatusEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.user.enterprise.enums.ErpSyncLevelEnum;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.admin.order.form.QueryOrderInfoForm;
import com.yiling.b2b.admin.order.form.SaveOrderDeliveryListInfoForm;
import com.yiling.b2b.admin.order.vo.OrderAddressVO;
import com.yiling.b2b.admin.order.vo.OrderCountNumberVO;
import com.yiling.b2b.admin.order.vo.OrderDeliveryVO;
import com.yiling.b2b.admin.order.vo.OrderDetailDeliveryVO;
import com.yiling.b2b.admin.order.vo.OrderGiftVO;
import com.yiling.b2b.admin.order.vo.OrderLogVO;
import com.yiling.b2b.admin.order.vo.OrderPageVO;
import com.yiling.b2b.admin.order.vo.OrderSelleDetailVO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.marketing.goodsgift.api.GoodsGiftApi;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDTO;
import com.yiling.marketing.promotion.api.PromotionGoodsGiftLimitApi;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderGiftApi;
import com.yiling.order.order.api.OrderLogApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderGiftDTO;
import com.yiling.order.order.dto.OrderLogDTO;
import com.yiling.order.order.dto.OrderNumberDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.OrderReturnDetailDTO;
import com.yiling.order.order.dto.request.QueryOrderPageRequest;
import com.yiling.order.order.dto.request.QueryOrderReturnPageRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryListInfoRequest;
import com.yiling.order.order.enums.OrderErpPushStatusEnum;
import com.yiling.order.order.enums.OrderRangeTypeEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.ErpSyncLevelEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * B2B商家后台订单
 *
 * @author:wei.wang
 * @date:2021/10/22
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Api(tags = "订单接口")
public class OrderController extends BaseController {
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderReturnApi orderReturnApi;
    @DubboReference
    OrderAddressApi orderAddressApi;
    @DubboReference
    OrderLogApi orderLogApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderDeliveryApi orderDeliveryApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    OrderProcessApi orderMallApi;
    @DubboReference
    OrderGiftApi orderGiftApi;
    @DubboReference
    PresaleOrderApi presaleOrderApi;
    @DubboReference
    GoodsGiftApi goodsGiftApi;

    @Autowired
    PictureUrlUtils pictureUrlUtils;
    @Autowired
    private FileService fileService;

    @ApiOperation(value = "订单列表")
    @PostMapping("/list")
    public Result<OrderNumberInfoPage> getOrderPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryOrderInfoForm form) {
        QueryOrderPageRequest request = PojoUtils.map(form, QueryOrderPageRequest.class);
        request.setOrderType(OrderTypeEnum.B2B.getCode());

        OrderNumberInfoPage orderNumberInfoPage = new OrderNumberInfoPage();
        List<Long> list = new ArrayList<>();

        //取消订单按钮
        Boolean judgeCancelFlag = false;
        //发货订单按钮
        Boolean judgeDeliveryFlag = false;
        request.setType(1);
        list.add(staffInfo.getCurrentEid());

        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(staffInfo.getCurrentEid());
        if (enterpriseDTO.getErpSyncLevel().compareTo(ErpSyncLevelEnum.ORDER_DOCKING.getCode()) <= 0) {
            judgeDeliveryFlag = true;
        }
        if (enterpriseDTO.getErpSyncLevel().compareTo(ErpSyncLevelEnum.BASED_DOCKING.getCode()) <= 0) {
            judgeCancelFlag = true;
        }

        request.setEidList(list);

        Page<OrderDTO> orderPage = orderApi.getOrderPage(request);
        Page<OrderPageVO> page = PojoUtils.map(orderPage, OrderPageVO.class);
        List<Long> goodsIds = new ArrayList<>();
        if (orderPage != null && CollectionUtil.isNotEmpty(orderPage.getRecords())) {
            List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());

            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(ids);
            List<OrderDetailDTO> orderDetailList = orderDetailApi.getOrderDetailByOrderIds(ids);
            Map<Long,BigDecimal> detailMap= orderDetailList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, OrderDetailDTO::getOriginalPrice));
            Map<Long, List<OrderDetailChangeDTO>> detailChangeMap = new HashMap<>();
            for (OrderDetailChangeDTO changeOne : orderDetailChangeList) {
                if (detailChangeMap.containsKey(changeOne.getOrderId())) {
                    List<OrderDetailChangeDTO> changeList = detailChangeMap.get(changeOne.getOrderId());
                    changeList.add(changeOne);
                } else {
                    detailChangeMap.put(changeOne.getOrderId(), new ArrayList<OrderDetailChangeDTO>() {{
                        add(changeOne);
                    }});
                }
            }

            for (OrderPageVO one : page.getRecords()) {
                //货款总额
                BigDecimal totalAmount = BigDecimal.ZERO;
                //原价总金额
                BigDecimal originalAmount = BigDecimal.ZERO;

                //还款总金额
                BigDecimal paymentDayAmount = BigDecimal.ZERO;
                //折扣金额
                BigDecimal discountAmount = BigDecimal.ZERO;
                //发货商品数量
                Integer deliveryOrderPieceNum = 0;
                //商品购买件数
                Integer goodsOrderPieceNum = 0;

                List<OrderDetailChangeDTO> detailChangeList = detailChangeMap.get(one.getId());
                for (OrderDetailChangeDTO orderDetailChangeOne : detailChangeList) {
                    BigDecimal originalPrice = detailMap.get(orderDetailChangeOne.getDetailId());
                    originalAmount = originalAmount.add(originalPrice.multiply(BigDecimal.valueOf(orderDetailChangeOne.getGoodsQuantity())));
                    totalAmount = totalAmount.add(orderDetailChangeOne.getGoodsAmount());
                    discountAmount = discountAmount.add(orderDetailChangeOne.getCouponDiscountAmount().add(orderDetailChangeOne.getPlatformCouponDiscountAmount()).add(orderDetailChangeOne.getPresaleDiscountAmount()).add(orderDetailChangeOne.getPlatformPaymentDiscountAmount()).add(orderDetailChangeOne.getShopPaymentDiscountAmount()));
                    goodsOrderPieceNum = goodsOrderPieceNum + orderDetailChangeOne.getGoodsQuantity();
                    deliveryOrderPieceNum = deliveryOrderPieceNum + orderDetailChangeOne.getDeliveryQuantity();
                    paymentDayAmount = paymentDayAmount.add(orderDetailChangeOne.getGoodsAmount()
                            .subtract(orderDetailChangeOne.getCouponDiscountAmount())
                            .subtract(orderDetailChangeOne.getPresaleDiscountAmount())
                            .subtract(orderDetailChangeOne.getCashDiscountAmount())
                            .subtract(orderDetailChangeOne.getTicketDiscountAmount())
                            .subtract(orderDetailChangeOne.getPlatformCouponDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerReturnAmount())
                            .add(orderDetailChangeOne.getSellerReturnCashDiscountAmount())
                            .add(orderDetailChangeOne.getSellerReturnTicketDiscountAmount())
                            .add(orderDetailChangeOne.getSellerPlatformCouponDiscountAmount())
                            .add(orderDetailChangeOne.getSellerCouponDiscountAmount())
                            .add(orderDetailChangeOne.getSellerPresaleDiscountAmount())
                            .subtract(orderDetailChangeOne.getReturnAmount())
                            .add(orderDetailChangeOne.getReturnCashDiscountAmount())
                            .add(orderDetailChangeOne.getReturnCouponDiscountAmount())
                            .add(orderDetailChangeOne.getReturnPlatformCouponDiscountAmount())
                            .add(orderDetailChangeOne.getReturnPresaleDiscountAmount())
                            .add(orderDetailChangeOne.getReturnTicketDiscountAmount()));
                    goodsIds.add(orderDetailChangeOne.getGoodsId());
                }
                //设置金额

                one.setPaymentDayAmount(paymentDayAmount);

                one.setDiscountAmount(discountAmount)
                        .setTotalAmount(totalAmount)
                        .setOriginalAmount(originalAmount)
                        .setPaymentAmount(totalAmount.subtract(discountAmount));

                if(PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(one.getPaymentStatus())
                        && PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(Long.valueOf(one.getPaymentMethod()))
                        && OrderStatusEnum.CANCELED != OrderStatusEnum.getByCode(one.getOrderStatus())){

                    one.setPaymentDaySubmitFlag(true);
                }

                //设置取消和发货按钮
                one.setJudgeDeliveryFlag(false);
                one.setJudgeCancelFlag(false);
                if(OrderStatusEnum.getByCode(one.getOrderStatus()) == OrderStatusEnum.UNAUDITED ){
                    PresaleOrderDTO orderInfo = presaleOrderApi.getOrderInfo(one.getId());
                    if(orderInfo != null && PaymentStatusEnum.getByCode(one.getPaymentStatus()) == PaymentStatusEnum.PARTPAID ){
                        if(orderInfo.getBalanceEndTime().getTime() >= System.currentTimeMillis() ){
                            one.setJudgeCancelFlag(true);
                        }
                    }
                }
                if(OrderStatusEnum.getByCode(one.getOrderStatus()) == OrderStatusEnum.UNDELIVERED ){
                    if (judgeDeliveryFlag && one.getPaymentMethod().compareTo(0) != 0) {
                        one.setJudgeDeliveryFlag(true);
                    }

                    if (judgeCancelFlag && (OrderErpPushStatusEnum.getByCode(one.getErpPushStatus()) == OrderErpPushStatusEnum.NOT_PUSH
                            || OrderErpPushStatusEnum.getByCode(one.getErpPushStatus()) == OrderErpPushStatusEnum.PUSH_FAIL) && OrderCategoryEnum.NORMAL == OrderCategoryEnum.getByCode(one.getOrderCategory()) ) {

                        one.setJudgeCancelFlag(true);

                    }
                }

                one.setGoodsOrderNum(detailChangeList.size()).setGoodsOrderPieceNum(goodsOrderPieceNum).setDeliveryOrderPieceNum(deliveryOrderPieceNum);
            }
            Map<Long, List<OrderReturnDetailDTO>> returnMap = orderReturnApi.queryByOrderIdList(ids);
            for (OrderPageVO one : page.getRecords()) {
                Integer returnNumber = 0;
                List<OrderReturnDetailDTO> orderReturnDetailDTOS = returnMap.get(one.getId());

                if (CollectionUtil.isNotEmpty(orderReturnDetailDTOS)) {
                    for (OrderReturnDetailDTO returnOne : orderReturnDetailDTOS) {
                        returnNumber = returnNumber + returnOne.getReturnQuantity();
                    }
                    if (returnNumber.compareTo(one.getGoodsOrderPieceNum()) == 0) {
                        one.setOrderReturnSubmitFlag(Boolean.FALSE);
                    }
                }
                if (request.getType() == 1) {
                    one.setOrderReturnSubmitFlag(Boolean.FALSE);
                }
                QueryOrderReturnPageRequest returnPageRequest = new QueryOrderReturnPageRequest();
                returnPageRequest.setOrderId(one.getId());
                Page<OrderReturnDTO> orderReturnPage = orderReturnApi.queryOrderReturnPage(returnPageRequest);
                if (orderReturnPage != null && CollectionUtil.isNotEmpty(orderReturnPage.getRecords())) {
                    one.setOrderReturnFlag(Boolean.TRUE);
                } else {
                    one.setOrderReturnFlag(Boolean.FALSE);
                }
            }
        }
        orderNumberInfoPage.setOrderPage(page);
        request.setStateType(1);
        Long unDeliverQuantity = orderApi.getB2BAdminNumber(request);
        orderNumberInfoPage.setUnDeliverQuantity(unDeliverQuantity);

        request.setStateType(2);
        Long receiveQuantity = orderApi.getB2BAdminNumber(request);
        orderNumberInfoPage.setReceiveQuantity(receiveQuantity);

        request.setStateType(3);
        Long paymentDayQuantity = orderApi.getB2BAdminNumber(request);
        orderNumberInfoPage.setPaymentDayQuantity(paymentDayQuantity);

        return Result.success(orderNumberInfoPage);
    }

    @ApiOperation(value = "销售订单详情")
    @GetMapping("/detail")
    public Result<OrderSelleDetailVO> getOrderSaleDetailAllInfo(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "orderId") Long orderId) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        OrderSelleDetailVO result = PojoUtils.map(orderInfo, OrderSelleDetailVO.class);

        OrderAddressDTO addressInfo = orderAddressApi.getOrderAddressInfo(orderId);
        OrderAddressVO address = PojoUtils.map(addressInfo, OrderAddressVO.class);
        if (addressInfo != null) {
            address.setAddress(addressInfo.getAddress());
        }
        result.setOrderAddress(address);

        List<OrderLogDTO> orderLogInfo = orderLogApi.getOrderLogInfo(orderId);
        result.setOrderLogInfo(PojoUtils.map(orderLogInfo, OrderLogVO.class));

        List<OrderDetailDTO> orderDetailInfo = orderDetailApi.getOrderDetailInfo(orderId);
        List<OrderDetailDeliveryVO> orderDetailDeliveryList = new ArrayList<>();
        List<Long> listIds = new ArrayList<>();
        List<Long> goodsSukIds = new ArrayList<>();
        Boolean flag = OrderStatusEnum.PARTDELIVERED.getCode() <= result.getOrderStatus();
        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(orderId);
        Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));
        //货款总额
        BigDecimal totalAmount = BigDecimal.ZERO;
        //原价总金额
        BigDecimal originalAmount = BigDecimal.ZERO;
        //优惠金额
        BigDecimal discountAmount = BigDecimal.ZERO;

        for (OrderDetailDTO one : orderDetailInfo) {
            OrderDetailDeliveryVO orderDetailDelivery = PojoUtils.map(one, OrderDetailDeliveryVO.class);
            OrderDetailChangeDTO orderDetailChangeOne = orderDetailChangeMap.get(one.getId());

            if (flag) {
                List<OrderDeliveryDTO> deliveryList = orderDeliveryApi.getOrderDeliveryList(orderId);
                List<OrderDeliveryDTO> list = new ArrayList<>();
                for (OrderDeliveryDTO delivery : deliveryList) {
                    if (one.getId().equals(delivery.getDetailId()) && one.getGoodsId().equals(delivery.getGoodsId())) {
                        list.add(delivery);
                    }
                }
                orderDetailDelivery.setOrderDeliveryList(PojoUtils.map(list, OrderDeliveryVO.class));
            }
            orderDetailDelivery.setGoodsQuantity(orderDetailChangeOne.getGoodsQuantity())
                    .setDeliveryQuantity(orderDetailChangeOne.getDeliveryQuantity())
                    .setGoodsPrice(orderDetailChangeOne.getGoodsPrice())
                    .setGoodsAmount(orderDetailChangeOne.getGoodsAmount().subtract(orderDetailChangeOne.getSellerReturnAmount()))
                    .setCouponDiscountAmount(orderDetailChangeOne.getCouponDiscountAmount()
                    .add(orderDetailChangeOne.getPlatformCouponDiscountAmount())
                    .add(orderDetailChangeOne.getPresaleDiscountAmount())
                    .add(orderDetailChangeOne.getPlatformPaymentDiscountAmount())
                    .add(orderDetailChangeOne.getShopPaymentDiscountAmount()));
            orderDetailDelivery.setRealAmount(orderDetailDelivery.getGoodsAmount().subtract(orderDetailDelivery.getCouponDiscountAmount()));
            discountAmount = discountAmount.add(orderDetailDelivery.getCouponDiscountAmount());
            totalAmount = totalAmount.add(orderDetailChangeOne.getGoodsAmount());
            originalAmount = originalAmount.add(BigDecimal.valueOf(orderDetailChangeOne.getGoodsQuantity()).multiply(one.getOriginalPrice()));
            orderDetailDeliveryList.add(orderDetailDelivery);
            listIds.add(one.getGoodsId());
            goodsSukIds.add(one.getGoodsSkuId());
        }

        List<OrderGiftDTO> orderGiftList = orderGiftApi.listByOrderId(orderId);
        if (CollectionUtil.isNotEmpty(orderGiftList)) {
            List<Long> longs = orderGiftList.stream().map(order -> order.getGoodsGiftId()).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(longs)) {
                List<GoodsGiftDTO> goodsGiftList = goodsGiftApi.listByListId(longs);
                List<OrderGiftVO> orderGiftResult = PojoUtils.map(goodsGiftList, OrderGiftVO.class);
                for (OrderGiftVO giftOne : orderGiftResult) {
                    if (StringUtils.isNotBlank(giftOne.getPictureUrl())) {
                        giftOne.setPictureUrl(fileService.getUrl(giftOne.getPictureUrl(), FileTypeEnum.MARKETING_GOODS_GIFT_PICTURE));
                    }
                }
                result.setOrderGiftList(orderGiftResult);
            }
        }

        result.setTotalAmount(totalAmount);
        result.setOriginalAmount(originalAmount);

        List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSukIds);
        Map<Long, GoodsSkuDTO> skuGoodsMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(goodsSkuByIds)) {
            skuGoodsMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
        }
        Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(listIds);
        for (OrderDetailDeliveryVO one : orderDetailDeliveryList) {
            one.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(one.getGoodsId())));
            one.setPackageNumber(skuGoodsMap.get(one.getGoodsSkuId()) != null ? skuGoodsMap.get(one.getGoodsSkuId()).getPackageNumber() : 1L);
            one.setGoodsRemark(skuGoodsMap.get(one.getGoodsSkuId()) != null ? skuGoodsMap.get(one.getGoodsSkuId()).getRemark() : "");
        }

        result.setPaymentAmount(totalAmount.subtract(discountAmount));
        result.setDiscountAmount(discountAmount);
        result.setOrderDetailDelivery(orderDetailDeliveryList);
        return Result.success(result);
    }

    @ApiOperation(value = "销售订单发货")
    @PostMapping("/delivery")
    public Result saveOrderDelivery(@CurrentUser CurrentStaffInfo staffInfo, @Valid @RequestBody SaveOrderDeliveryListInfoForm form) {
        SaveOrderDeliveryListInfoRequest request = PojoUtils.map(form, SaveOrderDeliveryListInfoRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        Boolean result = orderMallApi.frontDelivery(request);
        return Result.success(result);
    }

    @ApiOperation(value = "取消订单")
    @GetMapping("/cancel")
    public Result cancel(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "orderId") Long orderId) {

        Boolean result = orderMallApi.cancelOrderB2BAdmin(orderId, staffInfo.getCurrentUserId());
        return Result.success(result);
    }

    @ApiOperation(value = "订单数量接口")
    @GetMapping("/get/number")
    public Result<OrderCountNumberVO> getOrderNumber(@CurrentUser CurrentStaffInfo staffInfo) {
        List<Long> eids = new ArrayList<Long>() {{
            add(staffInfo.getCurrentEid());
        }};
        OrderNumberDTO orderNumber = orderApi.getOrderNumber(OrderRangeTypeEnum.ORDER_SALE_NOT_YILINGH_RANGE_TYPE.getCode(), eids, staffInfo.getCurrentUserId(), OrderTypeEnum.B2B.getCode(), null);
        return Result.success(PojoUtils.map(orderNumber, OrderCountNumberVO.class));
    }


    @ApiOperation(value = "确认回款")
    @GetMapping("/confirm/payment")
    public Result confirmPaymentDay(@CurrentUser CurrentStaffInfo staffInfo,
                         @RequestParam(value = "orderId") Long orderId) {
        OrderDTO one = orderApi.getOrderInfo(orderId);

        if(PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(one.getPaymentStatus())
                && PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(Long.valueOf(one.getPaymentMethod()))){

            Boolean result = orderMallApi.confirmPaymentDayRepayment(orderId, staffInfo.getCurrentUserId());
            return Result.success(result);
        }else{
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_ERROR);
        }
    }
    @ApiOperation(value = "修改物流信息")
    @PostMapping("/update/transport")
    public Result updateTransportInfo(@CurrentUser CurrentStaffInfo staffInfo, @Valid @RequestBody UpdateTransportInfoForm form){
        UpdateTransportInfoRequest request = PojoUtils.map(form, UpdateTransportInfoRequest.class);
        Boolean result = orderApi.updateOrderTransportInfo(request);
        return Result.success(result);
    }

}