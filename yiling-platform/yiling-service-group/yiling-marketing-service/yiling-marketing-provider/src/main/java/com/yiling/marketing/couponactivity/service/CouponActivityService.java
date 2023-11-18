package com.yiling.marketing.couponactivity.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.coupon.entity.CouponDO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityHasGetDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityResidueCountDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityIncreaseRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityOperateRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityBasicRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityRulesRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;

/**
 * <p>
 * 优惠券活动表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface CouponActivityService extends BaseService<CouponActivityDO> {

    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<CouponActivityDTO> queryListPage(QueryCouponActivityRequest request);

    /**
     * 获取优惠券活动详情
     * @param request
     * @return
     */
    CouponActivityDetailDTO getCouponActivityById(QueryCouponActivityDetailRequest request);

    /**
     * 根据ID复制优惠券活动
     * @param request
     * @return
     */
    CouponActivityDetailDTO copy(CouponActivityOperateRequest request);

    /**
     * 根据ID停用优惠券活动
     * @param request
     * @return
     */
    Boolean stop(CouponActivityOperateRequest request);

    /**
     * 根据ID作废优惠券活动
     * @param request
     * @return
     */
    Boolean scrap(CouponActivityOperateRequest request);

    List<Long> scrapAndReturnCoupon(CouponActivityOperateRequest request);

    /**
     * 根据ID增加优惠券数量
     * @param request
     * @return
     */
    Boolean increase(CouponActivityIncreaseRequest request);

    /**
     * 保存优惠券活动基本信息
     * @param request SaveCouponActivityBasicRequest
     * @return 优惠券活动id
     */
    Long saveOrUpdateBasic(SaveCouponActivityBasicRequest request);

    /**
     * 保存优惠券活动基本信息
     * @param request SaveCouponActivityBasicRequest
     * @return 优惠券活动id
     */
    Long saveOrUpdateBasicForMember(SaveCouponActivityBasicRequest request);

    /**
     * 保存优惠券活动使用规则
     * @param request SaveCouponActivityBasicRequest
     * @return 优惠券活动id
     */
    Long saveOrUpdateRules(SaveCouponActivityRulesRequest request);

    /**
     * 已发放/已领取优惠券分页列表
     * @param request
     * @return
     */
    Page<CouponActivityGiveDTO> queryGiveListPage(QueryCouponActivityGivePageRequest request);

    /**
     * 已发放/已领取优惠券分页列表
     * @param request
     * @return
     */
    List<CouponActivityDTO> getAvailableActivity(List<Long> request);

    /**
     * 已使用优惠券分页列表
     * @param request
     * @return
     */
