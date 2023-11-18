package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.export.export.bo.ExportReturnDetailB2BAdminBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.api.OrderReturnDetailApi;
import com.yiling.order.order.api.OrderReturnDetailBatchApi;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.OrderReturnDetailBatchDTO;
import com.yiling.order.order.dto.OrderReturnDetailDTO;
import com.yiling.order.order.dto.request.OrderReturnPageListRequest;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.ReturnSourceEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 退货单明细导出-商家端
 *
 * @author: yong.zhang
 * @date: 2021/11/11
 */
@Slf4j
@Service("b2BAdminOrderReturnDetailExportService")
public class B2BAdminOrderReturnDetailExportServiceImpl implements BaseExportQueryDataService<OrderReturnPageListRequest> {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderReturnDetailBatchApi orderReturnDetailBatchApi;
    @DubboReference
    DataPermissionsApi dataPermissionsApi;
    @DubboReference
    OrderReturnApi orderReturnApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderReturnDetailApi orderReturnDetailApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("returnNo", "退货单单据编号");
        FIELD.put("orderNo", "订单编号");
        FIELD.put("buyerEid", "采购商ID");
        FIELD.put("buyerEname", "采购商名称");
        FIELD.put("buyerProvince", "采购商所在省");
        FIELD.put("buyerCity", "采购商所在市");
        FIELD.put("buyerRegion", "采购商所在区");
        FIELD.put("goodsId", "商品ID");
        FIELD.put("goodsName", "商品名称");
        FIELD.put("goodsSpecification", "规格型号");
        FIELD.put("batchNo", "批次号/序列号");
        FIELD.put("expiredDate", "有效期至");
        FIELD.put("returnQuality", "批次退货数量");
        FIELD.put("goodsPrice", "商品单价");
        FIELD.put("goodsReturnAmount", "退货金额");
        FIELD.put("goodsDiscountAmount", "折扣金额");
        FIELD.put("returnAmount", "退款金额");
    }

    @Override
    public QueryExportDataDTO queryData(OrderReturnPageListRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        if (request.getStartTime() != null) {
            String start = DateUtil.format(request.getStartTime(), "yyyy-MM-dd 00:00:00");
            request.setStartTime(DateUtil.parse(start));
        }
        if (request.getEndTime() != null) {
            String end = DateUtil.format(request.getEndTime(), "yyyy-MM-dd 23:59:59");
            request.setEndTime(DateUtil.parse(end));
        }
        Page<OrderReturnDTO> page;
        int current = 1;
        log.info("【退货单导出】商家后台退货单明细导出，请求数据为:[{}]", request);
        do {
            request.setCurrent(current);
            request.setSize(500);
            //  查询导出的数据填入data
            page = orderReturnApi.pageList(request);
            List<OrderReturnDTO> returnDTOList = page.getRecords();
            List<Long> orderIdList = returnDTOList.stream().map(OrderReturnDTO::getOrderId).collect(Collectors.toList());
            List<Long> returnIdList = returnDTOList.stream().map(OrderReturnDTO::getId).collect(Collectors.toList());
            Map<Long, OrderReturnDTO> returnDTOMap = returnDTOList.stream().collect(Collectors.toMap(OrderReturnDTO::getId, o -> o, (k1, k2) -> k1));
            if (orderIdList.size() < 1 || returnIdList.size() < 1) {
                continue;
            }
            List<OrderDetailDTO> detailDTOList = orderDetailApi.getOrderDetailByOrderIds(orderIdList);
            Map<Long, OrderDetailDTO> detailDTOMap = detailDTOList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, o -> o, (k1, k2) -> k1));

            List<OrderReturnDetailDTO> returnDetailDTOList = orderReturnDetailApi.getOrderReturnDetailByReturnIds(returnIdList);
            Map<String, OrderReturnDetailDTO> returnDetailDTOMap = Maps.newHashMap();
            for (OrderReturnDetailDTO orderReturnDetailDTO : returnDetailDTOList) {
                returnDetailDTOMap.put(orderReturnDetailDTO.getReturnId() + "-" + orderReturnDetailDTO.getDetailId(), orderReturnDetailDTO);
            }
            List<OrderReturnDetailBatchDTO> detailBatchDTOList = orderReturnDetailBatchApi.getOrderReturnDetailBatchByReturnIds(returnIdList);

            for (OrderReturnDetailBatchDTO batchDTO : detailBatchDTOList) {
                if (batchDTO.getReturnQuantity() == 0) {
                    continue;
                }
                ExportReturnDetailB2BAdminBO b2BAdminBO = new ExportReturnDetailB2BAdminBO();

                OrderReturnDTO orderReturnDTO = returnDTOMap.get(batchDTO.getReturnId());
                b2BAdminBO.setReturnNo(orderReturnDTO.getReturnNo());
                b2BAdminBO.setOrderNo(orderReturnDTO.getOrderNo());
                b2BAdminBO.setBuyerEid(orderReturnDTO.getBuyerEid());
                b2BAdminBO.setBuyerEname(orderReturnDTO.getBuyerEname());
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(orderReturnDTO.getBuyerEid());
                b2BAdminBO.setBuyerProvince(enterpriseDTO.getProvinceName());
                b2BAdminBO.setBuyerCity(enterpriseDTO.getCityName());
                b2BAdminBO.setBuyerRegion(enterpriseDTO.getRegionName());
                OrderDetailDTO detailDTO = detailDTOMap.get(batchDTO.getDetailId());
                b2BAdminBO.setGoodsId(detailDTO.getGoodsId());
                b2BAdminBO.setGoodsName(detailDTO.getGoodsName());
                b2BAdminBO.setGoodsSpecification(detailDTO.getGoodsSpecification());
                b2BAdminBO.setBatchNo(batchDTO.getBatchNo());
                b2BAdminBO.setExpiredDate(getExpiredDate(batchDTO.getExpiryDate(), orderReturnDTO.getReturnType()));
                b2BAdminBO.setReturnQuality(batchDTO.getReturnQuantity());
                b2BAdminBO.setGoodsPrice(detailDTO.getGoodsPrice());

                OrderReturnDetailDTO returnDetailDTO = returnDetailDTOMap.get(batchDTO.getReturnId() + "-" + batchDTO.getDetailId());
                if (returnDetailDTO.getReturnQuantity() == 0) {
                    continue;
                }
                BigDecimal goodsReturnAmount = returnDetailDTO.getReturnAmount().multiply(BigDecimal.valueOf(batchDTO.getReturnQuantity())).divide(BigDecimal.valueOf(returnDetailDTO.getReturnQuantity()), 2, BigDecimal.ROUND_HALF_UP);
                BigDecimal discountAmount = returnDetailDTO.getReturnPlatformCouponDiscountAmount()
                        .add(returnDetailDTO.getReturnCouponDiscountAmount()).add(returnDetailDTO.getReturnTicketDiscountAmount())
                        .add(returnDetailDTO.getReturnCashDiscountAmount())
                        .add(returnDetailDTO.getReturnShopPaymentDiscountAmount())
                        .add(returnDetailDTO.getReturnPlatformPaymentDiscountAmount())
                        .multiply(BigDecimal.valueOf(batchDTO.getReturnQuantity())).divide(BigDecimal.valueOf(returnDetailDTO.getReturnQuantity()), 2, BigDecimal.ROUND_HALF_UP);
                b2BAdminBO.setGoodsReturnAmount(goodsReturnAmount);
                b2BAdminBO.setGoodsDiscountAmount(discountAmount);
                b2BAdminBO.setReturnAmount(goodsReturnAmount.subtract(discountAmount));

                Map<String, Object> dataPojo = BeanUtil.beanToMap(b2BAdminBO);
                data.add(dataPojo);
            }
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
        ExportDataDTO exportDataDTO = new ExportDataDTO();

        exportDataDTO.setSheetName("B2B退货单明细列表导出");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public OrderReturnPageListRequest getParam(Map<String, Object> map) {
        OrderReturnPageListRequest request = PojoUtils.map(map, OrderReturnPageListRequest.class);
        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        Long currentUserId = Long.parseLong(map.getOrDefault("currentUserId", 0L).toString());
        List<Long> eidList = new ArrayList<>();
        if (Constants.YILING_EID.equals(eid)) {
            List<Long> userIds = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_B2B, eid, currentUserId);
            request.setUserIdList(userIds);
            // 以岭人员
            eidList = enterpriseApi.listSubEids(Constants.YILING_EID);
        } else {
            //非以岭人员
            eidList.add(eid);
        }
        request.setSellerEidList(eidList);
        request.setReturnSourceEnum(ReturnSourceEnum.B2B_APP);
        request.setOrderTypeEnum(OrderTypeEnum.B2B);
        return request;
    }

    private String getExpiredDate(Date expireDate, Integer returnType) {
        OrderReturnTypeEnum returnTypeEnum = OrderReturnTypeEnum.getByCode(returnType);
        if (null == returnTypeEnum || returnTypeEnum == OrderReturnTypeEnum.SELLER_RETURN_ORDER) {
            return "---";
        }
        return DateUtil.format(expireDate, "yyyy-MM-dd HH:mm:ss");
    }
}
