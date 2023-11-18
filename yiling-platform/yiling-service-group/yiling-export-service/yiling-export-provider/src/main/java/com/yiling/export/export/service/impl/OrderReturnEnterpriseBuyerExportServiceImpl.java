package com.yiling.export.export.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.ExportOrderReturnEnterpriseBuyerCenterModel;
import com.yiling.export.export.model.ExportOrderReturnEnterpriseDetailSellCenterModule;
import com.yiling.export.export.model.ExportOrderReturnEnterpriseSellCenterModel;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.api.OrderReturnDetailApi;
import com.yiling.order.order.api.OrderReturnDetailBatchApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.OrderReturnDetailBatchDTO;
import com.yiling.order.order.dto.OrderReturnDetailDTO;
import com.yiling.order.order.dto.request.OrderReturnPageListRequest;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wang.wei
 * @date 2023/4/06
 */
@Slf4j
@Service("orderReturnEnterpriseBuyerExportService")
public class OrderReturnEnterpriseBuyerExportServiceImpl implements BaseExportQueryDataService<OrderReturnPageListRequest> {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    DataPermissionsApi dataPermissionsApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderReturnDetailApi orderReturnDetailApi;
    @DubboReference
    OrderReturnApi orderReturnApi;
    @DubboReference
    OrderReturnDetailBatchApi orderReturnDetailBatchApi;
    @DubboReference
    OrderDetailApi orderDetailApi;

    @Override
    public QueryExportDataDTO queryData(OrderReturnPageListRequest request) {
        return null;
    }

    @Override
    public OrderReturnPageListRequest getParam(Map<String, Object> map) {

        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        Long currentUserId = Long.parseLong(map.getOrDefault("currentUserId", 0L).toString());

        OrderReturnPageListRequest request = PojoUtils.map(map, OrderReturnPageListRequest.class);
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
        request.setBuyerEidList(list);
        String name = map.getOrDefault("name", "").toString();
        request.setSellerEname(name);


        return request;
    }

    @Override
    public boolean isReturnData() {
        return false;
    }

