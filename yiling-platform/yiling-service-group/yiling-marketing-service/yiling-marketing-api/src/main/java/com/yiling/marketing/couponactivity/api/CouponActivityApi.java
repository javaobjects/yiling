package com.yiling.marketing.couponactivity.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.QueryCouponCanUseListRequest;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.coupon.dto.request.UseMemberCouponRequest;
import com.yiling.marketing.couponactivity.bo.CouponActivityRulesBO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanAndOwnDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEidOrGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpriseDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpriseGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpriseLimitDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveEnterpriseInfoDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGoodsDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityHasGetDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityListFiveByGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityResidueCountDTO;
import com.yiling.marketing.couponactivity.dto.CouponMemberActivityLimitDTO;
import com.yiling.marketing.couponactivity.dto.CouponMemberActivityRuleDTO;
import com.yiling.marketing.couponactivity.dto.GetCouponActivityResultDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityExistFlagRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityIncreaseRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityOperateRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityEnterpriseGiveRecordRequest;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.DeleteCouponActivityGoodsLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponBudgetRequest;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseGiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityEnterpriseRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGoodsRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponMemberActivityPageRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityBasicRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityEnterpriseLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGoodsLimitRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityRulesRequest;

/**
 * 优惠券活动 API
 *
 * @author: houjie.sun
 * @date: 2021/10/25
 */
public interface CouponActivityApi {

    /**
     * 分页列表
     *
     * @param request QueryCouponActivityRequest
     * @return 优惠券活动分页列表
     */
    Page<CouponActivityDTO> queryListPage(QueryCouponActivityRequest request);

    /**
     * 分页列表
     *
     * @param request QueryCouponActivityRequest
     * @return 优惠券活动分页列表
     */
    Page<CouponActivityDTO> queryListForMarketing(QueryCouponActivityRequest request);

    /**
     * 获取优惠券活动详情
     *
     * @param request
     * @return
     */
    CouponActivityDetailDTO getCouponActivityById(QueryCouponActivityDetailRequest request);

    /**
     * 根据ID复制优惠券活动
     *
     * @param request
     * @return
     */
    CouponActivityDetailDTO copy(CouponActivityOperateRequest request);

    /**
     * 根据ID停用优惠券活动
     *
     * @param request
     * @return
     */
    Boolean stop(CouponActivityOperateRequest request);

    /**
     * 根据ID作废优惠券活动
     *
     * @param request
     * @return
     */
    Boolean scrap(CouponActivityOperateRequest request);

    /**
     * 作废优惠券，切勿返回卡报表哦逐渐
     *
     * @param request
     * @return
     */
    List<Long> scrapAndReturnCoupon(CouponActivityOperateRequest request);

    /**
     * 根据ID增加优惠券数量
     *
     * @param request
     * @return
     */
    Boolean increase(CouponActivityIncreaseRequest request);

    /**
     * 保存优惠券活动基本信息
     *
     * @param request SaveCouponActivityBasicRequest
     * @return 优惠券活动id
     */
    Long saveOrUpdateBasic(SaveCouponActivityBasicRequest request);

    /**
     * 保存会员优惠券活动基本信息
     *
     * @param request SaveCouponActivityBasicRequest
     * @return 优惠券活动id
     */
    Long saveOrUpdateBasicForMember(SaveCouponActivityBasicRequest request);

    /**
     * 保存优惠券活动使用规则
     *
     * @param request SaveCouponActivityRulesRequest
     * @return 优惠券活动id
     */
    Long saveOrUpdateRules(SaveCouponActivityRulesRequest request);

    /**
     * 保存优惠券活动操作日志
     *
     * @param CouponActivityId
     * @param type
     * @param content
     * @param status
     * @param faileReason
     * @return
     */
    Boolean saveCouponActivityLog(Long CouponActivityId, Integer type, String content, Integer status, String faileReason, Long createUser);

    /**
     * 查询企业信息列表
     *
     * @param request
     * @return
     */
    Page<CouponActivityEnterpriseDTO> queryEnterpriseListPage(QueryCouponActivityEnterpriseRequest request);

