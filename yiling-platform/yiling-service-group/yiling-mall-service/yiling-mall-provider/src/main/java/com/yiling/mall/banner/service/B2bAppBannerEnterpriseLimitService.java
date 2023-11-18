package com.yiling.mall.banner.service;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.banner.dto.request.B2bAppBannerEnterpriseSaveRequest;
import com.yiling.mall.banner.entity.B2bAppBannerEnterpriseLimitDO;

/**
 * <p>
 * B2B的店铺banner店铺表 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-11-10
 */
public interface B2bAppBannerEnterpriseLimitService extends BaseService<B2bAppBannerEnterpriseLimitDO> {

    /**
     * 新增 B2B的店铺banner店铺表
     *
     * @param bannerId banner表的id
     * @param bannerEnterpriseList 店铺banner店铺集合
     * @param opUserId 操作人
     * @param opTime 操作时间
     * @return 成功/失败
     */
    boolean saveBannerEnterprise(Long bannerId, List<B2bAppBannerEnterpriseSaveRequest> bannerEnterpriseList, Long opUserId, Date opTime);

    /**
     * 查询 B2B的店铺banner店铺
     *
     * @param bannerId banner表的id
     * @param eid 企业id
     * @return B2B的店铺banner店铺集合
     */
    List<B2bAppBannerEnterpriseLimitDO> listByBannerIdAndEid(Long bannerId, Long eid);

    /**
     * 删除B2B的店铺banner店铺数据
     *
     * @param bannerId banner表的id
     * @param opUserId 操作人
     * @param opTime 操作时间
     * @return 成功/失败
     */
    boolean deleteByBannerId(Long bannerId, Long opUserId, Date opTime);
}
