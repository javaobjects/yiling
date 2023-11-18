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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.bi.goods.api.InputGoodsRelationShipApi;
import com.yiling.bi.goods.dto.InputGoodsRelationShipDTO;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.bo.CrmEnterpriseRelationShipBO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.order.api.FlowSaleApi;
import com.yiling.dataflow.order.bo.FlowPermissionsBO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.export.export.bo.ExportFlowSalePageListBO;
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
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.ylprice.api.GoodsYilingPriceApi;
import com.yiling.goods.ylprice.dto.GoodsYilingPriceDTO;
import com.yiling.goods.ylprice.dto.request.QueryGoodsYilingPriceRequest;
import com.yiling.user.enterprise.api.CustomerContactApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseTagApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseTagDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.enums.PermissionAppEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * 销售流向导出
 *
 * @author: houjie.sun
 * @date: 2022/2/16
 */
@Service("crmFlowSalePageListExportService")
public class CrmFlowSalePageListExportServiceImpl implements BaseExportQueryDataService<QueryFlowPurchaseListPageRequest> {

    @DubboReference(timeout = 60 * 1000)
    FlowSaleApi                   flowSaleApi;
    @DubboReference
    EnterpriseApi                 enterpriseApi;
    @DubboReference
    CrmEnterpriseRelationShipApi  crmEnterpriseRelationShipApi;
    @DubboReference
    DataPermissionsApi            dataPermissionsApi;
    @DubboReference
    DictApi                       dictApi;
    @DubboReference
    EnterpriseTagApi              enterpriseTagApi;
    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;
    @DubboReference
    GoodsYilingPriceApi           goodsYilingPriceApi;
    @DubboReference
    CustomerContactApi            customerContactApi;
    @DubboReference
    GoodsApi                      goodsApi;
    @DubboReference
    InputGoodsRelationShipApi     inputGoodsRelationShipApi;
    @DubboReference
    CrmGoodsGroupApi              crmGoodsGroupApi;

