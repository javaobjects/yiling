package com.yiling.hmc.gzh.api.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.gzh.api.HmcGzhGreetingApi;
import com.yiling.hmc.gzh.dto.GzhGreetingDTO;
import com.yiling.hmc.gzh.dto.request.PublishGzhGreetingRequest;
import com.yiling.hmc.gzh.dto.request.QueryGzhGreetingRequest;
import com.yiling.hmc.gzh.dto.request.SaveGzhGreetingRequest;
import com.yiling.hmc.gzh.service.GzhGreetingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: fan.shen
 * @date: 2022-09-22
 */
@Slf4j
@DubboService
public class HmcGzhGreetingApiImpl implements HmcGzhGreetingApi {

    @Autowired
    private GzhGreetingService gzhGreetingService;

    @Override
    public Page<GzhGreetingDTO> pageList(QueryGzhGreetingRequest request) {
        return gzhGreetingService.pageList(request);
    }

    @Override
    public GzhGreetingDTO getDetailById(Long id) {
        return PojoUtils.map(gzhGreetingService.getById(id), GzhGreetingDTO.class);
    }

    @Override
    public GzhGreetingDTO getDetailBySceneId(Long sceneId) {
        return PojoUtils.map(gzhGreetingService.getDetailBySceneId(sceneId), GzhGreetingDTO.class);
    }

    @Override
    public Long saveGreetings(SaveGzhGreetingRequest request) {
        return gzhGreetingService.saveGreetings(request);
    }

    @Override
    public Boolean publishGreetings(PublishGzhGreetingRequest request) {
        return gzhGreetingService.publishGreetings(request);
    }

    @Override
    public GzhGreetingDTO checkIsExists(Integer sceneId) {
        return gzhGreetingService.checkIsExists(sceneId);
    }

    @Override
    public void updateTriggerCount(Long id) {
        gzhGreetingService.updateTriggerCount(id);
    }
}
