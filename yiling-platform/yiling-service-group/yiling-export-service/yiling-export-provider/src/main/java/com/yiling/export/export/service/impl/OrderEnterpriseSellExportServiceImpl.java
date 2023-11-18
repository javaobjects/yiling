package com.yiling.export.export.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportOrderB2BCenterBO;
import com.yiling.export.export.bo.ExportOrderB2BCenterDetailBO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.ExportOrderEnterpriseSellCenterDetailModel;
import com.yiling.export.export.model.ExportOrderEnterpriseSellCenterModel;
import com.yiling.export.export.model.FlowSaleExcelModel;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.marketing.common.enums.CouponActivityTypeEnum;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderCouponUseDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderEnterpriseDTO;
import com.yiling.order.order.dto.OrderPromotionActivityDTO;
import com.yiling.order.order.dto.request.QueryOrderPageRequest;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.search.flow.dto.request.EsFlowSaleSearchRequest;
import com.yiling.settlement.report.dto.ReportPriceParamNameDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseTagDTO;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wang.wei
 * @date 2023/4/06
 */
@Slf4j
@Service("orderEnterpriseSellExportServic")
public class OrderEnterpriseSellExportServiceImpl implements BaseExportQueryDataService<QueryOrderPageRequest> {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    DataPermissionsApi dataPermissionsApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderAddressApi orderAddressApi;
    @DubboReference
    CouponActivityApi couponActivityApi;
    @DubboReference
    OrderCouponUseApi orderCouponUseApi;
    @DubboReference
    OrderDeliveryApi orderDeliveryApi;
    @DubboReference
    OrderDetailApi orderDetailApi;

    @Override
    public QueryExportDataDTO queryData(QueryOrderPageRequest request) {
        return null;
    }

