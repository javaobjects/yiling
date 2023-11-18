package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysOrderDTO;
import com.yiling.user.payment.dto.request.QueryExpireDayOrderRequest;
import com.yiling.user.payment.enums.PaymentOrderRepaymentStatusEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;

/**
 * B2B商家后台-账期到期提醒列表导出
 * @author: lun.yu
 * @date: 2021/11/11
 */
@Service("b2bPaymentDaysOrderExportService")
public class B2bPaymentDaysOrderExportServiceImpl implements BaseExportQueryDataService<QueryExpireDayOrderRequest> {

    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    OrderApi orderApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    static {
        FIELD.put("orderNo", "订单编号");
        FIELD.put("customerEid","采购商ID");
        FIELD.put("customerName", "采购商名称");
        FIELD.put("deliveryTime", "发货时间");
        FIELD.put("expirationTime", "应还款日期");
        FIELD.put("usedAmount", "订单金额(元)");
        FIELD.put("returnAmount", "退款金额(元)");
        FIELD.put("repaymentAmount", "已还款金额(元)");
        FIELD.put("needRepaymentAmount", "待还款金额(元)");
        FIELD.put("repaymentStatusName", "还款状态");
    }

    @Override
    public QueryExportDataDTO queryData(QueryExpireDayOrderRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<PaymentDaysOrderDTO> page;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = paymentDaysAccountApi.expireDayOrderPage(request);
            if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }

            List<Long> orderId = page.getRecords().stream().map(PaymentDaysOrderDTO::getOrderId).collect(Collectors.toList());
            Map<Long, Date> deliveryTimeMap = MapUtil.newHashMap();
            if(CollUtil.isNotEmpty(orderId)){
                deliveryTimeMap = orderApi.listByIds(orderId).stream().collect(Collectors.toMap(BaseDTO::getId, OrderDTO::getDeliveryTime, (k1, k2) -> k2));
            }
            Map<Long, Date> finalDeliveryTimeMap = deliveryTimeMap;

            //代还款金额和还款状态需要做额外处理
            page.getRecords().forEach(e-> {
                //待还款金额 = 订单金额 – 已还款金额 - 退款金额
                e.setNeedRepaymentAmount(e.getUsedAmount().subtract(e.getRepaymentAmount()).subtract(e.getReturnAmount()));
                //发货时间
                e.setDeliveryTime(finalDeliveryTimeMap.get(e.getOrderId()));
                e.setRepaymentStatusName(Objects.requireNonNull(PaymentOrderRepaymentStatusEnum.getByCode(e.getRepaymentStatus())).getName());

                String date = "1970-01-01 00:00:00";
                if(e.getDeliveryTime().compareTo(DateUtil.parseDate(date)) == 0){
                    e.setDeliveryTime(null);
                }

            });

            page.getRecords().forEach(paymentDaysOrderDTO -> data.add(BeanUtil.beanToMap(paymentDaysOrderDTO)));
            current = current + 1;

        } while (CollUtil.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("账期到期提醒");
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
    public QueryExpireDayOrderRequest getParam(Map<String, Object> map) {
        //参数eid的转换为eidList
        QueryExpireDayOrderRequest request = PojoUtils.map(map, QueryExpireDayOrderRequest.class);
        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        request.setEidList(ListUtil.toList(eid));

        return request;
    }
}
