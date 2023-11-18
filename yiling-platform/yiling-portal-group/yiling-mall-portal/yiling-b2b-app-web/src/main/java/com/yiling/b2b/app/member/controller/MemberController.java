package com.yiling.b2b.app.member.controller;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.app.common.vo.B2bAppBannerVO;
import com.yiling.b2b.app.member.form.CreateMemberOrderForm;
import com.yiling.b2b.app.member.form.GetMemberOrderAmountForm;
import com.yiling.b2b.app.member.form.QueryFrugalPageForm;
import com.yiling.b2b.app.member.vo.MemberBuyRecordVO;
import com.yiling.b2b.app.member.vo.MemberDetailVO;
import com.yiling.b2b.app.member.vo.MemberExpiredVO;
import com.yiling.b2b.app.member.vo.MemberFrugalVO;
import com.yiling.b2b.app.member.vo.MemberSimpleVO;
import com.yiling.b2b.app.member.vo.OrderMemberDiscountVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.mall.banner.api.BannerApi;
import com.yiling.mall.banner.dto.B2bAppBannerDTO;
import com.yiling.mall.banner.enums.B2BBannerLinkTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityTypeEnum;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponMemberActivityRuleDTO;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderMemberDiscountDTO;
import com.yiling.order.order.dto.request.QueryDiscountOrderBuyerEidPagRequest;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.request.CreatePayOrderRequest;
import com.yiling.sales.assistant.message.enums.SourceEnum;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.EnterpriseMemberApi;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberBuyRecordApi;
import com.yiling.user.member.api.MemberBuyStageApi;
import com.yiling.user.member.api.MemberOrderApi;
import com.yiling.user.member.bo.MemberCheckStandBO;
import com.yiling.user.member.bo.MemberEnterpriseBO;
import com.yiling.user.member.bo.MemberExpiredBO;
import com.yiling.user.member.dto.EnterpriseMemberDTO;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.MemberBuyStageDTO;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.MemberSimpleDTO;
import com.yiling.user.member.dto.request.MemberOrderRequest;
import com.yiling.user.member.dto.request.QueryBuyRecordRequest;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * B2B移动端-会员 Controller
 *
 * @author: lun.yu
 * @date: 2021/10/26
 */
@Slf4j
@RestController
@RequestMapping("/member")
@Api(tags = "会员接口")
public class MemberController extends BaseController {

