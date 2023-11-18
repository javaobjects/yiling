package com.yiling.dataflow.wash.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dao.UnlockCustomerClassRuleMapper;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCustomerClassRulePageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCustomerClassRuleRequest;
import com.yiling.dataflow.wash.entity.UnlockCustomerClassRuleDO;
import com.yiling.dataflow.wash.entity.UnlockCustomerMatchingTaskDO;
import com.yiling.dataflow.wash.service.UnlockCustomerClassRuleService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 非锁客户分类规则表 服务实现类
 * </p>
 *
 * @author baifc
 * @since 2023-04-26
 */
@Service
public class UnlockCustomerClassRuleServiceImpl extends BaseServiceImpl<UnlockCustomerClassRuleMapper, UnlockCustomerClassRuleDO> implements UnlockCustomerClassRuleService {

    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @Override
    public List<UnlockCustomerClassRuleDO> getAvailableList() {
        LambdaQueryWrapper<UnlockCustomerClassRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockCustomerClassRuleDO::getStatus, 1);
        wrapper.orderByAsc(UnlockCustomerClassRuleDO::getOrderNo);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Page<UnlockCustomerClassRuleDO> listPage(QueryUnlockCustomerClassRulePageRequest request) {
        Page<UnlockCustomerClassRuleDO> page = new Page<>(request.getCurrent(), request.getSize());

        LambdaQueryWrapper<UnlockCustomerClassRuleDO> wrapper = new LambdaQueryWrapper<>();
        if (request.getRuleId() != null) {
            wrapper.eq(UnlockCustomerClassRuleDO::getRuleId, request.getRuleId());
        }
        if (StringUtils.isNotEmpty(request.getRemark())) {
            wrapper.like(UnlockCustomerClassRuleDO::getRemark, request.getRemark());
        }
        if (request.getCustomerClassification() != null) {
            wrapper.eq(UnlockCustomerClassRuleDO::getCustomerClassification, request.getCustomerClassification());
        }
        if (request.getStatus() != null) {
            wrapper.eq(UnlockCustomerClassRuleDO::getStatus, request.getStatus());
        }
        if (request.getStartOpTime() != null) {
            wrapper.ge(UnlockCustomerClassRuleDO::getUpdateTime, DateUtil.beginOfDay(request.getStartOpTime()));
        }
        if (request.getEndOpTime() != null) {
            wrapper.le(UnlockCustomerClassRuleDO::getUpdateTime, DateUtil.endOfDay(request.getEndOpTime()));
        }
        wrapper.orderByAsc(UnlockCustomerClassRuleDO::getOrderNo);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public UnlockCustomerClassRuleDO getById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public void save(SaveOrUpdateUnlockCustomerClassRuleRequest request) {
        verifyOrderNo(request);

        UnlockCustomerClassRuleDO unlockCustomerClassRuleDO = PojoUtils.map(request, UnlockCustomerClassRuleDO.class);
        unlockCustomerClassRuleDO.setLastOpTime(request.getOpTime());
        unlockCustomerClassRuleDO.setLastOpUser(request.getOpUserId());

        // 加锁
        String lockKey = RedisKey.generate("dataflow", "genereate-customer-class-rule-id");
        String lockId = redisDistributedLock.lock2(lockKey, 3, 3, TimeUnit.SECONDS);
        try {
            //  生成唯一规则id
            Long ruleId = null;
            String todayStr = DateUtil.format(new Date(), "yyyyMMdd");
            List<Long> ruleIdList = baseMapper.getLastRuleId(todayStr);
            if (CollUtil.isEmpty(ruleIdList)) {
                ruleId = Long.parseLong(todayStr + "0001");

            } else {
                Long lastRuleIdStr = ruleIdList.get(0);
                ruleId = lastRuleIdStr + 1;
            }
            unlockCustomerClassRuleDO.setRuleId(ruleId);
            // 插入
            baseMapper.insert(unlockCustomerClassRuleDO);
        } finally {
            redisDistributedLock.releaseLock(lockKey, lockId);
        }
    }

    @Override
    public void update(SaveOrUpdateUnlockCustomerClassRuleRequest request) {
        verifyOrderNo(request);
        UnlockCustomerClassRuleDO unlockCustomerClassRuleDO = PojoUtils.map(request, UnlockCustomerClassRuleDO.class);
        unlockCustomerClassRuleDO.setLastOpTime(request.getOpTime());
        unlockCustomerClassRuleDO.setLastOpUser(request.getOpUserId());
        baseMapper.updateById(unlockCustomerClassRuleDO);
    }

    @Override
    public void delete(Long id) {
        UnlockCustomerClassRuleDO unlockCustomerClassRuleDO = new UnlockCustomerClassRuleDO();
        unlockCustomerClassRuleDO.setId(id);
        this.deleteByIdWithFill(unlockCustomerClassRuleDO);
    }

    @Override
    public UnlockCustomerClassRuleDO ruleExecute(String customerName) {
        List<UnlockCustomerClassRuleDO> unlockCustomerClassRuleDOList = getAvailableList();
        for (UnlockCustomerClassRuleDO unlockCustomerClassRuleDO : unlockCustomerClassRuleDOList) {
            Integer condition1 = unlockCustomerClassRuleDO.getCondition1();     // 条件1 1-客户名称 2-客户名称结尾
            Integer operator1 = unlockCustomerClassRuleDO.getOperator1();   // 运算符1 1-包含 2-不包含 3-等于
            String conditionValue1 = unlockCustomerClassRuleDO.getConditionValue1();    // 条件1的值

            Integer condition2 = unlockCustomerClassRuleDO.getCondition2();
            Integer operator2 = unlockCustomerClassRuleDO.getOperator2();
            String conditionValue2 = unlockCustomerClassRuleDO.getConditionValue2();

            if (ruleMatch(condition1, operator1, conditionValue1, customerName)) {  // 条件1满足
                if (unlockCustomerClassRuleDO.getConditionRelation() == 2) {    // 或
                    return unlockCustomerClassRuleDO;
                } else {
                    if (ruleMatch(condition2, operator2, conditionValue2, customerName)) {
                        return unlockCustomerClassRuleDO;
                    }
                }

            } else  {     // 条件1不满足
                if (unlockCustomerClassRuleDO.getConditionRelation() == 2) {    // 或
                    if (ruleMatch(condition2, operator2, conditionValue2, customerName)) {
                        return unlockCustomerClassRuleDO;
                    }
                }
            }
        }
        return null;
    }

    /**
     *  规则匹配
     * @param condition 条件1 1-客户名称 2-客户名称结尾
     * @param operator 运算符1 1-包含 2-不包含 3-等于
     * @param contitionValue 条件1的值
     * @param customerName 客户名称
     * @return
     */
    private boolean ruleMatch(Integer condition, Integer operator, String conditionValue, String customerName) {
        if (condition == 1) {
            if (operator == 1) {  // 客户名称包含
                return customerName.contains(conditionValue);
            }
            if (operator == 2) {    // 客户名称不包含
                return !customerName.contains(conditionValue);
            }
        }
        if (condition == 2 && operator == 3) {      // 客户名称结尾等于
            return customerName.endsWith(conditionValue);
        }
        return false;
    }

    private void verifyOrderNo(SaveOrUpdateUnlockCustomerClassRuleRequest request) {
        LambdaQueryWrapper<UnlockCustomerClassRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockCustomerClassRuleDO::getOrderNo, request.getOrderNo());
        if (request.getId() != null) {
            wrapper.ne(UnlockCustomerClassRuleDO::getId, request.getId());
        }
        List<UnlockCustomerClassRuleDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isNotEmpty(list)) {
            throw new BusinessException(ResultCode.EXCEL_DATA_SAVING_FAILED, "排序数值不允许重复！");
        }
    }
}
