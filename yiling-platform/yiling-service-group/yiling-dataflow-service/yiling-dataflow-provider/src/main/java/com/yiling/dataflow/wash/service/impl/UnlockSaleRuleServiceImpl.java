package com.yiling.dataflow.wash.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dao.UnlockSaleRuleMapper;
import com.yiling.dataflow.wash.dto.request.QueryuUnlockSaleRulePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockSaleRuleRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleCustomerRangeDO;
import com.yiling.dataflow.wash.entity.UnlockSaleDepartmentDO;
import com.yiling.dataflow.wash.entity.UnlockSaleRuleDO;
import com.yiling.dataflow.wash.enums.WashErrorEnum;
import com.yiling.dataflow.wash.service.UnlockSaleCustomerRangeService;
import com.yiling.dataflow.wash.service.UnlockSaleDepartmentService;
import com.yiling.dataflow.wash.service.UnlockSaleRuleService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.PojoUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Service
public class UnlockSaleRuleServiceImpl extends BaseServiceImpl<UnlockSaleRuleMapper, UnlockSaleRuleDO> implements UnlockSaleRuleService {

    @Autowired
    private   UnlockSaleCustomerRangeService unlockSaleCustomerRangeService;
    @Autowired
    private   UnlockSaleDepartmentService    unlockSaleDepartmentService;
    @Resource
    protected RedisDistributedLock           redisDistributedLock;

