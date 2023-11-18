package com.yiling.dataflow.flow.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupRelationService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.dataflow.crm.service.CrmGoodsInfoService;
import com.yiling.dataflow.flow.dao.FlowSaleSummaryMapper;
import com.yiling.dataflow.flow.dto.request.QueryFlowSaleSummaryRequest;
import com.yiling.dataflow.flow.dto.request.UpdateFlowSaleSummaryRequest;
import com.yiling.dataflow.flow.entity.FlowSaleSummaryDO;
import com.yiling.dataflow.flow.service.FlowSaleSummaryDayService;
import com.yiling.dataflow.flow.service.FlowSaleSummaryService;
import com.yiling.dataflow.flow.util.FlowJudgeTypeUtil;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.order.entity.FlowSaleDO;
import com.yiling.dataflow.order.service.FlowSaleService;
import com.yiling.dataflow.spda.dto.SpdaEnterpriseDataDTO;
import com.yiling.dataflow.spda.service.SpdaEnterpriseDataService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-10
 */
@Service
public class FlowSaleSummaryServiceImpl extends BaseServiceImpl<FlowSaleSummaryMapper, FlowSaleSummaryDO> implements FlowSaleSummaryService {

    @Autowired
    private FlowSaleService                  flowSaleService;
    @Autowired
    private CrmEnterpriseService             crmEnterpriseService;
    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;
//    @Autowired
//    private CrmGoodsGroupRelationService     crmGoodsGroupRelationService;
    @Autowired
    private CrmGoodsGroupService             crmGoodsGroupService;
    @Autowired
    private CrmGoodsInfoService              crmGoodsInfoService;
    @Autowired
    private SpdaEnterpriseDataService        spdaEnterpriseDataService;
    @Autowired
    private FlowJudgeTypeUtil                flowJudgeTypeUtil;
    @Autowired
    FlowSaleSummaryDayService flowSaleSummaryDayService;

    @Override
    public void updateFlowSaleSummaryByFlowSaleId(Long flowSaleId) {
        //需要删除汇总表
        this.getBaseMapper().deleteFlowSaleSummaryByFlowSaleId(flowSaleId);
        FlowSaleDO flowSaleDO = flowSaleService.getById(flowSaleId);
        if (flowSaleDO != null) {
            //3添加汇总数据
            this.inserteFlowSaleSummaryByFlowSaleList(Arrays.asList(flowSaleDO), flowSaleDO.getCrmEnterpriseId());
        }
    }

    @Override
    public void updateFlowSaleSummaryByDateTimeAndEid(UpdateFlowSaleSummaryRequest request) {
        //1先删除流向数据汇总数据
        if (request.getEid() == null || request.getEid() == 0) {
            return;
        }
        if (request.getCrmId() == null || request.getCrmId() == 0) {
            return;
        }
        if (request.getStartTime() == null) {
            return;
        }
        if (request.getEndTime() == null) {
            return;
        }
        this.baseMapper.deleteFlowSaleSummaryByEidAndSoTime(Arrays.asList(request.getEid()), request.getStartTime(), request.getEndTime());
        //2在查询流向数据
        {
            QueryFlowPurchaseListPageRequest queryFlowPurchaseListPageRequest = new QueryFlowPurchaseListPageRequest();
            queryFlowPurchaseListPageRequest.setStartTime(request.getStartTime());
            queryFlowPurchaseListPageRequest.setEndTime(request.getEndTime());
            queryFlowPurchaseListPageRequest.setEid(request.getEid());
            queryFlowPurchaseListPageRequest.setDeleteFlag(2);
            Page<FlowSaleDO> page;
            int current = 1;
            int size = 2000;
            do {
                queryFlowPurchaseListPageRequest.setSize(size);
                queryFlowPurchaseListPageRequest.setCurrent(current);
                page = flowSaleService.page(queryFlowPurchaseListPageRequest);
                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                    break;
                }
                this.inserteFlowSaleSummaryByFlowSaleList(page.getRecords(), request.getCrmId());
                current++;
            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        }
    }

