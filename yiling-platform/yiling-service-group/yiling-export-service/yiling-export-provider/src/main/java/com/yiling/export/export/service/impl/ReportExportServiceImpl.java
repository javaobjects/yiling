package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.export.export.bo.ExportReportBO;
import com.yiling.export.export.bo.ExportReportDetailBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.settlement.report.api.ReportApi;
import com.yiling.settlement.report.dto.ReportDTO;
import com.yiling.settlement.report.dto.ReportDetailB2bDTO;
import com.yiling.settlement.report.dto.ReportDetailFlowDTO;
import com.yiling.settlement.report.dto.request.ExportReportRequest;
import com.yiling.settlement.report.dto.request.QueryReportDetailB2bPageByReportIdRequest;
import com.yiling.settlement.report.dto.request.QueryReportDetailFlowPageRequest;
import com.yiling.settlement.report.dto.request.QueryReportPageRequest;
import com.yiling.settlement.report.enums.ReportOrderIdenEnum;
import com.yiling.settlement.report.enums.ReportOrderaAnormalReasonEnum;
import com.yiling.settlement.report.enums.ReportPurchaseChannelEnum;
import com.yiling.settlement.report.enums.ReportRebateStatusEnum;
import com.yiling.settlement.report.enums.ReportStatusEnum;
import com.yiling.settlement.report.enums.ReportTypeEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-11-04
 */
@Service("reportExportServiceImpl")
@Slf4j
public class ReportExportServiceImpl implements BaseExportQueryDataService<ExportReportRequest> {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    ReportApi reportApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    DictApi dictApi;
    @DubboReference
    CustomerApi customerApi;


    private static final LinkedHashMap<String, String> FIELD_SHEET = new LinkedHashMap<String, String>() {

        {
            put("eid", "商业id");
            put("eName", "商业名称");
            put("typeStr", "报表类型");
            put("memberAmount", "会员返利金额");
            put("salesAmount", "销售额返利");
            put("terminalSalesAmount", "终端促销返利");
            put("ladderAmount", "阶梯返利金额");
            put("xsyAmount", "小三员奖励金额");
            put("actFirstAmount", "特殊活动1金额");
            put("actSecondAmount", "特殊活动2金额");
            put("actThirdAmount", "特殊活动3金额");
            put("actFourthAmount", "特殊活动4金额");
            put("actFifthAmount", "特殊活动5金额");
            put("adjustAmount", "调整金额");
            put("adjustReason", "调整原因");
            put("totalAmount", "合计金额");
            put("rejectReason", "驳回原因");
            put("statusStr", "报表状态");
            put("createTime", "创建时间");
            put("updateUserName", "修改人");
            put("updateTime", "修改时间");
            put("rebateAmount", "订单返利金额");
            put("rebateStatusStr", "订单返利状态");

        }
    };