    @Override
    public byte[] getExportByte(OrderReturnPageListRequest request, String fileName) {
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
            for (int i = 0; i < 2; i++) {
                int current = 1;
                int size = 1000;
                int sheetNumber = 1;
                outer:
                do {
                    WriteSheet writeSheet = null;
                    if (i == 0) {
                        writeSheet = EasyExcel.writerSheet(fileIdx, "企业销售退货单信息" + sheetNumber).head(ExportOrderReturnEnterpriseBuyerCenterModel.class).build();
                    } else if (i == 1) {
                        writeSheet = EasyExcel.writerSheet(fileIdx, "企业销售退货单明细信息" + sheetNumber).head(ExportOrderReturnEnterpriseDetailSellCenterModule.class).build();
                    }
                    while (true) {
                        request.setCurrent(current);
                        request.setSize(size);
                        if (i == 0) {
                            List<ExportOrderReturnEnterpriseBuyerCenterModel> list = getOrderEnterpriseSell(request);
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

                            List<ExportOrderReturnEnterpriseDetailSellCenterModule> list = getOrderEnterpriseSellDetail(request);
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

    private List<ExportOrderReturnEnterpriseBuyerCenterModel> getOrderEnterpriseSell(OrderReturnPageListRequest request) {
        Page<OrderReturnDTO> page = orderReturnApi.pageList(request);
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return ListUtil.empty();
        }
        List<ExportOrderReturnEnterpriseBuyerCenterModel> result = new ArrayList<>();
        List<OrderReturnDTO> returnDTOList = page.getRecords();
        List<Long> orderIdList = returnDTOList.stream().map(OrderReturnDTO::getOrderId).collect(Collectors.toList());
        List<OrderDTO> orderList = orderApi.listByIds(orderIdList);
        Map<Long, OrderDTO> orderMap = orderList.stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));
        for (OrderReturnDTO orderReturnDTO : returnDTOList) {
            OrderDTO orderDTO = orderMap.get(orderReturnDTO.getOrderId());
            ExportOrderReturnEnterpriseBuyerCenterModel b2BCenterBO = new ExportOrderReturnEnterpriseBuyerCenterModel();
            b2BCenterBO.setReturnNo(orderReturnDTO.getReturnNo());
            b2BCenterBO.setOrderNo(orderReturnDTO.getOrderNo());
            b2BCenterBO.setBuyerEid(orderReturnDTO.getBuyerEid());
            b2BCenterBO.setBuyerEname(orderReturnDTO.getBuyerEname());
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(orderReturnDTO.getSellerEid());
            b2BCenterBO.setBuyerProvince(enterpriseDTO.getProvinceName());
            b2BCenterBO.setBuyerCity(enterpriseDTO.getCityName());
            b2BCenterBO.setBuyerRegion(enterpriseDTO.getRegionName());
            b2BCenterBO.setSellerEid(orderReturnDTO.getSellerEid());
            b2BCenterBO.setSellerEname(orderReturnDTO.getSellerEname());
            b2BCenterBO.setReturnType(getReturnType(orderReturnDTO.getReturnType()));
            b2BCenterBO.setReturnStatus(getReturnStatus(orderReturnDTO.getReturnStatus()));
            b2BCenterBO.setCreatedTime(DateUtil.format(orderReturnDTO.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            b2BCenterBO.setPaymentMethod(getPayMethod(orderDTO.getPaymentMethod()));
            b2BCenterBO.setReturnAmount(orderReturnDTO.getReturnAmount());
            b2BCenterBO.setDiscountAmount(orderReturnDTO.getPlatformCouponDiscountAmount().add(orderReturnDTO.getCouponDiscountAmount()).add(orderReturnDTO.getCashDiscountAmount()).add(orderReturnDTO.getTicketDiscountAmount()));
            b2BCenterBO.setRealReturnAmount(orderReturnDTO.getReturnAmount().subtract(b2BCenterBO.getDiscountAmount()));
            result.add(b2BCenterBO);
        }
        return result;
    }

    private String getReturnType(Integer returnType) {
        // 退货单类型：1-供应商退货单 2-破损退货单 3-采购退货单
        OrderReturnTypeEnum returnTypeEnum = OrderReturnTypeEnum.getByCode(returnType);
        if (null == returnTypeEnum) {
            return "---";
        }
        return returnTypeEnum.getName();
    }

    private String getReturnStatus(Integer returnStatus) {
        // 退货单状态：1-待审核 2-审核通过 3-审核驳回
        OrderReturnStatusEnum returnStatusEnum = OrderReturnStatusEnum.getByCode(returnStatus);
        if (null == returnStatusEnum) {
            return "---";
        }
        return returnStatusEnum.getName();
    }

    private String getPayMethod(Integer paymentMethod) {
        //支付方式：1-线下支付 2-账期 3-预付款
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.getByCode(paymentMethod.longValue());

        if (null == paymentMethodEnum) {
            return "---";
        }
        return paymentMethodEnum.getName();
    }

    private List<ExportOrderReturnEnterpriseDetailSellCenterModule> getOrderEnterpriseSellDetail(OrderReturnPageListRequest request) {
        Page<OrderReturnDTO> page = orderReturnApi.pageList(request);
        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return ListUtil.empty();
        }
        List<ExportOrderReturnEnterpriseDetailSellCenterModule> result = new ArrayList<>();
        List<OrderReturnDTO> returnDTOList = page.getRecords();
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
            ExportOrderReturnEnterpriseDetailSellCenterModule b2BCenterBO = new ExportOrderReturnEnterpriseDetailSellCenterModule();

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
            BigDecimal discountAmount = returnDetailDTO.getReturnPlatformCouponDiscountAmount().add(returnDetailDTO.getReturnCouponDiscountAmount()).add(returnDetailDTO.getReturnCashDiscountAmount()).add(returnDetailDTO.getReturnTicketDiscountAmount()).multiply(BigDecimal.valueOf(batchDTO.getReturnQuantity())).divide(BigDecimal.valueOf(returnDetailDTO.getReturnQuantity()), 2, BigDecimal.ROUND_HALF_UP);
            b2BCenterBO.setGoodsReturnAmount(goodsReturnAmount);
            b2BCenterBO.setGoodsDiscountAmount(discountAmount);
            b2BCenterBO.setReturnAmount(goodsReturnAmount.subtract(discountAmount));
            result.add(b2BCenterBO);
        }
        return result;
    }

    private String getExpiredDate(Date expireDate, Integer returnType) {
        OrderReturnTypeEnum returnTypeEnum = OrderReturnTypeEnum.getByCode(returnType);
        if (null == returnTypeEnum || returnTypeEnum == OrderReturnTypeEnum.SELLER_RETURN_ORDER) {
            return "---";
        }
        return DateUtil.format(expireDate, "yyyy-MM-dd HH:mm:ss");
    }

}
