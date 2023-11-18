package com.yiling.goods.medicine.api;

import com.yiling.goods.medicine.dto.request.BatchSaveGoodsSaleVolumeRequest;

/**
 * @author shichen
 * @类名 GoodsSaleVolumeApi
 * @描述
 * @创建时间 2023/5/10
 * @修改人 shichen
 * @修改时间 2023/5/10
 **/
public interface GoodsSaleVolumeApi {

    /**
     * 日期批量保存商品销量
     * @param request
     * @return
     */
    Boolean batchSaveSaleVolume(BatchSaveGoodsSaleVolumeRequest request);
}
