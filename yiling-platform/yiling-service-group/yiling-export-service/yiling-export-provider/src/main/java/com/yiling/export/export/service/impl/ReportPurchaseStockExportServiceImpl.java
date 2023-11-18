package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.api.FlowPurchaseInventoryApi;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryListPageDTO;
import com.yiling.dataflow.order.dto.FlowPurchaseInventoryLogListPageDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryLogListPageRequest;
import com.yiling.dataflow.order.enums.FlowPurchaseInventoryBusinessTypeEnum;
import com.yiling.export.export.bo.ExportRebatedGoodsCountBO;
import com.yiling.export.export.bo.ExportReportPurchaseBO;
import com.yiling.export.export.bo.ExportReportPurchaseStockReviseBO;
import com.yiling.export.export.bo.ExportReportStockOccupyBO;
import com.yiling.export.export.bo.ExportReportStockReviseBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.settlement.report.api.ReportApi;
import com.yiling.settlement.report.dto.RebatedGoodsCountDTO;
import com.yiling.settlement.report.dto.ReportPurchaseStockOccupyDTO;
import com.yiling.settlement.report.dto.request.ExportReportPurchaseStockRequest;
import com.yiling.settlement.report.dto.request.QueryRevisePurchaseStockRequest;
import com.yiling.settlement.report.dto.request.QueryReviseStockPageRequest;
import com.yiling.settlement.report.dto.request.QueryStockOccupyPageRequest;
import com.yiling.settlement.report.enums.ReportTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-11-04
 */
@Service("reportPurchaseStockExportServiceImpl")
@Slf4j
public class ReportPurchaseStockExportServiceImpl implements BaseExportQueryDataService<ExportReportPurchaseStockRequest> {

    @DubboReference
    ReportApi reportApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    FlowPurchaseInventoryApi flowPurchaseInventoryApi;


    private static final LinkedHashMap<String, String> FIELD_SHEET = new LinkedHashMap<String, String>() {

        {
            put("eid", "商业id");
            put("ename", "商业名称");
            put("goodsInSn", "商品内码");
            put("goodsName", "商品名称");
            put("goodsSpecifications", "商品规格");
            put("ylGoodsId", "以岭商品ID");
            put("ylGoodsName", "以岭商品名称");
            put("ylGoodsSpecifications", "以岭商品规格");
            put("totalPoQuantity", "采购库存");
            put("poSourceStr", "购进渠道");
            put("rebateStock", "已返利库存");
            put("poQuantity", "采购剩余库存");
            put("reviseStock", "调整库存");

        }
    };

    private static final LinkedHashMap<String, String> FIELD_SHEET2 = new LinkedHashMap<String, String>() {

        {
            put("eid", "商业id");
            put("ename", "商业名称");
            put("goodsInSn", "商品内码");
            put("goodsName", "商品名称");
            put("goodsSpecifications", "商品规格");
            put("ylGoodsId", "以岭商品ID");
            put("ylGoodsName", "以岭商品名称");
            put("ylGoodsSpecifications", "以岭商品规格");
            put("poSourceStr", "供应商名称");
            put("createTimeStr", "调整时间");
            put("poQuantity", "调整数量");
            put("opUserName", "操作人");

        }
    };

    private static final LinkedHashMap<String, String> FIELD_SHEET3 = new LinkedHashMap<String, String>() {

        {
            put("reportId", "返利总表ID");
            put("typeStr", "返利类型");
            put("orderNo", "订单号");
            put("soNo", "erp订单号");
            put("sellerName", "商业名称");
            put("sellerEid", "商业ID");
            put("goodsInSn", "商品内码");
            put("ylGoodsId", "以岭商品ID");
            put("ylGoodsName", "以岭商品名称");
            put("ylGoodsSpecification", "以岭商品规格");
            put("soQuantity", "数量");
            put("soUnit", "单位");
            put("purchaseChannelStr", "购进渠道");
        }
    };


