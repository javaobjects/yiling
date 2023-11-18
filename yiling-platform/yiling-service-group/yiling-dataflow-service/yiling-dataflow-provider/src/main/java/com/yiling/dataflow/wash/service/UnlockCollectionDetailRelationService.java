package com.yiling.dataflow.wash.service;

import java.util.List;

import com.yiling.dataflow.wash.entity.UnlockCollectionDetailRelationDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 采集明细 服务类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-17
 */
public interface UnlockCollectionDetailRelationService extends BaseService<UnlockCollectionDetailRelationDO> {

    List<UnlockCollectionDetailRelationDO> getListByUnlockCollectionDetailId(Long unlockCollectionDetailId);

    void deleteByUnlockCollectionDetailId(Long unlockCollectionDetailId);

    List<UnlockCollectionDetailRelationDO> getListByCrmGoodCode(Long  crmGoodsCode);
}
