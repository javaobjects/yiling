package com.yiling.dataflow.wash.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.dataflow.wash.entity.UnlockAreaRecordDO;
import com.yiling.dataflow.wash.entity.UnlockAreaRecordRelationDO;
import com.yiling.dataflow.wash.dao.UnlockAreaRecordRelationMapper;
import com.yiling.dataflow.wash.service.UnlockAreaRecordRelationService;
import com.yiling.dataflow.wash.service.UnlockAreaRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 区域备案 服务实现类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-15
 */
@Service
public class UnlockAreaRecordRelationServiceImpl extends BaseServiceImpl<UnlockAreaRecordRelationMapper, UnlockAreaRecordRelationDO> implements UnlockAreaRecordRelationService {

    @Autowired
    private UnlockAreaRecordService unlockAreaRecordService;

    @Override
    public List<UnlockAreaRecordRelationDO> getByUnlockAreaRecordId(Long unlockAreaRecord) {
        LambdaQueryWrapper<UnlockAreaRecordRelationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockAreaRecordRelationDO::getUnlockAreaRecordId, unlockAreaRecord);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void deleteByUnlockAreaRecordId(Long unlockAreaRecord) {
        LambdaUpdateWrapper<UnlockAreaRecordRelationDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UnlockAreaRecordRelationDO::getUnlockAreaRecordId, unlockAreaRecord);
        baseMapper.delete(wrapper);
    }

    @Override
    public List<UnlockAreaRecordRelationDO> getByClassAndCategoryId(Integer customerClassification, Long categoryId) {
        List<UnlockAreaRecordDO> unlockAreaRecordDOList = unlockAreaRecordService.getListByClassAndCategory(customerClassification, categoryId);
        if (CollUtil.isEmpty(unlockAreaRecordDOList)) {
            return new ArrayList<>();
        }
        List<Long> unlockAreaRecordIdList = unlockAreaRecordDOList.stream().map(UnlockAreaRecordDO::getId).collect(Collectors.toList());
        LambdaQueryWrapper<UnlockAreaRecordRelationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UnlockAreaRecordRelationDO::getUnlockAreaRecordId, unlockAreaRecordIdList);
        return baseMapper.selectList(wrapper);
    }
}