    @Override
    public QueryExportDataDTO queryData(ExportReportPurchaseStockRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        QueryFlowPurchaseInventoryListPageRequest queryRequest = PojoUtils.map(request, QueryFlowPurchaseInventoryListPageRequest.class);


        //不同sheet数据
        List<Map<String, Object>> purchaseStockData = new ArrayList<>();
        List<Map<String, Object>> stockReviseData = new ArrayList<>();
        List<Map<String, Object>> stockOccupyData = new ArrayList<>();


        int mainCurrent = 1;
        Page<FlowPurchaseInventoryListPageDTO> mainPage;
        //分页查询结算单列表
        do {

            queryRequest.setCurrent(mainCurrent);
            queryRequest.setSize(50);
            //查询结算单
            mainPage = flowPurchaseInventoryApi.pageByEidAndYlGoodsId(queryRequest);

            List<FlowPurchaseInventoryListPageDTO> mainRecords = mainPage.getRecords();

            if (CollUtil.isEmpty(mainRecords)) {
                break;
            }
            List<ExportReportPurchaseBO> sheet1Data = PojoUtils.map(mainRecords, ExportReportPurchaseBO.class);

            //查询调整库存
            List<QueryRevisePurchaseStockRequest> queryParList= ListUtil.toList();
            mainRecords.forEach(e->{
                QueryRevisePurchaseStockRequest var=PojoUtils.map(e,QueryRevisePurchaseStockRequest.class);
                var.setPurchaseChannel(e.getPoSource());
                queryParList.add(var);
            });
            //查询库存调整
            Map<Long, BigDecimal> goodsStockList = flowPurchaseInventoryApi.getSumAdjustmentQuantityByInventoryIdList(mainRecords.stream().map(FlowPurchaseInventoryListPageDTO::getId).collect(Collectors.toList()));

            //查询已返利数量
            List<RebatedGoodsCountDTO> goodsRebateCountList = reportApi.queryRebateCount(PojoUtils.map(queryParList,QueryStockOccupyPageRequest.class));
            Map<ExportRebatedGoodsCountBO,Integer> goodsRebateCountMap = PojoUtils.map(goodsRebateCountList, ExportRebatedGoodsCountBO.class).stream().collect(Collectors.toMap(e->e,ExportRebatedGoodsCountBO::getQuantity));

            //生成sheet1&2
            sheet1Data.forEach(e -> {
                ExportRebatedGoodsCountBO var=new ExportRebatedGoodsCountBO();
                var.setEid(e.getEid());
                var.setYlGoodsId(e.getYlGoodsId());
                var.setGoodsInSn(e.getGoodsInSn());
                var.setPurchaseChannel(e.getPoSource());
                e.setRebateStock(goodsRebateCountMap.getOrDefault(var,0).longValue());

                e.setReviseStock(goodsStockList.getOrDefault(e.getId(),BigDecimal.ZERO).longValue());

                e.setTotalPoQuantity(e.getTotalPoQuantity()+goodsStockList.getOrDefault(e.getId(),BigDecimal.ZERO).longValue());
                e.setPoQuantity(e.getPoQuantity());
                if (ObjectUtil.equal(e.getPoSource(), 1)) {
                    e.setPoSourceStr("大运河采购");
                } else {
                    e.setPoSourceStr("京东采购");
                }
                Map<String, Object> dataPojo = BeanUtil.beanToMap(e);
                purchaseStockData.add(dataPojo);

                //查询sheet2数据
                QueryFlowPurchaseInventoryLogListPageRequest reviseStockRequest = PojoUtils.map(e, QueryFlowPurchaseInventoryLogListPageRequest.class);
                reviseStockRequest.setBusinessType(FlowPurchaseInventoryBusinessTypeEnum.SETTLEMENT_ADJUSTMENT.getCode());
                reviseStockRequest.setInventoryId(e.getId());
                queryStockRevise(reviseStockRequest, stockReviseData);

                //查询sheet3数据
                QueryStockOccupyPageRequest reviseStockOccupyRequest = PojoUtils.map(e, QueryStockOccupyPageRequest.class);
                reviseStockOccupyRequest.setPurchaseChannel(e.getPoSource());
                queryStockOccupy(reviseStockOccupyRequest, stockOccupyData);

            });

            mainCurrent = mainCurrent + 1;
        } while (mainPage != null && CollectionUtils.isNotEmpty(mainPage.getRecords()));

        ExportDataDTO exportPurchaseStock = new ExportDataDTO();
        exportPurchaseStock.setSheetName("进销存信息");
        //如果是非以岭导出，删除结算单备注字段
        exportPurchaseStock.setFieldMap(FIELD_SHEET);
        // 页签数据
        exportPurchaseStock.setData(purchaseStockData);

        ExportDataDTO exportReportStockRevise = new ExportDataDTO();
        exportReportStockRevise.setSheetName("调整信息");
        // 页签字段
        exportReportStockRevise.setFieldMap(FIELD_SHEET2);
        // 页签数据
        exportReportStockRevise.setData(stockReviseData);

        ExportDataDTO exportReportStockOccupy = new ExportDataDTO();
        exportReportStockOccupy.setSheetName("返利占用信息");
        // 页签字段
        exportReportStockOccupy.setFieldMap(FIELD_SHEET3);
        // 页签数据
        exportReportStockOccupy.setData(stockOccupyData);


        //封装excel
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportPurchaseStock);
        sheets.add(exportReportStockRevise);
        sheets.add(exportReportStockOccupy);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public ExportReportPurchaseStockRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, ExportReportPurchaseStockRequest.class);
    }


    /**
     * 查询库存修改
     *
     * @param request
     * @param stockReviseData
     */
    private void queryStockRevise(QueryFlowPurchaseInventoryLogListPageRequest request, List<Map<String, Object>> stockReviseData) {
        int current = 1;
        Page<FlowPurchaseInventoryLogListPageDTO> page;
        //分页查询结算单列表
        do {

            request.setCurrent(current);
            request.setSize(50);
            //查询库存修改日志
            page = flowPurchaseInventoryApi.adjustmentLogPageLogByInventoryId(request);

            List<FlowPurchaseInventoryLogListPageDTO> mainRecords = page.getRecords();

            if (CollUtil.isEmpty(mainRecords)) {
                break;
            }

            List<ExportReportStockReviseBO> sheet2Data = PojoUtils.map(mainRecords, ExportReportStockReviseBO.class);

            //生成sheet2
            sheet2Data.forEach(e -> {
                if (ObjectUtil.equal(e.getPoSource(), 1)) {
                    e.setPoSourceStr("大运河采购");
                } else {
                    e.setPoSourceStr("京东采购");
                }
                e.setCreateTimeStr(DateUtil.formatDateTime(e.getCreateTime()));
                Map<String, Object> dataPojo = BeanUtil.beanToMap(e);
                stockReviseData.add(dataPojo);

            });

            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));
    }

    /**
     * 查询库存占用的报表
     *
     * @param reviseStockRequest
     * @param stockReviseData
     */
    private void queryStockOccupy(QueryStockOccupyPageRequest reviseStockRequest, List<Map<String, Object>> stockReviseData) {
        int current = 1;
        Page<ReportPurchaseStockOccupyDTO> page;
        //分页查询结算单列表
        do {

            reviseStockRequest.setCurrent(current);
            reviseStockRequest.setSize(50);
            //查询结算单
            page = reportApi.queryStockOccupyPage(reviseStockRequest);

            List<ReportPurchaseStockOccupyDTO> mainRecords = page.getRecords();

            if (CollUtil.isEmpty(mainRecords)) {
                break;
            }

            List<ExportReportStockOccupyBO> sheet3Data = PojoUtils.map(mainRecords, ExportReportStockOccupyBO.class);

            //查询占用库存的商品单位
            List<Long> goodsIdList = sheet3Data.stream().filter(e -> ObjectUtil.equal(e.getType(), ReportTypeEnum.B2B.getCode())).map(ExportReportStockOccupyBO::getGoodsId).distinct().collect(Collectors.toList());
            Map<Long, GoodsDTO> goodsMap = goodsApi.batchQueryInfo(goodsIdList).stream().collect(Collectors.toMap(GoodsDTO::getId, e -> e));
            //查询商业名称
            List<Long> eidList = sheet3Data.stream().map(ExportReportStockOccupyBO::getSellerEid).distinct().collect(Collectors.toList());
            Map<Long, String> eidMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
            //生成sheet3
            sheet3Data.forEach(e -> {
                e.setSellerName(eidMap.getOrDefault(e.getSellerEid(),""));
                if (ObjectUtil.equal(e.getPurchaseChannel(), 1)) {
                    e.setPurchaseChannelStr("大运河采购");
                } else {
                    e.setPurchaseChannelStr("京东采购");
                }
                if (ObjectUtil.equal(e.getType(), ReportTypeEnum.B2B.getCode())) {
                    e.setGoodsInSn(e.getGoodsErpCode());
                    e.setYlGoodsSpecification(e.getSpecifications());
                    e.setYlGoodsId(e.getGoodsId());
                    e.setYlGoodsName(e.getGoodsName());
                    e.setSoQuantity(e.getReceiveQuantity());
                    e.setTypeStr(ReportTypeEnum.B2B.getName());
                }else {
                    e.setTypeStr(ReportTypeEnum.FLOW.getName());
                }
                GoodsDTO goodsDTO = goodsMap.get(e.getYlGoodsId());
                if (ObjectUtil.equal(e.getType(), ReportTypeEnum.B2B.getCode()) && ObjectUtil.isNotNull(goodsDTO)) {
                    e.setSoUnit(goodsDTO.getSellUnit());
                }
                Map<String, Object> dataPojo = BeanUtil.beanToMap(e);
                stockReviseData.add(dataPojo);

            });

            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));
    }


}
