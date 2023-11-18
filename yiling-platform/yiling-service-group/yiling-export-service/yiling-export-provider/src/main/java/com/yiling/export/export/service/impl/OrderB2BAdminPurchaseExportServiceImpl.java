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
import com.yiling.export.export.bo.ExportOrderB2BAdminBO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.ExportB2BOrderPurchaseModule;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.request.QueryOrderPageRequest;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;

/**
 * 订单B2B商家后台采购订单导出
 *
 * @author:wei.wang
 * @date:2021/8/21
 */
@Service("orderB2BAdminPurchaseExportService")
public class OrderB2BAdminPurchaseExportServiceImpl implements BaseExportQueryDataService<QueryOrderPageRequest> {

    /*private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("orderNo", "订单编号");
        FIELD.put("buyerEid", "采购商ID");
        FIELD.put("buyerEname", "采购商名称");
        FIELD.put("provinceName", "采购商所在省");
        FIELD.put("cityName", "采购商所在市");
        FIELD.put("regionName", "采购商所在区");
        FIELD.put("createTime", "下单时间");
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

    }*/

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderAddressApi orderAddressApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    /**
     * 查询excel中的数据
     *
     * @param queryOrderPageRequest
     * @return
     */
    public List<ExportB2BOrderPurchaseModule> getResult(QueryOrderPageRequest queryOrderPageRequest) {


        Page<OrderDTO> page = orderApi.getB2BPurchaseOrderPage(queryOrderPageRequest);

        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return ListUtil.empty();
        }
        List<ExportB2BOrderPurchaseModule> result = new ArrayList<>();

        List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());
        //Map<Long, OrderDTO> orderMap = page.getRecords().stream().collect(Collectors.toMap(OrderDTO::getId, o -> o, (k1, k2) -> k1));

        List<Long> eidList = page.getRecords().stream().map(order -> order.getBuyerEid()).collect(Collectors.toList());

        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eidList);
        Map<Long, EnterpriseDTO> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, o -> o, (k1, k2) -> k1));

        List<OrderAddressDTO> orderAddressList = orderAddressApi.getOrderAddressList(ids);
        Map<Long, OrderAddressDTO> addressMap = orderAddressList.stream().collect(Collectors.toMap(OrderAddressDTO::getOrderId, o -> o, (k1, k2) -> k1));

        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(ids);
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
        for (OrderDTO one : page.getRecords()) {
            EnterpriseDTO enterpriseDTO = enterpriseMap.get(one.getBuyerEid());
            OrderAddressDTO addressOne = addressMap.get(one.getId());

            BigDecimal deliveryAmount = BigDecimal.ZERO;
            BigDecimal discountAmount = BigDecimal.ZERO;
            List<OrderDetailChangeDTO> changeList = mapList.get(one.getId());
            for (OrderDetailChangeDTO changeDTO : changeList) {
                deliveryAmount = deliveryAmount.add(changeDTO.getGoodsAmount().subtract(changeDTO.getSellerReturnAmount()));
                discountAmount = discountAmount.add(changeDTO.getCouponDiscountAmount().add(changeDTO.getPlatformCouponDiscountAmount()).add(changeDTO.getShopPaymentDiscountAmount()).add(changeDTO.getPlatformPaymentDiscountAmount()).add(changeDTO.getCashDiscountAmount()).add(changeDTO.getTicketDiscountAmount()));
            }
            ExportOrderB2BAdminBO orderB2BAdmin = new ExportOrderB2BAdminBO();
            orderB2BAdmin.setOrderNo(one.getOrderNo());
            orderB2BAdmin.setBuyerEid(one.getBuyerEid());
            orderB2BAdmin.setBuyerEname(one.getBuyerEname());
            orderB2BAdmin.setProvinceName(StringUtils.isNotEmpty(enterpriseDTO.getProvinceName()) ? enterpriseDTO.getProvinceName() : "");
            orderB2BAdmin.setCityName(StringUtils.isNotEmpty(enterpriseDTO.getCityName()) ? enterpriseDTO.getCityName() : "");
            orderB2BAdmin.setRegionName(StringUtils.isNotEmpty(enterpriseDTO.getRegionName()) ? enterpriseDTO.getRegionName() : "");
            orderB2BAdmin.setCreateTime(one.getCreateTime());
            orderB2BAdmin.setOrderStatusName(OrderStatusEnum.getByCode(one.getOrderStatus()) != null ? OrderStatusEnum.getByCode(one.getOrderStatus()).getName() : "---");
            orderB2BAdmin.setPaymentMethodName(PaymentMethodEnum.getByCode(Long.valueOf(one.getPaymentMethod())) != null ? PaymentMethodEnum.getByCode(Long.valueOf(one.getPaymentMethod())).getName() : "---");
            orderB2BAdmin.setPaymentStatusName(PaymentStatusEnum.getByCode(one.getPaymentStatus()) != null ? PaymentStatusEnum.getByCode(one.getPaymentStatus()).getName() : "---");
            orderB2BAdmin.setTotalAmount(one.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
            orderB2BAdmin.setDeliveryAmount(deliveryAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            orderB2BAdmin.setDiscountAmount(discountAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
            orderB2BAdmin.setPayAmount(one.getTotalAmount().subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
            orderB2BAdmin.setMobile(StringUtils.isNotEmpty(addressOne.getMobile()) ? addressOne.getMobile() : " ");
            orderB2BAdmin.setReceiveName(StringUtils.isNotEmpty(addressOne.getName()) ? addressOne.getName() : " ");
            orderB2BAdmin.setAddress(StringUtils.isNotEmpty(addressOne.getAddress()) ? addressOne.getAddress() : " ");

            //Map<String, Object> dataPojo = BeanUtil.beanToMap(orderB2BAdmin);
            ExportB2BOrderPurchaseModule dataPojo = PojoUtils.map(orderB2BAdmin, ExportB2BOrderPurchaseModule.class);
            result.add(dataPojo);
        }

        //exportDataDTO.setSheetName("B2B采购订单列表");
        return result;
    }


    @Override
    public QueryExportDataDTO queryData(QueryOrderPageRequest request) {
        return null;
    }

    @Override
    public QueryOrderPageRequest getParam(Map<String, Object> map) {
        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        QueryOrderPageRequest request = PojoUtils.map(map, QueryOrderPageRequest.class);
        request.setType(2);
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
                writeSheet = EasyExcel.writerSheet(fileIdx, "B2B采购订单列表" + sheetNumber).head(ExportB2BOrderPurchaseModule.class).build();
                while (true) {
                    request.setCurrent(current);
                    request.setSize(size);

                    List<ExportB2BOrderPurchaseModule> list = getResult(request);
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