    /**
     * 查询已添加企业信息列表
     *
     * @param request
     * @return
     */
    Page<CouponActivityEnterpriseLimitDTO> queryEnterpriseLimitListPage(QueryCouponActivityEnterpriseLimitRequest request);

    /**
     * 添加企业信息
     *
     * @param request
     * @return
     */
    Boolean saveEnterpriseLimit(SaveCouponActivityEnterpriseLimitRequest request);

    /**
     * 删除已添加企业信息
     *
     * @param request
     * @return
     */
    Boolean deleteEnterpriseLimit(DeleteCouponActivityEnterpriseLimitRequest request);

    /**
     * 查询商品信息列表
     * @param request
     * @return
     */
    //    Page<CouponActivityGoodsDTO> queryGoodsListPage(QueryCouponActivityGoodsRequest request);

    /**
     * 查询已添加企业信息列表
     *
     * @param request
     * @return
     */
    Page<CouponActivityGoodsDTO> queryGoodsLimitListPage(QueryCouponActivityGoodsRequest request);

    /**
     * 查询已添加企业信息列表
     *
     * @param request
     * @return
     */
    List<Long> queryGoodsLimitList(QueryCouponActivityGoodsRequest request);

    /**
     * 添加商品信息
     *
     * @param request
     * @return
     */
    Boolean saveGoodsLimit(SaveCouponActivityGoodsLimitRequest request);

    /**
     * 添加商品信息
     *
     * @param request
     * @return
     */
    Boolean batchSaveGoodsLimit(SaveCouponActivityGoodsLimitRequest request);

    /**
     * 删除已添加商品信息
     *
     * @param request
     * @return
     */
    Boolean deleteGoodsLimit(DeleteCouponActivityGoodsLimitRequest request);

    /**
     * 查询待发放企业信息列表
     *
     * @param request
     * @return
     */
    Page<CouponActivityEnterpriseGiveDTO> queryEnterpriseGiveListPage(QueryCouponActivityEnterpriseGiveRequest request);

    /**
     * 查询已发放企业信息列表
     *
     * @param request
     * @return
     */
    Page<CouponActivityGiveEnterpriseInfoDTO> queryEnterpriseGiveRecordListPage(QueryCouponActivityGiveEnterpriseInfoRequest request);

    /**
     * 查询已发放企业信息列表
     *
     * @param request
     * @return
     */
    Boolean addGiveEnterpriseInfo(SaveCouponActivityGiveEnterpriseInfoRequest request);

    /**
     * 删除已添加企业信息
     *
     * @param request
     * @return
     */
    Boolean deleteEnterpriseGiveRecord(DeleteCouponActivityEnterpriseGiveRecordRequest request);

    /**
     * 已发放优惠券分页列表
     *
     * @param request
     * @return
     */
    Page<CouponActivityGiveDTO> queryGiveListPage(QueryCouponActivityGivePageRequest request);

    /**
     * 已使用优惠券分页列表
     * @param request
     * @return
     */
    //    Page<CouponActivityUsedDTO> queryUsedListPage(QueryCouponActivityDetailRequest request);

    /**
     * 根据优惠券活动ID获取优惠券活动剩余数量、企业限制类型
     *
     * @param couponActivityId
     */
    CouponActivityResidueCountDTO getResidueCount(Long couponActivityId);

    /**
     * 获取优惠规则
     *
     * @param couponActivity
     * @return
     */
    String buildCouponRules(CouponActivityDTO couponActivity);

    /**
     * 根据ids获取优惠券活动详情列表
     *
     * @param ids
     * @return
     */
    List<CouponActivityDetailDTO> getCouponActivityById(List<Long> ids);

    /**
     * 根据ids获取优惠券活动详情列表
     *
     * @param ids
     * @return
     */
     List<CouponDTO> getCouponById(List<Long> ids);

    /**
     * 根据ids获取有效的优惠券活动详情列表
     *
     * @param ids
     * @param totalCountFlag 优惠券总数量达到标识：0-未达到，可发放/领取 1-已达到，不可再发放/领取
     * @param statusFlag 优惠券状态标识：0-启用 1-启用、停用
     * @return
     */
    List<CouponActivityDTO> getEffectiveCouponActivityByIdList(List<Long> ids, int totalCountFlag, int statusFlag);