    @DubboReference
    MemberApi memberApi;
    @DubboReference
    com.yiling.mall.member.api.MemberApi mallMemberApi;
    @DubboReference
    MemberBuyStageApi memberBuyStageApi;
    @DubboReference
    NoApi noApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    PayApi payApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    CouponActivityApi couponActivityApi;
    @DubboReference
    MemberBuyRecordApi memberBuyRecordApi;
    @DubboReference
    BannerApi bannerApi;
    @DubboReference
    EnterpriseMemberApi enterpriseMemberApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    ShopApi shopApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "获取头部会员列表")
    @GetMapping("/getMemberList")
    public Result<CollectionObject<MemberSimpleVO>> getMemberList(@CurrentUser CurrentStaffInfo currentStaffInfo) {
        List<MemberSimpleDTO> memberSimpleDTOList = memberApi.queryAllList();
        List<MemberSimpleVO> memberSimpleVOList = PojoUtils.map(memberSimpleDTOList, MemberSimpleVO.class);
        return Result.success(new CollectionObject<>(memberSimpleVOList));
    }

    @ApiOperation(value = "获取会员详情")
    @GetMapping("/getMember")
    public Result<MemberDetailVO> getMember(@CurrentUser CurrentStaffInfo currentStaffInfo, @ApiParam(value = "会员ID") @RequestParam(value = "id", required = false) Long id) {
        if (Objects.isNull(id) || id == 0L) {
            // 兼容旧版本
            List<MemberEnterpriseBO> memberEnterpriseBOS = enterpriseMemberApi.getMemberListByEid(currentStaffInfo.getCurrentEid());
            List<MemberEnterpriseBO> enterpriseBOList = memberEnterpriseBOS.stream().filter(MemberEnterpriseBO::getMemberFlag).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(enterpriseBOList)) {
                id = enterpriseBOList.get(0).getMemberId();
            } else {
                id = memberEnterpriseBOS.get(0).getMemberId();
            }
        }

        MemberDetailVO memberDetailVO = PojoUtils.map(memberApi.getCurrentMember(currentStaffInfo.getCurrentEid(), id), MemberDetailVO.class);
        memberDetailVO.setBgPicture(StrUtil.isNotEmpty(memberDetailVO.getBgPicture()) ? fileService.getUrl(memberDetailVO.getBgPicture(), FileTypeEnum.MEMBER_BACKGROUND_PICTURE) : memberDetailVO.getBgPicture());

        if (CollUtil.isNotEmpty(memberDetailVO.getMemberBuyStageList())) {
            memberDetailVO.getMemberBuyStageList().forEach(memberBuyStageVO ->
                    memberBuyStageVO.setPerDayPrice(memberBuyStageVO.getPrice().divide(BigDecimal.valueOf(memberBuyStageVO.getValidTime()), 2, BigDecimal.ROUND_HALF_UP)));
        }

        // 获取省钱金额
        List<MemberBuyRecordDTO> memberBuyRecordDTOS = memberBuyRecordApi.getMemberRecordListByEid(currentStaffInfo.getCurrentEid(), id);
        if (CollUtil.isNotEmpty(memberBuyRecordDTOS)) {
            QueryDiscountOrderBuyerEidPagRequest request = new QueryDiscountOrderBuyerEidPagRequest();
            request.setBuyerEid(currentStaffInfo.getCurrentEid());
            request.setStartTime(memberBuyRecordDTOS.get(0).getStartTime());
            request.setEndTime(new Date());
            BigDecimal amount = orderApi.getMemberOrderAllDiscountAmount(request);
            memberDetailVO.setFrugalAmount(amount);
        }

        return Result.success(memberDetailVO);
    }

    @ApiOperation(value = "获取会员活动图")
    @GetMapping("/getMemberBanner")
    public Result<CollectionObject<B2bAppBannerVO>> getMemberBanner(@CurrentUser CurrentStaffInfo currentStaffInfo) {
        List<B2bAppBannerDTO> memberBannerDtoList = bannerApi.listByScenarioAndSource(3, SourceEnum.B2B.getCode(), 30);
        List<B2bAppBannerVO> appBannerVOList = PojoUtils.map(memberBannerDtoList, B2bAppBannerVO.class);
        for (B2bAppBannerVO b2bAppBannerVO : appBannerVOList) {
            operateBanner(b2bAppBannerVO);
        }
        return Result.success(new CollectionObject<>(appBannerVOList));
    }

    /**
     * 优化兼容
     *
     * @param b2bAppBannerVO 返回给前端的数据实体
     */
    private void operateBanner(B2bAppBannerVO b2bAppBannerVO) {
        B2BBannerLinkTypeEnum bannerLinkTypeEnum = B2BBannerLinkTypeEnum.getByCode(b2bAppBannerVO.getLinkType());
        if (bannerLinkTypeEnum == B2BBannerLinkTypeEnum.SEARCH) {
            b2bAppBannerVO.setSearchKeywords(b2bAppBannerVO.getActivityLinks());
        } else if (bannerLinkTypeEnum == B2BBannerLinkTypeEnum.GOODS) {
            b2bAppBannerVO.setGoodsId(StringUtils.isNotEmpty(b2bAppBannerVO.getActivityLinks()) ? Long.parseLong(b2bAppBannerVO.getActivityLinks()) : 0L);
        } else if (bannerLinkTypeEnum == B2BBannerLinkTypeEnum.SHOP) {
            b2bAppBannerVO.setSellerEid(StringUtils.isNotEmpty(b2bAppBannerVO.getActivityLinks()) ? Long.parseLong(b2bAppBannerVO.getActivityLinks()) : 0L);
        }
    }

    @ApiOperation(value = "生成会员订单")
    @PostMapping("/createMemberOrder")
    public Result<String> createMemberOrder(@CurrentUser CurrentStaffInfo currentStaffInfo , @RequestBody @Valid CreateMemberOrderForm form) {
        MemberBuyStageDTO buyStageDTO = Optional.ofNullable(memberBuyStageApi.getById(form.getBuyStageId())).orElseThrow(() -> new BusinessException(UserErrorCode.BUY_STAGE_NOT_EXIST));
        form.setPayAmount(buyStageDTO.getPrice());
        MemberOrderRequest request = PojoUtils.map(form,MemberOrderRequest.class);
        request.setEid(currentStaffInfo.getCurrentEid());
        EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(currentStaffInfo.getCurrentEid())).orElse(new EnterpriseDTO());
        request.setEname(enterpriseDTO.getName());
        request.setOpUserId(currentStaffInfo.getCurrentUserId());
        request.setOrderNo(noApi.gen(NoEnum.MEMBER_ORDER_NO));

        if(Objects.nonNull(request.getPromoterId()) && request.getPromoterId() != 0){
            EnterpriseDTO dto = Optional.ofNullable(enterpriseApi.getById(request.getPromoterId())).orElseThrow(() -> new BusinessException(UserErrorCode.PROMOTER_ENTERPRISE_NOT_EXISTS));
            request.setPromoterName(dto.getName());
        }

        if(Objects.nonNull(request.getPromoterUserId()) && request.getPromoterUserId() != 0){
            UserDTO dto = Optional.ofNullable(userApi.getById(request.getPromoterUserId())).orElseThrow(() -> new BusinessException(UserErrorCode.EMPLOYEE_NOT_EXISTS));
            request.setPromoterUserName(dto.getName());
        }

        // 设置优惠券
        this.setCoupon(form, request);

        //生成会员订单信息
        MemberOrderDTO memberOrder = mallMemberApi.createMemberOrder(request);

        //调用创建支付订单接口生成交易订单ID
        CreatePayOrderRequest payOrderRequest = new CreatePayOrderRequest();
        payOrderRequest.setTradeType(TradeTypeEnum.MEMBER);
        // 设置收银台信息
        MemberCheckStandBO memberCheckStandBO = this.getMemberCheckStandBO(request);
        payOrderRequest.setContent(JSONObject.toJSONString(memberCheckStandBO));
        CreatePayOrderRequest.appOrderRequest appOrderRequest = new CreatePayOrderRequest.appOrderRequest();
        appOrderRequest.setAppOrderId(memberOrder.getId());
        appOrderRequest.setAppOrderNo(memberOrder.getOrderNo());
        appOrderRequest.setAmount(request.getPayAmount());
        appOrderRequest.setUserId(currentStaffInfo.getCurrentUserId());
        appOrderRequest.setBuyerEid(currentStaffInfo.getCurrentEid());
        appOrderRequest.setSellerEid(Constants.YILING_EID);
        List<CreatePayOrderRequest.appOrderRequest> list = ListUtil.toList(appOrderRequest);
        payOrderRequest.setAppOrderList(list);
        payOrderRequest.setOpUserId(currentStaffInfo.getCurrentUserId());
        log.info("生成会员订单接口请求创建支付交易订单参数：{}", JSONObject.toJSONString(payOrderRequest));
        Result<String> payOrder = payApi.createPayOrder(payOrderRequest);
        log.info("生成会员订单接口请求创建支付交易订单返回payId：{}，当前企业ID：{}", payOrder.getData(), currentStaffInfo.getCurrentEid());

        return Result.success(payOrder.getData());
    }

    private MemberCheckStandBO getMemberCheckStandBO(MemberOrderRequest request) {
        MemberCheckStandBO memberCheckStandBO = PojoUtils.map(request, MemberCheckStandBO.class);
        memberCheckStandBO.setPromoterName(Objects.nonNull(memberCheckStandBO.getPromoterName()) ? memberCheckStandBO.getPromoterName() : "");
        memberCheckStandBO.setPromoterUserName(Objects.nonNull(memberCheckStandBO.getPromoterUserName()) ? memberCheckStandBO.getPromoterUserName() : "");
        memberCheckStandBO.setOriginalPrice(Objects.nonNull(memberCheckStandBO.getOriginalPrice()) ? memberCheckStandBO.getOriginalPrice() : BigDecimal.ZERO);
        memberCheckStandBO.setDiscountAmount(Objects.nonNull(memberCheckStandBO.getDiscountAmount()) ? memberCheckStandBO.getDiscountAmount() : BigDecimal.ZERO);
        memberCheckStandBO.setPayAmount(Objects.nonNull(memberCheckStandBO.getPayAmount()) ? memberCheckStandBO.getPayAmount() : BigDecimal.ZERO);
        return memberCheckStandBO;
    }

    @ApiOperation(value = "获取使用优惠券后支付金额")
    @PostMapping("/getPayAmount")
    public Result<BigDecimal> getPayAmount(@CurrentUser CurrentStaffInfo currentStaffInfo, @RequestBody @Valid GetMemberOrderAmountForm form) {
        MemberBuyStageDTO buyStageDTO = Optional.ofNullable(memberBuyStageApi.getById(form.getBuyStageId())).orElseThrow(() -> new BusinessException(UserErrorCode.BUY_STAGE_NOT_EXIST));
        BigDecimal payAmount = buyStageDTO.getPrice();
        if (Objects.nonNull(form.getCouponId()) && form.getCouponId() != 0) {
            BigDecimal discountValue = this.getDiscountAmount(form.getCouponId(), payAmount);
            payAmount = payAmount.subtract(discountValue);
        }

        return Result.success(payAmount);
    }

    /**
     * 设置优惠券信息
     *
     * @param form
     * @param request
     */
    private void setCoupon(CreateMemberOrderForm form, MemberOrderRequest request) {
        // 获取优惠券活动信息
        request.setOriginalPrice(form.getPayAmount());
        if (Objects.nonNull(form.getCouponId()) && form.getCouponId() != 0) {
            BigDecimal discountValue = this.getDiscountAmount(form.getCouponId(), form.getPayAmount());
            request.setDiscountAmount(discountValue);
            request.setPayAmount(form.getPayAmount().subtract(discountValue));
            log.info("生成会员订单编号={} 使用优惠券ID={} 原价={} 优惠金额={} 支付金额={}", request.getOrderNo(), form.getCouponId(), form.getPayAmount(), discountValue, form.getPayAmount().subtract(discountValue));
        }
    }

    /**
     * 获取折扣金额
     *
     * @param couponId 优惠券Id
     * @param payAmount 原价
     * @return 折扣金额
     */
    private BigDecimal getDiscountAmount(Long couponId, BigDecimal payAmount) {

        CouponMemberActivityRuleDTO memberActivityRuleDTO = couponActivityApi.getMemberCouponActivityRulesById(couponId);
        if (Objects.isNull(memberActivityRuleDTO)) {
            throw new BusinessException(CouponErrorCode.COUPON_NULL_ERROR);
        }

        BigDecimal discountValue = BigDecimal.ZERO;
        if (memberActivityRuleDTO.getDiscountValue().compareTo(BigDecimal.ZERO) > 0) {

            if (memberActivityRuleDTO.getType().equals(CouponActivityTypeEnum.REDUCE.getCode())) {
                // 满减券
                discountValue = memberActivityRuleDTO.getDiscountValue();

            } else if (memberActivityRuleDTO.getType().equals(CouponActivityTypeEnum.DISCOUNT.getCode())) {
                // 满折券
                BigDecimal discountMax = memberActivityRuleDTO.getDiscountMax();
                BigDecimal discountPoint = memberActivityRuleDTO.getDiscountValue().divide(BigDecimal.valueOf(100));
                BigDecimal discountAfter = payAmount.multiply(discountPoint).setScale(2, BigDecimal.ROUND_HALF_UP);
                discountValue = payAmount.subtract(discountAfter);
                // 如果折扣金额大于最高优惠额度，则使用最高优惠金额作为折扣金额
                if (discountMax != null && discountMax.compareTo(BigDecimal.ZERO) > 0 && discountValue.compareTo(discountMax) > 0) {
                    discountValue = discountMax;
                    log.info("开通会员使用满折券 优惠券卡包ID={} 计算的折扣金额={} 最高优惠金额={}", couponId, payAmount.subtract(discountAfter), discountMax);
                }

            }
        }
        return discountValue;
    }

    @ApiOperation(value = "查看购买记录")
    @PostMapping("/queryBuyRecordListPage")
    public Result<Page<MemberBuyRecordVO>> queryBuyRecordListPage(@CurrentUser CurrentStaffInfo currentStaffInfo, @RequestBody @Valid QueryPageListForm form) {
        QueryBuyRecordRequest request = PojoUtils.map(form, QueryBuyRecordRequest.class);
        request.setEid(currentStaffInfo.getCurrentEid());
        Page<MemberBuyRecordVO> buyRecordVOPage = getMemberBuyRecordVOPage(request);

        return Result.success(buyRecordVOPage);
    }

    @Deprecated
    @ApiOperation(value = "查看购买记录（此接口为版本兼容接口，后续版本会弃用）")
    @GetMapping("/queryBuyRecordPage")
    public Result<Page<MemberBuyRecordVO>> queryBuyRecordPage(@CurrentUser CurrentStaffInfo currentStaffInfo) {
        QueryBuyRecordRequest request = new QueryBuyRecordRequest();
        request.setEid(currentStaffInfo.getCurrentEid());
        Page<MemberBuyRecordVO> buyRecordVOPage = getMemberBuyRecordVOPage(request);

        return Result.success(buyRecordVOPage);
    }

    private Page<MemberBuyRecordVO> getMemberBuyRecordVOPage(QueryBuyRecordRequest request) {
        Page<MemberBuyRecordDTO> buyRecordDtoPage = memberBuyRecordApi.queryBuyRecordListPage(request);

        // 根据会员订单号查询购买条件名称
        List<String> orderNoList = buyRecordDtoPage.getRecords().stream().map(MemberBuyRecordDTO::getOrderNo).collect(Collectors.toList());
        Map<String, String> stageNameMap = memberBuyStageApi.getStageNameByOrderNo(orderNoList);

        Page<MemberBuyRecordVO> buyRecordVOPage = PojoUtils.map(buyRecordDtoPage, MemberBuyRecordVO.class);
        buyRecordVOPage.getRecords().forEach(memberBuyRecordVO -> memberBuyRecordVO.setStageName(stageNameMap.get(memberBuyRecordVO.getOrderNo())));
        return buyRecordVOPage;
    }

    @ApiOperation(value = "获取省钱计算器头部内容")
    @GetMapping("/getFrugalContent")
    public Result<MemberFrugalVO> getFrugalContent(@CurrentUser CurrentStaffInfo currentStaffInfo, @ApiParam(value = "会员Id", required = true) @RequestParam("memberId") Long memberId) {
        MemberDTO memberDTO = memberApi.getById(memberId);
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(currentStaffInfo.getCurrentEid());

        // 计算会员天数
        long day = 0;

        List<MemberBuyRecordDTO> memberBuyRecordDTOS = memberBuyRecordApi.getMemberRecordListByEid(currentStaffInfo.getCurrentEid(), memberId);
        if (memberBuyRecordDTOS.size() == 0) {
            throw new BusinessException(UserErrorCode.MEMBER_BUY_RECORD_NOT_EXIST);
        } else if (memberBuyRecordDTOS.size() == 1) {
            // 计算会员天数
            day = DateUtil.betweenDay(memberBuyRecordDTOS.get(0).getStartTime(), new Date(), true);

        } else {
            Date now = new Date();
            for (MemberBuyRecordDTO buyRecordDTO : memberBuyRecordDTOS) {
                if (buyRecordDTO.getStartTime().before(now) && buyRecordDTO.getEndTime().before(now)) {
                    long current = DateUtil.betweenDay(buyRecordDTO.getStartTime(), buyRecordDTO.getEndTime(), true);
                    day += current;
                } else if (buyRecordDTO.getStartTime().before(now) && buyRecordDTO.getEndTime().after(now)) {
                    long current = DateUtil.betweenDay(buyRecordDTO.getStartTime(), new Date(), true);
                    day += current;
                    break;
                }
            }
        }

        // 获取省钱金额
        QueryDiscountOrderBuyerEidPagRequest request = new QueryDiscountOrderBuyerEidPagRequest();
        request.setBuyerEid(currentStaffInfo.getCurrentEid());
        request.setStartTime(memberBuyRecordDTOS.get(0).getStartTime());
        request.setEndTime(new Date());
        BigDecimal amount = orderApi.getMemberOrderAllDiscountAmount(request);

        MemberFrugalVO memberFrugalVO = new MemberFrugalVO();
        memberFrugalVO.setEname(enterpriseDTO.getName());
        memberFrugalVO.setMemberName(memberDTO.getName());
        memberFrugalVO.setFrugalAmount(amount);
        memberFrugalVO.setMemberDays((int) day);

        return Result.success(memberFrugalVO);
    }

    @ApiOperation(value = "省钱订单分页列表")
    @PostMapping("/queryFrugalPageList")
    public Result<Page<OrderMemberDiscountVO>> queryFrugalPageList(@CurrentUser CurrentStaffInfo currentStaffInfo, @RequestBody @Valid QueryFrugalPageForm form) {
        EnterpriseMemberDTO enterpriseMemberDTO = Optional.ofNullable(enterpriseMemberApi.getEnterpriseMember(currentStaffInfo.getCurrentEid(), form.getMemberId()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_MEMBER_NOT_EXIST));

        QueryDiscountOrderBuyerEidPagRequest request = PojoUtils.map(form, QueryDiscountOrderBuyerEidPagRequest.class);
        request.setBuyerEid(currentStaffInfo.getCurrentEid());
        request.setStartTime(enterpriseMemberDTO.getStartTime());
        request.setEndTime(new Date());
        Page<OrderMemberDiscountDTO> memberDiscountDTOPage = orderApi.getMemberOrderDiscountInfo(request);
        Page<OrderMemberDiscountVO> discountVOPage = PojoUtils.map(memberDiscountDTOPage, OrderMemberDiscountVO.class);
        if (CollUtil.isEmpty(memberDiscountDTOPage.getRecords())) {
            return Result.success(discountVOPage);
        }

        // 获取店铺logo
        List<Long> eidList = discountVOPage.getRecords().stream().map(OrderMemberDiscountVO::getSellerEid).distinct().collect(Collectors.toList());
        Map<Long, String> shopMap = shopApi.listShopByEidList(eidList).stream().collect(Collectors.toMap(ShopDTO::getShopEid, ShopDTO::getShopLogo, (k1, k2) -> k2));

        discountVOPage.getRecords().forEach(orderMemberDiscountVO -> orderMemberDiscountVO.setShopLogo(shopMap.get(orderMemberDiscountVO.getSellerEid())));

        return Result.success(discountVOPage);
    }

    @ApiOperation(value = "获取会员到期需要提醒列表")
    @GetMapping("/getMemberExpiredList")
    public Result<CollectionObject<MemberExpiredVO>> getMemberExpiredList(@CurrentUser CurrentStaffInfo currentStaffInfo) {
        List<MemberExpiredBO> expiredBOList = enterpriseMemberApi.getMemberExpiredList(currentStaffInfo.getCurrentEid(), currentStaffInfo.getCurrentUserId());
        return Result.success(new CollectionObject<>(PojoUtils.map(expiredBOList, MemberExpiredVO.class)));
    }


}
