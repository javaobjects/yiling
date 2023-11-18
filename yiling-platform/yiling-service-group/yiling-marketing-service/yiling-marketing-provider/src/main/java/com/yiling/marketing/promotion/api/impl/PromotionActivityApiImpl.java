package com.yiling.marketing.promotion.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode;
import com.yiling.marketing.goodsgift.entity.GoodsGiftDO;
import com.yiling.marketing.goodsgift.service.GoodsGiftService;
import com.yiling.marketing.payPromotion.handler.PayPromotionHandler;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
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
import com.yiling.marketing.promotion.entity.PromotionActivityDO;
import com.yiling.marketing.promotion.entity.PromotionCombinationPackageDO;
import com.yiling.marketing.promotion.entity.PromotionEnterpriseLimitDO;
import com.yiling.marketing.promotion.entity.PromotionGoodsGiftLimitDO;
import com.yiling.marketing.promotion.entity.PromotionGoodsLimitDO;
import com.yiling.marketing.promotion.entity.PromotionSecKillSpecialDO;
import com.yiling.marketing.promotion.entity.PromotionSpecialActivityEnterpriseDO;
import com.yiling.marketing.promotion.entity.PromotionSpecialAppointmentDO;
import com.yiling.marketing.promotion.service.PromotionActivityService;
import com.yiling.marketing.promotion.service.PromotionBuyRecordService;
import com.yiling.marketing.promotion.service.PromotionCombinationPackageService;
import com.yiling.marketing.promotion.service.PromotionEnterpriseLimitService;
import com.yiling.marketing.promotion.service.PromotionGoodsGiftLimitService;
import com.yiling.marketing.promotion.service.PromotionGoodsLimitService;
import com.yiling.marketing.promotion.service.PromotionSecKillSpecialService;
import com.yiling.marketing.promotion.service.PromotionSpecialActivityEnterpriseService;
import com.yiling.marketing.promotion.service.PromotionSpecialActivityService;
import com.yiling.marketing.promotion.service.PromotionSpecialAppointmentService;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.bo.CombinationBuyNumberBO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@DubboService
@Slf4j
public class PromotionActivityApiImpl implements PromotionActivityApi {

    @Autowired
    private PromotionActivityService promotionActivityService;

    @Autowired
    private PromotionEnterpriseLimitService promotionEnterpriseLimitService;

    @Autowired
    private PromotionGoodsGiftLimitService promotionGoodsGiftLimitService;

    @Autowired
    private PromotionGoodsLimitService promotionGoodsLimitService;

    @Autowired
    private PromotionSecKillSpecialService promotionSecKillSpecialService;

    @Autowired
    private GoodsGiftService goodsGiftService;

    @Autowired
    private PromotionBuyRecordService buyRecordService;

    @Autowired
    private PromotionCombinationPackageService promotionCombinationPackageService;

    @Autowired
    private PromotionSpecialActivityService promotionSpecialActivityService;

    @Autowired
    private PromotionSpecialActivityEnterpriseService enterpriseService;

    @Autowired
    private PromotionSpecialAppointmentService promotionSpecialAppointmentService;

    @Autowired
    private PayPromotionHandler payPromotionHandler;

    @DubboReference
    OrderPromotionActivityApi orderPromotionActivityApi;

    @DubboReference
    UserApi userApi;


    @Override
    public Result<List<PromotionGoodsGiftLimitDTO>> judgePromotion(PromotionJudgeRequest request) {
        return promotionActivityService.judgePromotion(request);
    }

    @Override
    public Result<List<Long>> settleJudgePromotion(PromotionSettleJudgeRequest request) {
        return promotionActivityService.settleJudgePromotion(request);
    }

    @Override
    public boolean promotionReduceStock(PromotionReduceRequest request) {
        return promotionActivityService.promotionReduceStock(request);
    }

    @Override
    public Long savePromotionActivity(PromotionSaveRequest request) {
        return promotionActivityService.savePromotionActivity(request);
    }

