package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.FlowGoodsRelationDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationPageRequest;
import com.yiling.dataflow.relation.api.FlowGoodsRelationApi;
import com.yiling.dataflow.relation.enums.FlowGoodsRelationLabelEnum;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/7/22
 */
@Slf4j
@Service("flowGoodsRelationExportService")
public class FlowGoodsRelationExportServiceImpl implements BaseExportQueryDataService<QueryFlowGoodsRelationPageRequest> {

    @DubboReference
    FlowGoodsRelationApi flowGoodsRelationApi;


    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("eid", "*商业ID");
        FIELD.put("ename", "商业名称");
        FIELD.put("goodsName", "商品名称");
        FIELD.put("goodsSpecifications", "商品规格");
        FIELD.put("goodsInSn", "*商品内码");
        FIELD.put("goodsManufacturer", "生产厂家");
        FIELD.put("goodsRelationLabelStr", "*商品关系标签(1-以岭品,2-非以岭品,3-中药饮片)");
        FIELD.put("ylGoodsId", "*以岭商品ID(商品关系标签为1时必填)");
        FIELD.put("ylGoodsName", "以岭商品名称");
        FIELD.put("ylGoodsSpecifications", "以岭商品规格");
    }


    @Override
    public QueryExportDataDTO queryData(QueryFlowGoodsRelationPageRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        // 商品关系标签
        Map<Integer, String> flowGoodsRelationLabelMap = FlowGoodsRelationLabelEnum.getCodeMap();
        // 商品关系标签导出条件
        String goodsRelationLabelStr = request.getGoodsRelationLabelStr();
        if (StrUtil.isNotBlank(goodsRelationLabelStr)) {
            List<Integer> goodsRelationLabelList = new ArrayList<>();
            if (goodsRelationLabelStr.contains(",")) {
                List<Integer> list = Convert.toList(Integer.class, goodsRelationLabelStr);
                goodsRelationLabelList.addAll(list);
            } else {
                goodsRelationLabelList.add(Integer.parseInt(goodsRelationLabelStr));
            }
            request.setGoodsRelationLabelList(goodsRelationLabelList);
        }

        // 需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<FlowGoodsRelationDTO> page;
        int current = 1;
        int size = 500;
        do {
            request.setCurrent(current);
            request.setSize(size);
            page = flowGoodsRelationApi.page(request);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            page.getRecords().forEach(o -> {
                Long ylGoodsId = o.getYlGoodsId();
                if(ObjectUtil.isNotNull(ylGoodsId) && 0 == ylGoodsId.intValue()){
                    o.setYlGoodsId(null);
                }
                // 商品关系标签
                convertGoodsRelationLabel(o, flowGoodsRelationLabelMap);

                data.add(BeanUtil.beanToMap(o));
            });
            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("商家品与以岭品对应关系导出");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    private void convertGoodsRelationLabel(FlowGoodsRelationDTO flowGoodsRelationDTO, Map<Integer, String> flowGoodsRelationLabelMap) {
        Integer goodsRelationLabel = flowGoodsRelationDTO.getGoodsRelationLabel();
        if (ObjectUtil.isNull(goodsRelationLabel)) {
            flowGoodsRelationDTO.setGoodsRelationLabelStr(FlowGoodsRelationLabelEnum.EMPTY.getDesc());
            return;
        }
        String relationLabelDesc = flowGoodsRelationLabelMap.get(goodsRelationLabel);
        if (StrUtil.isBlank(relationLabelDesc)) {
            flowGoodsRelationDTO.setGoodsRelationLabelStr(FlowGoodsRelationLabelEnum.EMPTY.getDesc());
            return;
        }
        flowGoodsRelationDTO.setGoodsRelationLabelStr(relationLabelDesc);
    }

    @Override
    public QueryFlowGoodsRelationPageRequest getParam(Map<String, Object> map) {
        QueryFlowGoodsRelationPageRequest request = PojoUtils.map(map, QueryFlowGoodsRelationPageRequest.class);
        log.info("商家品与以岭品对应关系导出, request:{}", JSON.toJSONString(request));
        return request;
    }

}
