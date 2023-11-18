package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportReturnPOPBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
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
import com.yiling.order.order.dto.request.QueryOrderReturnInfoRequest;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.ReturnSourceEnum;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 退货单信息导出
 *
 * @author: yong.zhang
 * @date: 2021/8/19
 */
@Slf4j
@Service("orderReturnExportService")
public class OrderReturnExportServiceImpl implements BaseExportQueryDataService<QueryOrderReturnInfoRequest> {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    OrderReturnDetailBatchApi orderReturnDetailBatchApi;
    @DubboReference
    OrderReturnDetailApi orderReturnDetailApi;
    @DubboReference
    DataPermissionsApi dataPermissionsApi;
    @DubboReference
    OrderReturnApi orderReturnApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    DepartmentApi departmentApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("returnNo", "退货单单据编号");
        FIELD.put("orderNo", "订单编号");
        FIELD.put("returnType", "退货单单据类型");
        FIELD.put("returnStatus", "退货单单据状态");
        FIELD.put("createdTime", "单据提交时间");
        FIELD.put("paymentMethod", "支付方式");
        FIELD.put("totalReturnAmount", "退款总金额(元)");

        FIELD.put("goodsId", "商品ID");// 请注意：列H  至  列R 显示的是商品的信息；如果该订单有多个商品则有多行
        FIELD.put("goodsName", "商品名称");
        FIELD.put("goodsSpecification", "规格型号");
        FIELD.put("goodsReturnQuality", "退货数量");
        FIELD.put("goodsPrice", "商品单价");
        FIELD.put("goodsReturnAmount", "退货金额");// 请注意：显示是该商品的退货商品的 退货金额(M)= 退货数量(K)*商品单价(L)
        FIELD.put("goodsDiscountAmount", "折扣金额");// 请注意：显示的是该商品的退货商品的 折扣金额
        FIELD.put("returnAmounts", "退款金额");// 退款金额(O)=退货金额(M) - 折扣金额(N)