    @Override
    public PromotionActivityDTO queryById(Long id) {
        PromotionActivityDO promotionActivityDO = promotionActivityService.getById(id);
        return PojoUtils.map(promotionActivityDO, PromotionActivityDTO.class);
    }

    @Override
    public List<PromotionActivityDTO> batchQueryByIdList(List<Long> id) {
        List<PromotionActivityDO> list = promotionActivityService.listByIdList(id);
        return PojoUtils.map(list, PromotionActivityDTO.class);
    }

    @Override
    public List<PromotionEnterpriseLimitDTO> queryEnterpriseByActivityId(Long promotionActivityId) {
        List<PromotionEnterpriseLimitDO> promotionEnterpriseLimitDOS = promotionEnterpriseLimitService.queryByActivityId(promotionActivityId);
        return PojoUtils.map(promotionEnterpriseLimitDOS, PromotionEnterpriseLimitDTO.class);
    }

    @Override
    public List<PromotionGoodsGiftLimitDTO> queryGoodsGiftByActivityId(Long promotionActivityId) {
        List<PromotionGoodsGiftLimitDO> limitDOList = promotionGoodsGiftLimitService.queryByActivityId(promotionActivityId);
        List<Long> giftIdList = limitDOList.stream().map(PromotionGoodsGiftLimitDO::getGoodsGiftId).distinct().collect(Collectors.toList());
        List<GoodsGiftDO> goodsGiftDOList = goodsGiftService.listByIds(giftIdList);
        Map<Long, GoodsGiftDO> giftDOMap = goodsGiftDOList.stream().collect(Collectors.toMap(GoodsGiftDO::getId, o -> o, (k1, k2) -> k1));
        List<PromotionGoodsGiftLimitDTO> limitDTOList = PojoUtils.map(limitDOList, PromotionGoodsGiftLimitDTO.class);
        limitDTOList = limitDTOList.stream().map(item -> item.setGiftName(Optional.ofNullable(giftDOMap.get(item.getGoodsGiftId())).map(GoodsGiftDO::getName).orElse(null))).collect(Collectors.toList());
        return limitDTOList;
    }

    @Override
    public PromotionSecKillSpecialDTO querySecKillSpecialByActivityId(Long promotionActivityId) {
        PromotionSecKillSpecialDO promotionSecKillSpecialDO = promotionSecKillSpecialService.queryByPromotionActivityId(promotionActivityId);
        return PojoUtils.map(promotionSecKillSpecialDO, PromotionSecKillSpecialDTO.class);
    }

    @Override
    public List<PromotionGoodsLimitDTO> queryGoodsByActivityId(Long promotionActivityId) {
        List<PromotionGoodsLimitDO> doList = promotionGoodsLimitService.queryByActivityId(promotionActivityId);
        return PojoUtils.map(doList, PromotionGoodsLimitDTO.class);
    }

    @Override
    public Page<PromotionGoodsLimitDTO> pageGoodsByActivityId(ActivityGoodsPageRequest request) {
        Page<PromotionGoodsLimitDO> doPage = promotionGoodsLimitService.pageGoodsByActivityId(request);
        return PojoUtils.map(doPage, PromotionGoodsLimitDTO.class);
    }

    @Override
    public PromotionCombinationPackDTO quaryCombinationPackByActivityId(Long promotionActivityId) {
        PromotionCombinationPackageDO promotionCombinationPackage = promotionCombinationPackageService.queryByPromotionActivityId(promotionActivityId);
        return PojoUtils.map(promotionCombinationPackage, PromotionCombinationPackDTO.class);
    }

    @Override
    public Page<PromotionActivityPageDTO> pageList(PromotionActivityPageRequest request) {
        return promotionActivityService.pageList(request);
    }

    @Override
    public boolean editStatusById(PromotionActivityStatusRequest request) {
        return promotionActivityService.editStatusById(request);
    }

