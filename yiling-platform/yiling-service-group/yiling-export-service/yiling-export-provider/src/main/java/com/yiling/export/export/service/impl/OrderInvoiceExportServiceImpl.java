package com.yiling.export.export.service.impl;

import java.io.File;
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

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportOrderInvoiceBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.model.ExportOrderInvoiceModule;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderInvoiceApi;
import com.yiling.order.order.api.OrderInvoiceApplyApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderInvoiceApplyDTO;
import com.yiling.order.order.dto.OrderInvoiceDTO;
import com.yiling.order.order.dto.request.QueryInvoicePageRequest;
import com.yiling.order.order.enums.OrderInvoiceApplyStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 票折管理导出
 *
 * @author:wei.wang
 * @date:2021/8/23
 */
@Service("orderInvoiceExportService")
@Slf4j
public class OrderInvoiceExportServiceImpl implements BaseExportQueryDataService<QueryInvoicePageRequest> {

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    OrderInvoiceApplyApi orderInvoiceApplyApi;
    @DubboReference
    OrderInvoiceApi orderInvoiceApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    DataPermissionsApi dataPermissionsApi;
    @DubboReference
    DepartmentApi departmentApi;


    /*private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("orderId", "订单ID");
        FIELD.put("orderNo", "订单编号");
        FIELD.put("createTime", "下单时间");
        FIELD.put("paymentMethodName", "支付方式");
        FIELD.put("deliveryAmount", "实际发货货款总金额");
        FIELD.put("discountAmount", "折扣总金额");
        FIELD.put("invoiceAllAmount", "发票总金额");
        FIELD.put("invoiceStatusName", "发票状态");
        FIELD.put("invoiceApplyUserId", "发票申请人ID");
        FIELD.put("invoiceApplyUserName", "发票申请人姓名");
        FIELD.put("invoiceApplyTime", "发票申请时间");
        FIELD.put("invoiceNumber", "发票张数");
        FIELD.put("invoiceNo", "发票单号");
        FIELD.put("invoiceAmount", "发票金额");
        FIELD.put("buyerEid", "采购商ID");
        FIELD.put("buyerEname", "采购商名称");
        FIELD.put("sellerEid", "供应商ID");
        FIELD.put("sellerEname", "供应商名称");


    }*/

    /**
     * 查询excel中的数据
     *
     * @param queryInvoicePageRequest
     * @return
     */
    public List<ExportOrderInvoiceModule> getResult(QueryInvoicePageRequest queryInvoicePageRequest) {

        Page<OrderDTO> page = orderApi.getOrderInvoiceInfoPage(queryInvoicePageRequest);

        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return ListUtil.empty();
        }
        List<ExportOrderInvoiceModule> result = new ArrayList<>();
        List<Long> ids = page.getRecords().stream().map(order -> order.getId()).collect(Collectors.toList());

        List<OrderInvoiceApplyDTO> orderInvoiceApplyList = orderInvoiceApplyApi.getOrderInvoiceApplyByList(ids);
        Map<Long, OrderInvoiceApplyDTO> applyDTOMap = orderInvoiceApplyList.stream().collect(Collectors.toMap(OrderInvoiceApplyDTO::getOrderId, o -> o, (k1, k2) -> k1));

        Map<Long, OrderInvoiceApplyDTO> applyIdMap = orderInvoiceApplyList.stream().collect(Collectors.toMap(OrderInvoiceApplyDTO::getId, o -> o, (k1, k2) -> k1));