    @Override
    public void updateFlowSaleSummaryDay(UpdateFlowSaleSummaryRequest request) {
        //1先删除流向数据汇总数据
        if (request.getEid() == null || request.getEid() == 0) {
            return;
        }
        if (request.getStartTime() == null) {
            return;
        }
        if (request.getEndTime() == null) {
            return;
        }
        // 通过时间打标流向汇总表
        QueryFlowSaleSummaryRequest flowSaleSummaryDayRequest = new QueryFlowSaleSummaryRequest();
        flowSaleSummaryDayRequest.setEid(request.getEid());
        flowSaleSummaryDayRequest.setStartTime(request.getStartTime());
        flowSaleSummaryDayRequest.setEndTime(request.getEndTime());
        flowSaleSummaryDayService.updateFlowSaleSummaryDayByDateTimeAndEid(flowSaleSummaryDayRequest);
        flowSaleSummaryDayService.updateFlowSaleSummaryDayLingShouByTerminalCustomerType(flowSaleSummaryDayRequest);
        flowSaleSummaryDayService.updateFlowSaleSummaryDayPifaByTerminalCustomerType(flowSaleSummaryDayRequest);
    }

    @Override
    public Page<FlowSaleSummaryDO> pageList(QueryFlowSaleSummaryRequest request) {
        return this.baseMapper.page(request.getPage(), request);
    }

