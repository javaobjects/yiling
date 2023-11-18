package com.yiling.marketing.couponactivityautogive.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.couponactivity.dto.CouponActivityGiveDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityGoodsDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityAutoGiveGoodsRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryGiveEnterpriseInfoListRequest;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoRequest;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDetailDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveEnterpriseInfoDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveEnterpriseLimitDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveGoodsLimitDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveMemberDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGivePageDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveRecordDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.CouponActivityAutoGiveOperateRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.CouponActivityAutoGiveRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGiveGoodsLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.DeleteCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityGiveDetailRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityGiveFailRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveBasicRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveGoodsLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveMemberLimitRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.SaveCouponActivityAutoGiveRulesRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveCountRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveRecordStatusRequest;

/**
 * 优惠券自动发放活动 API
 *
 * @author: houjie.sun
 * @date: 2021/10/26
 */
public interface CouponActivityAutoGiveApi {

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
     * 根据优惠券活动ID查询 优惠券自动发放活动关联信息
     * @param couponActivityId
     * @return
     */
    CouponActivityAutoGiveCouponDTO getAutoGiveCouponByCouponActivityId(Long couponActivityId);

    /**
     * 根据优惠券活动ID列表查询 优惠券自动发放活动关联信息列表
     * @param couponActivityIdList
     * @return
     */
    List<CouponActivityAutoGiveCouponDTO> getByCouponByCouponActivityIdList(List<Long> couponActivityIdList);

    /**
     * 自动发放优惠券活动分页
     * @param request
     * @return
     */
    Page<CouponActivityAutoGivePageDTO> queryListPage(QueryCouponActivityAutoGiveRequest request);

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
     * 查询已添加企业信息列表
     * @param request
     * @return
     */
    Page<CouponActivityGoodsDTO> queryGoodsLimitListPage(QueryCouponActivityAutoGiveGoodsRequest request);

    /**
     * 添加商品信息
     * @param request
     * @return
     */
    Boolean saveGoodsLimit(SaveCouponActivityAutoGiveGoodsLimitRequest request);

    /**
     * 删除已添加商品信息
     * @param request
     * @return
     */
    Boolean deleteGoodsLimit(DeleteCouponActivityAutoGiveGoodsLimitRequest request);

    /**
     * 查询会员信息列表
     * @param request
     * @return
     */
//    Page<CouponActivityAutoGiveMemberDTO> queryEnterpriseListPage(QueryCouponActivityAutoGiveMemberRequest request);

    /**
     * 查询会员信息列表
     * @param request
     * @return
     */
    Page<CouponActivityAutoGiveMemberDTO> queryEnterpriseLimitListPage(QueryCouponActivityAutoGiveMemberLimitRequest request);

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
     * 已发放优惠券分页列表
     * @param request
     * @return
     */
    Page<CouponActivityGiveDTO> queryGiveListPage(QueryCouponActivityGivePageRequest request);


    /**
     * 自动发放优惠券活动分页
     * @param request
     * @return
     */
    Page<CouponActivityAutoGiveRecordDTO> queryGiveFailListPage(QueryCouponActivityGiveFailRequest request);

    /**
     * 根据发放记录id再次手动发放
     * @param request
     * @return
     */
    Boolean autoGive(CouponActivityAutoGiveRequest request);

    /**
     * 根据自动发券活动ID 查询优惠券自动发放活动关联信息列表
     * @param couponActivityAutoGiveIds
     * @return
     */
    List<CouponActivityAutoGiveCouponDTO> getAutoGiveCouponByAutoGiveIdList(List<Long> couponActivityAutoGiveIds);

    /**
     * 根据条件查询优惠券自动发放活动列表
     * @param request
     * @return
     */
    List<CouponActivityAutoGiveDetailDTO> getAllByCondition(QueryCouponActivityGiveDetailRequest request);

    /**
     * 根据企业ID 查询最近的优惠券自动发放活动记录
     * @param eid
     * @return
     */
    CouponActivityAutoGiveRecordDTO getAutoGiveRecordLastOneByEid(Long eid);

    /**
     * 根据自动发放活动ids查询指定商品
     * @param autoGiveIdList
     * @return
     */
    List<CouponActivityAutoGiveGoodsLimitDTO> getGoodsLimitByAutoGiveIdList(List<Long> autoGiveIdList);

    /**
     * 根据自动发放活动ids查询指定会员
     * @param autoGiveIdList
     * @return
     */
    List<CouponActivityAutoGiveEnterpriseLimitDTO> getEnterpriseLimitByAutoGiveIdList(List<Long> autoGiveIdList);

    /**
     * 根据企业ID、发放活动IDS 查询优惠券自动发放活动记录
     * @param eid
     * @param autoGiveIdList
     * @return
     */
    List<CouponActivityAutoGiveRecordDTO> getRecordListByEidAndAutoGiveIds(Long eid, List<Long> autoGiveIdList);

    /**
     * 保存自动发放记录，状态待发放
     * @param autoGiveList
     * @return
     */
    List<CouponActivityAutoGiveRecordDTO> saveAutoGiveRecordWithWaitStatus(List<CouponActivityAutoGiveRecordDTO> autoGiveList);

    /**
     * 根据ID更新发放记录状态
     * @param autoGiveList
     * @return
     */
    Boolean updateRecordStatus(List<UpdateAutoGiveRecordStatusRequest> autoGiveList);

    /**
     * 根据id更新已发放次数
     * @param request
     * @return
     */
    Boolean updateGiveCountByIdList(UpdateAutoGiveCountRequest request);

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
    List<CouponActivityAutoGiveEnterpriseInfoDTO> getByEidAndCouponActivityId(QueryGiveEnterpriseInfoListRequest request);


    /**
     * 根据发放活动IDS 查询优惠券自动发放活动记录
     * @param autoGiveIdList
     * @return
     */
    List<CouponActivityAutoGiveRecordDTO> getRecordListByAutoGiveIds(List<Long> autoGiveIdList);

    /**
     * 根据自动发放活动id查询指定商品
     * @param autoGiveId
     * @return
     */
    List<CouponActivityAutoGiveGoodsLimitDTO> getGoodsLimitByAutoGiveId(Long autoGiveId);

    /**
     * 根据自动发放优惠券id查询企业限制表
     * @param id
     * @return
     */
    List<CouponActivityAutoGiveEnterpriseLimitDTO> getByAutoGiveId(Long id);

}
