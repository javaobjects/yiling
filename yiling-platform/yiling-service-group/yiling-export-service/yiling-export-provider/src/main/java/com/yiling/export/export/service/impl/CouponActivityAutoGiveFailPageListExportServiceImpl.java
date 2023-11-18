package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.couponactivityautogive.api.CouponActivityAutoGiveApi;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveRecordDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityGiveFailRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.api.UserApi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 *  自动发放失败优惠券导出
 * @author: houjie.sun
 * @date: 2021/12/17
 */
@Service("couponActivityAutoGiveFailPageListExportService")
public class CouponActivityAutoGiveFailPageListExportServiceImpl implements BaseExportQueryDataService<QueryCouponActivityGiveFailRequest> {

    @DubboReference
    CouponActivityAutoGiveApi                          couponActivityAutoGiveApi;
    @DubboReference
    CouponApi                                          couponApi;
    @DubboReference
    EnterpriseApi                                      enterpriseApi;
    @DubboReference
    UserApi                                            userApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("couponActivityAutoGiveId", "发券活动ID");
        FIELD.put("couponActivityAutoGiveName", "发券活动名称");
        FIELD.put("couponActivityId", "优惠券ID");
        FIELD.put("couponActivityName", "优惠券名称");
        FIELD.put("eid", "获券企业ID");
        FIELD.put("ename", "获券企业名称");
        FIELD.put("faileReason", "失败原因");
        FIELD.put("createTime", "发放失败时间");
    }

    @Override
    public QueryExportDataDTO queryData(QueryCouponActivityGiveFailRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        // 需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<CouponActivityAutoGiveRecordDTO> page;
        int current = 1;
        int size = 500;
        do {
            request.setCurrent(current);
            request.setSize(size);
            page = couponActivityAutoGiveApi.queryGiveFailListPage(request);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            page.getRecords().forEach(p -> {
                data.add(BeanUtil.beanToMap(p));
            });

            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("自动发放失败优惠券导出");
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
    public QueryCouponActivityGiveFailRequest getParam(Map<String, Object> map) {
        QueryCouponActivityGiveFailRequest request = PojoUtils.map(map, QueryCouponActivityGiveFailRequest.class);
        return request;
    }
}
