package com.yiling.marketing.couponactivityautoget.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivityautoget.dto.CouponActivityAutoGetDTO;
import com.yiling.marketing.couponactivityautoget.dto.CouponActivityAutoGetDetailDTO;
import com.yiling.marketing.couponactivityautoget.dto.request.CouponActivityAutoGetOperateRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.QueryCouponActivityAutoGetRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.SaveCouponActivityAutoGetBasicRequest;
import com.yiling.marketing.couponactivityautoget.dto.request.SaveCouponActivityAutoGetRulesRequest;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetDO;

/**
 * <p>
 * 自主领券活动表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponActivityAutoGetService extends BaseService<CouponActivityAutoGetDO> {

    /**
     * 自主领券活动分页
     * @param request
     * @return
     */
    Page<CouponActivityAutoGetDO> queryListPage(QueryCouponActivityAutoGetRequest request);

    /**
     * 详情
     * @param id
     * @return
     */
    CouponActivityAutoGetDetailDTO getDetailById(Long id);

    /**
     * 保存/修改自主领券基本信息
     * @param request
     * @return
     */
    Long saveOrUpdateBasic(SaveCouponActivityAutoGetBasicRequest request);

    /**
     * 保存/修改自主领券基本信息
     * @param request
     * @return
     */
    Long saveOrUpdateRules(SaveCouponActivityAutoGetRulesRequest request);

    /**
     * 根据ID停用自主领券活动
     * @param request
     * @return
     */
    Boolean stop(CouponActivityAutoGetOperateRequest request);

    /**
     * 根据ID启用自主领券活动
     * @param request
     * @return
     */
    Boolean enable(CouponActivityAutoGetOperateRequest request);

    /**
     * 根据ID废弃自主领券活动
     * @param request
     * @return
     */
    Boolean scrap(CouponActivityAutoGetOperateRequest request);

    /**
     * 根据自主领券活动ID查询
     * @param id
     * @return
     */
    CouponActivityAutoGetDTO getAutoGetById(Long id);

    /**
     * 根据ID查列表询 自主领券活动列表
     * @param idList
     * @return
     */
    List<CouponActivityAutoGetDTO> getAutoGetByIdList(List<Long> idList);

    /**
     * 根据优惠券id、企业id 校验领取优惠券
     * @param couponActivity
     * @param userId
     * @param eid
     * @param etype
     * @param currentMember
     * @param autoGetIdList
     * @return
     */
    String couponActivityAutoGetCheck(CouponActivityDetailDTO couponActivity, Long userId, Long eid, Integer etype, Integer currentMember, List<Long> autoGetIdList);

    /**
     * 根据ID查列表询 自主领券活动列表
     * @param idList
     * @return
     */
    List<CouponActivityAutoGetDTO> getByIdList(List<Long> idList);
}
