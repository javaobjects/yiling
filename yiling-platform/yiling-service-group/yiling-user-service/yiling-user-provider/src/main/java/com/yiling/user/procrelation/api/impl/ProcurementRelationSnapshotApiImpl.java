package com.yiling.user.procrelation.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.procrelation.api.ProcurementRelationSnapshotApi;
import com.yiling.user.procrelation.dto.ProcRelationSnapshotDTO;
import com.yiling.user.procrelation.dto.ProcRelationSnapshotGoodsDTO;
import com.yiling.user.procrelation.dto.request.QueryProcGoodsSnapshotPageRequest;
import com.yiling.user.procrelation.entity.ProcRelationGoodsSnapshotDO;
import com.yiling.user.procrelation.entity.ProcRelationSnapshotDO;
import com.yiling.user.procrelation.service.ProcRelationGoodsSnapshotService;
import com.yiling.user.procrelation.service.ProcRelationSnapshotService;

/**
 * @author: dexi.yao
 * @date: 2023-05-23
 */
@DubboService
public class ProcurementRelationSnapshotApiImpl implements ProcurementRelationSnapshotApi {
    @Autowired
    ProcRelationSnapshotService procRelationSnapshotService;
    @Autowired
    ProcRelationGoodsSnapshotService goodsSnapshotService;

    @Override
    public List<ProcRelationSnapshotDTO> queryProcRelationSnapshot(Long relationId) {
        List<ProcRelationSnapshotDO> snapshotDOS = procRelationSnapshotService.queryProcRelationSnapshot(relationId);
        return PojoUtils.map(snapshotDOS, ProcRelationSnapshotDTO.class);
    }

    @Override
    public ProcRelationSnapshotDTO queryProcRelationSnapshotByVersionId(String versionId) {
        ProcRelationSnapshotDO snapshotDO = procRelationSnapshotService.querySnapshotByVersionId(versionId);
        return PojoUtils.map(snapshotDO, ProcRelationSnapshotDTO.class);
    }

    @Override
    public Page<ProcRelationSnapshotGoodsDTO> queryProcGoodsSnapshotPage(QueryProcGoodsSnapshotPageRequest request) {
        Page<ProcRelationGoodsSnapshotDO> snapshotDOS = goodsSnapshotService.queryProcRelationSnapshotGoodsPage(request);
        return PojoUtils.map(snapshotDOS, ProcRelationSnapshotGoodsDTO.class);
    }

    @Override
    public List<ProcRelationSnapshotGoodsDTO> queryProcGoodsSnapshotList(Long relationSnapshotId) {
        List<ProcRelationGoodsSnapshotDO> list = goodsSnapshotService.queryProcGoodsSnapshotList(relationSnapshotId);
        return PojoUtils.map(list,ProcRelationSnapshotGoodsDTO.class);
    }
}