    /**
     * 根据优惠券活动id查询所有可使用企业关系
     *
     * @param couponActivityIdList 优惠券活动id
     * @return
     */
    List<CouponActivityEnterpriseLimitDTO> getByCouponActivityIdList(List<Long> couponActivityIdList);


    // ********************************************** 移动端-店铺 ****************************************

    /**
     * 根据企业id 查询可领取优惠券活动列表
     *
     * @param request
     * @return
     */
    List<CouponActivityHasGetDTO> getCouponActivityListByEid(QueryCouponActivityCanReceiveRequest request);

    /**
     * 根据企业id 查询可领取优惠券活动列表分页
     *
     * @param request
     * @return
     */
    List<CouponActivityHasGetDTO> getCouponActivityListPageByEid(QueryCouponActivityCanReceivePageRequest request);

    /**
     * 赠品详情优惠券列表
     *
     * @param request
     * @return
     */
    Page<CouponActivityDTO> getPageForGoodsGift(QueryCouponActivityRequest request);

    /**
     * 发放的优惠券活动校验
     *
     * @param couponActivity
     * @param countList
     * @return
     */
    String checkCouponActivity(CouponActivityDTO couponActivity, List<Integer> countList, List<Map<String, Long>> giveCountList);

    // ********************************************** 移动端-商品详情 ****************************************

    /**
     * 根据商品id、企业id 查询优惠券活动列表
     * 最多返回5个
     *
     * @param goodsId 商品id
     * @param eid 店铺企业id
     * @return
     */
    List<CouponActivityListFiveByGoodsIdDTO> getListFiveByGoodsIdAndEid(Long goodsId, Long eid, Integer limit, Integer platformType);

    /**
     * 根据商品id、企业id 查询可领取、已领取优惠券活动列表
     *
     * @return
     */
    CouponActivityCanAndOwnDTO getCanAndOwnListByEid(QueryCouponActivityCanReceiveRequest request);

    /**
     * 根据商品id、企业id 查询所有参与活动的商品列表
     *
     * @param goodsId 商品id
     * @param eid 店铺企业id
     * @return
     */
    CouponActivityEidOrGoodsIdDTO getGoodsListByGoodsIdAndEid(Long goodsId, Long eid);

    /**
     * 根据优惠券活动id 领取优惠券
     *
     * @param request
     * @return
     */
    Boolean receiveByCouponActivityId(CouponActivityReceiveRequest request);

    /**
     * 根据企业ID、商品ID 查询是否存在优惠券活动
     *
     * @param request
     * @return key 商品id value 1-满减券；2-满折券，value 空表示没有参加优惠券活动
     */
    Map<Long, List<Integer>> getCouponActivityExistFlag(CouponActivityExistFlagRequest request);


    // ********************************************** 移动端-进货单 ****************************************

    /**
     * 获取支持使用优惠券的支付方式
     *
     * @return
     */
    List<Integer> suportCouponPayMethodList();

    /**
     * 根据企业id、购买商品id、小计金额 查询已领取可使用优惠券
     *
     * @param request
     * @return
     */
    CouponActivityCanUseDTO getCouponCanUseList(QueryCouponCanUseListRequest request);


    /**
     * 根据企业id、优惠券ID、商品明细 计算商品分摊优惠金额
     * 分摊维度到商品SKU
     *
     * @param request
     * @return
     */
    OrderUseCouponBudgetDTO orderUseCouponShareAmountBudget(OrderUseCouponBudgetRequest request);

    /**
     * 进货单结算 使用优惠券
     *
     * @param request
     * @return
     */
    Boolean orderUseCoupon(OrderUseCouponRequest request);


    /**
     * 获取当前用户可使用的会员优惠券列表
     * @param request
     * @return
     */
    Boolean useMemberCoupon(UseMemberCouponRequest request);

