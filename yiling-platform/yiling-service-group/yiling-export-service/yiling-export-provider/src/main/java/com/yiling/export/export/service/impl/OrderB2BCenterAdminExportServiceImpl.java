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
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportOrderB2BCenterBO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.ExportOrderCenterAdminModel;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.marketing.common.enums.CouponActivityTypeEnum;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderCouponUseDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;

/**
 * 订单中台订单导出
 * @author:wei.wang
 * @date:2021/8/21
 */
@Service("orderB2BCenterAdminExportService")
public class OrderB2BCenterAdminExportServiceImpl implements BaseExportQueryDataService<QueryBackOrderInfoRequest> {

    @DubboReference
    OrderDetailChangeApi                               orderDetailChangeApi;
    @DubboReference
    EnterpriseApi                                      enterpriseApi;
    @DubboReference
    OrderAddressApi                                    orderAddressApi;
    @DubboReference
    OrderProcessApi                                    orderMallApi;
    @DubboReference
    CouponActivityApi                                  couponActivityApi;
    @DubboReference
    OrderCouponUseApi                                  orderCouponUseApi;

    @Override
    public QueryExportDataDTO queryData(QueryBackOrderInfoRequest request) {

        return null;
    }

    @Override
    public QueryBackOrderInfoRequest getParam(Map<String, Object> map) {
        QueryBackOrderInfoRequest request = PojoUtils.map(map, QueryBackOrderInfoRequest.class);

        return request;
    }


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
                writeSheet = EasyExcel.writerSheet(fileIdx, "运营后台主订单" + sheetNumber).head(ExportOrderCenterAdminModel.class).build();
                while (true) {
                    request.setCurrent(current);
                    request.setSize(size);

                    List<ExportOrderCenterAdminModel> list = getResult(request);
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

    private List<ExportOrderCenterAdminModel> getResult(QueryBackOrderInfoRequest request){
        Page<OrderDTO> page = orderMallApi.getBackOrderPage(request);
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return ListUtil.empty();
        }
        List<ExportOrderCenterAdminModel> list = new ArrayList<>();

        List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());
        //Map<Long, OrderDTO> orderMap = page.getRecords().stream().collect(Collectors.toMap(OrderDTO::getId, o -> o, (k1, k2) -> k1));

