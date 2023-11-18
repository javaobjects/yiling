package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.dto.request.QueryMrSalesGoodsPageListRequest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 商家后台-销售助手-药代管理 可售药品分页列表数据导出
 *
 * @author xuan.zhou
 * @date 2022/6/7
 */
@Slf4j
@Service("mrSalesGoodsPageListDataExportService")
public class MrSalesGoodsPageListDataExportServiceImpl implements BaseExportQueryDataService<QueryMrSalesGoodsPageListRequest> {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    PopGoodsApi popGoodsApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("id", "可售药品ID");
        FIELD.put("name", "药品名称");
        FIELD.put("sellSpecifications", "销售规格");
        FIELD.put("goodsStatus", "状态");
    }

    @Override
    public QueryExportDataDTO queryData(QueryMrSalesGoodsPageListRequest request) {
        QueryGoodsPageListRequest queryGoodsPageListRequest = new QueryGoodsPageListRequest();

        // 区分以岭、非以岭
        if (Constants.YILING_EID.equals(request.getEid())) {
            List<Long> subEids = enterpriseApi.listSubEids(request.getEid());
            queryGoodsPageListRequest.setEidList(subEids);
        } else {
            queryGoodsPageListRequest.setEidList(ListUtil.toList(request.getEid()));
        }

        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<GoodsListItemBO> page;
        int current = 1;
        do {
            queryGoodsPageListRequest.setCurrent(current);
            queryGoodsPageListRequest.setSize(100);

            //  查询导出的数据填入data
            page = popGoodsApi.queryPopGoodsPageList(queryGoodsPageListRequest);
            List<GoodsListItemBO> list = page.getRecords();
            if (CollUtil.isEmpty(list)) {
                continue;
            }

            for (GoodsListItemBO goodsListItemBO : list) {
                Map<String, Object> dataPojo = BeanUtil.beanToMap(goodsListItemBO);
                dataPojo.put("id", goodsListItemBO.getId());
                dataPojo.put("name", goodsListItemBO.getName());
                dataPojo.put("sellSpecifications", goodsListItemBO.getSellSpecifications());
                dataPojo.put("goodsStatus", GoodsStatusEnum.getByCode(goodsListItemBO.getGoodsStatus()).getName());
                data.add(dataPojo);
            }

            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("可售商品数据");
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
    public QueryMrSalesGoodsPageListRequest getParam(Map<String, Object> map) {
        QueryMrSalesGoodsPageListRequest request = PojoUtils.map(map, QueryMrSalesGoodsPageListRequest.class);
        return request;
    }

}