    private static final String SALE_FLOW_SOURCE = "erp_sale_flow_source";

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("month", "月份");
        FIELD.put("department", "部门");
        FIELD.put("businessDepartment", "业务部门");
        FIELD.put("businessAreaDescription", "事业部");
        FIELD.put("customerCode", "药店代码");
        FIELD.put("customerName", "药店名称");
        FIELD.put("province", "省份");
        FIELD.put("cityCode", "城市代码");
        FIELD.put("city", "城市名称");
        FIELD.put("districtCountyCode", "区县代码");
        FIELD.put("districtCounty", "区县名称");
        FIELD.put("hospitalPharmacyType", "直营/加盟");
        FIELD.put("hospitalPharmacyAttribute", "连锁/单店");
        FIELD.put("hospitalPharmacyLevel", "药店属性");
        //    private String hospitalPharmacyLevel;//药店级别
        FIELD.put("chainDrugstoreSuperiorCode", "连锁上级编码");
        FIELD.put("chainDrugstoreSuperiorName", "连锁名称");
        FIELD.put("protocolConcatenationCode", "协议连锁编码");
        FIELD.put("protocolConcatenationName", "协议连锁名称");
        FIELD.put("isKa", "是否KA");
        FIELD.put("substituteRunning", "是否代跑");
        FIELD.put("crmGoodsId", "产品代码");
        FIELD.put("goodsName", "产品名称");
        FIELD.put("breed", "品种");
        //    流向内购进
//    流向外购进窜货批复
//    流向外购进窜货扣减
//    流向外购进反流向
//    流向外购进合计
        FIELD.put("quantity", "购进合计");
        FIELD.put("amount", "购进总金额");
        FIELD.put("price", "单价");
        FIELD.put("provincialArea", "大区/省区");
        FIELD.put("businessProvince", "业务省区");
        FIELD.put("businessArea", "业务区域");
        FIELD.put("representativeCode", "员工工号");
        FIELD.put("representativeName", "员工姓名");
        FIELD.put("superiorSupervisorCode", "主管工号");
        FIELD.put("superiorSupervisorName", "主管姓名");
        FIELD.put("productGroup", "产品组");
        FIELD.put("targetOrNot", "是否目标");
    }

    @Override
    public QueryExportDataDTO queryData(QueryFlowPurchaseListPageRequest request) {
        // 需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //获取crm的关联关系
        List<InputGoodsRelationShipDTO> inputGoodsRelationShipDTOList = inputGoodsRelationShipApi.getInputGoodsRelationShipAll();
        Map<String, List<InputGoodsRelationShipDTO>> inputGoodsRelationShipDTOMap = inputGoodsRelationShipDTOList.stream().collect(Collectors.groupingBy(InputGoodsRelationShipDTO::getB2bSpecid));
        //组的信息
//        List<CrmGoodsGroupDTO> crmGoodsGroupDTOList = crmGoodsGroupApi.getCrmGoodsGroupAll();
//        Map<String, CrmGoodsGroupDTO> crmGoodsGroupDTOMap = new HashMap<>();
//        for (CrmGoodsGroupDTO crmGoodsGroupDTO : crmGoodsGroupDTOList) {
//            crmGoodsGroupDTOMap.put(crmGoodsGroupDTO.getGoodsCode() + "-" + crmGoodsGroupDTO.getGroupName(), crmGoodsGroupDTO);
//        }
        // 需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<ExportFlowSalePageListBO> page;
        int current = 1;
        int size = 500;
        do {
            if (StrUtil.isBlank(request.getEname()) && StrUtil.isBlank(request.getProvinceCode()) && StrUtil.isBlank(request.getCityCode()) && StrUtil.isBlank(request.getRegionCode())
                    && StrUtil.isBlank(request.getEnterpriseName()) && StrUtil.isBlank(request.getGoodsName()) && ObjectUtil.isNull(request.getSpecificationId())
                    && StrUtil.isBlank(request.getLicense()) && ObjectUtil.isNull(request.getStartTime()) && ObjectUtil.isNull(request.getEndTime())
                    && CollUtil.isEmpty(request.getSourceList()) && StrUtil.isBlank(request.getManufacturer()) && CollUtil.isEmpty(request.getEnterpriseTagIdList())) {
                break;
            }
            if (CollUtil.isEmpty(request.getEidList())) {
                break;
            }
            request.setCurrent(current);
            request.setSize(size);
            page = PojoUtils.map(flowSaleApi.page(request), ExportFlowSalePageListBO.class);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                break;
            }

            List<ExportFlowSalePageListBO> records = page.getRecords();
            //List<String> enames = records.stream().map(ExportFlowSalePageListBO::getEname).distinct().collect(Collectors.toList());
            List<Long> eids = records.stream().map(ExportFlowSalePageListBO::getEid).distinct().collect(Collectors.toList());
            //通过eids查询三者关系
            //List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipDTOList = crmEnterpriseRelationShipApi.getCrmEnterpriseRelationShipByNameList(enames);
            List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipDTOList=crmEnterpriseRelationShipApi.getCrmEnterpriseRelationShipByCrmenterpriseIdList(eids);
            Map<String, List<CrmEnterpriseRelationShipDTO>> crmEnterpriseRelationShipMap = crmEnterpriseRelationShipDTOList.stream().collect(Collectors.groupingBy(CrmEnterpriseRelationShipDTO::getCustomerName));
            //通过商品规格ID查询crm的商品信息

            for (ExportFlowSalePageListBO record : page.getRecords()) {
//                CrmEnterpriseRelationShipBO crmEnterpriseRelationShipBO = getCrmEnterpriseRelationShipDTO(crmEnterpriseRelationShipMap, crmGoodsGroupDTOMap, inputGoodsRelationShipDTOMap,record);
                CrmEnterpriseRelationShipBO crmEnterpriseRelationShipBO = getCrmEnterpriseRelationShipDTO(crmEnterpriseRelationShipMap, inputGoodsRelationShipDTOMap,record);
                if (crmEnterpriseRelationShipBO.getPrice() != null) {
                    crmEnterpriseRelationShipBO.setQuantity(record.getSoQuantity());
                    crmEnterpriseRelationShipBO.setAmount(crmEnterpriseRelationShipBO.getPrice().multiply(crmEnterpriseRelationShipBO.getQuantity()));
                }
                data.add(BeanUtil.beanToMap(crmEnterpriseRelationShipBO));
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

//    private CrmEnterpriseRelationShipBO getCrmEnterpriseRelationShipDTO(Map<String, List<CrmEnterpriseRelationShipDTO>> crmEnterpriseRelationShipMap, Map<String, CrmGoodsGroupDTO> crmGoodsGroupDTOMap, Map<String, List<InputGoodsRelationShipDTO>> inputGoodsRelationShipDTOMap, ExportFlowSalePageListBO exportFlowSalePageListBO)
    private CrmEnterpriseRelationShipBO getCrmEnterpriseRelationShipDTO(Map<String, List<CrmEnterpriseRelationShipDTO>> crmEnterpriseRelationShipMap, Map<String, List<InputGoodsRelationShipDTO>> inputGoodsRelationShipDTOMap, ExportFlowSalePageListBO exportFlowSalePageListBO) {
        CrmEnterpriseRelationShipBO crmEnterpriseRelationShipBO = null;
        List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipList = crmEnterpriseRelationShipMap.getOrDefault(exportFlowSalePageListBO.getEname(),  ListUtil.empty());
        List<InputGoodsRelationShipDTO> inputGoodsRelationShipList = inputGoodsRelationShipDTOMap.getOrDefault(String.valueOf(exportFlowSalePageListBO.getSpecificationId()), ListUtil.empty());
        for (CrmEnterpriseRelationShipDTO crmEnterpriseRelationShipDTO : crmEnterpriseRelationShipList) {
            for (InputGoodsRelationShipDTO inputGoodsRelationShipDTO : inputGoodsRelationShipList) {
//                CrmGoodsGroupDTO crmGoodsGroupDTO = crmGoodsGroupDTOMap.getOrDefault(inputGoodsRelationShipDTO.getCrmGoodsid() + "-" + crmEnterpriseRelationShipDTO.getProductGroup(), null);
//                if (crmGoodsGroupDTO != null) {
                    crmEnterpriseRelationShipBO = PojoUtils.map(crmEnterpriseRelationShipDTO, CrmEnterpriseRelationShipBO.class);
                    crmEnterpriseRelationShipBO.setPrice(inputGoodsRelationShipDTO.getSxPrice());
                    crmEnterpriseRelationShipBO.setCrmGoodsId(inputGoodsRelationShipDTO.getCrmGoodsid());
                    crmEnterpriseRelationShipBO.setGoodsName(inputGoodsRelationShipDTO.getGoodsName());
                    crmEnterpriseRelationShipBO.setBreed(inputGoodsRelationShipDTO.getBreed());
//                }
            }
        }
        if (crmEnterpriseRelationShipBO == null) {
            crmEnterpriseRelationShipBO = new CrmEnterpriseRelationShipBO();
            crmEnterpriseRelationShipBO.setCustomerName(exportFlowSalePageListBO.getEname());
            crmEnterpriseRelationShipBO.setGoodsName(exportFlowSalePageListBO.getGoodsName());
        }
        return crmEnterpriseRelationShipBO;
    }

    private Map<String, String> getDictDataMap() {
        Map<String, String> dictDataMap = new HashMap<>();
        DictBO dict = dictApi.getDictByName(SALE_FLOW_SOURCE);
        if (ObjectUtil.isNotNull(dict)) {
            List<DictBO.DictData> dataList = dict.getDataList();
            dictDataMap = dataList.stream().collect(Collectors.toMap(d -> d.getValue(), d -> d.getLabel(), (k1, k2) -> k1));
        }
        return dictDataMap;
    }

    @Override
    public QueryFlowPurchaseListPageRequest getParam(Map<String, Object> map) {
        QueryFlowPurchaseListPageRequest request = PojoUtils.map(map, QueryFlowPurchaseListPageRequest.class);
        if (ObjectUtil.isNotNull(map.get("sellSpecificationsId"))) {
            request.setSpecificationId(Long.valueOf(map.get("sellSpecificationsId").toString()));
        }
        Long currentEid = Long.parseLong(map.getOrDefault("eid", 0L).toString());
        Long currentUserId = Long.parseLong(map.getOrDefault("currentUserId", 0L).toString());
        // 权限校验，获取当前用户负责的公司
        Map<Long, List<EnterpriseDTO>> contactUserMap = contactUserMap(currentEid, currentUserId);
        List<Long> eidList = new ArrayList<>();
        // 导出来源:1-运营后台，2-商家后台
        String menuSource = null;
        if (ObjectUtil.isNotNull(map.get("menuSource"))) {
            menuSource = map.get("menuSource").toString();
        }
        if (ObjectUtil.equal("1", menuSource)) {
            eidList = getEidListByNameProvinceCityRegion(request.getEname(), request.getProvinceCode(), request.getCityCode(), request.getRegionCode());
        } else {
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

    private void buildFlowSalePageVo(ExportFlowSalePageListBO record, Map<String, String> dictDataMap,
                                     Map<Long, StandardGoodsSpecificationDTO> standardGoodsMap, Map<Long, List<GoodsYilingPriceDTO>> goodsYilingPriceMap,
                                     Map<Long, List<EnterpriseTagDTO>> enterpriseTagMap, Map<Long, List<UserDTO>> enterpriseCustomerContactMap) {
        // 总金额
        BigDecimal totalAmount = record.getSoQuantity().multiply(record.getSoPrice());
        record.setSoTotalAmount(totalAmount.setScale(4, BigDecimal.ROUND_HALF_UP));
        record.setSoQuantity(FlowUtil.deimalcFormat(record.getSoQuantity()));
        // 销售日期
        Date soTime1 = record.getSoTime();
        if (ObjectUtil.isNotNull(soTime1)) {
            record.setSoTimeStr(DateUtil.format(soTime1, "yyyy-MM-dd"));
        }
        // 以岭品id
        Long ylGoodsId = record.getYlGoodsId();
        if (ObjectUtil.isNotNull(ylGoodsId) && ylGoodsId.intValue() == 0) {
            record.setYlGoodsId(null);
        }
        // 去除空格
        record.setGoodsName(record.getGoodsName().trim());
        record.setSoSpecifications(record.getSoSpecifications().trim());
        record.setSoLicense(record.getSoLicense().trim());
        record.setSoManufacturer(record.getSoManufacturer().replace(" ", ""));
        record.setSoBatchNo(record.getSoBatchNo().trim());
        record.setSoUnit(record.getSoUnit().trim());
        record.setGoodsInSn(record.getGoodsInSn().trim());
        // 订单来源
        String source = "其它渠道销售";
        String label = dictDataMap.get(record.getSoSource());
        if (StrUtil.isNotBlank(label)) {
            source = label;
        }
        record.setSoSource(source);
        // 规格id
        Long specificationId = record.getSpecificationId();
        if (ObjectUtil.isNotNull(specificationId) && specificationId.intValue() != 0) {
            // 规格
            StandardGoodsSpecificationDTO standardGoodsSpecificationDTO = standardGoodsMap.get(specificationId);
            if (ObjectUtil.isNotNull(standardGoodsSpecificationDTO)) {
                record.setStandardName(standardGoodsSpecificationDTO.getName());
                record.setStandardSellSpecifications(standardGoodsSpecificationDTO.getSellSpecifications());
            }
            // 商销价
            List<GoodsYilingPriceDTO> goodsYilingPriceDTOS = goodsYilingPriceMap.get(specificationId);
            if (CollUtil.isNotEmpty(goodsYilingPriceDTOS)) {
                for (GoodsYilingPriceDTO goodsYilingPriceDTO : goodsYilingPriceDTOS) {
                    long soTime = record.getSoTime().getTime();
                    if (soTime >= goodsYilingPriceDTO.getStartTime().getTime() && soTime <= goodsYilingPriceDTO.getEndTime().getTime()) {
                        record.setYlPrice(goodsYilingPriceDTO.getPrice());
                        BigDecimal totalYlPrice = record.getSoQuantity().multiply(goodsYilingPriceDTO.getPrice());
                        record.setTotalYlPrice(totalYlPrice);
                    }
                }
            }
        }
        // 商业标签
        List<EnterpriseTagDTO> enterpriseTagDTOS = enterpriseTagMap.get(record.getEid());
        if (CollUtil.isNotEmpty(enterpriseTagDTOS)) {
            List<String> tagNameList = enterpriseTagDTOS.stream().map(EnterpriseTagDTO::getName).distinct().collect(Collectors.toList());
            record.setTagNames(String.join(",", tagNameList));
        }
        // 商务负责人
        List<UserDTO> enterpriseCustomerContactList = enterpriseCustomerContactMap.get(record.getEid());
        if (CollUtil.isNotEmpty(enterpriseCustomerContactList)) {
            List<String> contactUserNameList = enterpriseCustomerContactList.stream().map(UserDTO::getName).distinct().collect(Collectors.toList());
            record.setCustomerContact(String.join(",", contactUserNameList));
        }
    }

    private Map<Long, List<GoodsYilingPriceDTO>> getGoodsYilingPrice(List<Long> specificationIdList, List<Date> soTimeList) {
        Map<Long, List<GoodsYilingPriceDTO>> goodsYilingPriceMap = new HashMap<>();
        if (CollUtil.isEmpty(specificationIdList) || CollUtil.isEmpty(soTimeList)) {
            return goodsYilingPriceMap;
        }
        QueryGoodsYilingPriceRequest goodsYilingPriceRequest = new QueryGoodsYilingPriceRequest();
        goodsYilingPriceRequest.setSpecificationIdList(specificationIdList);
        goodsYilingPriceRequest.setTimeList(soTimeList);
        List<GoodsYilingPriceDTO> goodsYilingPriceList = goodsYilingPriceApi.listBySpecificationIdAndDate(goodsYilingPriceRequest);
        if (CollUtil.isNotEmpty(goodsYilingPriceList)) {
            goodsYilingPriceMap = goodsYilingPriceList.stream().collect(Collectors.groupingBy(GoodsYilingPriceDTO::getSpecificationId));
        }
        return goodsYilingPriceMap;
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

    private void handlerEnterpriseTagIdList(Map<String, Object> map, QueryFlowPurchaseListPageRequest request, List<Long> eidList) {
        if (CollUtil.isEmpty(eidList)) {
            return;
        }
        String enterpriseTagId = map.getOrDefault("enterpriseTagId", "").toString();
        if (StrUtil.isBlank(enterpriseTagId)) {
            return;
        }
        List<Long> enterpriseTagIdList = new ArrayList<>();
        if (enterpriseTagId.contains(",")) {
            enterpriseTagIdList = Convert.toList(Long.class, enterpriseTagId);
        } else {
            enterpriseTagIdList.add(Long.parseLong(enterpriseTagId));
        }
        request.setEnterpriseTagIdList(enterpriseTagIdList);
        List<Long> eidListNew = filterEidTagList(eidList, request.getEnterpriseTagIdList());
        if (CollUtil.isEmpty(eidListNew)) {
            eidList.removeAll(eidList);
        } else {
            eidList.removeAll(eidList);
            eidList.addAll(eidListNew);
        }
    }

    private void handlerSourceList(Map<String, Object> map, QueryFlowPurchaseListPageRequest request) {
        String source = map.getOrDefault("source", "").toString();
        if (StrUtil.isNotBlank(source)) {
            List<String> sourceList = new ArrayList<>();
            if (source.contains(",")) {
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
     * @param eidList             企业id列表
     * @param enterpriseTagIdList 企业标签id列表
     * @return
     */
    private List<Long> filterEidTagList(List<Long> eidList, List<Long> enterpriseTagIdList) {
        if (CollUtil.isEmpty(eidList)) {
            return ListUtil.empty();
        }
        if (CollUtil.isEmpty(enterpriseTagIdList)) {
            return eidList;
        }
        // 根据标签idl列表 查询企业id列表
        List<Long> eidTagList = enterpriseTagApi.getEidListByTagIdList(enterpriseTagIdList);
        if (CollUtil.isEmpty(eidTagList)) {
            return ListUtil.empty();
        }
        return eidList.stream().filter(eid -> eidTagList.contains(eid)).distinct().collect(Collectors.toList());
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
        if (CollUtil.isNotEmpty(userIdList) && !userIdList.contains(currentUserId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        Map<Long, List<EnterpriseDTO>> contactUserMap = new HashMap<>();
        if (CollUtil.isEmpty(userIdList)) {
            EnterpriseDTO enterpriseDTO = new EnterpriseDTO();
            enterpriseDTO.setId(-99L);
            contactUserMap.put(currentUserId, new ArrayList() {{
                add(enterpriseDTO);
            }});
            return contactUserMap;
        }
        // 当前用户负责的企业
        contactUserMap = enterpriseApi.listByContactUserIds(Constants.YILING_EID, userIdList);
        return contactUserMap;
    }

    private List<EnterpriseDTO> getEnterpriseListByName(String name) {
        QueryEnterpriseByNameRequest request = new QueryEnterpriseByNameRequest();
        request.setName(name);
        request.setTypeList(Arrays.asList(EnterpriseTypeEnum.BUSINESS.getCode(), EnterpriseTypeEnum.CHAIN_BASE.getCode(), EnterpriseTypeEnum.CHAIN_DIRECT.getCode(), EnterpriseTypeEnum.CHAIN_JOIN.getCode()));
        return enterpriseApi.getEnterpriseListByName(request);
    }

    private List<Long> getEidListByNameProvinceCityRegion(String name, String provinceCode, String cityCode, String regionCode) {
        List<EnterpriseDTO> enterpriseList = new ArrayList<>();
        List<EnterpriseDTO> enterpriseNameList = null;
        List<EnterpriseDTO> enterpriseRegionList = null;
        boolean nameFlag = false;
        boolean regionFlag = false;
        if (StrUtil.isNotBlank(name)) {
            nameFlag = true;
            enterpriseNameList = getEnterpriseListByName(name);
        }
        if (StrUtil.isNotBlank(provinceCode) || StrUtil.isNotBlank(cityCode) || StrUtil.isNotBlank(regionCode)) {
            regionFlag = true;
            QueryEnterpriseListRequest eRequest = new QueryEnterpriseListRequest();
            eRequest.setProvinceCode(provinceCode);
            eRequest.setCityCode(cityCode);
            eRequest.setRegionCode(regionCode);
            enterpriseRegionList = enterpriseApi.queryListByArea(eRequest);
        }

        if (!nameFlag && !regionFlag) {
            // 查询所有POP商业类型的企业信息
            return getAllBusinessEnterprise();
        } else if (nameFlag && regionFlag) {
            if (CollUtil.isNotEmpty(enterpriseNameList) && CollUtil.isNotEmpty(enterpriseRegionList)) {
                List<Long> eidListTemp = enterpriseRegionList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
                enterpriseList = enterpriseNameList.stream().filter(o -> eidListTemp.contains(o.getId())).collect(Collectors.toList());
            }
        } else {
            if (CollUtil.isNotEmpty(enterpriseNameList)) {
                enterpriseList.addAll(enterpriseNameList);
            }
            if (CollUtil.isNotEmpty(enterpriseRegionList)) {
                enterpriseList.addAll(enterpriseRegionList);
            }
        }

        if (CollUtil.isNotEmpty(enterpriseList)) {
            return enterpriseList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
        }
        return ListUtil.empty();
    }

    private List<Long> getAllBusinessEnterprise() {
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
            List<Integer> inTypeList = new ArrayList<>();
            inTypeList.add(EnterpriseTypeEnum.BUSINESS.getCode());
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
