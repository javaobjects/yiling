package com.yiling.marketing.integral.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.goodsgift.entity.GoodsGiftDO;
import com.yiling.marketing.goodsgift.service.GoodsGiftService;
import com.yiling.marketing.integral.bo.ExchangeGoodsDetailBO;
import com.yiling.marketing.integral.bo.IntegralExchangeGoodsDetailBO;
import com.yiling.marketing.integral.dao.IntegralExchangeGoodsMapper;
import com.yiling.marketing.integral.dto.request.QueryIntegralExchangeOrderRequest;
import com.yiling.marketing.integral.dto.request.RecentExchangeGoodsRequest;
import com.yiling.marketing.integral.entity.IntegralExchangeGoodsDO;
import com.yiling.marketing.integral.entity.IntegralExchangeOrderDO;
import com.yiling.marketing.integral.entity.IntegralExchangeOrderReceiptInfoDO;
import com.yiling.marketing.integral.service.IntegralBehaviorService;
import com.yiling.marketing.integral.service.IntegralExchangeGoodsMemberService;
import com.yiling.marketing.integral.service.IntegralExchangeGoodsService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.integral.service.IntegralExchangeOrderReceiptInfoService;
import com.yiling.marketing.integral.service.IntegralExchangeOrderService;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDeliveryAddressDO;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityDeliveryAddressService;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.integral.api.IntegralRecordApi;
import com.yiling.user.integral.api.IntegralUseRuleApi;
import com.yiling.user.integral.api.UserIntegralApi;
import com.yiling.user.integral.dto.IntegralBehaviorDTO;
import com.yiling.user.integral.dto.IntegralExchangeOrderDTO;
import com.yiling.user.integral.dto.request.AddIntegralRecordRequest;
import com.yiling.user.integral.dto.request.UpdateIUserIntegralRequest;
import com.yiling.user.integral.enums.IntegralBehaviorEnum;
import com.yiling.user.integral.enums.IntegralExchangeOrderStatusEnum;
import com.yiling.user.integral.enums.IntegralGoodsTypeEnum;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;
import com.yiling.user.integral.enums.IntegralShelfStatusEnum;
import com.yiling.user.integral.enums.IntegralUserFlagEnum;
import com.yiling.user.integral.enums.UserIntegralChangeTypeEnum;
import com.yiling.user.member.api.EnterpriseMemberApi;
import com.yiling.user.member.dto.MemberBuyRecordDTO;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分兑换商品表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-09
 */
@Slf4j
@Service
public class IntegralExchangeGoodsServiceImpl extends BaseServiceImpl<IntegralExchangeGoodsMapper, IntegralExchangeGoodsDO> implements IntegralExchangeGoodsService {

    @Autowired
    GoodsGiftService goodsGiftService;
    @Autowired
    CouponActivityService couponActivityService;
    @Autowired
    IntegralExchangeGoodsMemberService integralExchangeGoodsMemberService;
    @Autowired
    RedisDistributedLock redisDistributedLock;
    @Autowired
    IntegralExchangeOrderService integralExchangeOrderService;
    @Autowired
    IntegralExchangeOrderReceiptInfoService integralExchangeOrderReceiptInfoService;
    @Autowired
    LotteryActivityDeliveryAddressService activityDeliveryAddressService;
    @Autowired
    IntegralBehaviorService integralBehaviorService;

    @DubboReference
    UserIntegralApi userIntegralApi;
    @DubboReference
    IntegralRecordApi integralRecordApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    NoApi noApi;
    @DubboReference
    EnterpriseMemberApi enterpriseMemberApi;

    @Lazy
    @Autowired
    IntegralExchangeGoodsServiceImpl _this;

    @Value("${lottery.img.memberCoupon:https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/web/prd/html/img/vip.png}")
    private String memberCouponImg;
    @Value("${lottery.img.productCoupon:https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/web/prd/html/img/product.png}")
    private String productCouponImg;

