package com.yiling.dataflow.wash.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.dataflow.wash.entity.UnlockCollectionDetailDO;
import com.yiling.dataflow.wash.entity.UnlockCollectionDetailRelationDO;
import com.yiling.dataflow.wash.dao.UnlockCollectionDetailRelationMapper;
import com.yiling.dataflow.wash.service.UnlockCollectionDetailRelationService;
import com.yiling.dataflow.wash.service.UnlockCollectionDetailService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 采集明细 服务实现类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-17
 */
@Service
public class UnlockCollectionDetailRelationServiceImpl extends BaseServiceImpl<UnlockCollectionDetailRelationMapper, UnlockCollectionDetailRelationDO> implements UnlockCollectionDetailRelationService {

    @Autowired
    private UnlockCollectionDetailService unlockCollectionDetailService;

    @Override
    public List<UnlockCollectionDetailRelationDO> getListByUnlockCollectionDetailId(Long unlockCollectionDetailId) {
        LambdaQueryWrapper<UnlockCollectionDetailRelationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockCollectionDetailRelationDO::getUnlockCollectionDetailId, unlockCollectionDetailId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void deleteByUnlockCollectionDetailId(Long unlockCollectionDetailId) {
        LambdaUpdateWrapper<UnlockCollectionDetailRelationDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UnlockCollectionDetailRelationDO::getUnlockCollectionDetailId, unlockCollectionDetailId);
        baseMapper.delete(wrapper);
    }

    @Override
    public List<UnlockCollectionDetailRelationDO> getListByCrmGoodCode(Long crmGoodsCode) {
        List<UnlockCollectionDetailDO> unlockCollectionDetailDOList = unlockCollectionDetailService.getListByCrmGoodsCode(crmGoodsCode);
        if (CollUtil.isEmpty(unlockCollectionDetailDOList)) {
            return new ArrayList<>();
        }
        List<Long> idList = unlockCollectionDetailDOList.stream().map(UnlockCollectionDetailDO::getId).collect(Collectors.toList());

        LambdaQueryWrapper<UnlockCollectionDetailRelationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UnlockCollectionDetailRelationDO::getUnlockCollectionDetailId, idList);
        return baseMapper.selectList(wrapper);
    }
}
