package com.yiling.sjms.wash.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyCountRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.api.CrmGoodsTagApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsTagDTO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.report.api.FlowWashReportApi;
import com.yiling.dataflow.report.api.FlowWashSaleReportApi;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.report.dto.request.SumFlowWashReportRequest;
import com.yiling.dataflow.report.dto.request.UpdateFlowWashSaleReportRequest;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.api.FlowMonthWashTaskApi;
import com.yiling.dataflow.wash.api.FlowSaleWashApi;
import com.yiling.dataflow.wash.api.UnlockAreaRecordApi;
import com.yiling.dataflow.wash.api.UnlockCollectionDetailApi;
import com.yiling.dataflow.wash.api.UnlockFlowWashSaleApi;
import com.yiling.dataflow.wash.api.UnlockFlowWashTaskApi;
import com.yiling.dataflow.wash.api.UnlockSaleBusinessApi;
import com.yiling.dataflow.wash.api.UnlockSaleCustomerApi;
import com.yiling.dataflow.wash.api.UnlockSaleCustomerRangeApi;
import com.yiling.dataflow.wash.api.UnlockSaleCustomerRangeProvinceApi;
import com.yiling.dataflow.wash.api.UnlockSaleDepartmentApi;
import com.yiling.dataflow.wash.api.UnlockSaleGoodsApi;
import com.yiling.dataflow.wash.api.UnlockSaleGoodsCategoryApi;
import com.yiling.dataflow.wash.api.UnlockSaleGoodsTagApi;
import com.yiling.dataflow.wash.api.UnlockSaleRuleApi;
import com.yiling.dataflow.wash.api.UnlockThirdRecordApi;
import com.yiling.dataflow.wash.dto.FlowSaleWashDTO;
import com.yiling.dataflow.wash.dto.UnlockAreaRecordDTO;
import com.yiling.dataflow.wash.dto.UnlockCollectionDetailDTO;
import com.yiling.dataflow.wash.dto.UnlockFlowWashSaleDTO;
import com.yiling.dataflow.wash.dto.UnlockFlowWashTaskDTO;
import com.yiling.dataflow.wash.dto.UnlockSaleCustomerRangeDTO;
import com.yiling.dataflow.wash.dto.UnlockSaleDepartmentDTO;
import com.yiling.dataflow.wash.dto.UnlockSaleRuleDTO;
import com.yiling.dataflow.wash.dto.UnlockThirdRecordDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashListRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCustomerWashTaskRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockFlowWashSaleRequest;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.UnlockSaleRuleSourceEnum;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhang.shuang
 * @date 2023/3/6
 */
@Slf4j
@Service
public class UnFlowWashHandler {

    @DubboReference
    private CrmEnterpriseApi                   crmEnterpriseApi;
    @DubboReference
    private FlowMonthWashTaskApi               flowMonthWashTaskApi;
    @DubboReference
    private FlowMonthWashControlApi            flowMonthWashControlApi;
    @DubboReference
    private EsbEmployeeApi                     esbEmployeeApi;
    @DubboReference
    private UnlockFlowWashSaleApi              unlockFlowWashSaleApi;
    @DubboReference
    private UnlockSaleRuleApi                  unlockSaleRuleApi;
    @DubboReference
    private UnlockSaleBusinessApi              unlockSaleBusinessApi;
    @DubboReference
    private UnlockSaleCustomerApi              unlockSaleCustomerApi;
    @DubboReference
    private UnlockSaleCustomerRangeApi         unlockSaleCustomerRangeApi;
    @DubboReference
    private UnlockSaleCustomerRangeProvinceApi unlockSaleCustomerRangeProvinceApi;
    @DubboReference
    private UnlockSaleGoodsApi                 unlockSaleGoodsApi;
    @DubboReference
    private UnlockSaleGoodsCategoryApi         unlockSaleGoodsCategoryApi;
    @DubboReference
    private UnlockSaleGoodsTagApi              unlockSaleGoodsTagApi;
    @DubboReference
    private UnlockSaleDepartmentApi            unlockSaleDepartmentApi;
    @DubboReference
    private CrmGoodsGroupApi                   crmGoodsGroupApi;
    @DubboReference
    private CrmEnterpriseRelationShipApi       crmEnterpriseRelationShipApi;
    @DubboReference
    private FlowWashReportApi                  flowWashReportApi;
    @DubboReference
    private CrmGoodsTagApi                     crmGoodsTagApi;
    @DubboReference
    private CrmGoodsCategoryApi                crmGoodsCategoryApi;
    @DubboReference
    private CrmGoodsInfoApi                    crmGoodsInfoApi;
    @DubboReference
    private UnlockFlowWashTaskApi              unlockFlowWashTaskApi;
    @DubboReference(timeout = 1000 * 60)
    private FlowWashSaleReportApi              flowWashSaleReportApi;
    @DubboReference(timeout = 1000 * 60)
    private FlowSaleWashApi                    flowSaleWashApi;
    @DubboReference
    private UnlockThirdRecordApi               unlockThirdRecordApi;
    @DubboReference
    private UnlockAreaRecordApi                unlockAreaRecordApi;
    @DubboReference
    private UnlockCollectionDetailApi          unlockCollectionDetailApi;
    @DubboReference
    private CrmSupplierApi                     crmSupplierApi;

    @Autowired(required = false)
    private RocketMqProducerService rocketMqProducerService;

