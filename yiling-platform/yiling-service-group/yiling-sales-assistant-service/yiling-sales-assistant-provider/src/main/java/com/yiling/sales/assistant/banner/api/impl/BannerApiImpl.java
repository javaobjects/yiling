package com.yiling.sales.assistant.banner.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.banner.api.BannerApi;
import com.yiling.sales.assistant.banner.dto.BannerDTO;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerAppListRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerDeleteRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerPageRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerSaveRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerStatusRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerWeightRequest;
import com.yiling.sales.assistant.banner.entity.BannerDO;
import com.yiling.sales.assistant.banner.service.BannerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * banner的API实现类
 *
 * @author: yong.zhang
 * @date: 2021/9/17
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BannerApiImpl implements BannerApi {

    private final BannerService saBannerService;

    @Override
    public BannerDTO queryById(Long id) {
        BannerDO bannerDO = saBannerService.getById(id);
        return PojoUtils.map(bannerDO, BannerDTO.class);
    }

    @Override
    public boolean saveSaAppBanner(SaAppBannerSaveRequest request) {
        log.info("saveSaAppBanner, request:[{}]", request);
        return saBannerService.saveSaAppBanner(request);
    }

    @Override
    public boolean editSaAppBannerWeight(SaAppBannerWeightRequest request) {
        return saBannerService.editWeight(request);
    }

    @Override
    public boolean editSaAppBannerStatus(SaAppBannerStatusRequest request) {
        return saBannerService.editStatus(request);
    }

    @Override
    public boolean deleteSaAppBanner(SaAppBannerDeleteRequest request) {
        return saBannerService.deleteById(request);
    }

    @Override
    public Page<BannerDTO> pageSaList(SaAppBannerPageRequest request) {
        Page<BannerDO> pageList = saBannerService.pageList(request);
        return PojoUtils.map(pageList, BannerDTO.class);
    }

    @Override
    public List<BannerDTO> listByScenarioAndSource(SaAppBannerAppListRequest request) {
        List<BannerDO> bannerDOList = saBannerService.listByScenarioAndSource(request);
        return PojoUtils.map(bannerDOList, BannerDTO.class);
    }
}
