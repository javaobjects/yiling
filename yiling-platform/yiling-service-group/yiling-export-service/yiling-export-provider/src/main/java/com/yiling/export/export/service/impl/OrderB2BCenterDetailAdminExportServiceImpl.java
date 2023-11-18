package com.yiling.export.export.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportOrderB2BCenterDetailBO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.ExportOrderCenterAdminModel;
import com.yiling.export.export.model.ExportOrderCenterDetailAdminModel;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.dto.OrderCouponUseDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.settlement.report.dto.ReportPriceParamNameDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseTagApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseTagDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;

/**
 * 订单中台后台订单导出
 *
 * @author:wei.wang
 * @date:2021/8/21
 */
@Service("orderB2BCenterDetailAdminExportService")
public class OrderB2BCenterDetailAdminExportServiceImpl implements BaseExportQueryDataService<QueryBackOrderInfoRequest> {

    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderDeliveryApi orderDeliveryApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderProcessApi orderMallApi;
    @DubboReference
    EnterpriseTagApi enterpriseTagApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    OrderPromotionActivityApi orderPromotionActivityApi;
    @DubboReference
    GoodsYilingPriceApi goodsYilingPriceApi;
    @DubboReference
    OrderCouponUseApi orderCouponUseApi;

   /* private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("orderNo", "订单编号");
        FIELD.put("sellerEid", "供应商ID");
        FIELD.put("sellerEname", "供应商名称");
        FIELD.put("sellerLag", "供应商标签");
        FIELD.put("sellerProvinceName", "供应商所在省");
        FIELD.put("buyerEid", "采购商ID");
        FIELD.put("buyerEname", "采购商名称");
        FIELD.put("provinceName", "采购商所在省");
        FIELD.put("cityName", "采购商所在市");
        FIELD.put("regionName", "采购商所在区");
        FIELD.put("createTime", "下单时间");
        FIELD.put("receiveTime", "收货时间");
        FIELD.put("orderStatusName", "订单状态");
        FIELD.put("paymentMethodName", "支付方式");
        FIELD.put("paymentStatusName", "支付状态");
        FIELD.put("orderSourceName", "订单来源");
        //FIELD.put("contacterId", "商务负责人ID");
        //FIELD.put("contacterName", "商务负责人姓名");
        FIELD.put("goodsId", "商品ID");
        FIELD.put("goodsName", "商品名称");
        FIELD.put("goodsSpecification", "规格型号");
        FIELD.put("goodsQuantity", "购买数量");
        FIELD.put("batchGoodsQuantity", "发货数量");
        FIELD.put("goodsLicenseNo", "批准文号");
        FIELD.put("batchNo", "批次号/序列号");
        FIELD.put("expiryDate", "有效期至");
        FIELD.put("originalPrice", "商品原价");
        FIELD.put("goodsPrice", "商品成交价");
        FIELD.put("goodsAmount", "金额小计");
        FIELD.put("goodsPayAmount", "支付金额");
        FIELD.put("goodsDiscountRate", "折扣比率");
        FIELD.put("goodsDiscountAmount", "折扣金额");
        FIELD.put("activityType", "商品促销");
        FIELD.put("platformRatio", "商品促销平台承担比例");
        FIELD.put("businessRatio", "商品促销商家承担比例");
        FIELD.put("platformDiscountAmount", "平台承担折扣金额");
        FIELD.put("businessDiscountAmount", "商家承担折扣金额");
        FIELD.put("goodsSellPrice", "商销单价");
        FIELD.put("goodsSellAmount", "商销总金额");
        FIELD.put("goodsSupportPrice", "供货单价");
        FIELD.put("goodsSupportAmount", "供货总金额");



    }*/


    @Override
    public boolean isReturnData() {
        return false;
    }

