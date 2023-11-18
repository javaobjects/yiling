package com.yiling.user.procrelation.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.procrelation.dto.ProcRelationSnapshotDTO;
import com.yiling.user.procrelation.dto.ProcRelationSnapshotGoodsDTO;
import com.yiling.user.procrelation.dto.request.QueryProcGoodsSnapshotPageRequest;

import java.util.List;

/**
 * @author: dexi.yao
 * @date: 2023-05-19
 */
public interface ProcurementRelationSnapshotApi {

    /**
     * 根据采购关系id查询采购关系快照
     *
     * @param relationId
     * @return
     */
    List<ProcRelationSnapshotDTO> queryProcRelationSnapshot(Long relationId);

    /**
     * 根据采购关系快照版本id查询快照信息
     *
     * @param versionId
     * @return
     */
    ProcRelationSnapshotDTO queryProcRelationSnapshotByVersionId(String versionId);

    /**
     * 分页查询采购关系快照的商品列表
     *
     * @param request
     * @return
     */
    Page<ProcRelationSnapshotGoodsDTO> queryProcGoodsSnapshotPage(QueryProcGoodsSnapshotPageRequest request);

    /**
     * 查询采购关系快照的商品列表
     *
     * @param relationSnapshotId
     * @return
     */
    List<ProcRelationSnapshotGoodsDTO> queryProcGoodsSnapshotList(Long relationSnapshotId);

}
