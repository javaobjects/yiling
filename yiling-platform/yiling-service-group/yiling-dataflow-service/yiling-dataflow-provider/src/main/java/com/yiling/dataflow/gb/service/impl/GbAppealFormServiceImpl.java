package com.yiling.dataflow.gb.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.agency.service.CrmDepartmentAreaRelationService;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.gb.bo.GbAppealFormEsbInfoBO;
import com.yiling.dataflow.gb.bo.GbAppealFormFlowStatisticBO;
import com.yiling.dataflow.gb.dao.GbAppealFormMapper;
import com.yiling.dataflow.gb.dto.request.DeleteGbAppealFlowAllocationRequest;
import com.yiling.dataflow.gb.dto.request.DeleteGbAppealFlowRelatedRequest;
import com.yiling.dataflow.gb.dto.request.DeleteGbAppealFormRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealFormExecuteEditDetailRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealFormUpdateExecuteStatusRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealSubstractCancleRequest;
import com.yiling.dataflow.gb.dto.request.GbAppealSubstractResultRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormFlowStatisticPageRequest;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormListPageRequest;
import com.yiling.dataflow.gb.dto.request.SaveGbAppealFormRequest;
import com.yiling.dataflow.gb.dto.request.SaveGbAppealFormSaleReportMatchRequest;
import com.yiling.dataflow.gb.dto.request.SubstractGbAppealFlowAllocationRequest;
import com.yiling.dataflow.gb.dto.request.SubstractGbAppealFlowRelatedRequest;
import com.yiling.dataflow.gb.dto.request.SubstractGbAppealFlowStatisticRequest;
import com.yiling.dataflow.gb.dto.request.UpdateGbAppealFormFlowMatchNumberRequest;
import com.yiling.dataflow.gb.entity.GbAppealAllocationDO;
import com.yiling.dataflow.gb.entity.GbAppealFlowRelatedDO;
import com.yiling.dataflow.gb.entity.GbAppealFlowStatisticDO;
import com.yiling.dataflow.gb.entity.GbAppealFormDO;
import com.yiling.dataflow.gb.entity.GbOrderDO;
import com.yiling.dataflow.gb.enums.GbAllocationTypeEnum;
import com.yiling.dataflow.gb.enums.GbDataExecStatusEnum;
import com.yiling.dataflow.gb.enums.GbErrorEnum;
import com.yiling.dataflow.gb.enums.GbExecTypeEnum;
import com.yiling.dataflow.gb.enums.GbOrderExecStatusEnum;
import com.yiling.dataflow.gb.service.GbAppealAllocationService;
import com.yiling.dataflow.gb.service.GbAppealFlowRelatedService;
import com.yiling.dataflow.gb.service.GbAppealFlowStatisticService;
import com.yiling.dataflow.gb.service.GbAppealFormService;
import com.yiling.dataflow.gb.service.GbOrderService;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;
import com.yiling.dataflow.utils.FlowDataIdUtils;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.dataflow.wash.enums.WashStageStatusEnum;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.Constants;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.api.EsbOrganizationApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 流向团购申诉申请表 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-11
 */
@Slf4j
@Service
public class GbAppealFormServiceImpl extends BaseServiceImpl<GbAppealFormMapper, GbAppealFormDO> implements GbAppealFormService {