    public void wash(Long ufwtId) {
        // 获取清洗日程
        UnlockFlowWashTaskDTO unlockFlowWashTaskDTO = unlockFlowWashTaskApi.getById(ufwtId);
        if (unlockFlowWashTaskDTO == null) {
            log.error("非锁流向清洗，ufwtId={}，unlockFlowWashTask不存在", ufwtId);
            return;
        }

        if (unlockFlowWashTaskDTO.getRuleStatus() != 1) {
            log.error("非锁流向清洗，ufwtId={}，任务状态不对", ufwtId);
            return;
        }

        SaveOrUpdateUnlockCustomerWashTaskRequest request = new SaveOrUpdateUnlockCustomerWashTaskRequest();
        request.setId(unlockFlowWashTaskDTO.getId());
        request.setRuleStatus(2);
        unlockFlowWashTaskApi.saveUnlockFlowWashTask(request);

        CrmEnterpriseDTO crmEnterpriseDO = null;
        try {
            crmEnterpriseDO = crmEnterpriseApi
                    .getCrmEnterpriseBackById(unlockFlowWashTaskDTO.getCrmEnterpriseId(), BackupUtil.generateTableSuffix(unlockFlowWashTaskDTO.getYear(), unlockFlowWashTaskDTO.getMonth()));
        } catch (Exception e) {
            log.error("非锁流向清洗，unlockFlowWashTask={}，crmEnterprise查询失败，year={}, month={}", unlockFlowWashTaskDTO.getId(), unlockFlowWashTaskDTO.getYear(), unlockFlowWashTaskDTO.getMonth(), e);
            return;
        }

        if (crmEnterpriseDO == null) {
            log.error("非锁流向清洗，unlockFlowWashTask={}，crmEnterprise备份数据不存在，year={}, month={}", unlockFlowWashTaskDTO.getId(), unlockFlowWashTaskDTO.getYear(), unlockFlowWashTaskDTO.getMonth());
            return;
        }

        //获取本次需要清洗的非锁规则
        List<UnlockSaleRuleDTO> unlockSaleRuleDOList = unlockSaleRuleApi.getUnlockSaleRuleList();
        List<Long> ids = unlockSaleRuleDOList.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<UnlockSaleCustomerRangeDTO> unlockSaleCustomerRangeDTOS = unlockSaleCustomerRangeApi.getUnlockSaleCustomerRangeByRuleIds(ids);
        Map<Long, UnlockSaleCustomerRangeDTO> unlockSaleCustomerRangeDTOMap = unlockSaleCustomerRangeDTOS.stream().collect(Collectors.toMap(UnlockSaleCustomerRangeDTO::getRuleId, Function.identity()));
        unlockSaleCustomerRangeDTOS.clear();
        List<UnlockSaleDepartmentDTO> unlockSaleDepartmentDTOS = unlockSaleDepartmentApi.getUnlockSaleDepartmentByRuleIds(ids);
        Map<Long, UnlockSaleDepartmentDTO> unlockSaleDepartmentDTOMap = unlockSaleDepartmentDTOS.stream().collect(Collectors.toMap(UnlockSaleDepartmentDTO::getRuleId, Function.identity()));
        unlockSaleDepartmentDTOS.clear();
        List<CrmGoodsInfoDTO> crmGoodsInfoDTOList = crmGoodsInfoApi.getBakCrmGoodsInfoAll(BackupUtil.generateTableSuffix(unlockFlowWashTaskDTO.getYear(), unlockFlowWashTaskDTO.getMonth()));
        Map<Long, CrmGoodsInfoDTO> crmGoodsInfoDTOMap = crmGoodsInfoDTOList.stream().collect(Collectors.toMap(CrmGoodsInfoDTO::getGoodsCode, Function.identity()));
        crmGoodsInfoDTOList.clear();
        //  获取该次任务数据
        List<UnlockFlowWashSaleDTO> unlockFlowWashSaleDTOList = unlockFlowWashSaleApi.getListByUfwtId(ufwtId);
        for (UnlockFlowWashSaleDTO unlockFlowWashSaleDTO : unlockFlowWashSaleDTOList) {
            //先清空计入数据
            unlockFlowWashSaleDTO.setOperateStatus(1);
            unlockFlowWashSaleDTO.setUnlockSaleRuleId(0L);
            unlockFlowWashSaleDTO.setRuleNotes("");
            unlockFlowWashSaleDTO.setJudgment(0);
            unlockFlowWashSaleDTO.setDistributionStatus(1);
            unlockFlowWashSaleDTO.setDistributionSource(0);
            unlockFlowWashSaleDTO.setDepartment("");
            unlockFlowWashSaleDTO.setBusinessDepartment("");
            unlockFlowWashSaleDTO.setProvincialArea("");
            unlockFlowWashSaleDTO.setBusinessProvince("");
            unlockFlowWashSaleDTO.setRepresentativeCode("");
            unlockFlowWashSaleDTO.setRepresentativeName("");
            unlockFlowWashSaleDTO.setSuperiorSupervisorCode("");
            unlockFlowWashSaleDTO.setSuperiorSupervisorName("");
            unlockFlowWashSaleDTO.setDistrictCounty("");
            unlockFlowWashSaleDTO.setDistrictCountyCode("");
            unlockFlowWashSaleDTO.setPostCode(0L);
            unlockFlowWashSaleDTO.setPostName("");
            unlockFlowWashSaleDTO.setRemark("");
            UnlockSaleRuleDTO unlockSaleRuleDTO = distributionRule(unlockSaleRuleDOList, unlockSaleCustomerRangeDTOMap, crmGoodsInfoDTOMap.get(unlockFlowWashSaleDTO.getGoodsCode()), unlockFlowWashSaleDTO, unlockFlowWashTaskDTO);
            if (unlockSaleRuleDTO != null) {
                fillUnlockFlowWashSale(unlockSaleRuleDTO, unlockSaleDepartmentDTOMap.get(unlockSaleRuleDTO.getId()), unlockFlowWashSaleDTO);
                unlockFlowWashSaleDTO.setDistributionSource(1);
                unlockFlowWashSaleDTO.setDistributionStatus(2);
                unlockFlowWashSaleDTO.setUpdateTime(new Date());
                unlockFlowWashSaleDTO.setUpdateUser(0L);
                unlockFlowWashSaleDTO.setRemark("规则ID:" + unlockSaleRuleDTO.getCode());
            }
        }
        //更新非锁流向和销售合并报表
        updateUnlockFlowWashSale(unlockFlowWashSaleDTOList);
    }

