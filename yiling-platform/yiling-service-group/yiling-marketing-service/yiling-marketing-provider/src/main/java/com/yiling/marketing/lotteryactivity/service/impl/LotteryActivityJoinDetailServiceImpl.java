package com.yiling.marketing.lotteryactivity.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDetailDTO;
import com.yiling.marketing.goodsgift.entity.GoodsGiftDO;
import com.yiling.marketing.goodsgift.service.GoodsGiftService;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityExpressCashBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityJoinDetailBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinDetailDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityReceiptInfoDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRewardSettingDTO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryActivityJoinDetailRequest;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryActivityReceiptInfoRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailNumberRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailRewardCountRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateCashRewardRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateLotteryActivityReceiptInfoRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDeliveryAddressDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityJoinDetailDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityJoinDetailMapper;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityReceiptInfoDO;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityCashStatusEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityCashTypeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityErrorCode;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityRewardTypeEnum;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityDeliveryAddressService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityJoinDetailService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityReceiptInfoService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRewardSettingService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityService;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.enterprise.api.DeliveryAddressApi;
import com.yiling.user.enterprise.dto.DeliveryAddressDTO;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 抽奖活动参与明细表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Slf4j
@Service
public class LotteryActivityJoinDetailServiceImpl extends BaseServiceImpl<LotteryActivityJoinDetailMapper, LotteryActivityJoinDetailDO> implements LotteryActivityJoinDetailService {

    @Autowired
    LotteryActivityReceiptInfoService lotteryActivityReceiptInfoService;
    @Autowired
    LotteryActivityRewardSettingService lotteryActivityRewardSettingService;
    @Autowired
    GoodsGiftService goodsGiftService;
    @Autowired
    LotteryActivityService lotteryActivityService;
    @Autowired
    LotteryActivityDeliveryAddressService activityDeliveryAddressService;

    @DubboReference
    LocationApi locationApi;

