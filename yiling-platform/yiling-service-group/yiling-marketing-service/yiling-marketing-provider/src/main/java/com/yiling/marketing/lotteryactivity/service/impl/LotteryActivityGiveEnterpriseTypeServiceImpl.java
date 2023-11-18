package com.yiling.marketing.lotteryactivity.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveEnterpriseDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveEnterpriseTypeDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityGiveEnterpriseTypeMapper;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveEnterpriseTypeService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 抽奖活动-赠送范围企业类型表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Service
public class LotteryActivityGiveEnterpriseTypeServiceImpl extends BaseServiceImpl<LotteryActivityGiveEnterpriseTypeMapper, LotteryActivityGiveEnterpriseTypeDO> implements LotteryActivityGiveEnterpriseTypeService {

    @Override
    public List<Integer> getByLotteryActivityId(Long lotteryActivityId) {
        LambdaQueryWrapper<LotteryActivityGiveEnterpriseTypeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityGiveEnterpriseTypeDO::getLotteryActivityId, lotteryActivityId);
        return this.list(wrapper).stream().map(LotteryActivityGiveEnterpriseTypeDO::getEnterpriseType).distinct().collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGiveEnterpriseTypeByLotteryActivityId(Long lotteryActivityId, List<Integer> typeList, Long opUserId) {
        LambdaQueryWrapper<LotteryActivityGiveEnterpriseTypeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityGiveEnterpriseTypeDO::getLotteryActivityId, lotteryActivityId);
        List<Integer> typeDoList = this.list(wrapper).stream().map(LotteryActivityGiveEnterpriseTypeDO::getEnterpriseType).distinct().collect(Collectors.toList());

        List<Integer> addList = typeList.stream().filter(type -> !typeDoList.contains(type)).collect(Collectors.toList());
        List<Integer> removeList = typeDoList.stream().filter(typeDo -> !typeList.contains(typeDo)).collect(Collectors.toList());

        // 添加企业类型
        if (CollUtil.isNotEmpty(addList)) {
            List<LotteryActivityGiveEnterpriseTypeDO> giveEnterpriseTypeDOList = addList.stream().map(type -> {
                LotteryActivityGiveEnterpriseTypeDO giveEnterpriseTypeDO = new LotteryActivityGiveEnterpriseTypeDO();
                giveEnterpriseTypeDO.setLotteryActivityId(lotteryActivityId);
                giveEnterpriseTypeDO.setEnterpriseType(type);
                giveEnterpriseTypeDO.setOpUserId(opUserId);
                return giveEnterpriseTypeDO;
            }).collect(Collectors.toList());
            this.saveBatch(giveEnterpriseTypeDOList);
        }

        if (CollUtil.isNotEmpty(removeList)) {
            // 删除赠送指定企业类型表数据
            LotteryActivityGiveEnterpriseTypeDO giveEnterpriseTypeDO = new LotteryActivityGiveEnterpriseTypeDO();
            giveEnterpriseTypeDO.setOpUserId(opUserId);
            LambdaQueryWrapper<LotteryActivityGiveEnterpriseTypeDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LotteryActivityGiveEnterpriseTypeDO::getLotteryActivityId, lotteryActivityId);
            queryWrapper.in(LotteryActivityGiveEnterpriseTypeDO::getEnterpriseType, removeList);
            this.batchDeleteWithFill(giveEnterpriseTypeDO, queryWrapper);
        }

        return true;
    }
}
