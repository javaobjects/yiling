package com.yiling.admin.data.center.order.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.common.utils.PictureUrlUtils;
import com.yiling.admin.data.center.order.form.QueryBackOrderInfoForm;
import com.yiling.admin.data.center.order.form.QueryOrderNumberCountForm;
import com.yiling.admin.data.center.order.vo.EnterpriseInfoVO;
import com.yiling.admin.data.center.order.vo.OrderAddressVO;
import com.yiling.admin.data.center.order.vo.OrderDetailVO;
import com.yiling.admin.data.center.order.vo.OrderInvoiceApplyAllInfoVO;
import com.yiling.admin.data.center.order.vo.OrderInvoiceVo;
import com.yiling.admin.data.center.order.vo.OrderFullDetailInfoVO;
import com.yiling.admin.data.center.order.vo.OrderLogVO;
import com.yiling.admin.data.center.order.vo.OrderNumberVO;
import com.yiling.admin.data.center.order.vo.OrderPageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.FileInfoVO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.marketing.common.enums.CouponActivityTypeEnum;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.order.order.api.*;
import com.yiling.order.order.dto.*;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.enums.OrderAttachmentTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author:wei.wang
 * @date:2021/6/23
 */
@RestController
@Api(tags = "订单管理")
@RequestMapping("/order")
public class OrderController extends BaseController {
    @DubboReference
    OrderApi             orderApi;
    @DubboReference
    OrderDetailApi       orderDetailApi;
    @DubboReference
    GoodsApi             goodsApi;
    @DubboReference
    OrderAddressApi      orderAddressApi;
    @DubboReference
    OrderLogApi          orderLogApi;
    @DubboReference
    EnterpriseApi        enterpriseApi;
    @DubboReference
    OrderInvoiceApplyApi orderInvoiceApplyApi;
    @DubboReference
    UserApi              userApi;
    @DubboReference
    OrderInvoiceApi      orderInvoiceApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderProcessApi      orderMallApi;
    @DubboReference
    CouponActivityApi    couponActivityApi;
    @DubboReference
    OrderCouponUseApi    orderCouponUseApi;
    @DubboReference
    OrderGiftApi         orderGiftApi;
    @DubboReference
    OrderPromotionActivityApi orderPromotionActivityApi;

    @Autowired
    PictureUrlUtils      pictureUrlUtils;
    @Autowired
    FileService          fileService;

    @ApiOperation(value = "订单数量接口")
    @PostMapping("/get/number")
    public Result<OrderNumberVO> getOrderNumber(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryOrderNumberCountForm form) {
        QueryBackOrderInfoRequest request = PojoUtils.map(form, QueryBackOrderInfoRequest.class);
        OrderNumberDTO orderNumber = orderApi.getCentreOrderNumberCount(request);
        return Result.success(PojoUtils.map(orderNumber, OrderNumberVO.class));
    }

    @ApiOperation(value = "订单列表")
    @PostMapping("/get/page")
    public Result<Page<OrderPageVO>> getOrderPage(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryBackOrderInfoForm form) {

        QueryBackOrderInfoRequest request = PojoUtils.map(form, QueryBackOrderInfoRequest.class);

        Page<OrderDTO> orderPage = orderMallApi.getBackOrderPage(request);
        Page<OrderPageVO> page = PojoUtils.map(orderPage, OrderPageVO.class);
        List<Long> goodsIds = new ArrayList<>();
        List<Long> goodsSukIds = new ArrayList<>();
        if (orderPage != null && CollectionUtil.isNotEmpty(orderPage.getRecords())) {
            List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());
            List<OrderPromotionActivityDTO> orderPromotionActivityList = orderPromotionActivityApi.listByOrderIds(ids);
            Map<Long,Integer> activityMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(orderPromotionActivityList)){
                activityMap = orderPromotionActivityList.stream().collect(Collectors.toMap(OrderPromotionActivityDTO::getOrderId, OrderPromotionActivityDTO::getActivityType, (k1, k2) -> k1));
            }

            List<Long> eidList = page.getRecords().stream().map(OrderPageVO::getBuyerEid).collect(Collectors.toList());
            List<EnterpriseDTO> enterpriseLists = enterpriseApi.listByIds(eidList);
            Map<Long, EnterpriseDTO> enterpriseMap = enterpriseLists.stream().collect(Collectors.toMap(EnterpriseDTO::getId, one -> one, (k1, k2) -> k1));