    @Override
    public Page<LotteryActivityJoinDetailDTO> queryJoinDetailPage(QueryJoinDetailPageRequest request) {
        // 查询参与次数明细分页
        QueryWrapper<LotteryActivityJoinDetailDO> wrapper = WrapperUtils.getWrapper(request);
        Page<LotteryActivityJoinDetailDO> joinDetailDOPage = this.page(request.getPage(), wrapper);

        // 过滤真实物品，查询收货信息
        Map<Long, LotteryActivityReceiptInfoDTO> receiptInfoDTOMap = MapUtil.newHashMap();
        List<LotteryActivityJoinDetailDO> realGoodJoinList = joinDetailDOPage.getRecords().stream().filter(lotteryActivityJoinDetailDO ->
                LotteryActivityRewardTypeEnum.getByCode(lotteryActivityJoinDetailDO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(realGoodJoinList)) {
            List<Long> detailIdList = realGoodJoinList.stream().map(BaseDO::getId).collect(Collectors.toList());

            List<LotteryActivityReceiptInfoDTO> receiptInfoDTOList = lotteryActivityReceiptInfoService.getByDetailIdList(detailIdList);
            receiptInfoDTOMap = receiptInfoDTOList.stream().collect(Collectors.toMap(LotteryActivityReceiptInfoDTO::getJoinDetailId, Function.identity(), (k1, k2) -> k2));

        }

        // 设置收货信息
        Page<LotteryActivityJoinDetailDTO> joinDetailDTOPage = PojoUtils.map(joinDetailDOPage, LotteryActivityJoinDetailDTO.class);
        Map<Long, LotteryActivityReceiptInfoDTO> finalReceiptInfoDTOMap = receiptInfoDTOMap;
        joinDetailDTOPage.getRecords().forEach(lotteryActivityJoinDetailDTO -> {
            LotteryActivityReceiptInfoDTO receiptInfoDTO = finalReceiptInfoDTOMap.get(lotteryActivityJoinDetailDTO.getId());
            lotteryActivityJoinDetailDTO.setLotteryActivityReceiptInfo(receiptInfoDTO);
        });

        // 设置奖品图片
        if (CollUtil.isNotEmpty(joinDetailDTOPage.getRecords())) {
            joinDetailDTOPage.getRecords().forEach(lotteryActivityJoinDetailDTO -> {
                if (LotteryActivityRewardTypeEnum.getByCode(lotteryActivityJoinDetailDTO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS
                        || LotteryActivityRewardTypeEnum.getByCode(lotteryActivityJoinDetailDTO.getRewardType()) == LotteryActivityRewardTypeEnum.VIRTUAL_GOODS) {
                    if (Objects.nonNull(lotteryActivityJoinDetailDTO.getRewardId()) && lotteryActivityJoinDetailDTO.getRewardId() != 0) {
                        GoodsGiftDO goodsGiftDO = goodsGiftService.getById(lotteryActivityJoinDetailDTO.getRewardId());
                        if (Objects.nonNull(goodsGiftDO)) {
                            lotteryActivityJoinDetailDTO.setRewardImg(goodsGiftDO.getPictureUrl());
                            lotteryActivityJoinDetailDTO.setRewardName(goodsGiftDO.getName());
                        }
                    }
                } else {
                    lotteryActivityJoinDetailDTO.setRewardImg(lotteryActivityService.getRewardImg(lotteryActivityJoinDetailDTO.getRewardType()));
                }
            });
        }

        return joinDetailDTOPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cashReward(UpdateCashRewardRequest request) {
        log.info("抽奖活动兑付奖品入参={}", JSONObject.toJSONString(request));
        if (LotteryActivityCashTypeEnum.getByCode(request.getCashType()) == LotteryActivityCashTypeEnum.ONE) {
            // 单个兑付
            if (Objects.isNull(request.getId()) || request.getId() == 0 ) {
                throw new BusinessException(ResultCode.PARAM_MISS);
            }

            // 更新参与明细为已兑付状态
            LotteryActivityJoinDetailDO joinDetailDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_JOIN_DETAIL_NOT_EXIST));
            if (LotteryActivityCashStatusEnum.HAD_CASH == LotteryActivityCashStatusEnum.getByCode(joinDetailDO.getStatus())) {
                throw new BusinessException(LotteryActivityErrorCode.CURRENT_REWARD_HAD_CASH);
            }
            if (LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS) {
                if (Objects.isNull(request.getActivityReceiptInfo())) {
                    throw new BusinessException(ResultCode.PARAM_MISS);
                }
            }

            LotteryActivityJoinDetailDO detailDO = new LotteryActivityJoinDetailDO();
            detailDO.setId(joinDetailDO.getId());
            detailDO.setStatus(LotteryActivityCashStatusEnum.HAD_CASH.getCode());
            detailDO.setCashDate(new Date());
            detailDO.setOpUserId(request.getOpUserId());
            this.updateById(detailDO);
            // 真实物品兑付：更新收件信息
            if (LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS) {
                LotteryActivityReceiptInfoDTO receiptInfoDTO = lotteryActivityReceiptInfoService.getByDetailId(joinDetailDO.getId());
                LotteryActivityReceiptInfoDO receiptInfoDO = PojoUtils.map(request.getActivityReceiptInfo(), LotteryActivityReceiptInfoDO.class);
                receiptInfoDO.setJoinDetailId(joinDetailDO.getId());
                String[] namesByCodes = locationApi.getNamesByCodes(receiptInfoDO.getProvinceCode(), receiptInfoDO.getCityCode(), receiptInfoDO.getRegionCode());
                receiptInfoDO.setProvinceName(namesByCodes[0]);
                receiptInfoDO.setCityName(namesByCodes[1]);
                receiptInfoDO.setRegionName(namesByCodes[2]);
                receiptInfoDO.setOpUserId(request.getOpUserId());
                if (Objects.nonNull(receiptInfoDTO)) {
                    receiptInfoDO.setId(receiptInfoDTO.getId());
                    lotteryActivityReceiptInfoService.updateById(receiptInfoDO);
                } else {
                    lotteryActivityReceiptInfoService.save(receiptInfoDO);
                }
            }
            log.info("抽奖活动单个兑付完成 兑付参与抽奖明细ID={}", request.getId());

        } else if (LotteryActivityCashTypeEnum.getByCode(request.getCashType()) == LotteryActivityCashTypeEnum.CURRENT) {
            // 查询出当前页的数据
            LambdaQueryWrapper<LotteryActivityJoinDetailDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(LotteryActivityJoinDetailDO::getLotteryActivityId, request.getLotteryActivityId());
            wrapper.orderByDesc(LotteryActivityJoinDetailDO::getLotteryTime);
            Page<LotteryActivityJoinDetailDO> joinDetailDOPage = this.page(request.getPage(), wrapper);

            // 更新为已兑付状态
            List<LotteryActivityJoinDetailDO> joinDetailDOList = joinDetailDOPage.getRecords().stream().filter(joinDetailDO ->
                    LotteryActivityCashStatusEnum.getByCode(joinDetailDO.getStatus()) == LotteryActivityCashStatusEnum.UN_CASH).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(joinDetailDOList)) {
                joinDetailDOList.forEach(lotteryActivityJoinDetailDO -> {
                    LotteryActivityJoinDetailDO joinDetailDO = new LotteryActivityJoinDetailDO();
                    joinDetailDO.setId(lotteryActivityJoinDetailDO.getId());
                    joinDetailDO.setStatus(LotteryActivityCashStatusEnum.HAD_CASH.getCode());
                    joinDetailDO.setCashDate(new Date());
                    joinDetailDO.setOpUserId(request.getOpUserId());
                    this.updateById(joinDetailDO);
                });
                log.info("抽奖活动兑付当前页完成 实际兑付的数量为={}", joinDetailDOList.size());
            }

        } else if (LotteryActivityCashTypeEnum.getByCode(request.getCashType()) == LotteryActivityCashTypeEnum.ALL) {
            // 兑付全部
            LambdaQueryWrapper<LotteryActivityJoinDetailDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(LotteryActivityJoinDetailDO::getLotteryActivityId, request.getLotteryActivityId());
            wrapper.eq(LotteryActivityJoinDetailDO::getStatus, LotteryActivityCashStatusEnum.UN_CASH.getCode());

            LotteryActivityJoinDetailDO joinDetailDO = new LotteryActivityJoinDetailDO();
            joinDetailDO.setStatus(LotteryActivityCashStatusEnum.HAD_CASH.getCode());
            joinDetailDO.setCashDate(new Date());
            joinDetailDO.setOpUserId(request.getOpUserId());

            this.update(joinDetailDO, wrapper);
            log.info("抽奖活动兑付全部完成");

        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCashInfo(UpdateLotteryActivityReceiptInfoRequest receiptInfoRequest) {
        LotteryActivityJoinDetailDO joinDetailDO = Optional.ofNullable(this.getById(receiptInfoRequest.getJoinDetailId())).orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_JOIN_DETAIL_NOT_EXIST));
        if (LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) != LotteryActivityRewardTypeEnum.REAL_GOODS) {
            throw new BusinessException(LotteryActivityErrorCode.NOT_REAL_GOOD_NOT_UPDATE_CASH);
        }

