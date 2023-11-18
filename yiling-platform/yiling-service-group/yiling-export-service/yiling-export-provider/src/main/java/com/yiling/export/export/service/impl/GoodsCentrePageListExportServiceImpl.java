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
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

/**
 * @author: shuang.zhang
 * @date: 2021/6/15
 */
@Service("goodsCentrePageListExportService")
public class GoodsCentrePageListExportServiceImpl implements BaseExportQueryDataService<QueryGoodsPageListRequest> {

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    InventoryApi inventoryApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<String, String>() {

        private static final long serialVersionUID = -6207056584544766132L;

        {
            put("id", "*商品ID");
            put("ename", "企业名称");
            put("name", "*商品名称");
            put("specifications", "*售卖规格");
            put("licenseNo", "*批准文号/生产许可证号");
            put("manufacturer", "*生产厂家");
            put("manufacturerAddress", "生产地址");
            put("auditStatusDesc", "商品审核状态");
        }
    };

    @Override
    public QueryExportDataDTO queryData(QueryGoodsPageListRequest queryGoodsPageListRequest) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<GoodsListItemBO> page = null;
        int current = 1;
        do {
            queryGoodsPageListRequest.setCurrent(current);
            queryGoodsPageListRequest.setSize(500);
            page = goodsApi.queryPageListGoods(queryGoodsPageListRequest);
            if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }

            for (GoodsListItemBO e : page.getRecords()) {
                Map<String, Object> dataPojo = BeanUtil.beanToMap(e);
                dataPojo.put("auditStatusDesc", GoodsStatusEnum.getByCode(e.getAuditStatus()).getName());
                data.add(dataPojo);
            }
            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("商品信息导出");
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
    public QueryGoodsPageListRequest getParam(Map<String, Object> map) {
        QueryGoodsPageListRequest request = PojoUtils.map(map, QueryGoodsPageListRequest.class);
        List<Long> eidList = getEidList(map);
        request.setEidList(eidList);
        return request;
    }


    public List<Long> getEidList(Map<String, Object> map) {
        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());

        List<Long> eidList = new ArrayList<>();
        eidList.add(eid);
        if (Constants.YILING_EID.equals(eid)) {
            List<Long> eidLists = enterpriseApi.listSubEids(eid);
            if (CollUtil.isNotEmpty(eidLists)) {
                eidList.addAll(eidLists);
            }
        }
        return eidList;
    }
}
