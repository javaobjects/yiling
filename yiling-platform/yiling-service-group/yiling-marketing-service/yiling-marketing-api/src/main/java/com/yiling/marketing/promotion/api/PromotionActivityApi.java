package com.yiling.marketing.promotion.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.pojo.Result;
import com.yiling.marketing.promotion.dto.AvtivityCenterDTO;
import com.yiling.marketing.promotion.dto.OrderUsePaymentActivityDTO;
import com.yiling.marketing.promotion.dto.PaymentActivityUseDTO;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.marketing.promotion.dto.PromotionActivityPageDTO;
import com.yiling.marketing.promotion.dto.PromotionAppListDTO;
import com.yiling.marketing.promotion.dto.PromotionCheckContextDTO;
import com.yiling.marketing.promotion.dto.PromotionCombinationPackDTO;
import com.yiling.marketing.promotion.dto.PromotionDTO;
import com.yiling.marketing.promotion.dto.PromotionEnterpriseLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionSecKillSpecialDTO;
import com.yiling.marketing.promotion.dto.PromotionSpecialActivityAppointmentPageDTO;
import com.yiling.marketing.promotion.dto.SpecialActivityEnterpriseDTO;
import com.yiling.marketing.promotion.dto.SpecialActivityPageDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityAppointmentItemDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityGoodsInfoDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityInfoDTO;
import com.yiling.marketing.promotion.dto.request.ActivityGoodsPageRequest;
import com.yiling.marketing.promotion.dto.request.OrderUsePaymentActivityRequest;
import com.yiling.marketing.promotion.dto.request.PromotionActivityPageRequest;
import com.yiling.marketing.promotion.dto.request.PromotionActivityRequest;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppCartRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.marketing.promotion.dto.request.PromotionEnterpriseRequest;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftReturnRequest;
import com.yiling.marketing.promotion.dto.request.PromotionJudgeRequest;
import com.yiling.marketing.promotion.dto.request.PromotionReduceRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSaveBuyRecordRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSettleJudgeRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSpecialSaveRequest;
import com.yiling.marketing.promotion.dto.request.PromotionUpdateBuyRecordRequest;
import com.yiling.marketing.promotion.dto.request.QueryPaymentActivityRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityAppointmentAddRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityInfoRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityPageRequest;

/**
 * 赠品 API
 *
 * @author: houjie.sun
 * @date: 2021/10/23
 */
public interface PromotionActivityApi {


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
     * 获得扣减库存
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
     * 根据id查询促销活动信息
     *
     * @param id
     * @return
     */
    PromotionActivityDTO queryById(Long id);

    /**
     * 批量查询促销活动信息
     * @param idList
     * @return
     */
    List<PromotionActivityDTO> batchQueryByIdList(List<Long> idList);

    List<PromotionEnterpriseLimitDTO> queryEnterpriseByActivityId(Long promotionActivityId);

    List<PromotionGoodsGiftLimitDTO> queryGoodsGiftByActivityId(Long promotionActivityId);

    PromotionSecKillSpecialDTO querySecKillSpecialByActivityId(Long promotionActivityId);

    List<PromotionGoodsLimitDTO> queryGoodsByActivityId(Long promotionActivityId);

    Page<PromotionGoodsLimitDTO> pageGoodsByActivityId(ActivityGoodsPageRequest request);

    PromotionCombinationPackDTO quaryCombinationPackByActivityId(Long promotionActivityId);

    /**
     * 分页查询营销活动信息
     *
     * @param request
     * @return
     */
    Page<PromotionActivityPageDTO> pageList(PromotionActivityPageRequest request);

    /**
     * 促销活动 状态修改--停用和做废，停用后不能再获得，作废后已经发放的也失效
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

    /**
     * 根据批量赠品库id查询  启动并且在活动时间内的促销活动
     *
     * @param goodsGiftIdList
     * @return
     */
    List<PromotionGoodsGiftLimitDTO> queryByGiftIdList(List<Long> goodsGiftIdList);