        List<Long> eidList = page.getRecords().stream().map(order -> order.getBuyerEid()).collect(Collectors.toList());

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
        for (OrderDTO one : page.getRecords()) {
            EnterpriseDTO enterpriseDTO = enterpriseMap.get(one.getBuyerEid());
            OrderAddressDTO addressOne = addressMap.get(one.getId());

            BigDecimal deliveryAmount = BigDecimal.ZERO;
            BigDecimal discountAmount = BigDecimal.ZERO;
            List<OrderDetailChangeDTO> changeList = mapList.get(one.getId());
            if (CollUtil.isNotEmpty(changeList)) {
                for (OrderDetailChangeDTO changeDTO : changeList) {
                    deliveryAmount = deliveryAmount.add(changeDTO.getGoodsAmount().subtract(changeDTO.getSellerReturnAmount()));
                    discountAmount = discountAmount.add(changeDTO.getCouponDiscountAmount().add(changeDTO.getPlatformCouponDiscountAmount())
                            .add(changeDTO.getCashDiscountAmount()).add(changeDTO.getTicketDiscountAmount())
                            .add(changeDTO.getPlatformPaymentDiscountAmount()).add(changeDTO.getShopPaymentDiscountAmount())
                            .subtract(changeDTO.getSellerPlatformCouponDiscountAmount())
                            .subtract(changeDTO.getSellerCouponDiscountAmount())
                            .subtract(changeDTO.getSellerReturnCashDiscountAmount())
                            .subtract(changeDTO.getSellerReturnTicketDiscountAmount()))
                            .subtract(changeDTO.getSellerShopPaymentDiscountAmount())
                            .subtract(changeDTO.getSellerPlatformPaymentDiscountAmount());
                }
            }
            ExportOrderB2BCenterBO orderB2BAdmin = new ExportOrderB2BCenterBO();
            orderB2BAdmin.setOrderNo(one.getOrderNo());
            orderB2BAdmin.setBuyerEid(one.getBuyerEid());
            orderB2BAdmin.setBuyerEname(one.getBuyerEname());
            orderB2BAdmin.setProvinceName(Optional.ofNullable(enterpriseDTO).map(EnterpriseDTO::getProvinceName).orElse(""));
            orderB2BAdmin.setCityName(Optional.ofNullable(enterpriseDTO).map(EnterpriseDTO::getCityName).orElse(""));
            orderB2BAdmin.setRegionName(Optional.ofNullable(enterpriseDTO).map(EnterpriseDTO::getRegionName).orElse(""));
            orderB2BAdmin.setSellerEid(one.getSellerEid());
            orderB2BAdmin.setSellerEname(one.getSellerEname());
            orderB2BAdmin.setCreateTime(one.getCreateTime());

            if(one.getReceiveTime().getTime()> DateUtil.parse("1970-01-01 00:00:00","yyyy-MM-dd HH:mm:ss").getTime() ){
                orderB2BAdmin.setReceiveTime(DateUtil.formatDateTime(one.getReceiveTime()));
            }else{
                orderB2BAdmin.setReceiveTime(" ");
            }

            orderB2BAdmin.setOrderStatusName(OrderStatusEnum.getByCode(one.getOrderStatus()) != null ? OrderStatusEnum.getByCode(one.getOrderStatus()).getName() : "---");
            orderB2BAdmin.setPaymentMethodName(PaymentMethodEnum.getByCode(Long.valueOf(one.getPaymentMethod())) != null ? PaymentMethodEnum.getByCode(Long.valueOf(one.getPaymentMethod())).getName() : "---");
            orderB2BAdmin.setPaymentStatusName(PaymentStatusEnum.getByCode(one.getPaymentStatus()) != null ? PaymentStatusEnum.getByCode(one.getPaymentStatus()).getName() : "---");
            orderB2BAdmin.setTotalAmount(one.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
            orderB2BAdmin.setDeliveryAmount(deliveryAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            orderB2BAdmin.setDiscountAmount(discountAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            orderB2BAdmin.setPayAmount(deliveryAmount.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
            orderB2BAdmin.setMobile(Optional.ofNullable(addressOne).map(OrderAddressDTO::getMobile).orElse(" "));
            orderB2BAdmin.setReceiveName(Optional.ofNullable(addressOne).map(OrderAddressDTO::getName).orElse(" "));
            orderB2BAdmin.setAddress(Optional.ofNullable(addressOne).map(OrderAddressDTO::getAddress).orElse(" "));
            orderB2BAdmin.setContacterId(one.getContacterId());
            orderB2BAdmin.setContacterName(one.getContacterName());
            orderB2BAdmin.setCouponActivityInfo(buildCouponActivityInfo(one.getId(), orderCouponUseMap, couponActivityMap));

            ExportOrderCenterAdminModel map = PojoUtils.map(orderB2BAdmin, ExportOrderCenterAdminModel.class);
            list.add(map);
        }

        return list;
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


   /* private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("orderNo", "订单编号");
        FIELD.put("buyerEid", "采购商ID");
        FIELD.put("buyerEname", "采购商名称");
        FIELD.put("provinceName", "采购商所在省");
        FIELD.put("cityName", "采购商所在市");
        FIELD.put("regionName", "采购商所在区");
        FIELD.put("sellerEid", "供应商ID");
        FIELD.put("sellerEname", "供应商名称");
        FIELD.put("createTime", "下单时间");
        FIELD.put("receiveTime", "收货时间");
        FIELD.put("orderStatusName", "订单状态");
        FIELD.put("paymentMethodName", "支付方式");
        FIELD.put("paymentStatusName", "支付状态");
        FIELD.put("totalAmount", "订单总金额");
        FIELD.put("deliveryAmount", "实际发货货款总金额");
        FIELD.put("discountAmount", "折扣总金额");
        FIELD.put("payAmount", "支付总金额");
        FIELD.put("mobile", "收货人联系电话");
        FIELD.put("receiveName", "收货人姓名");
        FIELD.put("address", "收货人详细地址");
        //FIELD.put("contacterId", "商务负责人ID");
        //FIELD.put("contacterName", "商务负责人姓名");
        FIELD.put("couponActivityInfo", "使用优惠券");

    }

    *//**
     * 查询excel中的数据
     *
     * @param request
     * @return
     *//*
    @Override
    public QueryExportDataDTO queryData(QueryBackOrderInfoRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<OrderDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = orderMallApi.getBackOrderPage(request);

            if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }

            List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());
            //Map<Long, OrderDTO> orderMap = page.getRecords().stream().collect(Collectors.toMap(OrderDTO::getId, o -> o, (k1, k2) -> k1));

            List<Long> eidList = page.getRecords().stream().map(order -> order.getBuyerEid()).collect(Collectors.toList());

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
            for (OrderDTO one : page.getRecords()) {
                EnterpriseDTO enterpriseDTO = enterpriseMap.get(one.getBuyerEid());
                OrderAddressDTO addressOne = addressMap.get(one.getId());

                BigDecimal deliveryAmount = BigDecimal.ZERO;
                BigDecimal discountAmount = BigDecimal.ZERO;
                List<OrderDetailChangeDTO> changeList = mapList.get(one.getId());
                if (CollUtil.isNotEmpty(changeList)) {
                    for (OrderDetailChangeDTO changeDTO : changeList) {
                        deliveryAmount = deliveryAmount.add(changeDTO.getGoodsAmount().subtract(changeDTO.getSellerReturnAmount()));
                        discountAmount = discountAmount.add(changeDTO.getCouponDiscountAmount().add(changeDTO.getPlatformCouponDiscountAmount())
                                .add(changeDTO.getCashDiscountAmount()).add(changeDTO.getTicketDiscountAmount())
                                .add(changeDTO.getPlatformPaymentDiscountAmount()).add(changeDTO.getShopPaymentDiscountAmount())
                                .subtract(changeDTO.getSellerPlatformCouponDiscountAmount())
                                .subtract(changeDTO.getSellerCouponDiscountAmount())
                                .subtract(changeDTO.getSellerReturnCashDiscountAmount())
                                .subtract(changeDTO.getSellerReturnTicketDiscountAmount()))
                                .subtract(changeDTO.getSellerShopPaymentDiscountAmount())
                                .subtract(changeDTO.getSellerPlatformPaymentDiscountAmount());
                    }
                }
                ExportOrderB2BCenterBO orderB2BAdmin = new ExportOrderB2BCenterBO();
                orderB2BAdmin.setOrderNo(one.getOrderNo());
                orderB2BAdmin.setBuyerEid(one.getBuyerEid());
                orderB2BAdmin.setBuyerEname(one.getBuyerEname());
                orderB2BAdmin.setProvinceName(Optional.ofNullable(enterpriseDTO).map(EnterpriseDTO::getProvinceName).orElse(""));
                orderB2BAdmin.setCityName(Optional.ofNullable(enterpriseDTO).map(EnterpriseDTO::getCityName).orElse(""));
                orderB2BAdmin.setRegionName(Optional.ofNullable(enterpriseDTO).map(EnterpriseDTO::getRegionName).orElse(""));
                orderB2BAdmin.setSellerEid(one.getSellerEid());
                orderB2BAdmin.setSellerEname(one.getSellerEname());
                orderB2BAdmin.setCreateTime(one.getCreateTime());

                if(one.getReceiveTime().getTime()> DateUtil.parse("1970-01-01 00:00:00","yyyy-MM-dd HH:mm:ss").getTime() ){
                    orderB2BAdmin.setReceiveTime(DateUtil.formatDateTime(one.getReceiveTime()));
                }else{
                    orderB2BAdmin.setReceiveTime(" ");
                }

                orderB2BAdmin.setOrderStatusName(OrderStatusEnum.getByCode(one.getOrderStatus()) != null ? OrderStatusEnum.getByCode(one.getOrderStatus()).getName() : "---");
                orderB2BAdmin.setPaymentMethodName(PaymentMethodEnum.getByCode(Long.valueOf(one.getPaymentMethod())) != null ? PaymentMethodEnum.getByCode(Long.valueOf(one.getPaymentMethod())).getName() : "---");
                orderB2BAdmin.setPaymentStatusName(PaymentStatusEnum.getByCode(one.getPaymentStatus()) != null ? PaymentStatusEnum.getByCode(one.getPaymentStatus()).getName() : "---");
                orderB2BAdmin.setTotalAmount(one.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                orderB2BAdmin.setDeliveryAmount(deliveryAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
                orderB2BAdmin.setDiscountAmount(discountAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
                orderB2BAdmin.setPayAmount(deliveryAmount.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
                orderB2BAdmin.setMobile(Optional.ofNullable(addressOne).map(OrderAddressDTO::getMobile).orElse(" "));
                orderB2BAdmin.setReceiveName(Optional.ofNullable(addressOne).map(OrderAddressDTO::getName).orElse(" "));
                orderB2BAdmin.setAddress(Optional.ofNullable(addressOne).map(OrderAddressDTO::getAddress).orElse(" "));
                orderB2BAdmin.setContacterId(one.getContacterId());
                orderB2BAdmin.setContacterName(one.getContacterName());
                orderB2BAdmin.setCouponActivityInfo(buildCouponActivityInfo(one.getId(), orderCouponUseMap, couponActivityMap));

                Map<String, Object> dataPojo = BeanUtil.beanToMap(orderB2BAdmin);
                data.add(dataPojo);
            }
            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("运营后台主订单");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    *//**
     * 构建优惠券使用信息
     * @param orderId    订单id
     * @param orderCouponUseMap 订单所用券map
     * @param couponActivityMap 优惠券活动map
     * @return
     *//*
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

    @Override
    public QueryBackOrderInfoRequest getParam(Map<String, Object> map) {
        QueryBackOrderInfoRequest request = PojoUtils.map(map, QueryBackOrderInfoRequest.class);

        return request;
    }*/
}
