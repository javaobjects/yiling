package com.yiling.sales.assistant.banner.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerAppListRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerDeleteRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerPageRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerSaveRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerStatusRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerWeightRequest;
import com.yiling.sales.assistant.banner.entity.BannerDO;

/**
 * <p>
 * 销售助手banner表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-09-14
 */
public interface BannerService extends BaseService<BannerDO> {

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
    boolean editWeight(SaAppBannerWeightRequest request);

    /**
     * 销售助手的banner修改启用状态
     *
     * @param request   请求参数
     * @return  成功/失败
     */
    boolean editStatus(SaAppBannerStatusRequest request);

    /**
     * 销售助手的banner删除
     *
     * @param request   请求参数
     * @return  成功/失败
     */
    boolean deleteById(SaAppBannerDeleteRequest request);

    /**
     * 分页列表查询B2B的banner-运营后台
     *
     * @param request   请求参数
     * @return  banner集合
     */
    Page<BannerDO> pageList(SaAppBannerPageRequest request);

    /**
     * app查询banner信息
     *
     * @param request   查询请求参数
     * @return  banner集合
     */
    List<BannerDO> listByScenarioAndSource(SaAppBannerAppListRequest request);

}
