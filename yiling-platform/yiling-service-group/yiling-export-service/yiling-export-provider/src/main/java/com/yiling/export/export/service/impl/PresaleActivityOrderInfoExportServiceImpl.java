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
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.presale.api.PresaleActivityApi;
import com.yiling.marketing.presale.dto.PresaleActivityOrderDTO;
import com.yiling.marketing.presale.dto.request.QueryPresaleOrderRequest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 优惠券信息导出（优惠券活动）
 *
 * @author: shixing.sun
 * @date: 2022/07/25
 */
@Slf4j
@Service("PresaleActivityOrderInfoExportService")
public class PresaleActivityOrderInfoExportServiceImpl implements BaseExportQueryDataService<QueryPresaleOrderRequest> {

    @DubboReference
    PresaleActivityApi presaleActivityApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("createTime", "时间");
        FIELD.put("id", "活动ID");
        FIELD.put("name", "活动名称");
        FIELD.put("eid", "企业ID");
        FIELD.put("ename", "企业名称");
        FIELD.put("orderNo", "订单编号");
        FIELD.put("goodsId", "商品编号");
        FIELD.put("goodsName", "商品名称");
        FIELD.put("sellSpecifications", "规格");
        FIELD.put("manufacturer", "生产厂家");
        FIELD.put("sellerEname", "商家");
        FIELD.put("presalePrice", "预售价");
        FIELD.put("goodsQuantity", "数量");
        FIELD.put("presaleTypeStr", "预售类型");
        FIELD.put("depositRatioStr", "定金比例");
        FIELD.put("goodsPresaleType", "促销方式");
        FIELD.put("depositAmount", "定金");
        FIELD.put("balanceAmount", "尾款");
        FIELD.put("discountAmount", "优惠金额");
        FIELD.put("totalAmount", "实付总金额");
        FIELD.put("status", "状态");
    }

    @Override
    public QueryExportDataDTO queryData(QueryPresaleOrderRequest request) {
        log.info("PresaleActivityOrderInfoExportService request：{}", JSON.toJSON(request));
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();

        // 查询优惠券活动信息（商家后台）


        Page<PresaleActivityOrderDTO> page;
        int current = 1;
        int size = 500;
        do {
            // 查询已使用优惠券
            request.setCurrent(current);
            request.setSize(size);
            page = presaleActivityApi.queryOrderInfoByPresaleId(request);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            page.getRecords().forEach(item -> {
                String depositRatioStr = item.getDepositRatio() + "%";
                item.setDepositRatioStr(depositRatioStr);
                data.add(BeanUtil.beanToMap(item));
            });
            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("预售活动关联订单信息");
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
    public QueryPresaleOrderRequest getParam(Map<String, Object> map) {
        QueryPresaleOrderRequest request = PojoUtils.map(map, QueryPresaleOrderRequest.class);
        return request;
    }

}
