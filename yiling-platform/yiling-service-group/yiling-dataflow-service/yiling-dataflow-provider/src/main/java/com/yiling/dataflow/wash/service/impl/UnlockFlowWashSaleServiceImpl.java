package com.yiling.dataflow.wash.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dao.UnlockFlowWashSaleMapper;
import com.yiling.dataflow.wash.dto.request.QueryUnlockFlowWashSalePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveUnlockFlowWashSaleRequest;
import com.yiling.dataflow.wash.dto.request.UpdateUnlockFlowWashSaleRequest;
import com.yiling.dataflow.wash.entity.UnlockFlowWashSaleDO;
import com.yiling.dataflow.wash.service.UnlockFlowWashSaleService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 流向销售合并报 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Service
public class UnlockFlowWashSaleServiceImpl extends BaseServiceImpl<UnlockFlowWashSaleMapper, UnlockFlowWashSaleDO> implements UnlockFlowWashSaleService {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchInsert(List<UnlockFlowWashSaleDO> unlockFlowWashSaleDOList) {
        // 分页截取并插入
        if (CollUtil.isEmpty(unlockFlowWashSaleDOList)) {
            return;
        }
        handleFlowSaleWashDOList(unlockFlowWashSaleDOList);

        int total = unlockFlowWashSaleDOList.size();
        int pageCount;
        int size = 1000;

        if (total % size == 0) {
            pageCount = total / size;
        } else {
            pageCount = total / size + 1;
        }

        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引
        for (int i = 0; i < pageCount; i++) {
            fromIndex = i * size;
            toIndex = (i + 1) * size;
            if (toIndex > total) {
                toIndex = total;
            }
            baseMapper.insertBatchSomeColumn(unlockFlowWashSaleDOList.subList(fromIndex, toIndex));
        }
    }

    @Override
    public void updateBatchById(List<SaveUnlockFlowWashSaleRequest> unlockFlowWashSaleList) {
        this.updateBatchById(PojoUtils.map(unlockFlowWashSaleList, UnlockFlowWashSaleDO.class));
    }

