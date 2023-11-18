package com.yiling.export.export.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.dataflow.order.api.FlowShopSaleApi;
import com.yiling.dataflow.order.dto.request.QueryFlowShopSaleListPageRequest;
import com.yiling.dataflow.relation.api.FlowGoodsRelationApi;
import com.yiling.export.export.bo.ExportFlowShopSalePageListBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.export.export.util.FlowUtil;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.user.enterprise.api.CustomerContactApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseTagApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
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
import lombok.extern.slf4j.Slf4j;

/**
 * 连锁纯销流向导出
 *
 * @author: houjie.sun
 * @date: 2023/4/12
 */
@Slf4j
@Service("flowShopSalePageListExportService")
public class FlowShopSalePageListExportServiceImpl implements BaseExportQueryDataService<QueryFlowShopSaleListPageRequest> {

    @DubboReference(timeout = 60 * 1000)
    FlowShopSaleApi flowShopSaleApi;
    @DubboReference(timeout = 60 * 1000)
    EnterpriseApi enterpriseApi;
    @DubboReference
    DataPermissionsApi dataPermissionsApi;
    @DubboReference
    DictApi dictApi;
    @DubboReference
    EnterpriseTagApi enterpriseTagApi;
    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;
    @DubboReference
    GoodsYilingPriceApi goodsYilingPriceApi;
    @DubboReference
    CustomerContactApi customerContactApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    FlowGoodsRelationApi flowGoodsRelationApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("eid", "总店企业ID");
        FIELD.put("ename", "总店企业名称");
        FIELD.put("shopEid", "门店企业ID");
        FIELD.put("shopEname", "门店企业名称");
        FIELD.put("shopNo", "门店编码");
        FIELD.put("soNo", "销售订单号");
        FIELD.put("soTimeStr", "销售日期");
        FIELD.put("goodsName", "商品名称");
        FIELD.put("soSpecifications", "规格");
        FIELD.put("soBatchNo", "批号");
        FIELD.put("soQuantity", "数量");
        FIELD.put("soUnit", "单位");
        FIELD.put("soPrice", "单价");
        FIELD.put("soTotalAmount", "金额");
        FIELD.put("soManufacturer", "生产厂家");
        FIELD.put("soProductTime", "生产日期");
        FIELD.put("soEffectiveTime", "有效日期");
        FIELD.put("goodsInSn", "商品内码");
        FIELD.put("soLicense", "批准文号");
        FIELD.put("provinceName", "省");
        FIELD.put("cityName", "市");
        FIELD.put("regionName", "区");
    }

    @Override
    public QueryExportDataDTO queryData(QueryFlowShopSaleListPageRequest request) {
//        log.info("商家后台连锁纯销导出, queryData request:{}", JSONUtil.toJsonStr(request));

        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        // 需要循环调用
        if (ObjectUtil.equals(1, request.getTimeType()) && ObjectUtil.isNull(request.getEndTime())) {
            // 选择6个月以前的查询，则只会传开始时间
            Date startTime = request.getStartTime();
            Date endTime = DateUtil.endOfMonth(startTime);  // 获取当月结束时间
            request.setEndTime(endTime);

            Integer year = DateUtil.year(request.getStartTime()); // 获取年份
            request.setSelectYear(year);
        }

        List<Map<String, Object>> data = new ArrayList<>();
        Page<ExportFlowShopSalePageListBO> page;
        int current = 1;
        int size = 500;
        do {
            if(ObjectUtil.isNull(request.getEid()) && ObjectUtil.isNull(request.getShopEid()) && StrUtil.isBlank(request.getProvinceCode()) && StrUtil.isBlank(request.getCityCode()) && StrUtil.isBlank(request.getRegionCode()) && StrUtil.isBlank(request.getGoodsName()) && StrUtil.isBlank(request.getLicense()) && ObjectUtil.isNull(request.getStartTime()) && ObjectUtil.isNull(request.getEndTime()) && StrUtil.isBlank(request.getManufacturer()) && CollUtil.isEmpty(request.getEnterpriseTagIdList())){
                log.info("商家后台连锁纯销导出, queryData 参数为空");
                break;
            }
            if (null == request.getEidList()) {
                log.info("商家后台连锁纯销导出, queryData eidList为空");
                break;
            }
            if (CollUtil.isNotEmpty(request.getEidList()) && ObjectUtil.isNotNull(request.getEid()) && 0 != request.getEid().intValue()) {
                if (!request.getEidList().contains(request.getEid())) {
                    log.info("商家后台连锁纯销导出, queryData 此企业无权限");
                    break;
                }
            }
            if (null == request.getShopEidList()) {
                log.info("商家后台连锁纯销导出, queryData shopEidList为空");
                break;
            }
            if (CollUtil.isNotEmpty(request.getShopEidList()) && ObjectUtil.isNotNull(request.getShopEid()) && 0 != request.getShopEid().intValue()) {
                if (!request.getShopEidList().contains(request.getShopEid())) {
                    log.info("商家后台连锁纯销导出, queryData 此门店不符合区域");
                    break;
                }
            }
            request.setCurrent(current);
            request.setSize(size);
            page = PojoUtils.map(flowShopSaleApi.page(request), ExportFlowShopSalePageListBO.class);
//            log.info("商家后台连锁纯销导出, queryData page:{}", JSONUtil.toJsonStr(page));
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }

            for (ExportFlowShopSalePageListBO record : page.getRecords()) {
                buildFlowShopSalePageVo(record);
                data.add(BeanUtil.beanToMap(record));
            }
            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));

        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("销售流向导出");
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
    public QueryFlowShopSaleListPageRequest getParam(Map<String, Object> map) {
        QueryFlowShopSaleListPageRequest request = PojoUtils.map(map, QueryFlowShopSaleListPageRequest.class);
        // 总店eid
        Long mainEid = Long.parseLong(map.getOrDefault("mainEid", 0L).toString());
        request.setEid(mainEid);
        // 登录用户信息
        Long currentEid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        Long currentUserId = Long.parseLong(map.getOrDefault("currentUserId", 0L).toString());

        // 导出来源:1-运营后台，2-商家后台
        String menuSource = null;
        if(ObjectUtil.isNotNull(map.get("menuSource"))){
            menuSource = map.get("menuSource").toString();
        }

        List<Long> eidList;
        if(ObjectUtil.equal("1", menuSource)){
            // 1-运营后台，不设数据权限
            eidList = new ArrayList<>();
        } else {
            // 2-商家后台，需要数据权限
            // 权限校验，获取当前用户负责的公司
            Map<Long, List<EnterpriseDTO>> contactUserMap = contactUserMap(currentEid, currentUserId);
            if (MapUtil.isNotEmpty(contactUserMap) && CollUtil.isNotEmpty(contactUserMap.get(currentUserId)) && contactUserMap.get(currentUserId).get(0).getId() == -99L) {
                eidList = new ArrayList<>();
            } else {
                eidList = getShopResponsibleEidList(currentUserId, contactUserMap);
            }
        }
        // 企业标签筛选
        eidList = handlerEnterpriseTagIdList(map, request, eidList);
//        log.info("商家后台连锁纯销导出, 当前用户:{}, eidList:{}", currentUserId, JSONUtil.toJsonStr(eidList));
        if (CollUtil.isNotEmpty(eidList)) {
            if (ObjectUtil.isNotNull(request.getEid()) && 0 != request.getEid().intValue() && eidList.contains(request.getEid())) {
                eidList.clear();
                eidList.add(request.getEid());
            }
        }
        request.setEidList(eidList);
        // 区域查询，门店
        List<Long> shopEidList = getShopEidListByShopProvinceCityRegion(request.getProvinceCode(), request.getCityCode(), request.getRegionCode());
//        log.info("商家后台连锁纯销导出, 当前用户:{}, eidList:{}", currentUserId, JSONUtil.toJsonStr(shopEidList));
        if (CollUtil.isNotEmpty(shopEidList)) {
            if (ObjectUtil.isNotNull(request.getShopEid()) && 0 != request.getShopEid().intValue() && shopEidList.contains(request.getShopEid())) {
                shopEidList.clear();
                shopEidList.add(request.getShopEid());
            }
        }
        request.setShopEidList(shopEidList);
        return request;
    }

    /**
     * 获取负责的公司
     *
     * @param currentUserId
     * @param contactUserMap
     * @return
     */
    public List<Long> getShopResponsibleEidList(Long currentUserId, Map<Long, List<EnterpriseDTO>> contactUserMap) {
        // 用户没有负责企业
        if (MapUtil.isEmpty(contactUserMap) || !contactUserMap.keySet().contains(currentUserId) || CollUtil.isEmpty(contactUserMap.get(currentUserId))) {
            return null;
        }
        // 有负责企业
        List<EnterpriseDTO> enterpriseList = contactUserMap.get(currentUserId);
//        log.info("商家后台连锁纯销导出, 当前用户:{}, 有权限的总店:{}", currentUserId, JSONUtil.toJsonStr(enterpriseList));
        if (CollUtil.isEmpty(enterpriseList)) {
            return null;
        } else {
            return enterpriseList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
        }
    }

    private void buildFlowShopSalePageVo(ExportFlowShopSalePageListBO record) {
        // 总金额
        BigDecimal totalAmount = record.getSoQuantity().multiply(record.getSoPrice());
        record.setSoTotalAmount(totalAmount.setScale(4, BigDecimal.ROUND_HALF_UP));
        record.setSoQuantity(FlowUtil.deimalcFormat(record.getSoQuantity()));
        // 销售日期
        Date soTime1 = record.getSoTime();
        if(ObjectUtil.isNotNull(soTime1)){
            record.setSoTimeStr(DateUtil.format(soTime1, "yyyy-MM-dd"));
        }
        // 去除空格
        record.setGoodsName(record.getGoodsName().trim());
        record.setSoSpecifications(record.getSoSpecifications().trim());
        record.setSoLicense(record.getSoLicense().trim());
        record.setSoManufacturer(record.getSoManufacturer().replace(" ",""));
        record.setSoBatchNo(record.getSoBatchNo().trim());
        record.setSoUnit(record.getSoUnit().trim());
        record.setGoodsInSn(record.getGoodsInSn().trim());
    }

    private List<Long> handlerEnterpriseTagIdList(Map<String, Object> map, QueryFlowShopSaleListPageRequest request, List<Long> eidList) {
        if(null == eidList){
            return null;
        }
        String enterpriseTagId = map.getOrDefault("enterpriseTagId", "").toString();
        if(StrUtil.isBlank(enterpriseTagId)){
            return eidList;
        }
        List<Long> enterpriseTagIdList = new ArrayList<>();
        if(enterpriseTagId.contains(",")){
            enterpriseTagIdList = Convert.toList(Long.class, enterpriseTagId);
        } else {
            enterpriseTagIdList.add(Long.parseLong(enterpriseTagId));
        }
        request.setEnterpriseTagIdList(enterpriseTagIdList);
        return filterEidTagListForShopSale(eidList, request.getEnterpriseTagIdList());
    }

    /**
     * 企业标签筛选
     *
     * @param eidList 企业id列表
     * @param enterpriseTagIdList 企业标签id列表
     * @return
     */
    private List<Long> filterEidTagListForShopSale(List<Long> eidList, List<Long> enterpriseTagIdList) {
        if (null == eidList) {
            return null;
        }
        if (CollUtil.isEmpty(enterpriseTagIdList)) {
            return eidList;
        }
        // 根据标签idl列表 查询企业id列表
        List<Long> eidTagList = enterpriseTagApi.getEidListByTagIdList(enterpriseTagIdList);
        if (CollUtil.isEmpty(eidTagList)) {
            return null;
        }

        if (CollUtil.isNotEmpty(eidList)) {
            eidList = eidList.stream().filter(eid -> eidTagList.contains(eid)).distinct().collect(Collectors.toList());
            if (CollUtil.isEmpty(eidList)) {
                return null;
            }
            return eidList;
        }
        return eidTagList;
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
//            log.info("商家后台连锁纯销导出, 当前用户:{}, 没有相关权限", currentUserId);
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
//        log.info("商家后台连锁纯销导出, 当前用户:{}, 数据权限:{}", currentUserId, JSONUtil.toJsonStr(contactUserMap));
        return contactUserMap;
    }

    private List<EnterpriseDTO> getEnterpriseListByIds(List<Long> eids) {
        if (CollUtil.isEmpty(eids)) {
            return ListUtil.empty();
        }
        List<EnterpriseDTO> enterpriseDTOS = enterpriseApi.listByIds(eids);
        if (CollUtil.isEmpty(enterpriseDTOS)) {
            return ListUtil.empty();
        }
        return enterpriseDTOS;
    }

    private List<Long> getShopEidListByShopProvinceCityRegion(String provinceCode, String cityCode, String regionCode) {
        if (StrUtil.isNotBlank(provinceCode) || StrUtil.isNotBlank(cityCode) || StrUtil.isNotBlank(regionCode)) {
            List<Integer> inTypeList = new ArrayList<>();
            inTypeList.add(EnterpriseTypeEnum.CHAIN_DIRECT.getCode());
            inTypeList.add(EnterpriseTypeEnum.CHAIN_JOIN.getCode());

            QueryEnterpriseListRequest eRequest = new QueryEnterpriseListRequest();
            eRequest.setProvinceCode(provinceCode);
            eRequest.setCityCode(cityCode);
            eRequest.setRegionCode(regionCode);
            eRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            eRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
            eRequest.setTypeList(inTypeList);
            List<EnterpriseDTO> enterpriseRegionList = enterpriseApi.queryListByArea(eRequest);
            if (CollUtil.isEmpty(enterpriseRegionList)) {
                return null;
            }
            return enterpriseRegionList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
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