    @Override
    public QueryOrderPageRequest getParam(Map<String, Object> map) {

        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        Long currentUserId = Long.parseLong(map.getOrDefault("currentUserId", 0L).toString());

        QueryOrderPageRequest request = PojoUtils.map(map, QueryOrderPageRequest.class);

        /*List<Long> subEidLists = enterpriseApi.listSubEids(Constants.YILING_EID);

        List<Long> list = new ArrayList<>();

        if (Constants.YILING_EID.equals(eid)) {
            list.addAll(subEidLists);
            List<Long> contacterIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, eid, currentUserId);
            request.setContacterIdList(contacterIdList);
        } else {
            //非以岭人员
            list.add(eid);
        }*/
        List<Long> list = new ArrayList<>();
        List<Long> userIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, eid, currentUserId);
        if (CollUtil.isNotEmpty(userIdList) && !userIdList.contains(currentUserId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        Map<Long, List<EnterpriseDTO>> contactUserMap = null;
        if (CollUtil.isNotEmpty(userIdList)) {
            contactUserMap = enterpriseApi.listByContactUserIds(eid, userIdList);
            if(MapUtil.isNotEmpty(contactUserMap)){
                if(!contactUserMap.keySet().contains(currentUserId) || CollUtil.isEmpty(contactUserMap.get(currentUserId))){
                    list.add(-1L);
                }

                List<EnterpriseDTO> enterpriseList = contactUserMap.get(currentUserId);
                list = enterpriseList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
                log.info("企业订单中台数据企业{}", JSON.toJSONString(list));
            }else{
                list.add(-1L);
            }
        }
        request.setType(1);
        request.setEidList(list);

        return request;
    }

    @Override
    public boolean isReturnData() {
        return false;
    }

    @Override
    public byte[] getExportByte(QueryOrderPageRequest request, String fileName) {
        if (request == null) {
            return null;
        }

        //fileName = fileName.substring(0, fileName.lastIndexOf(".xlsx"));

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
            for (int i = 0; i < 2; i++) {
                int current = 1;
                int size = 1000;
                int sheetNumber = 1;
                outer:
                do {
                    WriteSheet writeSheet = null;
                    if (i == 0) {
                        writeSheet = EasyExcel.writerSheet(fileIdx, "企业销售订单信息" + sheetNumber).head(ExportOrderEnterpriseSellCenterModel.class).build();
                    } else if (i == 1) {
                        writeSheet = EasyExcel.writerSheet(fileIdx, "企业销售订单明细信息" + sheetNumber).head(ExportOrderEnterpriseSellCenterDetailModel.class).build();
                    }
                    while (true) {
                        request.setCurrent(current);
                        request.setSize(size);
                        if (i == 0) {
                            List<ExportOrderEnterpriseSellCenterModel> list = getOrderEnterpriseSell(request);
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

                        } else if (i == 1) {

                            List<ExportOrderEnterpriseSellCenterDetailModel> list = getOrderEnterpriseSellDetail(request);
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

            }
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

    private List<ExportOrderEnterpriseSellCenterModel> getOrderEnterpriseSell(QueryOrderPageRequest request) {
        Page<OrderEnterpriseDTO> page = orderApi.getOrderEnterprisePage(request);
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return ListUtil.empty();
        }

        List<ExportOrderEnterpriseSellCenterModel> list = new ArrayList<>();

        List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());

        List<Long> eidList = page.getRecords().stream().map(order -> order.getBuyerEid()).collect(Collectors.toList());

        List<OrderDTO> orderList = orderApi.listByIds(ids);
        Map<Long, OrderDTO> orderMap = orderList.stream().collect(Collectors.toMap(OrderDTO::getId, o -> o, (k1, k2) -> k1));


        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eidList);
        Map<Long, EnterpriseDTO> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, o -> o, (k1, k2) -> k1));

        List<OrderAddressDTO> orderAddressList = orderAddressApi.getOrderAddressList(ids);
        Map<Long, OrderAddressDTO> addressMap = orderAddressList.stream().collect(Collectors.toMap(OrderAddressDTO::getOrderId, o -> o, (k1, k2) -> k1));

        // 获取优惠券相关信息
        List<OrderCouponUseDTO> orderCouponUseList = orderCouponUseApi.listByOrderIds(ids);
        Map<Long, List<OrderCouponUseDTO>> orderCouponUseMap = orderCouponUseList.stream().collect(Collectors.groupingBy(OrderCouponUseDTO::getOrderId));
        List<Long> couponActivityIdList = orderCouponUseList.stream().map(OrderCouponUseDTO::getCouponActivityId).collect(Collectors.toList());
        List<CouponActivityDetailDTO> couponActivityList = couponActivityApi.getCouponActivityById(couponActivityIdList);
        Map<Long, CouponActivityDetailDTO> couponActivityMap = couponActivityList.stream().collect(Collectors.toMap(CouponActivityDetailDTO::getId, one -> one, (k1, k2) -> k1));

        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(ids);
        Map<Long, List<OrderDetailChangeDTO>> mapList = new HashMap<>();
        for (OrderDetailChangeDTO changeOne : orderDetailChangeList) {
            if (mapList.containsKey(changeOne.getOrderId())) {
                List<OrderDetailChangeDTO> orderChangeOneList = mapList.get(changeOne.getOrderId());
                orderChangeOneList.add(changeOne);
            } else {
                mapList.put(changeOne.getOrderId(), new ArrayList<OrderDetailChangeDTO>() {
                    {
                        add(changeOne);
                    }
                });
            }
        }
        for (OrderEnterpriseDTO one : page.getRecords()) {
            EnterpriseDTO enterpriseDTO = enterpriseMap.get(one.getBuyerEid());
            OrderAddressDTO addressOne = addressMap.get(one.getId());
            OrderDTO orderOne = orderMap.get(one.getId());
            BigDecimal deliveryAmount = BigDecimal.ZERO;
            BigDecimal discountAmount = BigDecimal.ZERO;
            List<OrderDetailChangeDTO> changeList = mapList.get(one.getId());
            if (CollUtil.isNotEmpty(changeList)) {
                for (OrderDetailChangeDTO changeDTO : changeList) {
                    deliveryAmount = deliveryAmount.add(changeDTO.getGoodsAmount().subtract(changeDTO.getSellerReturnAmount()));
                    discountAmount = discountAmount.add(changeDTO.getCouponDiscountAmount().add(changeDTO.getPlatformCouponDiscountAmount()).add(changeDTO.getCashDiscountAmount()).add(changeDTO.getTicketDiscountAmount()).subtract(changeDTO.getSellerPlatformCouponDiscountAmount()).subtract(changeDTO.getSellerCouponDiscountAmount()).subtract(changeDTO.getSellerReturnCashDiscountAmount()).subtract(changeDTO.getSellerReturnTicketDiscountAmount()));
                }
            }
            ExportOrderEnterpriseSellCenterModel orderB2BAdmin = new ExportOrderEnterpriseSellCenterModel();
            orderB2BAdmin.setOrderNo(one.getOrderNo());
            orderB2BAdmin.setBuyerEid(one.getBuyerEid());
            orderB2BAdmin.setBuyerEname(one.getBuyerEname());
            orderB2BAdmin.setProvinceName(one.getBuyerProvinceName());
            orderB2BAdmin.setCityName(one.getBuyerCityName());
            orderB2BAdmin.setRegionName(one.getBuyerRegionName());
            orderB2BAdmin.setSellerEid(one.getSellerEid());
            orderB2BAdmin.setSellerEname(one.getSellerEname());
            orderB2BAdmin.setCreateTime(one.getCreateTime());

            if (orderOne.getReceiveTime().getTime() > DateUtil.parse("1970-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime()) {
                orderB2BAdmin.setReceiveTime(DateUtil.formatDateTime(orderOne.getReceiveTime()));
            } else {
                orderB2BAdmin.setReceiveTime(" ");
            }

            orderB2BAdmin.setOrderStatusName(OrderStatusEnum.getByCode(one.getOrderStatus()) != null ? OrderStatusEnum.getByCode(one.getOrderStatus()).getName() : "---");
            orderB2BAdmin.setPaymentMethodName(PaymentMethodEnum.getByCode(Long.valueOf(one.getPaymentMethod())) != null ? PaymentMethodEnum.getByCode(Long.valueOf(one.getPaymentMethod())).getName() : "---");
            orderB2BAdmin.setPaymentStatusName(PaymentStatusEnum.getByCode(one.getPaymentStatus()) != null ? PaymentStatusEnum.getByCode(one.getPaymentStatus()).getName() : "---");
            orderB2BAdmin.setTotalAmount(orderOne.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
            orderB2BAdmin.setDeliveryAmount(deliveryAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            orderB2BAdmin.setDiscountAmount(discountAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            orderB2BAdmin.setPayAmount(deliveryAmount.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
            orderB2BAdmin.setMobile(Optional.ofNullable(addressOne).map(OrderAddressDTO::getMobile).orElse(" "));
            orderB2BAdmin.setReceiveName(Optional.ofNullable(addressOne).map(OrderAddressDTO::getName).orElse(" "));
            orderB2BAdmin.setAddress(Optional.ofNullable(addressOne).map(OrderAddressDTO::getAddress).orElse(" "));
           /* orderB2BAdmin.setContacterId(orderOne.getContacterId());
            orderB2BAdmin.setContacterName(orderOne.getContacterName());*/
            orderB2BAdmin.setCouponActivityInfo(buildCouponActivityInfo(one.getId(), orderCouponUseMap, couponActivityMap));
            list.add(orderB2BAdmin);
        }
        return list;
    }

    /**
     * 构建优惠券使用信息
     *
     * @param orderId 订单id
     * @param orderCouponUseMap 订单所用券map
     * @param couponActivityMap 优惠券活动map
     * @return
     */
    private String buildCouponActivityInfo(Long orderId, Map<Long, List<OrderCouponUseDTO>> orderCouponUseMap, Map<Long, CouponActivityDetailDTO> couponActivityMap) {
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

    private List<ExportOrderEnterpriseSellCenterDetailModel> getOrderEnterpriseSellDetail(QueryOrderPageRequest request){
        Page<OrderEnterpriseDTO> page = orderApi.getOrderEnterprisePage(request);
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return ListUtil.empty();
        };
        List<ExportOrderEnterpriseSellCenterDetailModel> result = new ArrayList<>();

        List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());
        List<OrderDTO> orderList = orderApi.listByIds(ids);
        Map<Long, OrderDTO> orderMap = orderList.stream().collect(Collectors.toMap(OrderDTO::getId, o -> o, (k1, k2) -> k1));

        /*List<OrderPromotionActivityDTO> orderPromotionActivityList = orderPromotionActivityApi.listByOrderIds(ids);
        Map<Long,Integer> activityMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(orderPromotionActivityList)){
            activityMap = orderPromotionActivityList.stream().collect(Collectors.toMap(OrderPromotionActivityDTO::getOrderId, OrderPromotionActivityDTO::getActivityType, (k1, k2) -> k1));
        }*/

        List<Long> eidList = page.getRecords().stream().map(order -> order.getBuyerEid()).collect(Collectors.toList());
        List<Long> eidSellerList = page.getRecords().stream().map(order -> order.getSellerEid()).collect(Collectors.toList());

        //Map<Long, List<EnterpriseTagDTO>> longListMap = enterpriseTagApi.listByEidList(eidSellerList);
        eidList.addAll(eidSellerList);


        /*List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eidList);
        Map<Long, EnterpriseDTO> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, o -> o, (k1, k2) -> k1));*/
        Map<Long, OrderEnterpriseDTO> orderEnterpriseMap = page.getRecords().stream().collect(Collectors.toMap(OrderEnterpriseDTO::getId, o -> o, (k1, k2) -> k1));

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

        /*List<Long> goodsList = orderDetailChangeList.stream().map(o -> o.getGoodsId()).collect(Collectors.toList());

        List<Long> promotionActivityIds = orderDetailList.stream().map(OrderDetailDTO::getPromotionActivityId).distinct().collect(Collectors.toList());

        Map<Long, PromotionActivityDTO> couponActivityMap = new HashMap<>();
        List<PromotionActivityDTO> couponActivityList = promotionActivityApi.batchQueryByIdList(promotionActivityIds);
        if(CollectionUtil.isNotEmpty(couponActivityList)){
            couponActivityMap = couponActivityList.stream().collect(Collectors.toMap(PromotionActivityDTO::getId, E -> E, (k1, k2) -> k1));

        }*/

        /*List<ReportPriceParamNameDTO> priceParamNameList = PojoUtils.map(goodsYilingPriceApi.getPriceParamNameList(goodsList, DateUtil.date()), ReportPriceParamNameDTO.class);
        Map<String, BigDecimal> reportPriceMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(priceParamNameList)){
            reportPriceMap = priceParamNameList.stream().collect(Collectors.toMap(k -> k.getParamId() + "_" + k.getGoodsId(), ReportPriceParamNameDTO::getPrice, (k1, k2) -> k1));

        }*/
        /*Map<Long, List<OrderCouponUseDTO>> couponUseMap = new HashMap<>();
        List<OrderCouponUseDTO> orderCouponUseList = orderCouponUseApi.listByOrderIds(ids);
        if(CollectionUtil.isNotEmpty(orderCouponUseList)){
            couponUseMap = orderCouponUseList.stream().collect(Collectors.groupingBy(OrderCouponUseDTO::getOrderId));
        }*/

        for (OrderDetailDTO detailOne : orderDetailList) {
            OrderDTO orderDTO = orderMap.get(detailOne.getOrderId());
            /*String lagName = "";
            List<EnterpriseTagDTO> enterpriseTagList = longListMap.get(orderDTO.getSellerEid());
            if (CollectionUtil.isNotEmpty(enterpriseTagList)){
                List<String> lagList = enterpriseTagList.stream().map(EnterpriseTagDTO::getName).distinct().collect(Collectors.toList());
                lagName = StringUtils.join(lagList, ",");
            }*/
            //PromotionActivityDTO couponActivityDetailDTO = couponActivityMap.get(detailOne.getPromotionActivityId());

            OrderDetailChangeDTO changeDTO = changeMap.get(detailOne.getId());
            BigDecimal discountAmount = changeDTO.getCouponDiscountAmount()
                    .add(changeDTO.getPlatformCouponDiscountAmount())
                    .add(changeDTO.getCashDiscountAmount()).add(changeDTO.getTicketDiscountAmount())
                    .subtract(changeDTO.getSellerPlatformCouponDiscountAmount())
                    .subtract(changeDTO.getSellerCouponDiscountAmount())
                    .subtract(changeDTO.getSellerReturnCashDiscountAmount())
                    .subtract(changeDTO.getSellerReturnTicketDiscountAmount());

            BigDecimal discountAmountCoupon = changeDTO.getCouponDiscountAmount()
                    .subtract(changeDTO.getSellerCouponDiscountAmount());

            BigDecimal discountAmountPlatformCoupon = changeDTO.getPlatformCouponDiscountAmount()
                    .subtract(changeDTO.getSellerPlatformCouponDiscountAmount());

            BigDecimal goodsAmount = changeDTO.getGoodsAmount().subtract(changeDTO.getSellerReturnAmount());


            BigDecimal goodsDiscountRate = BigDecimal.ZERO;
            if(goodsAmount.compareTo(BigDecimal.ZERO) !=0){
                goodsDiscountRate = discountAmount.multiply(BigDecimal.valueOf(100)).divide(goodsAmount, 2, BigDecimal.ROUND_HALF_UP);
            }
            /*Map<Integer, OrderCouponUseDTO> couponUseTypeMap = new HashMap<>();
            List<OrderCouponUseDTO> orderCouponUseDTOS = couponUseMap.get(detailOne.getOrderId());
            if(CollectionUtil.isNotEmpty(orderCouponUseDTOS)){
                couponUseTypeMap = orderCouponUseDTOS.stream().collect(Collectors.toMap(OrderCouponUseDTO::getCouponType, E -> E, (k1, k2) -> k1));

            }*/

            if (mapDelivery.containsKey(detailOne.getId())) {
                List<OrderDeliveryDTO> list = mapDelivery.get(detailOne.getId());
                BigDecimal totalDiscount = BigDecimal.ZERO;
                BigDecimal totalDiscountPlatformCoupon = BigDecimal.ZERO;
                BigDecimal totalDiscountCoupon = BigDecimal.ZERO;
                BigDecimal goodsAmountAll = BigDecimal.ZERO;
                for (int i = 0;i<list.size();i++) {
                    ExportOrderEnterpriseSellCenterDetailModel exportOrder = getExportOrderEnterpriseSellCenterDetailModel(detailOne, orderDTO,orderEnterpriseMap);
                    /*if(couponActivityDetailDTO != null && couponActivityDetailDTO.getType() != 1 ){
                        exportOrder.setPlatformRatio(couponActivityDetailDTO.getPlatformPercent());
                        exportOrder.setBusinessRatio(couponActivityDetailDTO.getMerchantPercent());
                    }*/

                    //exportOrder.setSellerLag(lagName);
                    exportOrder.setGoodsDiscountRate(goodsDiscountRate);
                    exportOrder.setBatchNo(list.get(i).getBatchNo());
                    exportOrder.setExpiryDate(list.get(i).getExpiryDate());
                    exportOrder.setBatchGoodsQuantity(list.get(i).getDeliveryQuantity());

                    /*exportOrder.setGoodsSellPrice(reportPriceMap.get(3+"_"+detailOne.getGoodsId()) == null ? BigDecimal.ZERO : reportPriceMap.get(3+"_"+detailOne.getGoodsId()) );
                    exportOrder.setGoodsSellAmount(exportOrder.getGoodsSellPrice().multiply(BigDecimal.valueOf(exportOrder.getBatchGoodsQuantity())).setScale(2,BigDecimal.ROUND_HALF_UP));
                    exportOrder.setGoodsSupportPrice(reportPriceMap.get(2+"_"+detailOne.getGoodsId()) == null ? BigDecimal.ZERO : reportPriceMap.get(2+"_"+detailOne.getGoodsId()) );
                    exportOrder.setGoodsSupportAmount(exportOrder.getGoodsSupportPrice().multiply(BigDecimal.valueOf(exportOrder.getBatchGoodsQuantity())).setScale(2,BigDecimal.ROUND_HALF_UP));
*/

                    if(i == list.size() -1){
                        exportOrder.setGoodsDiscountAmount(discountAmount.subtract(totalDiscount));
                        exportOrder.setGoodsAmount(changeDTO.getDeliveryAmount().subtract(goodsAmountAll));

                        /*BigDecimal platformDiscountAmount = BigDecimal.ZERO;
                        OrderCouponUseDTO orderPlatformCouponUseOne = couponUseTypeMap.get(1);
                        if(orderPlatformCouponUseOne != null){
                            platformDiscountAmount = platformDiscountAmount.add(discountAmountPlatformCoupon.subtract(totalDiscountPlatformCoupon)
                                    .multiply(orderPlatformCouponUseOne.getPlatformRatio()).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN));
                        }*/
                        /*OrderCouponUseDTO orderCouponUseOne = couponUseTypeMap.get(2);
                        if(orderCouponUseOne != null){
                            platformDiscountAmount = platformDiscountAmount.add(discountAmountCoupon.subtract(totalDiscountCoupon).multiply(orderCouponUseOne.getPlatformRatio()).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_DOWN));
                        }*/

                        //exportOrder.setPlatformDiscountAmount(platformDiscountAmount);
                        //exportOrder.setBusinessDiscountAmount(discountAmount.subtract(totalDiscount).subtract(platformDiscountAmount));
                    }else{
                        BigDecimal goodsDiscountAmount = discountAmount.multiply(BigDecimal.valueOf(list.get(i).getDeliveryQuantity()))
                                .divide(BigDecimal.valueOf(changeDTO.getGoodsQuantity()-changeDTO.getSellerReturnQuantity()),2,BigDecimal.ROUND_DOWN);

                        totalDiscount = totalDiscount.add(goodsDiscountAmount);
                        exportOrder.setGoodsDiscountAmount(goodsDiscountAmount);

                        //平台优惠券折扣
                        BigDecimal goodsDiscountAmountPlatformCoupon = discountAmountPlatformCoupon.multiply(BigDecimal.valueOf(list.get(i).getDeliveryQuantity()))
                                .divide(BigDecimal.valueOf(changeDTO.getGoodsQuantity()-changeDTO.getSellerReturnQuantity()),2,BigDecimal.ROUND_DOWN);

                        totalDiscountPlatformCoupon = totalDiscountPlatformCoupon.add(goodsDiscountAmountPlatformCoupon);

                       /* BigDecimal platformDiscountAmount = BigDecimal.ZERO;
                        OrderCouponUseDTO orderPlatformCouponUseOne = couponUseTypeMap.get(1);

                        if(orderPlatformCouponUseOne != null){
                            platformDiscountAmount = platformDiscountAmount.add(goodsDiscountAmountPlatformCoupon.multiply(orderPlatformCouponUseOne.getPlatformRatio()).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN));
                        }*/

                        //商家优惠券折扣
                        BigDecimal goodsDiscountAmountCoupon = discountAmountCoupon.multiply(BigDecimal.valueOf(list.get(i).getDeliveryQuantity()))
                                .divide(BigDecimal.valueOf(changeDTO.getGoodsQuantity()-changeDTO.getSellerReturnQuantity()),2,BigDecimal.ROUND_DOWN);

                        totalDiscountCoupon = totalDiscountCoupon.add(goodsDiscountAmountCoupon);
                        /*OrderCouponUseDTO orderCouponUseOne = couponUseTypeMap.get(2);
                        if(orderCouponUseOne != null){
                            platformDiscountAmount = platformDiscountAmount.add(goodsDiscountAmountCoupon.multiply(orderCouponUseOne.getPlatformRatio()).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN));
                        }*/

                        BigDecimal goodsAmountOne = exportOrder.getGoodsPrice().multiply(BigDecimal.valueOf(exportOrder.getBatchGoodsQuantity())).setScale(2,BigDecimal.ROUND_HALF_UP);
                        goodsAmountAll = goodsAmountAll.add(goodsAmountOne);
                        exportOrder.setGoodsAmount(goodsAmountOne);
                        /*exportOrder.setPlatformDiscountAmount(platformDiscountAmount);
                        exportOrder.setBusinessDiscountAmount(goodsDiscountAmount.subtract(platformDiscountAmount));*/
                    }

                    exportOrder.setGoodsPayAmount(exportOrder.getGoodsAmount().subtract(exportOrder.getGoodsDiscountAmount()).setScale(2,BigDecimal.ROUND_HALF_UP));
                    result.add(exportOrder);
                }
            } else {
                ExportOrderEnterpriseSellCenterDetailModel exportOrder = getExportOrderEnterpriseSellCenterDetailModel(detailOne, orderDTO, orderEnterpriseMap);
                //exportOrder.setSellerLag(lagName);

                /*if(couponActivityDetailDTO != null && couponActivityDetailDTO.getType() != 1 ){
                    exportOrder.setPlatformRatio(couponActivityDetailDTO.getPlatformPercent());
                    exportOrder.setBusinessRatio(couponActivityDetailDTO.getMerchantPercent());
                }*/

                exportOrder.setGoodsDiscountRate(goodsDiscountRate);
                exportOrder.setBatchGoodsQuantity(0);
                exportOrder.setGoodsAmount(exportOrder.getGoodsPrice().multiply(BigDecimal.valueOf(exportOrder.getBatchGoodsQuantity())).setScale(2,BigDecimal.ROUND_HALF_UP));
                exportOrder.setGoodsDiscountAmount(BigDecimal.ZERO);
                exportOrder.setGoodsPayAmount(exportOrder.getGoodsAmount().subtract(exportOrder.getGoodsDiscountAmount()).setScale(2,BigDecimal.ROUND_HALF_UP));
                //exportOrder.setGoodsSellPrice(reportPriceMap.get(3+"_"+detailOne.getGoodsId()) == null ? BigDecimal.ZERO : reportPriceMap.get(3+"_"+detailOne.getGoodsId()) );
                //exportOrder.setGoodsSellAmount(BigDecimal.ZERO);
                //exportOrder.setGoodsSupportPrice(reportPriceMap.get(2+"_"+detailOne.getGoodsId()) == null ? BigDecimal.ZERO : reportPriceMap.get(2+"_"+detailOne.getGoodsId()) );
                //exportOrder.setGoodsSupportAmount(BigDecimal.ZERO);
                //exportOrder.setBusinessDiscountAmount(BigDecimal.ZERO);
                //exportOrder.setPlatformDiscountAmount(BigDecimal.ZERO);
                result.add(exportOrder);
            }
        }

        return result;


    }

    private ExportOrderEnterpriseSellCenterDetailModel getExportOrderEnterpriseSellCenterDetailModel(OrderDetailDTO detailOne, OrderDTO orderDTO,Map<Long, OrderEnterpriseDTO> orderEnterpriseMap) {
        ExportOrderEnterpriseSellCenterDetailModel exportOrder = new ExportOrderEnterpriseSellCenterDetailModel();
        exportOrder.setOrderNo(orderDTO.getOrderNo());
        exportOrder.setBuyerEid(orderDTO.getBuyerEid());
        exportOrder.setBuyerEname(orderDTO.getBuyerEname());
        exportOrder.setSellerEid(orderDTO.getSellerEid());
        exportOrder.setSellerEname(orderDTO.getSellerEname());
        //EnterpriseDTO enterprise = orderEnterpriseMap.get(orderDTO.getSellerEid());
        OrderEnterpriseDTO orderEnterpriseDTO = orderEnterpriseMap.get(orderDTO.getId());
        exportOrder.setSellerProvinceName(orderEnterpriseDTO.getSellerProvinceName());
        //EnterpriseDTO enterpriseDTO = enterpriseMap.get(orderDTO.getBuyerEid());
        exportOrder.setProvinceName(orderEnterpriseDTO.getBuyerProvinceName());
        exportOrder.setCityName(orderEnterpriseDTO.getBuyerCityName());
        exportOrder.setRegionName(orderEnterpriseDTO.getBuyerRegionName());
      /*  exportOrder.setContacterId(orderDTO.getContacterId());
        exportOrder.setContacterName(orderDTO.getContacterName());*/
        exportOrder.setGoodsId(detailOne.getGoodsId());
        exportOrder.setGoodsName(detailOne.getGoodsName());
        exportOrder.setGoodsQuantity(detailOne.getGoodsQuantity());
        exportOrder.setGoodsSpecification(detailOne.getGoodsSpecification());
        exportOrder.setGoodsLicenseNo(detailOne.getGoodsLicenseNo());
        exportOrder.setGoodsPrice(detailOne.getGoodsPrice());

        exportOrder.setCreateTime(orderDTO.getCreateTime());

        if(orderDTO.getReceiveTime().getTime()> DateUtil.parse("1970-01-01 00:00:00","yyyy-MM-dd HH:mm:ss").getTime() ){
            exportOrder.setReceiveTime(DateUtil.formatDateTime(orderDTO.getReceiveTime()));
        }else{
            exportOrder.setReceiveTime(" ");
        }

        exportOrder.setOrderStatusName(OrderStatusEnum.getByCode(orderDTO.getOrderStatus()) != null ? OrderStatusEnum.getByCode(orderDTO.getOrderStatus()).getName() : "---");
        exportOrder.setPaymentMethodName(PaymentMethodEnum.getByCode(Long.valueOf(orderDTO.getPaymentMethod())) != null ? PaymentMethodEnum.getByCode(Long.valueOf(orderDTO.getPaymentMethod())).getName() : "---");
        exportOrder.setPaymentStatusName(PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()) != null ? PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()).getName() : "---");
        exportOrder.setOrderSourceName(OrderSourceEnum.getByCode(orderDTO.getOrderSource()) != null ? OrderSourceEnum.getByCode(orderDTO.getOrderSource()).getName() : "---");
        exportOrder.setOriginalPrice(detailOne.getOriginalPrice());

        /*if( activityMap.getOrDefault(detailOne.getOrderId(),0) != 0 &&
                PromotionActivityTypeEnum.getByCode(activityMap.get(detailOne.getOrderId())) != null &&
                activityMap.getOrDefault(detailOne.getOrderId(),0) != 1  ){
            exportOrder.setActivityType(PromotionActivityTypeEnum.getByCode(activityMap.get(detailOne.getOrderId())).getName());
        }*/
        return exportOrder;
    }

}
