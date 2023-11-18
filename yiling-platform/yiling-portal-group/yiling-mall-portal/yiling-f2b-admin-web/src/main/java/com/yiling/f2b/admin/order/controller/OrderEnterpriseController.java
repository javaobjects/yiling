package com.yiling.f2b.admin.order.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.f2b.admin.order.form.QueryOrderEnterpriseForm;
import com.yiling.f2b.admin.order.vo.EnterpriseInfoVO;
import com.yiling.f2b.admin.order.vo.OrderAddressVO;
import com.yiling.f2b.admin.order.vo.OrderDetailVO;
import com.yiling.f2b.admin.order.vo.OrderEnterpriseDetailInfoVO;
import com.yiling.f2b.admin.order.vo.OrderEnterprisePageVO;
import com.yiling.f2b.admin.order.vo.OrderInvoiceApplyAllInfoVO;
import com.yiling.f2b.admin.order.vo.OrderInvoiceVo;
import com.yiling.f2b.admin.order.vo.OrderLogVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.FileInfoVO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.marketing.common.enums.CouponActivityTypeEnum;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderGiftApi;
import com.yiling.order.order.api.OrderInvoiceApi;
import com.yiling.order.order.api.OrderInvoiceApplyApi;
import com.yiling.order.order.api.OrderLogApi;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderAttachmentDTO;
import com.yiling.order.order.dto.OrderCouponUseDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderEnterpriseDTO;
import com.yiling.order.order.dto.OrderGiftDTO;
import com.yiling.order.order.dto.OrderGoodsTypeAndNumberDTO;
import com.yiling.order.order.dto.OrderInvoiceApplyDTO;
import com.yiling.order.order.dto.OrderInvoiceDTO;
import com.yiling.order.order.dto.OrderLogDTO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.dto.request.QueryOrderPageRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(tags = "企业订单数据")
@RequestMapping("/order/enterprise")
@Slf4j
public class OrderEnterpriseController extends BaseController {
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    DataPermissionsApi dataPermissionsApi;
    @DubboReference
    OrderPromotionActivityApi orderPromotionActivityApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderCouponUseApi orderCouponUseApi;
    @DubboReference
    CouponActivityApi couponActivityApi;
    @DubboReference
    OrderGiftApi orderGiftApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderAddressApi orderAddressApi;
    @DubboReference
    OrderLogApi orderLogApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    OrderInvoiceApplyApi orderInvoiceApplyApi;
    @DubboReference
    OrderInvoiceApi orderInvoiceApi;

