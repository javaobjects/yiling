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
import com.yiling.export.export.bo.ExportOrderB2BAdminDetailBO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.ExportB2BOrderSaleDetailModule;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.request.QueryOrderPageRequest;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;

/**
 * 订单B2B商家后台订单明细导出
 *
 * @author:wei.wang
 * @date:2021/8/21
 */
@Service("orderB2BAdminDetailExportService")
public class OrderB2BAdminDetailExportServiceImpl implements BaseExportQueryDataService<QueryOrderPageRequest> {

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderDeliveryApi orderDeliveryApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    /*private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("orderNo", "订单编号");
        FIELD.put("buyerEid", "采购商ID");
        FIELD.put("buyerEname", "采购商名称");
        FIELD.put("provinceName", "采购商所在省");
        FIELD.put("cityName", "采购商所在市");
        FIELD.put("regionName", "采购商所在区");
        FIELD.put("createTime", "下单时间");
        FIELD.put("goodsId", "商品ID");
        FIELD.put("goodsName", "商品名称");
        FIELD.put("goodsQuantity", "购买数量");
        FIELD.put("goodsSpecification", "规格型号");
        FIELD.put("goodsLicenseNo", "批准文号");
        FIELD.put("batchNo", "批次号/序列号");
        FIELD.put("expiryDate", "有效期至");
        FIELD.put("batchGoodsQuantity", "发货数量");
        FIELD.put("goodsPrice", "商品单价");
        FIELD.put("goodsAmount", "金额小计");
        FIELD.put("goodsDiscountRate", "折扣比率");
        FIELD.put("goodsDiscountAmount", "折扣金额");
        FIELD.put("goodsPayAmount", "支付金额");

    }*/

    /**
     * 查询excel中的数据
     *
     * @param queryOrderPageRequest
     * @return
     */
    public List<ExportB2BOrderSaleDetailModule> getResult(QueryOrderPageRequest queryOrderPageRequest) {


        Page<OrderDTO> page = orderApi.getOrderPage(queryOrderPageRequest);

        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return ListUtil.empty();
        }
        List<ExportB2BOrderSaleDetailModule> result = new ArrayList<>();

