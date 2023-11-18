package com.yiling.mall.navigation.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.mall.navigation.api.NavigationInfoApi;
import com.yiling.mall.navigation.dto.NavigationInfoDTO;
import com.yiling.mall.navigation.dto.NavigationInfoFrontDTO;
import com.yiling.mall.navigation.dto.request.QueryNavigationInfoPageRequest;
import com.yiling.mall.navigation.dto.request.SaveNavigationInfoRequest;
import com.yiling.mall.navigation.dto.request.UpdateNavigationInfoRequest;
import com.yiling.mall.navigation.service.NavigationInfoService;

/**
 * @author:wei.wang
 * @date:2021/6/15
 */
@DubboService
public class NavigationInfoApiImpl implements NavigationInfoApi {

    @Autowired
    NavigationInfoService navigationInfoService;

    /**
     * 获取导航分页信息
     *
     * @param request
     * @return
     */
    @Override
    public Page<NavigationInfoDTO> getNavigationInfo(QueryNavigationInfoPageRequest request) {
        Page<NavigationInfoDTO> page = navigationInfoService.getNavigationInfo(request);
        return page;
    }

    /**
     * 保存导航信息
     *
     * @param request
     * @return
     */
    @Override
    public Boolean saveNavigationInfo(SaveNavigationInfoRequest request) {
        return navigationInfoService.saveNavigationInfo(request);
    }

    /**
     * 修改导航信息
     *
     * @param request
     * @return
     */
    @Override
    public Boolean updateNavigationInfo(UpdateNavigationInfoRequest request) {
        return navigationInfoService.updateNavigationInfo(request);
    }

    /**
     * POP首页获取导航信息
     *
     * @param number
     * @return
     */
    @Override
    public List<NavigationInfoFrontDTO> getNavigationInfoFront(Integer number) {
        return navigationInfoService.getNavigationInfoFront(number);
    }
}
