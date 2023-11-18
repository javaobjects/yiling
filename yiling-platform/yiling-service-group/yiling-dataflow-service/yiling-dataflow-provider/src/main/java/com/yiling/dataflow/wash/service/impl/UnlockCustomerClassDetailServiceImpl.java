package com.yiling.dataflow.wash.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.report.service.FlowWashSaleReportService;
import com.yiling.dataflow.utils.BackupUtil;
import com.yiling.dataflow.wash.dao.UnlockCustomerClassDetailMapper;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassDetailCountRequest;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassDetailPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCustomerWashTaskRequest;
import com.yiling.dataflow.wash.entity.UnlockCustomerClassDetailDO;
import com.yiling.dataflow.wash.entity.UnlockCustomerClassRuleDO;
import com.yiling.dataflow.wash.entity.UnlockFlowWashSaleDO;
import com.yiling.dataflow.wash.entity.UnlockFlowWashTaskDO;
import com.yiling.dataflow.wash.service.UnlockCustomerClassDetailService;
import com.yiling.dataflow.wash.service.UnlockCustomerClassRuleService;
import com.yiling.dataflow.wash.service.UnlockFlowWashSaleService;
import com.yiling.dataflow.wash.service.UnlockFlowWashTaskService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 非锁客户分类规则表 服务实现类
 * </p>
 *
 * @author baifc
 * @since 2023-04-26
 */
@Slf4j
@Service
public class UnlockCustomerClassDetailServiceImpl extends BaseServiceImpl<UnlockCustomerClassDetailMapper, UnlockCustomerClassDetailDO> implements UnlockCustomerClassDetailService {

    @Autowired
    private UnlockFlowWashTaskService unlockFlowWashTaskService;

    @Autowired
    private FlowWashSaleReportService flowWashSaleReportService;

    @Autowired
    private UnlockFlowWashSaleService unlockFlowWashSaleService;

    @Autowired
    private UnlockCustomerClassRuleService unlockCustomerClassRuleService;

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;