    @Override
    public byte[] getExportByte(QueryBackOrderInfoRequest request, String fileName) {
        if (request == null) {
            return null;
        }


        String tmpDir = FileUtil.getTmpDirPath() + File.separator + System.currentTimeMillis();
        String tmpExcelDir = tmpDir + File.separator + fileName;
        File tmpExcelFileDir = new File(tmpExcelDir);
        if (!tmpExcelFileDir.exists()) {
            tmpExcelFileDir.mkdirs();
        }

        String fileFullName = tmpExcelDir + File.separator + fileName + ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(fileFullName).build();
        int fileIdx = 0;
        try {
            int current = 1;
            int size = 1000;
            int sheetNumber = 1;
            outer:
            do {
                WriteSheet writeSheet = null;
                writeSheet = EasyExcel.writerSheet(fileIdx, "运营后台订单明细列表" + sheetNumber).head(ExportOrderCenterDetailAdminModel.class).build();
                while (true) {
                    request.setCurrent(current);
                    request.setSize(size);

                    List<ExportOrderCenterDetailAdminModel> list = getResult(request);
                    // 写文件
                    if (writeSheet != null) {
                        excelWriter.write(list, writeSheet);
                    }
                    if (CollUtil.isEmpty(list)) {
                        fileIdx++;
                        break outer;
                    }
                    if (list.size() < size) {
                        fileIdx++;
                        break outer;
                    }

                    if (current % 1000 == 0) {   // 100w数据sheet分页
                        current++;
                        sheetNumber++;
                        fileIdx++;
                        break;
                    }
                    current++;
                }

            } while (true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
        //  压缩文件
        try {
            File zipFile = FileUtil.newFile(fileFullName);
            if (null != zipFile) {
                return FileUtil.readBytes(zipFile);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            FileUtil.del(tmpDir);
        }
        return null;
    }

    /**
     * 组装数据
     * @param request
     * @return
     */
    public List<ExportOrderCenterDetailAdminModel> getResult(QueryBackOrderInfoRequest request) {


        Page<OrderDTO> page = orderMallApi.getBackOrderPage(request);
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return ListUtil.empty();
        }
        List<ExportOrderCenterDetailAdminModel> result = new ArrayList<>();

        List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());
        Map<Long, OrderDTO> orderMap = page.getRecords().stream().collect(Collectors.toMap(OrderDTO::getId, o -> o, (k1, k2) -> k1));

        List<OrderPromotionActivityDTO> orderPromotionActivityList = orderPromotionActivityApi.listByOrderIds(ids);
        Map<Long, Integer> activityMap = new HashMap<>();
        Map<Long, List<OrderPromotionActivityDTO>> promotionActivityMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(orderPromotionActivityList)) {
            activityMap = orderPromotionActivityList.stream().filter(o -> o.getActivityType() != 5).collect(Collectors.toMap(OrderPromotionActivityDTO::getOrderId, OrderPromotionActivityDTO::getActivityType, (k1, k2) -> k1));
            promotionActivityMap = orderPromotionActivityList.stream().collect(Collectors.groupingBy(s -> s.getOrderId()));
        }


        List<Long> eidList = page.getRecords().stream().map(order -> order.getBuyerEid()).collect(Collectors.toList());
        List<Long> eidSellerList = page.getRecords().stream().map(order -> order.getSellerEid()).collect(Collectors.toList());

        Map<Long, List<EnterpriseTagDTO>> longListMap = enterpriseTagApi.listByEidList(eidSellerList);
        eidList.addAll(eidSellerList);


        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eidList);
        Map<Long, EnterpriseDTO> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, o -> o, (k1, k2) -> k1));

        List<OrderDeliveryDTO> OrderDeliveryList = orderDeliveryApi.listByOrderIds(ids);
        Map<Long, List<OrderDeliveryDTO>> mapDelivery = new HashMap<>();
        for (OrderDeliveryDTO one : OrderDeliveryList) {
            if (mapDelivery.containsKey(one.getDetailId())) {
                List<OrderDeliveryDTO> list = mapDelivery.get(one.getDetailId());
                list.add(one);
            } else {
                mapDelivery.put(one.getDetailId(), new ArrayList<OrderDeliveryDTO>() {{
                    add(one);
                }});
            }
        }

        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(ids);
        Map<Long, OrderDetailChangeDTO> changeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));

        Map<Long, List<OrderDetailChangeDTO>> mapList = new HashMap<>();
        for (OrderDetailChangeDTO changeOne : orderDetailChangeList) {
            if (mapList.containsKey(changeOne.getOrderId())) {
                List<OrderDetailChangeDTO> orderChangeOneList = mapList.get(changeOne.getOrderId());
                orderChangeOneList.add(changeOne);
            } else {
                mapList.put(changeOne.getOrderId(), new ArrayList<OrderDetailChangeDTO>() {{
                    add(changeOne);
                }});
            }
        }

        List<OrderDetailDTO> orderDetailList = orderDetailApi.getOrderDetailByOrderIds(ids);

        List<Long> goodsList = orderDetailChangeList.stream().map(o -> o.getGoodsId()).collect(Collectors.toList());

        List<Long> promotionActivityIds = orderDetailList.stream().map(OrderDetailDTO::getPromotionActivityId).distinct().collect(Collectors.toList());

        Map<Long, PromotionActivityDTO> couponActivityMap = new HashMap<>();
        List<PromotionActivityDTO> couponActivityList = promotionActivityApi.batchQueryByIdList(promotionActivityIds);
        if (CollectionUtil.isNotEmpty(couponActivityList)) {
            couponActivityMap = couponActivityList.stream().collect(Collectors.toMap(PromotionActivityDTO::getId, E -> E, (k1, k2) -> k1));

        }

        List<ReportPriceParamNameDTO> priceParamNameList = PojoUtils.map(goodsYilingPriceApi.getPriceParamNameList(goodsList, DateUtil.date()), ReportPriceParamNameDTO.class);
        Map<String, BigDecimal> reportPriceMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(priceParamNameList)) {
            reportPriceMap = priceParamNameList.stream().collect(Collectors.toMap(k -> k.getParamId() + "_" + k.getGoodsId(), ReportPriceParamNameDTO::getPrice, (k1, k2) -> k1));

        }
        Map<Long, List<OrderCouponUseDTO>> couponUseMap = new HashMap<>();
        List<OrderCouponUseDTO> orderCouponUseList = orderCouponUseApi.listByOrderIds(ids);
        if (CollectionUtil.isNotEmpty(orderCouponUseList)) {
            couponUseMap = orderCouponUseList.stream().collect(Collectors.groupingBy(OrderCouponUseDTO::getOrderId));
        }

        for (OrderDetailDTO detailOne : orderDetailList) {
            OrderDTO orderDTO = orderMap.get(detailOne.getOrderId());
            String lagName = "";
            List<EnterpriseTagDTO> enterpriseTagList = longListMap.get(orderDTO.getSellerEid());
            if (CollectionUtil.isNotEmpty(enterpriseTagList)) {
                List<String> lagList = enterpriseTagList.stream().map(EnterpriseTagDTO::getName).distinct().collect(Collectors.toList());
                lagName = StringUtils.join(lagList, ",");
            }
            PromotionActivityDTO couponActivityDetailDTO = couponActivityMap.get(detailOne.getPromotionActivityId());

            OrderDetailChangeDTO changeDTO = changeMap.get(detailOne.getId());
            BigDecimal discountAmount = changeDTO.getCouponDiscountAmount().add(changeDTO.getPlatformCouponDiscountAmount()).add(changeDTO.getCashDiscountAmount()).add(changeDTO.getTicketDiscountAmount()).add(changeDTO.getPlatformPaymentDiscountAmount()).add(changeDTO.getShopPaymentDiscountAmount()).subtract(changeDTO.getSellerPlatformCouponDiscountAmount()).subtract(changeDTO.getSellerCouponDiscountAmount()).subtract(changeDTO.getSellerReturnCashDiscountAmount()).subtract(changeDTO.getSellerPlatformPaymentDiscountAmount()).subtract(changeDTO.getSellerShopPaymentDiscountAmount()).subtract(changeDTO.getSellerReturnTicketDiscountAmount());

            BigDecimal discountAmountCoupon = changeDTO.getCouponDiscountAmount().subtract(changeDTO.getSellerCouponDiscountAmount());

            BigDecimal discountAmountPlatformCoupon = changeDTO.getPlatformCouponDiscountAmount().subtract(changeDTO.getSellerPlatformCouponDiscountAmount());

            BigDecimal shopPaymentDiscountAmount = changeDTO.getShopPaymentDiscountAmount().subtract(changeDTO.getSellerShopPaymentDiscountAmount());

            BigDecimal platformPaymentDiscountAmount = changeDTO.getPlatformPaymentDiscountAmount().subtract(changeDTO.getSellerPlatformPaymentDiscountAmount());

            BigDecimal goodsAmount = changeDTO.getGoodsAmount().subtract(changeDTO.getSellerReturnAmount());


            BigDecimal goodsDiscountRate = BigDecimal.ZERO;
            if (goodsAmount.compareTo(BigDecimal.ZERO) != 0) {
                goodsDiscountRate = discountAmount.multiply(BigDecimal.valueOf(100)).divide(goodsAmount, 2, BigDecimal.ROUND_HALF_UP);
            }
            Map<Integer, OrderCouponUseDTO> couponUseTypeMap = new HashMap<>();
            List<OrderCouponUseDTO> orderCouponUseDTOS = couponUseMap.get(detailOne.getOrderId());
            if (CollectionUtil.isNotEmpty(orderCouponUseDTOS)) {
                couponUseTypeMap = orderCouponUseDTOS.stream().collect(Collectors.toMap(OrderCouponUseDTO::getCouponType, E -> E, (k1, k2) -> k1));

            }

            List<OrderPromotionActivityDTO> orderPromotionActivityDTOS = promotionActivityMap.get(detailOne.getOrderId());

            OrderPromotionActivityDTO shopPromotionActivity = null;
            OrderPromotionActivityDTO platformPaymentPromotionActivity = null;
            if (CollectionUtil.isNotEmpty(orderPromotionActivityDTOS)) {
                for (OrderPromotionActivityDTO promotionActivityDTO : orderPromotionActivityDTOS) {
                    if (promotionActivityDTO.getActivityType() == 5) {
                        if (promotionActivityDTO.getSponsorType() == 1) {
                            shopPromotionActivity = promotionActivityDTO;

                        } else if (promotionActivityDTO.getSponsorType() == 2) {
                            platformPaymentPromotionActivity = promotionActivityDTO;
                        }
                    }
                }

            }

            if (mapDelivery.containsKey(detailOne.getId())) {
                List<OrderDeliveryDTO> list = mapDelivery.get(detailOne.getId());
                BigDecimal totalDiscount = BigDecimal.ZERO;
                BigDecimal totalDiscountPlatformCoupon = BigDecimal.ZERO;
                BigDecimal totalDiscountCoupon = BigDecimal.ZERO;
                BigDecimal shopPaymentDiscount = BigDecimal.ZERO;
                BigDecimal platformPaymentDiscount = BigDecimal.ZERO;
                BigDecimal goodsAmountAll = BigDecimal.ZERO;
                for (int i = 0; i < list.size(); i++) {
                    ExportOrderB2BCenterDetailBO exportOrder = getExportOrderB2BAdminDetailBO(detailOne, orderDTO, enterpriseMap, activityMap);
                    if (couponActivityDetailDTO != null && couponActivityDetailDTO.getType() != 1) {
                        exportOrder.setPlatformRatio(couponActivityDetailDTO.getPlatformPercent());
                        exportOrder.setBusinessRatio(couponActivityDetailDTO.getMerchantPercent());
                    }

                    exportOrder.setSellerLag(lagName);
                    exportOrder.setGoodsDiscountRate(goodsDiscountRate);
                    exportOrder.setBatchNo(list.get(i).getBatchNo());
                    exportOrder.setExpiryDate(list.get(i).getExpiryDate());
                    exportOrder.setBatchGoodsQuantity(list.get(i).getDeliveryQuantity());

                    exportOrder.setGoodsSellPrice(reportPriceMap.get(3 + "_" + detailOne.getGoodsId()) == null ? BigDecimal.ZERO : reportPriceMap.get(3 + "_" + detailOne.getGoodsId()));
                    exportOrder.setGoodsSellAmount(exportOrder.getGoodsSellPrice().multiply(BigDecimal.valueOf(exportOrder.getBatchGoodsQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP));
                    exportOrder.setGoodsSupportPrice(reportPriceMap.get(2 + "_" + detailOne.getGoodsId()) == null ? BigDecimal.ZERO : reportPriceMap.get(2 + "_" + detailOne.getGoodsId()));
                    exportOrder.setGoodsSupportAmount(exportOrder.getGoodsSupportPrice().multiply(BigDecimal.valueOf(exportOrder.getBatchGoodsQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP));


                    if (i == list.size() - 1) {
                        exportOrder.setGoodsDiscountAmount(discountAmount.subtract(totalDiscount));
                        exportOrder.setGoodsAmount(changeDTO.getDeliveryAmount().subtract(goodsAmountAll));

                        BigDecimal platformDiscountAmount = BigDecimal.ZERO;
                        OrderCouponUseDTO orderPlatformCouponUseOne = couponUseTypeMap.get(1);
                        if (orderPlatformCouponUseOne != null) {
                            platformDiscountAmount = platformDiscountAmount.add(discountAmountPlatformCoupon.subtract(totalDiscountPlatformCoupon).multiply(orderPlatformCouponUseOne.getPlatformRatio()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN));
                        }
                        OrderCouponUseDTO orderCouponUseOne = couponUseTypeMap.get(2);
                        if (orderCouponUseOne != null) {
                            platformDiscountAmount = platformDiscountAmount.add(discountAmountCoupon.subtract(totalDiscountCoupon).multiply(orderCouponUseOne.getPlatformRatio()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN));
                        }

                        if (shopPromotionActivity != null) {
                            platformDiscountAmount = platformDiscountAmount.add(shopPaymentDiscountAmount.subtract(shopPaymentDiscount).multiply(shopPromotionActivity.getActivityPlatformPercent()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN));
                        }

                        if (platformPaymentPromotionActivity != null) {
                            platformDiscountAmount = platformDiscountAmount.add(platformPaymentDiscountAmount.subtract(platformPaymentDiscount).multiply(platformPaymentPromotionActivity.getActivityPlatformPercent()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN));
                        }

                        exportOrder.setPlatformDiscountAmount(platformDiscountAmount);
                        exportOrder.setBusinessDiscountAmount(discountAmount.subtract(totalDiscount).subtract(platformDiscountAmount));
                    } else {
                        BigDecimal goodsDiscountAmount = discountAmount.multiply(BigDecimal.valueOf(list.get(i).getDeliveryQuantity())).divide(BigDecimal.valueOf(changeDTO.getGoodsQuantity() - changeDTO.getSellerReturnQuantity()), 2, BigDecimal.ROUND_DOWN);

                        totalDiscount = totalDiscount.add(goodsDiscountAmount);
                        exportOrder.setGoodsDiscountAmount(goodsDiscountAmount);

                        //平台优惠券折扣
                        BigDecimal goodsDiscountAmountPlatformCoupon = discountAmountPlatformCoupon.multiply(BigDecimal.valueOf(list.get(i).getDeliveryQuantity())).divide(BigDecimal.valueOf(changeDTO.getGoodsQuantity() - changeDTO.getSellerReturnQuantity()), 2, BigDecimal.ROUND_DOWN);

                        //商家支付优惠
                        BigDecimal goodsDiscountAmountShopPayment = shopPaymentDiscountAmount.multiply(BigDecimal.valueOf(list.get(i).getDeliveryQuantity())).divide(BigDecimal.valueOf(changeDTO.getGoodsQuantity() - changeDTO.getSellerReturnQuantity()), 2, BigDecimal.ROUND_DOWN);

                        shopPaymentDiscount = shopPaymentDiscount.add(goodsDiscountAmountShopPayment);

                        //平台支付优惠
                        BigDecimal goodsDiscountAmountPlatformPayment = platformPaymentDiscountAmount.multiply(BigDecimal.valueOf(list.get(i).getDeliveryQuantity())).divide(BigDecimal.valueOf(changeDTO.getGoodsQuantity() - changeDTO.getSellerReturnQuantity()), 2, BigDecimal.ROUND_DOWN);

                        platformPaymentDiscount = platformPaymentDiscount.add(goodsDiscountAmountPlatformPayment);

                        totalDiscountPlatformCoupon = totalDiscountPlatformCoupon.add(goodsDiscountAmountPlatformCoupon);

                        BigDecimal platformDiscountAmount = BigDecimal.ZERO;
                        OrderCouponUseDTO orderPlatformCouponUseOne = couponUseTypeMap.get(1);

                        if (orderPlatformCouponUseOne != null) {
                            platformDiscountAmount = platformDiscountAmount.add(goodsDiscountAmountPlatformCoupon.multiply(orderPlatformCouponUseOne.getPlatformRatio()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN));
                        }

                        //商家优惠券折扣
                        BigDecimal goodsDiscountAmountCoupon = discountAmountCoupon.multiply(BigDecimal.valueOf(list.get(i).getDeliveryQuantity())).divide(BigDecimal.valueOf(changeDTO.getGoodsQuantity() - changeDTO.getSellerReturnQuantity()), 2, BigDecimal.ROUND_DOWN);

                        totalDiscountCoupon = totalDiscountCoupon.add(goodsDiscountAmountCoupon);
                        OrderCouponUseDTO orderCouponUseOne = couponUseTypeMap.get(2);
                        if (orderCouponUseOne != null) {
                            platformDiscountAmount = platformDiscountAmount.add(goodsDiscountAmountCoupon.multiply(orderCouponUseOne.getPlatformRatio()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN));
                        }

                        if (shopPromotionActivity != null) {
                            platformDiscountAmount = platformDiscountAmount.add(goodsDiscountAmountShopPayment.multiply(shopPromotionActivity.getActivityPlatformPercent()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN));
                        }

                        if (platformPaymentPromotionActivity != null) {
                            platformDiscountAmount = platformDiscountAmount.add(goodsDiscountAmountPlatformPayment.multiply(platformPaymentPromotionActivity.getActivityPlatformPercent()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN));

                        }

                        BigDecimal goodsAmountOne = exportOrder.getGoodsPrice().multiply(BigDecimal.valueOf(exportOrder.getBatchGoodsQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP);
                        goodsAmountAll = goodsAmountAll.add(goodsAmountOne);
                        exportOrder.setGoodsAmount(goodsAmountOne);
                        exportOrder.setPlatformDiscountAmount(platformDiscountAmount);
                        exportOrder.setBusinessDiscountAmount(goodsDiscountAmount.subtract(platformDiscountAmount));
                    }

                    exportOrder.setGoodsPayAmount(exportOrder.getGoodsAmount().subtract(exportOrder.getGoodsDiscountAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));

                    //Map<String, Object> dataPojo = BeanUtil.beanToMap(exportOrder);
                    ExportOrderCenterDetailAdminModel dataPojo = PojoUtils.map(exportOrder, ExportOrderCenterDetailAdminModel.class);
                    result.add(dataPojo);
                }
            } else {
                ExportOrderB2BCenterDetailBO exportOrder = getExportOrderB2BAdminDetailBO(detailOne, orderDTO, enterpriseMap, activityMap);
                exportOrder.setSellerLag(lagName);

                if (couponActivityDetailDTO != null && couponActivityDetailDTO.getType() != 1) {
                    exportOrder.setPlatformRatio(couponActivityDetailDTO.getPlatformPercent());
                    exportOrder.setBusinessRatio(couponActivityDetailDTO.getMerchantPercent());
                }

                exportOrder.setGoodsDiscountRate(goodsDiscountRate);
                exportOrder.setBatchGoodsQuantity(0);
                exportOrder.setGoodsAmount(exportOrder.getGoodsPrice().multiply(BigDecimal.valueOf(exportOrder.getBatchGoodsQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP));
                exportOrder.setGoodsDiscountAmount(BigDecimal.ZERO);
                exportOrder.setGoodsPayAmount(exportOrder.getGoodsAmount().subtract(exportOrder.getGoodsDiscountAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
                exportOrder.setGoodsSellPrice(reportPriceMap.get(3 + "_" + detailOne.getGoodsId()) == null ? BigDecimal.ZERO : reportPriceMap.get(3 + "_" + detailOne.getGoodsId()));
                exportOrder.setGoodsSellAmount(BigDecimal.ZERO);
                exportOrder.setGoodsSupportPrice(reportPriceMap.get(2 + "_" + detailOne.getGoodsId()) == null ? BigDecimal.ZERO : reportPriceMap.get(2 + "_" + detailOne.getGoodsId()));
                exportOrder.setGoodsSupportAmount(BigDecimal.ZERO);
                exportOrder.setBusinessDiscountAmount(BigDecimal.ZERO);
                exportOrder.setPlatformDiscountAmount(BigDecimal.ZERO);
                //Map<String, Object> dataPojo = BeanUtil.beanToMap(exportOrder);
                ExportOrderCenterDetailAdminModel dataPojo = PojoUtils.map(exportOrder, ExportOrderCenterDetailAdminModel.class);
                result.add(dataPojo);
            }
        }

        return result;
    }

    private ExportOrderB2BCenterDetailBO getExportOrderB2BAdminDetailBO(OrderDetailDTO detailOne, OrderDTO orderDTO, Map<Long, EnterpriseDTO> enterpriseMap, Map<Long, Integer> activityMap) {
        ExportOrderB2BCenterDetailBO exportOrder = new ExportOrderB2BCenterDetailBO();
        exportOrder.setOrderNo(orderDTO.getOrderNo());
        exportOrder.setBuyerEid(orderDTO.getBuyerEid());
        exportOrder.setBuyerEname(orderDTO.getBuyerEname());
        exportOrder.setSellerEid(orderDTO.getSellerEid());
        exportOrder.setSellerEname(orderDTO.getSellerEname());
        EnterpriseDTO enterprise = enterpriseMap.get(orderDTO.getSellerEid());
        exportOrder.setSellerProvinceName(StringUtils.isNotEmpty(enterprise.getProvinceName()) ? enterprise.getProvinceName() : "");
        EnterpriseDTO enterpriseDTO = enterpriseMap.get(orderDTO.getBuyerEid());
        exportOrder.setProvinceName(StringUtils.isNotEmpty(enterpriseDTO.getProvinceName()) ? enterpriseDTO.getProvinceName() : "");
        exportOrder.setCityName(StringUtils.isNotEmpty(enterpriseDTO.getCityName()) ? enterpriseDTO.getCityName() : "");
        exportOrder.setRegionName(StringUtils.isNotEmpty(enterpriseDTO.getRegionName()) ? enterpriseDTO.getRegionName() : "");
        exportOrder.setContacterId(orderDTO.getContacterId());
        exportOrder.setContacterName(orderDTO.getContacterName());
        exportOrder.setGoodsId(detailOne.getGoodsId());
        exportOrder.setGoodsName(detailOne.getGoodsName());
        exportOrder.setGoodsQuantity(detailOne.getGoodsQuantity());
        exportOrder.setGoodsSpecification(detailOne.getGoodsSpecification());
        exportOrder.setGoodsLicenseNo(detailOne.getGoodsLicenseNo());
        exportOrder.setGoodsPrice(detailOne.getGoodsPrice());

        exportOrder.setCreateTime(orderDTO.getCreateTime());

        if (orderDTO.getReceiveTime().getTime() > DateUtil.parse("1970-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime()) {
            exportOrder.setReceiveTime(DateUtil.formatDateTime(orderDTO.getReceiveTime()));
        } else {
            exportOrder.setReceiveTime(" ");
        }

        exportOrder.setOrderStatusName(OrderStatusEnum.getByCode(orderDTO.getOrderStatus()) != null ? OrderStatusEnum.getByCode(orderDTO.getOrderStatus()).getName() : "---");
        exportOrder.setPaymentMethodName(PaymentMethodEnum.getByCode(Long.valueOf(orderDTO.getPaymentMethod())) != null ? PaymentMethodEnum.getByCode(Long.valueOf(orderDTO.getPaymentMethod())).getName() : "---");
        exportOrder.setPaymentStatusName(PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()) != null ? PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()).getName() : "---");
        exportOrder.setOrderSourceName(OrderSourceEnum.getByCode(orderDTO.getOrderSource()) != null ? OrderSourceEnum.getByCode(orderDTO.getOrderSource()).getName() : "---");
        exportOrder.setOriginalPrice(detailOne.getOriginalPrice());

        if (activityMap.getOrDefault(detailOne.getOrderId(), 0) != 0 && PromotionActivityTypeEnum.getByCode(activityMap.get(detailOne.getOrderId())) != null && activityMap.getOrDefault(detailOne.getOrderId(), 0) != 1) {
            exportOrder.setActivityType(PromotionActivityTypeEnum.getByCode(activityMap.get(detailOne.getOrderId())).getName());
        }
        return exportOrder;
    }

    @Override
    public QueryExportDataDTO queryData(QueryBackOrderInfoRequest request) {
        return null;
    }


    @Override
    public QueryBackOrderInfoRequest getParam(Map<String, Object> map) {
        QueryBackOrderInfoRequest request = PojoUtils.map(map, QueryBackOrderInfoRequest.class);

        return request;
    }
}