    @Override
    public PromotionActivityDTO copy(PromotionActivityStatusRequest request) {
        return promotionActivityService.copy(request);
    }

    @Override
    public List<PromotionGoodsGiftLimitDTO> queryByGiftIdList(List<Long> goodsGiftIdList) {
        return promotionActivityService.queryByGiftIdList(goodsGiftIdList);
    }

    @Override
    public List<PromotionGoodsLimitDTO> queryNotRepeatByGoodsIdList(List<Long> goodsIdList, Integer sponsorType, Integer type) {
        List<PromotionGoodsLimitDO> doList = promotionActivityService.queryNotRepeatByGoodsIdList(goodsIdList, sponsorType, type, null);
        return PojoUtils.map(doList, PromotionGoodsLimitDTO.class);
    }

    @Override
    public List<PromotionAppListDTO> queryAppCartPromotion(PromotionAppCartRequest cartRequest) {
        return promotionActivityService.queryAppCartPromotion(cartRequest);
    }

    @Override
    public boolean returnPromotionGoodsGift(PromotionGoodsGiftReturnRequest request) {
        return promotionActivityService.returnPromotionGoodsGift(request);
    }

    @Override
    public List<PromotionAppListDTO> queryEnterprisePromotion(PromotionEnterpriseRequest enterpriseRequest) {
        return promotionActivityService.queryEnterprisePromotion(enterpriseRequest);
    }

    @Override
    public List<PromotionGoodsLimitDTO> queryGoodsPromotionInfo(PromotionAppRequest request) {
        return promotionActivityService.queryGoodsPromotionInfo(request);
    }

    @Override
    public Boolean saveBuyRecord(PromotionSaveBuyRecordRequest buyRecordRequest) {
        return buyRecordService.saveBuyRecord(buyRecordRequest);
    }

    @Override
    public Boolean updateBuyRecordQuantity(PromotionUpdateBuyRecordRequest request) {
        return buyRecordService.updateBuyRecordQuantity(request);
    }

    @Override
    public List<PromotionDTO> queryPromotionInfoByActivityAndBuyerId(PromotionActivityRequest request) {
        return promotionActivityService.queryPromotionInfoByActivityAndBuyerId(request);
    }

    @Override
    public List<PromotionGoodsLimitDTO> pagePromotionByShopId(ActivityGoodsPageRequest request) {
        return promotionActivityService.pagePromotionByShopId(request);
    }

    @Override
    public Boolean promotionIsAvailable(PromotionCheckContextDTO promotionCheckContextDTO) {
        return promotionActivityService.promotionIsAvailable(promotionCheckContextDTO);
    }

    @Override
    public Boolean promotionIsAvailableByContext(PromotionCheckContextDTO promotionCheckContextDTO) {
        return promotionActivityService.promotionIsAvailableByContext(promotionCheckContextDTO);
    }

    @Override
    public PromotionGoodsLimitDTO queryPromotionInfoByActivity(ActivityGoodsPageRequest request) {
        return promotionActivityService.queryPromotionInfoByActivity(request);
    }

    @Override
    public Page<SpecialActivityPageDTO> querySpecialActivity(SpecialActivityPageRequest request) {
        return promotionSpecialActivityService.querySpecialActivity(request);
    }

    @Override
    public long savePromotionSpecialActivity(PromotionSpecialSaveRequest request) {
        return promotionSpecialActivityService.savePromotionSpecialActivity(request);
    }

    @Override
    public SpecialActivityPageDTO querySpecialActivityInfo(Long id) {
        return promotionSpecialActivityService.querySpecialActivityInfo(id);
    }

    @Override
    public Page<SpecialActivityEnterpriseDTO> pageActivityInfo(SpecialActivityPageRequest request) {
        return promotionSpecialActivityService.pageActivityInfo(request);
    }

    @Override
    public boolean editSpecialActivityStatusById(PromotionActivityStatusRequest request) {
        return promotionSpecialActivityService.editStatusById(request);
    }

