package com.yiling.dataflow.wash.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.UnlockCollectionDetailDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCollectionDetailPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCollectionDetailRequest;

/**
 * @author fucheng.bai
 * @date 2023/5/15
 */
public interface UnlockCollectionDetailApi {

    Page<UnlockCollectionDetailDTO> listPage(QueryUnlockCollectionDetailPageRequest request);

    void add(SaveOrUpdateUnlockCollectionDetailRequest request);

    void update(SaveOrUpdateUnlockCollectionDetailRequest request);

    UnlockCollectionDetailDTO getById(Long id);

    UnlockCollectionDetailDTO getByCrmGoodsCodeAndRegionCode(Long crmGoodsCode, String regionCode);

    void delete(Long id);
}