    public void inserteFlowSaleSummaryByFlowSaleList(List<FlowSaleDO> flowSaleDOList, Long crmId) {
        List<FlowSaleSummaryDO> flowSaleSummaryList = new ArrayList<>();
        CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getById(crmId);
        for (FlowSaleDO flowSaleDO : flowSaleDOList) {
            StringBuffer sign = new StringBuffer("");
            FlowSaleSummaryDO flowSaleSummary = new FlowSaleSummaryDO();
            flowSaleSummary.setCrmEnterpriseId(crmId);
            flowSaleSummary.setSoId(flowSaleDO.getSoId());
            flowSaleSummary.setSoTime(flowSaleDO.getSoTime());
            flowSaleSummary.setEid(flowSaleDO.getEid());
            flowSaleSummary.setFlowSaleId(flowSaleDO.getId());
            flowSaleSummary.setYear(String.valueOf(DateUtil.year(flowSaleDO.getSoTime())));
            flowSaleSummary.setMonth(String.valueOf(DateUtil.month(flowSaleDO.getSoTime()) + 1));
            flowSaleSummary.setGoodsPossibleError(flowSaleDO.getGoodsPossibleError());
            flowSaleSummary.setSalesType(flowSaleDO.getCustomerTypeName());
            flowSaleSummary.setDataTag(flowSaleDO.getDataTag());
            flowSaleSummary.setDelFlag(flowSaleDO.getDelFlag());
            //------商品信息-------
            flowSaleSummary.setOriginalProductName(flowSaleDO.getGoodsName());
            flowSaleSummary.setOriginalProductSpecification(flowSaleDO.getSoSpecifications());
            flowSaleSummary.setBatchNo(flowSaleDO.getSoBatchNo());
            flowSaleSummary.setOriginalPrice(flowSaleDO.getSoPrice());
            flowSaleSummary.setCrmGoodsCode(flowSaleDO.getCrmGoodsCode());
            flowSaleSummary.setOriginalQuantity(flowSaleDO.getSoQuantity());
            flowSaleSummary.setOriginalUnit(flowSaleDO.getSoUnit());

            if (crmEnterpriseDO != null) {
                //------商业信息-------
                flowSaleSummary.setBusinessCode(crmEnterpriseDO.getCode());
                flowSaleSummary.setBusinessName(crmEnterpriseDO.getName());
                flowSaleSummary.setCommercialProvince(crmEnterpriseDO.getProvinceName());
                flowSaleSummary.setCommercialCity(crmEnterpriseDO.getCityName());
                flowSaleSummary.setCommercialCounty(crmEnterpriseDO.getRegionName());
//                flowSaleSummary.setSalesType(crmEnterpriseDO.getTerminnalType());
            } else {
                sign.append("没有映射到crm商业编码;");
            }

            //------商品信息-------
            flowSaleSummary.setIsLocked("否");
            if (flowSaleDO.getCrmGoodsCode() != null && flowSaleDO.getCrmGoodsCode() != 0) {
                CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoService.getCrmGoodsInfoByCode(flowSaleDO.getCrmGoodsCode());
                if (crmGoodsInfoDTO != null) {
                    flowSaleSummary.setVarieties(crmGoodsInfoDTO.getVarietyType());
                    flowSaleSummary.setCrmGoodsName(crmGoodsInfoDTO.getGoodsName());
                    flowSaleSummary.setCrmGoodsSpec(crmGoodsInfoDTO.getGoodsSpec());
                    flowSaleSummary.setPrice(crmGoodsInfoDTO.getSupplyPrice());
                    if (flowSaleSummary.getPrice() != null && flowSaleSummary.getOriginalQuantity() != null) {
                        flowSaleSummary.setAmountPrice(flowSaleSummary.getPrice().multiply(flowSaleSummary.getOriginalQuantity()));
                    }
                    //查找三者关系
                    List<Long> crmGoodsGroupList = crmGoodsGroupService.findCrmDepartProductGroupByGoodsCode(flowSaleDO.getCrmGoodsCode());
                    if (CollUtil.isNotEmpty(crmGoodsGroupList)) {
//                        if (flowSaleDO.getCrmEnterpriseId() != null && flowSaleDO.getCrmEnterpriseId() != 0) {
//                            Long crmBusRelationShipId = crmEnterpriseRelationShipService.listByCrmEnterpriseIdList(crmGoodsGroupList, flowSaleDO.getCrmEnterpriseId());
//                            if (crmBusRelationShipId != null && crmBusRelationShipId != 0) {
//                                CrmEnterpriseRelationShipDO crmEnterpriseRelationShipDO = crmEnterpriseRelationShipService.getById(crmBusRelationShipId);
//                                flowSaleSummary.setBusinessDepartment(crmEnterpriseRelationShipDO.getDepartment());
//                                flowSaleSummary.setCommercialBusinessDepartment(crmEnterpriseRelationShipDO.getBusinessDepartment());
//                                flowSaleSummary.setCommercialArea(crmEnterpriseRelationShipDO.getBusinessArea());
//                                flowSaleSummary.setCommercialAreaCode(crmEnterpriseRelationShipDO.getBusinessAreaCode());
//                                flowSaleSummary.setCommercialBusinessProvince(crmEnterpriseRelationShipDO.getBusinessProvince());
//                                flowSaleSummary.setCommercialProvinceArea(crmEnterpriseRelationShipDO.getProvincialArea());
//                                flowSaleSummary.setCommercialManagerCode(crmEnterpriseRelationShipDO.getSuperiorSupervisorCode());
//                                flowSaleSummary.setCommercialManagerName(crmEnterpriseRelationShipDO.getSuperiorSupervisorName());
//                                flowSaleSummary.setCommercialRepresentativeCode(crmEnterpriseRelationShipDO.getRepresentativeCode());
//                                flowSaleSummary.setCommercialRepresentativeName(crmEnterpriseRelationShipDO.getRepresentativeName());
//                                flowSaleSummary.setAgreementLevel(crmEnterpriseRelationShipDO.getAgreementType());
//                                flowSaleSummary.setCommercialAttributes(crmEnterpriseRelationShipDO.getRemark());
//                                flowSaleSummary.setChainAgreementType(crmEnterpriseRelationShipDO.getIsKa());
//                            } else {
//                                sign.append("商业公司没有匹配到三者关系;");
//                            }
//                        }
                        if ( StrUtil.isNotEmpty(flowSaleDO.getEnterpriseCrmCode())) {
                            Long crmBusRelationShipId = crmEnterpriseRelationShipService.listByCrmEnterpriseIdList(crmGoodsGroupList, Long.parseLong(flowSaleDO.getEnterpriseCrmCode()));
                            if (crmBusRelationShipId != null && crmBusRelationShipId != 0) {
                                CrmEnterpriseRelationShipDO crmEnterpriseRelationShipDO = crmEnterpriseRelationShipService.getById(crmBusRelationShipId);
                                flowSaleSummary.setOrganizationName(crmEnterpriseRelationShipDO.getCustomerName());
                                flowSaleSummary.setOrganizationDepartment(crmEnterpriseRelationShipDO.getDepartment());
                                flowSaleSummary.setOrganizationBusinessDepartment(crmEnterpriseRelationShipDO.getBusinessDepartment());
                                flowSaleSummary.setOrganizationProvinceArea(crmEnterpriseRelationShipDO.getProvincialArea());
                                flowSaleSummary.setOrganizationBusinessProvince(crmEnterpriseRelationShipDO.getBusinessProvince());
                                flowSaleSummary.setOrganizationAreaCode(crmEnterpriseRelationShipDO.getBusinessAreaCode());
                                flowSaleSummary.setOrganizationArea(crmEnterpriseRelationShipDO.getBusinessArea());
                                flowSaleSummary.setManagerCode(crmEnterpriseRelationShipDO.getSuperiorSupervisorCode());
                                flowSaleSummary.setManagerName(crmEnterpriseRelationShipDO.getSuperiorSupervisorName());
                                flowSaleSummary.setRepresentativeCode(crmEnterpriseRelationShipDO.getRepresentativeCode());
                                flowSaleSummary.setRepresentativeName(crmEnterpriseRelationShipDO.getRepresentativeName());
                                flowSaleSummary.setInstitutionalProvince(crmEnterpriseRelationShipDO.getProvince());
                                flowSaleSummary.setInstitutionalCityCode(crmEnterpriseRelationShipDO.getCityCode());
                                flowSaleSummary.setInstitutionalCity(crmEnterpriseRelationShipDO.getCity());
                                flowSaleSummary.setInstitutionalCountyCode(crmEnterpriseRelationShipDO.getDistrictCountyCode());
                                flowSaleSummary.setInstitutionalCounty(crmEnterpriseRelationShipDO.getDistrictCounty());
                                flowSaleSummary.setTerminalType(crmEnterpriseRelationShipDO.getSupplyChainRole());
                                flowSaleSummary.setTerminalTypeTwo(crmEnterpriseRelationShipDO.getHospitalPharmacyType());
                                flowSaleSummary.setOrganizationLevel(crmEnterpriseRelationShipDO.getHospitalPharmacyAttribute());
                                flowSaleSummary.setInstitutionalCountryLevel(crmEnterpriseRelationShipDO.getHospitalPharmacyLevel());
                                if (crmEnterpriseRelationShipDO.getSupplyChainRole().equals(CrmSupplyChainRoleEnum.DISTRIBUTOR.getName())) {
                                    // 经销商
                                    flowSaleSummary.setTerminalCustomerType(2);
                                    flowSaleSummary.setTerminalCustomerTypeName(crmEnterpriseRelationShipDO.getSupplyChainRole());
                                } else if (crmEnterpriseRelationShipDO.getSupplyChainRole().equals(CrmSupplyChainRoleEnum.PHARMACY.getName())) {
                                    // 终端药店
                                    flowSaleSummary.setTerminalCustomerType(1);
                                    flowSaleSummary.setTerminalCustomerTypeName(crmEnterpriseRelationShipDO.getSupplyChainRole());
                                } else if (crmEnterpriseRelationShipDO.getSupplyChainRole().equals(CrmSupplyChainRoleEnum.HOSPITAL.getName())) {
                                    // 终端医院
                                    flowSaleSummary.setTerminalCustomerType(3);
                                    flowSaleSummary.setTerminalCustomerTypeName(crmEnterpriseRelationShipDO.getSupplyChainRole());
                                }

                                flowSaleSummary.setIsLocked("是");
                            } else {
                                sign.append("终端公司没有匹配到三者关系;");
                            }
                        }
                    } else {
                        sign.append("crm商品编码没有获取商品组信息;");
                    }
                } else {
                    sign.append("crm商品编码没有获取商品信息;");
                }
            } else {
                sign.append("没有映射到crm商品编码;");
            }
            //------终端信息-------
            flowSaleSummary.setCustomerTypeName(flowSaleDO.getCustomerTypeName());
            flowSaleSummary.setOriginalOrganizationName(flowSaleDO.getEnterpriseName());
            flowSaleSummary.setOrganizationCode(String.valueOf(flowSaleDO.getEnterpriseCrmCode()));
            flowSaleSummary.setSignMsg(sign.toString());
            //添加匹配关系
            if (StrUtil.isNotEmpty(flowSaleDO.getEnterpriseName()) && flowSaleSummary.getTerminalCustomerType() == null) {
                SpdaEnterpriseDataDTO spdaEnterpriseDataDTO = spdaEnterpriseDataService.getSpdaEnterpriseDataByName(replaceStr(flowSaleDO.getEnterpriseName()));
                if (spdaEnterpriseDataDTO != null) {
                    if (spdaEnterpriseDataDTO.getFirstTag().equals("零售") && Arrays.asList("", "单体药店", "连锁门店").contains(spdaEnterpriseDataDTO.getSecondTag())) {
                        flowSaleSummary.setTerminalCustomerType(1);
                    } else {
                        flowSaleSummary.setTerminalCustomerType(2);
                    }
                    if (StrUtil.isEmpty(spdaEnterpriseDataDTO.getSecondTag())) {
                        flowSaleSummary.setTerminalCustomerTypeName(spdaEnterpriseDataDTO.getFirstTag());
                    } else {
                        flowSaleSummary.setTerminalCustomerTypeName(spdaEnterpriseDataDTO.getSecondTag());
                    }
                } else {
                    String typeName = flowJudgeTypeUtil.flowJudgeType(flowSaleDO.getEnterpriseName());
                    if (StrUtil.isNotEmpty(typeName)) {
                        if (Arrays.asList("诊所", "综合医院", "专科医院", "厂矿职工医院", "社区卫生服务中心", "社区卫生服务站", "乡镇卫生院", "村卫生室/卫生所", "医务室", "其他医疗终端").contains(typeName)) {
                            flowSaleSummary.setTerminalCustomerType(3);
                        } else if (Arrays.asList("普通商业", "连锁商业").contains(typeName)) {
                            flowSaleSummary.setTerminalCustomerType(2);
                        } else if (Arrays.asList("连锁分店", "单体药店").contains(typeName)) {
                            flowSaleSummary.setTerminalCustomerType(1);
                        } else if (Arrays.asList("其它").contains(typeName)) {
                            flowSaleSummary.setTerminalCustomerType(4);
                        }
                        flowSaleSummary.setTerminalCustomerTypeName(typeName);
                    }
                }
            }
            flowSaleSummaryList.add(flowSaleSummary);
        }
        //批量插入
        this.saveBatch(flowSaleSummaryList);
    }