    public void updateUnlockFlowWashSale(List<UnlockFlowWashSaleDTO> unlockFlowWashSaleDTOList) {
        //更新非锁流向
        List<UpdateFlowWashSaleReportRequest.UpdateFlowWashSaleReportDetailRequest> detailRequests = new ArrayList<>();
        List<SaveUnlockFlowWashSaleRequest> saveUnlockFlowWashSaleRequestList = new ArrayList<>();
        for (UnlockFlowWashSaleDTO unlockFlowWashSaleDTO : unlockFlowWashSaleDTOList) {
            //匹配成功
            if (unlockFlowWashSaleDTO.getDistributionStatus() == 2) {
                SaveUnlockFlowWashSaleRequest saveUnlockFlowWashSaleRequest = new SaveUnlockFlowWashSaleRequest();
                saveUnlockFlowWashSaleRequest.setId(unlockFlowWashSaleDTO.getId());
                saveUnlockFlowWashSaleRequest.setOperateStatus(unlockFlowWashSaleDTO.getOperateStatus());
                saveUnlockFlowWashSaleRequest.setUnlockSaleRuleId(unlockFlowWashSaleDTO.getUnlockSaleRuleId());
                saveUnlockFlowWashSaleRequest.setRuleNotes(unlockFlowWashSaleDTO.getRuleNotes());
                saveUnlockFlowWashSaleRequest.setJudgment(unlockFlowWashSaleDTO.getJudgment());
                saveUnlockFlowWashSaleRequest.setDistributionStatus(unlockFlowWashSaleDTO.getDistributionStatus());
                saveUnlockFlowWashSaleRequest.setDistributionSource(unlockFlowWashSaleDTO.getDistributionSource());

                //部门
                saveUnlockFlowWashSaleRequest.setDepartment(unlockFlowWashSaleDTO.getDepartment());
                //业务部门
                saveUnlockFlowWashSaleRequest.setBusinessDepartment(unlockFlowWashSaleDTO.getBusinessDepartment());
                saveUnlockFlowWashSaleRequest.setProvincialArea(unlockFlowWashSaleDTO.getProvincialArea());
                saveUnlockFlowWashSaleRequest.setBusinessProvince(unlockFlowWashSaleDTO.getBusinessProvince());
                saveUnlockFlowWashSaleRequest.setRepresentativeCode(unlockFlowWashSaleDTO.getRepresentativeCode());
                saveUnlockFlowWashSaleRequest.setRepresentativeName(unlockFlowWashSaleDTO.getRepresentativeName());
                saveUnlockFlowWashSaleRequest.setSuperiorSupervisorCode(unlockFlowWashSaleDTO.getSuperiorSupervisorCode());
                saveUnlockFlowWashSaleRequest.setSuperiorSupervisorName(unlockFlowWashSaleDTO.getSuperiorSupervisorName());
                saveUnlockFlowWashSaleRequest.setDistrictCounty(unlockFlowWashSaleDTO.getDistrictCounty());
                saveUnlockFlowWashSaleRequest.setDistrictCountyCode(unlockFlowWashSaleDTO.getDistrictCountyCode());
                saveUnlockFlowWashSaleRequest.setPostCode(unlockFlowWashSaleDTO.getPostCode());
                saveUnlockFlowWashSaleRequest.setPostName(unlockFlowWashSaleDTO.getPostName());
                saveUnlockFlowWashSaleRequest.setRemark(unlockFlowWashSaleDTO.getRemark());
                saveUnlockFlowWashSaleRequest.setSalesPrice(unlockFlowWashSaleDTO.getSalesPrice());
                saveUnlockFlowWashSaleRequest.setSoTotalAmount(unlockFlowWashSaleDTO.getSoTotalAmount());
                saveUnlockFlowWashSaleRequest.setUpdateTime(unlockFlowWashSaleDTO.getUpdateTime());
                saveUnlockFlowWashSaleRequest.setUpdateUser(unlockFlowWashSaleDTO.getUpdateUser());
                saveUnlockFlowWashSaleRequestList.add(saveUnlockFlowWashSaleRequest);

                UpdateFlowWashSaleReportRequest.UpdateFlowWashSaleReportDetailRequest updateFlowWashSaleReportDetailRequest = new UpdateFlowWashSaleReportRequest.UpdateFlowWashSaleReportDetailRequest();
                //部门
                updateFlowWashSaleReportDetailRequest.setDepartment(unlockFlowWashSaleDTO.getDepartment());
                //业务部门
                updateFlowWashSaleReportDetailRequest.setBusinessDepartment(unlockFlowWashSaleDTO.getBusinessDepartment());
                updateFlowWashSaleReportDetailRequest.setProvincialArea(unlockFlowWashSaleDTO.getProvincialArea());
                updateFlowWashSaleReportDetailRequest.setBusinessProvince(unlockFlowWashSaleDTO.getBusinessProvince());
                updateFlowWashSaleReportDetailRequest.setRepresentativeCode(unlockFlowWashSaleDTO.getRepresentativeCode());
                updateFlowWashSaleReportDetailRequest.setRepresentativeName(unlockFlowWashSaleDTO.getRepresentativeName());
                updateFlowWashSaleReportDetailRequest.setSuperiorSupervisorCode(unlockFlowWashSaleDTO.getSuperiorSupervisorCode());
                updateFlowWashSaleReportDetailRequest.setSuperiorSupervisorName(unlockFlowWashSaleDTO.getSuperiorSupervisorName());
                saveUnlockFlowWashSaleRequest.setDistrictCounty(unlockFlowWashSaleDTO.getDistrictCounty());
                saveUnlockFlowWashSaleRequest.setDistrictCountyCode(unlockFlowWashSaleDTO.getDistrictCountyCode());
                updateFlowWashSaleReportDetailRequest.setPostCode(unlockFlowWashSaleDTO.getPostCode());
                updateFlowWashSaleReportDetailRequest.setPostName(unlockFlowWashSaleDTO.getPostName());
                updateFlowWashSaleReportDetailRequest.setSalesPrice(unlockFlowWashSaleDTO.getSalesPrice());
                updateFlowWashSaleReportDetailRequest.setSoTotalAmount(unlockFlowWashSaleDTO.getSoTotalAmount());
                updateFlowWashSaleReportDetailRequest.setId(unlockFlowWashSaleDTO.getFlowWashSaleReportId());
                updateFlowWashSaleReportDetailRequest.setOpUserId(unlockFlowWashSaleDTO.getUpdateUser());
                detailRequests.add(updateFlowWashSaleReportDetailRequest);
            }
        }
        if (CollUtil.isNotEmpty(saveUnlockFlowWashSaleRequestList)) {
            unlockFlowWashSaleApi.updateBatchById(saveUnlockFlowWashSaleRequestList);
        }
        //刷新销售合并报表
        if (CollUtil.isNotEmpty(detailRequests)) {
            UpdateFlowWashSaleReportRequest updateFlowWashSaleReportRequest = new UpdateFlowWashSaleReportRequest();
            updateFlowWashSaleReportRequest.setDetailRequests(detailRequests);
            flowWashSaleReportApi.updateDepartmentInfo(updateFlowWashSaleReportRequest);
        }
    }


