package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportRefundBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderRefundApi;
import com.yiling.order.order.dto.PageOrderRefundDTO;
import com.yiling.order.order.dto.request.RefundPageRequest;
import com.yiling.order.order.enums.RefundStatusEnum;
import com.yiling.order.order.enums.RefundTypeEnum;
import com.yiling.payment.enums.PayChannelEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2021/11/22
 */
@Slf4j
@Service("b2BRefundExportService")
public class B2BRefundExportServiceImpl implements BaseExportQueryDataService<RefundPageRequest> {

    @DubboReference
    OrderRefundApi orderRefundApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("refundNo", "退款单号");
        FIELD.put("refundType", "单据类型");
        FIELD.put("returnNo", "相关单号");
        FIELD.put("orderNo", "订单号");
        FIELD.put("payChannel", "支付平台");
        FIELD.put("thirdTradeNo", "支付流水号");
        FIELD.put("thirdFundNo", "退款流水号");
        FIELD.put("refundStatus", "退款状态");
        FIELD.put("createTime", "创建时间");
        FIELD.put("refundTime", "退款时间");
        FIELD.put("buyerEname", "采购商");
        FIELD.put("sellerEname", "供应商");
        FIELD.put("refundAmount", "退款总额");
        FIELD.put("platformCouponDiscountAmount", "平台承担券退款金额");
        FIELD.put("couponDiscountAmount", "商家承担券退款金额");
        FIELD.put("returnAmount", "货款退款金额");
    }

    @Override
    public QueryExportDataDTO queryData(RefundPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        if (request.getCreateStartTime() != null) {
            String start = DateUtil.format(request.getCreateStartTime(), "yyyy-MM-dd 00:00:00");
            request.setCreateStartTime(DateUtil.parse(start));
        }
        if (request.getCreateStopTime() != null) {
            String end = DateUtil.format(request.getCreateStopTime(), "yyyy-MM-dd 23:59:59");
            request.setCreateStopTime(DateUtil.parse(end));
        }
        Page<PageOrderRefundDTO> page;
        int current = 1;
        log.info("【退款单导出】退款单信息导出，请求数据为:[{}]", request);
        do {
            request.setCurrent(current);
            request.setSize(500);
            //  查询导出的数据填入data
            page = orderRefundApi.pageList(request);
            for (PageOrderRefundDTO refundDTO : page.getRecords()) {
                ExportRefundBO refundBO = new ExportRefundBO();
                refundBO.setRefundNo(refundDTO.getRefundNo());
                refundBO.setRefundType(getRefundType(refundDTO.getRefundType()));
                refundBO.setReturnNo(refundDTO.getReturnNo());
                refundBO.setOrderNo(refundDTO.getOrderNo());
                refundBO.setPayChannel(getPayChannel(refundDTO.getPayWay()));
                refundBO.setRefundStatus(getRefundStatus(refundDTO.getRefundStatus()));
                refundBO.setCreateTime(DateUtil.format(refundDTO.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                refundBO.setRefundTime(DateUtil.format(refundDTO.getRefundTime(), "yyyy-MM-dd HH:mm:ss"));
                refundBO.setBuyerEname(refundDTO.getBuyerEname());
                refundBO.setSellerEname(refundDTO.getSellerEname());
                refundBO.setRefundAmount(refundDTO.getRefundAmount());
                refundBO.setPlatformCouponDiscountAmount(refundDTO.getPlatformCouponDiscountAmount());
                refundBO.setCouponDiscountAmount(refundDTO.getCouponDiscountAmount());
                refundBO.setReturnAmount(refundDTO.getReturnAmount());
                refundBO.setThirdTradeNo(refundDTO.getThirdTradeNo());
                refundBO.setThirdFundNo(refundDTO.getThirdFundNo());

                if (RefundTypeEnum.MEMBER_RETURN == RefundTypeEnum.getByCode(refundDTO.getRefundType())) {
                    refundBO.setReturnAmount(refundDTO.getPaymentAmount());
                }
                Map<String, Object> dataPojo = BeanUtil.beanToMap(refundBO);
                data.add(dataPojo);
            }
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
        ExportDataDTO exportDataDTO = new ExportDataDTO();

        exportDataDTO.setSheetName("B2B退款单列表导出");
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
    public RefundPageRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, RefundPageRequest.class);
    }

    private String getRefundType(Integer refundType) {
        RefundTypeEnum refundTypeEnum = RefundTypeEnum.getByCode(refundType);
        if (null == refundTypeEnum) {
            return "---";
        }
        return refundTypeEnum.getName();
    }

    private String getRefundStatus(Integer refundStatus) {
        RefundStatusEnum refundStatusEnum = RefundStatusEnum.getByCode(refundStatus);
        if (null == refundStatusEnum) {
            return "---";
        }
        return refundStatusEnum.getName();
    }

    private String getPayChannel(String peyWay) {
        PayChannelEnum payChannelEnum = PayChannelEnum.getByCode(peyWay);
        if (null == payChannelEnum) {
            return "---";
        }
        return payChannelEnum.getName();
    }
}