    private static final LinkedHashMap<String, String> FIELD_SHEET2 = new LinkedHashMap<String, String>() {

        {
            put("reportId", "报表id");
            put("id", "报表明细id");
            put("typeStr", "报表类型");
            put("orderId", "订单id");
            put("orderNo", "订单编号");
            put("eid", "商业id");
            put("eName", "商业名称");
            put("erpCustomerName", "ERP客户名称");
            put("paymentMethodStr", "支付方式");
            put("goodsId", "商品ID");
            put("goodsType", "商品类型");
            put("goodsName", "商品名称");
            put("specifications", "规格");
            put("license", "批准文号");
            put("batchNo", "批次号");
            put("expiryDate", "有效期");
            put("goodsQuantity", "购买数量");
            put("deliveryQuantity", "发货数量");
            put("refundQuantity", "退货数量");
            put("receiveQuantity", "收货数量");
            put("supplyPrice", "供货价");
            put("outPrice", "出货价");
            put("merchantSalePrice", "商销价");
            put("originalPrice", "商品原价");
            put("goodsPrice", "商品单价");
            put("goodsAmount", "商品小计");
            put("discountAmount", "批次签收总折扣金额");
            put("discountPercentage", "总折扣比例");
            put("paymentAmount", "支付金额");
            put("activityType", "活动类型");
            put("activityDescribe", "活动内容");
            put("platformPercentage", "平台承担折扣比例");
            put("platformAmount", "批次签收平台承担折扣金额");
            put("shopPercentage", "商家承担折扣比例");
            put("shopAmount", "批次签收商家承担折扣金额");
            put("purchaseChannelStr", "购进渠道");
            put("syncPurChannel", "动销渠道");
            put("salesAmount", "销售额返利金额");
            put("thresholdCount", "阶梯数量");
            put("ladderName", "阶梯活动名称");
            put("ladderAmount", "阶梯返利金额");
            put("xsyName", "小三元活动名称");
            put("xsyAmount", "小三员奖励金额");
            put("actXsyCycle", "小三员周期");
            put("actFirstName", "特殊活动1名称");
            put("actFirstAmount", "特殊活动1金额");
            put("actFirstCycle", "特殊活动1周期");
            put("actSecondName", "特殊活动2名称");
            put("actSecondAmount", "特殊活动2金额");
            put("actSecondCycle", "特殊活动2周期");
            put("actThirdName", "特殊活动3名称");
            put("actThirdAmount", "特殊活动3金额");
            put("actThirdCycle", "特殊活动3周期");
            put("actFourthName", "特殊活动4名称");
            put("actFourthAmount", "特殊活动4金额");
            put("actFourthCycle", "特殊活动4周期");
            put("actFifthName", "特殊活动5名称");
            put("actFifthAmount", "特殊活动5金额");
            put("actFifthCycle", "特殊活动5周期");
            put("soNo", "Erp销售订单号");
            put("soSourceStr", "订单来源");
            put("soTime", "销售日期");
            put("enterpriseInnerCode", "客户编码");
            put("enterpriseName", "客户名称");
            put("soUnit", "商品单位");
            put("soManufacturer", "商品生产厂家");
            put("goodsInSn", "商品内码");
            put("provinceName", "所属省份名称");
            put("cityName", "所属城市名称");
            put("regionName", "所属区域名称");
            put("ylGoodsId", "对应以岭的商品id");
            put("ylGoodsName", "以岭商品名称");
            put("ylGoodsSpecification", "以岭商品规格");
            put("identificationStatusStr", "标识状态");
            put("abnormalReasonStr", "异常原因");
            put("abnormalDescribed", "异常描述");
            put("rebateStatusStr", "返利状态");
        }
    };