    @Override
    public Page<UnlockCustomerClassDetailDO> listPage(QueryUnlockCustomerClassDetailPageRequest request) {

        Page<UnlockCustomerClassDetailDO> page = new Page<>(request.getCurrent(), request.getSize());

        LambdaQueryWrapper<UnlockCustomerClassDetailDO> wrapper = new LambdaQueryWrapper<>();
        if (request.getCrmEnterpriseId() != null) {
            wrapper.eq(UnlockCustomerClassDetailDO::getCrmEnterpriseId, request.getCrmEnterpriseId());
        }
        if (StringUtils.isNotEmpty(request.getCustomerName())) {
            wrapper.like(UnlockCustomerClassDetailDO::getCustomerName, request.getCustomerName());
        }
        if (request.getClassFlag() != null) {
            wrapper.eq(UnlockCustomerClassDetailDO::getClassFlag, request.getClassFlag());
        }
        if (request.getStartOpTime() != null) {
            wrapper.ge(UnlockCustomerClassDetailDO::getLastOpTime, DateUtil.beginOfDay(request.getStartOpTime()));
        }
        if (request.getEndOpTime() != null) {
            wrapper.le(UnlockCustomerClassDetailDO::getLastOpTime, DateUtil.endOfDay(request.getEndOpTime()));
        }
        if (request.getCustomerClassification() != null) {
            wrapper.eq(UnlockCustomerClassDetailDO::getCustomerClassification, request.getCustomerClassification());
        }
        wrapper.orderByDesc(UnlockCustomerClassDetailDO::getId);

        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public Integer countByRequest(QueryUnlockCustomerClassDetailCountRequest request) {

        if (request.getClassFlag() == null) {
            throw new BusinessException(ResultCode.FAILED);
        }

        LambdaQueryWrapper<UnlockCustomerClassDetailDO> wrapper = new LambdaQueryWrapper<>();
        if (request.getCrmEnterpriseId() != null) {
            wrapper.eq(UnlockCustomerClassDetailDO::getCrmEnterpriseId, request.getCrmEnterpriseId());
        }
        if (StringUtils.isNotEmpty(request.getCustomerName())) {
            wrapper.like(UnlockCustomerClassDetailDO::getCustomerName, "%" + request.getCustomerName() + "%");
        }

        if (request.getCustomerClassification() != null) {
            wrapper.eq(UnlockCustomerClassDetailDO::getCustomerClassification, request.getCustomerClassification());
        }
        if (request.getStartOpTime() != null) {
            wrapper.ge(UnlockCustomerClassDetailDO::getUpdateTime, request.getStartOpTime());
        }
        if (request.getEndOpTime() != null) {
            wrapper.le(UnlockCustomerClassDetailDO::getUpdateTime, request.getEndOpTime());
        }

        wrapper.eq(UnlockCustomerClassDetailDO::getClassFlag, request.getClassFlag());
        wrapper.orderByDesc(UnlockCustomerClassDetailDO::getId);

        return baseMapper.selectCount(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unlockCustomerClassMappingHandle(Long ufwtId) {
        UnlockFlowWashTaskDO unlockFlowWashTaskDO = unlockFlowWashTaskService.getById(ufwtId);
        if (unlockFlowWashTaskDO == null) {
            return;
        }
        if (unlockFlowWashTaskDO.getClassificationStatus() != 1) {
            log.error("非锁流向清洗，ufwtId={}，任务状态不对", ufwtId);
            return;
        }

        SaveOrUpdateUnlockCustomerWashTaskRequest saveOrUpdateUnlockCustomerWashTaskRequest = new SaveOrUpdateUnlockCustomerWashTaskRequest();
        saveOrUpdateUnlockCustomerWashTaskRequest.setId(unlockFlowWashTaskDO.getId());
        saveOrUpdateUnlockCustomerWashTaskRequest.setClassificationStatus(2);
        unlockFlowWashTaskService.saveUnlockCustomerWashTask(saveOrUpdateUnlockCustomerWashTaskRequest);

        unlockFlowWashSaleService.deleteByUfwtId(ufwtId);
        int year = unlockFlowWashTaskDO.getYear();
        int month = unlockFlowWashTaskDO.getMonth();
        Long crmEnterpriseId = unlockFlowWashTaskDO.getCrmEnterpriseId();

        FlowWashSaleReportPageRequest request = new FlowWashSaleReportPageRequest();
        request.setYear(String.valueOf(year));
        request.setMonth(String.valueOf(month));
        request.setCrmId(crmEnterpriseId);
        request.setIsLockFlag(2);

        //  分页查询报表数据
        int current = 1;
        int size = 2000;
        while (true) {
            request.setCurrent(current);
            request.setSize(size);
            Page<FlowWashSaleReportDTO> pageResult = flowWashSaleReportService.pageList(request);
            List<FlowWashSaleReportDTO> list = pageResult.getRecords();

            if (CollUtil.isEmpty(list)) {
                break;
            }

            List<UnlockFlowWashSaleDO> unlockFlowWashSaleDOList = new ArrayList<>();

            //  匹配客户分类
            for (FlowWashSaleReportDTO flowWashSaleReportDTO : list) {
                CrmEnterpriseDO supplier = crmEnterpriseService.getSuffixByCrmEnterpriseId(flowWashSaleReportDTO.getCrmId(), BackupUtil.generateTableSuffix(year, month));
                // 如果是已对照的非锁流向
                if (flowWashSaleReportDTO.getCustomerCrmId() != null && flowWashSaleReportDTO.getCustomerCrmId() > 0) {
                    CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getSuffixByCrmEnterpriseId(flowWashSaleReportDTO.getCustomerCrmId(), BackupUtil.generateTableSuffix(year, month));
                    if (crmEnterpriseDO != null) {
                        Integer supplyChainRole = crmEnterpriseDO.getSupplyChainRole();
                        UnlockFlowWashSaleDO unlockFlowWashSaleDO = PojoUtils.map(flowWashSaleReportDTO, UnlockFlowWashSaleDO.class);
                        unlockFlowWashSaleDO.setUfwtId(ufwtId);
                        unlockFlowWashSaleDO.setCustomerClassification(Long.valueOf(supplyChainRole));
                        unlockFlowWashSaleDO.setFlowWashSaleReportId(flowWashSaleReportDTO.getId());
                        unlockFlowWashSaleDO.setDistributionStatus(1);
                        if (supplier != null) {
                            unlockFlowWashSaleDO.setProvinceName(supplier.getProvinceName());
                            unlockFlowWashSaleDO.setProvinceCode(supplier.getProvinceCode());
                            unlockFlowWashSaleDO.setCityName(supplier.getCityName());
                            unlockFlowWashSaleDO.setCityCode(supplier.getCityCode());
                            unlockFlowWashSaleDO.setRegionName(supplier.getRegionName());
                            unlockFlowWashSaleDO.setRegionCode(supplier.getRegionCode());
                        }
                        unlockFlowWashSaleDOList.add(unlockFlowWashSaleDO);
                        continue;
                    }
                }

                //  走非锁客户分类明细
                String customerName = flowWashSaleReportDTO.getOriginalEnterpriseName();

                UnlockCustomerClassDetailDO unlockCustomerClassDetailDO = findByCrmIdAndCustomerName(crmEnterpriseId, customerName);
                if (unlockCustomerClassDetailDO != null && unlockCustomerClassDetailDO.getClassFlag() == 1) {
                    Integer customerClassification = unlockCustomerClassDetailDO.getCustomerClassification();

                    // 生成UnlockFlowWashSaleDO
                    UnlockFlowWashSaleDO unlockFlowWashSaleDO = PojoUtils.map(flowWashSaleReportDTO, UnlockFlowWashSaleDO.class);
                    unlockFlowWashSaleDO.setUfwtId(ufwtId);
                    unlockFlowWashSaleDO.setCustomerClassification(customerClassification.longValue());
                    unlockFlowWashSaleDO.setFlowWashSaleReportId(flowWashSaleReportDTO.getId());
                    unlockFlowWashSaleDO.setDistributionStatus(1);
                    if (supplier != null) {
                        unlockFlowWashSaleDO.setProvinceName(supplier.getProvinceName());
                        unlockFlowWashSaleDO.setProvinceCode(supplier.getProvinceCode());
                        unlockFlowWashSaleDO.setCityName(supplier.getCityName());
                        unlockFlowWashSaleDO.setCityCode(supplier.getCityCode());
                        unlockFlowWashSaleDO.setRegionName(supplier.getRegionName());
                        unlockFlowWashSaleDO.setRegionCode(supplier.getRegionCode());
                    }
                    unlockFlowWashSaleDOList.add(unlockFlowWashSaleDO);
                    continue;
                }

                //  明细中没有，需要走非锁客户分类规则
                UnlockCustomerClassRuleDO unlockCustomerClassRuleDO = unlockCustomerClassRuleService.ruleExecute(customerName);

                //  生成或更新非锁客户分类明细
                saveOrUpdateByRuleMatch(crmEnterpriseId, unlockFlowWashTaskDO.getName(), customerName, unlockCustomerClassDetailDO, unlockCustomerClassRuleDO);

                // 生成非锁流向数据
                UnlockFlowWashSaleDO unlockFlowWashSaleDO = PojoUtils.map(flowWashSaleReportDTO, UnlockFlowWashSaleDO.class);
                if (unlockCustomerClassRuleDO != null) {
                    unlockFlowWashSaleDO.setCustomerClassification(unlockCustomerClassRuleDO.getCustomerClassification().longValue());
                }
                unlockFlowWashSaleDO.setFlowWashSaleReportId(flowWashSaleReportDTO.getId());
                unlockFlowWashSaleDO.setUfwtId(ufwtId);
                unlockFlowWashSaleDO.setDistributionStatus(1);
                if (supplier != null) {
                    unlockFlowWashSaleDO.setProvinceName(supplier.getProvinceName());
                    unlockFlowWashSaleDO.setProvinceCode(supplier.getProvinceCode());
                    unlockFlowWashSaleDO.setCityName(supplier.getCityName());
                    unlockFlowWashSaleDO.setCityCode(supplier.getCityCode());
                    unlockFlowWashSaleDO.setRegionName(supplier.getRegionName());
                    unlockFlowWashSaleDO.setRegionCode(supplier.getRegionCode());
                }
                unlockFlowWashSaleDOList.add(unlockFlowWashSaleDO);
            }

            unlockFlowWashSaleService.batchInsert(unlockFlowWashSaleDOList);
            current++;

            if (list.size() < size) {
                break;
            }
        }


    }

    @Override
    public UnlockCustomerClassDetailDO findByCrmIdAndCustomerName(Long crmId, String customerName) {
        LambdaQueryWrapper<UnlockCustomerClassDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockCustomerClassDetailDO::getCrmEnterpriseId, crmId);
        wrapper.eq(UnlockCustomerClassDetailDO::getCustomerName, customerName);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public void deleteByCrmIdAndCustomerName(Long crmId, String customerName) {
        UnlockCustomerClassDetailDO unlockCustomerClassDetailDO = findByCrmIdAndCustomerName(crmId, customerName);
        if (unlockCustomerClassDetailDO != null) {
            baseMapper.deleteById(unlockCustomerClassDetailDO.getId());
        }
    }

    private void saveOrUpdateByRuleMatch(Long crmEnterpriseId, String ename, String customerName, UnlockCustomerClassDetailDO unlockCustomerClassDetailDO, UnlockCustomerClassRuleDO unlockCustomerClassRuleDO) {
        //  生成或更新非锁客户分类明细
        if (unlockCustomerClassRuleDO == null) {
            if (unlockCustomerClassDetailDO == null) {      // 新增未分类明细
                UnlockCustomerClassDetailDO detail = new UnlockCustomerClassDetailDO();
                detail.setCrmEnterpriseId(crmEnterpriseId);
                detail.setEname(ename);
                detail.setCustomerName(customerName);
                detail.setClassFlag(0);    // 未分类
                save(detail);
            }
        } else {
            if (unlockCustomerClassDetailDO != null) {      // 更新
                unlockCustomerClassDetailDO.setClassGround(1);  // 规则
                unlockCustomerClassDetailDO.setClassFlag(1);    // 已分类
                unlockCustomerClassDetailDO.setCustomerClassification(unlockCustomerClassRuleDO.getCustomerClassification());
                unlockCustomerClassDetailDO.setRuleId(unlockCustomerClassRuleDO.getRuleId());
                unlockCustomerClassDetailDO.setRemark("规则ID：" + unlockCustomerClassRuleDO.getRuleId());
                unlockCustomerClassDetailDO.setLastOpTime(DateUtil.date());
                unlockCustomerClassDetailDO.setLastOpUser(1L);  // 设置为admin
                updateById(unlockCustomerClassDetailDO);
            } else {    // 新增已分类明细
                UnlockCustomerClassDetailDO detail = new UnlockCustomerClassDetailDO();
                detail.setCrmEnterpriseId(crmEnterpriseId);
                detail.setEname(ename);
                detail.setCustomerName(customerName);
                detail.setClassGround(1);  // 规则
                detail.setClassFlag(1);    // 已分类
                detail.setCustomerClassification(unlockCustomerClassRuleDO.getCustomerClassification());
                detail.setRuleId(unlockCustomerClassRuleDO.getRuleId());
                detail.setRemark("规则ID：" + unlockCustomerClassRuleDO.getRuleId());
                detail.setLastOpTime(DateUtil.date());
                detail.setLastOpUser(1L);  // 设置为admin
                save(detail);
            }
        }
    }


}