//    Page<CouponActivityUsedDTO> queryUsedListPage(QueryCouponActivityDetailRequest request);

    /**
     * 根据ids获取优惠券活动详情列表
     * @param ids
     * @return
     */
    List<CouponActivityDetailDTO> getCouponActivityById(List<Long> ids);

    /**
     * 根据ids获取有效的优惠券活动详情列表
     * @param ids
     * @param totalCountFlag 优惠券总数量达到标识：0-未达到，可发放/领取 1-已达到，不可再发放/领取
     * @param statusFlag 优惠券状态标识：0-启用 1-启用、停用
     * @return
     */
    List<CouponActivityDO> getEffectiveCouponActivityByIdList(List<Long> ids, int totalCountFlag, int statusFlag);

    /**
     * 根据优惠券活动ID获取优惠券活动剩余数量、企业限制类型
     *
     * @param couponActivityId
     */
    CouponActivityResidueCountDTO getResidueCount(Long couponActivityId);

    /**
     * 查询未限制企业的优惠券活动列表
     * @return
     */
    List<CouponActivityDO> getEffectiveListWithoutEnterpriseLimit();

    /**
     * 组装获取优惠规则的请求信息
     * @param couponActivityDetail
     * @return
     */
    CouponActivityDTO buildCouponActivityDtoForCouponRules(CouponActivityDetailDTO couponActivityDetail);

    /**
     * 获取优惠规则
     * @param couponActivity
     * @return
     */
    String buildCouponRules(CouponActivityDTO couponActivity);

    /**
     * 根据企业id、优惠券活动id列表 获取已领取优惠券数量
     * @param eid
     * @param idAllList
     * @return
     */
    Map<Long, Integer> getCouponHasGetCountMap(Long eid, List<Long> idAllList);

    /**
     * 根据企业id、优惠券活动id列表 获取已领取优惠券数量
     * @param eid
     * @param idAllList
     * @return
     */
    Map<Long, List<CouponDO>> getCouponHasGetCount(Long eid, List<Long> idAllList);

    /**
     * 根据优惠券活动id列表 获取优惠券规则设置的可领取数量
     * @param couponActivityList
     * @return
     */
    Map<Long, Integer> getCanGetNumMap(List<CouponActivityDO> couponActivityList);

    /**
     * 获取优惠规则-移动端
     * @param couponActivity
     * @return
     */
    Map<String, String> buildCouponRulesMobile(CouponActivityDTO couponActivity);

    /**
     * 根据id、使用数量 更新优惠券已使用数量
     * @param map
     * @return
     */
    Boolean updateUseCountByIds(Map<Long, Integer> map, Long usetId);

    /**
     * 发放的优惠券活动校验
     * @param couponActivity
     * @param countList
     * @return
     */
    String checkCouponActivity(CouponActivityDO couponActivity, List<Integer> countList, List<Map<String, Long>> giveCountList);

    /**
     * 赠品详情优惠券列表
     * @param request
     * @return
     */
    Page<CouponActivityDO> getPageForGoodsGift(QueryCouponActivityRequest request);

    /**
     * 查询限制商家、限制商品的优惠券活动列表
     * @return
     */
    List<CouponActivityDO> getByEnterpriseAndGoodsLimit(QueryCouponActivityLimitRequest request);

    /**
     * 根据eids获取优惠券活动详情列表
     * @param eids
     * @return
     */
    List<CouponActivityDO> getCouponActivityByEeid(List<Long> eids);

    /**
     * 修改删除商家、商品设置校验
     * @param couponActivityId
     */
    String deleteCouponActivityLimitCheck(Long currentEid, Long couponActivityId);


    /**
     * 根据优惠券id查询
     * @param idList
     * @return
     */
    List<CouponActivityCanUseDetailDTO> queryByCouponActivityIdList(List<Long> idList);

    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<CouponActivityDTO> queryListForMarketing(QueryCouponActivityRequest request);

    /**
     * 根据优惠券id查询
     * @param request
     * @return
     */
    Long getActivityIdByCouponId(QueryActivityDetailRequest request);

    /**
     * 根据优惠券id查询
     * @param ids
     * @return
     */
    List<CouponDTO> getCouponById(List<Long> ids);

    /**
     * 根据优惠券id查询
     * @param id
     * @return
     */
    CouponActivityDetailDTO getDetailById(Long id);

    /**
     * 发放优惠券
     * @param requests
     * @return
     */
    Boolean giveCoupon(List<SaveCouponRequest> requests);

    /**
     * 发放优惠券
     * @param requests
     * @return
     */
    Boolean giveCouponBySingal(SaveCouponRequest requests,CouponActivityDetailDTO couponActivityDetailDTO);


    /**
     * 根据优惠券id查询
     * @param request
     * @return
     */
    Map<Long, CouponActivityDetailDTO> getRemainDtoByActivityIds(List<Long> request);

    /**
     * 根据优惠券id查询剩余优惠券库存
     *
     * @param request
     * @return
     */
    Map<Long, Integer> getRemainByActivityIds(List<Long> request);

    Boolean scrapActivity(CouponActivityOperateRequest request);

    Boolean updateCoupon(List<Long> partIds);

    List<CouponDTO> getHasGiveCountByEids(Long couponActivityId, List<Long> eidList);

    void updateHasGiveNum(Long couponActivityId, int size);

    void SyncCouponGiveNum();
}