        List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());
        Map<Long, OrderDTO> orderMap = page.getRecords().stream().collect(Collectors.toMap(OrderDTO::getId, o -> o, (k1, k2) -> k1));

        List<Long> eidList = page.getRecords().stream().map(order -> order.getBuyerEid()).collect(Collectors.toList());
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
        for (OrderDetailDTO detailOne : orderDetailList) {
            OrderDTO orderDTO = orderMap.get(detailOne.getOrderId());

            OrderDetailChangeDTO changeDTO = changeMap.get(detailOne.getId());
            BigDecimal discountAmount = changeDTO.getCouponDiscountAmount().add(changeDTO.getPlatformCouponDiscountAmount()).add(changeDTO.getCashDiscountAmount()).add(changeDTO.getTicketDiscountAmount()).add(changeDTO.getPlatformPaymentDiscountAmount()).add(changeDTO.getShopPaymentDiscountAmount()).subtract(changeDTO.getSellerPlatformCouponDiscountAmount()).subtract(changeDTO.getSellerCouponDiscountAmount()).subtract(changeDTO.getSellerShopPaymentDiscountAmount()).subtract(changeDTO.getSellerPlatformPaymentDiscountAmount()).subtract(changeDTO.getSellerReturnCashDiscountAmount()).subtract(changeDTO.getSellerReturnTicketDiscountAmount());
            BigDecimal goodsAmount = changeDTO.getGoodsAmount().subtract(changeDTO.getSellerReturnAmount());

            EnterpriseDTO enterpriseDTO = enterpriseMap.get(orderDTO.getBuyerEid());
            BigDecimal goodsDiscountRate = BigDecimal.ZERO;
            if (goodsAmount.compareTo(BigDecimal.ZERO) != 0) {
                goodsDiscountRate = discountAmount.multiply(BigDecimal.valueOf(100)).divide(goodsAmount, 2, BigDecimal.ROUND_HALF_UP);
            }
            if (mapDelivery.containsKey(detailOne.getId())) {
                List<OrderDeliveryDTO> list = mapDelivery.get(detailOne.getId());
                BigDecimal totalDiscount = BigDecimal.ZERO;
                BigDecimal goodsAmountAll = BigDecimal.ZERO;
                for (int i = 0; i < list.size(); i++) {
                    ExportOrderB2BAdminDetailBO exportOrder = getExportOrderB2BAdminDetailBO(detailOne, orderDTO, enterpriseDTO);
                    exportOrder.setGoodsDiscountRate(goodsDiscountRate);
                    exportOrder.setBatchNo(list.get(i).getBatchNo());
                    exportOrder.setExpiryDate(list.get(i).getExpiryDate());
                    exportOrder.setBatchGoodsQuantity(list.get(i).getDeliveryQuantity());

                    if (i == list.size() - 1) {
                        exportOrder.setGoodsDiscountAmount(discountAmount.subtract(totalDiscount));
                        exportOrder.setGoodsAmount(changeDTO.getDeliveryAmount().subtract(goodsAmountAll));
                    } else {
                        BigDecimal goodsDiscountAmount = discountAmount.multiply(BigDecimal.valueOf(list.get(i).getDeliveryQuantity())).divide(BigDecimal.valueOf(changeDTO.getGoodsQuantity() - changeDTO.getSellerReturnQuantity()), 2, BigDecimal.ROUND_DOWN);
                        totalDiscount = totalDiscount.add(goodsDiscountAmount);
                        BigDecimal goodsAmountOne = exportOrder.getGoodsPrice().multiply(BigDecimal.valueOf(exportOrder.getBatchGoodsQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP);
                        goodsAmountAll = goodsAmountAll.add(goodsAmountOne);
                        exportOrder.setGoodsAmount(goodsAmountOne);
                        exportOrder.setGoodsDiscountAmount(goodsDiscountAmount);
                    }

                    exportOrder.setGoodsPayAmount(exportOrder.getGoodsAmount().subtract(exportOrder.getGoodsDiscountAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    ExportB2BOrderSaleDetailModule dataPojo = PojoUtils.map(exportOrder, ExportB2BOrderSaleDetailModule.class);
                    //Map<String, Object> dataPojo = BeanUtil.beanToMap(exportOrder);
                    result.add(dataPojo);
                }
            } else {
                ExportOrderB2BAdminDetailBO exportOrder = getExportOrderB2BAdminDetailBO(detailOne, orderDTO, enterpriseDTO);
                exportOrder.setGoodsDiscountRate(goodsDiscountRate);
                exportOrder.setBatchGoodsQuantity(0);
                exportOrder.setGoodsAmount(exportOrder.getGoodsPrice().multiply(BigDecimal.valueOf(exportOrder.getBatchGoodsQuantity())).setScale(2, BigDecimal.ROUND_HALF_UP));
                exportOrder.setGoodsDiscountAmount(BigDecimal.ZERO);
                exportOrder.setGoodsPayAmount(exportOrder.getGoodsAmount().subtract(exportOrder.getGoodsDiscountAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));

                ExportB2BOrderSaleDetailModule dataPojo = PojoUtils.map(exportOrder, ExportB2BOrderSaleDetailModule.class);
                //Map<String, Object> dataPojo = BeanUtil.beanToMap(exportOrder);
                result.add(dataPojo);
            }
        }

        //exportDataDTO.setSheetName("B2B销售订单明细列表");
        return result;
    }

    private ExportOrderB2BAdminDetailBO getExportOrderB2BAdminDetailBO(OrderDetailDTO detailOne, OrderDTO orderDTO, EnterpriseDTO enterpriseDTO) {
        ExportOrderB2BAdminDetailBO exportOrder = new ExportOrderB2BAdminDetailBO();
        exportOrder.setOrderNo(orderDTO.getOrderNo());
        exportOrder.setBuyerEid(orderDTO.getBuyerEid());
        exportOrder.setBuyerEname(orderDTO.getBuyerEname());
        exportOrder.setProvinceName(StringUtils.isNotEmpty(enterpriseDTO.getProvinceName()) ? enterpriseDTO.getProvinceName() : "");
        exportOrder.setCityName(StringUtils.isNotEmpty(enterpriseDTO.getCityName()) ? enterpriseDTO.getCityName() : "");
        exportOrder.setRegionName(StringUtils.isNotEmpty(enterpriseDTO.getRegionName()) ? enterpriseDTO.getRegionName() : "");
        exportOrder.setCreateTime(orderDTO.getCreateTime());
        exportOrder.setGoodsId(detailOne.getGoodsId());
        exportOrder.setGoodsName(detailOne.getGoodsName());
        exportOrder.setGoodsQuantity(detailOne.getGoodsQuantity());
        exportOrder.setGoodsSpecification(detailOne.getGoodsSpecification());
        exportOrder.setGoodsLicenseNo(detailOne.getGoodsLicenseNo());
        exportOrder.setGoodsPrice(detailOne.getGoodsPrice());
        return exportOrder;
    }

    @Override
    public QueryExportDataDTO queryData(QueryOrderPageRequest request) {
        return null;
    }

    @Override
    public QueryOrderPageRequest getParam(Map<String, Object> map) {
        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        QueryOrderPageRequest request = PojoUtils.map(map, QueryOrderPageRequest.class);
        request.setType(1);
        //request.setYiLingOrdinary(Boolean.FALSE);
        request.setOrderType(OrderTypeEnum.B2B.getCode());
        request.setEidList(new ArrayList<Long>() {{
            add(eid);
        }});
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
                writeSheet = EasyExcel.writerSheet(fileIdx, "B2B销售订单明细列表" + sheetNumber).head(ExportB2BOrderSaleDetailModule.class).build();
                while (true) {
                    request.setCurrent(current);
                    request.setSize(size);

                    List<ExportB2BOrderSaleDetailModule> list = getResult(request);
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
}