    @Override
    public QueryExportDataDTO queryData(ExportReportRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        QueryReportPageRequest queryRequest;
        //如果根据选中进行导出
        if (StrUtil.isNotBlank(request.getReportIdList())) {
            queryRequest = new QueryReportPageRequest();
            queryRequest.setReportIdList(Arrays.stream(StrUtil.splitToLong(request.getReportIdList(), ",")).boxed().collect(Collectors.toList()));
        } else {
            //如果根据筛选条件进行导出
            queryRequest = PojoUtils.map(request, QueryReportPageRequest.class);
            queryRequest.setStatus(Arrays.stream(StrUtil.splitToInt(request.getReportStatusList(), ",")).boxed().collect(Collectors.toList()));
            queryRequest.setType(request.getType());

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
        }


        //不同sheet数据
        List<Map<String, Object>> reportData = new ArrayList<>();
        List<Map<String, Object>> reportDetailData = new ArrayList<>();


        int mainCurrent = 1;
        Page<ReportDTO> mainPage;
        //分页查询结算单列表
        do {

            queryRequest.setCurrent(mainCurrent);
            queryRequest.setSize(50);
            //查询结算单
            mainPage = reportApi.queryReportPage(queryRequest);

            List<ReportDTO> mainRecords = mainPage.getRecords();

            if (CollUtil.isEmpty(mainRecords)) {
                break;
            }
            //b2b报表明细
            List<Long> b2bReportIdList = mainRecords.stream().filter(e -> ObjectUtil.equal(ReportTypeEnum.B2B.getCode(), e.getType())).map(ReportDTO::getId).collect(Collectors.toList());
            //流向报表明细
            List<Long> flowReportIdList = mainRecords.stream().filter(e -> ObjectUtil.equal(ReportTypeEnum.FLOW.getCode(), e.getType())).map(ReportDTO::getId).collect(Collectors.toList());

            //查询报表明细
            Map<Long, List<ReportDetailB2bDTO>> b2bReportDetail = queryB2bReportDetail(b2bReportIdList);
            Map<Long, List<ReportDetailFlowDTO>> flowReportDetail = queryFlowReportDetail(flowReportIdList);

            //查询供应商信息
            List<Long> eidIdList = mainRecords.stream().map(ReportDTO::getEid).collect(Collectors.toList());
            eidIdList = eidIdList.stream().distinct().collect(Collectors.toList());
            //企业map
            Map<Long, String> entDTOMap = enterpriseApi.listByIds(eidIdList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
            List<Long> userIdList = mainRecords.stream().map(ReportDTO::getUpdateUser).distinct().collect(Collectors.toList());
            Map<Long, String> userMap = userApi.listByIds(userIdList).stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));


            //生成sheet1&2
            mainRecords.forEach(report -> {
                ExportReportBO var1 = PojoUtils.map(report, ExportReportBO.class);
                var1.setEName(entDTOMap.getOrDefault(report.getEid(), ""));
                var1.setTypeStr(ReportTypeEnum.getByCode(var1.getType()).getName());
                var1.setStatusStr(ReportStatusEnum.getByCode(var1.getStatus()).getName());
                var1.setUpdateUserName(userMap.getOrDefault(report.getUpdateUser(), ""));
                if (ObjectUtil.isNotNull(report.getRebateAmount())){
                    var1.setRebateAmount(report.getRebateAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                if (ObjectUtil.isNotNull(ReportRebateStatusEnum.getByCode(var1.getRebateStatus()))) {
                    var1.setRebateStatusStr(ReportRebateStatusEnum.getByCode(var1.getRebateStatus()).getName());
                }
                Map<String, Object> dataPojo = BeanUtil.beanToMap(var1);
                reportData.add(dataPojo);
                List<ExportReportDetailBO> sheet2;
                //生成sheet2
                if (ObjectUtil.equal(var1.getType(), ReportTypeEnum.B2B.getCode())) {
                    sheet2 = getDetailByB2b(b2bReportDetail.get(report.getId()), report);
                } else {
                    sheet2 = getDetailByFlow(flowReportDetail.get(report.getId()), report);
                }


                sheet2.forEach(e -> {
                    //设置公有字段
                    e.setEid(report.getEid());
                    e.setEName(entDTOMap.getOrDefault(report.getEid(), ""));

                    Map<String, Object> dataPojo2 = BeanUtil.beanToMap(e);
                    reportDetailData.add(dataPojo2);
                });
            });

            mainCurrent = mainCurrent + 1;
        } while (mainPage != null && CollectionUtils.isNotEmpty(mainPage.getRecords()));

        ExportDataDTO exportReportDTO = new ExportDataDTO();
        exportReportDTO.setSheetName("返利报表单");

        //如果是非以岭导出，删除结算单备注字段
        exportReportDTO.setFieldMap(FIELD_SHEET);
        // 页签数据
        exportReportDTO.setData(reportData);

        ExportDataDTO exportReportDetailDTO = new ExportDataDTO();
        exportReportDetailDTO.setSheetName("报表明细");
        // 页签字段
        exportReportDetailDTO.setFieldMap(FIELD_SHEET2);
        // 页签数据
        exportReportDetailDTO.setData(reportDetailData);


        //封装excel
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportReportDTO);
        sheets.add(exportReportDetailDTO);
        result.setSheets(sheets);
        return result;
    }