    @Override
    public List<UnlockFlowWashSaleDO> getListByUfwtId(Long ufwtId) {
        LambdaQueryWrapper<UnlockFlowWashSaleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockFlowWashSaleDO::getUfwtId, ufwtId);
        List<UnlockFlowWashSaleDO> list = baseMapper.selectList(wrapper);
        return list;
    }

    @Override
    public void updateClassificationCrmIdAndCustomerName(UpdateUnlockFlowWashSaleRequest request) {
        LambdaQueryWrapper<UnlockFlowWashSaleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockFlowWashSaleDO::getYear, request.getYear());
        wrapper.eq(UnlockFlowWashSaleDO::getMonth, request.getMonth());
        wrapper.eq(UnlockFlowWashSaleDO::getCrmId, request.getCrmId());
        wrapper.eq(UnlockFlowWashSaleDO::getOriginalEnterpriseName, request.getCustomerName());

        List<UnlockFlowWashSaleDO> list = baseMapper.selectList(wrapper);
        for (UnlockFlowWashSaleDO unlockFlowWashSaleDO : list) {
            unlockFlowWashSaleDO.setCustomerClassification(request.getCustomerClassification().longValue());
        }

        this.updateBatchById(list);
    }

    @Override
    public Page<UnlockFlowWashSaleDO> pageList(QueryUnlockFlowWashSalePageRequest request) {
        QueryWrapper<UnlockFlowWashSaleDO> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getYear, request.getYear());
        queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getMonth, request.getMonth());

        if (request.getCrmId() != null && request.getCrmId() != 0) {
            queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getCrmId, request.getCrmId());
        }
        if (request.getCustomerCrmId() != null && request.getCustomerCrmId() != 0) {
            queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getCustomerCrmId, request.getCustomerCrmId());
        }
        if (request.getGoodsCode() != null && request.getGoodsCode() != 0) {
            queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getGoodsCode, request.getGoodsCode());
        }
        if (StringUtils.isNotBlank(request.getSoGoodsName())) {
            queryWrapper.lambda().likeRight(UnlockFlowWashSaleDO::getSoGoodsName, request.getSoGoodsName());
        }
        if (StringUtils.isNotBlank(request.getOriginalEnterpriseName())) {
            queryWrapper.lambda().likeRight(UnlockFlowWashSaleDO::getOriginalEnterpriseName, request.getOriginalEnterpriseName());
        }
        if (request.getJudgment() != null && request.getJudgment() != 0) {
            queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getJudgment, request.getJudgment());
        }
        if (request.getDistributionStatus() != null && request.getDistributionStatus() != 0) {
            queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getDistributionStatus, request.getDistributionStatus());
        }
        if (request.getDistributionSource() != null && request.getDistributionSource() != 0) {
            queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getDistributionSource, request.getDistributionSource());
        }
        if (request.getStartUpdate() != null) {
            queryWrapper.lambda().ge(UnlockFlowWashSaleDO::getUpdateTime, request.getStartUpdate());
        }
        if (request.getEndUpdate() != null) {
            queryWrapper.lambda().le(UnlockFlowWashSaleDO::getUpdateTime, request.getEndUpdate());
        }


        queryWrapper.lambda().orderByDesc(UnlockFlowWashSaleDO::getId);

        Page<UnlockFlowWashSaleDO> reportFlowDOList = this.page(request.getPage(), queryWrapper);

        return reportFlowDOList;
    }

    @Override
    public Integer countDistributionStatus(QueryUnlockFlowWashSalePageRequest request) {
        QueryWrapper<UnlockFlowWashSaleDO> queryWrapper = new QueryWrapper<>();

        queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getYear, request.getYear());
        queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getMonth, request.getMonth());

        if (request.getCrmId() != null && request.getCrmId() != 0) {
            queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getCrmId, request.getCrmId());
        }
        if (request.getCustomerCrmId() != null && request.getCustomerCrmId() != 0) {
            queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getCustomerCrmId, request.getCustomerCrmId());
        }
        if (request.getGoodsCode() != null && request.getGoodsCode() != 0) {
            queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getGoodsCode, request.getGoodsCode());
        }
        if (StringUtils.isNotBlank(request.getSoGoodsName())) {
            queryWrapper.lambda().likeRight(UnlockFlowWashSaleDO::getSoGoodsName, request.getSoGoodsName());
        }
        if (StringUtils.isNotBlank(request.getOriginalEnterpriseName())) {
            queryWrapper.lambda().likeRight(UnlockFlowWashSaleDO::getOriginalEnterpriseName, request.getOriginalEnterpriseName());
        }
        if (request.getJudgment() != null && request.getJudgment() != 0) {
            queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getJudgment, request.getJudgment());
        }
        if (request.getDistributionStatus() != null && request.getDistributionStatus() != 0) {
            queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getDistributionStatus, request.getDistributionStatus());
        }
        if (request.getDistributionSource() != null && request.getDistributionSource() != 0) {
            queryWrapper.lambda().eq(UnlockFlowWashSaleDO::getDistributionSource, request.getDistributionSource());
        }
        if (request.getStartUpdate() != null) {
            queryWrapper.lambda().ge(UnlockFlowWashSaleDO::getUpdateTime, request.getStartUpdate());
        }
        if (request.getEndUpdate() != null) {
            queryWrapper.lambda().le(UnlockFlowWashSaleDO::getUpdateTime, request.getEndUpdate());
        }
        return this.count(queryWrapper);
    }

    @Override
    public Integer deleteByUfwtId(Long ufwtId) {
        return this.baseMapper.deleteByUfwtId(ufwtId);
    }


    private void handleFlowSaleWashDOList(List<UnlockFlowWashSaleDO> unlockFlowWashSaleDOList) {
        for (UnlockFlowWashSaleDO unlockFlowWashSaleDO : unlockFlowWashSaleDOList) {
            unlockFlowWashSaleDO.setId(null);
            unlockFlowWashSaleDO.setCrmId(Convert.toLong(unlockFlowWashSaleDO.getCrmId(), 0L));
            unlockFlowWashSaleDO.setUfwtId(Convert.toLong(unlockFlowWashSaleDO.getUfwtId(), 0L));
            unlockFlowWashSaleDO.setYear(Convert.toStr(unlockFlowWashSaleDO.getYear(), ""));
            unlockFlowWashSaleDO.setMonth(Convert.toStr(unlockFlowWashSaleDO.getMonth(), ""));
            unlockFlowWashSaleDO.setFlowSaleWashId(Convert.toLong(unlockFlowWashSaleDO.getFlowSaleWashId(), 0L));
            unlockFlowWashSaleDO.setFlowWashSaleReportId(Convert.toLong(unlockFlowWashSaleDO.getFlowWashSaleReportId(), 0L));
            unlockFlowWashSaleDO.setSoTime(Convert.toDate(unlockFlowWashSaleDO.getSoTime(), DateUtil.parse("1970-01-01", "yyyy-MM-dd")));
            unlockFlowWashSaleDO.setSoYear(Convert.toStr(unlockFlowWashSaleDO.getSoYear(), ""));
            unlockFlowWashSaleDO.setSoMonth(Convert.toStr(unlockFlowWashSaleDO.getSoMonth(), ""));
            unlockFlowWashSaleDO.setEname(Convert.toStr(unlockFlowWashSaleDO.getEname(), ""));
            unlockFlowWashSaleDO.setOriginalEnterpriseName(Convert.toStr(unlockFlowWashSaleDO.getOriginalEnterpriseName(), ""));
            unlockFlowWashSaleDO.setCustomerCrmId(Convert.toLong(unlockFlowWashSaleDO.getCustomerCrmId(), 0L));
            unlockFlowWashSaleDO.setEnterpriseName(Convert.toStr(unlockFlowWashSaleDO.getEnterpriseName(), ""));
            unlockFlowWashSaleDO.setIsChainFlag(Convert.toInt(unlockFlowWashSaleDO.getIsChainFlag(), 0));
            unlockFlowWashSaleDO.setDepartment(Convert.toStr(unlockFlowWashSaleDO.getDepartment(), ""));
            unlockFlowWashSaleDO.setBusinessDepartment(Convert.toStr(unlockFlowWashSaleDO.getBusinessDepartment(), ""));
            unlockFlowWashSaleDO.setProvincialArea(Convert.toStr(unlockFlowWashSaleDO.getProvincialArea(), ""));
            unlockFlowWashSaleDO.setBusinessProvince(Convert.toStr(unlockFlowWashSaleDO.getBusinessProvince(), ""));
            unlockFlowWashSaleDO.setDistrictCountyCode(Convert.toStr(unlockFlowWashSaleDO.getDistrictCountyCode(), ""));
            unlockFlowWashSaleDO.setDistrictCounty(Convert.toStr(unlockFlowWashSaleDO.getDistrictCounty(), ""));
            unlockFlowWashSaleDO.setSuperiorSupervisorCode(Convert.toStr(unlockFlowWashSaleDO.getSuperiorSupervisorCode(), ""));
            unlockFlowWashSaleDO.setSuperiorSupervisorName(Convert.toStr(unlockFlowWashSaleDO.getSuperiorSupervisorName(), ""));
            unlockFlowWashSaleDO.setRepresentativeCode(Convert.toStr(unlockFlowWashSaleDO.getRepresentativeCode(), ""));
            unlockFlowWashSaleDO.setRepresentativeName(Convert.toStr(unlockFlowWashSaleDO.getRepresentativeName(), ""));
            unlockFlowWashSaleDO.setPostCode(Convert.toLong(unlockFlowWashSaleDO.getPostCode(), 0L));
            unlockFlowWashSaleDO.setPostName(Convert.toStr(unlockFlowWashSaleDO.getPostName(), ""));
            unlockFlowWashSaleDO.setProvinceCode(Convert.toStr(unlockFlowWashSaleDO.getProvinceCode(), ""));
            unlockFlowWashSaleDO.setProvinceName(Convert.toStr(unlockFlowWashSaleDO.getProvinceName(), ""));
            unlockFlowWashSaleDO.setCityCode(Convert.toStr(unlockFlowWashSaleDO.getCityCode(), ""));
            unlockFlowWashSaleDO.setCityName(Convert.toStr(unlockFlowWashSaleDO.getCityName(), ""));
            unlockFlowWashSaleDO.setRegionCode(Convert.toStr(unlockFlowWashSaleDO.getRegionCode(), ""));
            unlockFlowWashSaleDO.setRegionName(Convert.toStr(unlockFlowWashSaleDO.getRegionName(), ""));
            unlockFlowWashSaleDO.setSoGoodsName(Convert.toStr(unlockFlowWashSaleDO.getSoGoodsName(), ""));
            unlockFlowWashSaleDO.setSoSpecifications(Convert.toStr(unlockFlowWashSaleDO.getSoSpecifications(), ""));
            unlockFlowWashSaleDO.setGoodsCode(Convert.toLong(unlockFlowWashSaleDO.getGoodsCode(), 0L));
            unlockFlowWashSaleDO.setGoodsName(Convert.toStr(unlockFlowWashSaleDO.getGoodsName(), ""));
            unlockFlowWashSaleDO.setGoodsSpec(Convert.toStr(unlockFlowWashSaleDO.getGoodsSpec(), ""));
            unlockFlowWashSaleDO.setSoQuantity(Convert.toBigDecimal(unlockFlowWashSaleDO.getSoQuantity(), BigDecimal.ZERO));
            unlockFlowWashSaleDO.setSalesPrice(Convert.toBigDecimal(unlockFlowWashSaleDO.getSalesPrice(), BigDecimal.ZERO));
            unlockFlowWashSaleDO.setSoTotalAmount(Convert.toBigDecimal(unlockFlowWashSaleDO.getSoTotalAmount(), BigDecimal.ZERO));
            unlockFlowWashSaleDO.setDistributionSource(Convert.toInt(unlockFlowWashSaleDO.getDistributionSource(), 0));
            unlockFlowWashSaleDO.setCustomerClassification(Convert.toLong(unlockFlowWashSaleDO.getCustomerClassification(), 0L));
            unlockFlowWashSaleDO.setUnlockSaleRuleId(Convert.toLong(unlockFlowWashSaleDO.getUnlockSaleRuleId(), 0L));
            unlockFlowWashSaleDO.setJudgment(Convert.toInt(unlockFlowWashSaleDO.getJudgment(), 0));
            unlockFlowWashSaleDO.setRuleNotes(Convert.toStr(unlockFlowWashSaleDO.getRuleNotes(), ""));
            unlockFlowWashSaleDO.setDistributionStatus(Convert.toInt(unlockFlowWashSaleDO.getDistributionStatus(), 0));
            unlockFlowWashSaleDO.setOperateStatus(Convert.toInt(unlockFlowWashSaleDO.getOperateStatus(), 0));
            unlockFlowWashSaleDO.setCommerceJobNumber(Convert.toStr(unlockFlowWashSaleDO.getCommerceJobNumber(), ""));
            unlockFlowWashSaleDO.setCommerceLiablePerson(Convert.toStr(unlockFlowWashSaleDO.getCommerceLiablePerson(), ""));
            unlockFlowWashSaleDO.setEnterpriseCersId(Convert.toLong(unlockFlowWashSaleDO.getEnterpriseCersId(), 0L));
            unlockFlowWashSaleDO.setCreateTime(Convert.toDate(unlockFlowWashSaleDO.getCreateTime(), new Date()));
            unlockFlowWashSaleDO.setUpdateTime(Convert.toDate(unlockFlowWashSaleDO.getUpdateTime(), new Date()));
            unlockFlowWashSaleDO.setCreateUser(Convert.toLong(unlockFlowWashSaleDO.getCreateUser(), 0L));
            unlockFlowWashSaleDO.setUpdateUser(Convert.toLong(unlockFlowWashSaleDO.getUpdateUser(), 0L));
            unlockFlowWashSaleDO.setRemark(Convert.toStr(unlockFlowWashSaleDO.getRemark(), ""));
        }
    }
}
