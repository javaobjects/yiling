package com.yiling.b2b.admin.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.admin.order.form.QueryOrderInfoForm;
import com.yiling.b2b.admin.order.vo.EnterpriseInfoVO;
import com.yiling.b2b.admin.order.vo.OrderDeliveryVO;
import com.yiling.b2b.admin.order.vo.OrderDetailDeliveryVO;
import com.yiling.b2b.admin.order.vo.OrderLogVO;
import com.yiling.b2b.admin.order.vo.OrderPurchaseDetailVO;
import com.yiling.b2b.admin.order.vo.OrderPurchaseVO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderLogApi;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.api.OrderReturnDetailBatchApi;
import com.yiling.order.order.api.PresaleOrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderLogDTO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.dto.OrderReturnDetailBatchDTO;
import com.yiling.order.order.dto.PresaleOrderDTO;
import com.yiling.order.order.dto.request.QueryOrderPageRequest;
import com.yiling.order.order.enums.OrderCategoryEnum;
import com.yiling.order.order.enums.OrderErpPushStatusEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PreSaleActivityTypeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.ErpSyncLevelEnum;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * B2B商家后台采购订单
 *
 * @author:wei.wang
 * @date:2021/10/22
 */
@Slf4j
@RestController
@RequestMapping("/order/purchase")
@Api(tags = "订单接口")
public class OrderPurchaseController extends BaseController {
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;

