package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.ExportReportFlowOrderSyncInfoBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.settlement.report.api.ReportOrderSyncApi;
import com.yiling.settlement.report.dto.ReportFlowSaleOrderSyncDTO;
import com.yiling.settlement.report.dto.request.ExportFlowOrderSyncRequest;
import com.yiling.settlement.report.dto.request.QueryFlowOrderPageListRequest;
import com.yiling.settlement.report.enums.ReportOrderIdenEnum;
import com.yiling.settlement.report.enums.ReportStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-11-04
 */
@Service("reportFlowOrderSyncInfoExportServiceImpl")
@Slf4j
public class ReportFlowOrderSyncInfoExportServiceImpl implements BaseExportQueryDataService<ExportFlowOrderSyncRequest> {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    ReportOrderSyncApi reportOrderSyncApi;


    private static final LinkedHashMap<String, String> FIELD_SHEET = new LinkedHashMap<String, String>() {

        {
            put("soNo", "订单编号");
            put("id", "订单详情编号");
            put("enterpriseInnerCode", "采购商ID");
            put("enterpriseName", "采购商名称");
            put("eid", "供应商ID");
            put("sellerEName", "供应商名称");
            put("goodsName", "商品名称");
            put("goodsInSn", "商品内码");
            put("soSpecifications", "商品规格");
            put("ylGoodsId", "以岭商品ID");
            put("ylGoodsName", "以岭商品名称");
            put("ylSpecifications", "以岭商品规格");
            put("identificationStatusStr", "标识状态");

        }
    };


    @Override
    public QueryExportDataDTO queryData(ExportFlowOrderSyncRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        QueryFlowOrderPageListRequest queryRequest = PojoUtils.map(request, QueryFlowOrderPageListRequest.class);

        if (StrUtil.isNotBlank(request.getIdList())) {
            queryRequest.setIdList(Arrays.stream(StrUtil.splitToLong(request.getIdList(), ",")).boxed().collect(Collectors.toList()));
        }

        //返利状态
        if (ObjectUtil.isNull(request.getReportStatus()) || ObjectUtil.equal(request.getReportStatus(), -1)) {
            queryRequest.setReportStatusList(ListUtil.toList(0, ReportStatusEnum.OPERATOR_REJECT.getCode(), ReportStatusEnum.FINANCE_REJECT.getCode(), ReportStatusEnum.ADMIN_REJECT.getCode()));
        } else {
            queryRequest.setReportStatusList(ListUtil.toList(request.getReportStatus()));
        }

        if (!(StringUtils.isEmpty(request.getProvinceCode()) && StringUtils.isEmpty(request.getCityCode()) && StringUtils.isEmpty(request.getRegionCode()))) {
            QueryEnterpriseListRequest eidRequest = new QueryEnterpriseListRequest();
            eidRequest.setCityCode(request.getCityCode());
            eidRequest.setRegionCode(request.getRegionCode());
            eidRequest.setProvinceCode(request.getProvinceCode());
            eidRequest.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
            eidRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.queryListByArea(eidRequest);
            List<Long> eids = enterpriseDTOS.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(eids)) {
                queryRequest.setEidList(eids);
            } else {
                return result;
            }
        }
        if (ObjectUtil.isNotNull(request.getEid()) && ObjectUtil.notEqual(request.getEid(), 0L)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getEid());
            if (ObjectUtil.isNull(enterpriseDTO)) {
                return result;
            }
            if (CollUtil.isNotEmpty(queryRequest.getEidList()) && !queryRequest.getEidList().contains(enterpriseDTO.getId())) {
                return result;
            }
            queryRequest.setEidList(ListUtil.toList(enterpriseDTO.getId()));
        }
        if (CollUtil.isEmpty(queryRequest.getEidList())) {
            return result;
        }

        //不同sheet数据
        List<Map<String, Object>> orderSyncInfo = new ArrayList<>();


        int mainCurrent = 1;
        Page<ReportFlowSaleOrderSyncDTO> mainPage;
        //分页查询结算单列表
        do {

            queryRequest.setCurrent(mainCurrent);
            queryRequest.setSize(50);
            //查询结算单
            mainPage = reportOrderSyncApi.queryFlowOrderPageList(queryRequest);

            List<ReportFlowSaleOrderSyncDTO> mainRecords = mainPage.getRecords();

            if (CollUtil.isEmpty(mainRecords)) {
                break;
            }

            //查询供应商信息
            List<Long> eidIdList = mainRecords.stream().map(ReportFlowSaleOrderSyncDTO::getEid).distinct().collect(Collectors.toList());
            //企业map
            Map<Long, String> entDTOMap = enterpriseApi.listByIds(eidIdList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
            //查询以岭商品名
            List<Long> goodsIdList = mainRecords.stream().map(ReportFlowSaleOrderSyncDTO::getYlGoodsId).distinct().collect(Collectors.toList());
            Map<Long, GoodsDTO> goodsDTOMap = goodsApi.batchQueryInfo(goodsIdList).stream().collect(Collectors.toMap(GoodsDTO::getId, e -> e));


            //生成sheet1
            mainRecords.forEach(orderDetail -> {
                ExportReportFlowOrderSyncInfoBO var1 = PojoUtils.map(orderDetail, ExportReportFlowOrderSyncInfoBO.class);
                var1.setSellerEName(entDTOMap.getOrDefault(orderDetail.getEid(), ""));
                GoodsDTO goodsDTO = goodsDTOMap.get(var1.getYlGoodsId());
                if (ObjectUtil.isNotNull(goodsDTO)) {
                    var1.setYlGoodsName(goodsDTO.getName());
                    var1.setYlSpecifications(goodsDTO.getSpecifications());
                }
                if (ObjectUtil.isNotNull(ReportOrderIdenEnum.getByCode(var1.getIdentificationStatus()))) {
                    var1.setIdentificationStatusStr(ReportOrderIdenEnum.getByCode(var1.getIdentificationStatus()).getName());
                }
                Map<String, Object> dataPojo = BeanUtil.beanToMap(var1);
                orderSyncInfo.add(dataPojo);

            });

            mainCurrent = mainCurrent + 1;
        } while (mainPage != null && CollectionUtils.isNotEmpty(mainPage.getRecords()));

        ExportDataDTO exportReportSyncOrder = new ExportDataDTO();
        exportReportSyncOrder.setSheetName("订单返利");

        //如果是非以岭导出，删除结算单备注字段
        exportReportSyncOrder.setFieldMap(FIELD_SHEET);
        // 页签数据
        exportReportSyncOrder.setData(orderSyncInfo);

        //封装excel
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportReportSyncOrder);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public ExportFlowOrderSyncRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, ExportFlowOrderSyncRequest.class);
    }

}
