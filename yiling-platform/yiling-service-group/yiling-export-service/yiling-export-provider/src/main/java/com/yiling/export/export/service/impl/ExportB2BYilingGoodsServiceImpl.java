package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportB2BYilingGoodsBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.task.api.TaskApi;
import com.yiling.sales.assistant.task.dto.TaskSelectGoodsDTO;
import com.yiling.sales.assistant.task.dto.request.QueryGoodsPageRequest;
import com.yiling.sales.assistant.task.enums.TaskTypeEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/9/28
 */
@Service("exportB2BYilingGoodsService")
@Slf4j
public class ExportB2BYilingGoodsServiceImpl implements BaseExportQueryDataService<QueryGoodsPageRequest> {
    @DubboReference
    TaskApi taskApi;
    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<String, String>() {
        {
            put("goodsId", "商品ID");
            put("goodsName", "商品名称");
            put("sellSpecifications", "售卖规格");
        }
    };
    @Override
    public QueryExportDataDTO queryData(QueryGoodsPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<TaskSelectGoodsDTO> page = null;
        int current = 1;
        request.setTaskType(TaskTypeEnum.PLATFORM.getCode());
        do {

            request.setCurrent(current);
            request.setSize(500);
            page = this.handleData(request, data);
            if (null != page && CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;
        } while (null != page &&  page.getTotal()>500 && CollUtil.isNotEmpty(page.getRecords()));
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("B2B以岭品");
        exportDataDTO.setFieldMap(FIELD);
        exportDataDTO.setData(data);
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;

    }

    private Page<TaskSelectGoodsDTO> handleData(QueryGoodsPageRequest request, List<Map<String, Object>> data) {
        Page<TaskSelectGoodsDTO> goodsDTOPage = taskApi.queryGoodsForAdd(request);
        if(goodsDTOPage.getTotal()==0){
            return null;
        }
        goodsDTOPage.getRecords().forEach(goods->{
            ExportB2BYilingGoodsBO export = new ExportB2BYilingGoodsBO();
            PojoUtils.map(goods,export);
            Map<String, Object> dataPojo = BeanUtil.beanToMap(export);
            data.add(dataPojo);
        });
        return goodsDTOPage;
    }

        @Override
    public QueryGoodsPageRequest getParam(Map<String, Object> map) {
        return new QueryGoodsPageRequest();
    }
}