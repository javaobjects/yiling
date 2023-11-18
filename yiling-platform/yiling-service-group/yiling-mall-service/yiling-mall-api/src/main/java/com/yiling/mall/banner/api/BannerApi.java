package com.yiling.mall.banner.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.mall.banner.dto.B2bAppBannerDTO;
import com.yiling.mall.banner.dto.B2bAppBannerEnterpriseLimitDTO;
import com.yiling.mall.banner.dto.BannerDTO;
import com.yiling.mall.banner.dto.request.B2bAppBannerDeleteRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerPageRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerSaveRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerStatusRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerWeightRequest;
import com.yiling.mall.banner.dto.request.CheckBannerRequest;
import com.yiling.mall.banner.dto.request.QueryBannerPageListRequest;
import com.yiling.mall.banner.dto.request.SaveBannerRequest;
import com.yiling.mall.banner.dto.request.UpdateBannerRequest;

/**
 * banner Api
 *
 * @author: yuecheng.chen
 * @date: 2021/6/15
 */
public interface BannerApi {
    /**
     * 查询banner分页列表
     *
     * @param request
     * @return
     */
    Page<BannerDTO> pageList(QueryBannerPageListRequest request);

    /**
     * 获取banner明细
     *
     * @param bannerId bannerId
     * @return
     */
    BannerDTO get(Long bannerId);

    /**
     * 创建banner
     *
     * @param request
     * @return
     */
    Boolean createBanner(SaveBannerRequest request);

    /**
     * 修改banner
     *
     * @param request
     * @return
     */
    Boolean updateBanner(UpdateBannerRequest request);

    /**
     * pop首页获取banner可展示列表
     *
     * @param num 查询数量
     * @return
     */
    List<BannerDTO> getAvailableBannerList(Integer num);

    /**
     * 检查banner内容是否合法；
     *
     * @param request
     * @return true:合法； false:不通过
     */
    Boolean checkRightful(CheckBannerRequest request);

    /**
     * 修改状态
     *
     * @param id bannerId
     * @param status 启用停用状态
     * @param opUserId 操作人
     * @return
     */
    Boolean updateStatusById(Long id, Integer status, Long opUserId);


    //===================================================================================

    /**
     * 根据id查询banner信息
     *
     * @param id
     * @return
     */
    B2bAppBannerDTO queryByB2BId(Long id);

    /**
     * B2B新增和编辑banner信息
     *
     * @param request
     * @return
     */
    boolean saveB2bAppBanner(B2bAppBannerSaveRequest request);

    /**
     * B2B中banner排序
     *
     * @return
     */
    boolean editB2bAppBannerWeight(B2bAppBannerWeightRequest request);

    /**
     * B2B中banner使用状态修改
     *
     * @param request
     * @return
     */
    boolean editB2bAppBannerStatus(B2bAppBannerStatusRequest request);

    /**
     * 删除B2B中banner
     *
     * @param request
     * @return
     */
    boolean deleteB2bAppBanner(B2bAppBannerDeleteRequest request);

    /**
     * 分页列表查询B2B中banner-运营后台
     *
     * @param request
     * @return
     */
    Page<B2bAppBannerDTO> pageB2bList(B2bAppBannerPageRequest request);

    /**
     * app查询banner信息
     *
     * @param usageScenario
     * @param bannerSource
     * @param count
     * @return
     */
    List<B2bAppBannerDTO> listByScenarioAndSource(Integer usageScenario, Integer bannerSource, int count);

    /**
     * 根据banner的id和店铺id查询店铺banner信息
     *
     * @param bannerId banner表的id
     * @param eid 店铺id
     * @return 店铺banner信息
     */
    List<B2bAppBannerEnterpriseLimitDTO> listBannerEnterpriseByBannerIdAndEid(Long bannerId, Long eid);

    /**
     * app查询店铺banner信息
     *
     * @param usageScenario 使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner 3-B2B移动端会员中心Banner 4-B2B移动端店铺Banner
     * @param bannerSource 来源1-POP 2-销售助手 3-B2B
     * @param eid 企业id
     * @param count 数量
     * @return 店铺banner信息
     */
    List<B2bAppBannerDTO> listByScenarioAndSourceAndEid(Integer usageScenario, Integer bannerSource, Long eid, int count);
}