        FIELD.put("batchNo", "批次号/序列号");// 请注意：列P 至 列R 显示的是该商品的某个批次号的 退货信息；如果该商品退了多个批次号则有多行
        FIELD.put("expiredDate", "有效期至");
        FIELD.put("returnQuality", "批次退货数量");
    }

    @Override
    public QueryExportDataDTO queryData(QueryOrderReturnInfoRequest request) {
        log.info("POP退货单导出，请求数据为:[{}]", JSON.toJSONString(request));
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<OrderReturnDTO> page;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            OrderReturnPageListRequest returnInfoRequest = PojoUtils.map(request, OrderReturnPageListRequest.class);
            returnInfoRequest.setReturnSourceEnum(ReturnSourceEnum.POP_PC);
            returnInfoRequest.setOrderId(request.getOrderId());
            // 查询导出来源:1.采购商退货单，2.销售商退货单，3.运营端退货单
            if (1 == request.getQueryType()) {
                FIELD.put("contractNumber", "合同编号");
                List<Long> buyerEidList = new ArrayList<Long>() {{
                    add(request.getEid());
                }};
                returnInfoRequest.setBuyerEidList(buyerEidList);
                page = orderReturnApi.pageList(returnInfoRequest);
            } else if (2 == request.getQueryType()) {
                FIELD.put("contractNumber", "合同编号");
                EnterpriseDepartmentDTO enterpriseDepartment = null;
                if(request.getDepartmentType() != null){
                    if (request.getDepartmentType() == 3){
                        //大运河数拓部门
                        enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_ST_DEPARTMENT_CODE);
                    }else if(request.getDepartmentType() == 4){
                        //大运河分销部门
                        enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_FX_DEPARTMENT_CODE);
                    }
                }

                if(enterpriseDepartment != null){
                    returnInfoRequest.setDepartmentId(enterpriseDepartment.getId());
                }
                returnInfoRequest.setSellerEidList(request.getEidList());
                returnInfoRequest.setOrderTypeEnum(OrderTypeEnum.POP);
                page = orderReturnApi.pageList(returnInfoRequest);
            } else {
                page = orderReturnApi.pageList(returnInfoRequest);
            }
            if (CollectionUtils.isNotEmpty(page.getRecords())) {

                for (OrderReturnDTO orderReturnDTO : page.getRecords()) {
                    List<OrderReturnDetailBatchDTO> detailBatchDTOList = orderReturnDetailBatchApi.getOrderReturnDetailBatch(orderReturnDTO.getId(), null, null);
                    List<OrderReturnDetailDTO> returnDetailDTOList = orderReturnDetailApi.getOrderReturnDetailByReturnId(orderReturnDTO.getId());
                    List<OrderDetailDTO> detailDTOList = orderDetailApi.getOrderDetailInfo(orderReturnDTO.getOrderId());
                    OrderDTO orderDTO = orderApi.getOrderInfo(orderReturnDTO.getOrderId());

                    Map<Long, OrderReturnDetailDTO> returnDetailDTOMap = returnDetailDTOList.stream().collect(Collectors.toMap(OrderReturnDetailDTO::getDetailId, o -> o, (k1, k2) -> k1));
                    Map<Long, OrderDetailDTO> detailDTOMap = detailDTOList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, o -> o, (k1, k2) -> k1));

                    for (OrderReturnDetailBatchDTO batchDTO : detailBatchDTOList) {
                        if (0 == batchDTO.getReturnQuantity()) {
                            continue;
                        }

                        OrderReturnDetailDTO returnDetailDTO = returnDetailDTOMap.get(batchDTO.getDetailId());
                        OrderDetailDTO detailDTO = detailDTOMap.get(batchDTO.getDetailId());
                        ExportReturnPOPBO returnPOPBO = new ExportReturnPOPBO();
                        returnPOPBO.setReturnNo(orderReturnDTO.getReturnNo());
                        returnPOPBO.setOrderNo(orderReturnDTO.getOrderNo());
                        returnPOPBO.setReturnType(getReturnType(orderReturnDTO.getReturnType()));
                        returnPOPBO.setReturnStatus(getReturnStatus(orderReturnDTO.getReturnStatus()));
                        returnPOPBO.setCreatedTime(DateUtil.format(orderReturnDTO.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                        returnPOPBO.setPaymentMethod(getPayMethod(orderDTO.getPaymentMethod()));
                        returnPOPBO.setTotalReturnAmount(orderReturnDTO.getReturnAmount().subtract(orderReturnDTO.getCashDiscountAmount()).subtract(orderReturnDTO.getTicketDiscountAmount()));
                        returnPOPBO.setGoodsId(returnDetailDTO.getGoodsId());
                        returnPOPBO.setGoodsName(detailDTO.getGoodsName());
                        returnPOPBO.setGoodsSpecification(detailDTO.getGoodsSpecification());
                        returnPOPBO.setGoodsReturnQuality(returnDetailDTO.getReturnQuantity());
                        returnPOPBO.setGoodsPrice(detailDTO.getGoodsPrice());
                        returnPOPBO.setGoodsReturnAmount(returnDetailDTO.getReturnAmount());
                        returnPOPBO.setGoodsDiscountAmount(returnDetailDTO.getReturnCashDiscountAmount().add(returnDetailDTO.getReturnTicketDiscountAmount()));
                        returnPOPBO.setReturnAmounts(returnDetailDTO.getReturnAmount().subtract(returnDetailDTO.getReturnCashDiscountAmount()).subtract(returnDetailDTO.getReturnTicketDiscountAmount()));
                        returnPOPBO.setBatchNo(batchDTO.getBatchNo());
                        returnPOPBO.setExpiredDate(getExpiredDate(batchDTO.getExpiryDate(), orderReturnDTO.getReturnType()));
                        returnPOPBO.setReturnQuality(batchDTO.getReturnQuantity());

                        returnPOPBO.setContractNumber(StringUtils.isNotEmpty(orderDTO.getContractNumber()) ? orderDTO.getContractNumber() : "---");
                        Map<String, Object> dataPojo = BeanUtil.beanToMap(returnPOPBO);
                        data.add(dataPojo);
                    }
                }
            }
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("退货单列表导出");
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
    public QueryOrderReturnInfoRequest getParam(Map<String, Object> map) {
        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        Long currentUserId = Long.parseLong(map.getOrDefault("currentUserId", 0L).toString());

        QueryOrderReturnInfoRequest request = PojoUtils.map(map, QueryOrderReturnInfoRequest.class);

        List<Long> eidList = new ArrayList<>();
        if (Constants.YILING_EID.equals(eid)) {
            List<Long> userIds = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, eid, currentUserId);
            request.setUserIdList(userIds);
            // 以岭人员
            eidList = enterpriseApi.listSubEids(Constants.YILING_EID);
        } else {
            //非以岭人员
            eidList.add(eid);

        }
        request.setEidList(eidList);
        request.setEid(eid);
        request.setReturnSource(ReturnSourceEnum.POP_PC.getCode());
        return request;
    }

    private String getExpiredDate(Date expireDate, Integer returnType) {
        OrderReturnTypeEnum returnTypeEnum = OrderReturnTypeEnum.getByCode(returnType);
        if (null == returnTypeEnum || returnTypeEnum == OrderReturnTypeEnum.SELLER_RETURN_ORDER) {
            return "---";
        }
        return DateUtil.format(expireDate, "yyyy-MM-dd HH:mm:ss");
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
        if (null == paymentMethod) {
            return "---";
        }
        //支付方式：1-线下支付 2-账期 3-预付款
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.getByCode(paymentMethod.longValue());

        if (null == paymentMethodEnum) {
            return "---";
        }
        return paymentMethodEnum.getName();
    }
}
