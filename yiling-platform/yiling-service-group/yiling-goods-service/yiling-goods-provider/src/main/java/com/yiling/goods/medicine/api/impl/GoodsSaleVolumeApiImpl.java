package com.yiling.goods.medicine.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.goods.medicine.api.GoodsSaleVolumeApi;
import com.yiling.goods.medicine.dto.request.BatchSaveGoodsSaleVolumeRequest;
import com.yiling.goods.medicine.service.GoodsSaleVolumeService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 GoodsSaleVolumeApiImpl
 * @描述
 * @创建时间 2023/5/10
 * @修改人 shichen
 * @修改时间 2023/5/10
 **/
@Slf4j
@DubboService
public class GoodsSaleVolumeApiImpl implements GoodsSaleVolumeApi {
    @Autowired
    private GoodsSaleVolumeService goodsSaleVolumeService;

    @Override
    public Boolean batchSaveSaleVolume(BatchSaveGoodsSaleVolumeRequest request) {
        return goodsSaleVolumeService.batchSaveSaleVolume(request);
    }
}
