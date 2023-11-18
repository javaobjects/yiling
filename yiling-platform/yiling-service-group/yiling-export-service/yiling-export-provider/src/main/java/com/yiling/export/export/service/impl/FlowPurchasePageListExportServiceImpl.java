package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.bo.FlowPermissionsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.export.export.bo.ExportFlowPurchasePageListBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.export.export.util.FlowUtil;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseTagApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 采购流向导出
 *
 * @author: houjie.sun
 * @date: 2022/2/16
 */
@Service("flowPurchasePageListExportService")
public class FlowPurchasePageListExportServiceImpl implements BaseExportQueryDataService<QueryFlowPurchaseListPageRequest> {

    @DubboReference(timeout = 60 * 1000)
    FlowPurchaseApi flowPurchaseApi;
    @DubboReference
    EnterpriseApi                                      enterpriseApi;
    @DubboReference
    DataPermissionsApi                                 dataPermissionsApi;
    @DubboReference
    DictApi dictApi;
    @DubboReference
    EnterpriseTagApi enterpriseTagApi;
    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;

    private static final String PURCHASE_FLOW_SOURCE = "erp_purchase_flow_source";

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("eid", "商业ID");
        FIELD.put("ename", "商业名称");
        FIELD.put("poNo", "采购订单号");
        FIELD.put("poSource", "订单来源");
        FIELD.put("poTime", "采购日期");
        FIELD.put("goodsName", "商品名称");
        FIELD.put("poSpecifications", "规格");
        FIELD.put("poBatchNo", "批号");
        FIELD.put("enterpriseInnerCode", "供应商编码");
        FIELD.put("enterpriseName", "供应商名称");
        FIELD.put("poQuantity", "入库数量");
        FIELD.put("poUnit", "单位");
        FIELD.put("poPrice", "单价");
        FIELD.put("poTotalAmount", "金额");
        FIELD.put("poManufacturer", "生产厂家");
        FIELD.put("poProductTime", "生产日期");
        FIELD.put("poEffectiveTime", "有效日期");
        FIELD.put("goodsInSn", "商品内码");
        FIELD.put("poLicense", "批准文号");
        FIELD.put("poSource", "订单来源");
        FIELD.put("provinceName", "省");
        FIELD.put("cityName", "市");
        FIELD.put("regionName", "区");
        FIELD.put("standardName", "标准商品名称");
        FIELD.put("standardSellSpecifications", "标准商品规格");
    }

    @Override
    public QueryExportDataDTO queryData(QueryFlowPurchaseListPageRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        // 需要循环调用
        request.setEid(null);

        if (request.getTimeType().equals(1) && ObjectUtil.isNull(request.getEndTime())) {
            // 选择6个月以前的查询，则只会传开始时间
            Date startTime = request.getStartTime();
            Date endTime = DateUtil.endOfMonth(startTime);  // 获取当月结束时间
            request.setEndTime(endTime);

            Integer year = DateUtil.year(request.getStartTime()); // 获取年份
            request.setSelectYear(year);
        }

        List<Map<String, Object>> data = new ArrayList<>();
        Page<ExportFlowPurchasePageListBO> page;
        int current = 1;
        int size = 500;

        // erp采购流向订单来源
        Map<String,String> dictDataMap = getDictDataMap();

        do {
            if(StrUtil.isBlank(request.getEname()) && StrUtil.isBlank(request.getProvinceCode()) && StrUtil.isBlank(request.getCityCode()) && StrUtil.isBlank(request.getRegionCode())
                    && StrUtil.isBlank(request.getEnterpriseName()) && StrUtil.isBlank(request.getGoodsName()) && ObjectUtil.isNull(request.getSpecificationId()) && StrUtil.isBlank(request.getLicense())
                    && ObjectUtil.isNull(request.getStartTime()) && ObjectUtil.isNull(request.getEndTime()) && CollUtil.isEmpty(request.getSourceList())
                    && CollUtil.isEmpty(request.getEnterpriseTagIdList()) && ObjectUtil.isNull(request.getSpecificationIdFlag())){
                break;
            }
            if (CollUtil.isEmpty(request.getEidList())) {
                break;
            }
            request.setCurrent(current);
            request.setSize(size);
            page = PojoUtils.map(flowPurchaseApi.page(request), ExportFlowPurchasePageListBO.class);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            List<ExportFlowPurchasePageListBO> records = page.getRecords();
            List<Long> specificationIdList = records.stream().filter(o -> ObjectUtil.isNotNull(o.getSpecificationId()) && o.getSpecificationId().intValue() != 0)
                    .map(ExportFlowPurchasePageListBO::getSpecificationId).distinct().collect(Collectors.toList());
            // 规格
            Map<Long, StandardGoodsSpecificationDTO> standardGoodsMap = getStandardGoodsSpecification(specificationIdList);
            // 总金额
            for (ExportFlowPurchasePageListBO record : records) {
                buildFlowPurchasePageVo(record, dictDataMap, standardGoodsMap);

                data.add(BeanUtil.beanToMap(record));
            }
            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("采购流向导出");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据
        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }

    private Map<Long, StandardGoodsSpecificationDTO> getStandardGoodsSpecification(List<Long> specificationIdList) {
        Map<Long, StandardGoodsSpecificationDTO> standardGoodsMap = new HashMap<>();
        if (CollUtil.isEmpty(specificationIdList)) {
            return standardGoodsMap;
        }
        List<StandardGoodsSpecificationDTO> standardGoodsList = standardGoodsSpecificationApi.getListStandardGoodsSpecificationByIds(specificationIdList);
        if (CollUtil.isNotEmpty(standardGoodsList)) {
            standardGoodsMap = standardGoodsList.stream().collect(Collectors.toMap(StandardGoodsSpecificationDTO::getId, Function.identity(), (k1, k2) -> k2));
        }
        return standardGoodsMap;
    }

    private void buildFlowPurchasePageVo(ExportFlowPurchasePageListBO record, Map<String,String> dictDataMap, Map<Long, StandardGoodsSpecificationDTO> standardGoodsMap){
        BigDecimal totalAmount = record.getPoQuantity().multiply(record.getPoPrice());
        record.setPoTotalAmount(totalAmount.setScale(4, BigDecimal.ROUND_HALF_UP));
        record.setPoQuantity(FlowUtil.deimalcFormat(record.getPoQuantity()));
        // 去除空格
        record.setGoodsName(record.getGoodsName().trim());
        record.setPoSpecifications(record.getPoSpecifications().trim());
        record.setPoLicense(record.getPoLicense().trim());
        record.setPoManufacturer(record.getPoManufacturer().replace(" ",""));
        record.setPoBatchNo(record.getPoBatchNo().trim());
        record.setPoUnit(record.getPoUnit().trim());
        record.setGoodsInSn(record.getGoodsInSn().trim());
        // 订单来源
        String source = "其它渠道采购";
        String label = dictDataMap.get(record.getPoSource());
        if(StrUtil.isNotBlank(label)){
            source = label;
        }
        record.setPoSource(source);
        // 规格id
        Long specificationId = record.getSpecificationId();
        if (ObjectUtil.isNotNull(specificationId) && specificationId.intValue() != 0) {
            // 规格
            StandardGoodsSpecificationDTO standardGoodsSpecificationDTO = standardGoodsMap.get(specificationId);
            if (ObjectUtil.isNotNull(standardGoodsSpecificationDTO)) {
                record.setStandardName(standardGoodsSpecificationDTO.getName());
                record.setStandardSellSpecifications(standardGoodsSpecificationDTO.getSellSpecifications());
            }
        }
    }

    @Override
    public QueryFlowPurchaseListPageRequest getParam(Map<String, Object> map) {
        QueryFlowPurchaseListPageRequest request = PojoUtils.map(map, QueryFlowPurchaseListPageRequest.class);
        if(ObjectUtil.isNotNull(map.get("sellSpecificationsId"))){
            request.setSpecificationId(Long.valueOf(map.get("sellSpecificationsId").toString()));
        }
        if(ObjectUtil.isNotNull(map.get("specificationIdFlag"))){
            request.setSpecificationIdFlag(Integer.parseInt(map.get("specificationIdFlag").toString()));
        }
        Long currentEid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        Long currentUserId = Long.parseLong(map.getOrDefault("currentUserId", 0L).toString());

        List<Long> eidList = new ArrayList<>();
        // 导出来源:1-运营后台，2-商家后台
        String menuSource = null;
        if(ObjectUtil.isNotNull(map.get("menuSource"))){
            menuSource = map.get("menuSource").toString();
        }
        if(ObjectUtil.equal("1", menuSource)){
            // 1-运营后台，无数据权限
            eidList = getEidListByNameProvinceCityRegion(request.getEname(), request.getProvinceCode(), request.getCityCode(), request.getRegionCode());
        } else {
            // 2-商家后台，需要数据权限
            // 权限校验，获取当前用户负责的公司
            Map<Long, List<EnterpriseDTO>> contactUserMap = contactUserMap(currentEid, currentUserId);
            if (MapUtil.isNotEmpty(contactUserMap) && CollUtil.isNotEmpty(contactUserMap.get(currentUserId)) && contactUserMap.get(currentUserId).get(0).getId() == -99L) {
                eidList = getEidListByNameProvinceCityRegion(request.getEname(), request.getProvinceCode(), request.getCityCode(), request.getRegionCode());
            } else {
                FlowPermissionsBO flowPermissionsBO = PojoUtils.map(request, FlowPermissionsBO.class);
                eidList = FlowUtil.getResponsibleEidList(currentUserId, contactUserMap, flowPermissionsBO);
            }
        }
        // 企业标签筛选
        handlerEnterpriseTagIdList(map, request, eidList);
        request.setEidList(eidList);
        // 订单来源筛选
        handlerSourceList(map, request);
        return request;
    }


    private void handlerEnterpriseTagIdList(Map<String, Object> map, QueryFlowPurchaseListPageRequest request, List<Long> eidList) {
        if(CollUtil.isEmpty(eidList)){
            return;
        }
        String enterpriseTagId = map.getOrDefault("enterpriseTagId", "").toString();
        if(StrUtil.isBlank(enterpriseTagId)){
            return;
        }
        List<Long> enterpriseTagIdList = new ArrayList<>();
        if(enterpriseTagId.contains(",")){
            enterpriseTagIdList = Convert.toList(Long.class, enterpriseTagId);
        } else {
            enterpriseTagIdList.add(Long.parseLong(enterpriseTagId));
        }
        request.setEnterpriseTagIdList(enterpriseTagIdList);
        List<Long> eidListNew = filterEidTagList(eidList, request.getEnterpriseTagIdList());
        if(CollUtil.isEmpty(eidListNew)){
            eidList.removeAll(eidList);
        } else {
            eidList.removeAll(eidList);
            eidList.addAll(eidListNew);
        }
    }

    private void handlerSourceList(Map<String, Object> map, QueryFlowPurchaseListPageRequest request) {
        String source = map.getOrDefault("source", "").toString();
        if(StrUtil.isNotBlank(source)){
            List<String> sourceList = new ArrayList<>();
            if(source.contains(",")){
                sourceList = Convert.toList(String.class, source);
            } else {
                sourceList.add(source);
            }
            request.setSourceList(sourceList);
        }
    }

    /**
     * 企业标签筛选
     *
     * @param eidList 企业id列表
     * @param enterpriseTagIdList 企业标签id列表
     * @return
     */
    private List<Long> filterEidTagList(List<Long> eidList, List<Long> enterpriseTagIdList){
        if(CollUtil.isEmpty(eidList)){
            return ListUtil.empty();
        }
        if(CollUtil.isEmpty(enterpriseTagIdList)){
            return eidList;
        }
        // 根据标签idl列表 查询企业id列表
        List<Long> eidTagList = enterpriseTagApi.getEidListByTagIdList(enterpriseTagIdList);
        if(CollUtil.isEmpty(eidTagList)){
            return ListUtil.empty();
        }
        return eidList.stream().filter(eid -> eidTagList.contains(eid)).distinct().collect(Collectors.toList());
    }

    private Map<String, String> getDictDataMap() {
        Map<String,String> dictDataMap = new HashMap<>();
        DictBO dict = dictApi.getDictByName(PURCHASE_FLOW_SOURCE);
        if(ObjectUtil.isNotNull(dict)){
            List<DictBO.DictData> dataList = dict.getDataList();
            dictDataMap = dataList.stream().collect(Collectors.toMap(d -> d.getValue(), d -> d.getLabel(), (k1,k2) -> k1));
        }
        return dictDataMap;
    }

    /**
     * 校验用户权限
     *
     * @param currentEid
     * @param currentUserId
     * @return
     */
    private Map<Long, List<EnterpriseDTO>> contactUserMap(Long currentEid, Long currentUserId) {
        // 当前用户权限
        List<Long> userIdList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, Constants.YILING_EID, currentUserId);
        if(CollUtil.isNotEmpty(userIdList) && !userIdList.contains(currentUserId)){
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        Map<Long, List<EnterpriseDTO>> contactUserMap = new HashMap<>();
        if(CollUtil.isEmpty(userIdList)){
            EnterpriseDTO enterpriseDTO = new EnterpriseDTO();
            enterpriseDTO.setId(-99L);
            contactUserMap.put(currentUserId, new ArrayList(){{add(enterpriseDTO);}});
            return contactUserMap;
        }
        // 当前用户负责的企业
        contactUserMap = enterpriseApi.listByContactUserIds(Constants.YILING_EID, userIdList);
        return contactUserMap;
    }

    private List<EnterpriseDTO> getEnterpriseListByName(String name){
        QueryEnterpriseByNameRequest request = new QueryEnterpriseByNameRequest();
        request.setName(name);
        request.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(),EnterpriseTypeEnum.CHAIN_BASE.getCode(),EnterpriseTypeEnum.CHAIN_DIRECT.getCode(),EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
        return enterpriseApi.getEnterpriseListByName(request);
    }

    private List<Long> getEidListByNameProvinceCityRegion(String name, String provinceCode, String cityCode, String regionCode){
        List<EnterpriseDTO> enterpriseList = new ArrayList<>();
        List<EnterpriseDTO> enterpriseNameList = null;
        List<EnterpriseDTO> enterpriseRegionList = null;
        boolean nameFlag = false;
        boolean regionFlag = false;

        List<Integer> inTypeList = new ArrayList<>();
        inTypeList.add(EnterpriseTypeEnum.BUSINESS.getCode());

        if(StrUtil.isNotBlank(name)){
            nameFlag = true;
            enterpriseNameList = getEnterpriseListByName(name);
        }
        if(StrUtil.isNotBlank(provinceCode) || StrUtil.isNotBlank(cityCode) || StrUtil.isNotBlank(regionCode)){
            regionFlag = true;
            QueryEnterpriseListRequest eRequest = new QueryEnterpriseListRequest();
            eRequest.setProvinceCode(provinceCode);
            eRequest.setCityCode(cityCode);
            eRequest.setRegionCode(regionCode);
            eRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            eRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
            eRequest.setTypeList(inTypeList);
            enterpriseRegionList = enterpriseApi.queryListByArea(eRequest);
        }

        if(!nameFlag && !regionFlag){
            // 查询所有POP商业类型的企业信息
            return getAllBusinessEnterprise(inTypeList);
        } else if(nameFlag && regionFlag){
            if(CollUtil.isNotEmpty(enterpriseNameList) && CollUtil.isNotEmpty(enterpriseRegionList)){
                List<Long> eidListTemp = enterpriseRegionList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
                enterpriseList = enterpriseNameList.stream().filter(o -> eidListTemp.contains(o.getId())).collect(Collectors.toList());
            }
        } else {
            if(CollUtil.isNotEmpty(enterpriseNameList)){
                enterpriseList.addAll(enterpriseNameList);
            }
            if(CollUtil.isNotEmpty(enterpriseRegionList)){
                enterpriseList.addAll(enterpriseRegionList);
            }
        }

        if(CollUtil.isNotEmpty(enterpriseList)){
            return enterpriseList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
        }
        return ListUtil.empty();
    }

    private List<Long> getAllBusinessEnterprise(List<Integer> inTypeList) {
        List<Long> eidList = new ArrayList<>();
        Page<EnterpriseDTO> enterpriseDTOPage;
        int current = 1;
        int size = 500;
        do {
            QueryEnterprisePageListRequest enterprisePageListRequest = new QueryEnterprisePageListRequest();
            enterprisePageListRequest.setCurrent(current);
            enterprisePageListRequest.setSize(size);
            enterprisePageListRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            enterprisePageListRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
            // 仅能添加开通POP、商业类型的
            enterprisePageListRequest.setPopFlag(1);
            enterprisePageListRequest.setInTypeList(inTypeList);

            enterpriseDTOPage = enterpriseApi.pageList(enterprisePageListRequest);
            if (ObjectUtil.isNull(enterpriseDTOPage) || CollUtil.isEmpty(enterpriseDTOPage.getRecords())) {
                break;
            }
            List<EnterpriseDTO> records = enterpriseDTOPage.getRecords();
            for (EnterpriseDTO record : records) {
                eidList.add(record.getId());
            }
            current = current + 1;
        } while (enterpriseDTOPage != null && CollectionUtils.isNotEmpty(enterpriseDTOPage.getRecords()));
        return eidList;
    }

}