    /**
     * 查询商品是否被重复添加
     * @param goodsIdList - 商品列表
     * @param sponsorType - 活动分类（1-平台活动；2-商家活动）
     * @param type - 活动类型（1-满赠；2-特价；3-秒杀）
     * @return
     */
    List<PromotionGoodsLimitDTO> queryNotRepeatByGoodsIdList(List<Long> goodsIdList, Integer sponsorType, Integer type);

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
     * 查询企业参与的活动信息
     * @param enterpriseRequest
     */
    List<PromotionAppListDTO> queryEnterprisePromotion(PromotionEnterpriseRequest enterpriseRequest);

    /**
     * 查询商品促销信息
     * @param request
     * @return
     */
    List<PromotionGoodsLimitDTO> queryGoodsPromotionInfo(PromotionAppRequest request);

    /**
     * 保存购买记录
     * @param buyRecordRequest
     * @return
     */
    Boolean saveBuyRecord(PromotionSaveBuyRecordRequest buyRecordRequest);

    /**
     * 退货更新购买数量
     * @param request
     * @return
     */
    Boolean updateBuyRecordQuantity(PromotionUpdateBuyRecordRequest request);

    /**
     * 根据营销活动id和商家id返回生效或者失效营销活动信息
     * @param request
     * @return
     */
    List<PromotionDTO> queryPromotionInfoByActivityAndBuyerId(PromotionActivityRequest request);

    /**
     * 根据商家id返回组合包信息
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
     * 分页查询专场活动信息
     *
     * @param request
     * @return
     */
    Page<SpecialActivityPageDTO> querySpecialActivity(SpecialActivityPageRequest request);

    /**
     * 新增/编辑促销活动
     *
     * @param request
     * @return
     */
    long savePromotionSpecialActivity(PromotionSpecialSaveRequest request);

    /**
     * 通过id查询专场活动信息
     *
     * @param id
     * @return
     */
    SpecialActivityPageDTO querySpecialActivityInfo(Long id);

    /**
     * 查询符合条件的企业和营销活动信息
     *
     * @param request
     * @return
     */
    Page<SpecialActivityEnterpriseDTO> pageActivityInfo(SpecialActivityPageRequest request);

    /**
     * 专场活动停用
     *
     * @param request
     * @return
     */
    boolean editSpecialActivityStatusById(PromotionActivityStatusRequest request);

    Page<PromotionSpecialActivityAppointmentPageDTO> querySpecialActivityAppointment(SpecialActivityPageRequest request);

    /**
     * 新增预约
     *
     * @param appointmentAddForm
     * @return
     */
    Boolean appointmentAdd(SpecialActivityAppointmentAddRequest appointmentAddForm);

    /**
     * 新增预约
     *
     * @param currentUserId
     * @return
     */
    Page<SpecialAvtivityAppointmentItemDTO> queryMyAppointment(Long currentUserId, SpecialActivityPageRequest type);

    /**
     * 活动中心列表
     *
     * @param
     * @return
     */
    AvtivityCenterDTO activityCenter(Long currentEid);

    /**
     * 专场活动列表
     *
     * @param
     * @return
     */
    List<SpecialAvtivityInfoDTO> specialActivityInfo(SpecialActivityInfoRequest form);

    /**
     * 营销活动下产品列表
     *
     * @param
     * @return
     */
    List<SpecialAvtivityGoodsInfoDTO> getGoodsInfoByActivityId(SpecialActivityInfoRequest request);

    /**
     * 我的预约总数
     *
     * @param
     * @return
     */
    SpecialAvtivityAppointmentItemDTO myAppointmentCount(Long currentUserId);



    /**
     * 根据企业id、购买商品id、小计金额 查询可以使用的促销活动
     * @param request
     * @return
     */
    PaymentActivityUseDTO getPaymentActivityUseList(QueryPaymentActivityRequest request);



    /**
     * 根据企业id、活动Id、商品明细 计算商品分摊优惠金额
     * 分摊维度到具体商品维度
     * @param request
     * @return
     */
    OrderUsePaymentActivityDTO orderUsePaymentActivityShareAmountBudget(OrderUsePaymentActivityRequest request);



    List<PromotionCombinationPackDTO> quaryCombinationPackByActivityIds(List<Long> combinationIds,Long buyerEid);
}