    /**
     * 非锁销售流向分配
     *
     * @param unlockSaleRuleDOList
     * @param entity
     */
    private UnlockSaleRuleDTO distributionRule(List<UnlockSaleRuleDTO> unlockSaleRuleDOList, Map<Long, UnlockSaleCustomerRangeDTO> unlockSaleCustomerRangeDTOMap, CrmGoodsInfoDTO crmGoodsInfoDTO, UnlockFlowWashSaleDTO entity, UnlockFlowWashTaskDTO unlockFlowWashTaskDTO) {
        boolean bool = false;
        UnlockSaleRuleDTO disUnlockSaleRuleDO = null;
        for (UnlockSaleRuleDTO unlockSaleRuleDO : unlockSaleRuleDOList) {
            if (UnlockSaleRuleSourceEnum.MANUAL.getCode().equals(unlockSaleRuleDO.getSource())) {
                //返回true匹配成功
                bool = matchManualUnlockSaleRuleMANUAL(entity, unlockSaleRuleDO, unlockSaleCustomerRangeDTOMap.get(unlockSaleRuleDO.getId()), crmGoodsInfoDTO, unlockFlowWashTaskDTO);
                if (bool) {
                    disUnlockSaleRuleDO = unlockSaleRuleDO;
                    break;
                }
            }
            if (UnlockSaleRuleSourceEnum.SMALL_BUSINESS.getCode().equals(unlockSaleRuleDO.getSource())) {
                bool = matchSmallWholeSale(entity, unlockSaleRuleDO, unlockFlowWashTaskDTO);
                if (bool) {
                    disUnlockSaleRuleDO = unlockSaleRuleDO;
                    //填充非锁流向信息
                    break;
                }
            }
            if (UnlockSaleRuleSourceEnum.REGION_FILINGS.getCode().equals(unlockSaleRuleDO.getSource())) {
                bool = matchFilings(entity, unlockSaleRuleDO, crmGoodsInfoDTO, unlockFlowWashTaskDTO);
                if (bool) {
                    disUnlockSaleRuleDO = unlockSaleRuleDO;
                    //填充非锁流向信息
                    break;
                }
            }
            if (UnlockSaleRuleSourceEnum.LARGE_PURCHASE.getCode().equals(unlockSaleRuleDO.getSource())) {
                bool = matchLargePurchase(entity, unlockSaleRuleDO, unlockFlowWashTaskDTO);
                if (bool) {
                    disUnlockSaleRuleDO = unlockSaleRuleDO;
                    //填充非锁流向信息
                    break;
                }
            }
            if (UnlockSaleRuleSourceEnum.SYSTEM.getCode().equals(unlockSaleRuleDO.getSource())) {
                bool = matchSystem(entity, unlockSaleRuleDO, unlockFlowWashTaskDTO);
                if (bool) {
                    disUnlockSaleRuleDO = unlockSaleRuleDO;
                    //填充非锁流向信息
                    break;
                }
            }
        }
        return disUnlockSaleRuleDO;
    }