    @Override
    public Page<PromotionSpecialActivityAppointmentPageDTO> querySpecialActivityAppointment(SpecialActivityPageRequest request) {
        return promotionSpecialActivityService.querySpecialActivityAppointment(request);
    }

    @Override
    public Boolean appointmentAdd(SpecialActivityAppointmentAddRequest appointmentAddForm) {
        QueryWrapper<PromotionSpecialAppointmentDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PromotionSpecialAppointmentDO::getSpecialActivityId, appointmentAddForm.getSpecialActivityId()).
                eq(PromotionSpecialAppointmentDO::getSpecialActivityEnterpriseId, appointmentAddForm.getSpecialActivityEnterpriseId()).eq(PromotionSpecialAppointmentDO::getAppointmentUserEid, appointmentAddForm.getAppointmentUserEid());
        PromotionSpecialAppointmentDO appointment = promotionSpecialAppointmentService.getOne(queryWrapper);
        if(!ObjectUtils.isEmpty(appointment)){
            throw new BusinessException(CouponActivityErrorCode.SPECIAL_ACTIVITY_ALREADY_APPOINTED);
        }
        PromotionSpecialAppointmentDO appointmentDO = new PromotionSpecialAppointmentDO();
        appointmentDO.setSpecialActivityId(appointmentAddForm.getSpecialActivityId());
        appointmentDO.setSpecialActivityEnterpriseId(appointmentAddForm.getSpecialActivityEnterpriseId());
        appointmentDO.setAppointmentUserEid(appointmentAddForm.getAppointmentUserEid());
        appointmentDO.setAppointmentUserId(appointmentAddForm.getAppointmentUserId());
        appointmentDO.setAppointmentTime(new Date());
        appointmentDO.setCreateTime(new Date());
        appointmentDO.setCreateUser(appointmentAddForm.getAppointmentUserId());
        appointmentDO.setUpdateTime(new Date());
        appointmentDO.setUpdateUser(appointmentAddForm.getAppointmentUserId());
        UserDTO userDTO = userApi.getById(appointmentAddForm.getAppointmentUserId());
        appointmentDO.setAppointmentUserName(userDTO.getName());
        appointmentDO.setDelFlag(0);
        return promotionSpecialAppointmentService.saveOrUpdate(appointmentDO);
    }

    @Override
    public Page<SpecialAvtivityAppointmentItemDTO> queryMyAppointment(Long currentEid, SpecialActivityPageRequest request) {
        Page<SpecialAvtivityAppointmentItemDTO> appointmentDOS = promotionSpecialAppointmentService.queryMyAppointment(currentEid, request);
        if (CollectionUtils.isEmpty(appointmentDOS.getRecords())) {
            return new Page<SpecialAvtivityAppointmentItemDTO>();
        }
        List<SpecialAvtivityAppointmentItemDTO> records = appointmentDOS.getRecords();

        // 专场活动表企业id集合
        List<Long> enterpriseIds = records.stream().map(SpecialAvtivityAppointmentItemDTO::getSpecialActivityEnterpriseId).collect(Collectors.toList());
        List<PromotionSpecialActivityEnterpriseDO> enterpriseDOList = enterpriseService.listByIds(enterpriseIds);
        Map<Long, PromotionSpecialActivityEnterpriseDO> enterpriseDOMap = enterpriseDOList.stream().collect(Collectors.toMap(PromotionSpecialActivityEnterpriseDO::getId, Function.identity()));
        //活动信息
        List<Long> activityIds = enterpriseDOList.stream().map(PromotionSpecialActivityEnterpriseDO::getPromotionActivityId).collect(Collectors.toList());
        List<PromotionActivityDO> activityDOList = promotionActivityService.listByIdList(activityIds);
        Map<Long, String> activityInfo = activityDOList.stream().collect(Collectors.toMap(PromotionActivityDO::getId, PromotionActivityDO::getName));

        records.forEach(item -> {
            // 营销活动名称
            PromotionSpecialActivityEnterpriseDO enterpriseDO = enterpriseDOMap.get(item.getSpecialActivityEnterpriseId());
            item.setEid(enterpriseDO.getEid());
            item.setPromotionName(activityInfo.get(enterpriseDO.getPromotionActivityId()));
            item.setPromotionActivityId(enterpriseDO.getPromotionActivityId());
            // 专场活动上传的图片
            item.setPic(enterpriseDO.getPic());
        });
        return appointmentDOS;
    }

    @Override
    public AvtivityCenterDTO activityCenter(Long currentEid) {
        return promotionSpecialActivityService.activityCenter(currentEid);
    }

    @Override
    public List<SpecialAvtivityInfoDTO> specialActivityInfo(SpecialActivityInfoRequest form) {
        return promotionSpecialActivityService.specialActivityInfo(form);
    }

    @Override
    public List<SpecialAvtivityGoodsInfoDTO> getGoodsInfoByActivityId(SpecialActivityInfoRequest request) {
        return promotionActivityService.getGoodsInfoByActivityId(request);
    }

    @Override
    public SpecialAvtivityAppointmentItemDTO myAppointmentCount(Long currentUserId) {
        return promotionActivityService.myAppointmentCount(currentUserId);
    }

    @Override
    public List<PromotionCombinationPackDTO> quaryCombinationPackByActivityIds(List<Long> combinationIds, Long buyerEid) {
        QueryWrapper<PromotionCombinationPackageDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(PromotionCombinationPackageDO::getPromotionActivityId, combinationIds);
        List<PromotionCombinationPackageDO> list = promotionCombinationPackageService.list(wrapper);
        List<PromotionCombinationPackDTO> packDTOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            packDTOS = PojoUtils.map(list, PromotionCombinationPackDTO.class);
            packDTOS.forEach(combinationPackageVO -> {
                CombinationBuyNumberBO combinationBuyNumberBO = orderPromotionActivityApi.sumCombinationActivityNumber(buyerEid, combinationPackageVO.getPromotionActivityId());
                log.info("sumCombinationActivityNumber" + JSONUtil.toJsonStr(combinationBuyNumberBO));
                if ((combinationBuyNumberBO.getBuyerQty() >= combinationPackageVO.getPerPersonNum()) && combinationPackageVO.getPerPersonNum() != 0) {
                    combinationPackageVO.setReachLimit(true);
                }
                if ((combinationBuyNumberBO.getSumQty() >= combinationPackageVO.getTotalNum()) && combinationPackageVO.getTotalNum() != 0) {
                    combinationPackageVO.setReachLimit(true);
                }
                if ((combinationBuyNumberBO.getBuyerDayQty() >= combinationPackageVO.getPerDayNum()) && combinationPackageVO.getPerDayNum() != 0) {
                    combinationPackageVO.setReachLimit(true);
                }
                if (combinationPackageVO.getTotalNum() == 0) {
                    combinationPackageVO.setSurplusBuyNum(-1);
                } else {
                    Long sumQty = combinationBuyNumberBO.getSumQty();
                    int combinationCanBuyNum = (combinationPackageVO.getTotalNum() - sumQty.intValue()) < 0 ? 0 : combinationPackageVO.getTotalNum() - sumQty.intValue();
                    combinationPackageVO.setSurplusBuyNum(combinationCanBuyNum);
                }
            });
        }
        return packDTOS;
    }


    @Override
    public PaymentActivityUseDTO getPaymentActivityUseList(QueryPaymentActivityRequest request) {
        return payPromotionHandler.getPaymentActivityUseList(request);
    }

    @Override
    public OrderUsePaymentActivityDTO orderUsePaymentActivityShareAmountBudget(OrderUsePaymentActivityRequest request) {
        return payPromotionHandler.orderUsePaymentActivityShareAmountBudget(request);
    }
}
