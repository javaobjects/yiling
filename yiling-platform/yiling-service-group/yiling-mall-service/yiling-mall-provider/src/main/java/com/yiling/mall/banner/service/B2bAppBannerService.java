package com.yiling.mall.banner.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.banner.dto.B2bAppBannerDTO;
import com.yiling.mall.banner.dto.request.B2bAppBannerPageRequest;
import com.yiling.mall.banner.dto.request.B2bAppBannerSaveRequest;
import com.yiling.mall.banner.entity.B2bAppBannerDO;

/**
 * <p>
 * B2B的banner表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-22
 */
public interface B2bAppBannerService extends BaseService<B2bAppBannerDO> {

    /**
     * B2B新增和编辑banner信息
     *
     * @param request
     * @return
     */
    boolean saveB2bAppBanner(B2bAppBannerSaveRequest request);

    /**
     * B2B的banner排序，权重数字修改
     *
     * @param id
     * @param sort
     * @param currentUserId
     * @return
     */
    boolean editWeight(Long id, Integer sort, Long currentUserId);

    /**
     * B2B的banner修改启用状态
     *
     * @param id
     * @param bannerStatus 状态：1-启用 2-停用
     * @param currentUserId
     * @return
     */
    boolean editStatus(Long id, Integer bannerStatus, Long currentUserId);

    /**
     * B2B的banner删除
     *
     * @param id
     * @param currentUserId
     * @return
     */
    boolean deleteById(Long id, Long currentUserId);

    /**
     * 分页列表查询B2B的banner-运营后台
     *
     * @param request
     * @return
     */
    Page<B2bAppBannerDTO> pageList(B2bAppBannerPageRequest request);

    /**
     * app查询banner信息
     *
     * @param usageScenario
     * @param bannerSource
     * @param count
     * @return
     */
    List<B2bAppBannerDO> listByScenarioAndSource(Integer usageScenario, Integer bannerSource, int count);

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
