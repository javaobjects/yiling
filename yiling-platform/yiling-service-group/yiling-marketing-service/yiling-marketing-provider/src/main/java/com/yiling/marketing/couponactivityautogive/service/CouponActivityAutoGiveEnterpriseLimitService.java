package com.yiling.marketing.couponactivityautogive.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveEnterpriseLimitDO;

/**
 * <p>
 * 自动发券活动会员企业限制表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponActivityAutoGiveEnterpriseLimitService extends BaseService<CouponActivityAutoGiveEnterpriseLimitDO> {

    /**
     * 查询企业信息列表
     * @param request
     * @return
     */
//    Page<CouponActivityAutoGiveMemberDTO> queryEnterpriseListPage(QueryCouponActivityAutoGiveMemberRequest request);

    /**
     * 查询企业信息列表
     * @param request
     * @return
     */
    Page<CouponActivityAutoGiveEnterpriseLimitDO> queryEnterpriseLimitListPage(QueryCouponActivityAutoGiveMemberLimitRequest request);

    /**
     * 添加企业信息
     * @param request
     * @return
     */
    Boolean saveEnterpriseLimit(SaveCouponActivityAutoGiveMemberLimitRequest request);

    /**
     * 删除已添加会员企业
     * @param request
     * @return
     */
    Boolean deleteEnterpriseLimit(DeleteCouponActivityAutoGiveMemberLimitRequest request);

    /**
     * 根据自动发放优惠券id查询企业限制表
     * @param id
     * @return
     */
    List<CouponActivityAutoGiveEnterpriseLimitDO> getByAutoGiveId(Long id);

    /**
     * 根据自动发放活动ids查询指定会员
     * @param autoGiveIdList
     * @return
     */
    List<CouponActivityAutoGiveEnterpriseLimitDO> getEnterpriseLimitByAutoGiveIdList(List<Long> autoGiveIdList);
}