    public void fillUnlockFlowWashSale(UnlockSaleRuleDTO unlockSaleRuleDTO, UnlockSaleDepartmentDTO unlockSaleDepartmentDTO, UnlockFlowWashSaleDTO entity) {
        /**
         * 来源：1-手动设置2-小三批备案3-区域备案4-集采明细
         */
        Integer source = unlockSaleRuleDTO.getSource();
        if (source == 1||source == 5) {
            // 销量计入规则：1-指定部门2-指定部门+省区3-商业公司三者关系4-商业公司负责人
            if (unlockSaleRuleDTO.getSaleRange() == 1) {
                //部门
                entity.setDepartment(unlockSaleDepartmentDTO.getDepartmentName());
                //业务部门
                entity.setBusinessDepartment(unlockSaleDepartmentDTO.getBusinessDepartmentName());
            }
            if (unlockSaleRuleDTO.getSaleRange() == 2) {
                entity.setDepartment(unlockSaleDepartmentDTO.getDepartmentName());
                entity.setBusinessDepartment(unlockSaleDepartmentDTO.getBusinessDepartmentName());
                //商务负责人省区
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpIdOrJobId(entity.getCommerceJobNumber(), null, BackupUtil.generateTableSuffix(Integer.parseInt(entity.getYear()), Integer.parseInt(entity.getMonth())));
                if (esbEmployeeDTO != null) {
                    //业务省区
                    entity.setBusinessProvince(esbEmployeeDTO.getYxProvince());
                    //省区
                }
            }
            if (unlockSaleRuleDTO.getSaleRange() == 3) {
                //业务代表、主管、业务省区、业务部门、省区、部门
                List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipDTOList = crmEnterpriseRelationShipApi.listSuffixByIdList(Arrays.asList(entity.getEnterpriseCersId()), BackupUtil.generateTableSuffix(Integer.parseInt(entity.getYear()), Integer.parseInt(entity.getMonth())));
                if (CollUtil.isNotEmpty(crmEnterpriseRelationShipDTOList)) {
                    CrmEnterpriseRelationShipDTO crmEnterpriseRelationShipDTO = crmEnterpriseRelationShipDTOList.get(0);
                    //部门
                    entity.setDepartment(crmEnterpriseRelationShipDTO.getDepartment());
                    //业务部门
                    entity.setBusinessDepartment(crmEnterpriseRelationShipDTO.getBusinessDepartment());
                    //省区
                    entity.setProvincialArea(crmEnterpriseRelationShipDTO.getProvincialArea());
                    //业务省区
                    entity.setBusinessProvince(crmEnterpriseRelationShipDTO.getBusinessProvince());
                    entity.setRepresentativeCode(crmEnterpriseRelationShipDTO.getRepresentativeCode());
                    entity.setRepresentativeName(crmEnterpriseRelationShipDTO.getRepresentativeName());
                    entity.setSuperiorSupervisorCode(crmEnterpriseRelationShipDTO.getSuperiorSupervisorCode());
                    entity.setSuperiorSupervisorName(crmEnterpriseRelationShipDTO.getSuperiorSupervisorName());
                    entity.setDistrictCounty(crmEnterpriseRelationShipDTO.getBusinessArea());
                    entity.setDistrictCountyCode(crmEnterpriseRelationShipDTO.getBusinessAreaCode());
                    entity.setPostCode(crmEnterpriseRelationShipDTO.getPostCode());
                    entity.setPostName(crmEnterpriseRelationShipDTO.getPostName());
                }
            }
            if (unlockSaleRuleDTO.getSaleRange() == 4) {
                //商务负责人省区
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpIdOrJobId(entity.getCommerceJobNumber(), null, BackupUtil.generateTableSuffix(Integer.parseInt(entity.getYear()), Integer.parseInt(entity.getMonth())));
                if (esbEmployeeDTO != null) {
                    //部门
                    entity.setDepartment(esbEmployeeDTO.getDeptName());
                    //业务部门
                    entity.setBusinessDepartment(esbEmployeeDTO.getYxDept());
                    //省区
                    //业务省区
                    entity.setBusinessProvince(esbEmployeeDTO.getYxProvince());
                    entity.setRepresentativeCode(esbEmployeeDTO.getEmpId());
                    entity.setRepresentativeName(esbEmployeeDTO.getEmpName());
                    entity.setPostName(esbEmployeeDTO.getJobName());
                    entity.setPostCode(esbEmployeeDTO.getJobId());
                    entity.setDistrictCounty(esbEmployeeDTO.getYxArea());
                    // 获取上级主管信息
                    String superiorEmpId = esbEmployeeDTO.getSuperior();
                    if (StringUtils.isNotEmpty(superiorEmpId)) {
                        EsbEmployeeDTO superiorEsbEmployeeDTO = esbEmployeeApi.getByEmpIdOrJobId(superiorEmpId, null, BackupUtil.generateTableSuffix(Integer.parseInt(entity.getYear()), Integer.parseInt(entity.getMonth())));
                        if (superiorEsbEmployeeDTO != null) {
                            entity.setSuperiorSupervisorCode(superiorEsbEmployeeDTO.getEmpId());
                            entity.setSuperiorSupervisorName(superiorEsbEmployeeDTO.getEmpName());
                        }
                    }
                }
            }
            entity.setUnlockSaleRuleId(unlockSaleRuleDTO.getId());
            entity.setRuleNotes(unlockSaleRuleDTO.getNotes());
            entity.setJudgment(unlockSaleRuleDTO.getJudgment());
        } else if (source == 2) {
            entity.setUnlockSaleRuleId(unlockSaleRuleDTO.getId());
            entity.setRuleNotes(unlockSaleRuleDTO.getNotes());
            entity.setJudgment(unlockSaleRuleDTO.getJudgment());
        } else if (source == 3) {
            entity.setUnlockSaleRuleId(unlockSaleRuleDTO.getId());
            entity.setRuleNotes(unlockSaleRuleDTO.getNotes());
            entity.setJudgment(unlockSaleRuleDTO.getJudgment());
        } else if (source == 4) {
            entity.setUnlockSaleRuleId(unlockSaleRuleDTO.getId());
            entity.setRuleNotes(unlockSaleRuleDTO.getNotes());
            entity.setJudgment(unlockSaleRuleDTO.getJudgment());
        }
    }

    /**
     * 匹配集采设置规则
     *
     * @param entity
     * @param unlockSaleRuleDO
     * @param unlockFlowWashTaskDTO
     * @return false不满足条件 true满足条件
     */
    private boolean matchLargePurchase(UnlockFlowWashSaleDTO entity, UnlockSaleRuleDTO unlockSaleRuleDO, UnlockFlowWashTaskDTO unlockFlowWashTaskDTO) {
        if (entity.getGoodsCode() == null || entity.getGoodsCode() == 0) {
            return false;
        }
        //商业公司区域+产品编码
        UnlockCollectionDetailDTO unlockCollectionDetailDTO = unlockCollectionDetailApi.getByCrmGoodsCodeAndRegionCode(entity.getGoodsCode(), entity.getRegionCode());
        //返回对应集采信息
        if (unlockCollectionDetailDTO == null) {
            return false;
        }
        entity.setSalesPrice(unlockCollectionDetailDTO.getCollectionPrice());
        entity.setSoTotalAmount(unlockCollectionDetailDTO.getCollectionPrice().multiply(entity.getSoQuantity()));
        entity.setDepartment("普药集采");
        entity.setBusinessDepartment("普药集采");
        //回写集采价格
        return true;
    }

    /**
     * 系统内置
     *
     * @param entity
     * @param unlockSaleRuleDO
     * @param unlockFlowWashTaskDTO
     * @return false不满足条件 true满足条件
     */
    private boolean matchSystem(UnlockFlowWashSaleDTO entity, UnlockSaleRuleDTO unlockSaleRuleDO, UnlockFlowWashTaskDTO unlockFlowWashTaskDTO) {
        //执行逻辑：非锁流向中，如果客户未对照，但原始客户名称 与 商业档案中的机构名称完全一致，且连锁总部为“是”，商业公司档案为“无效”，则部门为“渠道内”，判断为“不计入”，非锁备注为“渠道内销量”。'
        Long customerCrmId = entity.getCustomerCrmId();
        if (customerCrmId == null || customerCrmId == 0) {
            return false;
        }
        QueryCrmAgencyCountRequest request = new QueryCrmAgencyCountRequest();
        request.setSupplyChainRole(CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode());
        request.setName(entity.getOriginalEnterpriseName());
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getBakFirstCrmEnterpriseInfo(request, BackupUtil.generateTableSuffix(Integer.parseInt(entity.getYear()), Integer.parseInt(entity.getMonth())));
        if (crmEnterpriseDTO == null) {
            return false;
        }

        if (crmEnterpriseDTO.getBusinessCode() != 2) {
            return false;
        }

        CrmSupplierDTO crmSupplierDTO = crmSupplierApi.getBakCrmSupplierByCrmEnterId(crmEnterpriseDTO.getId(), BackupUtil.generateTableSuffix(Integer.parseInt(entity.getYear()), Integer.parseInt(entity.getMonth())));
        if (crmSupplierDTO == null) {
            return false;
        }

        if (crmSupplierDTO.getHeadChainFlag() != 1) {
            return false;
        }
        return true;
    }

