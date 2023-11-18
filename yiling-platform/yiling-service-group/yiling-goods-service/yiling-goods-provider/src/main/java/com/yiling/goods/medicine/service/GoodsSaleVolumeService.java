package com.yiling.goods.medicine.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.medicine.dto.request.BatchSaveGoodsSaleVolumeRequest;
import com.yiling.goods.medicine.entity.GoodsSaleVolumeDO;

/**
 * @author shichen
 * @类名 GoodsSaleVolumeService
 * @描述
 * @创建时间 2023/5/8
 * @修改人 shichen
 * @修改时间 2023/5/8
 **/
public interface GoodsSaleVolumeService extends BaseService<GoodsSaleVolumeDO> {

    /**
     * 日期批量保存商品销量
     * @param request
     * @return
     */
    Boolean batchSaveSaleVolume(BatchSaveGoodsSaleVolumeRequest request);
}
