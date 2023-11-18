package com.yiling.dataflow.wash.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.UnlockCollectionDetailApi;
import com.yiling.dataflow.wash.dto.UnlockCollectionDetailDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCollectionDetailPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCollectionDetailRequest;
import com.yiling.dataflow.wash.service.UnlockCollectionDetailService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author fucheng.bai
 * @date 2023/5/15
 */
@DubboService
public class UnlockCollectionDetailApiImpl implements UnlockCollectionDetailApi {

    @Autowired
    private UnlockCollectionDetailService unlockCollectionDetailService;

    @Override
    public Page<UnlockCollectionDetailDTO> listPage(QueryUnlockCollectionDetailPageRequest request) {
        return PojoUtils.map(unlockCollectionDetailService.listPage(request), UnlockCollectionDetailDTO.class);
    }

    @Override
    public void add(SaveOrUpdateUnlockCollectionDetailRequest request) {
        unlockCollectionDetailService.add(request);
    }

    @Override
    public void update(SaveOrUpdateUnlockCollectionDetailRequest request) {
        unlockCollectionDetailService.update(request);
    }

    @Override
    public UnlockCollectionDetailDTO getById(Long id) {
        return PojoUtils.map(unlockCollectionDetailService.getById(id), UnlockCollectionDetailDTO.class);
    }

    @Override
    public UnlockCollectionDetailDTO getByCrmGoodsCodeAndRegionCode(Long crmGoodsCode, String regionCode) {
        return PojoUtils.map(unlockCollectionDetailService.getByCrmGoodsCodeAndRegionCode(crmGoodsCode, regionCode), UnlockCollectionDetailDTO.class);
    }

    @Override
    public void delete(Long id) {
        unlockCollectionDetailService.delete(id);
    }
}
