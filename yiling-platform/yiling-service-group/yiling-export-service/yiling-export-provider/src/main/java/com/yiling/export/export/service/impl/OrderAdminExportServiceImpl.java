package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportOrderPurchaseBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;

import cn.hutool.core.bean.BeanUtil;

/**
 * @author:wei.wang
 * @date:2021/8/23
 */
@Service("orderAdminExportService")
public class OrderAdminExportServiceImpl implements BaseExportQueryDataService<QueryBackOrderInfoRequest> {

    @DubboReference
    OrderApi             orderApi;
    @DubboReference
    OrderDetailApi       orderDetailApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderDeliveryApi     orderDeliveryApi;
    @DubboReference
    OrderAddressApi      orderAddressApi;
    @DubboReference
    OrderProcessApi      orderMallApi;


    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("orderNo", "订单编号");
        FIELD.put("buyerEid", "采购商ID");
        FIELD.put("buyerEname", "采购商名称");
        FIELD.put("sellerEid", "供应商ID");
        FIELD.put("sellerEname", "供应商名称");
        FIELD.put("contractNumber", "合同编号");
        FIELD.put("createTime", "下单时间");
        FIELD.put("orderStatusName", "订单状态");
        FIELD.put("paymentMethodName", "支付方式");
        FIELD.put("paymentStatusName", "支付状态");
        FIELD.put("totalAmount", "订单总金额");
        FIELD.put("deliveryAmount", "实际发货货款总金额");
        FIELD.put("discountAmount", "折扣总金额");
        FIELD.put("payAmount", "支付总金额");
        FIELD.put("goodsId", "商品ID");
        FIELD.put("goodsName", "商品名称");
        FIELD.put("goodsSpecification", "规格型号");
        FIELD.put("goodsLicenseNo", "批准文号");
        FIELD.put("goodsQuantity", "购买数量");
        FIELD.put("goodsDeliveryQuantity", "发货数量");
        FIELD.put("goodsPrice", "商品单价");
        FIELD.put("goodsAmount", "金额小计");
        FIELD.put("goodsDiscountRate", "折扣比率");
        FIELD.put("goodsDiscountAmount", "折扣金额");
        FIELD.put("goodsPayAmount", "支付金额");
        FIELD.put("batchNo", "批次号/序列号");
        FIELD.put("expiryDate", "有效期至");
        FIELD.put("batchGoodsQuantity", "批次发货数量");
        FIELD.put("mobile", "收货人联系电话");
        FIELD.put("receiveName", "收货人姓名");
        FIELD.put("address", "收货人详细地址");
        FIELD.put("contacterId", "商务负责人ID");
        FIELD.put("contacterName", "商务负责人姓名");


    }

    /**
     * 查询excel中的数据
     *
     * @param request
     * @return
     */
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
            Map<Long, OrderDTO> orderMap = page.getRecords().stream().collect(Collectors.toMap(OrderDTO::getId, o -> o, (k1, k2) -> k1));

            List<OrderAddressDTO> orderAddressList = orderAddressApi.getOrderAddressList(ids);
            Map<Long, OrderAddressDTO> addressMap = orderAddressList.stream().collect(Collectors.toMap(OrderAddressDTO::getOrderId, o -> o, (k1, k2) -> k1));

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
            for (OrderDetailDTO detailOne : orderDetailList) {
                OrderDTO orderDTO = orderMap.get(detailOne.getOrderId());

                BigDecimal deliveryAmount = BigDecimal.ZERO;
                BigDecimal discountAmount = BigDecimal.ZERO;
                List<OrderDetailChangeDTO> changeList = mapList.get(detailOne.getOrderId());
                for (OrderDetailChangeDTO changeDTO : changeList) {
                    deliveryAmount = deliveryAmount.add(changeDTO.getGoodsAmount().subtract(changeDTO.getSellerReturnAmount()));
                    discountAmount = discountAmount.add(changeDTO.getCashDiscountAmount().add(changeDTO.getTicketDiscountAmount())
                            .subtract(changeDTO.getSellerReturnCashDiscountAmount()).subtract(changeDTO.getSellerReturnTicketDiscountAmount()));
                }

                OrderDetailChangeDTO changeDTO = changeMap.get(detailOne.getId());
                OrderAddressDTO addressOne = addressMap.get(detailOne.getOrderId());

                if (mapDelivery.containsKey(detailOne.getId())) {
                    List<OrderDeliveryDTO> list = mapDelivery.get(detailOne.getId());
                    for (OrderDeliveryDTO deliveryOne : list) {
                        ExportOrderPurchaseBO purchaseOne = getExportOrderPurchaseBO(detailOne, orderDTO, deliveryAmount, discountAmount, changeDTO, addressOne);
                        purchaseOne.setBatchNo(deliveryOne.getBatchNo());
                        purchaseOne.setExpiryDate(deliveryOne.getExpiryDate());
                        purchaseOne.setBatchGoodsQuantity(deliveryOne.getDeliveryQuantity());
                        Map<String, Object> dataPojo = BeanUtil.beanToMap(purchaseOne);
                        data.add(dataPojo);
                    }
                } else {
                    ExportOrderPurchaseBO purchaseOne = getExportOrderPurchaseBO(detailOne, orderDTO, deliveryAmount, discountAmount, changeDTO, addressOne);
                    Map<String, Object> dataPojo = BeanUtil.beanToMap(purchaseOne);
                    data.add(dataPojo);
                }
            }

            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("采购订单列表导出");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    private ExportOrderPurchaseBO getExportOrderPurchaseBO(OrderDetailDTO detailOne, OrderDTO orderDTO, BigDecimal deliveryAmount, BigDecimal discountAmount, OrderDetailChangeDTO changeDTO, OrderAddressDTO addressOne) {
        ExportOrderPurchaseBO purchaseOne = new ExportOrderPurchaseBO();
        purchaseOne.setOrderNo(orderDTO.getOrderNo());
        purchaseOne.setBuyerEid(orderDTO.getBuyerEid());
        purchaseOne.setBuyerEname(orderDTO.getBuyerEname());
        purchaseOne.setSellerEid(orderDTO.getSellerEid());
        purchaseOne.setSellerEname(orderDTO.getSellerEname());
        purchaseOne.setContractNumber(orderDTO.getContractNumber());
        purchaseOne.setCreateTime(orderDTO.getCreateTime());
        purchaseOne.setOrderStatusName(OrderStatusEnum.getByCode(orderDTO.getOrderStatus()) != null ? OrderStatusEnum.getByCode(orderDTO.getOrderStatus()).getName() : "---");
        purchaseOne.setPaymentMethodName(PaymentMethodEnum.getByCode(Long.valueOf(orderDTO.getPaymentMethod())) != null ? PaymentMethodEnum.getByCode(Long.valueOf(orderDTO.getPaymentMethod())).getName() : "---");
        purchaseOne.setPaymentStatusName(PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()) != null ? PaymentStatusEnum.getByCode(orderDTO.getPaymentStatus()).getName() : "---");
        purchaseOne.setTotalAmount(orderDTO.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        purchaseOne.setDeliveryAmount(deliveryAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        purchaseOne.setDiscountAmount(discountAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        purchaseOne.setPayAmount(deliveryAmount.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
        purchaseOne.setGoodsId(detailOne.getGoodsId());
        purchaseOne.setGoodsName(detailOne.getGoodsName());
        purchaseOne.setGoodsSpecification(detailOne.getGoodsSpecification());
        purchaseOne.setGoodsLicenseNo(detailOne.getGoodsLicenseNo());
        purchaseOne.setGoodsQuantity(detailOne.getGoodsQuantity());
        purchaseOne.setGoodsDeliveryQuantity(changeDTO.getDeliveryQuantity());
        purchaseOne.setGoodsPrice(changeDTO.getGoodsPrice());
        purchaseOne.setGoodsAmount(changeDTO.getGoodsAmount().subtract(changeDTO.getSellerReturnAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
        purchaseOne.setGoodsDiscountAmount(changeDTO.getCashDiscountAmount().add(changeDTO.getTicketDiscountAmount()).subtract(changeDTO.getSellerReturnCashDiscountAmount()).subtract(changeDTO.getSellerReturnTicketDiscountAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
        if (purchaseOne.getGoodsAmount().compareTo(BigDecimal.ZERO) != 0) {
            purchaseOne.setGoodsDiscountRate(purchaseOne.getGoodsDiscountAmount().multiply(BigDecimal.valueOf(100)).divide(purchaseOne.getGoodsAmount(), 2, BigDecimal.ROUND_HALF_UP));
        } else {
            purchaseOne.setGoodsDiscountRate(BigDecimal.ZERO);
        }
        purchaseOne.setGoodsPayAmount(purchaseOne.getGoodsAmount().subtract(purchaseOne.getGoodsDiscountAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
        purchaseOne.setMobile(addressOne.getMobile());
        purchaseOne.setReceiveName(addressOne.getName());
        purchaseOne.setAddress(addressOne.getAddress());
        purchaseOne.setContacterId(orderDTO.getContacterId());
        purchaseOne.setContacterName(orderDTO.getContacterName());
        return purchaseOne;
    }

    @Override
    public QueryBackOrderInfoRequest getParam(Map<String, Object> map) {

        QueryBackOrderInfoRequest request = PojoUtils.map(map, QueryBackOrderInfoRequest.class);
        return request;
    }
}
