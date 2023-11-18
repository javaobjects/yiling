package com.yiling.user.procrelation.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.procrelation.entity.ProcRelationSnapshotDO;

/**
 * <p>
 * pop采购关系表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-05-19
 */
public interface ProcRelationSnapshotService extends BaseService<ProcRelationSnapshotDO> {

    /**
     * 根据版本id查询pop采购关系快照
     *
     * @param versionId
     * @return
     */
    ProcRelationSnapshotDO querySnapshotByVersionId(String versionId);

    /**
     * 根据采购关系id查询采购关系快照
     *
     * @param relationId
     * @return
     */
    List<ProcRelationSnapshotDO> queryProcRelationSnapshot(Long relationId);
}
