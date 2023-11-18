package com.yiling.hmc.common.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.common.api.AdvertisementApi;
import com.yiling.hmc.common.dto.AdvertisementDTO;
import com.yiling.hmc.common.dto.request.AdvertisementDeleteRequest;
import com.yiling.hmc.common.dto.request.AdvertisementListRequest;
import com.yiling.hmc.common.dto.request.AdvertisementPageRequest;
import com.yiling.hmc.common.dto.request.AdvertisementSaveRequest;
import com.yiling.hmc.common.entity.AdvertisementDO;
import com.yiling.hmc.common.service.AdvertisementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/3/23
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdvertisementApiImpl implements AdvertisementApi {

    private final AdvertisementService advertisementService;

    @Override
    public AdvertisementDTO queryById(Long id) {
        AdvertisementDO advertisementDO = advertisementService.getById(id);
        return PojoUtils.map(advertisementDO, AdvertisementDTO.class);
    }

    @Override
    public boolean saveAdvertisement(AdvertisementSaveRequest request) {
        return advertisementService.saveAdvertisement(request);
    }

    @Override
    public boolean deleteAdvertisement(AdvertisementDeleteRequest request) {
        return advertisementService.deleteAdvertisement(request);
    }

    @Override
    public Page<AdvertisementDTO> pageList(AdvertisementPageRequest request) {
        Page<AdvertisementDO> doPage = advertisementService.pageList(request);
        return PojoUtils.map(doPage, AdvertisementDTO.class);
    }

    @Override
    public List<AdvertisementDTO> listByCondition(AdvertisementListRequest request) {
        List<AdvertisementDO> advertisementList = advertisementService.listByCondition(request);
        return PojoUtils.map(advertisementList, AdvertisementDTO.class);
    }
}
