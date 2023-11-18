package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.wash.api.UnlockCollectionDetailRelationApi;
import com.yiling.dataflow.wash.dto.UnlockCollectionDetailRelationDTO;
import com.yiling.dataflow.wash.service.UnlockCollectionDetailRelationService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author fucheng.bai
 * @date 2023/5/17
 */
@DubboService
public class UnlockCollectionDetailRelationApiImpl implements UnlockCollectionDetailRelationApi {

    @Autowired
    private UnlockCollectionDetailRelationService unlockCollectionDetailRelationService;

    @Override
    public List<UnlockCollectionDetailRelationDTO> getListByCrmGoodsCode(Long crmGoodsCode) {
        return PojoUtils.map(unlockCollectionDetailRelationService.getListByCrmGoodCode(crmGoodsCode), UnlockCollectionDetailRelationDTO.class);
    }

    @Override
    public List<UnlockCollectionDetailRelationDTO> getListByUnlockCollectionDetailId(Long unlockCollectionDetailId) {
        return PojoUtils.map(unlockCollectionDetailRelationService.getListByUnlockCollectionDetailId(unlockCollectionDetailId), UnlockCollectionDetailRelationDTO.class);
    }
}
