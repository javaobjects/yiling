package com.yiling.export.export.service.impl;

import java.util.ArrayList;
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
import com.yiling.user.integral.api.IntegralRecordApi;
import com.yiling.user.integral.bo.IntegralGiveUseRecordBO;
import com.yiling.user.integral.dto.request.QueryIntegralRecordRequest;

import cn.hutool.core.bean.BeanUtil;

/**
 * 运营后台-积分发放/扣减记录导出
 *
 * @author: lun.yu
 * @date: 2023-01-10
 */
@Service("integralRecordExportService")
public class IntegralRecordExportServiceImpl implements BaseExportQueryDataService<QueryIntegralRecordRequest> {

    @DubboReference
    IntegralRecordApi integralRecordApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("operTime", "发放时间");
        FIELD.put("integralValue", "积分值");
        FIELD.put("behaviorId", "行为ID");
        FIELD.put("behaviorName", "行为名称");
        FIELD.put("opRemark", "备注");
        FIELD.put("uid", "用户ID");
        FIELD.put("uname", "用户名称");
        FIELD.put("mobile", "提交人手机号");
    }

    private static final LinkedHashMap<String, String> FIELD2 = new LinkedHashMap<>();

    static {
        FIELD2.put("operTime", "扣减时间");
        FIELD2.put("integralValue", "积分值");
        FIELD2.put("opRemark", "备注");
        FIELD2.put("ruleIdStr", "规则ID");
        FIELD2.put("ruleName", "规则名称");
        FIELD2.put("behaviorId", "行为ID");
        FIELD2.put("behaviorName", "行为名称");
        FIELD2.put("uid", "用户ID");
        FIELD2.put("uname", "用户名称");
        FIELD2.put("mobile", "提交人手机号");
    }

    @Override
    public QueryExportDataDTO queryData(QueryIntegralRecordRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<IntegralGiveUseRecordBO> page;
        int current = 1;

        do {
            request.setCurrent(current);
            request.setSize(500);
            page = integralRecordApi.queryListPage(request);
            if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }

            page.getRecords().forEach(giveUseRecordBO -> {
                if (giveUseRecordBO.getRuleId() == null || giveUseRecordBO.getRuleId() == 0L) {
                    giveUseRecordBO.setRuleIdStr("");
                } else {
                    giveUseRecordBO.setRuleIdStr(giveUseRecordBO.getRuleId().toString());
                }
                data.add(BeanUtil.beanToMap(giveUseRecordBO));
            });
            current = current + 1;

        } while (CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        String sheetName = request.getType() == 1 ? "积分发放记录" : "积分扣减记录";
        exportDataDTO.setSheetName(sheetName);
        // 页签字段
        exportDataDTO.setFieldMap(request.getType() == 1 ? FIELD : FIELD2);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public QueryIntegralRecordRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, QueryIntegralRecordRequest.class);
    }

}
