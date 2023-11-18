package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.yiling.framework.common.util.Constants;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.enums.PermissionAppEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderExportApi;
import com.yiling.order.order.dto.OrderExpectExportDTO;
import com.yiling.order.order.dto.request.QueryOrderExpectPageRequest;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;

import cn.hutool.core.bean.BeanUtil;

/**
 * 预订单列表导出
 *
 * @author:wei.wang
 * @date:2021/8/20
 */
@Service("orderExpectExportService")
public class OrderExpectExportServiceImpl<dataPermissionsApi> implements BaseExportQueryDataService<QueryOrderExpectPageRequest> {
    @DubboReference
    OrderExportApi     orderExportApi;
    @DubboReference
    DataPermissionsApi dataPermissionsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("orderNo", "订单编号");
        FIELD.put("buyerEid", "采购商ID");
        FIELD.put("buyerEname", "采购商名称");
        FIELD.put("contractNumber", "合同编号");
        FIELD.put("createTime", "下单时间");
        FIELD.put("auditStatusName", "审核状态");
        FIELD.put("auditUser", "审核人ID");
        FIELD.put("auditName", "审核人姓名");
        FIELD.put("auditTime", "审核时间");
        FIELD.put("paymentMethodName", "支付方式");
        FIELD.put("totalAmount", "订单总金额");
        FIELD.put("discountAmount", "折扣总金额");
        FIELD.put("payAmount", "支付总金额");
        FIELD.put("goodsId", "商品ID");
        FIELD.put("goodsName", "商品名称");
        FIELD.put("goodsSpecification", "规格型号");
        FIELD.put("goodsLicenseNo", "批准文号");
        FIELD.put("goodsQuantity", "购买数量");
        FIELD.put("goodsPrice", "商品单价");
        FIELD.put("goodsAmount", "金额小计");
        FIELD.put("goodsDiscountRate", "折扣比率");
        FIELD.put("goodsDiscountAmount", "折扣金额");
        FIELD.put("goodsPayAmount", "支付金额");
        FIELD.put("mobile", "收货人联系电话");
        FIELD.put("receiveName", "收货人姓名");
        FIELD.put("address", "收货人详细地址");
        FIELD.put("contacterId", "商务负责人ID");
        FIELD.put("contacterName", "商务负责人姓名");
        FIELD.put("sellerEid", "供应商ID");
        FIELD.put("sellerEname", "供应商名称");


    }

    /**
     * 查询excel中的数据
     *
     * @param queryOrderExpectPageRequest
     * @return
     */
    @Override
    public QueryExportDataDTO queryData(QueryOrderExpectPageRequest queryOrderExpectPageRequest) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        List<OrderExpectExportDTO> orderList = orderExportApi.orderExpectExport(queryOrderExpectPageRequest);
        for (OrderExpectExportDTO one : orderList) {
            one.setAuditStatusName(OrderAuditStatusEnum.getByCode(one.getAuditStatus()).getName());
            if (one.getPaymentMethod() == 0) {
                one.setPaymentMethodName("---");
            } else {
                one.setPaymentMethodName(PaymentMethodEnum.getByCode(one.getPaymentMethod()).getName());
            }
            data.add(BeanUtil.beanToMap(one));
        }
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("预订单列表导出");
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
    public QueryOrderExpectPageRequest getParam(Map<String, Object> map) {
        //参数eid的转换为eidList
        QueryOrderExpectPageRequest request = PojoUtils.map(map, QueryOrderExpectPageRequest.class);
        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        Long currentUserId = Long.parseLong(map.getOrDefault("currentUserId", 0L).toString());

        request.setOrderType(OrderTypeEnum.POP.getCode());
        List<Long> sellerEidList = new ArrayList<>();
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eid);

        if (Constants.YILING_EID.equals(eid) ) {
            //以岭人员
            List<Long> subEidLists = enterpriseApi.listSubEids(Constants.YILING_EID);
            sellerEidList.addAll(subEidLists);

            List<Long> contacterIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, eid, currentUserId);
            request.setContacterIdList(contacterIdList);
        }else if(EnterpriseChannelEnum.INDUSTRY_DIRECT == EnterpriseChannelEnum.getByCode(enterpriseDTO.getChannelId())){
            //工业直属人员
            sellerEidList.add(eid);
            List<Long> contacterIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, eid, currentUserId);
            request.setContacterIdList(contacterIdList);
        }else{
            //一二级商
            request.setBuyerEid(eid);
        }

        request.setSellerEidList(sellerEidList);
        return request;
    }
}
