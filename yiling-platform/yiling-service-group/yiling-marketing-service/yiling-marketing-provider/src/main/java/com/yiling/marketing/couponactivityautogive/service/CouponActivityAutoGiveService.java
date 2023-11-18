package com.yiling.marketing.couponactivityautogive.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDetailDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.CouponActivityAutoGiveOperateRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityGiveDetailRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveBasicRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveRulesRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveCountRequest;
import com.yiling.marketing.couponactivityautogive.entity.CouponActivityAutoGiveDO;

/**
 * <p>
 * 自动发券活动表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponActivityAutoGiveService extends BaseService<CouponActivityAutoGiveDO> {

    /**
     * 自动发放优惠券活动分页
     * @param request
     * @return
     */
    Page<CouponActivityAutoGiveDO> queryListPage(QueryCouponActivityAutoGiveRequest request);

    /**
     * 保存/修改自动发券基本信息
     * @param request
     * @return
     */
    Long saveOrUpdateBasic(SaveCouponActivityAutoGiveBasicRequest request);

    /**
     * 保存/修改自动发券基本信息
     * @param request
     * @return
     */
    Long saveOrUpdateRules(SaveCouponActivityAutoGiveRulesRequest request);

    /**
     * 详情
     * @param id
     * @return
     */
    CouponActivityAutoGiveDetailDTO getDetailById(Long id);

    /**
     * 根据ID停用自动发券活动
     * @param request
     * @return
     */
    Boolean stop(CouponActivityAutoGiveOperateRequest request);

    /**
     * 根据ID启用自动发券活动
     * @param request
     * @return
     */
    Boolean enable(CouponActivityAutoGiveOperateRequest request);

    /**
     * 根据ID废弃自动发券活动
     * @param request
     * @return
     */
    Boolean scrap(CouponActivityAutoGiveOperateRequest request);

    /**
     * 根据ID查询 优惠券自动发放活动
     * @param id
     * @return
     */
    CouponActivityAutoGiveDTO getAutoGiveById(Long id);

    /**
     * 根据ID列表查询 优惠券自动发放活动列表
     * @param idList
     * @return
     */
    List<CouponActivityAutoGiveDTO> getAutoGiveByIdList(List<Long> idList);

    /**
     * 根据条件查询优惠券自动发放活动列表
     * @param request
     * @return
     */
    List<CouponActivityAutoGiveDetailDTO> getAllByCondition(QueryCouponActivityGiveDetailRequest request);

    /**
     * 根据id更新已发放次数
     * @param request
     * @return
     */
    Boolean updateGiveCountByIdList(UpdateAutoGiveCountRequest request);


}
