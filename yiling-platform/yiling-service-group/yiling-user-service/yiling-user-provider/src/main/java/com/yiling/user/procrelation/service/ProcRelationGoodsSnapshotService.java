package com.yiling.user.procrelation.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.procrelation.dto.ProcRelationSnapshotGoodsDTO;
import com.yiling.user.procrelation.dto.request.QueryProcGoodsSnapshotPageRequest;
import com.yiling.user.procrelation.entity.ProcRelationGoodsSnapshotDO;

/**
 * <p>
 * pop采购关系的可采商品 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-05-19
 */
public interface ProcRelationGoodsSnapshotService extends BaseService<ProcRelationGoodsSnapshotDO> {

    /**
     * 根据采购关系id查询采购关系快照下的商品
     *
     * @param request
     * @return
     */
    Page<ProcRelationGoodsSnapshotDO> queryProcRelationSnapshotGoodsPage(QueryProcGoodsSnapshotPageRequest request);

    /**
     * 查询采购关系快照的商品列表
     *
     * @param relationSnapshotId
     * @return
     */
    List<ProcRelationGoodsSnapshotDO> queryProcGoodsSnapshotList(Long relationSnapshotId);
}