    /**
     * 根据优惠券活动id查询优惠券可用企业关系
     *
     * @param couponActivityId
     * @return
     */
    List<CouponActivityEnterpriseLimitDTO> getByCouponActivityId(Long couponActivityId);

    /**
     * 通过优惠券活动id查询
     *
     * @param idList
     * @return
     */
    List<CouponActivityCanUseDetailDTO> queryByCouponActivityIdList(List<Long> idList);

    /**
     * 根据id查询优惠规则
     *
     * @param couponActivityId
     * @return
     */
    CouponActivityRulesBO getCouponActivityRulesById(Long couponActivityId);

    /**
     * 根据会员优惠券id查询优惠规则
     *
     * @param couponActivityId
     * @return
     */
    CouponMemberActivityRuleDTO getMemberCouponActivityRulesById(Long couponActivityId);

    /**
     * 根据卡包id获取会员优惠券id
     *
     * @param couponId
     * @return
     */
    Long getActivityIdByCouponId(QueryActivityDetailRequest couponId);

    /**
     * 获取列表
     *
     * @param request
     * @return
     */
    Page<CouponActivityEnterpDTO> getCouponsCenter(QueryCouponActivityCanReceivePageRequest request);

    /**
     * 获取优惠券中心列表
     *
     * @param request
     * @return
     */
    List<CouponActivityHasGetDTO> getMemberCouponsCenter(QueryCouponActivityCanReceivePageRequest request);

    /**
     * 根据优惠券活动id 领取优惠券
     *
     * @param request
     * @return
     */
    GetCouponActivityResultDTO receiveByCouponActivityIdForApp(CouponActivityReceiveRequest request);

    /**
     * 根据优惠券活动id 领取优惠券
     *
     * @param request
     * @return
     */
    Boolean saveMemberCouponRelation(SaveCouponActivityBasicRequest request);

    /**
     * 根据优惠券活动id 领取优惠券
     *
     * @param request
     * @return
     */
    Boolean saveAutoGetMemberCouponRelation(SaveCouponActivityBasicRequest request);

    /**
     * 根据优惠券id查询剩余优惠券库存
     *
     * @param request
     * @return
     */
    Map<Long,Integer> getRemainByActivityIds(List<Long> request);

    /**
     * 根据优惠券id查询剩余优惠券库存
     *
     * @param request
     * @return
     */
    Map<Long,CouponActivityDetailDTO> getRemainDtoByActivityIds(List<Long> request);

    /**
     * 删除会员优惠券关联德会员规格
     *
     * @param request
     * @return
     */
    Boolean deleteMemberCouponRelation(SaveCouponActivityBasicRequest request);

    /**
     * 分页会员优惠券关联德会员规格
     *
     * @param request
     * @return
     */
    Page<CouponMemberActivityLimitDTO> PageActivityMeberLimit(QueryCouponMemberActivityPageRequest request);

    /**
     * 分页会员优惠券关联德会员规格
     *
     * @param request
     * @return
     */
    Page<CouponMemberActivityLimitDTO> PageActivityAutoGetMeberLimit(QueryCouponMemberActivityPageRequest request);

    /**
     * 删除会员优惠券关联德会员规格
     *
     * @param request
     * @return
     */
    Boolean deleteAutoGetMemberCouponRelation(SaveCouponActivityBasicRequest request);


    /**
     * 一键领取某个商家下面的优惠券
     *
     * @param request
     * @return
     */
    Boolean oneKeyReceive(QueryCouponActivityCanReceiveRequest request);

    /**
     * id获取优惠券详情
     *
     * @param id
     * @return
     */
    CouponActivityDetailDTO getActivityCouponById(Long id);

    /**
     * 发放优惠券
     *
     * @param requests
     * @return
     */
    Boolean giveCoupon(List<SaveCouponRequest>  requests);

    Boolean giveCouponBySingal(SaveCouponRequest  requests,CouponActivityDetailDTO couponActivityDetailDTO);

    Boolean scrapActivity(CouponActivityOperateRequest request);

    Boolean updateCoupon(List<Long> partIds);

    List<CouponDTO>  getHasGiveCountByEids(Long couponActivityId, List<Long> eidList);

    void SyncCouponGiveNum();
}