        LotteryActivityReceiptInfoDTO receiptInfoDTO = lotteryActivityReceiptInfoService.getByDetailId(receiptInfoRequest.getJoinDetailId());
        LotteryActivityReceiptInfoDO activityReceiptInfoDO = PojoUtils.map(receiptInfoRequest, LotteryActivityReceiptInfoDO.class);
        activityReceiptInfoDO.setJoinDetailId(receiptInfoRequest.getJoinDetailId());
        String[] namesByCodes = locationApi.getNamesByCodes(receiptInfoRequest.getProvinceCode(), receiptInfoRequest.getCityCode(), receiptInfoRequest.getRegionCode());
        activityReceiptInfoDO.setProvinceName(namesByCodes[0]);
        activityReceiptInfoDO.setCityName(namesByCodes[1]);
        activityReceiptInfoDO.setRegionName(namesByCodes[2]);

        if (Objects.nonNull(receiptInfoDTO)) {
            activityReceiptInfoDO.setId(receiptInfoDTO.getId());
            activityReceiptInfoDO.setOpUserId(receiptInfoRequest.getOpUserId());
            lotteryActivityReceiptInfoService.updateById(activityReceiptInfoDO);

        } else {
            activityReceiptInfoDO.setOpUserId(receiptInfoRequest.getOpUserId());
            lotteryActivityReceiptInfoService.save(activityReceiptInfoDO);
        }

