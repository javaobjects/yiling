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
import com.yiling.mall.order.api.OrderFlowApi;
import com.yiling.order.order.bo.OrderFlowBO;
import com.yiling.order.order.dto.request.QueryOrderFlowRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;

import cn.hutool.core.bean.BeanUtil;

/**
 * @author: shuang.zhang
 * @date: 2021/9/15
 */
@Service("orderFlowYilingExportService")
public class OrderFlowYilingExportServiceImpl implements BaseExportQueryDataService<QueryOrderFlowRequest> {

    @DubboReference
    OrderFlowApi orderFlowApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<String, String>() {

        private static final long serialVersionUID = -6207056584544766132L;

        {
            put("goodsName", "商品名称");
            put("goodsInSn", "商品内码");
            put("buyerEname", "采购商名称");
            put("buyerChannelName", "渠道类型");
            put("licenseNo", "批准文号");
            put("sellSpecifications", "规格");
            put("sellUnit", "单位");
            put("batchNo", "批次号");
            put("goodsQuantity", "购买数量");
            put("deliveryQuantity", "发货数量");
            put("returnQuantity", "退回退货数量");
            put("buyerReturnQuantity", "采购商退货数量");
            put("goodsPrice", "商品单价");
            put("goodsAmount", "商品小计");
            put("createTime", "下单时间");
            put("deliveryTime", "发货时间");
        }
    };

    @Override
    public QueryExportDataDTO queryData(QueryOrderFlowRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<OrderFlowBO> page = null;
        int current = 1;
        do {
            request.setEidList(enterpriseApi.listSubEids(Constants.YILING_EID));
            request.setCurrent(current);
            request.setSize(500);
            page = orderFlowApi.getPageList(request);
            if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }

            for (OrderFlowBO e : page.getRecords()) {
                Map<String, Object> dataPojo = BeanUtil.beanToMap(e);
                data.add(dataPojo);
            }
            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("以岭流向信息导出");
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
    public QueryOrderFlowRequest getParam(Map<String, Object> map) {
        QueryOrderFlowRequest request = PojoUtils.map(map, QueryOrderFlowRequest.class);
        return request;
    }

}
