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
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

/**
 * 商品定价模板导出
 *
 * @author: yuehceng.chen
 * @date: 2021/6/23
 */
@Service("goodsPriceMouldExportService")
public class GoodsPriceMouldExportServiceImpl implements BaseExportQueryDataService<QueryGoodsPageListRequest> {

    @DubboReference
    GoodsApi      goodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("id", "商品ID");
        FIELD.put("name", "商品名称");
        FIELD.put("licenseNo", "批准文号/生产许可证号");
        FIELD.put("manufacturer", "生产厂家");
        FIELD.put("manufacturerAddress", "生产地址");
        FIELD.put("specifications", "商品规格");
        FIELD.put("unit", "单位");
        FIELD.put("price", "基价");
        FIELD.put("customerEid", "客户ID");
        FIELD.put("customerName", "客户名称");
        FIELD.put("customerGroupId", "客户分组ID");
        FIELD.put("customerGroupName", "客户分组名称");
        FIELD.put("specificPriceValue", "设置具体价格");
        FIELD.put("floatPriceValue", "设置浮点价格");
    }

    @Override
    public QueryExportDataDTO queryData(QueryGoodsPageListRequest queryGoodsPageListRequest) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        int current = 1;
        do {
            queryGoodsPageListRequest.setCurrent(current);
            queryGoodsPageListRequest.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
            queryGoodsPageListRequest.setSize(500);
            Page<GoodsListItemBO> page = goodsApi.queryPageListGoods(queryGoodsPageListRequest);
            if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }
            for (GoodsListItemBO e : page.getRecords()) {
                Map<String, Object> dataPojo = BeanUtil.beanToMap(e);
                data.add(dataPojo);
            }
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(data));
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("商品定价信息模板导出");
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
        //参数eid的转换为eidList
        QueryGoodsPageListRequest request = PojoUtils.map(map, QueryGoodsPageListRequest.class);
        Long eid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        if (eid != 0) {
            List<Long> eidList = enterpriseApi.listSubEids(eid);
            if (CollUtil.isNotEmpty(eidList)) {
                request.setEidList(eidList);
            }
        }
        return request;
    }
}
