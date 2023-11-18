package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysOrderDTO;
import com.yiling.user.payment.dto.request.QueryExpireDayOrderRequest;
import com.yiling.user.payment.enums.PaymentOrderRepaymentStatusEnum;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.bo.Admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * 账期到期提醒列表导出
 * @author: lun.yu
 * @date: 2021/8/18
 */
@Service("paymentDaysOrderExport")
public class PaymentDaysOrderExportServiceImpl implements BaseExportQueryDataService<QueryExpireDayOrderRequest> {


    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    AdminApi adminApi;
    @DubboReference
    EmployeeApi employeeApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();
    static {
        FIELD.put("orderNo", "订单号");
        FIELD.put("eid","授信主体ID");
        FIELD.put("ename","授信主体名称");
        FIELD.put("customerEid", "采购商ID");
        FIELD.put("customerName", "采购商名称");
        FIELD.put("period", "信用周期(天)");
        FIELD.put("usedTime", "下单时间");
        FIELD.put("expirationTime", "应还款日期");
        FIELD.put("usedAmount", "支付总金额");
        FIELD.put("repaymentAmount", "已还款金额");
        FIELD.put("needRepaymentAmount", "待还款金额");
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
            //代还款金额和还款状态需要做额外处理
            page.getRecords().forEach(e-> {
                e.setUsedAmount(e.getUsedAmount().subtract(e.getReturnAmount()));
                e.setNeedRepaymentAmount(e.getUsedAmount().subtract(e.getRepaymentAmount()));
                e.setRepaymentStatusName(Objects.requireNonNull(PaymentOrderRepaymentStatusEnum.getByCode(e.getRepaymentStatus())).getName());
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

        //以岭：如果是普通用户查看，展示订单商务联系人为自己的订单；如果是企业管理员查看，查看该企业所有的订单
        if(request.getOpUserId() != null && request.getOpUserId() != 0){
            Admin admin = adminApi.getById(request.getOpUserId());
            if(Objects.nonNull(admin) && admin.getAdminFlag() == 1){
                List<Long> eidList = enterpriseApi.listSubEids(eid);
                if (CollectionUtils.isEmpty(eidList)) {
                    eidList = Collections.singletonList(eid);
                } else {
                    eidList.add(eid);
                }
                request.setEidList(eidList);
            }else{
                //工业直属的场景
                boolean adminFlag = employeeApi.isAdmin(eid, request.getOpUserId());
                if(adminFlag){
                    List<Long> eidList = ListUtil.toList(eid);
                    request.setEidList(eidList);
                }else{
                    request.setContacterId(request.getOpUserId());
                }
            }
        }

        return request;
    }
}
