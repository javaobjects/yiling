package com.yiling.dataflow.wash.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.UnlockCollectionDetailDTO;
import com.yiling.dataflow.wash.dto.request.QueryUnlockCollectionDetailPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveOrUpdateUnlockCollectionDetailRequest;
import com.yiling.dataflow.wash.entity.UnlockCollectionDetailDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 采集明细 服务类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-05-11
 */
public interface UnlockCollectionDetailService extends BaseService<UnlockCollectionDetailDO> {

    Page<UnlockCollectionDetailDO> listPage(QueryUnlockCollectionDetailPageRequest request);

    void add(SaveOrUpdateUnlockCollectionDetailRequest request);

    void update(SaveOrUpdateUnlockCollectionDetailRequest request);

    UnlockCollectionDetailDO getById(Long id);

    List<UnlockCollectionDetailDO> getListByCrmGoodsCode(Long crmGoodsCode);

    UnlockCollectionDetailDO getByCrmGoodsCodeAndRegionCode(Long crmGoodsCode, String regionCode);

    void delete(Long id);
}