        log.info("抽奖活动后台修改兑付信息完成 抽奖活动ID={} 参与明细ID={} 修改信息={}", joinDetailDO.getLotteryActivityId(), joinDetailDO.getId(), JSONObject.toJSONString(receiptInfoRequest));
        return true;
    }

    @Override
    public Map<Long, Long> getJoinDetailNumber(QueryJoinDetailNumberRequest request) {
        if (CollUtil.isEmpty(request.getLotteryActivityIdList())) {
            return MapUtil.newHashMap();
        }
        // 查询参与/中奖次数总数量
        LambdaQueryWrapper<LotteryActivityJoinDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(LotteryActivityJoinDetailDO::getLotteryActivityId, request.getLotteryActivityIdList());
        if (CollUtil.isNotEmpty(request.getNotInRewardTypeList())) {
            wrapper.notIn(LotteryActivityJoinDetailDO::getRewardType, request.getNotInRewardTypeList());
        }

        List<LotteryActivityJoinDetailDO> joinDetailDOList = this.list(wrapper);
        return joinDetailDOList.stream().collect(Collectors.groupingBy(LotteryActivityJoinDetailDO::getLotteryActivityId, Collectors.counting()));
    }

    @Override
    public List<LotteryActivityJoinDetailDTO> queryHitList(Long lotteryActivityId, Integer limit) {
        return baseMapper.queryHitList(lotteryActivityId, limit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addReceiptInfo(UpdateLotteryActivityReceiptInfoRequest request) {
        LotteryActivityJoinDetailDO joinDetailDO = Optional.ofNullable(this.getById(request.getJoinDetailId()))
                .orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_JOIN_DETAIL_NOT_EXIST));

        LotteryActivityReceiptInfoDTO receiptInfoDTO = lotteryActivityReceiptInfoService.getByDetailId(joinDetailDO.getId());
        if (Objects.nonNull(receiptInfoDTO)) {
            throw new BusinessException(LotteryActivityErrorCode.RECEIPT_INFO_EXIST);
        }
        if (LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) != LotteryActivityRewardTypeEnum.REAL_GOODS) {
            throw new BusinessException(LotteryActivityErrorCode.NOT_REAL_GOOD_NOT_UPDATE_CASH);
        }

        LotteryActivityReceiptInfoDO activityReceiptInfoDO = PojoUtils.map(request, LotteryActivityReceiptInfoDO.class);
        String[] namesByCodes = locationApi.getNamesByCodes(activityReceiptInfoDO.getProvinceCode(), activityReceiptInfoDO.getCityCode(), activityReceiptInfoDO.getRegionCode());
        activityReceiptInfoDO.setProvinceName(namesByCodes[0]);
        activityReceiptInfoDO.setCityName(namesByCodes[1]);
        activityReceiptInfoDO.setRegionName(namesByCodes[2]);
        log.info("B2B抽奖活动添加收货地址成功 抽奖活动ID={} 用户ID={} 地址信息={}", joinDetailDO.getLotteryActivityId(), joinDetailDO.getUid(), JSONObject.toJSONString(request));
        return lotteryActivityReceiptInfoService.save(activityReceiptInfoDO);
    }

    @Override
    public LotteryActivityJoinDetailBO getRewardDetail(Long joinDetailId) {
        LotteryActivityJoinDetailDO joinDetailDO = Optional.ofNullable(this.getById(joinDetailId))
                .orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_JOIN_DETAIL_NOT_EXIST));
        // 只有真实物品和虚拟物品才能查看奖品详情
        if (LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) != LotteryActivityRewardTypeEnum.REAL_GOODS
                && LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) != LotteryActivityRewardTypeEnum.VIRTUAL_GOODS) {
            throw new BusinessException(LotteryActivityErrorCode.REWARD_TYPE_NOT_SEE_DETAIL);
        }

        LotteryActivityJoinDetailBO joinDetailBO = PojoUtils.map(joinDetailDO, LotteryActivityJoinDetailBO.class);
        // 真实商品查询收货地址信息
        LotteryActivityReceiptInfoDTO receiptInfoDTO = lotteryActivityReceiptInfoService.getByDetailId(joinDetailId);
        if (Objects.nonNull(receiptInfoDTO)) {
            PojoUtils.map(receiptInfoDTO, joinDetailBO);
        }
        // 设置奖品图片
        if (LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS
                || LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) == LotteryActivityRewardTypeEnum.VIRTUAL_GOODS) {
            if (Objects.nonNull(joinDetailDO.getRewardId()) && joinDetailDO.getRewardId() != 0) {
                GoodsGiftDO goodsGiftDO = goodsGiftService.getById(joinDetailDO.getRewardId());
                if (Objects.nonNull(goodsGiftDO)) {
                    joinDetailBO.setRewardImg(goodsGiftDO.getPictureUrl());
                    joinDetailBO.setRewardName(goodsGiftDO.getName());
                }
            }
        } else {
            joinDetailBO.setRewardImg(lotteryActivityService.getRewardImg(joinDetailBO.getRewardType()));
        }


        return joinDetailBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addJoinDetail(AddLotteryActivityJoinDetailRequest request) {
        LotteryActivityJoinDetailDO joinDetailDO = PojoUtils.map(request, LotteryActivityJoinDetailDO.class);
        joinDetailDO.setStatus(LotteryActivityCashStatusEnum.UN_CASH.getCode());
        this.save(joinDetailDO);
        return joinDetailDO.getId();
    }

    @Override
    public LotteryActivityExpressCashBO getExpressOrCash(Long joinDetailId) {
        LotteryActivityJoinDetailDO joinDetailDO = Optional.ofNullable(this.getById(joinDetailId))
                .orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_JOIN_DETAIL_NOT_EXIST));
        if (LotteryActivityCashStatusEnum.getByCode(joinDetailDO.getStatus()) != LotteryActivityCashStatusEnum.HAD_CASH) {
            throw new BusinessException(LotteryActivityErrorCode.REWARD_STATUS_NOT_SEE_DETAIL);
        }
        // 只有真实物品和虚拟物品才能查看奖品详情
        if (LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) != LotteryActivityRewardTypeEnum.REAL_GOODS
                && LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) != LotteryActivityRewardTypeEnum.VIRTUAL_GOODS) {
            throw new BusinessException(LotteryActivityErrorCode.REWARD_TYPE_NOT_SEE_DETAIL);
        }

        LotteryActivityExpressCashBO expressCashBO = null;
        if (LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS) {
            LotteryActivityReceiptInfoDTO receiptInfoDTO = lotteryActivityReceiptInfoService.getByDetailId(joinDetailId);
            expressCashBO = PojoUtils.map(receiptInfoDTO, LotteryActivityExpressCashBO.class);
        } else if (LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) == LotteryActivityRewardTypeEnum.VIRTUAL_GOODS) {
            List<LotteryActivityRewardSettingDTO> settingDTOS = lotteryActivityRewardSettingService.getByLotteryActivityId(joinDetailDO.getLotteryActivityId());
            settingDTOS = settingDTOS.stream().filter(rewardSettingDTO -> rewardSettingDTO.getLevel().equals(joinDetailDO.getLevel()) && joinDetailDO.getRewardType().equals(rewardSettingDTO.getRewardType())).collect(Collectors.toList());
            expressCashBO = new LotteryActivityExpressCashBO();

            // 获取虚拟物品的卡号和密码
            if (CollUtil.isNotEmpty(settingDTOS)) {
                GoodsGiftDetailDTO goodsGifDetail = goodsGiftService.getGoodsGifDetail(settingDTOS.get(0).getRewardId());
                String cardNo = goodsGifDetail.getCardNo();
                String password = goodsGifDetail.getPassword();
                if (StrUtil.isNotEmpty(cardNo)) {
                    String[] cardArr = cardNo.split(",");
                    String[] passwordArr = new String[cardArr.length];
                    if (StrUtil.isNotEmpty(password)) {
                        passwordArr = password.split(",");
                    }

                    List<LotteryActivityExpressCashBO.CardInfo> cardInfoList = ListUtil.toList();
                    for (int i=0; i< cardArr.length; i++) {
                        LotteryActivityExpressCashBO.CardInfo cardInfo = new LotteryActivityExpressCashBO.CardInfo();
                        cardInfo.setCardNo(cardArr[i]);
                        cardInfo.setPassword(passwordArr.length >= i + 1 ? passwordArr[i] : null);
                        cardInfoList.add(cardInfo);
                    }
                    expressCashBO.setCardInfoList(cardInfoList);
                }
            }

        }

        return expressCashBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateReceiptInfo(UpdateLotteryActivityReceiptInfoRequest request) {
        LotteryActivityJoinDetailDO joinDetailDO = Optional.ofNullable(this.getById(request.getJoinDetailId()))
                .orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_JOIN_DETAIL_NOT_EXIST));

        if (!joinDetailDO.getStatus().equals(LotteryActivityCashStatusEnum.UN_CASH.getCode())) {
            throw new BusinessException(LotteryActivityErrorCode.HAD_CASH_NOT_UPDATE_RECEIPT);
        }
        if (LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) != LotteryActivityRewardTypeEnum.REAL_GOODS) {
            throw new BusinessException(LotteryActivityErrorCode.NOT_REAL_GOOD_NOT_UPDATE_CASH);
        }
        LotteryActivityReceiptInfoDTO receiptInfoDTO = lotteryActivityReceiptInfoService.getByDetailId(joinDetailDO.getId());
        if (Objects.isNull(receiptInfoDTO)) {
            throw new BusinessException(LotteryActivityErrorCode.RECEIPT_INFO_NOT_EXIST);
        }

        LotteryActivityReceiptInfoDO activityReceiptInfoDO = PojoUtils.map(request, LotteryActivityReceiptInfoDO.class);
        String[] namesByCodes = locationApi.getNamesByCodes(activityReceiptInfoDO.getProvinceCode(), activityReceiptInfoDO.getCityCode(), activityReceiptInfoDO.getRegionCode());
        activityReceiptInfoDO.setProvinceName(namesByCodes[0]);
        activityReceiptInfoDO.setCityName(namesByCodes[1]);
        activityReceiptInfoDO.setRegionName(namesByCodes[2]);

        LambdaQueryWrapper<LotteryActivityReceiptInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityReceiptInfoDO::getJoinDetailId, joinDetailDO.getId());
        lotteryActivityReceiptInfoService.update(activityReceiptInfoDO, wrapper);
        log.info("抽奖活动修改收货地址成功 抽奖活动ID={} 用户ID={} 地址信息={}", joinDetailDO.getLotteryActivityId(), joinDetailDO.getUid(), JSONObject.toJSONString(request));

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addToBReceiptInfo(AddLotteryActivityReceiptInfoRequest request) {
        // 校验收货地址是否已经存在了，存在了则不能再添加
        LotteryActivityReceiptInfoDTO receiptInfoDTO = lotteryActivityReceiptInfoService.getByDetailId(request.getJoinDetailId());
        if (Objects.nonNull(receiptInfoDTO)) {
            throw new BusinessException(LotteryActivityErrorCode.RECEIPT_INFO_EXIST);
        }
        // 校验只有真实物品才能进行添加收货地址
        LotteryActivityJoinDetailDO joinDetailDO = Optional.ofNullable(this.getById(request.getJoinDetailId()))
                .orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_JOIN_DETAIL_NOT_EXIST));
        if (LotteryActivityRewardTypeEnum.getByCode(joinDetailDO.getRewardType()) != LotteryActivityRewardTypeEnum.REAL_GOODS) {
            throw new BusinessException(LotteryActivityErrorCode.NOT_REAL_GOOD_NOT_UPDATE_CASH);
        }

        // 根据收货地址ID查询出收货地址信息
        LotteryActivityDeliveryAddressDO deliveryAddressDO = Optional.ofNullable(activityDeliveryAddressService.getById(request.getDeliveryAddressId())).orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.RECEIPT_INFO_NOT_EXIST));

        LotteryActivityReceiptInfoDO activityReceiptInfoDO = new LotteryActivityReceiptInfoDO();
        activityReceiptInfoDO.setJoinDetailId(request.getJoinDetailId());
        activityReceiptInfoDO.setContactor(deliveryAddressDO.getReceiver());
        activityReceiptInfoDO.setContactorPhone(deliveryAddressDO.getMobile());
        activityReceiptInfoDO.setProvinceCode(deliveryAddressDO.getProvinceCode());
        activityReceiptInfoDO.setProvinceName(deliveryAddressDO.getProvinceName());
        activityReceiptInfoDO.setCityCode(deliveryAddressDO.getCityCode());
        activityReceiptInfoDO.setCityName(deliveryAddressDO.getCityName());
        activityReceiptInfoDO.setRegionCode(deliveryAddressDO.getRegionCode());
        activityReceiptInfoDO.setRegionName(deliveryAddressDO.getRegionName());
        activityReceiptInfoDO.setAddress(deliveryAddressDO.getAddress());
        activityReceiptInfoDO.setOpUserId(request.getOpUserId());
        return lotteryActivityReceiptInfoService.save(activityReceiptInfoDO);
    }

    @Override
    public Integer getCurrentRewardCount(QueryJoinDetailRewardCountRequest request) {
        QueryWrapper<LotteryActivityJoinDetailDO> wrapper = WrapperUtils.getWrapper(request);
        wrapper.lambda().ge(LotteryActivityJoinDetailDO::getCreateTime, DateUtil.beginOfDay(new Date()));
        wrapper.lambda().le(LotteryActivityJoinDetailDO::getCreateTime, DateUtil.endOfDay(new Date()));
        return this.list(wrapper).stream().mapToInt(LotteryActivityJoinDetailDO::getRewardNumber).sum();
    }

}
