package com.yiling.mall.openposition.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.openposition.api.OpenPositionApi;
import com.yiling.mall.openposition.dto.OpenPositionDTO;
import com.yiling.mall.openposition.dto.request.QueryOpenPositionPageRequest;
import com.yiling.mall.openposition.dto.request.SaveOpenPositionRequest;
import com.yiling.mall.openposition.dto.request.UpdateOpenPositionStatusRequest;
import com.yiling.mall.openposition.service.OpenPositionService;

import lombok.extern.slf4j.Slf4j;

/**
 * B2B-开屏位API 实现
 *
 * @author: lun.yu
 * @date: 2023-05-06
 */
@Slf4j
@DubboService
public class OpenPositionApiImpl implements OpenPositionApi {

    @Autowired
    private OpenPositionService openPositionService;

    @Override
    public Page<OpenPositionDTO> queryListPage(QueryOpenPositionPageRequest request) {
        return openPositionService.queryListPage(request);
    }

    @Override
    public boolean saveOpenPosition(SaveOpenPositionRequest request) {
        return openPositionService.saveOpenPosition(request);
    }

    @Override
    public boolean deleteOpenPosition(Long id, Long opUserId) {
        return openPositionService.deleteOpenPosition(id, opUserId);
    }

    @Override
    public boolean updateStatus(UpdateOpenPositionStatusRequest request) {
        return openPositionService.updateStatus(request);
    }

    @Override
    public OpenPositionDTO getById(Long id) {
        return PojoUtils.map(openPositionService.getById(id), OpenPositionDTO.class);
    }

    @Override
    public OpenPositionDTO getOpenPositionPicture(Integer platform) {
        return openPositionService.getOpenPositionPicture(platform);
    }
}