            List<OrderDetailDTO> details = orderDetailApi.getOrderDetailByOrderIds(ids);
            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(ids);
            Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));

            List<OrderCouponUseDTO> orderCouponUseList = orderCouponUseApi.listByOrderIds(ids);
            Map<Long, List<OrderCouponUseDTO>> orderCouponUseMap = orderCouponUseList.stream().collect(Collectors.groupingBy(OrderCouponUseDTO::getOrderId));
            List<Long> couponActivityIdList = orderCouponUseList.stream().map(OrderCouponUseDTO::getCouponActivityId).collect(Collectors.toList());
            List<CouponActivityDetailDTO> couponActivityList = couponActivityApi.getCouponActivityById(couponActivityIdList);
            Map<Long, CouponActivityDetailDTO> couponActivityMap = couponActivityList.stream().collect(Collectors.toMap(CouponActivityDetailDTO::getId, one -> one, (k1, k2) -> k1));

            List<OrderGiftDTO> orderGiftList = orderGiftApi.listByOrderIds(ids);
            Map<Long, List<OrderGiftDTO>> orderGiftMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(orderGiftList)){
                orderGiftMap = orderGiftList.stream().collect(Collectors.groupingBy(OrderGiftDTO::getOrderId));
            }

            for (OrderPageVO one : page.getRecords()) {
                EnterpriseDTO enterpriseDTO = enterpriseMap.get(one.getBuyerEid());
                //货款总额
                BigDecimal totalAmount = BigDecimal.ZERO;
                //折扣金额
                BigDecimal discountAmount = BigDecimal.ZERO;
                //原件金额
                BigDecimal originalAmount = BigDecimal.ZERO;
                List<OrderDetailVO> goodOrderList = new ArrayList<>();
                for (OrderDetailDTO detail : details) {
                    if (one.getId().equals(detail.getOrderId())) {
                        OrderDetailChangeDTO orderDetailChangeOne = orderDetailChangeMap.get(detail.getId());
                        OrderDetailVO orderDetailOne = PojoUtils.map(detail, OrderDetailVO.class);
                        originalAmount = originalAmount.add(detail.getOriginalPrice().multiply(BigDecimal.valueOf(detail.getGoodsQuantity())));
                        orderDetailOne.setGoodsQuantity(orderDetailChangeOne.getGoodsQuantity())
                                .setDeliveryQuantity(orderDetailChangeOne.getDeliveryQuantity()).setGoodsPrice(orderDetailChangeOne.getGoodsPrice())
                                .setGoodsAmount(orderDetailChangeOne.getGoodsAmount().subtract(orderDetailChangeOne.getSellerReturnAmount()))
                                .setDiscountAmount(orderDetailChangeOne.getCashDiscountAmount().add(orderDetailChangeOne.getTicketDiscountAmount())
                                        .add(orderDetailChangeOne.getPlatformCouponDiscountAmount()).add(orderDetailChangeOne.getCouponDiscountAmount())
                                        .add(orderDetailChangeOne.getPresaleDiscountAmount())
                                        .add(orderDetailChangeOne.getShopPaymentDiscountAmount()).add(orderDetailChangeOne.getPlatformPaymentDiscountAmount())
                                        .subtract(orderDetailChangeOne.getSellerPresaleDiscountAmount())
                                        .subtract(orderDetailChangeOne.getSellerCouponDiscountAmount())
                                        .subtract(orderDetailChangeOne.getSellerPlatformCouponDiscountAmount())
                                        .subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount())
                                        .subtract(orderDetailChangeOne.getSellerShopPaymentDiscountAmount()).subtract(orderDetailChangeOne.getSellerPlatformPaymentDiscountAmount())
                                        .subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount()));
                        orderDetailOne.setRealAmount(orderDetailOne.getGoodsAmount().subtract(orderDetailOne.getDiscountAmount()));
                        totalAmount = totalAmount.add(orderDetailOne.getGoodsAmount());
                        discountAmount = discountAmount.add(orderDetailOne.getDiscountAmount());
                        goodOrderList.add(orderDetailOne);
                    }
                    goodsIds.add(detail.getGoodsId());
                    goodsSukIds.add(detail.getGoodsSkuId());
                }
                if (one.getInvoiceStatus() == null) {
                    one.setInvoiceStatus(1);
                }
                //设置金额
                one.setOriginalAmount(originalAmount);
                one.setDiscountAmount(discountAmount).setTotalAmount(totalAmount).setPaymentAmount(totalAmount.subtract(discountAmount));
                one.setGoodOrderList(goodOrderList);
                OrderGoodsTypeAndNumberDTO goodsTypeNumber = orderDetailApi.getOrderGoodsTypeAndNumber(one.getId());
                one.setGoodsOrderNum(goodsTypeNumber.getGoodsOrderNum()).setGoodsOrderPieceNum(goodsTypeNumber.getGoodsOrderPieceNum())
                        .setReceiveOrderNum(goodsTypeNumber.getReceiveOrderNum()).setReceiveOrderPieceNum(goodsTypeNumber.getReceiveOrderPieceNum())
                        .setDeliveryOrderNum(goodsTypeNumber.getDeliveryOrderNum()).setDeliveryOrderPieceNum(goodsTypeNumber.getDeliveryOrderPieceNum());
                if (enterpriseDTO != null) {
                    one.setBuyerAddress(enterpriseDTO.getProvinceName() + enterpriseDTO.getCityName() + enterpriseDTO.getRegionName());
                }

                // 设置优惠券使用信息
                one.setCouponActivityInfo(buildCouponActivityInfo(one.getId(), orderCouponUseMap, couponActivityMap));
                //设置活动类型
                one.setActivityType(activityMap.get(one.getId()) == null ? 0 : activityMap.get(one.getId()));
            }
            Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(goodsIds);
            List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSukIds);
            Map<Long, GoodsSkuDTO> skuGoodsMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(goodsSkuByIds)){
                skuGoodsMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
            }

            for (OrderPageVO one : page.getRecords()) {
                List<OrderGiftDTO> giftList = orderGiftMap.get(one.getId());
                for (OrderDetailVO goods : one.getGoodOrderList()) {
                    goods.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(goods.getGoodsId())));
                    goods.setPackageNumber(skuGoodsMap.get(goods.getGoodsSkuId()) != null ? skuGoodsMap.get(goods.getGoodsSkuId()).getPackageNumber() : 1L );
                    goods.setGoodsRemark(skuGoodsMap.get(goods.getGoodsSkuId()) != null ? skuGoodsMap.get(goods.getGoodsSkuId()).getRemark() : "" );
                }
                if(CollectionUtil.isNotEmpty(giftList)){
                    one.setHaveGiftFlag(1);
                }else{
                    one.setHaveGiftFlag(0);
                }

            }
        }
        return Result.success(page);
    }


    /**
     * 构建优惠券使用信息
     * @param orderId    订单id
     * @param orderCouponUseMap 订单所用券map
     * @param couponActivityMap 优惠券活动map
     * @return
     */
    private String buildCouponActivityInfo(Long orderId, Map<Long, List<OrderCouponUseDTO>> orderCouponUseMap,Map<Long, CouponActivityDetailDTO> couponActivityMap) {
        List<OrderCouponUseDTO> orderCouponUse = orderCouponUseMap.get(orderId);
        String result = null;
        if (CollUtil.isNotEmpty(orderCouponUse)) {
            StringBuffer sb = new StringBuffer();
            orderCouponUse.forEach(item -> {
                CouponActivityDetailDTO couponActivity = couponActivityMap.get(item.getCouponActivityId());
                if (Objects.isNull(couponActivity)) {
                    return;
                }
                if (CouponActivityTypeEnum.REDUCE.getCode().equals(couponActivity.getType())) {
                    sb.append(CouponActivityTypeEnum.REDUCE.getShortName());
                    sb.append("满").append(couponActivity.getThresholdValue()).append("元");
                    sb.append("减").append(couponActivity.getDiscountValue()).append("元");
                    sb.append(",");
                }
                if (CouponActivityTypeEnum.DISCOUNT.getCode().equals(couponActivity.getType())) {
                    DecimalFormat df = new DecimalFormat("##.##");
                    sb.append(CouponActivityTypeEnum.DISCOUNT.getShortName());
                    sb.append("满").append(couponActivity.getThresholdValue()).append("元");
                    sb.append("打").append(df.format(couponActivity.getDiscountValue().divide(BigDecimal.valueOf(10)))).append("折");
                    sb.append(",");
                    if (couponActivity.getDiscountMax().compareTo(BigDecimal.valueOf(0)) > 0) {
                        sb.append("最高优惠").append(couponActivity.getDiscountMax()).append("元");
                        sb.append(",");
                    }
                }
            });
            if (sb.indexOf(",") > 0) {
                result = sb.toString().substring(0, sb.lastIndexOf(","));
            }
        }
        return result;
    }

    @ApiOperation(value = "订单详情列表")
    @GetMapping("/get/detail")
    public Result<OrderFullDetailInfoVO> getOrderSaleDetailAllInfo(@RequestParam(value = "orderId", required = true) Long orderId) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        OrderFullDetailInfoVO result = PojoUtils.map(orderInfo, OrderFullDetailInfoVO.class);
        OrderAddressDTO addressInfo = orderAddressApi.getOrderAddressInfo(orderId);
        OrderAddressVO address = PojoUtils.map(addressInfo, OrderAddressVO.class);
        if (addressInfo != null) {
            address.setAddress(addressInfo.getAddress());
        }
        result.setOrderAddress(address);

        List<Long> enterpriseIds = new ArrayList<Long>() {
            {
                add(orderInfo.getDistributorEid());
                add(orderInfo.getBuyerEid());
            }
        };
        List<EnterpriseDTO> enterpriseLists = enterpriseApi.listByIds(enterpriseIds);
        Map<Long, EnterpriseDTO> enterpriseMap = enterpriseLists.stream().collect(Collectors.toMap(EnterpriseDTO::getId, one -> one, (k1, k2) -> k1));

        EnterpriseDTO enterpriseDistributor = enterpriseMap.get(orderInfo.getDistributorEid());
        EnterpriseInfoVO enterpriseInfo = PojoUtils.map(enterpriseDistributor, EnterpriseInfoVO.class);
        if (enterpriseDistributor != null) {
            enterpriseInfo.setAddress(enterpriseDistributor.getProvinceName() + enterpriseDistributor.getCityName()
                    + enterpriseDistributor.getRegionName() + enterpriseDistributor.getAddress());
        }
        result.setEnterpriseDistributorInfo(enterpriseInfo);

        EnterpriseDTO enterpriseBuyer = enterpriseMap.get(orderInfo.getBuyerEid());
        EnterpriseInfoVO enterpriseBuyerInfo = PojoUtils.map(enterpriseBuyer, EnterpriseInfoVO.class);
        if (enterpriseBuyer != null) {
            enterpriseBuyerInfo.setAddress(enterpriseBuyer.getProvinceName() + enterpriseBuyer.getCityName() + enterpriseBuyer.getRegionName() + enterpriseBuyer.getAddress());
        }
        result.setEnterpriseBuyerInfo(enterpriseBuyerInfo);

        List<OrderLogDTO> orderLogInfo = orderLogApi.getOrderLogInfo(orderId);
        result.setOrderLogInfo(PojoUtils.map(orderLogInfo, OrderLogVO.class));

        List<OrderAttachmentDTO> orderContractList = orderApi.listOrderAttachmentsByType(orderId, OrderAttachmentTypeEnum.RECEIPT_FILE);
        //组装回执单信息
        List<FileInfoVO> fileList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(orderContractList)) {
            for (OrderAttachmentDTO one : orderContractList) {
                FileInfoVO file = new FileInfoVO();
                file.setFileKey(one.getFileKey());
                file.setFileUrl(fileService.getUrl(one.getFileKey(), FileTypeEnum.ORDER_RECEIVE_ONE_RECEIPT));
                fileList.add(file);
            }
        }
        result.setReceiveReceiptList(fileList);

        OrderInvoiceApplyAllInfoVO orderInvoiceApplyAllInfo = getOrderInvoiceApplyAllInfoVO(orderId,orderInfo.getInvoiceStatus());
        result.setOrderInvoiceApplyAllInfo(orderInvoiceApplyAllInfo);

        List<OrderDetailDTO> orderDetailInfo = orderDetailApi.getOrderDetailInfo(orderId);
        List<OrderDetailVO> orderDetailDelivery = new ArrayList<>();
        List<Long> listIds = new ArrayList<>();
        List<Long> goodsSukIds = new ArrayList<>();

        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(orderId);
        Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));
        //货款总额
        BigDecimal totalAmount = BigDecimal.ZERO;
        //票折总金额
        BigDecimal discountAmount = BigDecimal.ZERO;
        //原件总金额
        BigDecimal originalAmount = BigDecimal.ZERO;
        for (OrderDetailDTO one : orderDetailInfo) {
            OrderDetailChangeDTO orderDetailChangeOne = orderDetailChangeMap.get(one.getId());
            OrderDetailVO detail = PojoUtils.map(one, OrderDetailVO.class);
            detail.setGoodsQuantity(orderDetailChangeOne.getGoodsQuantity()).setDeliveryQuantity(orderDetailChangeOne.getDeliveryQuantity())
                    .setGoodsPrice(orderDetailChangeOne.getGoodsPrice())
                    .setGoodsAmount(orderDetailChangeOne.getGoodsAmount().subtract(orderDetailChangeOne.getSellerReturnAmount()))
                    .setDiscountAmount(orderDetailChangeOne.getCashDiscountAmount().add(orderDetailChangeOne.getTicketDiscountAmount())
                            .add(orderDetailChangeOne.getPlatformCouponDiscountAmount()).add(orderDetailChangeOne.getCouponDiscountAmount())
                            .add(orderDetailChangeOne.getPresaleDiscountAmount()).subtract(orderDetailChangeOne.getSellerPresaleDiscountAmount())
                            .add(orderDetailChangeOne.getPlatformPaymentDiscountAmount()).add(orderDetailChangeOne.getShopPaymentDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerPlatformPaymentDiscountAmount()).subtract(orderDetailChangeOne.getSellerShopPaymentDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerPlatformCouponDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerCouponDiscountAmount()).subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount()));
            detail.setReceiveQuantity(orderDetailChangeOne.getReceiveQuantity());
            detail.setRealAmount(detail.getGoodsAmount().subtract(detail.getDiscountAmount()));
            discountAmount = discountAmount.add(detail.getDiscountAmount());
            totalAmount = totalAmount.add(detail.getGoodsAmount());
            originalAmount = originalAmount.add(BigDecimal.valueOf(one.getGoodsQuantity()).multiply(one.getOriginalPrice()));

            orderDetailDelivery.add(detail);
            listIds.add(one.getGoodsId());
            goodsSukIds.add(one.getGoodsSkuId());
        }
        result.setTotalAmount(totalAmount);
        result.setDiscountAmount(discountAmount);
        result.setPaymentAmount(totalAmount.subtract(discountAmount));
        result.setOriginalAmount(originalAmount);
        List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSukIds);
        Map<Long, GoodsSkuDTO> skuGoodsMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(goodsSkuByIds)){
            skuGoodsMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
        }
        Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(listIds);
        for (OrderDetailVO one : orderDetailDelivery) {
            one.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(one.getGoodsId())));
            one.setPackageNumber(skuGoodsMap.get(one.getGoodsSkuId()) != null ? skuGoodsMap.get(one.getGoodsSkuId()).getPackageNumber() : 1L );
            one.setGoodsRemark(skuGoodsMap.get(one.getGoodsSkuId()) != null ? skuGoodsMap.get(one.getGoodsSkuId()).getRemark() : "" );
        }

        result.setOrderDetailDelivery(orderDetailDelivery);
        return Result.success(result);
    }

    private  OrderInvoiceApplyAllInfoVO getOrderInvoiceApplyAllInfoVO(Long orderId,Integer invoiceStatus ){
        OrderInvoiceApplyAllInfoVO result = new OrderInvoiceApplyAllInfoVO();
        List<OrderInvoiceApplyDTO> orderInvoiceApplyList = orderInvoiceApplyApi.listByOrderId(orderId);
        if(CollectionUtil.isNotEmpty(orderInvoiceApplyList)){
            result.setStatus(invoiceStatus);
            List<Long> applyIdList = orderInvoiceApplyList.stream().map(o -> o.getId()).collect(Collectors.toList());
            List<OrderInvoiceDTO> orderInvoiceByApplyIdList = orderInvoiceApi.getOrderInvoiceByApplyIdList(applyIdList);
            if(CollectionUtil.isNotEmpty(orderInvoiceByApplyIdList)){
                result.setInvoiceNumber(orderInvoiceByApplyIdList.size());
                Map<Long, List<OrderInvoiceDTO>> applyMap = orderInvoiceByApplyIdList.stream().collect(Collectors.groupingBy(s -> s.getApplyId()));
                List<OrderInvoiceVo> orderInvoiceInfo = new ArrayList<>();
                for(OrderInvoiceApplyDTO applyOne : orderInvoiceApplyList){
                    OrderInvoiceVo orderInvoiceOne = new OrderInvoiceVo();
                    List<OrderInvoiceDTO> orderInvoiceList = applyMap.get(applyOne.getId());
                    if(CollectionUtil.isNotEmpty(orderInvoiceList)){
                        String invoiceNo = orderInvoiceList.stream().map(o -> o.getInvoiceNo()).collect(Collectors.joining(","));
                        orderInvoiceOne.setInvoiceNo(invoiceNo);
                    }

                    orderInvoiceOne.setApplyId(applyOne.getId());
                    orderInvoiceInfo.add(orderInvoiceOne);
                }
                result.setOrderInvoiceInfo(orderInvoiceInfo);
            }
        }
        return result;
    }

}