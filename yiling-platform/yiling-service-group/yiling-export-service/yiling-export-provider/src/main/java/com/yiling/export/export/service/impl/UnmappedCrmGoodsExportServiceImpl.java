package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.order.bo.FlowCrmGoodsBO;
import com.yiling.dataflow.order.dto.request.QueryCrmGoodsRequest;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpClientApi;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 UnmappedCrmGoodsExportServiceImpl
 * @描述
 * @创建时间 2023/2/23
 * @修改人 shichen
 * @修改时间 2023/2/23
 **/
@Service("unmappedCrmGoodsExportService")
@Slf4j
public class UnmappedCrmGoodsExportServiceImpl implements BaseExportQueryDataService<QueryCrmGoodsRequest> {

    @DubboReference
    private FlowSaleApi flowSaleApi;

    @DubboReference
    private ErpClientApi erpClientApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("oldGoodsName", "原始产品名称");
        FIELD.put("spec", "原始产品规格");
        FIELD.put("goodsCode", "对应产品代码");
        FIELD.put("goodsName", "对应产品名称");
        FIELD.put("customerCode", "经销商代码");
        FIELD.put("customer", "经销商名称");
        FIELD.put("implementer", "实施负责人");
    }

    @Override
    public QueryExportDataDTO queryData(QueryCrmGoodsRequest request) {
        if(null == request){
            request = new QueryCrmGoodsRequest();
        }
        request.setLimit(10000);
        QueryExportDataDTO result = new QueryExportDataDTO();
        List<FlowCrmGoodsBO> crmGoodsList = flowSaleApi.getUnmappedCrmGoods(request);
        if(CollectionUtil.isNotEmpty(crmGoodsList)){
            List<Long> eidList = crmGoodsList.stream().map(FlowCrmGoodsBO::getEid).distinct().collect(Collectors.toList());
            Map<Long, String> implementerMap = erpClientApi.getInstallEmployeeByEidList(eidList);
            List<Map<String, Object>> data = new ArrayList<>();
            for(FlowCrmGoodsBO crmGoods:crmGoodsList){
                Map<String, Object> map = MapUtil.newHashMap(4);
                map.put("oldGoodsName",crmGoods.getGoodsName());
                map.put("spec",crmGoods.getSpecifications());
                map.put("customerCode",crmGoods.getCrmCode());
                map.put("customer",crmGoods.getEname());
                map.put("implementer",implementerMap.get(crmGoods.getEid()));
                data.add(map);
            }
            ExportDataDTO exportDataDTO = new ExportDataDTO();
            exportDataDTO.setSheetName("crm未映射商品导出");
            // 页签字段
            exportDataDTO.setFieldMap(FIELD);
            // 页签数据
            exportDataDTO.setData(data);

            List<ExportDataDTO> sheets = new ArrayList<>();
            sheets.add(exportDataDTO);
            result.setSheets(sheets);
        }
        return result;
    }

    @Override
    public QueryCrmGoodsRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, QueryCrmGoodsRequest.class);
    }
}
