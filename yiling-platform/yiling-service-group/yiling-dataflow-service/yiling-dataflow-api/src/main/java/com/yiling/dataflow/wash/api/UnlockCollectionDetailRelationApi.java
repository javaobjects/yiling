package com.yiling.dataflow.wash.api;

import java.util.List;

import com.yiling.dataflow.wash.dto.UnlockCollectionDetailRelationDTO;

/**
 * @author fucheng.bai
 * @date 2023/5/17
 */
public interface UnlockCollectionDetailRelationApi {

    List<UnlockCollectionDetailRelationDTO> getListByCrmGoodsCode(Long crmGoodsCode);

    List<UnlockCollectionDetailRelationDTO> getListByUnlockCollectionDetailId(Long unlockCollectionDetailId);
}