    @Override
    public IntegralExchangeGoodsDetailBO getDetail(Long id) {
        IntegralExchangeGoodsDO exchangeGoodsDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.INTEGRAL_EXCHANGE_GOODS_NOT_EXIST));

        IntegralExchangeGoodsDetailBO exchangeGoodsDetailBO = PojoUtils.map(exchangeGoodsDO, IntegralExchangeGoodsDetailBO.class);

        if (IntegralGoodsTypeEnum.getByCode(exchangeGoodsDO.getGoodsType()) == IntegralGoodsTypeEnum.REAL_GOODS
                || IntegralGoodsTypeEnum.getByCode(exchangeGoodsDO.getGoodsType()) == IntegralGoodsTypeEnum.VIRTUAL_GOODS) {
            // 真实物品或虚拟物品
            GoodsGiftDO goodsGiftDO = Optional.ofNullable(goodsGiftService.getById(exchangeGoodsDO.getGoodsId())).orElse(new GoodsGiftDO());
            // 价值和可用库存
            exchangeGoodsDetailBO.setPrice(goodsGiftDO.getPrice());
            exchangeGoodsDetailBO.setAvailableQuantity(goodsGiftDO.getAvailableQuantity());

        } else if (IntegralGoodsTypeEnum.getByCode(exchangeGoodsDO.getGoodsType()) == IntegralGoodsTypeEnum.GOODS_COUPON
                || IntegralGoodsTypeEnum.getByCode(exchangeGoodsDO.getGoodsType()) == IntegralGoodsTypeEnum.MEMBER_COUPON ) {
            // 商品优惠券或会员优惠券
            CouponActivityDetailDTO activityDetailDTO = couponActivityService.getDetailById(exchangeGoodsDO.getGoodsId());
            if (Objects.nonNull(activityDetailDTO)) {
                // 面值和可用库存
                exchangeGoodsDetailBO.setPrice(activityDetailDTO.getDiscountValue());
                exchangeGoodsDetailBO.setAvailableQuantity(activityDetailDTO.getSurplusCount().longValue());
            }
        }

        if (IntegralUserFlagEnum.getByCode(exchangeGoodsDO.getUserFlag()) == IntegralUserFlagEnum.ASSIGN) {
            List<Long> memberIdList = integralExchangeGoodsMemberService.getByExchangeGoodsId(id);
            exchangeGoodsDetailBO.setMemberIdList(memberIdList);
        }

        return exchangeGoodsDetailBO;
    }

    @Override
    public ExchangeGoodsDetailBO getGoodsDetail(Long id) {
        IntegralExchangeGoodsDO exchangeGoodsDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.INTEGRAL_EXCHANGE_GOODS_NOT_EXIST));

        ExchangeGoodsDetailBO exchangeGoodsDetailBO = PojoUtils.map(exchangeGoodsDO, ExchangeGoodsDetailBO.class);
        // 赠品库
        if (exchangeGoodsDetailBO.getGoodsType().equals(IntegralGoodsTypeEnum.REAL_GOODS.getCode())
                || exchangeGoodsDetailBO.getGoodsType().equals(IntegralGoodsTypeEnum.VIRTUAL_GOODS.getCode())) {
            GoodsGiftDO goodsGiftDO = Optional.ofNullable(goodsGiftService.getById(exchangeGoodsDO.getGoodsId())).orElse(new GoodsGiftDO());
            exchangeGoodsDetailBO.setIntroduction(goodsGiftDO.getIntroduction());
            exchangeGoodsDetailBO.setPictureUrl(goodsGiftDO.getPictureUrl());
            exchangeGoodsDetailBO.setInventory(goodsGiftDO.getAvailableQuantity().intValue());
            exchangeGoodsDetailBO.setUnit(goodsGiftDO.getUnit());

        } else if (exchangeGoodsDetailBO.getGoodsType().equals(IntegralGoodsTypeEnum.GOODS_COUPON.getCode())
                || exchangeGoodsDetailBO.getGoodsType().equals(IntegralGoodsTypeEnum.MEMBER_COUPON.getCode())) {
            // 优惠券
            CouponActivityDO couponActivityDO = couponActivityService.getById(exchangeGoodsDO.getGoodsId());
            String couponRules = couponActivityService.buildCouponRules(PojoUtils.map(couponActivityDO, CouponActivityDTO.class));
            exchangeGoodsDetailBO.setDiscountInfo(couponRules);
            exchangeGoodsDetailBO.setUsePeriod(DateUtil.format(couponActivityDO.getBeginTime(), "yyyy-MM-dd HH:mm:ss") + " - " + DateUtil.format(couponActivityDO.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
            if (exchangeGoodsDetailBO.getGoodsType().equals(IntegralGoodsTypeEnum.GOODS_COUPON.getCode())) {
                exchangeGoodsDetailBO.setPictureUrl(productCouponImg);
            } else {
                exchangeGoodsDetailBO.setPictureUrl(memberCouponImg);
            }
            CouponActivityDetailDTO couponActivityDetailDTO = couponActivityService.getDetailById(exchangeGoodsDO.getGoodsId());
            exchangeGoodsDetailBO.setInventory(couponActivityDetailDTO.getSurplusCount());
            exchangeGoodsDetailBO.setUnit("张");
        }

        return exchangeGoodsDetailBO;
    }

    @Override
    public boolean exchange(RecentExchangeGoodsRequest request) {
        IntegralExchangeGoodsDO exchangeGoodsDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.INTEGRAL_EXCHANGE_GOODS_NOT_EXIST));
        // 校验
        this.validExchange(request, exchangeGoodsDO);

        // 开始兑换：扣减积分、扣减商品库存数量
        _this.exchangeTx(request, exchangeGoodsDO);

        return true;
    }

    @GlobalTransactional
    public void exchangeTx(RecentExchangeGoodsRequest request, IntegralExchangeGoodsDO exchangeGoodsDO) {
        // 1.扣减积分
        UpdateIUserIntegralRequest userIntegralRequest = new UpdateIUserIntegralRequest();
        userIntegralRequest.setPlatform(request.getPlatform());
        userIntegralRequest.setUid(request.getUid());
        Integer integral = exchangeGoodsDO.getExchangeUseIntegral() * request.getExchangeNum();
        userIntegralRequest.setIntegralValue(integral);
        userIntegralRequest.setChangeType(UserIntegralChangeTypeEnum.EXCHANGE_USE.getCode());
        userIntegralRequest.setOpUserId(request.getOpUserId());
        Integer integralValue = userIntegralApi.updateIntegral(userIntegralRequest);
        log.info("用户ID={} 平台={} 兑换消耗积分值={} 兑换后剩余积分值为={}", request.getUid(), request.getPlatform(), integral, integralValue);

        // 生成积分兑换订单
        Date now = new Date();
        IntegralExchangeOrderDO exchangeOrderDO = new IntegralExchangeOrderDO();
        exchangeOrderDO.setOrderNo(noApi.gen(NoEnum.INTEGRAL_ORDER_NO));
        exchangeOrderDO.setSubmitTime(now);
        exchangeOrderDO.setExchangeGoodsId(exchangeGoodsDO.getId());
        exchangeOrderDO.setGoodsType(exchangeGoodsDO.getGoodsType());
        exchangeOrderDO.setGoodsId(exchangeGoodsDO.getGoodsId());
        exchangeOrderDO.setGoodsName(exchangeGoodsDO.getGoodsName());
        exchangeOrderDO.setExchangeNum(request.getExchangeNum());
        exchangeOrderDO.setOrderIntegral(request.getExchangeNum() * exchangeGoodsDO.getExchangeUseIntegral());
        // 真实物品状态为未兑付
        if (IntegralGoodsTypeEnum.REAL_GOODS == IntegralGoodsTypeEnum.getByCode(exchangeGoodsDO.getGoodsType())) {
            exchangeOrderDO.setStatus(IntegralExchangeOrderStatusEnum.UN_CASH.getCode());
        } else {
            exchangeOrderDO.setStatus(IntegralExchangeOrderStatusEnum.HAD_CASH.getCode());
            exchangeOrderDO.setExchangeTime(now);
        }
        exchangeOrderDO.setPlatform(exchangeGoodsDO.getPlatform());
        exchangeOrderDO.setUid(request.getUid());
        exchangeOrderDO.setUname(Optional.ofNullable(enterpriseApi.getById(request.getUid())).orElse(new EnterpriseDTO()).getName());
        exchangeOrderDO.setOpUserId(request.getOpUserId());
        integralExchangeOrderService.save(exchangeOrderDO);

        // 积分发放/扣减记录
        AddIntegralRecordRequest integralRecordRequest = new AddIntegralRecordRequest();
        integralRecordRequest.setPlatform(request.getPlatform());
        integralRecordRequest.setUid(request.getUid());
        if (request.getPlatform().equals(IntegralRulePlatformEnum.B2B.getCode())) {
            integralRecordRequest.setUname(Optional.ofNullable(enterpriseApi.getById(request.getUid())).orElse(new EnterpriseDTO()).getName());
        }
        integralRecordRequest.setChangeType(UserIntegralChangeTypeEnum.EXCHANGE_USE.getCode());
        integralRecordRequest.setIntegralValue(integral);
        integralRecordRequest.setOpRemark(exchangeOrderDO.getOrderNo());
        IntegralBehaviorDTO behaviorDTO = integralBehaviorService.getByName(IntegralBehaviorEnum.EXCHANGE_USE.getName());
        integralRecordRequest.setBehaviorId(behaviorDTO.getId());
        integralRecordRequest.setBehaviorName(IntegralBehaviorEnum.EXCHANGE_USE.getName());
        integralRecordRequest.setOpUserId(request.getOpUserId());
        integralRecordApi.addRecord(integralRecordRequest);

        // 2.扣减商品库存数量
        if (exchangeGoodsDO.getGoodsType().equals(IntegralGoodsTypeEnum.REAL_GOODS.getCode())
                || exchangeGoodsDO.getGoodsType().equals(IntegralGoodsTypeEnum.VIRTUAL_GOODS.getCode())) {

            boolean deduct = goodsGiftService.deduct(request.getExchangeNum(), exchangeGoodsDO.getGoodsId());
            if (!deduct) {
                throw new BusinessException(UserErrorCode.EXCHANGE_GOODS_FAIL);
            }
            log.info("积分兑换商品扣减库存成功 商品ID={} 兑换数量={}", exchangeGoodsDO.getGoodsId(), request.getExchangeNum());

        } else if (exchangeGoodsDO.getGoodsType().equals(IntegralGoodsTypeEnum.GOODS_COUPON.getCode())
                || exchangeGoodsDO.getGoodsType().equals(IntegralGoodsTypeEnum.MEMBER_COUPON.getCode())) {

            // 商品优惠券/会员优惠券
            EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(request.getUid())).orElse(new EnterpriseDTO());
            List<SaveCouponRequest>  requests = ListUtil.toList();
            for (int i = 0; i < request.getExchangeNum(); i++) {
                SaveCouponRequest saveCouponRequest = new SaveCouponRequest();
                saveCouponRequest.setCouponActivityId(exchangeGoodsDO.getGoodsId());
                saveCouponRequest.setEid(request.getUid());
                saveCouponRequest.setEname(enterpriseDTO.getName());
                saveCouponRequest.setCouponActivityAutoId(exchangeGoodsDO.getId());
                saveCouponRequest.setCouponActivityAutoName(exchangeGoodsDO.getGoodsName());
                saveCouponRequest.setGetType(CouponGetTypeEnum.INTEGRAL_EXCHANGE.getCode());
                requests.add(saveCouponRequest);
            }
            boolean result = couponActivityService.giveCoupon(requests);
            if (!result) {
                throw new BusinessException(UserErrorCode.EXCHANGE_GOODS_FAIL);
            }
            log.info("用户ID={} 平台={} 积分兑换商品调用发放优惠券接口返回结果={} 参数={}", request.getUid(), request.getPlatform(), result, JSONObject.toJSONString(requests));

        }

        // 如果为真实物品还要存入收货信息
        if (exchangeGoodsDO.getGoodsType().equals(IntegralGoodsTypeEnum.REAL_GOODS.getCode()) && Objects.nonNull(request.getAddressId())) {
            LotteryActivityDeliveryAddressDO deliveryAddressDO = activityDeliveryAddressService.getById(request.getAddressId());
            if (Objects.nonNull(deliveryAddressDO)) {
                IntegralExchangeOrderReceiptInfoDO orderReceiptInfoDO = new IntegralExchangeOrderReceiptInfoDO();
                orderReceiptInfoDO.setOrderId(exchangeOrderDO.getId());
                orderReceiptInfoDO.setOrderNo(exchangeOrderDO.getOrderNo());
                orderReceiptInfoDO.setContactor(deliveryAddressDO.getReceiver());
                orderReceiptInfoDO.setContactorPhone(deliveryAddressDO.getMobile());
                orderReceiptInfoDO.setProvinceCode(deliveryAddressDO.getProvinceCode());
                orderReceiptInfoDO.setProvinceName(deliveryAddressDO.getProvinceName());
                orderReceiptInfoDO.setCityCode(deliveryAddressDO.getCityCode());
                orderReceiptInfoDO.setCityName(deliveryAddressDO.getCityName());
                orderReceiptInfoDO.setRegionCode(deliveryAddressDO.getRegionCode());
                orderReceiptInfoDO.setRegionName(deliveryAddressDO.getRegionName());
                orderReceiptInfoDO.setAddress(deliveryAddressDO.getAddress());
                orderReceiptInfoDO.setOpUserId(request.getOpUserId());
                integralExchangeOrderReceiptInfoService.save(orderReceiptInfoDO);
            }
        }

    }

    private void validExchange(RecentExchangeGoodsRequest request, IntegralExchangeGoodsDO exchangeGoodsDO) {
        // 校验积分数量
        Integer exchangeUseIntegral = exchangeGoodsDO.getExchangeUseIntegral();
        Integer exchangeNum = request.getExchangeNum();
        Integer userIntegral = userIntegralApi.getUserIntegralByUid(request.getUid(), IntegralRulePlatformEnum.B2B.getCode());
        if ( exchangeUseIntegral * exchangeNum > userIntegral) {
            throw new BusinessException(UserErrorCode.EXCHANGE_GOODS_INTEGRAL_LESS);
        }

        // 当前已经兑换的数量
        QueryIntegralExchangeOrderRequest exchangeOrderRequest = new QueryIntegralExchangeOrderRequest();
        exchangeOrderRequest.setExchangeGoodsId(exchangeGoodsDO.getId());
        int exchangeSum = integralExchangeOrderService.getExchangeOrderByCond(exchangeOrderRequest).stream().mapToInt(IntegralExchangeOrderDTO::getExchangeNum).sum();
        // 可兑换数量
        if (exchangeSum + request.getExchangeNum() > exchangeGoodsDO.getCanExchangeNum()) {
            throw new BusinessException(UserErrorCode.EXCHANGE_GOODS_INVENTORY_FAIL);
        }
        // 单品兑换限制（份/用户）
        if (exchangeGoodsDO.getSingleMaxExchange() > 0) {
            if (exchangeGoodsDO.getExchangeEndTime().compareTo(DateUtil.parse("1970-01-01 00:00:00")) != 0) {
                if (DateUtil.isIn(new Date(), exchangeGoodsDO.getExchangeStartTime(), exchangeGoodsDO.getExchangeEndTime())) {
                    this.checkSingleMaxExchange(request, exchangeGoodsDO);
                }
            } else {
                this.checkSingleMaxExchange(request, exchangeGoodsDO);
            }
        }
        // 有效期校验
        if (!DateUtil.isIn(new Date(), exchangeGoodsDO.getValidStartTime(), exchangeGoodsDO.getValidEndTime())) {
            throw new BusinessException(UserErrorCode.INTEGRAL_EXCHANGE_GOODS_OUT_VALID);
        }
        // 上下架状态
        if (exchangeGoodsDO.getShelfStatus().equals(IntegralShelfStatusEnum.SOLD_OUT.getCode())) {
            throw new BusinessException(UserErrorCode.INTEGRAL_EXCHANGE_GOODS_SOLD_OUT);
        }

        // 是否区用户身份
        if (IntegralUserFlagEnum.getByCode(exchangeGoodsDO.getUserFlag()) == IntegralUserFlagEnum.ASSIGN) {
            // 获取当前用户的会员信息
            List<Long> memberList = enterpriseMemberApi.getMemberByEid(request.getUid());
            // 获取设置区分的会员有哪些
            List<Long> memberIdList = integralExchangeGoodsMemberService.getByExchangeGoodsId(exchangeGoodsDO.getId());
            if (!CollUtil.containsAny(memberIdList, memberList)) {
                throw new BusinessException(UserErrorCode.INTEGRAL_EXCHANGE_GOODS_ONLY_MEMBER);
            }
        }

        // 赠品库
        if (exchangeGoodsDO.getGoodsType().equals(IntegralGoodsTypeEnum.REAL_GOODS.getCode())
                || exchangeGoodsDO.getGoodsType().equals(IntegralGoodsTypeEnum.VIRTUAL_GOODS.getCode())) {
            GoodsGiftDO goodsGiftDO = Optional.ofNullable(goodsGiftService.getById(exchangeGoodsDO.getGoodsId())).orElse(new GoodsGiftDO());
            // 校验库存数量
            if (goodsGiftDO.getAvailableQuantity() <= 0 || goodsGiftDO.getAvailableQuantity() < exchangeNum) {
                throw new BusinessException(UserErrorCode.EXCHANGE_GOODS_INVENTORY_FAIL);
            }

        } else if (exchangeGoodsDO.getGoodsType().equals(IntegralGoodsTypeEnum.GOODS_COUPON.getCode())
                || exchangeGoodsDO.getGoodsType().equals(IntegralGoodsTypeEnum.MEMBER_COUPON.getCode())) {
            // 优惠券
            CouponActivityDetailDTO couponActivityDetailDTO = couponActivityService.getDetailById(exchangeGoodsDO.getGoodsId());
            // 校验库存数量
            if (couponActivityDetailDTO.getSurplusCount() <= 0 || couponActivityDetailDTO.getSurplusCount() < exchangeNum) {
                throw new BusinessException(UserErrorCode.EXCHANGE_GOODS_INVENTORY_FAIL);
            }
        }

    }

    private void checkSingleMaxExchange(RecentExchangeGoodsRequest request, IntegralExchangeGoodsDO exchangeGoodsDO) {
        QueryIntegralExchangeOrderRequest exchangeOrderRequest = new QueryIntegralExchangeOrderRequest();
        exchangeOrderRequest.setPlatform(request.getPlatform());
        exchangeOrderRequest.setUid(request.getUid());
        exchangeOrderRequest.setExchangeGoodsId(request.getId());
        List<IntegralExchangeOrderDTO> exchangeOrderDTOS = integralExchangeOrderService.getExchangeOrderByCond(exchangeOrderRequest);
        int sum = exchangeOrderDTOS.stream().mapToInt(IntegralExchangeOrderDTO::getExchangeNum).sum();
        if (sum >= exchangeGoodsDO.getSingleMaxExchange() || (sum + request.getExchangeNum()) > exchangeGoodsDO.getSingleMaxExchange()) {
            throw new BusinessException(UserErrorCode.INTEGRAL_EXCHANGE_GOODS_HAVE_TOP);
        }
    }

    /**
     * 获取积分值分布式锁名称
     *
     * @param platform
     * @param uid
     * @return
     */
    private String getLockName(Integer platform, Long uid) {
        return "integral:integral_value" + ":" + platform + ":" + uid;
    }

}
