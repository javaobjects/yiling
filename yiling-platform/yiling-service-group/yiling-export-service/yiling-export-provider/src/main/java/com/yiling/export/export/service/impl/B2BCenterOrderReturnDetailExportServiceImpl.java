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
import com.yiling.export.export.bo.ExportReturnDetailB2BCenterBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
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
import com.yiling.order.order.enums.ReturnSourceEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 退货单明细导出-运营端
 *
 * @author: yong.zhang
 * @date: 2021/11/11
 */
@Slf4j
@Service("b2BCenterOrderReturnDetailExportService")
public class B2BCenterOrderReturnDetailExportServiceImpl implements BaseExportQueryDataService<OrderReturnPageListRequest> {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderReturnDetailBatchApi orderReturnDetailBatchApi;
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
        FIELD.put("sellerEid", "供应商ID");
        FIELD.put("sellerEname", "供应商名称");
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
        Page<OrderReturnDTO> page;
        int current = 1;
        log.info("【退货单导出】运营后台退货单明细导出，请求数据为:[{}]", request);
        do {
            request.setCurrent(current);
            request.setSize(500);
            //  查询导出的数据填入data
            page = orderReturnApi.pageList(request);
            List<OrderReturnDTO> returnDTOList = page.getRecords();
            if (CollUtil.isEmpty(returnDTOList)) {
                continue;
            }
            List<Long> orderIdList = returnDTOList.stream().map(OrderReturnDTO::getOrderId).collect(Collectors.toList());
            List<Long> returnIdList = returnDTOList.stream().map(OrderReturnDTO::getId).collect(Collectors.toList());
            Map<Long, OrderReturnDTO> returnDTOMap = returnDTOList.stream().collect(Collectors.toMap(OrderReturnDTO::getId, o -> o, (k1, k2) -> k1));
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
                ExportReturnDetailB2BCenterBO b2BCenterBO = new ExportReturnDetailB2BCenterBO();

                OrderReturnDTO orderReturnDTO = returnDTOMap.get(batchDTO.getReturnId());
                b2BCenterBO.setReturnNo(orderReturnDTO.getReturnNo());
                b2BCenterBO.setOrderNo(orderReturnDTO.getOrderNo());
                b2BCenterBO.setBuyerEid(orderReturnDTO.getBuyerEid());
                b2BCenterBO.setBuyerEname(orderReturnDTO.getBuyerEname());
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(orderReturnDTO.getBuyerEid());
                b2BCenterBO.setBuyerProvince(enterpriseDTO.getProvinceName());
                b2BCenterBO.setBuyerCity(enterpriseDTO.getCityName());
                b2BCenterBO.setBuyerRegion(enterpriseDTO.getRegionName());
                b2BCenterBO.setSellerEid(orderReturnDTO.getSellerEid());
                b2BCenterBO.setSellerEname(orderReturnDTO.getSellerEname());
                OrderDetailDTO detailDTO = detailDTOMap.get(batchDTO.getDetailId());
                b2BCenterBO.setGoodsId(detailDTO.getGoodsId());
                b2BCenterBO.setGoodsName(detailDTO.getGoodsName());
                b2BCenterBO.setGoodsSpecification(detailDTO.getGoodsSpecification());
                b2BCenterBO.setBatchNo(batchDTO.getBatchNo());
                b2BCenterBO.setExpiredDate(getExpiredDate(batchDTO.getExpiryDate(), orderReturnDTO.getReturnType()));
                b2BCenterBO.setReturnQuality(batchDTO.getReturnQuantity());
                b2BCenterBO.setGoodsPrice(detailDTO.getGoodsPrice());

                OrderReturnDetailDTO returnDetailDTO = returnDetailDTOMap.get(batchDTO.getReturnId() + "-" + batchDTO.getDetailId());
                if (returnDetailDTO.getReturnQuantity() == 0) {
                    continue;
                }
                BigDecimal goodsReturnAmount = returnDetailDTO.getReturnAmount().multiply(BigDecimal.valueOf(batchDTO.getReturnQuantity())).divide(BigDecimal.valueOf(returnDetailDTO.getReturnQuantity()), 2, BigDecimal.ROUND_HALF_UP);
                BigDecimal discountAmount = returnDetailDTO.getReturnPlatformCouponDiscountAmount()
                        .add(returnDetailDTO.getReturnCouponDiscountAmount())
                        .add(returnDetailDTO.getReturnShopPaymentDiscountAmount())
                        .add(returnDetailDTO.getReturnPlatformPaymentDiscountAmount())
                        .add(returnDetailDTO.getReturnCashDiscountAmount())
                        .add(returnDetailDTO.getReturnTicketDiscountAmount())
                        .multiply(BigDecimal.valueOf(batchDTO.getReturnQuantity())).divide(BigDecimal.valueOf(returnDetailDTO.getReturnQuantity()), 2, BigDecimal.ROUND_HALF_UP);
                b2BCenterBO.setGoodsReturnAmount(goodsReturnAmount);
                b2BCenterBO.setGoodsDiscountAmount(discountAmount);
                b2BCenterBO.setReturnAmount(goodsReturnAmount.subtract(discountAmount));

                Map<String, Object> dataPojo = BeanUtil.beanToMap(b2BCenterBO);
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
        int returnSource = Integer.parseInt(map.getOrDefault("returnSource", 0).toString());
        if (0 != returnSource) {
            request.setReturnSourceEnum(ReturnSourceEnum.getByCode(returnSource));
        }
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