    @Autowired
    FileService fileService;
    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @ApiOperation(value = "企业订单")
    @PostMapping("/list")
    public Result<Page<OrderEnterprisePageVO>> getOrderList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryOrderEnterpriseForm form) {
        QueryOrderPageRequest request = PojoUtils.map(form, QueryOrderPageRequest.class);
        //List<Long> subEidLists = enterpriseApi.listSubEids(Constants.YILING_EID);

       /* List<Long> list = new ArrayList<>();

        if (staffInfo.getYilingFlag()) {
            list.addAll(subEidLists);
            List<Long> contacterIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
            request.setContacterIdList(contacterIdList);
        } else {
            //非以岭人员
            list.add(staffInfo.getCurrentEid());
        }*/

        List<Long> userIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
        if (CollUtil.isNotEmpty(userIdList) && !userIdList.contains(staffInfo.getCurrentUserId())) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        Map<Long, List<EnterpriseDTO>> contactUserMap = null;
        if (CollUtil.isNotEmpty(userIdList)) {
            contactUserMap = enterpriseApi.listByContactUserIds(staffInfo.getCurrentEid(), userIdList);
            if(MapUtil.isNotEmpty(contactUserMap)){
                if(!contactUserMap.keySet().contains(staffInfo.getCurrentUserId()) || CollUtil.isEmpty(contactUserMap.get(staffInfo.getCurrentUserId()))){
                    return Result.success(new Page<>());
                }
                List<EnterpriseDTO> enterpriseList = contactUserMap.get(staffInfo.getCurrentUserId());
                List<Long> list = enterpriseList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
                log.info("企业订单中台数据企业{}", JSON.toJSONString(list));
                request.setEidList(list);
            }else{
                return Result.success(new Page<>());
            }
        }


        Page<OrderEnterpriseDTO> orderPage = orderApi.getOrderEnterprisePage(request);
        Page<OrderEnterprisePageVO> page = PojoUtils.map(orderPage, OrderEnterprisePageVO.class);
        if (orderPage != null && CollectionUtil.isNotEmpty(orderPage.getRecords())) {
            List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());
            List<OrderPromotionActivityDTO> orderPromotionActivityList = orderPromotionActivityApi.listByOrderIds(ids);
            Map<Long,Integer> activityMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(orderPromotionActivityList)){
                activityMap = orderPromotionActivityList.stream().collect(Collectors.toMap(OrderPromotionActivityDTO::getOrderId, OrderPromotionActivityDTO::getActivityType, (k1, k2) -> k1));
            }

            List<Long> eidList = page.getRecords().stream().map(OrderEnterprisePageVO::getBuyerEid).collect(Collectors.toList());
            //List<EnterpriseDTO> enterpriseLists = enterpriseApi.listByIds(eidList);
            //Map<Long, EnterpriseDTO> enterpriseMap = enterpriseLists.stream().collect(Collectors.toMap(EnterpriseDTO::getId, one -> one, (k1, k2) -> k1));

            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(ids);
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

            for (OrderEnterprisePageVO one : page.getRecords()) {
                //EnterpriseDTO enterpriseDTO = enterpriseMap.get(one.getBuyerEid());
                //货款总额
                BigDecimal totalAmount = BigDecimal.ZERO;
                //折扣金额
                BigDecimal discountAmount = BigDecimal.ZERO;
                for (OrderDetailChangeDTO detail : orderDetailChangeList) {
                    if (one.getId().equals(detail.getOrderId())) {
                        totalAmount = totalAmount.add(detail.getGoodsAmount().subtract(detail.getSellerReturnAmount()));
                        discountAmount = discountAmount.add(detail.getCashDiscountAmount().add(detail.getTicketDiscountAmount())
                                .add(detail.getPlatformCouponDiscountAmount()).add(detail.getCouponDiscountAmount())
                                .add(detail.getPresaleDiscountAmount())
                                .add(detail.getShopPaymentDiscountAmount())
                                .add(detail.getPlatformPaymentDiscountAmount())
                                .subtract(detail.getSellerPlatformPaymentDiscountAmount())
                                .subtract(detail.getSellerShopPaymentDiscountAmount())
                                .subtract(detail.getSellerPresaleDiscountAmount())
                                .subtract(detail.getSellerCouponDiscountAmount())
                                .subtract(detail.getSellerPlatformCouponDiscountAmount())
                                .subtract(detail.getSellerReturnCashDiscountAmount())
                                .subtract(detail.getSellerReturnTicketDiscountAmount()));
                    }
                }

                //设置金额
                one.setDiscountAmount(discountAmount)
                        .setTotalAmount(totalAmount)
                        .setPaymentAmount(totalAmount.subtract(discountAmount));
                OrderGoodsTypeAndNumberDTO goodsTypeNumber = orderDetailApi.getOrderGoodsTypeAndNumber(one.getId());
                one.setGoodsOrderNum(goodsTypeNumber.getGoodsOrderNum()).setGoodsOrderPieceNum(goodsTypeNumber.getGoodsOrderPieceNum())
                        .setReceiveOrderNum(goodsTypeNumber.getReceiveOrderNum()).setReceiveOrderPieceNum(goodsTypeNumber.getReceiveOrderPieceNum())
                        .setDeliveryOrderNum(goodsTypeNumber.getDeliveryOrderNum()).setDeliveryOrderPieceNum(goodsTypeNumber.getDeliveryOrderPieceNum());
                //if (enterpriseDTO != null) {

                one.setBuyerAddress(one.getBuyerProvinceName() + one.getBuyerCityName() + one.getBuyerRegionName());

                //}

                // 设置优惠券使用信息
                one.setCouponActivityInfo(buildCouponActivityInfo(one.getId(), orderCouponUseMap, couponActivityMap));
                //设置活动类型
                one.setActivityType(activityMap.get(one.getId()) == null ? 0 : activityMap.get(one.getId()));
            }

            for (OrderEnterprisePageVO one : page.getRecords()) {
                List<OrderGiftDTO> giftList = orderGiftMap.get(one.getId());

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

    @ApiOperation(value = "企业订单详情")
    @GetMapping("/detail")
    public Result<OrderEnterpriseDetailInfoVO> getOrderSaleDetailAllInfo(@RequestParam(value = "orderId", required = true) Long orderId) {
        OrderDTO orderInfo = orderApi.getOrderInfo(orderId);
        OrderEnterpriseDetailInfoVO result = PojoUtils.map(orderInfo, OrderEnterpriseDetailInfoVO.class);
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
        //原价总金额
        BigDecimal originalTotalAmount = BigDecimal.ZERO;


        for (OrderDetailDTO one : orderDetailInfo) {
            OrderDetailChangeDTO orderDetailChangeOne = orderDetailChangeMap.get(one.getId());
            OrderDetailVO detail = PojoUtils.map(one, OrderDetailVO.class);
            detail.setGoodsQuantity(orderDetailChangeOne.getGoodsQuantity()).setDeliveryQuantity(orderDetailChangeOne.getDeliveryQuantity())
                    .setGoodsPrice(orderDetailChangeOne.getGoodsPrice())
                    .setGoodsAmount(orderDetailChangeOne.getGoodsAmount().subtract(orderDetailChangeOne.getSellerReturnAmount()))
                    .setDiscountAmount(orderDetailChangeOne.getCashDiscountAmount().add(orderDetailChangeOne.getTicketDiscountAmount())
                            .add(orderDetailChangeOne.getPlatformCouponDiscountAmount()).add(orderDetailChangeOne.getCouponDiscountAmount())
                            .add(orderDetailChangeOne.getPresaleDiscountAmount())
                            .add(orderDetailChangeOne.getPlatformPaymentDiscountAmount())
                            .add(orderDetailChangeOne.getShopPaymentDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerPlatformPaymentDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerShopPaymentDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerPresaleDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerPlatformCouponDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerCouponDiscountAmount()).subtract(orderDetailChangeOne.getSellerReturnCashDiscountAmount())
                            .subtract(orderDetailChangeOne.getSellerReturnTicketDiscountAmount()));
            detail.setReceiveQuantity(orderDetailChangeOne.getReceiveQuantity());
            detail.setRealAmount(detail.getGoodsAmount().subtract(detail.getDiscountAmount()));
            discountAmount = discountAmount.add(detail.getDiscountAmount());
            totalAmount = totalAmount.add(detail.getGoodsAmount());
            BigDecimal multiply = detail.getOriginalPrice().multiply(BigDecimal.valueOf(detail.getGoodsQuantity()));
            originalTotalAmount = originalTotalAmount.add(multiply);
            orderDetailDelivery.add(detail);
            listIds.add(one.getGoodsId());
            goodsSukIds.add(one.getGoodsSkuId());
        }
        result.setOriginalTotalAmount(originalTotalAmount);
        result.setTotalAmount(totalAmount);
        result.setDiscountAmount(discountAmount);
        result.setPaymentAmount(totalAmount.subtract(discountAmount));

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