    /**
     * 匹配备案设置规则
     *
     * @param entity
     * @param unlockSaleRuleDO
     * @param unlockFlowWashTaskDTO
     * @return false不满足条件 true满足条件
     */
    private boolean matchFilings(UnlockFlowWashSaleDTO entity, UnlockSaleRuleDTO unlockSaleRuleDO, CrmGoodsInfoDTO crmGoodsInfoDTO, UnlockFlowWashTaskDTO unlockFlowWashTaskDTO) {
        //传一个非锁客户分类+品种+商业公司区域
        if (entity.getGoodsCode() == null || entity.getGoodsCode() == 0) {
            return false;
        }

        if (crmGoodsInfoDTO.getCategoryId() == null || crmGoodsInfoDTO.getCategoryId() == 0) {
            return false;
        }

        Long categoryId = crmGoodsCategoryApi.findFirstCategoryByFinal(crmGoodsInfoDTO.getCategoryId(), BackupUtil.generateTableSuffix(unlockFlowWashTaskDTO.getYear(), unlockFlowWashTaskDTO.getMonth()));

        //返回对应备案信息
        UnlockAreaRecordDTO unlockAreaRecordDTO = unlockAreaRecordApi.getByClassAndCategoryIdAndRegionCode(entity.getCustomerClassification(), categoryId, entity.getRegionCode());
        if (unlockAreaRecordDTO == null) {
            return false;
        }
        //通过商业三者关系回填计入员工代表，上级主管，部门，省区信息 销量计入类型：1-销量计入主管 2-销量计入代表
        //（1）如果销量计入代表，则非锁流向中，根据设置的代表岗位，自动关联出部门、省区、区办、上级主管等信息。
        //（2）如果销量计入主管，则根据主管岗位关联出部门、省区、区办等信息，把主管信息填入上级主管工号、上级主管姓名字段，业务代表工号和业务代表姓名为空！！！
        if (unlockAreaRecordDTO.getType() == 2) {
            entity.setPostCode(Convert.toLong(unlockAreaRecordDTO.getRepresentativePostCode(), 0L));
            entity.setPostName(unlockAreaRecordDTO.getRepresentativePostName());
            entity.setRepresentativeName(unlockAreaRecordDTO.getRepresentativeName());
            entity.setRepresentativeCode(unlockAreaRecordDTO.getRepresentativeCode());
            entity.setSuperiorSupervisorCode(unlockAreaRecordDTO.getExecutiveCode());
            entity.setSuperiorSupervisorName(unlockAreaRecordDTO.getExecutiveName());
            entity.setBusinessDepartment(unlockAreaRecordDTO.getDepartment());
            entity.setBusinessProvince(unlockAreaRecordDTO.getProvince());
            entity.setDistrictCounty(unlockAreaRecordDTO.getArea());
            EsbEmployeeDTO superiorEsbEmployeeDTO = esbEmployeeApi.getByEmpIdOrJobId(unlockAreaRecordDTO.getRepresentativeCode(), null, BackupUtil.generateTableSuffix(Integer.parseInt(entity.getYear()), Integer.parseInt(entity.getMonth())));
            if (superiorEsbEmployeeDTO != null) {
                entity.setDepartment(superiorEsbEmployeeDTO.getDeptName());
            }
        } else if (unlockAreaRecordDTO.getType() == 1) {
            //主管岗位
            entity.setPostCode(Convert.toLong(unlockAreaRecordDTO.getExecutivePostCode(), 0L));
            entity.setPostName(unlockAreaRecordDTO.getExecutivePostName());
            entity.setRepresentativeName(unlockAreaRecordDTO.getExecutiveName());
            entity.setRepresentativeCode(unlockAreaRecordDTO.getExecutiveCode());
            entity.setBusinessDepartment(unlockAreaRecordDTO.getDepartment());
            entity.setBusinessProvince(unlockAreaRecordDTO.getProvince());
            entity.setDistrictCounty(unlockAreaRecordDTO.getArea());
            EsbEmployeeDTO superiorEsbEmployeeDTO = esbEmployeeApi.getByEmpIdOrJobId(unlockAreaRecordDTO.getExecutiveCode(), null, BackupUtil.generateTableSuffix(Integer.parseInt(entity.getYear()), Integer.parseInt(entity.getMonth())));
            if (superiorEsbEmployeeDTO != null) {
                entity.setDepartment(superiorEsbEmployeeDTO.getDeptName());
                entity.setSuperiorSupervisorCode(superiorEsbEmployeeDTO.getEmpId());
                entity.setSuperiorSupervisorName(superiorEsbEmployeeDTO.getEmpName());
            }
        }
        return true;
    }