    @Autowired
    private GbAppealFlowRelatedService gbAppealFlowRelatedService;
    @Autowired
    private GbAppealFlowStatisticService gbAppealFlowStatisticService;
    @Autowired
    private GbAppealAllocationService gbAppealAllocationService;
    @Autowired
    private GbOrderService gbOrderService;
    @Autowired
    private FlowWashSaleReportService flowWashSaleReportService;
    @Autowired
    private FlowMonthWashControlService flowMonthWashControlService;
    @Autowired
    private CrmDepartmentAreaRelationService crmDepartmentAreaRelationService;
    @Autowired
    private CrmEnterpriseService crmEnterpriseService;
    @Autowired
    GbAppealFormMapper gbAppealFormMapper;
    @Autowired
    @Lazy
    private GbAppealFormServiceImpl _this;


    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    EsbOrganizationApi esbOrganizationApi;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Override
    public List<GbAppealFormDO> getListByGbOrderId(Long gbOrderId) {
        LambdaQueryWrapper<GbAppealFormDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(GbAppealFormDO::getGbOrderId, gbOrderId);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public List<GbAppealFormDO> getListByGbOrderIdList(List<Long> gbOrderIds) {
        LambdaQueryWrapper<GbAppealFormDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(GbAppealFormDO::getGbOrderId, gbOrderIds);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean substractMateFlow(GbAppealFormDO gbAppealFormDO, List<GbAppealFlowRelatedDO> gbAppealFlowRelatedDOList, List<GbAppealFlowStatisticDO> gbAppealFlowStatisticDOList, Map<Long, FlowWashSaleReportDTO> flowWashSaleReportMap, GbAppealFormEsbInfoBO gbAppealFormEsbInfoBO, Integer execType, Long opUserId, Date opTime) {
        // 待更新数据
        List<GbAppealSubstractResultRequest> substractResultRequestList = new ArrayList<>();
        // 新增的团购处理结果明细ID列表
        List<Long> allocationIds = new ArrayList<>();
        try {
            // 更新团购流向未匹配数量、匹配数量，通过乐观锁处理如果扣减失败需要回滚
            doSubstract(gbAppealFormDO, gbAppealFlowStatisticDOList, gbAppealFlowRelatedDOList, flowWashSaleReportMap, gbAppealFormEsbInfoBO, opUserId, opTime, substractResultRequestList, allocationIds);

            if (CollUtil.isNotEmpty(allocationIds)) {
                // 更新团购处理申请状态， 自动处理的，已处理
                if (GbExecTypeEnum.AUTO.getCode().equals(execType)) {
                    updateExecuteStatus(gbAppealFormDO.getId(), execType, opUserId, opTime);
                }

                // 新增团购处理流向,通知销售合并报表，TOPIC_GB_FLOW_NOTIFY
                sendMqToFlowSealReport(allocationIds);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("保存扣减数据, 流向数据扣减并发异常, 团购处理申请, id={}", gbAppealFormDO.getId());
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
    }

    private void updateExecuteStatus(Long id, Integer execType, Long opUserId, Date opTime) {
        GbAppealFormUpdateExecuteStatusRequest updateExecuteStatusRequest = new GbAppealFormUpdateExecuteStatusRequest();
        updateExecuteStatusRequest.setId(id);
        updateExecuteStatusRequest.setExecType(execType);
        updateExecuteStatusRequest.setDataExecStatus(GbDataExecStatusEnum.FINISH.getCode());
        updateExecuteStatusRequest.setOpUserId(opUserId);
        updateExecuteStatusRequest.setOpTime(opTime);
        updateByIdAndExecuteStatus(updateExecuteStatusRequest);
    }

    /**
     * 团购流向统计表进行扣减
     * 更新源流向统计未匹配数量、匹配数量
     *
     * @param id
     * @param substractQuantity
     * @param opUserId
     * @param opTime
     */
    private void appealFlowStatisticSubstract(Long id, BigDecimal substractQuantity, String method, Long opUserId, Date opTime) {
        SubstractGbAppealFlowStatisticRequest statisticRequest = new SubstractGbAppealFlowStatisticRequest();
        statisticRequest.setId(id);
        statisticRequest.setSubstractQuantity(substractQuantity);
        statisticRequest.setOpUserId(opUserId);
        statisticRequest.setOpTime(opTime);
        int statisticSubstractCount = gbAppealFlowStatisticService.substract(statisticRequest);
        if (statisticSubstractCount <= 0) {
            log.error(method + ", 更新团购处理源流向流向扣减统计表失败, flowStatisticId:{}, substractQuantity:{}", id, substractQuantity);
            throw new BusinessException(GbErrorEnum.GB_SUBTRACT_ERROR);
        }
    }

    private void appealFlowRelatedSubstract(Long id, BigDecimal substractQuantity, String method, Long opUserId, Date opTime) {
        SubstractGbAppealFlowRelatedRequest relatedRequest = new SubstractGbAppealFlowRelatedRequest();
        relatedRequest.setId(id);
        relatedRequest.setSubstractQuantity(substractQuantity.abs());
        relatedRequest.setOpUserId(opUserId);
        relatedRequest.setOpTime(opTime);
        boolean updateFlag = gbAppealFlowRelatedService.updateById(relatedRequest);
        if (!updateFlag) {
            log.error(method + ", 更新流向团购申诉申请关联流向表失败, gb_appeal_flow_related id:{}, substractQuantity:{}", id, substractQuantity);
            throw new BusinessException(GbErrorEnum.GB_SUBTRACT_ERROR);
        }
    }



    public boolean updateGbAppealFormAndFlowRelated(GbAppealFormDO gbAppealFormDO, Map<Long, FlowWashSaleReportDTO> flowWashSaleReportMap, GbAppealFormEsbInfoBO gbAppealFormEsbInfoBO, List<GbAppealSubstractResultRequest> substractResultRequestList) {
        for (GbAppealSubstractResultRequest substractRequest : substractResultRequestList) {
            // 更新团购流向关联表：gb_appeal_flow_related，团购处理、对应每一条源流向流向的已扣减数量
            appealFlowRelatedSubstract(substractRequest.getGafrId(), substractRequest.getSubstractQuantity(), "添加源流向扣减", substractRequest.getOpUserId(), substractRequest.getOpTime());

            // 更新团购处理申请状态：gb_appeal_flow
            GbAppealFormDO gbAppealFormUpdate = new GbAppealFormDO();
            gbAppealFormUpdate.setId(gbAppealFormDO.getId());
            gbAppealFormUpdate.setDataExecStatus(GbDataExecStatusEnum.FINISH.getCode());
            boolean updateAppealFormFlag = this.updateById(gbAppealFormUpdate);
            if (!updateAppealFormFlag) {
                log.error("添加源流向扣减, 更新流向团购申诉申请表状态失败, gb_appeal_form id:{}, data_exec_status:{}, flow_wash_id:{}, substractQuantity:{}", substractRequest.getAppealFormId(), "3-已处理", substractRequest.getFlowWashId(), substractRequest.getSubstractQuantity());
                throw new BusinessException(GbErrorEnum.GB_SUBTRACT_ERROR);
            }
        }
        return true;
    }

    private SubstractGbAppealFlowAllocationRequest buildGbAppealAllocationAddRequest(GbAppealFormDO gbAppealFormDO, GbAppealSubstractResultRequest substractResultRequest, FlowWashSaleReportDTO flowWashSaleReportDTO, GbAppealFormEsbInfoBO gbAppealFormEsbInfoBO) {
        SubstractGbAppealFlowAllocationRequest allocationRequest = new SubstractGbAppealFlowAllocationRequest();
        allocationRequest.setAppealFormId(substractResultRequest.getAppealFormId());
        allocationRequest.setFlowWashId(substractResultRequest.getFlowWashId());
        allocationRequest.setGafrId(substractResultRequest.getGafrId());
        // 数量，取扣减数量
        allocationRequest.setQuantity(substractResultRequest.getSubstractQuantity());
        // 产品单价，取销售报表的产品单价
        allocationRequest.setPrice(flowWashSaleReportDTO.getSalesPrice());
        // 金额 = 数量 * 产品单价
        BigDecimal totalAmount = allocationRequest.getQuantity().multiply(allocationRequest.getPrice());
        allocationRequest.setTotalAmount(totalAmount);
        allocationRequest.setGbOrderId(gbAppealFormDO.getGbOrderId());
        allocationRequest.setFormId(gbAppealFormDO.getFormId());
        allocationRequest.setGbNo(gbAppealFormDO.getGbNo());
        allocationRequest.setMatchMonth(gbAppealFormDO.getMatchMonth());
        allocationRequest.setGbMonth(gbAppealFormDO.getGbMonth());
        allocationRequest.setProvinceCode(gbAppealFormDO.getProvinceCode());
        allocationRequest.setSoTime(flowWashSaleReportDTO.getSoTime());
        allocationRequest.setCrmId(flowWashSaleReportDTO.getCrmId());
        allocationRequest.setEname(flowWashSaleReportDTO.getEname());
        allocationRequest.setOriginalEnterpriseName(flowWashSaleReportDTO.getOriginalEnterpriseName());
        allocationRequest.setCustomerCrmId(flowWashSaleReportDTO.getCustomerCrmId());
        allocationRequest.setEnterpriseName(flowWashSaleReportDTO.getEnterpriseName());
        allocationRequest.setSoGoodsName(flowWashSaleReportDTO.getSoGoodsName());
        allocationRequest.setSoSpecifications(flowWashSaleReportDTO.getSoSpecifications());
        allocationRequest.setGoodsCode(flowWashSaleReportDTO.getGoodsCode());
        allocationRequest.setGoodsName(flowWashSaleReportDTO.getGoodsName());
        // 根据团购销售计入人工号获取员工、部门、岗位信息
        EsbEmployeeDTO esbEmployeeDTO = Optional.ofNullable(gbAppealFormEsbInfoBO.getEsbEmployeeDTO()).orElse(new EsbEmployeeDTO());
        EsbOrganizationDTO organizationDTO = Optional.ofNullable(gbAppealFormEsbInfoBO.getOrganizationDTO()).orElse(new EsbOrganizationDTO());
        String provinceArea = Optional.ofNullable(gbAppealFormEsbInfoBO.getProvinceArea()).orElse("");
        allocationRequest.setDepartment(Optional.ofNullable(organizationDTO.getOrgName()).orElse(""));
        allocationRequest.setBusinessDepartment(Optional.ofNullable(esbEmployeeDTO.getYxDept()).orElse(""));
        allocationRequest.setProvincialArea(Optional.ofNullable(provinceArea).orElse(""));
        allocationRequest.setBusinessProvince(Optional.ofNullable(esbEmployeeDTO.getYxProvince()).orElse(""));
        Long deptId = Optional.ofNullable(esbEmployeeDTO.getDeptId()).orElse(0L);
        String districtCountyCode = deptId.toString();
        allocationRequest.setDistrictCountyCode(districtCountyCode);
        allocationRequest.setDistrictCounty(Optional.ofNullable(esbEmployeeDTO.getDeptName()).orElse(""));
        allocationRequest.setSuperiorSupervisorCode(Optional.ofNullable(esbEmployeeDTO.getSuperior()).orElse(""));
        allocationRequest.setSuperiorSupervisorName(Optional.ofNullable(esbEmployeeDTO.getSuperiorName()).orElse(""));
        allocationRequest.setRepresentativeCode(Optional.ofNullable(esbEmployeeDTO.getEmpId()).orElse(""));
        allocationRequest.setRepresentativeName(Optional.ofNullable(esbEmployeeDTO.getEmpName()).orElse(""));
        allocationRequest.setPostCode(Optional.ofNullable(esbEmployeeDTO.getJobId()).orElse(0L));
        allocationRequest.setPostName(Optional.ofNullable(esbEmployeeDTO.getJobName()).orElse(""));
        allocationRequest.setAllocationType(GbAllocationTypeEnum.ADD.getCode());
        allocationRequest.setOpUserId(substractResultRequest.getOpUserId());
        allocationRequest.setOpTime(substractResultRequest.getOpTime());
        // 生成新的源流向ID
        String flowKey = FlowDataIdUtils.nextId(FlowClassifyEnum.FLOW_GB.getCode(), FlowTypeEnum.SALE.getCode());
        allocationRequest.setFlowKey(flowKey);
        return allocationRequest;
    }

    private SubstractGbAppealFlowAllocationRequest buildGbAppealAllocationDeductRequest(GbAppealFormDO gbAppealFormDO, GbAppealSubstractResultRequest substractResultRequest, FlowWashSaleReportDTO flowWashSaleReportDTO) {
        SubstractGbAppealFlowAllocationRequest allocationRequest = new SubstractGbAppealFlowAllocationRequest();
        allocationRequest.setAppealFormId(substractResultRequest.getAppealFormId());
        allocationRequest.setFlowWashId(substractResultRequest.getFlowWashId());
        allocationRequest.setGafrId(substractResultRequest.getGafrId());
        // 数量，取扣减数量
        allocationRequest.setQuantity(substractResultRequest.getSubstractQuantity().negate());
        // 产品单价，取销售报表的产品单价
        allocationRequest.setPrice(flowWashSaleReportDTO.getSalesPrice());
        // 金额 = 数量 * 产品单价
        BigDecimal totalAmount = allocationRequest.getQuantity().multiply(allocationRequest.getPrice());
        allocationRequest.setTotalAmount(totalAmount);
        allocationRequest.setGbOrderId(gbAppealFormDO.getGbOrderId());
        allocationRequest.setFormId(gbAppealFormDO.getFormId());
        allocationRequest.setGbNo(gbAppealFormDO.getGbNo());
        allocationRequest.setMatchMonth(gbAppealFormDO.getMatchMonth());
        allocationRequest.setGbMonth(gbAppealFormDO.getGbMonth());
        allocationRequest.setProvinceCode(gbAppealFormDO.getProvinceCode());
        allocationRequest.setSoTime(flowWashSaleReportDTO.getSoTime());
        allocationRequest.setCrmId(flowWashSaleReportDTO.getCrmId());
        allocationRequest.setEname(flowWashSaleReportDTO.getEname());
        allocationRequest.setOriginalEnterpriseName(flowWashSaleReportDTO.getOriginalEnterpriseName());
        allocationRequest.setCustomerCrmId(flowWashSaleReportDTO.getCustomerCrmId());
        allocationRequest.setEnterpriseName(flowWashSaleReportDTO.getEnterpriseName());
        allocationRequest.setSoGoodsName(flowWashSaleReportDTO.getSoGoodsName());
        allocationRequest.setSoSpecifications(flowWashSaleReportDTO.getSoSpecifications());
        allocationRequest.setGoodsCode(flowWashSaleReportDTO.getGoodsCode());
        allocationRequest.setGoodsName(flowWashSaleReportDTO.getGoodsName());
        allocationRequest.setDepartment(flowWashSaleReportDTO.getDepartment());
        allocationRequest.setBusinessDepartment(flowWashSaleReportDTO.getBusinessDepartment());
        allocationRequest.setProvincialArea(flowWashSaleReportDTO.getProvincialArea());
        allocationRequest.setBusinessProvince(flowWashSaleReportDTO.getBusinessProvince());
        allocationRequest.setDistrictCountyCode(Optional.ofNullable(flowWashSaleReportDTO.getDistrictCountyCode()).orElse(""));
        allocationRequest.setDistrictCounty(flowWashSaleReportDTO.getDistrictCounty());
        allocationRequest.setSuperiorSupervisorCode(flowWashSaleReportDTO.getSuperiorSupervisorCode());
        allocationRequest.setSuperiorSupervisorName(flowWashSaleReportDTO.getSuperiorSupervisorName());
        allocationRequest.setRepresentativeCode(flowWashSaleReportDTO.getRepresentativeCode());
        allocationRequest.setRepresentativeName(flowWashSaleReportDTO.getRepresentativeName());
        allocationRequest.setPostCode(flowWashSaleReportDTO.getPostCode());
        allocationRequest.setPostName(flowWashSaleReportDTO.getPostName());
        allocationRequest.setAllocationType(GbAllocationTypeEnum.DEDUCT.getCode());
        allocationRequest.setOpUserId(substractResultRequest.getOpUserId());
        allocationRequest.setOpTime(substractResultRequest.getOpTime());
        // 生成新的源流向ID
        String flowKey = FlowDataIdUtils.nextId(FlowClassifyEnum.FLOW_GB.getCode(), FlowTypeEnum.SALE.getCode());
        allocationRequest.setFlowKey(flowKey);
        return allocationRequest;
    }


    /**
     * 流向扣减
     *
     * @param gbAppealFormDO 团购处理
     * @param gbAppealFlowStatisticDOList 团购流向数量统计列表
     * @param gbAppealFlowRelatedDOList 团购流向关联列表
     * @param flowWashSaleReportMap 源流向数据
     * @param gbAppealFormEsbInfoBO esb员工信息
     * @param opUserId 操作人
     * @param opTime 操作时间
     * @param substractResultRequestList 团购流向数量扣减列表待更新数据
     * @return
     */
    private void doSubstract(GbAppealFormDO gbAppealFormDO, List<GbAppealFlowStatisticDO> gbAppealFlowStatisticDOList, List<GbAppealFlowRelatedDO> gbAppealFlowRelatedDOList,
                             Map<Long, FlowWashSaleReportDTO> flowWashSaleReportMap, GbAppealFormEsbInfoBO gbAppealFormEsbInfoBO, Long opUserId, Date opTime,
                             List<GbAppealSubstractResultRequest> substractResultRequestList, List<Long> allocationIds) {
        // 团购处理与源流向关联map，key -> flowWashId, value -> GbAppealFlowRelatedDO
        Map<Long, GbAppealFlowRelatedDO> relatedMap = new HashMap<>();
        if (CollUtil.isNotEmpty(gbAppealFlowRelatedDOList)) {
            relatedMap = gbAppealFlowRelatedDOList.stream().collect(Collectors.toMap(o -> o.getFlowWashId(), o -> o, (k1, k2) -> k1));
        }

        // 待扣减数量, 即团购剩余的未匹配数量 已赋值到 团购数量上面
        BigDecimal totlUnMatchGbQuantity = gbAppealFormDO.getGbQuantity();
        for (GbAppealFlowStatisticDO flowStatistic : gbAppealFlowStatisticDOList) {
            if(totlUnMatchGbQuantity.compareTo(BigDecimal.ZERO)<=0){
                break;
            }

            //直接扣减原流向数据
            BigDecimal substractQuantity;
            if (flowStatistic.getUnMatchQuantity().compareTo(totlUnMatchGbQuantity) >= 0) {
                substractQuantity = totlUnMatchGbQuantity;
                //扣减完毕剩余0
                totlUnMatchGbQuantity=BigDecimal.ZERO;
            } else {
                substractQuantity = flowStatistic.getUnMatchQuantity();
                //扣减
                totlUnMatchGbQuantity = totlUnMatchGbQuantity.subtract(substractQuantity);
            }
            // 流向团购统计匹配数量，更新源流向被占用总数量
            GbAppealSubstractResultRequest substractResultRequest = buildAppealSubstractResultRequest(substractResultRequestList, gbAppealFormDO, flowStatistic, substractQuantity, relatedMap, opUserId, opTime);

            // 更新团购流向扣减统计表：gb_appeal_flow_statistic，未匹配数量、匹配数量
            appealFlowStatisticSubstract(flowStatistic.getId(), substractQuantity, "添加源流向扣减", opUserId, opTime);

            // 更新团购处理源流向关系的已扣减数量
            appealFlowRelatedSubstract(substractResultRequest.getGafrId(), substractQuantity, "添加源流向扣减", opUserId, opTime);

            // 保存团购处理流向结果：gb_appeal_allocation，同时生成扣减、新增
            List<Long> allocationIdList = generateAppealAllocationDetail(gbAppealFormDO, flowStatistic, flowWashSaleReportMap, gbAppealFormEsbInfoBO, substractResultRequest);
            allocationIds.addAll(allocationIdList);
        }
    }

    private GbAppealSubstractResultRequest buildAppealSubstractResultRequest(List<GbAppealSubstractResultRequest> substractResultRequestList, GbAppealFormDO gbAppealFormDO, GbAppealFlowStatisticDO flowStatistic, BigDecimal substractQuantity, Map<Long, GbAppealFlowRelatedDO> relatedMap, Long opUserId, Date opTime) {
        GbAppealSubstractResultRequest substractResultRequest = new GbAppealSubstractResultRequest();
        substractResultRequest.setAppealFlowStatisticId(flowStatistic.getId());
        substractResultRequest.setAppealFormId(gbAppealFormDO.getId());
        substractResultRequest.setFlowWashId(flowStatistic.getFlowWashId());
        substractResultRequest.setSubstractQuantity(substractQuantity);
        substractResultRequest.setOpUserId(opUserId);
        substractResultRequest.setOpTime(opTime);
        GbAppealFlowRelatedDO gbAppealFlowRelatedDO = relatedMap.get(flowStatistic.getFlowWashId());
        substractResultRequest.setGafrId(gbAppealFlowRelatedDO.getId());
        substractResultRequestList.add(substractResultRequest);
        return substractResultRequest;
    }

    /**
     * 扣减后生成团购处理结果明细
     *
     * @param gbAppealFormDO
     * @param flowStatistic
     * @param flowWashSaleReportMap
     * @param gbAppealFormEsbInfoBO
     * @param substractResultRequest
     * @return
     */
    private List<Long> generateAppealAllocationDetail(GbAppealFormDO gbAppealFormDO, GbAppealFlowStatisticDO flowStatistic, Map<Long, FlowWashSaleReportDTO> flowWashSaleReportMap, GbAppealFormEsbInfoBO gbAppealFormEsbInfoBO, GbAppealSubstractResultRequest substractResultRequest) {
        FlowWashSaleReportDTO flowWashSaleReportDTO = flowWashSaleReportMap.get(flowStatistic.getFlowWashId());
        // 扣减Request
        SubstractGbAppealFlowAllocationRequest substractAllocationRequest = buildGbAppealAllocationDeductRequest(gbAppealFormDO, substractResultRequest, flowWashSaleReportDTO);
        // 新增Request
        SubstractGbAppealFlowAllocationRequest addAllocationRequest = buildGbAppealAllocationAddRequest(gbAppealFormDO, substractResultRequest, flowWashSaleReportDTO, gbAppealFormEsbInfoBO);
        // 保存团购流向扣减、增加结果
        List<SubstractGbAppealFlowAllocationRequest> saveAllocationList = new ArrayList<>();
        saveAllocationList.add(substractAllocationRequest);
        saveAllocationList.add(addAllocationRequest);
        List<Long> ids = gbAppealAllocationService.saveOrUpdateAllocationList(saveAllocationList);
        if (ObjectUtil.isEmpty(ids)) {
            log.error("添加源流向扣减, 保存流向团购申诉分配结果表失败, appeal_form_id:{}, flow_wash_id:{}, substractQuantity:{}", gbAppealFormDO.getId(), flowStatistic.getFlowWashId(), addAllocationRequest.getQuantity());
            throw new BusinessException(GbErrorEnum.GB_SUBTRACT_ERROR);
        }
        return ids;
    }


    @Override
    public Page<GbAppealFormDO> listPage(QueryGbAppealFormListPageRequest request) {
        Page<GbAppealFormDO> page = new Page<>(request.getCurrent(), request.getSize());
//        return this.page(page, getListPageQueryWrapper(request));
        return gbAppealFormMapper.listPage(page, request);
    }

    @Override
    public boolean updateByIdAndExecuteStatus(GbAppealFormUpdateExecuteStatusRequest request) {
        Assert.notNull(request.getId(), "参数 id 不能为空");
        Assert.notNull(request.getDataExecStatus(), "参数 dataExecStatus 不能为空");
        GbAppealFormDO gbAppealFormDO = new GbAppealFormDO();
        gbAppealFormDO.setId(request.getId());
        gbAppealFormDO.setDataExecStatus(request.getDataExecStatus());
        gbAppealFormDO.setExecType(request.getExecType());
        gbAppealFormDO.setUpdateUser(request.getOpUserId());
        gbAppealFormDO.setUpdateTime(request.getOpTime());
        gbAppealFormDO.setLastUpdateUser(request.getOpUserId());
        gbAppealFormDO.setLastUpdateTime(request.getOpTime());
        return this.updateById(gbAppealFormDO);
    }

    @Override
    public Page<GbAppealFormFlowStatisticBO> flowStatisticListPage(QueryGbAppealFormFlowStatisticPageRequest request) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getAppealFormId(), "参数 appealFormId 不能为空");
        Page<GbAppealFormFlowStatisticBO> page = new Page<>(request.getCurrent(), request.getSize());
        return this.baseMapper.flowStatisticListPage(request, page);
    }

    @Override
    public BigDecimal getTotalFlowMatchQuantityByAppealFormId(Long appealFormId) {
        Assert.notNull(appealFormId, "参数 appealFormId 不能为空");
        return this.baseMapper.getTotalFlowMatchQuantityByAppealFormId(appealFormId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editGbAppealAllocation(GbAppealFormExecuteEditDetailRequest request, GbAppealFormDO gbAppealForm, GbAppealAllocationDO appealAllocation, GbAppealFlowStatisticDO appealFlowStatistic, GbAppealFlowRelatedDO appealFlowRelated) {
        // 对扣减数量差进行扣减处理
        try {
            boolean editFlag = doEditSubstract(request, gbAppealForm, appealAllocation, appealFlowStatistic, appealFlowRelated);

            // 更新销售报表中对应的团购流向数据，先删除、再新增
            List<Long> allocationIds = ListUtil.toList(request.getAppealAllocationId(), request.getNegateAllocationId());
            if (editFlag && CollUtil.isNotEmpty(allocationIds)) {
                // 删除销售报表团购处理流向
                deleteFlowSaleByAllocationIds(allocationIds);

                // 新增团购处理流向,通知销售合并报表，TOPIC_GB_FLOW_NOTIFY
                sendMqToFlowSealReport(allocationIds);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("编辑扣减数据, 流向数据扣减并发异常, 手动处理, 编辑扣减/新增明细 id={}, 团购处理申请 id={}, 源流向 id={}", request.getAppealAllocationId(), appealAllocation.getAppealFormId(), appealAllocation.getFlowWashId());
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveList(SaveGbAppealFormRequest request, FlowMonthWashControlDTO flowMonthWashControlDTO, List<GbOrderDO> gbOrderList, Map<Long, String> crmEnterpriseMap, Map<String, EsbEmployeeDTO> esbEmployeeMap) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notNull(request.getGbOrderIdList(), "参数 gbOrderIdList 不能为空");

        try {
            // 团购处理申请是否已存在。 系统新增团购跳过团购处理已存在的。用户新增团购已存在团购处理的，把团购数据更新为已处理。
            List<GbAppealFormDO> appealFormList = this.getListByGbOrderIdList(request.getGbOrderIdList());
            if (CollUtil.isNotEmpty(appealFormList)) {
                List<Long> existAppealFormGbOrderIds = appealFormList.stream().map(GbAppealFormDO::getGbOrderId).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(existAppealFormGbOrderIds)) {
                    log.warn("此团购数据已存在团购处理, gbOrderId:{}", existAppealFormGbOrderIds.toString());
                    if (GbExecTypeEnum.ARTIFICIAL.getCode().equals(request.getExecType())) {
                        List<Long> gbOrderUpdateIds = gbOrderList.stream().filter(o -> existAppealFormGbOrderIds.contains(o.getId()) && GbOrderExecStatusEnum.UN_START.getCode().equals(o.getExecStatus())).map(GbOrderDO::getId).collect(Collectors.toList());
                        if (CollUtil.isNotEmpty(gbOrderUpdateIds)) {
                            log.warn("此团购数据已存在团购处理, 且团购数据是未处理, 手动新增团购数据直接改为已处理, gbOrderId:{}", existAppealFormGbOrderIds.toString());
                            // 更新更新团购主表处理状态
                            finishGbOrder(request, gbOrderUpdateIds);
                            // 团购处理更新处理类型为 人工
                            List<GbAppealFormDO> gbAppealFormUpdateList = new ArrayList<>();
                            GbAppealFormDO gbAppealFormupdate;
                            for (GbAppealFormDO gbAppealFormDO : appealFormList) {
                                gbAppealFormupdate = new GbAppealFormDO();
                                buildGbAppealFormupdateExecType(request, gbAppealFormUpdateList, gbAppealFormupdate, gbAppealFormDO);
                            }
                            this.updateBatchById(gbAppealFormUpdateList);
                        }
                    }

                    gbOrderList = gbOrderList.stream().filter(o -> !existAppealFormGbOrderIds.contains(o.getId())).collect(Collectors.toList());
                    if (CollUtil.isEmpty(gbOrderList)) {
                        return true;
                    }
                }
            }

            // 待保存团购处理
            List<GbAppealFormDO> gbAppealFormSaveList = new ArrayList<>();
            GbAppealFormDO gbAppealFormSave;
            List<Long> gbOrderUpdateIds = new ArrayList<>();
            for (GbOrderDO gbOrderDO : gbOrderList) {
                // 团购处理
                gbAppealFormSave = new GbAppealFormDO();
                buildGbAppealFormSave(request, gbOrderDO, crmEnterpriseMap, esbEmployeeMap, gbAppealFormSave, flowMonthWashControlDTO, gbAppealFormSaveList);

                // 手动新增团购数据，创建团购处理后需要把团购数据更新为已处理
                if (GbExecTypeEnum.ARTIFICIAL.getCode().equals(request.getExecType())) {
                    gbOrderUpdateIds.add(gbOrderDO.getId());
                }
            }

            // 保存团购处理
            boolean saveAppealForm = this.saveBatch(gbAppealFormSaveList, 1000);
            if (saveAppealForm) {
                // 更新更新团购主表处理状态
                finishGbOrder(request, gbOrderUpdateIds);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("保存团购处理异常, 团购数据id={}, 处理类型execType={}", request.getGbOrderIdList(), request.getExecType());
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
    }

    private void buildGbAppealFormupdateExecType(SaveGbAppealFormRequest request, List<GbAppealFormDO> gbAppealFormUpdateList, GbAppealFormDO gbAppealFormupdate, GbAppealFormDO gbAppealFormDO) {
        gbAppealFormupdate.setId(gbAppealFormDO.getId());
        gbAppealFormupdate.setExecType(GbExecTypeEnum.ARTIFICIAL.getCode());
        gbAppealFormupdate.setOpUserId(request.getOpUserId());
        gbAppealFormupdate.setOpTime(request.getOpTime());
        gbAppealFormupdate.setLastUpdateUser(request.getOpUserId());
        gbAppealFormupdate.setLastUpdateTime(request.getOpTime());
        gbAppealFormUpdateList.add(gbAppealFormupdate);
    }


    private void finishGbOrder(SaveGbAppealFormRequest request, List<Long> gbOrderUpdateIds) {
        List<GbOrderDO> gbOrderUpdateList = new ArrayList<>();
        GbOrderDO gbOrderupdate;
        for (Long gbOrderId : gbOrderUpdateIds) {
            // 更新团购主表处理状态
            gbOrderupdate = new GbOrderDO();
            buildGbOrderupdate(request.getOpUserId(), request.getOpTime(), gbOrderId, gbOrderupdate);
            gbOrderUpdateList.add(gbOrderupdate);

        }
        // 更新团购主表处理状态为已处理
        gbOrderService.updateBatchById(gbOrderUpdateList);
    }

    private Map<String, EsbEmployeeDTO> getEsbEmployeeMap(List<GbOrderDO> gbOrderList) {
        Map<String, EsbEmployeeDTO> esbEmployeeMap = new HashMap<>();
        List<String> empIdList = gbOrderList.stream().map(GbOrderDO::getSellerEmpId).distinct().collect(Collectors.toList());
        List<EsbEmployeeDTO> esbEmployeeList = esbEmployeeApi.listByEmpIds(empIdList);
        if (CollUtil.isNotEmpty(esbEmployeeList)) {
            esbEmployeeMap = esbEmployeeList.stream().collect(Collectors.toMap(EsbEmployeeDTO::getEmpId, Function.identity()));
        }
        return esbEmployeeMap;
    }

    private Map<Long, String> getCrmEnterpriseMap(List<GbOrderDO> gbOrderList) {
        Map<Long, String> crmEnterpriseMap = new HashMap<>();
        List<Long> crmIdLIst = gbOrderList.stream().map(GbOrderDO::getCrmId).distinct().collect(Collectors.toList());
        List<CrmEnterpriseDO> crmEnterpriseList = crmEnterpriseService.listByIds(crmIdLIst);
        if (CollUtil.isNotEmpty(crmEnterpriseList)) {
            crmEnterpriseMap = crmEnterpriseList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getProvinceCode(), (k1,k2) -> k1));
        }
        return crmEnterpriseMap;
    }

    private void buildGbOrderupdate(Long opUserId, Date opTime, Long gbOrderId, GbOrderDO gbOrderupdate) {
        gbOrderupdate.setId(gbOrderId);
        gbOrderupdate.setExecStatus(GbOrderExecStatusEnum.FINISH.getCode());
        gbOrderupdate.setOpUserId(opUserId);
        gbOrderupdate.setOpTime(opTime);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancleSubstract(GbAppealSubstractCancleRequest request, List<GbAppealAllocationDO> appealAllocationList, GbAppealFlowRelatedDO appealFlowRelated, GbAppealFlowStatisticDO appealFlowStatistic) {
        // 流向数量扣减取消
        try {
            return doCancleSubstract(appealAllocationList, appealFlowRelated, appealFlowStatistic, request.getOpUserId(), request.getOpTime());
        } catch (Exception e) {
            log.error("团购处理取消加入扣减, 流向数据扣减并发异常, 团购处理ID:{}, 源流向ID:{}, 取消扣减增加结果ID:{}", request.getAppealFormId(), appealFlowRelated.getFlowWashId(), appealAllocationList.toString());
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAppealForm(DeleteGbAppealFormRequest request, GbAppealFormDO gbAppealFormDO, GbOrderDO gbOrderDO, List<GbAppealFlowRelatedDO> gbAppealFlowRelatedList, List<GbAppealFlowStatisticDO> gbAppealFlowStatisticList, List<GbAppealAllocationDO> gbAppealAllocationList) {
        try {
            if (CollUtil.isEmpty(gbAppealFlowRelatedList)) {
                log.info("删除团购处理, id:{}，没有匹配源流向, 直接删除团购处理、并把团购数据的处理状态更新成未处理");
                try {
                    // 删除团购处理，团购数据状态更新成未处理
                    return deleteAppealFormWithoutFlowRelated(gbAppealFormDO.getId(), gbOrderDO.getId(), request.getOpUserId(), request.getOpTime());
                } catch (Exception e) {
                    log.error("团购处理删除失败, Exception:{}", e.getMessage());
                    return false;
                }
            } else {
                log.info("删除团购处理, id:{}，有匹配源流向、有已扣减");
                // 已匹配源流向、并未扣减的
                List<Long> flowRelatedIdsNotSubstract = new ArrayList<>();
                // 已匹配源流向、并已扣减的
                List<GbAppealFlowRelatedDO> flowRelatedSubstract = new ArrayList<>();

                for (GbAppealFlowRelatedDO gbAppealFlowRelatedDO : gbAppealFlowRelatedList) {
                    if (gbAppealFlowRelatedDO.getMatchQuantity().compareTo(BigDecimal.ZERO) == 0) {
                        flowRelatedIdsNotSubstract.add(gbAppealFlowRelatedDO.getId());
                    }
                    if (gbAppealFlowRelatedDO.getMatchQuantity().compareTo(BigDecimal.ZERO) > 0) {
                        flowRelatedSubstract.add(gbAppealFlowRelatedDO);
                    }
                }


                // 扣减数量归还、删除关联表、删除扣减/新增结果
                deleteAppealFormWithFlowRelated(flowRelatedIdsNotSubstract, flowRelatedSubstract, gbAppealFlowStatisticList, gbAppealAllocationList, request.getOpUserId(), request.getOpTime());
                // 删除团购处理，团购数据状态更新成未处理
                deleteAppealFormWithoutFlowRelated(gbAppealFormDO.getId(), gbOrderDO.getId(), request.getOpUserId(), request.getOpTime());
                return true;
            }
        } catch (Exception e) {
            log.error("团购处理删除异常, 团购处理ID:{}", request.getId());
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean selectFlowForMatch(SaveGbAppealFormSaleReportMatchRequest request, GbAppealFormDO appealFormOld, Map<Long, FlowWashSaleReportDTO> flowSaleReportMap) {
        Assert.notNull(request, "参数 request 不能为空");
        Assert.notEmpty(request.getFlowWashIdList(), "参数 flowWashIdList 不能为空");

        try {
            // 生成团购处理与源流向关联关系、初始化源流向扣减统计表
            saveRelatedAndStatistic(request, flowSaleReportMap);

            // 更新团购处理源流向匹配条数
            updateGbAppealFormFlowMatchNumber(request, request.getFlowWashIdList());

            // 更新团购数据处理状态
            //        updateGbOrdeExecStatus(request, appealFormOld);

            return true;
        } catch (Exception e) {
            log.error("手动选择源流向匹配异常, 团购处理ID:{}, 源流向ID:{}", request.getAppealFormId(), request.getFlowWashIdList().toString());
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
    }

    private void updateGbOrdeExecStatus(SaveGbAppealFormSaleReportMatchRequest request, GbAppealFormDO appealFormOld) {
        GbOrderDO gbOrderupdate = new GbOrderDO();
        buildGbOrderupdate(request.getOpUserId(), request.getOpTime(), appealFormOld.getGbOrderId(), gbOrderupdate);
        gbOrderService.updateById(gbOrderupdate);
    }

    private void saveRelatedAndStatistic(SaveGbAppealFormSaleReportMatchRequest request, Map<Long, FlowWashSaleReportDTO> flowSaleReportMap) {
        List<Long> flowWashIdList = request.getFlowWashIdList();
        log.info("手动选择源流向匹配, 保存团购处理与源流向关联、初始化源流向扣减统计, appealFormId:{}, flowWashId:{}", request.getAppealFormId(), flowWashIdList.toString());
        // 源流向扣减统计是否已存在
        Map<Long, GbAppealFlowStatisticDO> flowStatisticMap = new HashMap<>();
        List<GbAppealFlowStatisticDO> flowStatisticList = gbAppealFlowStatisticService.getByFlowWashIdList(flowWashIdList);
        if (CollUtil.isNotEmpty(flowStatisticList)) {
            flowStatisticMap = flowStatisticList.stream().collect(Collectors.toMap(GbAppealFlowStatisticDO::getFlowWashId, Function.identity()));
        }

        List<GbAppealFlowRelatedDO> saveRelatedList = new ArrayList<>();
        GbAppealFlowRelatedDO saveFlowRelated;
        List<GbAppealFlowStatisticDO> saveStatisticList = new ArrayList<>();
        GbAppealFlowStatisticDO flowStatistic;
        for (Long flowWashId : flowWashIdList) {
            // 团购处理源流向关联
            saveFlowRelated = new GbAppealFlowRelatedDO();
            buildUpdateFlowRelated(request, saveRelatedList, flowSaleReportMap, saveFlowRelated, flowWashId);
            // 源流向扣减数量统计不存的进行初始化
            GbAppealFlowStatisticDO flowStatisticDO = flowStatisticMap.get(flowWashId);
            if (ObjectUtil.isNull(flowStatisticDO)) {
                flowStatistic = new GbAppealFlowStatisticDO();
                buildSaveStatistic(request, saveStatisticList, flowSaleReportMap, flowStatistic, flowWashId);
            }
        }
        if (ObjectUtil.isEmpty(saveRelatedList)) {
            throw new ServiceException(ResultCode.FAILED, "手动选择源流向匹配, 保存团购处理与源流向关联数据不能为空, appealFormId:"+ request.getAppealFormId() +", flowWashId:" + flowWashIdList.toString());
        }
        // 保存团购处理与源流向关联
        gbAppealFlowRelatedService.saveBatch(saveRelatedList, 1000);
        if (ObjectUtil.isNotEmpty(saveStatisticList)) {
            // 保存初始化源流向扣减统计
            gbAppealFlowStatisticService.saveBatch(saveStatisticList, 1000);
        }
    }

    private void updateGbAppealFormFlowMatchNumber(SaveGbAppealFormSaleReportMatchRequest request, List<Long> flowWashIdList) {
        log.info("手动选择源流向匹配, 更新团购处理源流向匹配条数, appealFormId:{}, flowMatchNumber:{}", request.getAppealFormId(), flowWashIdList.size());
        UpdateGbAppealFormFlowMatchNumberRequest updateNumberRequest = new UpdateGbAppealFormFlowMatchNumberRequest();
        updateNumberRequest.setId(request.getAppealFormId());
        updateNumberRequest.setFlowMatchNumber(new BigDecimal(flowWashIdList.size() + ""));
        updateNumberRequest.setOpUserId(request.getOpUserId());
        updateNumberRequest.setOpTime(request.getOpTime());
        this.baseMapper.updateFlowMatchNumberById(updateNumberRequest);
    }

    private void buildSaveStatistic(SaveGbAppealFormSaleReportMatchRequest request, List<GbAppealFlowStatisticDO> saveStatisticList, Map<Long, FlowWashSaleReportDTO> flowSaleReportMap, GbAppealFlowStatisticDO flowStatistic, Long flowWashId) {
        FlowWashSaleReportDTO flowWashSaleReportDTO = flowSaleReportMap.get(flowWashId);
        if (ObjectUtil.isNull(flowWashSaleReportDTO)) {
            return;
        }
        flowStatistic.setFlowKey(flowWashSaleReportDTO.getFlowKey());
        flowStatistic.setFlowWashId(flowWashId);
        flowStatistic.setSoQuantity(flowWashSaleReportDTO.getFinalQuantity());
        flowStatistic.setMatchQuantity(BigDecimal.ZERO);
        flowStatistic.setUnMatchQuantity(flowWashSaleReportDTO.getFinalQuantity());
        flowStatistic.setOpUserId(request.getOpUserId());
        flowStatistic.setOpTime(request.getOpTime());
        saveStatisticList.add(flowStatistic);
    }

    private void buildUpdateFlowRelated(SaveGbAppealFormSaleReportMatchRequest request, List<GbAppealFlowRelatedDO> saveRelatedList, Map<Long, FlowWashSaleReportDTO> flowSaleReportMap, GbAppealFlowRelatedDO updateFlowRelated, Long flowWashId) {
        FlowWashSaleReportDTO flowWashSaleReportDTO = flowSaleReportMap.get(flowWashId);
        if (ObjectUtil.isNull(flowWashSaleReportDTO)) {
            return;
        }
        updateFlowRelated.setFlowKey(flowWashSaleReportDTO.getFlowKey());
        updateFlowRelated.setAppealFormId(request.getAppealFormId());
        updateFlowRelated.setFlowWashId(flowWashId);
        updateFlowRelated.setMatchQuantity(BigDecimal.ZERO);
        updateFlowRelated.setOpUserId(request.getOpUserId());
        updateFlowRelated.setOpTime(request.getOpTime());
        saveRelatedList.add(updateFlowRelated);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAppealFlowRelatedForNotSubstract(Long appealFormId, Long flowWashId, Long appealFlowRelatedId, Long opUserId, Date opTime) {
        Assert.notNull(appealFormId, "参数 appealFormId 不能为空");
        Assert.notNull(appealFlowRelatedId, "参数 appealFlowRelatedId 不能为空");
        try {
            // 删除源流向匹配关联表
            deleteAppealFlowRelatedById(appealFlowRelatedId, "源流向匹配删除", opUserId, opTime);

            // 更新团购处理匹配源流向数量
            updateGbAppealFormFlowMatchNumber(appealFormId, "源流向匹配删除", opUserId, opTime);
            return true;
        } catch (Exception e) {
            log.error("源流向匹配删除异常, 团购处理ID:{}, 源流向ID:{}", appealFormId, flowWashId);
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteAppealFlowRelatedForSubstract(Long appealFormId, Long flowWashId, Long appealFlowRelatedId, Long appealFlowStatisticId, List<Long> appealAllocationIds, BigDecimal returnQuantity, Long opUserId, Date opTime) {
        try {
            // 更新源流向扣减统计表，已扣减数量归还
            BigDecimal returnMatchQuantity = returnQuantity.negate();
            appealFlowStatisticSubstract(appealFlowStatisticId, returnMatchQuantity, "源流向匹配删除", opUserId, opTime);

            // 删除扣减、新增结果明细
            deleteAppealAllocationByIds(appealAllocationIds, "源流向匹配删除", opUserId, opTime);

            // 删除源流向匹配关联表
            deleteAppealFlowRelatedById(appealFlowRelatedId, "源流向匹配删除", opUserId, opTime);

            // 更新团购处理匹配源流向数量
            updateGbAppealFormFlowMatchNumber(appealFormId, "源流向匹配删除", opUserId, opTime);

            // 销售报表，根据流向Id删除源流向数据
            deleteFlowSaleByAllocationIds(appealAllocationIds);

            return true;
        } catch (Exception e) {
            log.error("源流向匹配删除异常, 团购处理ID:{}, 源流向ID:{}", appealFormId, flowWashId);
            // 回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<GbAppealFormDO> listByFormIds(List<Long> formIds) {
        Assert.notEmpty(formIds, "参数 formIds 不能为空");
        LambdaQueryWrapper<GbAppealFormDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(GbAppealFormDO::getFormId, formIds);
        List<GbAppealFormDO> list = this.list(lambdaQueryWrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list;
    }

    @Override
    public FlowMonthWashControlDTO washControlGbStatusCheck(String month) {
        FlowMonthWashControlDTO washControl = flowMonthWashControlService.getCurrentFlowMonthWashControl();
        if (washControl == null) {
            log.warn("当前没有可以用的日程, month:{}", month);
            return null;
        }
        // 校验日程团购状态，锁定团购开始、非锁团购完成前都可以手动处理（阶段5进行中 至 阶段8 进行中）
        boolean gbFlag = true;
        Integer gbLockStatus = washControl.getGbLockStatus();
        Integer gbUnlockStatus = washControl.getGbUnlockStatus();
        if (ObjectUtil.isNotNull(gbLockStatus) && ObjectUtil.isNotNull(gbUnlockStatus)) {
            // 团购锁定：1-未开始 2-进行中 3-已完成。 团购非锁：1-未开始 2-进行中 3-已完成
            // 可处理 1.锁定、非锁 有一个是 2-进行中
            // 可处理 2.锁定:3-已完成。 非锁：1-未开始
            // 可处理 3.锁定:1-未开始。 非锁：3-已完成
            // 日程中的锁定、非锁状态目前是完全独立的，无开启先后顺序、无开启关系关联。
            // 因此判断锁定、非锁两个状态不是同时1-未开始、不是同时3-已完成  就可以
            if (WashStageStatusEnum.UN_START.getCode().equals(gbLockStatus) && WashStageStatusEnum.UN_START.getCode().equals(gbUnlockStatus)){
                gbFlag = false;
            }
            if (WashStageStatusEnum.FINISH.getCode().equals(gbLockStatus) && WashStageStatusEnum.FINISH.getCode().equals(gbUnlockStatus)){
                gbFlag = false;
            }
        }
        if (!gbFlag) {
            log.warn("当前日程阶段的锁定团购、非锁团购状态不符合，阶段5进行中~阶段8进行中才能手动处理团购, gbLockStatus:{}, gbUnlockStatus:{}", gbLockStatus, gbUnlockStatus);
            return null;
        }
        if (StrUtil.isNotBlank(month)) {
            String flowMonth = washControl.getYear().toString().concat("-").concat(String.format("%02d", washControl.getMonth()));
            if (!flowMonth.equals(month)) {
                log.warn("与当前日程阶段的所属年月不符合, 日程所属年月:{}, 操作的所属年月:{}", flowMonth, month);
                return null;
            }
        }
        return washControl;
    }

    /**
     * 删除团购处理，有源流向匹配、扣减
     *
     * @param flowRelatedIdsNotSubstract
     * @param flowRelatedSubstract
     * @param gbAppealFlowStatisticList
     * @param gbAppealAllocationList
     * @param opUserId
     * @param opTime
     * @return
     */
    public boolean deleteAppealFormWithFlowRelated(List<Long> flowRelatedIdsNotSubstract, List<GbAppealFlowRelatedDO> flowRelatedSubstract, List<GbAppealFlowStatisticDO> gbAppealFlowStatisticList,
                                                   List<GbAppealAllocationDO> gbAppealAllocationList, Long opUserId, Date opTime) {
        Map<Long, GbAppealFlowStatisticDO> flowStatisticMap = new HashMap<>();
        if (CollUtil.isNotEmpty(gbAppealFlowStatisticList)) {
            flowStatisticMap = gbAppealFlowStatisticList.stream().collect(Collectors.toMap(GbAppealFlowStatisticDO::getFlowWashId, O -> O, (k1, k2) -> k1));
        }

        // 未扣减的关联源流向，直接删除关联
        if (CollUtil.isNotEmpty(flowRelatedIdsNotSubstract)) {
            for (Long flowRelatedId : flowRelatedIdsNotSubstract) {
                deleteAppealFlowRelatedById(flowRelatedId, "删除团购处理", opUserId, opTime);
            }
        }

        // 已扣减的，扣减数量归还
        if (CollUtil.isNotEmpty(flowRelatedSubstract)) {
            for (GbAppealFlowRelatedDO related : flowRelatedSubstract) {
                // 更新源流向扣减统计表，已扣减数量归还
                BigDecimal returnMatchQuantity = related.getMatchQuantity().negate();
                GbAppealFlowStatisticDO statistic = flowStatisticMap.get(related.getFlowWashId());
                appealFlowStatisticSubstract(statistic.getId(), returnMatchQuantity, "删除团购处理", opUserId, opTime);

                // 删除源流向匹配关联表
                deleteAppealFlowRelatedById(related.getId(), "删除团购处理", opUserId, opTime);

                // 删除扣减、新增结果明细
                List<Long> allocationIds = gbAppealAllocationList.stream().filter(o -> related.getFlowWashId().equals(o.getFlowWashId())).map(GbAppealAllocationDO::getId).collect(Collectors.toList());
                deleteAppealAllocationByIds(allocationIds, "删除团购处理", opUserId, opTime);

                // 销售报表，根据流向Id删除源流向数据
                deleteFlowSaleByAllocationIds(allocationIds);
            }
        }

        return true;
    }

    private void deleteAppealFlowRelatedById(Long id, String metnod, Long opUserId, Date opTime) {
        DeleteGbAppealFlowRelatedRequest request = new DeleteGbAppealFlowRelatedRequest();
        request.setId(id);
        request.setOpUserId(opUserId);
        request.setOpTime(opTime);
        int deleteRelatedCount = gbAppealFlowRelatedService.deleteById(request);
        if (deleteRelatedCount <= 0) {
            log.error(metnod + ", 删除关联匹配源流向失败, appealFlowRelatedId:{}", id);
            throw new ServiceException("删除关联匹配源流向异常, appealFlowRelatedId:{}" + id);
        }
    }

    /**
     * 删除团购处理，无源流向匹配
     *
     * @param appealFormId
     * @param gbOrderId
     * @return
     */
    public boolean deleteAppealFormWithoutFlowRelated(Long appealFormId, Long gbOrderId, Long opUserId, Date opTime) {
        // 删除团购处理
        if (ObjectUtil.isNotNull(appealFormId)) {
            deleteGbAppealFormById(appealFormId, opUserId, opTime);
        }
        // 团购主表数据更新成未处理
        if (ObjectUtil.isNotNull(gbOrderId)) {
            updateGbOrderExecStatusById(gbOrderId);
        }
        return true;
    }


    private void updateGbOrderExecStatusById(Long gbOrderId) {
        Assert.notNull(gbOrderId, "参数 gbOrderId 不能为空");
        GbOrderDO gbOrderDO = new GbOrderDO();
        gbOrderDO.setId(gbOrderId);
        gbOrderDO.setExecStatus(GbOrderExecStatusEnum.UN_START.getCode());
        boolean gbOrderFlag = gbOrderService.updateById(gbOrderDO);
        if (!gbOrderFlag) {
            log.error("删除团购处理, 更新团购数据状态未处理失败, gbOrderId:{}", gbOrderId);
            throw new ServiceException("删除团购处理, 更新团购数据状态未处理异常, gbOrderId:" + gbOrderId);
        }
    }

    private void deleteGbAppealFormById(Long id, Long opUserId, Date opTime) {
        GbAppealFormDO gbAppealFormDO = new GbAppealFormDO();
        gbAppealFormDO.setId(id);
        gbAppealFormDO.setFlowMatchNumber(0L);
        gbAppealFormDO.setOpUserId(opUserId);
        gbAppealFormDO.setOpTime(opTime);
        gbAppealFormDO.setLastUpdateUser(opUserId);
        gbAppealFormDO.setLastUpdateTime(opTime);
        int deleteAppealFormCount = this.deleteByIdWithFill(gbAppealFormDO);
        if (deleteAppealFormCount <= 0) {
            log.error("删除团购处理, 删除团购处理主表失败, appealFormId:{}", id);
            throw new ServiceException("删除团购处理异常, appealFormId:{}" + id);
        }
    }

    public boolean doCancleSubstract(List<GbAppealAllocationDO> gbAppealAllocationList, GbAppealFlowRelatedDO gbAppealFlowRelatedDO, GbAppealFlowStatisticDO gbAppealFlowStatisticDO, Long opUserId, Date opTime) {
        Long appealFormId = gbAppealFlowRelatedDO.getAppealFormId();
        Long flowWashId = gbAppealFlowRelatedDO.getFlowWashId();
        // 已匹配源流向数量
        BigDecimal matchQuantity = gbAppealFlowRelatedDO.getMatchQuantity();
        // 归还数量
        BigDecimal substractQuantity = matchQuantity.negate();

        // 更新团购流向统计表：gb_appeal_flow_statistic，源流向统计未匹配数量、匹配数量
        appealFlowStatisticSubstract(gbAppealFlowStatisticDO.getId(), substractQuantity, "团购处理取消加入扣减", opUserId, opTime);

        // 更新团购流向关联表，关联表匹配数量置0
        appealFlowRelatedSubstract(gbAppealFlowRelatedDO.getId(), BigDecimal.ZERO, "团购处理取消加入扣减", opUserId, opTime);

        // 删除团购处理流向结果，同时删除扣减、新增
        List<Long> allocationIds = gbAppealAllocationList.stream().map(GbAppealAllocationDO::getId).collect(Collectors.toList());
        deleteAppealAllocationByIds(allocationIds, "团购处理取消加入扣减", opUserId, opTime);

        // 销售报表，根据流向Id删除源流向数据
        deleteFlowSaleByAllocationIds(allocationIds);

        return true;
    }

    private void deleteFlowSaleByAllocationIds(List<Long> appealAllocationIds) {
        boolean deleteFlag = flowWashSaleReportService.removeByFlowSaleWashId(appealAllocationIds, FlowClassifyEnum.FLOW_GB);
        if (!deleteFlag) {
            log.error("删除源流向数据失败, gbAppealAllocationId:{}", appealAllocationIds.toString());
            throw new BusinessException(GbErrorEnum.DELETE_FLOW_SALE_REPORT_ERROR);
        }
    }

    private void deleteAppealAllocationByIds(List<Long> allocationIds, String method,Long opUserId, Date opTime) {
        DeleteGbAppealFlowAllocationRequest deleteAllocationRequest = new DeleteGbAppealFlowAllocationRequest();
        deleteAllocationRequest.setIdList(allocationIds);
        deleteAllocationRequest.setOpUserId(opUserId);
        deleteAllocationRequest.setOpTime(opTime);
        int deleteAllocation = gbAppealAllocationService.deleteByIdList(deleteAllocationRequest);
        if (deleteAllocation <= 0) {
            log.error(method + ", 删除流向团购处理结果扣减、新增失败, appealAllocationId:{}", allocationIds.toString());
            throw new BusinessException(GbErrorEnum.GB_SUBTRACT_ERROR);
        }
    }

    private void buildGbAppealFormSave(SaveGbAppealFormRequest request, GbOrderDO gbOrderDO, Map<Long, String> crmEnterpriseMap,
                                       Map<String, EsbEmployeeDTO> esbEmployeeMap, GbAppealFormDO gbAppealFormDO, FlowMonthWashControlDTO flowMonthWashControlDTO,
                                       List<GbAppealFormDO> gbAppealFormSaveList) {
        gbAppealFormDO.setGbOrderId(gbOrderDO.getId());
        gbAppealFormDO.setFormId(gbOrderDO.getFormId());
        gbAppealFormDO.setGbNo(gbOrderDO.getGbNo());
        gbAppealFormDO.setGbProcess(gbOrderDO.getGbProcess());
        gbAppealFormDO.setFlowMonth(gbOrderDO.getFlowMonth());
        gbAppealFormDO.setMatchMonth(flowMonthWashControlDTO.getYear() + "-" + String.format("%02d", flowMonthWashControlDTO.getMonth()));
        gbAppealFormDO.setGbMonth(gbOrderDO.getGbMonth());
        gbAppealFormDO.setCrmId(gbOrderDO.getCrmId());
        gbAppealFormDO.setEname(gbOrderDO.getEname());
        gbAppealFormDO.setOrgCrmId(gbOrderDO.getOrgCrmId());
        gbAppealFormDO.setEnterpriseName(gbOrderDO.getEnterpriseName());
        gbAppealFormDO.setGoodsCode(gbOrderDO.getGoodsCode());
        gbAppealFormDO.setGoodsName(gbOrderDO.getGoodsName());
        gbAppealFormDO.setGbQuantity(gbOrderDO.getGbQuantity());
        gbAppealFormDO.setSellerEmpId(gbOrderDO.getSellerEmpId());
        gbAppealFormDO.setGbNature(gbOrderDO.getGbType());
        gbAppealFormDO.setDataExecStatus(GbDataExecStatusEnum.UN_START.getCode());
        gbAppealFormDO.setExecType(request.getExecType());
        gbAppealFormDO.setGbOrderCreateTime(gbOrderDO.getCreateTime());
        gbAppealFormDO.setOpUserId(request.getOpUserId());
        gbAppealFormDO.setOpTime(request.getOpTime());
        gbAppealFormDO.setLastUpdateUser(request.getOpUserId());
        gbAppealFormDO.setLastUpdateTime(request.getOpTime());
        // 省份代码
        String provinceCode = crmEnterpriseMap.get(gbOrderDO.getCrmId());
        gbAppealFormDO.setProvinceCode(Optional.ofNullable(provinceCode).orElse(""));
        // esb员工信息
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeMap.get(gbOrderDO.getSellerEmpId());
        if (ObjectUtil.isNotNull(esbEmployeeDTO)) {
            gbAppealFormDO.setSellerEmpName(Optional.ofNullable(esbEmployeeDTO.getEmpName()).orElse(""));
            gbAppealFormDO.setBusinessDepartment(Optional.ofNullable(esbEmployeeDTO.getYxDept()).orElse(""));
            gbAppealFormDO.setBusinessProvince(Optional.ofNullable(esbEmployeeDTO.getYxProvince()).orElse(""));
            Long deptId = Optional.ofNullable(esbEmployeeDTO.getDeptId()).orElse(0L);
            String districtCountyCode = deptId.toString();
            gbAppealFormDO.setDistrictCountyCode(districtCountyCode);
            gbAppealFormDO.setDistrictCounty(Optional.ofNullable(esbEmployeeDTO.getYxArea()).orElse(""));
        }
        gbAppealFormSaveList.add(gbAppealFormDO);
    }


    public boolean doEditSubstract(GbAppealFormExecuteEditDetailRequest editAllocationRequest, GbAppealFormDO gbAppealForm, GbAppealAllocationDO gbAppealAllocationDO, GbAppealFlowStatisticDO gbAppealFlowStatisticDO, GbAppealFlowRelatedDO gbAppealFlowRelatedDO) {
        // 更新团购流向统计表：gb_appeal_flow_statistic，源流向统计未匹配数量、匹配数量
        appealFlowStatisticSubstract(gbAppealFlowStatisticDO.getId(), gbAppealForm.getGbQuantity(), "编辑扣减数据", editAllocationRequest.getOpUserId(), editAllocationRequest.getOpTime());

        // 更新团购流向关联表, 更新团购匹配当前源流向的已扣减数量
        appealFlowRelatedSubstract(gbAppealFlowRelatedDO.getId(), editAllocationRequest.getQuantity().abs(), "编辑扣减数据", editAllocationRequest.getOpUserId(), editAllocationRequest.getOpTime());

        // 更新团购处理流向结果：gb_appeal_allocation，同时更新扣减、新增
        editAppealFlowAllocation(editAllocationRequest, gbAppealAllocationDO);
        return true;
    }

    private void editAppealFlowAllocation(GbAppealFormExecuteEditDetailRequest editAllocationRequest, GbAppealAllocationDO gbAppealAllocationDO) {
        // 销售合并报表，源流向
        // 用户编辑修改的处理结果明细Request
        SubstractGbAppealFlowAllocationRequest substractAllocationRequest = buildGbAppealAllocationEditRequest(editAllocationRequest, gbAppealAllocationDO.getPrice());
        // 需同步修改数量的处理结果明细Request
        SubstractGbAppealFlowAllocationRequest addAllocationRequest = buildGbAppealAllocationNegateRequest(editAllocationRequest, gbAppealAllocationDO.getPrice());
        // 更新团购流向扣减、增加结果
        List<SubstractGbAppealFlowAllocationRequest> saveAllocationList = new ArrayList<>();
        saveAllocationList.add(substractAllocationRequest);
        saveAllocationList.add(addAllocationRequest);
        log.info("编辑扣减数据, 更新列表, saveAllocationList:{}", JSONUtil.toJsonStr(saveAllocationList));
        List<Long> ids = gbAppealAllocationService.saveOrUpdateAllocationList(saveAllocationList);
        if (ObjectUtil.isEmpty(ids)) {
            log.error("编辑扣减数据, 更新流向团购申诉分配结果表失败, appealAllocationId:{}, appealFormId:{}, flowWashId:{}, substractQuantity:{}", gbAppealAllocationDO.getId(), gbAppealAllocationDO.getAppealFormId(), gbAppealAllocationDO.getFlowWashId(), editAllocationRequest.getQuantity());
            throw new BusinessException(GbErrorEnum.GB_SUBTRACT_ERROR);
        }
    }


    private SubstractGbAppealFlowAllocationRequest buildGbAppealAllocationNegateRequest(GbAppealFormExecuteEditDetailRequest editAllocationRequest, BigDecimal price) {
        SubstractGbAppealFlowAllocationRequest allocationRequest = new SubstractGbAppealFlowAllocationRequest();
        allocationRequest.setId(editAllocationRequest.getNegateAllocationId());
        // 数量，取编辑扣减数量的相反数
        allocationRequest.setQuantity(editAllocationRequest.getQuantity().negate());
        // 金额 = 数量 * 产品单价
        BigDecimal totalAmount = allocationRequest.getQuantity().multiply(price);
        allocationRequest.setTotalAmount(totalAmount);
        allocationRequest.setOpUserId(editAllocationRequest.getOpUserId());
        allocationRequest.setOpTime(editAllocationRequest.getOpTime());
        return allocationRequest;
    }

    private SubstractGbAppealFlowAllocationRequest buildGbAppealAllocationEditRequest(GbAppealFormExecuteEditDetailRequest editAllocationRequest, BigDecimal price) {
        SubstractGbAppealFlowAllocationRequest allocationRequest = new SubstractGbAppealFlowAllocationRequest();
        allocationRequest.setId(editAllocationRequest.getAppealAllocationId());
        // 数量，取编辑扣减数量
        allocationRequest.setQuantity(editAllocationRequest.getQuantity());
        // 金额 = 数量 * 产品单价
        BigDecimal totalAmount = allocationRequest.getQuantity().multiply(price);
        allocationRequest.setTotalAmount(totalAmount);
        allocationRequest.setOpUserId(editAllocationRequest.getOpUserId());
        allocationRequest.setOpTime(editAllocationRequest.getOpTime());
        buildAllocationEditRequestFiled(allocationRequest, editAllocationRequest);
        return allocationRequest;
    }

    private void buildAllocationEditRequestFiled(SubstractGbAppealFlowAllocationRequest allocationRequest, GbAppealFormExecuteEditDetailRequest editAllocationRequest) {
        // 销售计入人工号获取员工、部门、岗位信息
        allocationRequest.setOrgId(Optional.ofNullable(editAllocationRequest.getOrgId()).orElse(0L));
        allocationRequest.setDepartment(Optional.ofNullable(editAllocationRequest.getDepartment()).orElse(""));
        allocationRequest.setBusinessOrgId(Optional.ofNullable(editAllocationRequest.getBusinessOrgId()).orElse(0L));
        allocationRequest.setBusinessDepartment(Optional.ofNullable(editAllocationRequest.getBusinessDepartment()).orElse(""));
        allocationRequest.setProvincialArea(Optional.ofNullable(editAllocationRequest.getProvincialArea()).orElse(""));
        allocationRequest.setBusinessProvince(Optional.ofNullable(editAllocationRequest.getBusinessProvince()).orElse(""));
        allocationRequest.setDistrictCountyCode(Optional.ofNullable(editAllocationRequest.getDistrictCountyCode()).orElse(""));
        allocationRequest.setDistrictCounty(Optional.ofNullable(editAllocationRequest.getDistrictCounty()).orElse(""));
        allocationRequest.setSuperiorSupervisorCode(Optional.ofNullable(editAllocationRequest.getSuperiorSupervisorCode()).orElse(""));
        allocationRequest.setSuperiorSupervisorName(Optional.ofNullable(editAllocationRequest.getSuperiorSupervisorName()).orElse(""));
        allocationRequest.setRepresentativeCode(Optional.ofNullable(editAllocationRequest.getRepresentativeCode()).orElse(""));
        allocationRequest.setRepresentativeName(Optional.ofNullable(editAllocationRequest.getRepresentativeName()).orElse(""));
        allocationRequest.setPostCode(Optional.ofNullable(editAllocationRequest.getPostCode()).orElse(0L));
        allocationRequest.setPostName(Optional.ofNullable(editAllocationRequest.getPostName()).orElse(""));
    }


    private LambdaQueryWrapper<GbAppealFormDO> getListPageQueryWrapper(QueryGbAppealFormListPageRequest request) {
        LambdaQueryWrapper<GbAppealFormDO> lambdaQueryWrapper = new LambdaQueryWrapper();
        // 所属年月
        String matchMonth = request.getMatchMonth();
        if (StrUtil.isNotBlank(matchMonth)) {
            lambdaQueryWrapper.eq(GbAppealFormDO::getMatchMonth, matchMonth);
        }
        // 团购月份
        String gbMonth = request.getGbMonth();
        if (StrUtil.isNotBlank(gbMonth)) {
            lambdaQueryWrapper.eq(GbAppealFormDO::getGbMonth, gbMonth);
        }
        // 团购编号
        String gbNo = request.getGbNo();
        if (StrUtil.isNotBlank(gbNo)) {
            lambdaQueryWrapper.eq(GbAppealFormDO::getGbNo, gbNo);
        }
        // 出库商业编码
        Long crmId = request.getCrmId();
        if (ObjectUtil.isNotNull(crmId) && 0 != crmId.intValue()) {
            lambdaQueryWrapper.eq(GbAppealFormDO::getCrmId, crmId);
        }
        // 出库终端名称（全模糊）
        String enterpriseName = request.getEnterpriseName();
        if (StrUtil.isNotBlank(enterpriseName)) {
            lambdaQueryWrapper.like(GbAppealFormDO::getEnterpriseName, enterpriseName);
        }
        // 标准产品编码
        Long goodsCode = request.getGoodsCode();
        if (ObjectUtil.isNotNull(goodsCode) && 0 != goodsCode.intValue()) {
            lambdaQueryWrapper.eq(GbAppealFormDO::getGoodsCode, goodsCode);
        }
        // 处理状态
        Integer dataExecStatus = request.getDataExecStatus();
        if (ObjectUtil.isNotNull(dataExecStatus) && 0 != dataExecStatus.intValue()) {
            lambdaQueryWrapper.eq(GbAppealFormDO::getDataExecStatus, dataExecStatus);
        }
        // 最后操作人
        Long lastUpdateUser = request.getLastUpdateUser();
        if (ObjectUtil.isNotNull(lastUpdateUser) && 0 != lastUpdateUser.intValue()) {
            lambdaQueryWrapper.eq(GbAppealFormDO::getLastUpdateUser, lastUpdateUser);
        }
        // 最后操作时间
        Date lastUpdateTimeStart = request.getLastUpdateTimeStart();
        if (ObjectUtil.isNotNull(lastUpdateTimeStart)) {
            lambdaQueryWrapper.ge(GbAppealFormDO::getLastUpdateTime, DateUtil.beginOfDay(lastUpdateTimeStart));
        }
        Date lastUpdateTimeEnd = request.getLastUpdateTimeEnd();
        if (ObjectUtil.isNotNull(lastUpdateTimeEnd)) {
            lambdaQueryWrapper.le(GbAppealFormDO::getLastUpdateTime, DateUtil.endOfDay(lastUpdateTimeEnd));
        }
        // 所属年月
        Integer gbLockType = request.getGbLockType();
        if (gbLockType!=null&&gbLockType!=0) {
            lambdaQueryWrapper.eq(GbAppealFormDO::getGbLockType, gbLockType);
        }
        lambdaQueryWrapper.orderByDesc(GbAppealFormDO::getMatchMonth).orderByDesc(GbAppealFormDO::getGbOrderCreateTime);
        return lambdaQueryWrapper;
    }

    public List<FlowWashSaleReportDTO> pageFlowWashSaleReport(String year, String month, Long crmId, Long customerCrmId, Long goodsCode) {
        List<FlowWashSaleReportDTO> FlowWashSaleReportDTOList = new ArrayList<>();
        FlowWashSaleReportPageRequest reportPageRequest = new FlowWashSaleReportPageRequest();
        reportPageRequest.setYear(year);
        reportPageRequest.setMonth(month);
        reportPageRequest.setCrmId(crmId);
        reportPageRequest.setCustomerCrmId(customerCrmId);
        reportPageRequest.setGoodsCode(goodsCode);
        Page<FlowWashSaleReportDTO> page = null;
        int current = 1;
        do {
            reportPageRequest.setCurrent(current);
            reportPageRequest.setSize(500);
            page = flowWashSaleReportService.pageList(reportPageRequest);
            if (page == null || CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            FlowWashSaleReportDTOList.addAll(page.getRecords());
            current=current+1;
        } while (page != null && CollUtil.isNotEmpty(page.getRecords()));
        return FlowWashSaleReportDTOList;
    }



    /**
     * 更新团购处理匹配流向条数
     *
     * @param appealFormId
     * @return
     */
    public boolean updateGbAppealFormFlowMatchNumber (Long appealFormId, String method, Long opUserId, Date opTime) {
        // 查询匹配源流向数量
        int flowMatchNumber = gbAppealFlowRelatedService.countByAppealFormId(appealFormId);
        log.info("更新团购处理匹配流向条数, appealFormId:{}, flowMatchNumber:{}", appealFormId, flowMatchNumber);
        // 更新团购处理匹配流向条数
        GbAppealFormDO entity = new GbAppealFormDO();
        entity.setId(appealFormId);
        entity.setFlowMatchNumber(Long.parseLong(flowMatchNumber+""));
        entity.setOpUserId(opUserId);
        entity.setOpTime(opTime);
        entity.setLastUpdateUser(opUserId);
        entity.setLastUpdateTime(opTime);
        boolean updateFlag = this.updateById(entity);
        if (!updateFlag) {
            log.error(method + ", 更新团购处理匹配源流向数量失败, appealFormId:{}", appealFormId);
            throw new BusinessException(GbErrorEnum.DELETE_MATCH_FLOW_ERROR);
        }
        return true;
    }

    /**
     * 发送销售报表生成团购流向（团购处理结果流向新增）
     *
     * @param allocationIds 团购处理结果ID列表
     */
    private void sendMqToFlowSealReport(List<Long> allocationIds) {
        List<List<String>> allocationIdsNotify = new ArrayList<>();
        if (allocationIds.size() < 200) {
            allocationIdsNotify.add(Convert.toList(String.class, allocationIds));
        } else {
            List<List<Long>> partition = Lists.partition(allocationIds, 200);
            for (List<Long> ids : partition) {
                allocationIdsNotify.add(Convert.toList(String.class, ids));
            }
        }
        for (List<String> idStrList : allocationIdsNotify) {
            _this.sendMq(Constants.TOPIC_GB_FLOW_NOTIFY, Constants.TAG_GB_FLOW_NOTIFY, String.join(",", idStrList));
        }
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic, String topicTag, String msg) {
        MqMessageBO mqMessageBO = _this.sendPrepare(topic, topicTag, msg);
        mqMessageSendApi.send(mqMessageBO);
        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO sendPrepare(String topic, String topicTag, String msg) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }

}
