package com.yiling.mall.banner.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.banner.dto.request.CheckBannerRequest;
import com.yiling.mall.banner.dto.request.QueryBannerPageListRequest;
import com.yiling.mall.banner.dto.request.SaveBannerRequest;
import com.yiling.mall.banner.dto.request.UpdateBannerRequest;
import com.yiling.mall.banner.entity.BannerDO;

/**
 * <p>
 * banner表 服务类
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
public interface BannerService extends BaseService<BannerDO> {

    /**
     * 查询banner分页列表
     *
     * @param request
     * @return
     */
    Page<BannerDO> pageList(QueryBannerPageListRequest request);

    /**
     * 获取banner信息
     * @param bannerId  bannerId
     * @return
     */
    BannerDO get(Long bannerId);

    /**
     * 创建banner
     *
     * @param request
     * @return
     */
    Boolean addBanner(SaveBannerRequest request);

    /**
     * 修改banner
     *
     * @param request
     * @return
     */
    Boolean updateBanner(UpdateBannerRequest request);

    /**
     * 查询banner集合
     * @param num
     * @return
     */
    List<BannerDO> queryAvailableBannerList(Integer num);

    /**
     * 检查banner内容是否合法；
     * @param request
     * @return  true:合法； false:不通过
     */
    Boolean checkRightful(CheckBannerRequest request);

    /**
     * 修改状态
     * @param id bannerId
     * @param status 启用停用状态
     * @param opUserId 操作人
     * @return
     */
    Boolean updateStatusById(Long id,Integer status,Long opUserId);
}