    @Override
    public List<UnlockSaleRuleDO> getUnlockSaleRuleList() {
        LambdaQueryWrapper<UnlockSaleRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockSaleRuleDO::getStatus,0);
        List<UnlockSaleRuleDO> list = baseMapper.selectList(wrapper);
        return list;
    }


    @Override
    public Page<UnlockSaleRuleDO> listPage(QueryuUnlockSaleRulePageRequest request) {
        Page<UnlockSaleRuleDO> page = new Page<>(request.getCurrent(), request.getSize());

        LambdaQueryWrapper<UnlockSaleRuleDO> wrapper = new LambdaQueryWrapper<>();
        if (request.getId() != null && request.getId() != 0) {
            wrapper.eq(UnlockSaleRuleDO::getId, request.getId());
        }
        if (StrUtil.isNotEmpty(request.getCode())) {
            wrapper.like(UnlockSaleRuleDO::getCode, request.getCode());
        }
        if (StrUtil.isNotEmpty(request.getName())) {
            wrapper.like(UnlockSaleRuleDO::getName, request.getName());
        }
        if (request.getStatus() != null) {
            wrapper.eq(UnlockSaleRuleDO::getStatus, request.getStatus());
        }
        if (request.getStartUpdate() != null) {
            wrapper.ge(UnlockSaleRuleDO::getUpdateTime, request.getStartUpdate());
        }
        if (request.getEndUpdate() != null) {
            wrapper.le(UnlockSaleRuleDO::getUpdateTime, request.getEndUpdate());
        }
        wrapper.orderByAsc(UnlockSaleRuleDO::getSort);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public Long saveOrUpdate(SaveOrUpdateUnlockSaleRuleRequest ruleRequest) {
        if (ruleRequest.getId() != null && ruleRequest.getId() != 0) {
            if (ruleRequest.getSaveUnlockSaleCustomerRangeRequest() != null) {
                UnlockSaleCustomerRangeDO unlockSaleCustomerRangeDO = new UnlockSaleCustomerRangeDO();
                unlockSaleCustomerRangeDO.setRuleId(ruleRequest.getId());
                unlockSaleCustomerRangeDO.setOpUserId(ruleRequest.getOpUserId());
                unlockSaleCustomerRangeDO.setClassification(StringUtils.join(ruleRequest.getSaveUnlockSaleCustomerRangeRequest().getClassificationIds(), ","));
                unlockSaleCustomerRangeDO.setKeyword(ruleRequest.getSaveUnlockSaleCustomerRangeRequest().getKeywords());
                UnlockSaleCustomerRangeDO unlockSaleCustomerRange = unlockSaleCustomerRangeService.getUnlockSaleCustomerRangeByRuleId(ruleRequest.getId());
                if (unlockSaleCustomerRange != null) {
                    unlockSaleCustomerRangeDO.setId(unlockSaleCustomerRange.getId());
                }
                unlockSaleCustomerRangeService.saveOrUpdate(unlockSaleCustomerRangeDO);
            }
            if (ruleRequest.getSaveUnlockSaleDepartmentRequest() != null) {
                UnlockSaleDepartmentDO unlockSaleDepartmentDO = new UnlockSaleDepartmentDO();
                unlockSaleDepartmentDO.setOpUserId(ruleRequest.getOpUserId());
                unlockSaleDepartmentDO.setRuleId(ruleRequest.getId());
                unlockSaleDepartmentDO.setBusinessDepartmentName(ruleRequest.getSaveUnlockSaleDepartmentRequest().getBusinessDepartmentName());
                unlockSaleDepartmentDO.setBusinessDepartmentFullName(ruleRequest.getSaveUnlockSaleDepartmentRequest().getBusinessDepartmentFullName());
                unlockSaleDepartmentDO.setCrmBusinessDepartmentCode(ruleRequest.getSaveUnlockSaleDepartmentRequest().getCrmBusinessDepartmentCode());
                unlockSaleDepartmentDO.setCrmDepartmentCode(ruleRequest.getSaveUnlockSaleDepartmentRequest().getCrmDepartmentCode());
                unlockSaleDepartmentDO.setDepartmentName(ruleRequest.getSaveUnlockSaleDepartmentRequest().getDepartmentName());
                unlockSaleDepartmentDO.setDepartmentFullName(ruleRequest.getSaveUnlockSaleDepartmentRequest().getDepartmentFullName());
                unlockSaleDepartmentDO.setType(ruleRequest.getSaveUnlockSaleDepartmentRequest().getType());
                unlockSaleDepartmentDO.setSaleIncludedRange(ruleRequest.getSaveUnlockSaleDepartmentRequest().getSaleIncludedRange());
                UnlockSaleDepartmentDO unlockSaleDepartment = unlockSaleDepartmentService.getUnlockSaleDepartmentByRuleId(ruleRequest.getId());
                if (unlockSaleDepartment != null) {
                    unlockSaleDepartmentDO.setId(unlockSaleDepartment.getId());
                }
                unlockSaleDepartmentService.saveOrUpdate(unlockSaleDepartmentDO);
            }
        }
        UnlockSaleRuleDO unlockSaleRuleDO = PojoUtils.map(ruleRequest, UnlockSaleRuleDO.class);

        LambdaQueryWrapper<UnlockSaleRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockSaleRuleDO::getSort,unlockSaleRuleDO.getSort());
        if(unlockSaleRuleDO.getId()!=null&&unlockSaleRuleDO.getId()!=0){
            wrapper.ne(UnlockSaleRuleDO::getId,unlockSaleRuleDO.getId());
        }
        Integer i = baseMapper.selectCount(wrapper);
        if(i>0){
            throw new BusinessException(WashErrorEnum.UNLOCK_SALE_WASH_RULE_SORT_EXCEPTION);
        }
        this.saveOrUpdate(unlockSaleRuleDO);
        return unlockSaleRuleDO.getId();
    }

    @Override
    public String generateCode() {
        String time = DateUtil.format(new Date(), "yyyyMMdd");
        String lockName = RedisKey.generate("flow", "unlock", "rule", "add", time);
        StringBuffer sb = new StringBuffer();
        sb.append("SF").append(time).append(String.format("%03d", RandomUtil.randomInt(999)));
        String lockId = "";
        UnlockSaleRuleDO unlockSaleRuleDO = null;
        try {
            lockId = redisDistributedLock.lock2(lockName, 3, 3, TimeUnit.SECONDS);
            LambdaQueryWrapper<UnlockSaleRuleDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UnlockSaleRuleDO::getCode, sb.toString());
            unlockSaleRuleDO = this.getOne(wrapper);
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        if (unlockSaleRuleDO != null) {
            return generateCode();
        } else {
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        System.out.println(RandomUtil.randomInt(999));
    }
}
