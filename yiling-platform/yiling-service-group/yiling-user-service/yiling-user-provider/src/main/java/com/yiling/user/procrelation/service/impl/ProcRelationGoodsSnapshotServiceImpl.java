package com.yiling.user.procrelation.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.procrelation.dao.ProcRelationGoodsSnapshotMapper;
import com.yiling.user.procrelation.dto.request.QueryProcGoodsSnapshotPageRequest;
import com.yiling.user.procrelation.entity.ProcRelationGoodsSnapshotDO;
import com.yiling.user.procrelation.service.ProcRelationGoodsSnapshotService;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * pop采购关系的可采商品 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-05-19
 */
@Service
public class ProcRelationGoodsSnapshotServiceImpl extends BaseServiceImpl<ProcRelationGoodsSnapshotMapper, ProcRelationGoodsSnapshotDO> implements ProcRelationGoodsSnapshotService {

    @Override
    public Page<ProcRelationGoodsSnapshotDO> queryProcRelationSnapshotGoodsPage(QueryProcGoodsSnapshotPageRequest request) {
        if (ObjectUtil.isNull(request.getRelationSnapshotId()) || ObjectUtil.equal(0L, request.getRelationSnapshotId())) {
            return request.getPage();
        }
        LambdaQueryWrapper<ProcRelationGoodsSnapshotDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProcRelationGoodsSnapshotDO::getRelationSnapshotId, request.getRelationSnapshotId());
        return page(request.getPage(), wrapper);
    }

    @Override
    public List<ProcRelationGoodsSnapshotDO> queryProcGoodsSnapshotList(Long relationSnapshotId) {
        if (ObjectUtil.isNull(relationSnapshotId) || ObjectUtil.equal(0L, relationSnapshotId)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<ProcRelationGoodsSnapshotDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProcRelationGoodsSnapshotDO::getRelationSnapshotId, relationSnapshotId);
        return list(wrapper);
    }
}
