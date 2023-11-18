package com.yiling.marketing.couponactivityautoget.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetEnterpriseLimitDO;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetPromotionLimitDO;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGetMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGetMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGetMemberLimitRequest;

/**
 * <p>
 * 自主领券活动会员企业限制表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponActivityAutoGetEnterpriseLimitService extends BaseService<CouponActivityAutoGetEnterpriseLimitDO> {

    /**
     * 根据自主领券活动id查询会员企业限制表
     * @param autoGetId
     * @return
     */
    List<CouponActivityAutoGetEnterpriseLimitDO> getByAutoGetId(Long autoGetId);

    /**
     * 查询会员信息列表
     * @param request
     * @return
     */
    Page<CouponActivityAutoGetEnterpriseLimitDO> queryEnterpriseLimitListPage(QueryCouponActivityAutoGetMemberLimitRequest request);

    /**
     * 添加企业信息
     * @param request
     * @return
     */
    Boolean saveEnterpriseLimit(SaveCouponActivityAutoGetMemberLimitRequest request);

    /**
     * 添加企业信息
     * @param request
     * @return
     */
    Boolean savePromotionEnterpriseLimit(SaveCouponActivityAutoGetMemberLimitRequest request);

    /**
     * 删除已添加会员企业
     * @param request
     * @return
     */
    Boolean deleteEnterpriseLimit(DeleteCouponActivityAutoGetMemberLimitRequest request);

    /**
     * 删除已添加会员企业
     * @param request
     * @return
     */
    Boolean deletePromotionEnterpriseLimit(DeleteCouponActivityAutoGetMemberLimitRequest request);

    /**
     * 查询推广方企业信息
     * @param request
     * @return
     */
    Page<CouponActivityAutoGetPromotionLimitDO> queryPromotionListPage(QueryCouponActivityAutoGetMemberLimitRequest request);
}
