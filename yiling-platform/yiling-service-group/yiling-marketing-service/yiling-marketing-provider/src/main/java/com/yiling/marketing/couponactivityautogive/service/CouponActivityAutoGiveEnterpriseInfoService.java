package com.yiling.marketing.couponactivityautogive.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryGiveEnterpriseInfoListRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveEnterpriseInfoDO;

/**
 * <p>
 * 自动发券企业信息表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-11-24
 */
public interface CouponActivityAutoGiveEnterpriseInfoService extends BaseService<CouponActivityAutoGiveEnterpriseInfoDO> {

    Boolean insertBatch(List<CouponActivityAutoGiveEnterpriseInfoDO> list);

    Boolean updateBatch(List<CouponActivityAutoGiveEnterpriseInfoDO> list);

    /**
     * 根据优惠券ID查询发放企业信息
     * @param couponActivityId
     * @return
     */
    List<CouponActivityAutoGiveEnterpriseInfoDO> getByCouponActivityId(Long couponActivityId);

    /**
     * 根据企业ID查询发放企业信息
     * @param eid
     * @return
     */
    List<CouponActivityAutoGiveEnterpriseInfoDO> getByEid(Long eid);

    /**
     * 查询发券企业参与记录
     * @param request
     * @return
     */
    Page<CouponActivityAutoGiveEnterpriseInfoDO> queryGiveEnterpriseInfoListPage(QueryCouponActivityGiveEnterpriseInfoRequest request);

    /**
     * 保存或更新发放企业信息
     * @param request
     * @return
     */
    Boolean saveOrUpdate(SaveCouponActivityGiveEnterpriseInfoRequest request);

    /**
     * 根据企业ID、优惠券ID查询发放企业信息
     * @param request
     * @return
     */
    List<CouponActivityAutoGiveEnterpriseInfoDO> getByEidAndCouponActivityId(QueryGiveEnterpriseInfoListRequest request);

}