    public String replaceStr(String str) {
        if (StrUtil.isEmpty(str)) {
            return null;
        }
        String sss = new StringBuffer(str.replaceFirst("^\\d+\\s*", "")).reverse().toString().replaceFirst("^\\d+\\s*", "");
        return new StringBuilder(sss).reverse().toString();
    }

//    /**
//     * 在crm商业信息档案信息
//     *
//     * @return
//     */
//    public Map<String, CrmEnterpriseDTO> getCrmEnterpriseMap() {
//        List<CrmEnterpriseDTO> crmEnterpriseDOList = new ArrayList<>();
//        String crmEnterpriseJson = stringRedisTemplate.opsForValue().get(CRM_ENTERPRISE_KEY);
//        if (StrUtil.isNotEmpty(crmEnterpriseJson)) {
//            crmEnterpriseDOList = JSON.parseArray(crmEnterpriseJson, CrmEnterpriseDTO.class);
//        } else {
//            QueryCrmEnterprisePageRequest queryCrmEnterpriseRequest = new QueryCrmEnterprisePageRequest();
//            Page<CrmEnterpriseDTO> page;
//            int current = 1;
//            int size = 2000;
//            do {
//                queryCrmEnterpriseRequest.setSize(size);
//                queryCrmEnterpriseRequest.setCurrent(current);
//                page = crmEnterpriseService.getCrmEnterprisePage(queryCrmEnterpriseRequest);
//                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
//                    break;
//                }
//                // 取供应商已对接的采购
//                crmEnterpriseDOList.addAll(page.getRecords());
//                current++;
//            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
//            stringRedisTemplate.opsForValue().set(CRM_ENTERPRISE_KEY, JSON.toJSONString(crmEnterpriseDOList));
//        }
//        return crmEnterpriseDOList.stream().collect(Collectors.toMap(CrmEnterpriseDTO::getCode, Function.identity()));
//    }
//
//    public Map<String, CrmEnterpriseRelationShipDTO> getCrmEnterpriseRelationShipMap() {
//        List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipList = new ArrayList<>();
//        String CrmEnterpriseRelationShipJson = stringRedisTemplate.opsForValue().get(CRM_ENTERPRISE_RELATION_SHIP_KEY);
//        if (StrUtil.isNotEmpty(CrmEnterpriseRelationShipJson)) {
//            crmEnterpriseRelationShipList = JSON.parseArray(CrmEnterpriseRelationShipJson, CrmEnterpriseRelationShipDTO.class);
//        } else {
//            QueryCrmEnterpriseRelationShipRequest queryCrmEnterpriseRequest = new QueryCrmEnterpriseRelationShipRequest();
//            Page<CrmEnterpriseRelationShipDTO> page;
//            int current = 1;
//            int size = 2000;
//            do {
//                queryCrmEnterpriseRequest.setSize(size);
//                queryCrmEnterpriseRequest.setCurrent(current);
//                page = crmEnterpriseRelationShipService.getCrmEnterpriseRelationShipPage(queryCrmEnterpriseRequest);
//                if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
//                    break;
//                }
//                // 取供应商已对接的采购
//                crmEnterpriseRelationShipList.addAll(page.getRecords());
//                current++;
//            } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
//            stringRedisTemplate.opsForValue().set(CRM_ENTERPRISE_RELATION_SHIP_KEY, JSON.toJSONString(crmEnterpriseRelationShipList));
//        }
//        return crmEnterpriseRelationShipList.stream().collect(Collectors.toMap(e -> e.getCustomerCode() + "-" + e.getProductGroup(), Function.identity()));
//    }
}