    /**
     * 匹配小三批设置规则
     *
     * @param entity
     * @param unlockSaleRuleDO
     * @param unlockFlowWashTaskDTO
     * @return false不满足条件 true满足条件
     */
    private boolean matchSmallWholeSale(UnlockFlowWashSaleDTO entity, UnlockSaleRuleDTO unlockSaleRuleDO, UnlockFlowWashTaskDTO unlockFlowWashTaskDTO) {
        if (entity.getCustomerCrmId() == null || entity.getCustomerCrmId() == 0) {
            return false;
        }

        if (entity.getGoodsCode() == null && entity.getGoodsCode() == 0) {
            return false;
        }

        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseBackById(entity.getCustomerCrmId(), BackupUtil.generateTableSuffix(unlockFlowWashTaskDTO.getYear(), unlockFlowWashTaskDTO.getMonth()));
        if (crmEnterpriseDTO == null) {
            return false;
        }

        //传一个商业的客户ID，返回小三批的配置信息
        UnlockThirdRecordDTO unlockThirdRecordDTO = unlockThirdRecordApi.getByOrgCrmId(entity.getCustomerCrmId());
        if (unlockThirdRecordDTO == null) {
            return false;
        }
        //统计所有流向总金额
        SumFlowWashReportRequest reportRequest = new SumFlowWashReportRequest();
        reportRequest.setCustomerCrmId(entity.getCustomerCrmId());
        reportRequest.setYear(String.valueOf(unlockFlowWashTaskDTO.getYear()));
        reportRequest.setMonth(String.valueOf(unlockFlowWashTaskDTO.getMonth()));
        reportRequest.setFlowClassifyList(Arrays.asList(FlowClassifyEnum.NORMAL.getCode(), FlowClassifyEnum.FLOW_CROSS.getCode(), FlowClassifyEnum.SALE_APPEAL.getCode()));
        BigDecimal total = flowWashSaleReportApi.sumTotalMoney(reportRequest);

        //判断金额金额是否满足 则商业公司销售给当前客户的销量都算作非锁，对应流向不带入任何三者关系
        if (total.compareTo(unlockThirdRecordDTO.getPurchaseQuota().multiply(new BigDecimal(10000))) >= 0) {
            return false;
        } else {
            //则商业公司销售给当前客户的销量，找出所有商业公司的三者关系（注意这里找的是商业公司的三者关系）
            QueryFlowSaleWashListRequest queryFlowSaleWashListRequest = new QueryFlowSaleWashListRequest();
            queryFlowSaleWashListRequest.setYear(unlockFlowWashTaskDTO.getYear());
            queryFlowSaleWashListRequest.setMonth(unlockFlowWashTaskDTO.getMonth());
            queryFlowSaleWashListRequest.setCrmGoodsCode(entity.getGoodsCode());
            queryFlowSaleWashListRequest.setCrmOrganizationId(entity.getCustomerCrmId());
            List<FlowSaleWashDTO> flowSaleWashDTOList = flowSaleWashApi.getByYearMonth(queryFlowSaleWashListRequest);
            List<Long> enterpriseCersIds = flowSaleWashDTOList.stream().map(e -> e.getEnterpriseCersId()).filter(e -> e.longValue() > 0).collect(Collectors.toList());
            // 匹配经销商三者关系
            if (CollUtil.isNotEmpty(enterpriseCersIds)) {
                List<CrmEnterpriseRelationShipDTO> crmEnterpriseRelationShipDTOList = crmEnterpriseRelationShipApi.listSuffixByIdList(enterpriseCersIds, BackupUtil.generateTableSuffix(unlockFlowWashTaskDTO.getYear(), unlockFlowWashTaskDTO.getMonth()));
                if (CollUtil.isEmpty(crmEnterpriseRelationShipDTOList)) {
                    return false;
                }
                JSONArray jsonArray = JSONArray.parseArray(unlockThirdRecordDTO.getEffectiveDepartment());
                Map<String, String> map = jsonArray.stream().collect(Collectors.toMap(object -> {
                            JSONObject item = (JSONObject) object;
                            return item.getString("code");
                        },
                        object -> {
                            JSONObject item = (JSONObject) object;
                            return item.getString("name");
                        }
                ));
                for (CrmEnterpriseRelationShipDTO crmEnterpriseRelationShipDTO : crmEnterpriseRelationShipDTOList) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        //判断商业公司三者关系里面的部门，匹上生效部门就把商业公司的三者关系带入到客户的三者关系里面
                        //通过商业三者关系回填计入员工代表，上级主管，部门，省区信息
                        if (entry.getValue().equals(crmEnterpriseRelationShipDTO.getBusinessDepartment())) {
                            //部门
                            entity.setDepartment(crmEnterpriseRelationShipDTO.getDepartment());
                            //业务部门
                            entity.setBusinessDepartment(crmEnterpriseRelationShipDTO.getBusinessDepartment());
                            entity.setProvincialArea(crmEnterpriseRelationShipDTO.getProvincialArea());
                            entity.setBusinessProvince(crmEnterpriseRelationShipDTO.getBusinessProvince());
                            entity.setRepresentativeCode(crmEnterpriseRelationShipDTO.getRepresentativeCode());
                            entity.setRepresentativeName(crmEnterpriseRelationShipDTO.getRepresentativeName());
                            entity.setPostCode(crmEnterpriseRelationShipDTO.getPostCode());
                            entity.setPostName(crmEnterpriseRelationShipDTO.getPostName());
                            entity.setSuperiorSupervisorCode(crmEnterpriseRelationShipDTO.getSuperiorSupervisorCode());
                            entity.setSuperiorSupervisorName(crmEnterpriseRelationShipDTO.getSuperiorSupervisorName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 匹配手动设置规则
     *
     * @param entity
     * @param unlockSaleRuleDO
     * @param unlockFlowWashTaskDTO
     * @return false不满足条件 true满足条件
     */
    private boolean matchManualUnlockSaleRuleMANUAL(UnlockFlowWashSaleDTO entity, UnlockSaleRuleDTO unlockSaleRuleDO, UnlockSaleCustomerRangeDTO unlockSaleCustomerRangeDTO, CrmGoodsInfoDTO crmGoodsInfoDTO, UnlockFlowWashTaskDTO unlockFlowWashTaskDTO) {
        //商业公司范围：1-全部2-指定商业公司
        Integer businessRange = unlockSaleRuleDO.getBusinessRange();
        if (businessRange == 2) {
            List<Long> crmEnterpriseIds = unlockSaleBusinessApi.getCrmEnterpriseIdByRuleId(unlockSaleRuleDO.getId());
            if (!crmEnterpriseIds.contains(entity.getCrmId())) {
                return false;
            }
        }
        //客户公司范围：1-全部2-指定商业公司3-指定范围
        Integer customerRange = unlockSaleRuleDO.getCustomerRange();
        if (customerRange == 2) {
            List<Long> crmEnterpriseIds = unlockSaleCustomerApi.getCrmEnterpriseIdByRuleId(unlockSaleRuleDO.getId());
            if (!crmEnterpriseIds.contains(entity.getCustomerCrmId())) {
                return false;
            }
        }
        if (customerRange == 3) {
            if (unlockSaleCustomerRangeDTO != null) {
                //范围类型：1省份2分类类型3关键词 取交集
                List<String> provinceCodeList = unlockSaleCustomerRangeProvinceApi.getProvinceListByUscId(unlockSaleCustomerRangeDTO.getId());
                if (CollUtil.isNotEmpty(provinceCodeList)) {
                    if (entity.getCustomerCrmId() == null || entity.getCustomerCrmId() == 0) {
                        return false;
                    }
                    CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseBackById(entity.getCustomerCrmId(), BackupUtil.generateTableSuffix(unlockFlowWashTaskDTO.getYear(), unlockFlowWashTaskDTO.getMonth()));
                    if (!provinceCodeList.contains(crmEnterpriseDTO.getRegionCode())) {
                        return false;
                    }
                }
                String classificationStr = unlockSaleCustomerRangeDTO.getClassification();
                if (StrUtil.isNotEmpty(classificationStr)) {
                    List<String> classificationList = Arrays.asList(classificationStr.split(","));
                    List<Integer> classifications = classificationList.stream().map(Integer::parseInt).collect(Collectors.toList());
                    if (!classifications.contains(entity.getCustomerClassification())) {
                        return false;
                    }
                }
                String keywordStr = unlockSaleCustomerRangeDTO.getKeyword();
                if (StrUtil.isNotEmpty(keywordStr)) {
                    List<String> keywordList = Arrays.asList(keywordStr.split(","));
                    List<String> keywords = keywordList.stream().filter(e -> entity.getOriginalEnterpriseName().contains(e)).collect(Collectors.toList());
                    if (CollUtil.isEmpty(keywords)) {
                        return false;
                    }
                }
            }
        }
        //商品范围：1-全部2-指定商品3-指定标签4-指定品规
        Integer goodsRange = unlockSaleRuleDO.getGoodsRange();
        if (goodsRange == 2) {
            if (entity.getGoodsCode() == null || entity.getGoodsCode() == 0) {
                return false;
            }
            List<Long> goodsCodeList = unlockSaleGoodsCategoryApi.getCrmGoodsCodeByRuleId(unlockSaleRuleDO.getId());
            List<Long> categoryIdAll = new ArrayList<>();
            for (Long categoryId : goodsCodeList) {
                List<Long> categoryIds = new ArrayList<>();
                categoryIds.add(categoryId);
                getNumberByCategory(categoryId, categoryIds, BackupUtil.generateTableSuffix(unlockFlowWashTaskDTO.getYear(), unlockFlowWashTaskDTO.getMonth()));
                categoryIdAll.addAll(categoryIds);
            }
            if (!categoryIdAll.contains(crmGoodsInfoDTO.getCategoryId())) {
                return false;
            }
        }

        // todo 2-指定标签
        if (goodsRange == 3) {
            if (entity.getGoodsCode() == null || entity.getGoodsCode() == 0) {
                return false;
            }
            List<Long> goodsCodeList = unlockSaleGoodsTagApi.getCrmGoodsCodeByRuleId(unlockSaleRuleDO.getId());
            List<CrmGoodsTagDTO> crmGoodsTagDTOList = crmGoodsTagApi.findTagByGoodsId(crmGoodsInfoDTO.getId());
            if (!CollectionUtil.containsAny(goodsCodeList, crmGoodsTagDTOList.stream().map(e -> e.getId()).collect(Collectors.toList()))) {
                return false;
            }
        }
        if (goodsRange == 4) {
            if (entity.getGoodsCode() == null || entity.getGoodsCode() == 0) {
                return false;
            }
            List<Long> goodsCodeList = unlockSaleGoodsApi.getCrmGoodsCodeByRuleId(unlockSaleRuleDO.getId());
            if (!goodsCodeList.contains(entity.getGoodsCode())) {
                return false;
            }
        }
        return true;
    }

    private void getNumberByCategory(Long id, List<Long> ids, String Suffix) {
        //获取子类
        List<CrmGoodsCategoryDTO> crmGoodsCategoryDTOList = crmGoodsCategoryApi.findBakByParentId(id, Suffix);
        if (CollUtil.isNotEmpty(crmGoodsCategoryDTOList)) {
            ids.addAll(crmGoodsCategoryDTOList.stream().map(e -> e.getId()).collect(Collectors.toList()));
            for (CrmGoodsCategoryDTO crmGoodsCategoryDTO : crmGoodsCategoryDTOList) {
                getNumberByCategory(crmGoodsCategoryDTO.getId(), ids, Suffix);
            }
        }
    }

    /**
     * 销售合并报表非锁流向
     *
     * @param crmEnterpriseId
     * @param year
     * @param month
     * @return
     */
    public List<FlowWashSaleReportDTO> pageFlowWashSaleReportDTO(Long crmEnterpriseId, Integer year, Integer month) {
        //查询所有erp对接的企业信息
        List<FlowWashSaleReportDTO> flowWashSaleReportDTOList = new ArrayList<>();
        FlowWashSaleReportPageRequest request = new FlowWashSaleReportPageRequest();
        //查询非锁流向数据
        request.setIsChainFlag(2);
        request.setCrmId(crmEnterpriseId);
        request.setYear(String.valueOf(year));
        request.setMonth(String.valueOf(month));
        //需要循环调用
        Page<FlowWashSaleReportDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(1000);
            page = flowWashReportApi.saleReportPageList(request);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            for (FlowWashSaleReportDTO crmEnterpriseDTO : page.getRecords()) {
                flowWashSaleReportDTOList.add(crmEnterpriseDTO);
            }
            current = current + 1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return flowWashSaleReportDTOList;
    }
}
