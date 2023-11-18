package com.yiling.marketing.promotion.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.pojo.Result;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.marketing.promotion.dto.PromotionActivityPageDTO;
import com.yiling.marketing.promotion.dto.PromotionAppListDTO;
import com.yiling.marketing.promotion.dto.PromotionCheckContextDTO;
import com.yiling.marketing.promotion.dto.PromotionDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.SpecialActivityPageDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityAppointmentItemDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityGoodsInfoDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityInfoDTO;
import com.yiling.marketing.promotion.dto.request.ActivityGoodsPageRequest;
import com.yiling.marketing.promotion.dto.request.PromotionActivityPageRequest;
import com.yiling.marketing.promotion.dto.request.PromotionActivityRequest;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppCartRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.marketing.promotion.dto.request.PromotionEnterpriseRequest;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftReturnRequest;
import com.yiling.marketing.promotion.dto.request.PromotionJudgeRequest;
import com.yiling.marketing.promotion.dto.request.PromotionReduceRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSettleJudgeRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityInfoRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityPageRequest;
import com.yiling.marketing.promotion.entity.PromotionActivityDO;
import com.yiling.marketing.promotion.entity.PromotionGoodsLimitDO;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;

/**
 * <p>
 * 促销活动主表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface PromotionActivityService extends BaseService<PromotionActivityDO> {

    /**
     * 查看订单参与的满赠活动
     *
     * @param request
     * @return
     */
    Result<List<PromotionGoodsGiftLimitDTO>> judgePromotion(PromotionJudgeRequest request);

    /**
     * 结算订单参与的满赠活动
     *
     * @param request
     * @return
     */
    Result<List<Long>> settleJudgePromotion(PromotionSettleJudgeRequest request);

    /**
     * 满赠商品
     *
     * @param request
     * @return
     */
    boolean promotionReduceStock(PromotionReduceRequest request);

    /**
     * 新增/编辑促销活动
     *
     * @param request
     * @return
     */
    Long savePromotionActivity(PromotionSaveRequest request);

    /**
     * 分页查询营销活动信息
     *
     * @param request
     * @return
     */
    Page<PromotionActivityPageDTO> pageList(PromotionActivityPageRequest request);

    /**
     * 促销活动 状态修改
     *
     * @param request
     * @return
     */
    boolean editStatusById(PromotionActivityStatusRequest request);

    /**
     * 复制
     *
     * @param request
     * @return
     */
    PromotionActivityDTO copy(PromotionActivityStatusRequest request);

    List<PromotionActivityDO> listByIdList(List<Long> idList, Integer platform);

    List<PromotionActivityDO> listByIdList(List<Long> idList);

    /**
     * 根据批量赠品库id查询  启动并且在活动时间内的促销活动
     *
     * @param goodsGiftIdList
     * @return
     */
    List<PromotionGoodsGiftLimitDTO> queryByGiftIdList(List<Long> goodsGiftIdList);

    List<PromotionGoodsLimitDO> queryByGoodsIdList(List<Long> goodsIdList, PromotionTypeEnum typeEnum, Integer platform);

    /**
     * 判断商品是否被重复添加
     * @return
     */
    List<PromotionGoodsLimitDO> queryNotRepeatByGoodsIdList(List<Long> goodsIdList, Integer sponsorType, Integer type, Long id);

    /**
     * app购物车展示满赠信息
     *
     * @param cartRequest
     * @return
     */
    List<PromotionAppListDTO> queryAppCartPromotion(PromotionAppCartRequest cartRequest);

    /**
     * 赠品退回
     *
     * @param request
     * @return
     */
    boolean returnPromotionGoodsGift(PromotionGoodsGiftReturnRequest request);

    /**
     * 活动停用
     *
     * @param idList
     * @param opUserId
     * @param opTime
     * @return
     */
    boolean stopPromotion(List<Long> idList, Long opUserId, Date opTime);

    /**
     * 查询企业参与的活动信息
     * @param enterpriseRequest
     * @return
     */
    List<PromotionAppListDTO> queryEnterprisePromotion(PromotionEnterpriseRequest enterpriseRequest);

    /**
     * 查询商品促销信息
     * @param request
     * @return
     */
    List<PromotionGoodsLimitDTO> queryGoodsPromotionInfo(PromotionAppRequest request);

    /**
     * 查询商品促销信息
     * @param request
     * @return
     */
    List<PromotionDTO> queryPromotionInfoByActivityAndBuyerId(PromotionActivityRequest request);

    /**
     * 查询营销活动通过商店id
     * @param request
     * @return
     */
    List<PromotionGoodsLimitDTO> pagePromotionByShopId(ActivityGoodsPageRequest request);

    /**
     * 判断营销活动对于某个商家是否可用
     * @param promotionCheckContextDTO
     * @return
     */
    Boolean promotionIsAvailable(PromotionCheckContextDTO promotionCheckContextDTO);

    /**
     * 判断营销活动对于某个商家是否可用
     * @param promotionCheckContextDTO
     * @return
     */
    Boolean promotionIsAvailableByContext(PromotionCheckContextDTO promotionCheckContextDTO);

    /**
     * 根据营销活动id营销活动信息(包含主表和商品表信息，企业表，秒杀表信息)
     * @param request
     * @return
     */
    PromotionGoodsLimitDTO queryPromotionInfoByActivity(ActivityGoodsPageRequest request);

    /**
     * 根据营销活动id获取商品信息
     * @param request
     * @return
     */
    List<SpecialAvtivityGoodsInfoDTO> getGoodsInfoByActivityId(SpecialActivityInfoRequest request);

    /**
     * 我的总预约数量
     * @param currentUserId
     * @return
     */
    SpecialAvtivityAppointmentItemDTO myAppointmentCount(Long currentUserId);
}
