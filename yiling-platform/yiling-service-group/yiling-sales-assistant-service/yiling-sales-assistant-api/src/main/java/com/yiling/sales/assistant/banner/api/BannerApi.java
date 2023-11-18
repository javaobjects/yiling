package com.yiling.sales.assistant.banner.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sales.assistant.banner.dto.BannerDTO;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerAppListRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerDeleteRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerPageRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerSaveRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerStatusRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerWeightRequest;

/**
 * @author: yong.zhang
 * @date: 2021/9/17
 */
public interface BannerApi {

    /**
     * 通过id查询banner信息
     * 
     * @param id    banner数据的id
     * @return  banner信息
     */
    BannerDTO queryById(Long id);

    /**
     * 销售助手新增和编辑banner信息
     *
     * @param request   新增编辑请求参数
     * @return  成功/失败
     */
    boolean saveSaAppBanner(SaAppBannerSaveRequest request);

    /**
     * 销售助手的banner排序，权重数字修改
     *
     * @param request   请求参数
     * @return  成功/失败
     */
    boolean editSaAppBannerWeight(SaAppBannerWeightRequest request);

    /**
     * 销售助手的banner修改启用状态
     *
     * @param request   请求参数
     * @return  成功/失败
     */
    boolean editSaAppBannerStatus(SaAppBannerStatusRequest request);

    /**
     * 销售助手的banner删除
     *
     * @param request   请求参数
     * @return  成功/失败
     */
    boolean deleteSaAppBanner(SaAppBannerDeleteRequest request);

    /**
     * 分页列表查询B2B的banner-运营后台
     *
     * @param request   请求参数
     * @return  banner集合
     */
    Page<BannerDTO> pageSaList(SaAppBannerPageRequest request);

    /**
     * app查询banner信息
     *
     * @param request   查询请求参数
     * @return  banner集合
     */
    List<BannerDTO> listByScenarioAndSource(SaAppBannerAppListRequest request);
}