    @DubboReference
    OrderLogApi orderLogApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderDeliveryApi orderDeliveryApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    OrderProcessApi orderProcessApi;
    @DubboReference
    OrderReturnDetailBatchApi orderReturnDetailBatchApi;
    @DubboReference
    OrderPromotionActivityApi orderPromotionActivityApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    PresaleOrderApi presaleOrderApi;

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @ApiOperation(value = "采购订单列表")
    @PostMapping("/list")
    public Result<Page<OrderPurchaseVO>> getOrderPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryOrderInfoForm form) {
        QueryOrderPageRequest request = PojoUtils.map(form, QueryOrderPageRequest.class);
        request.setOrderType(OrderTypeEnum.B2B.getCode());

        List<Long> list = new ArrayList<>();
        request.setType(2);
        list.add(staffInfo.getCurrentEid());

        request.setEidList(list);

        Page<OrderDTO> orderPage = orderApi.getB2BPurchaseOrderPage(request);
        Page<OrderPurchaseVO> page = PojoUtils.map(orderPage, OrderPurchaseVO.class);
        List<Long> goodsIds = new ArrayList<>();
        if (orderPage != null && CollectionUtil.isNotEmpty(orderPage.getRecords())) {
            List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());

            List<Long> sellerEid = orderPage.getRecords().stream().map(order -> order.getSellerEid()).collect(Collectors.toList());
            List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(sellerEid);
            Map<Long, EnterpriseDTO> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, o -> o, (k1, k2) -> k1));

            List<OrderDetailDTO> orderDetailList = orderDetailApi.getOrderDetailByOrderIds(ids);
            Map<Long,BigDecimal> detailMap= orderDetailList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, OrderDetailDTO::getOriginalPrice));

            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(ids);
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

            for (OrderPurchaseVO one : page.getRecords()) {

                //货款总额
                BigDecimal totalAmount = BigDecimal.ZERO;
                //原价总金额
                BigDecimal originalAmount = BigDecimal.ZERO;

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
                    discountAmount = discountAmount.add(orderDetailChangeOne.getCouponDiscountAmount().add(orderDetailChangeOne.getPlatformCouponDiscountAmount()).add(orderDetailChangeOne.getPresaleDiscountAmount())
                            .add(orderDetailChangeOne.getPlatformPaymentDiscountAmount())
                            .add(orderDetailChangeOne.getShopPaymentDiscountAmount()));
                    goodsOrderPieceNum = goodsOrderPieceNum + orderDetailChangeOne.getGoodsQuantity();
                    deliveryOrderPieceNum = deliveryOrderPieceNum + orderDetailChangeOne.getDeliveryQuantity();

                    goodsIds.add(orderDetailChangeOne.getGoodsId());
                }
                //设置金额
                one.setDiscountAmount(discountAmount).setTotalAmount(totalAmount).setOriginalAmount(originalAmount).setPaymentAmount(totalAmount.subtract(discountAmount));

                EnterpriseDTO enterpriseDTO = enterpriseMap.get(one.getSellerEid());

                //设置取消和发货按钮
                Boolean cancelButtonFlag = false;
                if (PaymentMethodEnum.getByCode(Long.valueOf(one.getPaymentMethod())) == PaymentMethodEnum.ONLINE && PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(one.getPaymentStatus()) && OrderStatusEnum.UNAUDITED == OrderStatusEnum.getByCode(one.getOrderStatus())) {
                    cancelButtonFlag = true;
                } else if (OrderStatusEnum.UNDELIVERED == OrderStatusEnum.getByCode(one.getOrderStatus())) {

                    if (enterpriseDTO != null && OrderCategoryEnum.NORMAL == OrderCategoryEnum.getByCode(one.getOrderCategory()) && (enterpriseDTO.getErpSyncLevel().compareTo(ErpSyncLevelEnum.BASED_DOCKING.getCode()) <= 0) && (OrderErpPushStatusEnum.getByCode(one.getErpPushStatus()) == OrderErpPushStatusEnum.NOT_PUSH || OrderErpPushStatusEnum.getByCode(one.getErpPushStatus()) == OrderErpPushStatusEnum.PUSH_FAIL)) {

                        cancelButtonFlag = true;

                    }

                }
                OrderPromotionActivityDTO activityDTO = orderPromotionActivityApi.getOneByOrderIds(one.getId());

                //预售支付按钮类型
                Integer paymentNameType = 0;
                if (OrderCategoryEnum.PRESALE == OrderCategoryEnum.getByCode(one.getOrderCategory())) {
                    if (PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(one.getPaymentStatus())) {
                        paymentNameType = 1;
                    } else if (PaymentStatusEnum.PARTPAID == PaymentStatusEnum.getByCode(one.getPaymentStatus())) {
                        paymentNameType = 2;
                    }
                }

                //支付尾款控制按钮
                Boolean presaleButtonFlag = false;

                //Map<Long, Integer> promotionGoodsMap = new HashMap<>();
                if (activityDTO != null && PromotionActivityTypeEnum.PRESALE == PromotionActivityTypeEnum.getByCode(activityDTO.getActivityType())) {
                    PresaleOrderDTO presaleOrder = presaleOrderApi.getOrderInfo(one.getId());
                    if (presaleOrder != null) {
                        //判断类型为全款预售
                        one.setDepositAmount(presaleOrder.getDepositAmount());
                        one.setBalanceAmount(presaleOrder.getBalanceAmount());
                        if (PreSaleActivityTypeEnum.getByCode(presaleOrder.getActivityType()) == PreSaleActivityTypeEnum.FULL) {
                            paymentNameType = 2;
                            presaleButtonFlag = true;
                        } else if (PreSaleActivityTypeEnum.getByCode(presaleOrder.getActivityType()) == PreSaleActivityTypeEnum.DEPOSIT) {
                            long now = System.currentTimeMillis();
                            //
                            if ((presaleOrder.getBalanceStartTime().getTime() <= now) && (now <= presaleOrder.getBalanceEndTime().getTime())) {
                                presaleButtonFlag = true;
                            } else {
                                presaleButtonFlag = false;
                            }
                        }
                    }


                }
                one.setPresaleButtonFlag(presaleButtonFlag);
                one.setPaymentNameType(paymentNameType);
                one.setCancelButtonFlag(cancelButtonFlag);

                one.setGoodsOrderNum(detailChangeList.size()).setGoodsOrderPieceNum(goodsOrderPieceNum).setDeliveryOrderPieceNum(deliveryOrderPieceNum);

                if (OrderStatusEnum.DELIVERED == OrderStatusEnum.getByCode(one.getOrderStatus())) {
                    one.setReturnButtonFlag(true);
                    one.setReceiveButtonFlag(true);
                }

                if (PaymentMethodEnum.getByCode(Long.valueOf(one.getPaymentMethod())) == PaymentMethodEnum.ONLINE && PaymentStatusEnum.PAID != PaymentStatusEnum.getByCode(one.getPaymentStatus()) && OrderStatusEnum.UNAUDITED == OrderStatusEnum.getByCode(one.getOrderStatus())) {
                    one.setPaymentButtonFlag(true);
                }
                if(OrderStatusEnum.RECEIVED == OrderStatusEnum.getByCode(one.getOrderStatus())
                        || OrderStatusEnum.FINISHED == OrderStatusEnum.getByCode(one.getOrderStatus())
                        || OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(one.getOrderStatus())){
                    one.setDeleteButtonFlag(true);
                }
            }

        }

        return Result.success(page);
    }

    @ApiOperation(value = "采购订单详情")
    @GetMapping("/detail")
    public Result<OrderPurchaseDetailVO> getOrderSaleDetailAllInfo(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "orderId") Long orderId) {
        OrderDTO orderOne = orderApi.getOrderInfo(orderId);
        OrderPurchaseDetailVO result = PojoUtils.map(orderOne, OrderPurchaseDetailVO.class);

        EnterpriseDTO enterpriseDistributor = enterpriseApi.getById(orderOne.getSellerEid());
        EnterpriseInfoVO enterpriseInfo = PojoUtils.map(enterpriseDistributor, EnterpriseInfoVO.class);
        if (enterpriseDistributor != null) {
            enterpriseInfo.setAddress(enterpriseDistributor.getProvinceName() + enterpriseDistributor.getCityName() + enterpriseDistributor.getRegionName() + enterpriseDistributor.getAddress());
        }
        result.setSellerEnterpriseInfo(enterpriseInfo);

        List<OrderLogDTO> orderLogInfo = orderLogApi.getOrderLogInfo(orderId);
        result.setOrderLogInfo(PojoUtils.map(orderLogInfo, OrderLogVO.class));

        List<OrderDetailDTO> orderDetailInfo = orderDetailApi.getOrderDetailInfo(orderId);
        List<OrderDetailDeliveryVO> orderDetailDeliveryList = new ArrayList<>();
        List<Long> listIds = new ArrayList<>();
        List<Long> goodsSukIds = new ArrayList<>();
        Boolean flag = OrderStatusEnum.PARTDELIVERED.getCode() <= result.getOrderStatus();
        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(orderId);
        Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));

        //预售支付按钮类型
        Integer paymentNameType = 0;
        if (OrderCategoryEnum.PRESALE == OrderCategoryEnum.getByCode(orderOne.getOrderCategory())) {
            if (PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(orderOne.getPaymentStatus())) {
                paymentNameType = 1;
            } else if (PaymentStatusEnum.PARTPAID == PaymentStatusEnum.getByCode(orderOne.getPaymentStatus())) {
                paymentNameType = 2;
            }
        }

        //支付尾款控制按钮
        Boolean presaleButtonFlag = false;

        //组合包数量
        Map<Long, Integer> promotionGoodsMap = new HashMap<>();
        OrderPromotionActivityDTO activityDTO = orderPromotionActivityApi.getOneByOrderIds(orderId);
        if (activityDTO != null) {
            if (PromotionActivityTypeEnum.COMBINATION == PromotionActivityTypeEnum.getByCode(activityDTO.getActivityType())) {
                List<PromotionGoodsLimitDTO> promotionGoodsLimitList = promotionActivityApi.queryGoodsByActivityId(activityDTO.getActivityId());
                //促销的组合包允许购买数量
                promotionGoodsMap = promotionGoodsLimitList.stream().collect(Collectors.toMap(PromotionGoodsLimitDTO::getGoodsSkuId, PromotionGoodsLimitDTO::getAllowBuyCount, (k1, k2) -> k1));
            } else if (PromotionActivityTypeEnum.PRESALE == PromotionActivityTypeEnum.getByCode(activityDTO.getActivityType())) {
                PresaleOrderDTO orderInfo = presaleOrderApi.getOrderInfo(orderId);
                if (orderInfo != null) {
                    result.setDepositAmount(orderInfo.getDepositAmount());
                    result.setBalanceAmount(orderInfo.getBalanceAmount());
                    if (PreSaleActivityTypeEnum.getByCode(orderInfo.getActivityType()) == PreSaleActivityTypeEnum.FULL) {
                        paymentNameType = 2;
                        presaleButtonFlag = true;
                    } else if (PreSaleActivityTypeEnum.getByCode(orderInfo.getActivityType()) == PreSaleActivityTypeEnum.DEPOSIT) {
                        long now = DateUtil.date().getTime();
                        //
                        if ((orderInfo.getBalanceStartTime().getTime() <= now) && (now <= orderInfo.getBalanceEndTime().getTime())) {
                            presaleButtonFlag = true;
                        } else {
                            presaleButtonFlag = false;
                        }
                        if(now <= orderInfo.getBalanceEndTime().getTime()){
                            int d = (int) (((orderInfo.getBalanceEndTime().getTime() - now) / 86400000));
                            int h = (int) (((orderInfo.getBalanceEndTime().getTime() - now) % 86400000) / 3600000);
                            int m = (int) (((orderInfo.getBalanceEndTime().getTime() - now) % 86400000) % 3600000) / 60000;
                            int s = (int) ((((orderInfo.getBalanceEndTime().getTime() - now) % 86400000) % 3600000) % 60000) / 1000;

                            result.setPresaleRemainTime(d+"天"+h+"小时"+m + "分钟" + s + "秒");
                        }else{
                            result.setPresaleRemainTime(0+"天"+0+"小时"+0 + "分钟" + 0 + "秒");
                        }
                    }

                }
            }
        }

        List<Long> goodsSkuList = orderDetailInfo.stream().map(o -> o.getGoodsSkuId()).collect(Collectors.toList());
        List<GoodsSkuInfoDTO> goodsSkuInfoList = goodsApi.batchQueryInfoBySkuIds(goodsSkuList);
        Map<Long, String> sellUnitMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(goodsSkuInfoList)) {
            sellUnitMap = goodsSkuInfoList.stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, o -> o.getGoodsInfo().getSellUnit(), (k1, k2) -> k1));

        }

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
                List<OrderDeliveryVO> list = new ArrayList<>();
                for (OrderDeliveryDTO delivery : deliveryList) {
                    if (one.getId().equals(delivery.getDetailId()) && one.getGoodsId().equals(delivery.getGoodsId())) {
                        OrderDeliveryVO deliveryVO = PojoUtils.map(delivery, OrderDeliveryVO.class);

                        List<OrderReturnDetailBatchDTO> orderReturnDetailBatchList = orderReturnDetailBatchApi.queryByDetailIdAndBatchNoAndType(delivery.getDetailId(), delivery.getBatchNo(), null);
                        Integer returnQuantity = 0;
                        if (CollectionUtil.isNotEmpty(orderReturnDetailBatchList)) {
                            for (OrderReturnDetailBatchDTO returnOne : orderReturnDetailBatchList) {
                                returnQuantity = returnQuantity + returnOne.getReturnQuantity();
                            }

                        }
                        deliveryVO.setRemainReturnQuantity(deliveryVO.getDeliveryQuantity() - returnQuantity);
                        list.add(deliveryVO);

                    }

                }
                orderDetailDelivery.setOrderDeliveryList(list);
            }
            orderDetailDelivery.setGoodsQuantity(orderDetailChangeOne.getGoodsQuantity())
                    .setDeliveryQuantity(orderDetailChangeOne.getDeliveryQuantity())
                    .setGoodsPrice(orderDetailChangeOne.getGoodsPrice())
                    .setGoodsAmount(orderDetailChangeOne.getGoodsAmount()
                            .subtract(orderDetailChangeOne.getSellerReturnAmount()))
                    .setCouponDiscountAmount(orderDetailChangeOne.getCouponDiscountAmount()
                            .add(orderDetailChangeOne.getPlatformCouponDiscountAmount())
                            .add(orderDetailChangeOne.getPresaleDiscountAmount())
                            .add(orderDetailChangeOne.getPlatformPaymentDiscountAmount())
                            .add(orderDetailChangeOne.getShopPaymentDiscountAmount()));
            orderDetailDelivery.setRealAmount(orderDetailDelivery.getGoodsAmount().subtract(orderDetailDelivery.getCouponDiscountAmount()));

            orderDetailDelivery.setPromotionNumber(promotionGoodsMap.get(one.getGoodsSkuId()));
            orderDetailDelivery.setUnit(sellUnitMap.get(one.getGoodsSkuId()));

            discountAmount = discountAmount.add(orderDetailChangeOne.getPlatformCouponDiscountAmount())
                    .add(orderDetailChangeOne.getCouponDiscountAmount()
                            .add(orderDetailChangeOne.getPresaleDiscountAmount())
                            .add(orderDetailChangeOne.getPlatformPaymentDiscountAmount())
                            .add(orderDetailChangeOne.getShopPaymentDiscountAmount()));
            totalAmount = totalAmount.add(orderDetailChangeOne.getGoodsAmount());
            originalAmount = originalAmount.add(BigDecimal.valueOf(orderDetailChangeOne.getGoodsQuantity()).multiply(one.getOriginalPrice()));

            orderDetailDeliveryList.add(orderDetailDelivery);
            listIds.add(one.getGoodsId());
            goodsSukIds.add(one.getGoodsSkuId());
        }

        result.setTotalAmount(totalAmount);
        result.setOriginalAmount(originalAmount);
        if (PaymentMethodEnum.getByCode(Long.valueOf(orderOne.getPaymentMethod())) == PaymentMethodEnum.ONLINE && PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(orderOne.getPaymentStatus())) {
            Date createTime = orderOne.getCreateTime();
            long startTime = createTime.getTime();
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MINUTE, -15);
            long endTime = nowTime.getTime().getTime();
            if (startTime <= endTime) {
                result.setRemainTime("0分钟0秒");
            } else {
                int m = (int) (((startTime - endTime) % 86400000) % 3600000) / 60000;
                int s = (int) ((((startTime - endTime) % 86400000) % 3600000) % 60000) / 1000;
                result.setRemainTime(m + "分钟" + s + "秒");
            }
        }
        if (OrderStatusEnum.DELIVERED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())) {
            long day = DateUtil.betweenDay(orderOne.getDeliveryTime(), new Date(), true);
            if (NumberUtil.compare(7L, day) >= 0) {
                result.setRemainReceiveDay(7L - day);
            } else {
                result.setRemainReceiveDay(0L);
            }
        }

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
        if (OrderStatusEnum.DELIVERED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())) {
            result.setReturnButtonFlag(true);
            result.setReceiveButtonFlag(true);
        }

        if (PaymentMethodEnum.getByCode(Long.valueOf(orderOne.getPaymentMethod())) == PaymentMethodEnum.ONLINE && PaymentStatusEnum.PAID != PaymentStatusEnum.getByCode(orderOne.getPaymentStatus()) && OrderStatusEnum.UNAUDITED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())) {
            result.setPaymentButtonFlag(true);
        }

        if(OrderStatusEnum.RECEIVED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())
                || OrderStatusEnum.FINISHED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())
                || OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())){
            result.setDeleteButtonFlag(true);
        }
        result.setOrderCategory(orderOne.getOrderCategory());
        result.setPresaleButtonFlag(presaleButtonFlag);
        result.setPaymentNameType(paymentNameType);
        result.setPaymentAmount(totalAmount.subtract(discountAmount));
        result.setDiscountAmount(discountAmount);
        result.setOrderDetailDelivery(orderDetailDeliveryList);
        return Result.success(result);
    }


    @ApiOperation(value = "采购取消订单")
    @GetMapping("/cancel")
    public Result cancel(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "orderId") Long orderId) {

        Boolean result = orderProcessApi.cancel(orderId, staffInfo.getCurrentUserId());
        return Result.success(result);
    }

    @ApiOperation(value = "采购订单收货")
    @GetMapping("/receive")
    public Result receive(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "orderId") Long orderId) {

        Boolean result = orderProcessApi.B2BReceive(orderId, staffInfo.getCurrentUserId(), staffInfo.getCurrentEid());
        return Result.success(result);
    }

    @ApiOperation(value = "删除订单")
    @GetMapping("/hide")
    public Result hide(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "orderId") Long orderId) {
        Boolean result = orderApi.hideB2BOrder(orderId);
        return Result.success(result);
    }

}
