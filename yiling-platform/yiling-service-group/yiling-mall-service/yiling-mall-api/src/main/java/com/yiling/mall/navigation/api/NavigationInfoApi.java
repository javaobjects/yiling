package com.yiling.mall.navigation.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.mall.navigation.dto.NavigationInfoDTO;
import com.yiling.mall.navigation.dto.NavigationInfoFrontDTO;
import com.yiling.mall.navigation.dto.request.QueryNavigationInfoPageRequest;
import com.yiling.mall.navigation.dto.request.SaveNavigationInfoRequest;
import com.yiling.mall.navigation.dto.request.UpdateNavigationInfoRequest;

/**
 * @author:wei.wang
 * @date:2021/6/15
 */
public interface NavigationInfoApi {
    /**
     * 获取导航分页信息
     * @param request
     * @return
     */
    Page<NavigationInfoDTO> getNavigationInfo(QueryNavigationInfoPageRequest request);

    /**
     * 保存导航信息
     * @param request
     * @return
     */
    Boolean saveNavigationInfo(SaveNavigationInfoRequest request);

    /**
     * 修改导航信息
     * @param request
     * @return
     */
    Boolean updateNavigationInfo(UpdateNavigationInfoRequest request);

    /**
     * POP首页获取导航信息
     * @param number
     * @return
     */
    List<NavigationInfoFrontDTO> getNavigationInfoFront(Integer number);
}
