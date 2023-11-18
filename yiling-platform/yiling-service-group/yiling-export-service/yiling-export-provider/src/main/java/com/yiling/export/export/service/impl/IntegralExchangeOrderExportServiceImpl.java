package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.dto.request.QueryIntegralExchangeOrderPageRequest;
import com.yiling.user.integral.api.IntegralExchangeOrderApi;
import com.yiling.user.integral.api.IntegralRecordApi;
import com.yiling.user.integral.bo.IntegralExchangeOrderItemBO;
import com.yiling.user.integral.bo.IntegralGiveUseRecordBO;
import com.yiling.user.integral.dto.IntegralExchangeOrderExportDTO;
import com.yiling.user.integral.dto.request.QueryIntegralRecordRequest;
import com.yiling.user.integral.enums.IntegralExchangeOrderStatusEnum;
import com.yiling.user.integral.enums.IntegralGoodsTypeEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 运营后台-积分兑付订单记录导出
 *
 * @author: lun.yu
 * @date: 2023-01-13
 */
@Service("integralExchangeOrderExportService")
public class IntegralExchangeOrderExportServiceImpl implements BaseExportQueryDataService<QueryIntegralExchangeOrderPageRequest> {

    @DubboReference
    IntegralExchangeOrderApi integralExchangeOrderApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("submitTime", "兑换提交时间");
        FIELD.put("orderNo", "兑换订单号");
        FIELD.put("goodsTypeName", "兑换商品类型");
        FIELD.put("goodsName", "兑换商品名称");
        FIELD.put("exchangeNum", "兑换数量");
        FIELD.put("orderIntegral", "订单积分值");
        FIELD.put("statusName", "兑付状态");
        FIELD.put("uid", "用户ID");
        FIELD.put("uname", "用户名称");
        FIELD.put("mobile", "提交人手机号");
        FIELD.put("contactor", "收货人");
        FIELD.put("contactorPhone", "联系电话");
        FIELD.put("addressDetail", "收货地址");
        FIELD.put("expressCompanyOrderNo", "快递单号");
        FIELD.put("exchangeTime", "订单兑付时间");
    }

    @Override
    public QueryExportDataDTO queryData(QueryIntegralExchangeOrderPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<IntegralExchangeOrderItemBO> page;
        int current = 1;

        do {
            request.setCurrent(current);
            request.setSize(500);
            page = integralExchangeOrderApi.queryListPage(request);
            if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }

            Page<IntegralExchangeOrderExportDTO> orderExportDTOPage = PojoUtils.map(page, IntegralExchangeOrderExportDTO.class);

            orderExportDTOPage.getRecords().forEach(exchangeOrderExportDTO -> {
                if (StrUtil.isNotEmpty(exchangeOrderExportDTO.getProvinceName())) {
                    exchangeOrderExportDTO.setAddressDetail(exchangeOrderExportDTO.getProvinceName() + exchangeOrderExportDTO.getCityName() + exchangeOrderExportDTO.getRegionName() + exchangeOrderExportDTO.getAddress());
                }
                if (StrUtil.isNotEmpty(exchangeOrderExportDTO.getExpressCompany())) {
                    exchangeOrderExportDTO.setExpressCompanyOrderNo(exchangeOrderExportDTO.getExpressCompany() + " " + exchangeOrderExportDTO.getExpressOrderNo());
                }
                exchangeOrderExportDTO.setStatusName(Objects.requireNonNull(IntegralExchangeOrderStatusEnum.getByCode(exchangeOrderExportDTO.getStatus())).getName());
                exchangeOrderExportDTO.setGoodsTypeName(Objects.requireNonNull(IntegralGoodsTypeEnum.getByCode(exchangeOrderExportDTO.getGoodsType())).getName());
                if (exchangeOrderExportDTO.getExchangeTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) == 0) {
                    exchangeOrderExportDTO.setExchangeTime(null);
                }
                data.add(BeanUtil.beanToMap(exchangeOrderExportDTO));
            });
            current = current + 1;

        } while (CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("积分兑换订单");
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
    public QueryIntegralExchangeOrderPageRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, QueryIntegralExchangeOrderPageRequest.class);
    }

}
