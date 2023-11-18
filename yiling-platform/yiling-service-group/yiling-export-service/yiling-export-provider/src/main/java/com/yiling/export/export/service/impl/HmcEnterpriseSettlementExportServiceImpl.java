package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.HmcEnterpriseSettlementBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.control.api.GoodsPurchaseControlApi;
import com.yiling.hmc.control.bo.GoodsPurchaseControlBO;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.settlement.api.EnterpriseSettlementApi;
import com.yiling.hmc.settlement.bo.EnterpriseSettlementPageBO;
import com.yiling.hmc.settlement.dto.request.EnterpriseSettlementPageRequest;
import com.yiling.hmc.settlement.enums.InsuranceSettlementStatusEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 药品兑付明细待对账导出
 *
 * @author: yong.zhang
 * @date: 2022/4/19
 */
@Slf4j
@Service("hmcEnterpriseSettlementExportService")
public class HmcEnterpriseSettlementExportServiceImpl implements BaseExportQueryDataService<EnterpriseSettlementPageRequest> {

    @DubboReference
    EnterpriseSettlementApi enterpriseSettlementApi;

    @DubboReference
    GoodsPurchaseControlApi goodsPurchaseControlApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("goodsName", "商品名称");
        FIELD.put("goodsQuantity", "售卖数量（真实给到用户货的退的不算）");
        FIELD.put("price", "以岭给终端结算单价");
        FIELD.put("goodsAmount", "合计结算额");
        FIELD.put("detailId", "订单明细编号");
        FIELD.put("orderNo", "订单编号");
        FIELD.put("createTime", "创建日期");
        FIELD.put("finishTime", "订单完成日期");
        FIELD.put("channelName", "管控渠道");
        FIELD.put("insuranceSettlementStatus", "保司结算状态");
        FIELD.put("orderStatus", "订单状态");
        FIELD.put("ename", "药品服务终端名称");
        FIELD.put("eid", "药品服务终端ID");

        FIELD.put("settleAmount", "结账金额");
        FIELD.put("executionTime", "对账执行时间");
        FIELD.put("settleTime", "结算完成时间");
    }

    @Override
    public QueryExportDataDTO queryData(EnterpriseSettlementPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<EnterpriseSettlementPageBO> page;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            log.info("药品兑付明细待对账导出,请求数据为:[{}]", request);
            //  查询导出的数据填入data
            page = enterpriseSettlementApi.pageList(request);
            log.info("药品兑付明细待对账导出,返回数据为:[{}]", page);
            List<EnterpriseSettlementPageBO> boList = page.getRecords();
            if (CollUtil.isEmpty(boList)) {
                continue;
            }
            List<Long> sellSpecificationsIdList = page.getRecords().stream().map(EnterpriseSettlementPageBO::getSellSpecificationsId).collect(Collectors.toList());
            List<GoodsPurchaseControlBO> controlBOList = goodsPurchaseControlApi.queryGoodsPurchaseControlList(sellSpecificationsIdList);
            Map<Long, GoodsPurchaseControlBO> controlMap = controlBOList.stream().collect(Collectors.toMap(GoodsPurchaseControlBO::getSellSpecificationsId, e -> e, (k1, k2) -> k1));
            for (EnterpriseSettlementPageBO pageBO : boList) {
                HmcEnterpriseSettlementBO hmcEnterpriseSettlementBO = PojoUtils.map(pageBO, HmcEnterpriseSettlementBO.class);
                GoodsPurchaseControlBO controlBO = controlMap.get(pageBO.getSellSpecificationsId());
                hmcEnterpriseSettlementBO.setDetailId(pageBO.getId());
                hmcEnterpriseSettlementBO.setCreateTime(DateUtil.format(pageBO.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                hmcEnterpriseSettlementBO.setFinishTime(DateUtil.format(pageBO.getFinishTime(), "yyyy-MM-dd HH:mm:ss"));
                hmcEnterpriseSettlementBO.setInsuranceSettlementStatus(getInsuranceSettlementStatus(pageBO.getInsuranceSettlementStatus()));
                hmcEnterpriseSettlementBO.setOrderStatus(getOrderStatus(pageBO.getOrderStatus()));
                if (null != controlBO) {
                    hmcEnterpriseSettlementBO.setChannelName(getChannelName(controlBO.getChannelNameList()));
                }
                Map<String, Object> dataPojo = BeanUtil.beanToMap(hmcEnterpriseSettlementBO);
                data.add(dataPojo);
            }
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
        ExportDataDTO exportDataDTO = new ExportDataDTO();

        exportDataDTO.setSheetName("药品待对账导出");
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
    public EnterpriseSettlementPageRequest getParam(Map<String, Object> map) {
        EnterpriseSettlementPageRequest request = PojoUtils.map(map, EnterpriseSettlementPageRequest.class);
        String idListString = map.getOrDefault("idList", "").toString();
        if (StringUtils.isNotBlank(idListString)) {
            List<Long> idList = new ArrayList<>();
            String[] split = idListString.split(",");
            for (String str : split) {
                idList.add(Long.parseLong(str));
            }
            request.setIdList(idList);
        }
        return request;
    }

    private String getOrderStatus(Integer orderStatus) {
        // 订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退
        HmcOrderStatusEnum hmcOrderStatusEnum = HmcOrderStatusEnum.getByCode(orderStatus);
        if (null == hmcOrderStatusEnum) {
            return "---";
        }
        return hmcOrderStatusEnum.getName();
    }

    private String getInsuranceSettlementStatus(Integer insuranceSettlementStatus) {
        // 保司结算状态:1-待结算/2-已结算/3-无需结算失效单/4-预付款抵扣已结
        InsuranceSettlementStatusEnum insuranceSettlementStatusEnum = InsuranceSettlementStatusEnum.getByCode(insuranceSettlementStatus);
        if (null == insuranceSettlementStatusEnum) {
            return "---";
        }
        return insuranceSettlementStatusEnum.getName();
    }

    private String getChannelName(List<String> channelNameList) {
        return StringUtils.join(channelNameList, ",");
    }
}
