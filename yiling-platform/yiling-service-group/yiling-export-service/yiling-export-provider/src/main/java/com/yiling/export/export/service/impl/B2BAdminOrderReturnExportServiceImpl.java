package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportReturnB2BAdminBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderReturnApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.request.OrderReturnPageListRequest;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.ReturnSourceEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 退货单导出-商家端
 *
 * @author: yong.zhang
 * @date: 2021/11/11
 */
@Slf4j
@Service("b2BAdminOrderReturnExportService")
public class B2BAdminOrderReturnExportServiceImpl implements BaseExportQueryDataService<OrderReturnPageListRequest> {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    DataPermissionsApi dataPermissionsApi;
    @DubboReference
    OrderReturnApi orderReturnApi;
    @DubboReference
    OrderApi orderApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("returnNo", "退货单单据编号");
        FIELD.put("orderNo", "订单编号");
        FIELD.put("buyerEid", "采购商ID");
        FIELD.put("buyerEname", "采购商名称");
        FIELD.put("buyerProvince", "采购商所在省");
        FIELD.put("buyerCity", "采购商所在市");
        FIELD.put("buyerRegion", "采购商所在区");
        FIELD.put("returnType", "退货单单据类型");
        FIELD.put("returnStatus", "退货单单据状态");
        FIELD.put("createdTime", "单据提交时间");
        FIELD.put("paymentMethod", "支付方式");
        FIELD.put("returnAmount", "退货总金额");
        FIELD.put("discountAmount", "优惠总金额");
        FIELD.put("realReturnAmount", "实退总金额");
    }

    @Override
    public QueryExportDataDTO queryData(OrderReturnPageListRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<OrderReturnDTO> page;
        int current = 1;
        log.info("【退货单导出】商家后台退货单导出，请求数据为:[{}]", request);
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
            List<OrderDTO> orderList = orderApi.listByIds(orderIdList);
            Map<Long, OrderDTO> orderMap = orderList.stream().collect(Collectors.toMap(OrderDTO::getId, e -> e));
            for (OrderReturnDTO orderReturnDTO : returnDTOList) {
                OrderDTO orderDTO = orderMap.get(orderReturnDTO.getOrderId());
                ExportReturnB2BAdminBO b2BAdminBO = new ExportReturnB2BAdminBO();
                b2BAdminBO.setReturnNo(orderReturnDTO.getReturnNo());
                b2BAdminBO.setOrderNo(orderReturnDTO.getOrderNo());
                b2BAdminBO.setBuyerEid(orderReturnDTO.getBuyerEid());
                b2BAdminBO.setBuyerEname(orderReturnDTO.getBuyerEname());
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(orderReturnDTO.getBuyerEid());
                b2BAdminBO.setBuyerProvince(enterpriseDTO.getProvinceName());
                b2BAdminBO.setBuyerCity(enterpriseDTO.getCityName());
                b2BAdminBO.setBuyerRegion(enterpriseDTO.getRegionName());
                b2BAdminBO.setReturnType(getReturnType(orderReturnDTO.getReturnType()));
                b2BAdminBO.setReturnStatus(getReturnStatus(orderReturnDTO.getReturnStatus()));
                b2BAdminBO.setCreatedTime(DateUtil.format(orderReturnDTO.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                b2BAdminBO.setPaymentMethod(getPayMethod(orderDTO.getPaymentMethod()));
                b2BAdminBO.setDiscountAmount(orderReturnDTO.getPlatformCouponDiscountAmount()
                        .add(orderReturnDTO.getCouponDiscountAmount())
                        .add(orderReturnDTO.getCashDiscountAmount())
                        .add(orderReturnDTO.getShopPaymentDiscountAmount())
                        .add(orderReturnDTO.getPlatformPaymentDiscountAmount())
                        .add(orderReturnDTO.getTicketDiscountAmount()));
                b2BAdminBO.setReturnAmount(orderReturnDTO.getReturnAmount());
                b2BAdminBO.setRealReturnAmount(orderReturnDTO.getReturnAmount().subtract(b2BAdminBO.getDiscountAmount()));
                Map<String, Object> dataPojo = BeanUtil.beanToMap(b2BAdminBO);
                data.add(dataPojo);
            }
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
        ExportDataDTO exportDataDTO = new ExportDataDTO();

        exportDataDTO.setSheetName("B2B退货单列表导出");
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
}
