package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.common.util.CouponUtil;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.bo.CouponUseOrderBO;
import com.yiling.marketing.coupon.dto.request.QueryCouponPageRequest;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityUsedPageDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderCouponUseApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.UserApi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 已使用优惠券导出（优惠券活动）
 * @author: houjie.sun
 * @date: 2021/12/15
 */
@Service("couponActivityHasUsedExportService")
public class CouponActivityHasUsedExportServiceImpl implements BaseExportQueryDataService<QueryCouponActivityGivePageRequest> {

    @DubboReference
    CouponActivityApi                                  couponActivityApi;
    @DubboReference
    CouponApi                                          couponApi;
    @DubboReference
    OrderCouponUseApi                                  orderCouponUseApi;
    @DubboReference
    OrderApi                                           orderApi;
    @DubboReference
    EnterpriseApi                                      enterpriseApi;
    @DubboReference
    UserApi                                            userApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("couponId", "优惠券ID");
        FIELD.put("couponRules", "优惠规则");
        FIELD.put("eid", "获券企业id");
        FIELD.put("ename", "获券企业名称");
        FIELD.put("getTypeStr", "获取方式");
        FIELD.put("effectiveTime", "有效期信息");
        FIELD.put("getTime", "获券时间");
        FIELD.put("useTime", "使用时间");
        FIELD.put("orderNo", "订单号");
        FIELD.put("amount", "优惠金额");
        FIELD.put("orderAmount", "订单金额");
    }

    @Override
    public QueryExportDataDTO queryData(QueryCouponActivityGivePageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();

        // 查询优惠券活动信息
        QueryCouponActivityDetailRequest couponActivityRequest = new QueryCouponActivityDetailRequest();
        couponActivityRequest.setId(request.getCouponActivityId());
        CouponActivityDetailDTO couponActivity = couponActivityApi.getCouponActivityById(couponActivityRequest);
        if (ObjectUtil.isNotNull(couponActivity)) {
            // 优惠规则
            String name = couponActivity.getName();
            CouponActivityDTO dto = PojoUtils.map(couponActivity, CouponActivityDTO.class);
            String couponRules = couponActivityApi.buildCouponRules(dto);
            String effectiveTime = CouponUtil.buildEffectiveTime(couponActivity, false);

            Page<CouponActivityUsedPageDTO> page;
            int current = 1;
            int size = 500;
            do {
                // 查询已使用优惠券
                QueryCouponPageRequest couponUseRequest = PojoUtils.map(request, QueryCouponPageRequest.class);
                couponUseRequest.setCurrent(current);
                couponUseRequest.setSize(size);
                Page<CouponUseOrderBO> couponUsePage = couponApi.getOrderCountUsePageByActivityId(couponUseRequest);
                if (ObjectUtil.isNull(couponUsePage) || CollUtil.isEmpty(couponUsePage.getRecords())) {
                    break;
                }
                // 获券企业名称
                List<Long> eidList = couponUsePage.getRecords().stream().map(CouponUseOrderBO::getEid).distinct().collect(Collectors.toList());
                Map<Long, String> enameMap = getEnameMap(eidList);
                // 组装优惠券信息
                page = PojoUtils.map(couponUsePage, CouponActivityUsedPageDTO.class);
                for (CouponActivityUsedPageDTO couponUsed : page.getRecords()) {// couponUsed.setId(couponUsed.getCouponId());
                    couponUsed.setCouponRules(couponRules);
                    couponUsed.setCouponName(name);
                    couponUsed.setEname(enameMap.get(couponUsed.getEid()));
                    couponUsed.setEffectiveTime(effectiveTime);
                    // 获取方式，1-运营发放；2-自动发放；3-自主领取
                    String getTypeStr = CouponGetTypeEnum.getByCode(couponUsed.getGetType()).getName();
                    couponUsed.setGetTypeStr(StrUtil.isBlank(getTypeStr) ? "" : getTypeStr);
                }

                // 查询订单信息
                List<Long> orderIds = page.getRecords().stream().map(CouponActivityUsedPageDTO::getOrderId).collect(Collectors.toList());
                List<OrderDTO> orderList = orderApi.listByIds(orderIds);
                if (CollUtil.isEmpty(orderList)) {
                    page.getRecords().forEach(p -> {
                        data.add(BeanUtil.beanToMap(p));
                    });
                    // 如果直接跳出循环
                    current = current + 1;
                    continue;
                }
                Map<Long, OrderDTO> orderMap = orderList.stream().collect(Collectors.toMap(OrderDTO::getId, Function.identity()));
                // 组装订单信息
                for (CouponActivityUsedPageDTO couponUsed : page.getRecords()) {
                    OrderDTO order = orderMap.get(couponUsed.getOrderId());
                    if (ObjectUtil.isNotNull(order)) {
                        couponUsed.setOrderId(order.getId());
                        couponUsed.setOrderNo(order.getOrderNo());
                        couponUsed.setOrderAmount(order.getTotalAmount());
                    }
                    data.add(BeanUtil.beanToMap(couponUsed));
                }

                current = current + 1;
            } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));
        }

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("已使用优惠券导出（优惠券活动）");
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
    public QueryCouponActivityGivePageRequest getParam(Map<String, Object> map) {
        QueryCouponActivityGivePageRequest request = PojoUtils.map(map, QueryCouponActivityGivePageRequest.class);
        return request;
    }


    private Map<Long, String> getEnameMap(List<Long> eidList) {
        Map<Long, String> enameMap = new HashMap<>();
        if (CollUtil.isEmpty(eidList)) {
            return enameMap;
        }
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eidList);
        if (CollUtil.isNotEmpty(enterpriseList)) {
            enameMap = enterpriseList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getName(), (v1, v2) -> v1));
        }
        return enameMap;
    }
}
