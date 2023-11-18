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
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.api.MrApi;
import com.yiling.user.system.bo.MrBO;
import com.yiling.user.system.dto.request.QueryMrPageListRequest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 商家后台-销售助手-药代管理 分页列表数据导出
 *
 * @author xuan.zhou
 * @date 2022/6/7
 */
@Slf4j
@Service("mrPageListDataExportService")
public class MrPageListDataExportServiceImpl implements BaseExportQueryDataService<QueryMrPageListRequest> {

    @DubboReference
    MrApi mrApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("id", "员工ID");
        FIELD.put("code", "员工工号");
        FIELD.put("name", "员工姓名");
        FIELD.put("mobile", "员工手机号");
        FIELD.put("salesGoodsIds", "可售药品ID");
    }

    @Override
    public QueryExportDataDTO queryData(QueryMrPageListRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<MrBO> page;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(100);

            //  查询导出的数据填入data
            page = mrApi.pageList(request);
            List<MrBO> list = page.getRecords();
            if (CollUtil.isEmpty(list)) {
                continue;
            }

            List<Long> employeeIds = list.stream().map(MrBO::getId).collect(Collectors.toList());
            Map<Long, List<Long>> employeeSalesGoodsIdsMap = mrApi.listGoodsIdsByEmployeeIds(employeeIds);

            for (MrBO mrBO : list) {
                if (EnableStatusEnum.getByCode(mrBO.getStatus()) == EnableStatusEnum.DISABLED) {
                    // 停用的医药代表不导出
                    continue;
                }

                Map<String, Object> dataPojo = BeanUtil.beanToMap(mrBO);
                dataPojo.put("id", mrBO.getId());
                if (employeeSalesGoodsIdsMap.containsKey(mrBO.getId())) {
                    dataPojo.put("salesGoodsIds", StrUtil.join(",", employeeSalesGoodsIdsMap.get(mrBO.getId())));
                } else {
                    dataPojo.put("salesGoodsIds", "");
                }
                data.add(dataPojo);
            }

            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("药代数据");
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
    public QueryMrPageListRequest getParam(Map<String, Object> map) {
        QueryMrPageListRequest request = PojoUtils.map(map, QueryMrPageListRequest.class);
        return request;
    }

}