        List<Long> applyIds = orderInvoiceApplyList.stream().map(order -> order.getId()).collect(Collectors.toList());
        List<Long> userIds = orderInvoiceApplyList.stream().map(order -> order.getCreateUser()).collect(Collectors.toList());
        Map<Long, String> mapUser = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<UserDTO> userList = userApi.listByIds(userIds);
            mapUser = userList.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName, (k1, k2) -> k1));
        }

        Map<Long, List<OrderInvoiceDTO>> orderInvoiceMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(applyIds)) {
            List<OrderInvoiceDTO> orderInvoiceList = orderInvoiceApi.getOrderInvoiceByApplyIdList(applyIds);
            for (OrderInvoiceDTO one : orderInvoiceList) {
                if (orderInvoiceMap.containsKey(one.getOrderId())) {
                    List<OrderInvoiceDTO> orderInvoiceDTOS = orderInvoiceMap.get(one.getOrderId());
                    orderInvoiceDTOS.add(one);
                } else {
                    orderInvoiceMap.put(one.getOrderId(), new ArrayList<OrderInvoiceDTO>() {{
                        add(one);
                    }});
                }
            }
        }


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

        for (OrderDTO orderOne : page.getRecords()) {
            OrderInvoiceApplyDTO applyDTO = applyDTOMap.get(orderOne.getId());
            List<OrderInvoiceDTO> orderInvoiceDTOS = orderInvoiceMap.get(orderOne.getId());

            BigDecimal deliveryAmount = BigDecimal.ZERO;
            BigDecimal discountAmount = BigDecimal.ZERO;
            List<OrderDetailChangeDTO> changeList = mapList.get(orderOne.getId());
            for (OrderDetailChangeDTO changeDTO : changeList) {
                deliveryAmount = deliveryAmount.add(changeDTO.getGoodsAmount().subtract(changeDTO.getSellerReturnAmount()));
                discountAmount = discountAmount.add(changeDTO.getCashDiscountAmount().add(changeDTO.getTicketDiscountAmount()).subtract(changeDTO.getSellerReturnCashDiscountAmount()).subtract(changeDTO.getSellerReturnTicketDiscountAmount()));
            }

            if (CollectionUtils.isNotEmpty(orderInvoiceDTOS) && applyDTO != null) {
                for (OrderInvoiceDTO invoiceOne : orderInvoiceDTOS) {
                    ExportOrderInvoiceBO invoice = getExportOrderInvoiceBO(orderInvoiceMap, orderOne, deliveryAmount, discountAmount);
                    if (orderOne.getInvoiceStatus().compareTo(0) != 0) {
                        invoice.setInvoiceStatusName(OrderInvoiceApplyStatusEnum.getByCode(orderOne.getInvoiceStatus()) != null ? OrderInvoiceApplyStatusEnum.getByCode(orderOne.getInvoiceStatus()).getName() : "---");
                    } else {
                        invoice.setInvoiceStatusName(OrderInvoiceApplyStatusEnum.PENDING_APPLY.getName());
                    }
                    OrderInvoiceApplyDTO orderInvoiceApplyDTO = applyIdMap.get(invoiceOne.getApplyId());

                    invoice.setInvoiceApplyUserId(orderInvoiceApplyDTO.getCreateUser());
                    invoice.setInvoiceApplyUserName(mapUser.get(orderInvoiceApplyDTO.getCreateUser()));
                    invoice.setInvoiceApplyTime(orderInvoiceApplyDTO.getCreateTime());
                    invoice.setInvoiceNo(invoiceOne.getInvoiceNo());
                    invoice.setInvoiceAmount(invoiceOne.getInvoiceAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                    ExportOrderInvoiceModule dataPojo = PojoUtils.map(invoice, ExportOrderInvoiceModule.class);
                    //Map<String, Object> dataPojo = BeanUtil.beanToMap(invoice);
                    result.add(dataPojo);
                }
            } else {
                ExportOrderInvoiceBO invoice = getExportOrderInvoiceBO(orderInvoiceMap, orderOne, deliveryAmount, discountAmount);

                if (applyDTO != null) {
                    invoice.setInvoiceApplyUserId(applyDTO.getCreateUser());
                    invoice.setInvoiceApplyUserName(mapUser.get(applyDTO.getCreateUser()));
                    invoice.setInvoiceApplyTime(orderOne.getInvoiceTime());
                }
                if (orderOne.getInvoiceStatus().compareTo(0) != 0) {
                    invoice.setInvoiceStatusName(OrderInvoiceApplyStatusEnum.getByCode(orderOne.getInvoiceStatus()) != null ? OrderInvoiceApplyStatusEnum.getByCode(orderOne.getInvoiceStatus()).getName() : "---");
                } else {
                    invoice.setInvoiceStatusName(OrderInvoiceApplyStatusEnum.PENDING_APPLY.getName());
                }
                ExportOrderInvoiceModule dataPojo = PojoUtils.map(invoice, ExportOrderInvoiceModule.class);
                //Map<String, Object> dataPojo = BeanUtil.beanToMap(invoice);
                result.add(dataPojo);

            }
        }


        //aexportDataDTO.setSheetName("发票管理列表导出");
        // 页签字段
        //exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        //exportDataDTO.setData(data);
        return result;
    }

    private ExportOrderInvoiceBO getExportOrderInvoiceBO(Map<Long, List<OrderInvoiceDTO>> orderInvoiceMap, OrderDTO orderOne, BigDecimal deliveryAmount, BigDecimal discountAmount) {
        ExportOrderInvoiceBO invoice = new ExportOrderInvoiceBO();
        invoice.setOrderId(orderOne.getId());
        invoice.setOrderNo(orderOne.getOrderNo());
        invoice.setCreateTime(orderOne.getCreateTime());
        invoice.setPaymentMethodName(PaymentMethodEnum.getByCode(Long.valueOf(orderOne.getPaymentMethod())) != null ? PaymentMethodEnum.getByCode(Long.valueOf(orderOne.getPaymentMethod())).getName() : "---");

        invoice.setDeliveryAmount(deliveryAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        invoice.setDiscountAmount(discountAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        invoice.setInvoiceAllAmount(deliveryAmount.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP));
        invoice.setBuyerEid(orderOne.getBuyerEid());
        invoice.setBuyerEname(orderOne.getBuyerEname());
        invoice.setSellerEid(orderOne.getSellerEid());
        invoice.setSellerEname(orderOne.getSellerEname());
        invoice.setInvoiceNumber(orderInvoiceMap.get(orderOne.getId()) != null ? orderInvoiceMap.get(orderOne.getId()).size() : 0);
        return invoice;
    }

    @Override
    public QueryExportDataDTO queryData(QueryInvoicePageRequest queryInvoicePageRequest) {
        return null;
    }

    @Override
    public QueryInvoicePageRequest getParam(Map<String, Object> map) {
        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        Long currentUserId = Long.parseLong(map.getOrDefault("currentUserId", 0L).toString());

        QueryInvoicePageRequest request = PojoUtils.map(map, QueryInvoicePageRequest.class);

        List<Long> eidList = new ArrayList<>();

        if (Constants.YILING_EID.equals(eid)) {
            List<Long> ids = enterpriseApi.listSubEids(Constants.YILING_EID);
            List<Long> contacterIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, eid, currentUserId);
            request.setContacterIdList(contacterIdList);
            if (request.getDistributorEid() == null || request.getDistributorEid() == 0) {
                eidList.addAll(ids);
            } else {
                eidList.addAll(new ArrayList<Long>() {{
                    request.getDistributorEid();
                }});
            }

        } else {
            if (request.getDistributorEid() == null || request.getDistributorEid() == 0) {
                eidList.add(eid);
                EnterpriseDepartmentDTO enterpriseDepartment = null;
                if (request.getDepartmentType() != null) {
                    if (request.getDepartmentType() == 3) {
                        //大运河数拓部门
                        enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_ST_DEPARTMENT_CODE);
                    } else if (request.getDepartmentType() == 4) {
                        //大运河分销部门
                        enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_FX_DEPARTMENT_CODE);
                    }
                }

                if (enterpriseDepartment != null) {
                    request.setDepartmentId(enterpriseDepartment.getId());
                }
            } else {
                eidList.add(0L);
            }
        }
        request.setEidLists(eidList);
        request.setOrderType(OrderTypeEnum.POP.getCode());
        return request;
    }

    @Override
    public boolean isReturnData() {
        return false;
    }

    @Override
    public byte[] getExportByte(QueryInvoicePageRequest request, String fileName) {
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
                writeSheet = EasyExcel.writerSheet(fileIdx, "发票管理列表导出" + sheetNumber).head(ExportOrderInvoiceModule.class).build();
                while (true) {
                    request.setCurrent(current);
                    request.setSize(size);

                    List<ExportOrderInvoiceModule> list = getResult(request);
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