    @Override
    public ExportReportRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, ExportReportRequest.class);
    }

    /**
     * 查询b2b报表明细
     *
     * @param reportIds
     * @return
     */
    private Map<Long, List<ReportDetailB2bDTO>> queryB2bReportDetail(List<Long> reportIds) {
        Map<Long, List<ReportDetailB2bDTO>> result = MapUtil.newHashMap();
        if (CollUtil.isEmpty(reportIds)) {
            return result;
        }

        QueryReportDetailB2bPageByReportIdRequest queryRequest = new QueryReportDetailB2bPageByReportIdRequest();
        queryRequest.setReportIdList(reportIds);

        ArrayList<ReportDetailB2bDTO> list = ListUtil.toList();

        int mainCurrent = 1;
        Page<ReportDetailB2bDTO> mainPage;
        //分页查询结算单列表
        do {

            queryRequest.setCurrent(mainCurrent);
            queryRequest.setSize(100);
            //查询结算单
            mainPage = reportApi.queryReportDetailB2bPageByReportId(queryRequest);

            List<ReportDetailB2bDTO> mainRecords = mainPage.getRecords();
            list.addAll(mainRecords);

            mainCurrent = mainCurrent + 1;
        } while (mainPage != null && CollectionUtils.isNotEmpty(mainPage.getRecords()));
        if (CollUtil.isEmpty(list)) {
            return result;
        }
        result = list.stream().collect(Collectors.groupingBy(ReportDetailB2bDTO::getReportId));
        return result;
    }

    /**
     * 查询流向报表明细
     *
     * @param reportIds
     * @return
     */
    private Map<Long, List<ReportDetailFlowDTO>> queryFlowReportDetail(List<Long> reportIds) {

        Map<Long, List<ReportDetailFlowDTO>> result = MapUtil.newHashMap();

        if (CollUtil.isEmpty(reportIds)) {
            return result;
        }

        QueryReportDetailFlowPageRequest queryRequest = new QueryReportDetailFlowPageRequest();
        queryRequest.setReportIdList(reportIds);

        ArrayList<ReportDetailFlowDTO> list = ListUtil.toList();

        int mainCurrent = 1;
        Page<ReportDetailFlowDTO> mainPage;
        //分页查询结算单列表
        do {

            queryRequest.setCurrent(mainCurrent);
            queryRequest.setSize(100);
            //查询结算单
            mainPage = reportApi.queryReportDetailFlowPage(queryRequest);

            List<ReportDetailFlowDTO> mainRecords = mainPage.getRecords();
            list.addAll(mainRecords);

            mainCurrent = mainCurrent + 1;
        } while (mainPage != null && CollectionUtils.isNotEmpty(mainPage.getRecords()));
        if (CollUtil.isEmpty(list)) {
            return result;
        }
        result = list.stream().collect(Collectors.groupingBy(ReportDetailFlowDTO::getReportId));
        return result;
    }

    /**
     * 根据b2b明细生成sheet
     *
     * @param b2bDTOList
     * @param report
     * @return
     */
    private List<ExportReportDetailBO> getDetailByB2b(List<ReportDetailB2bDTO> b2bDTOList, ReportDTO report) {
        if (CollUtil.isEmpty(b2bDTOList)){
            return ListUtil.toList();
        }
        List<ExportReportDetailBO> list = PojoUtils.map(b2bDTOList, ExportReportDetailBO.class);
        //查询企业信息
        List<Long> eidList = b2bDTOList.stream().map(ReportDetailB2bDTO::getSellerEid).collect(Collectors.toList());
        eidList.addAll(b2bDTOList.stream().map(ReportDetailB2bDTO::getBuyerEid).collect(Collectors.toList()));
        eidList = eidList.stream().distinct().collect(Collectors.toList());

        Map<Long, EnterpriseDTO> entMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(EnterpriseDTO::getId, e -> e));

        list.forEach(e -> {
            e.setTypeStr(ReportTypeEnum.getByCode(report.getType()).getName());
            e.setPurchaseChannelStr(ReportPurchaseChannelEnum.getByCode(e.getPurchaseChannel()).getName());
            e.setPaymentMethodStr(PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue()).getName());
            e.setActXsyCycle(getDateStr(e.getXsyStartTime(), e.getXsyEndTime()));
            e.setActFirstCycle(getDateStr(e.getActFirstStartTime(), e.getActFirstEndTime()));
            e.setActSecondCycle(getDateStr(e.getActSecondStartTime(), e.getActSecondEndTime()));
            e.setActThirdCycle(getDateStr(e.getActThirdStartTime(), e.getActThirdEndTime()));
            e.setActFourthCycle(getDateStr(e.getActFourthStartTime(), e.getActFourthEndTime()));
            e.setActFifthCycle(getDateStr(e.getActFifthStartTime(), e.getActFifthEndTime()));
            e.setSoSourceStr(OrderSourceEnum.getByCode(e.getOrderSource()).getName());
            e.setSoTime(e.getOrderCreateTime());
            e.setEnterpriseInnerCode(e.getBuyerEid().toString());
            e.setGoodsInSn(e.getGoodsErpCode());
            e.setSoManufacturer(e.getGoodsManufacturer());
            e.setEnterpriseName(entMap.get(e.getBuyerEid()).getName());
            e.setProvinceName(entMap.get(e.getBuyerEid()).getProvinceName());
            e.setCityName(entMap.get(e.getBuyerEid()).getCityName());
            e.setRegionName(entMap.get(e.getBuyerEid()).getRegionName());
            EnterpriseCustomerDTO customerDTO = customerApi.get(e.getSellerEid(), e.getBuyerEid());
            if (ObjectUtil.isNotNull(customerDTO)){
                e.setErpCustomerName(customerDTO.getCustomerName());
            }
            if (ObjectUtil.isNotNull(ReportOrderIdenEnum.getByCode(e.getIdentificationStatus()))) {
                e.setIdentificationStatusStr(ReportOrderIdenEnum.getByCode(e.getIdentificationStatus()).getName());
            }
            if (ObjectUtil.isNotNull(ReportOrderaAnormalReasonEnum.getByCode(e.getAbnormalReason()))) {
                e.setAbnormalReasonStr(ReportOrderaAnormalReasonEnum.getByCode(e.getAbnormalReason()).getName());
            }
            if (ObjectUtil.isNotNull(ReportRebateStatusEnum.getByCode(e.getRebateStatus()))) {
                e.setRebateStatusStr(ReportRebateStatusEnum.getByCode(e.getRebateStatus()).getName());
            }
            if (e.getOrderGoodsType() == 1) {
                e.setYlGoodsId(e.getGoodsId());
                e.setYlGoodsName(e.getGoodsName());
                e.setYlGoodsSpecification(e.getSpecifications());
            }
        });
        return list;
    }

    /**
     * 根据流向明细生成sheet
     *
     * @param flowDTOList
     * @param report
     * @return
     */
    private List<ExportReportDetailBO> getDetailByFlow(List<ReportDetailFlowDTO> flowDTOList, ReportDTO report) {

        if (CollUtil.isEmpty(flowDTOList)){
            return ListUtil.toList();
        }
        //查询字典
        DictBO dict = dictApi.getDictByName("erp_sale_flow_source");
        Map<String, String> dictMap = dict.getDataList().stream().collect(Collectors.toMap(DictBO.DictData::getValue, DictBO.DictData::getLabel));

        List<ExportReportDetailBO> list = ListUtil.toList();
        flowDTOList.forEach(e -> {
            ExportReportDetailBO var = PojoUtils.map(e, ExportReportDetailBO.class);
            var.setLicense(e.getSoLicense());
            var.setExpiryDate(e.getSoEffectiveTime());
            var.setGoodsAmount(e.getSoTotalAmount());
            var.setGoodsPrice(e.getSoPrice());
            var.setGoodsQuantity(e.getSoQuantity().intValue());
            var.setBatchNo(e.getSoBatchNo());
            var.setSpecifications(e.getSoSpecifications());
            var.setSoSourceStr(dictMap.get(e.getSoSource()));
            var.setPurchaseChannelStr(ReportPurchaseChannelEnum.getByCode(e.getPurchaseChannel()).getName());
            var.setTypeStr(ReportTypeEnum.getByCode(report.getType()).getName());
            var.setActXsyCycle(getDateStr(e.getXsyStartTime(), e.getXsyEndTime()));
            var.setActFirstCycle(getDateStr(e.getActFirstStartTime(), e.getActFirstEndTime()));
            var.setActSecondCycle(getDateStr(e.getActSecondStartTime(), e.getActSecondEndTime()));
            var.setActThirdCycle(getDateStr(e.getActThirdStartTime(), e.getActThirdEndTime()));
            var.setActFourthCycle(getDateStr(e.getActFourthStartTime(), e.getActFourthEndTime()));
            var.setActFifthCycle(getDateStr(e.getActFifthStartTime(), e.getActFifthEndTime()));
            if (ObjectUtil.isNotNull(ReportOrderIdenEnum.getByCode(e.getIdentificationStatus()))) {
                var.setIdentificationStatusStr(ReportOrderIdenEnum.getByCode(e.getIdentificationStatus()).getName());
            }
            if (ObjectUtil.isNotNull(ReportOrderaAnormalReasonEnum.getByCode(e.getAbnormalReason()))) {
                var.setAbnormalReasonStr(ReportOrderaAnormalReasonEnum.getByCode(e.getAbnormalReason()).getName());
            }
            if (ObjectUtil.isNotNull(ReportRebateStatusEnum.getByCode(e.getRebateStatus()))) {
                var.setRebateStatusStr(ReportRebateStatusEnum.getByCode(e.getRebateStatus()).getName());
            }
            list.add(var);
        });
        return list;
    }

    private String getDateStr(Date date1, Date date2) {
        if (DateUtil.compare(date1, DateUtil.parseDate("1970-01-01 00:00:00")) == 0 || DateUtil.compare(date2, DateUtil.parseDate("1970-01-01 00:00:00")) == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DateUtil.format(date1, "yyyy-MM-dd"));
        stringBuilder.append("~");
        stringBuilder.append(DateUtil.format(date2, "yyyy-MM-dd"));
        return stringBuilder.toString();
    }
}
