package com.yiling.user.procrelation.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.procrelation.dao.ProcRelationSnapshotMapper;
import com.yiling.user.procrelation.entity.ProcRelationSnapshotDO;
import com.yiling.user.procrelation.enums.PorcRelationSnapshotTypeEnum;
import com.yiling.user.procrelation.service.ProcRelationSnapshotService;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * pop采购关系表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-05-19
 */
@Service
public class ProcRelationSnapshotServiceImpl extends BaseServiceImpl<ProcRelationSnapshotMapper, ProcRelationSnapshotDO> implements ProcRelationSnapshotService {

    @Override
    public ProcRelationSnapshotDO querySnapshotByVersionId(String versionId) {
        if (StrUtil.isBlank(versionId)) {
            return null;
        }
        LambdaQueryWrapper<ProcRelationSnapshotDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProcRelationSnapshotDO::getVersionId, versionId);
        return getOne(wrapper);
    }

    @Override
    public List<ProcRelationSnapshotDO> queryProcRelationSnapshot(Long relationId) {
        if (ObjectUtil.isNull(relationId) || ObjectUtil.equal(0L, relationId)) {
            return null;
        }
        LambdaQueryWrapper<ProcRelationSnapshotDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ProcRelationSnapshotDO::getRelationId, relationId);
        wrapper.eq(ProcRelationSnapshotDO::getSnapshotType, PorcRelationSnapshotTypeEnum.HISTORY.getCode());

        return list(wrapper);
    }
}
