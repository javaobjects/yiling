package com.yiling.b2b.app.integral.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.app.integral.form.QueryIntegralRecordForm;
import com.yiling.b2b.app.integral.form.QueryUserSignRecordTurnPageForm;
import com.yiling.b2b.app.integral.form.RecentExchangeGoodsForm;
import com.yiling.b2b.app.integral.vo.GenerateUserSignRecordVO;
import com.yiling.b2b.app.integral.vo.IntegralExchangeGoodsDetailVO;
import com.yiling.b2b.app.integral.vo.IntegralExchangeGoodsItemVO;
import com.yiling.b2b.app.integral.vo.IntegralExchangeOrderDetailVO;
import com.yiling.b2b.app.integral.vo.IntegralExchangeOrderExpressVO;
import com.yiling.b2b.app.integral.vo.IntegralExchangeOrderItemVO;
import com.yiling.b2b.app.integral.vo.IntegralGiveUseRecordVO;
import com.yiling.b2b.app.integral.vo.IntegralInstructionConfigVO;
import com.yiling.b2b.app.integral.vo.MyIntegralVO;
import com.yiling.b2b.app.integral.vo.UserSignRecordCalendarVO;
import com.yiling.b2b.app.integral.vo.UserSignRecordDetailVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.marketing.goodsgift.api.GoodsGiftApi;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDTO;
import com.yiling.marketing.goodsgift.dto.GoodsGiftDetailDTO;
import com.yiling.marketing.integral.bo.ExchangeGoodsDetailBO;
import com.yiling.marketing.integral.dto.request.RecentExchangeGoodsRequest;
import com.yiling.marketing.integralmessage.api.IntegralExchangeMessageConfigApi;
import com.yiling.marketing.integralmessage.dto.IntegralExchangeMessageConfigDTO;
import com.yiling.marketing.integralmessage.dto.request.QueryIntegralMessageListRequest;
import com.yiling.user.enterprise.dto.request.QueryIntegralExchangeOrderPageRequest;
import com.yiling.user.integral.api.IntegralBehaviorApi;
import com.yiling.user.integral.api.IntegralExchangeGoodsApi;
import com.yiling.user.integral.api.IntegralExchangeOrderApi;
import com.yiling.user.integral.api.IntegralGiveRuleApi;
import com.yiling.user.integral.api.IntegralInstructionConfigApi;
import com.yiling.user.integral.api.IntegralRecordApi;
import com.yiling.user.integral.api.UserIntegralApi;
import com.yiling.user.integral.bo.GenerateUserSignRecordBO;
import com.yiling.user.integral.bo.IntegralExchangeGoodsItemBO;
import com.yiling.user.integral.bo.IntegralExchangeOrderItemBO;
import com.yiling.user.integral.bo.IntegralGiveUseRecordBO;
import com.yiling.user.integral.bo.UserSignRecordDetailBO;
import com.yiling.user.integral.dto.IntegralBehaviorDTO;
import com.yiling.user.integral.dto.IntegralExchangeOrderDTO;
import com.yiling.user.integral.dto.IntegralExchangeOrderReceiptInfoDTO;
import com.yiling.user.integral.dto.IntegralGiveRuleDTO;
import com.yiling.user.integral.dto.IntegralInstructionConfigDTO;
import com.yiling.user.integral.dto.request.QueryIntegralExchangeGoodsPageRequest;
import com.yiling.user.integral.dto.request.QueryIntegralRecordRequest;
import com.yiling.user.integral.dto.request.QueryUserSignRecordRequest;
import com.yiling.user.integral.dto.request.QueryUserSignRecordTurnPageRequest;
import com.yiling.user.integral.enums.IntegralGoodsTypeEnum;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;
import com.yiling.user.integral.enums.IntegralShelfStatusEnum;
import com.yiling.user.integral.enums.IntegralUserFlagEnum;
import com.yiling.user.integral.enums.UserIntegralChangeTypeEnum;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 积分接口 Controller
 *
 * @author: lun.yu
 * @date: 2023-01-16
 */
@Slf4j
@RestController
@RequestMapping("/integral")
@Api(tags = "积分接口")
public class IntegralController extends BaseController {

    @DubboReference
    UserIntegralApi userIntegralApi;
    @DubboReference
    IntegralGiveRuleApi integralGiveRuleApi;
    @DubboReference
    IntegralBehaviorApi integralBehaviorApi;
    @DubboReference
    IntegralExchangeMessageConfigApi integralExchangeMessageConfigApi;
    @DubboReference
    IntegralExchangeGoodsApi integralExchangeGoodsApi;
    @DubboReference
    MemberApi memberApi;
    @DubboReference
    GoodsGiftApi goodsGiftApi;
    @DubboReference
    com.yiling.marketing.integral.api.IntegralExchangeGoodsApi integralExchangeGoodsMarketApi;
    @DubboReference
    IntegralRecordApi integralRecordApi;
    @DubboReference
    IntegralInstructionConfigApi integralInstructionConfigApi;
    @DubboReference
    IntegralExchangeOrderApi integralExchangeOrderApi;

    @Autowired
    FileService fileService;

    @Value("${lottery.img.memberCoupon:https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/web/prd/html/img/vip.png}")
    private String memberCouponImg;
    @Value("${lottery.img.productCoupon:https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/web/prd/html/img/product.png}")
    private String productCouponImg;

    @ApiOperation(value = "获取我的积分首页")
    @GetMapping("/getMyIntegralPage")
    public Result<MyIntegralVO> getMyIntegralPage(@CurrentUser CurrentStaffInfo staffInfo) {
        MyIntegralVO myIntegralVO = new MyIntegralVO();
        // 获取积分值
        Integer integralValue = userIntegralApi.getUserIntegralByUid(staffInfo.getCurrentEid(), IntegralRulePlatformEnum.B2B.getCode());
        myIntegralVO.setIntegralValue(integralValue);

        // 获取积分发放规则
        List<IntegralGiveRuleDTO> currentValidRuleList = integralGiveRuleApi.getCurrentValidRule(IntegralRulePlatformEnum.B2B.getCode());
        if (CollUtil.isNotEmpty(currentValidRuleList)) {
            Map<Long, List<IntegralGiveRuleDTO>> map = currentValidRuleList.stream().collect(Collectors.groupingBy(IntegralGiveRuleDTO::getBehaviorId));
            List<MyIntegralVO.GetIntegralRule> getIntegralRuleList = ListUtil.toList();
            for (Long behaviorId : map.keySet()) {
                IntegralGiveRuleDTO giveRuleDTO = map.get(behaviorId).get(0);
                MyIntegralVO.GetIntegralRule getIntegralRule = new MyIntegralVO.GetIntegralRule();
                getIntegralRule.setRuleId(giveRuleDTO.getId());
                getIntegralRule.setRuleName(giveRuleDTO.getName());
                getIntegralRule.setDescription(giveRuleDTO.getDescription());
                getIntegralRule.setBehaviorId(giveRuleDTO.getBehaviorId());
                IntegralBehaviorDTO behaviorDTO = integralBehaviorApi.getById(giveRuleDTO.getBehaviorId());
                getIntegralRule.setBehaviorName(behaviorDTO.getName());
                getIntegralRule.setIcon(behaviorDTO.getIcon());
                getIntegralRuleList.add(getIntegralRule);
            }
            myIntegralVO.setGetIntegralRuleList(getIntegralRuleList);
        }

        // 积分兑换消息
        QueryIntegralMessageListRequest messageListRequest = new QueryIntegralMessageListRequest();
        messageListRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
        messageListRequest.setLimit(3);
        List<IntegralExchangeMessageConfigDTO> messageConfigDTOList = integralExchangeMessageConfigApi.queryList(messageListRequest);

        List<MyIntegralVO.IntegralExchangeMessageVO> exchangeMessageVOList = ListUtil.toList();
        messageConfigDTOList.forEach(integralExchangeMessageConfigDTO -> {
            MyIntegralVO.IntegralExchangeMessageVO exchangeMessageVO = new MyIntegralVO.IntegralExchangeMessageVO();
            exchangeMessageVO.setExchangeMessageId(integralExchangeMessageConfigDTO.getId());
            exchangeMessageVO.setExchangeMessageName(integralExchangeMessageConfigDTO.getTitle());
            exchangeMessageVO.setExchangeMessageIcon(fileService.getUrl(integralExchangeMessageConfigDTO.getIcon(), FileTypeEnum.INTEGRAL_MESSAGE_PICTURE));
            exchangeMessageVO.setLink(integralExchangeMessageConfigDTO.getLink());
            exchangeMessageVOList.add(exchangeMessageVO);
        });
        myIntegralVO.setIntegralExchangeMessageList(exchangeMessageVOList);

        return Result.success(myIntegralVO);
    }

    @ApiOperation(value = "获取兑换商品分页列表")
    @PostMapping("/queryExchangeGoodsPage")
    public Result<Page<IntegralExchangeGoodsItemVO>> queryExchangeGoodsPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryPageListForm form) {
        QueryIntegralExchangeGoodsPageRequest request = PojoUtils.map(form, QueryIntegralExchangeGoodsPageRequest.class);
        request.setShelfStatus(IntegralShelfStatusEnum.SHELF.getCode());
        request.setValidGoods(1);
        request.setOrderCond(2);
        Page<IntegralExchangeGoodsItemBO> exchangeGoodsItemBOPage = integralExchangeGoodsApi.queryListPage(request);

        // 获取真实物品和虚拟物品的商品图片
        List<Long> goodIdList = exchangeGoodsItemBOPage.getRecords().stream().filter(goodsItemBO -> goodsItemBO.getGoodsType().equals(IntegralGoodsTypeEnum.REAL_GOODS.getCode())
                || goodsItemBO.getGoodsType().equals(IntegralGoodsTypeEnum.VIRTUAL_GOODS.getCode())).map(IntegralExchangeGoodsItemBO::getGoodsId).distinct().collect(Collectors.toList());
        Map<Long, String> pictureMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(goodIdList)) {
            pictureMap = goodsGiftApi.listByListId(goodIdList).stream().collect(Collectors.toMap(BaseDTO::getId, GoodsGiftDTO::getPictureUrl));
        }

        Page<IntegralExchangeGoodsItemVO> page = PojoUtils.map(exchangeGoodsItemBOPage, IntegralExchangeGoodsItemVO.class);
        Map<Long, String> finalPictureMap = pictureMap;
        page.getRecords().forEach(exchangeGoodsItemVO -> {
            // 设置真实物品和虚拟物品图片地址
            if (IntegralGoodsTypeEnum.REAL_GOODS.getCode().equals(exchangeGoodsItemVO.getGoodsType())
                    || exchangeGoodsItemVO.getGoodsType().equals(IntegralGoodsTypeEnum.VIRTUAL_GOODS.getCode())) {
                String pictureUrl = finalPictureMap.get(exchangeGoodsItemVO.getGoodsId());
                if (StrUtil.isNotEmpty(pictureUrl)) {
                    exchangeGoodsItemVO.setPictureUrl(fileService.getUrl(pictureUrl, FileTypeEnum.MARKETING_GOODS_GIFT_PICTURE));
                }
            }
        });

        return Result.success(page);
    }

    @ApiOperation(value = "获取商品详情")
    @GetMapping("/getGoodsDetail")
    public Result<IntegralExchangeGoodsDetailVO> getGoodsDetail(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        ExchangeGoodsDetailBO exchangeGoodsDetailBO = integralExchangeGoodsMarketApi.getGoodsDetail(id);

        IntegralExchangeGoodsDetailVO goodsDetailVO = PojoUtils.map(exchangeGoodsDetailBO, IntegralExchangeGoodsDetailVO.class);
        if (StrUtil.isNotEmpty(goodsDetailVO.getPictureUrl()) && (IntegralGoodsTypeEnum.getByCode(goodsDetailVO.getGoodsType()) == IntegralGoodsTypeEnum.REAL_GOODS ||
                IntegralGoodsTypeEnum.getByCode(goodsDetailVO.getGoodsType()) == IntegralGoodsTypeEnum.VIRTUAL_GOODS)) {
            goodsDetailVO.setPictureUrl(fileService.getUrl(goodsDetailVO.getPictureUrl(), FileTypeEnum.MARKETING_GOODS_GIFT_PICTURE));
        }

        return Result.success(goodsDetailVO);
    }

    @ApiOperation(value = "立即兑换")
    @PostMapping("/exchange")
    @Log(title = "立即兑换", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> exchange(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid RecentExchangeGoodsForm form) {
        RecentExchangeGoodsRequest request = PojoUtils.map(form, RecentExchangeGoodsRequest.class);
        request.setPlatform(IntegralRulePlatformEnum.B2B.getCode());
        request.setUid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(integralExchangeGoodsMarketApi.exchange(request));
    }

    @ApiOperation(value = "获取我的积分值")
    @GetMapping("/getMyIntegral")
    public Result<Integer> getMyIntegral(@CurrentUser CurrentStaffInfo staffInfo) {
        Integer integralValue = Optional.ofNullable(userIntegralApi.getUserIntegralByUid(staffInfo.getCurrentEid(), IntegralRulePlatformEnum.B2B.getCode())).orElse(0);
        return Result.success(integralValue);
    }

    @ApiOperation(value = "积分明细")
    @PostMapping("/queryIntegralRecordPage")
    public Result<Page<IntegralGiveUseRecordVO>> queryIntegralRecordPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryIntegralRecordForm form) {
        QueryIntegralRecordRequest request = PojoUtils.map(form, QueryIntegralRecordRequest.class);
        request.setPlatform(IntegralRulePlatformEnum.B2B.getCode());
        request.setUid(staffInfo.getCurrentEid());
        Page<IntegralGiveUseRecordBO> giveUseRecordBOPage = integralRecordApi.queryListPage(request);

        Page<IntegralGiveUseRecordVO> voPage = PojoUtils.map(giveUseRecordBOPage, IntegralGiveUseRecordVO.class);
        voPage.getRecords().forEach(integralGiveUseRecordVO -> {
            String orderNo = "";
            if (UserIntegralChangeTypeEnum.getByCode(integralGiveUseRecordVO.getChangeType()) == UserIntegralChangeTypeEnum.ORDER_GIVE_INTEGRAL) {
                orderNo = integralGiveUseRecordVO.getOpRemark().substring(integralGiveUseRecordVO.getOpRemark().indexOf("订单号："));
            } else if (UserIntegralChangeTypeEnum.getByCode(integralGiveUseRecordVO.getChangeType()) == UserIntegralChangeTypeEnum.EXCHANGE_USE) {
                orderNo = integralGiveUseRecordVO.getOpRemark().substring(integralGiveUseRecordVO.getOpRemark().indexOf("兑换单号："));
            }
            integralGiveUseRecordVO.setOrderNo(orderNo);
        });
        return Result.success(voPage);
    }

    @ApiOperation(value = "获取积分规则")
    @GetMapping("/getInstructionConfig")
    public Result<IntegralInstructionConfigVO> getInstructionConfig(@CurrentUser CurrentStaffInfo staffInfo) {
        IntegralInstructionConfigDTO instructionConfigDTO = integralInstructionConfigApi.get(null, IntegralRulePlatformEnum.B2B.getCode());
        return Result.success(PojoUtils.map(instructionConfigDTO, IntegralInstructionConfigVO.class));
    }

    @ApiOperation(value = "我的兑换记录")
    @PostMapping("/queryExchangeRecordPage")
    public Result<Page<IntegralExchangeOrderItemVO>> queryExchangeRecordPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryPageListForm form) {
        QueryIntegralExchangeOrderPageRequest request = PojoUtils.map(form, QueryIntegralExchangeOrderPageRequest.class);
        request.setUid(staffInfo.getCurrentEid());
        Page<IntegralExchangeOrderItemBO> exchangeOrderItemBOPage = integralExchangeOrderApi.queryListPage(request);

        Page<IntegralExchangeOrderItemVO> voPage = PojoUtils.map(exchangeOrderItemBOPage, IntegralExchangeOrderItemVO.class);
        voPage.getRecords().forEach(integralExchangeOrderItemVO -> {
            if (integralExchangeOrderItemVO.getGoodsType().equals(IntegralGoodsTypeEnum.REAL_GOODS.getCode()) || integralExchangeOrderItemVO.getGoodsType().equals(IntegralGoodsTypeEnum.VIRTUAL_GOODS.getCode())) {
                String pictureUrl = goodsGiftApi.getOneById(integralExchangeOrderItemVO.getGoodsId()).getPictureUrl();
                integralExchangeOrderItemVO.setGoodsPictureUrl(fileService.getUrl(pictureUrl, FileTypeEnum.MARKETING_GOODS_GIFT_PICTURE));

            } else if (integralExchangeOrderItemVO.getGoodsType().equals(IntegralGoodsTypeEnum.GOODS_COUPON.getCode())) {
                // 商品优惠券
                integralExchangeOrderItemVO.setGoodsPictureUrl(productCouponImg);

            } else if (integralExchangeOrderItemVO.getGoodsType().equals(IntegralGoodsTypeEnum.MEMBER_COUPON.getCode())) {
                // 会员优惠券
                integralExchangeOrderItemVO.setGoodsPictureUrl(memberCouponImg);

            }
        });

        return Result.success(voPage);
    }

    @ApiOperation(value = "兑换明细")
    @GetMapping("/getExchangeDetail")
    public Result<IntegralExchangeOrderDetailVO> getExchangeDetail(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        IntegralExchangeOrderDTO exchangeOrderDTO = integralExchangeOrderApi.getById(id);

        IntegralExchangeOrderDetailVO exchangeOrderDetailVO = PojoUtils.map(exchangeOrderDTO, IntegralExchangeOrderDetailVO.class);
        if (exchangeOrderDetailVO.getGoodsType().equals(IntegralGoodsTypeEnum.REAL_GOODS.getCode()) || exchangeOrderDetailVO.getGoodsType().equals(IntegralGoodsTypeEnum.VIRTUAL_GOODS.getCode())) {
            String pictureUrl = goodsGiftApi.getOneById(exchangeOrderDetailVO.getGoodsId()).getPictureUrl();
            exchangeOrderDetailVO.setGoodsPictureUrl(fileService.getUrl(pictureUrl, FileTypeEnum.MARKETING_GOODS_GIFT_PICTURE));
            if (exchangeOrderDetailVO.getGoodsType().equals(IntegralGoodsTypeEnum.REAL_GOODS.getCode())) {
                // 存在物流信息时为已发货，否则为待发货
                IntegralExchangeOrderReceiptInfoDTO orderReceiptInfoDTO = integralExchangeOrderApi.getReceiptInfoByOrderId(exchangeOrderDetailVO.getId());
                if (Objects.nonNull(orderReceiptInfoDTO) && StrUtil.isNotEmpty(orderReceiptInfoDTO.getExpressOrderNo())) {
                    exchangeOrderDetailVO.setInstruction("已发货");
                    exchangeOrderDetailVO.setLogisticsFlag(true);
                } else {
                    exchangeOrderDetailVO.setInstruction("待发货");
                    exchangeOrderDetailVO.setLogisticsFlag(false);
                }

            } else {
                // 虚拟物品时为卡号密码
                GoodsGiftDetailDTO goodsGifDetail = goodsGiftApi.getGoodsGifDetail(exchangeOrderDTO.getGoodsId());
                String cardNo = goodsGifDetail.getCardNo();
                String password = goodsGifDetail.getPassword();
                if (StrUtil.isNotEmpty(cardNo)) {
                    String[] cardArr = cardNo.split(",");
                    String[] passwordArr = new String[cardArr.length];
                    if (StrUtil.isNotEmpty(password)) {
                        passwordArr = password.split(",");
                    }

                    List<IntegralExchangeOrderDetailVO.CardInfo> cardInfoList = ListUtil.toList();
                    for (int i=0; i< cardArr.length; i++) {
                        IntegralExchangeOrderDetailVO.CardInfo cardInfo = new IntegralExchangeOrderDetailVO.CardInfo();
                        cardInfo.setCardNo(cardArr[i]);
                        cardInfo.setPassword(passwordArr.length >= i + 1 ? passwordArr[i] : null);
                        cardInfoList.add(cardInfo);
                    }
                    exchangeOrderDetailVO.setCardInfoList(cardInfoList);
                }
            }

        } else if (exchangeOrderDetailVO.getGoodsType().equals(IntegralGoodsTypeEnum.GOODS_COUPON.getCode())) {
            // 商品优惠券
            exchangeOrderDetailVO.setGoodsPictureUrl(productCouponImg);
            exchangeOrderDetailVO.setInstruction("兑换成功时已发放至账户");
        } else if (exchangeOrderDetailVO.getGoodsType().equals(IntegralGoodsTypeEnum.MEMBER_COUPON.getCode())) {
            // 会员优惠券
            exchangeOrderDetailVO.setGoodsPictureUrl(memberCouponImg);
            exchangeOrderDetailVO.setInstruction("兑换成功时已发放至账户");
        }

        return Result.success(exchangeOrderDetailVO);
    }

    @ApiOperation(value = "查看物流")
    @GetMapping("/getExpress")
    public Result<IntegralExchangeOrderExpressVO> getExpress(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam("ID") @RequestParam("id") Long id) {
        IntegralExchangeOrderReceiptInfoDTO orderReceiptInfoDTO = integralExchangeOrderApi.getReceiptInfoByOrderId(id);
        return Result.success(PojoUtils.map(orderReceiptInfoDTO, IntegralExchangeOrderExpressVO.class));
    }

    @ApiOperation(value = "获取签到详情")
    @GetMapping("/getSignDetail")
    public Result<UserSignRecordDetailVO> getSignDetail(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam("规则ID") @RequestParam("giveRuleId") Long giveRuleId) {
        UserSignRecordDetailBO userSignRecordDetailBO = userIntegralApi.getSignDetail(giveRuleId, IntegralRulePlatformEnum.B2B.getCode(), staffInfo.getCurrentEid());
        return Result.success(PojoUtils.map(userSignRecordDetailBO, UserSignRecordDetailVO.class));
    }

    @ApiOperation(value = "积分签到页")
    @GetMapping("/sign")
    public Result<UserSignRecordCalendarVO> sign(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam("规则ID") @RequestParam("giveRuleId") Long giveRuleId) {
        QueryUserSignRecordRequest request = new QueryUserSignRecordRequest();
        request.setGiveRuleId(giveRuleId);
        request.setPlatform(IntegralRulePlatformEnum.B2B.getCode());
        request.setUid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        List<GenerateUserSignRecordBO> signRecordDetailBO = userIntegralApi.generateUserSignData(request);

        UserSignRecordCalendarVO recordCalendarVO = new UserSignRecordCalendarVO();
        recordCalendarVO.setUserSignRecordList(this.getUserSignRecordVOList(signRecordDetailBO));
        recordCalendarVO.setYear(DateUtil.year(new Date()));
        recordCalendarVO.setMonth(DateUtil.thisMonth() + 1);
        return Result.success(recordCalendarVO);
    }

    @ApiOperation(value = "上/下一页")
    @PostMapping("/turnPage")
    public Result<UserSignRecordCalendarVO> turnPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryUserSignRecordTurnPageForm form) {
        QueryUserSignRecordTurnPageRequest request = PojoUtils.map(form, QueryUserSignRecordTurnPageRequest.class);
        request.setPlatform(IntegralRulePlatformEnum.B2B.getCode());
        request.setUid(staffInfo.getCurrentEid());
        List<GenerateUserSignRecordBO> recordTurnPageBOList = userIntegralApi.userSignRecordTurnPage(request);

        UserSignRecordCalendarVO recordCalendarVO = new UserSignRecordCalendarVO();
        recordCalendarVO.setUserSignRecordList(this.getUserSignRecordVOList(recordTurnPageBOList));
        if (request.getMonth() == 1 && request.getTurnType() == 1) {
            recordCalendarVO.setYear(request.getYear() - 1);
        } else if (request.getMonth() == 12 && request.getTurnType() == 2) {
            recordCalendarVO.setYear(request.getYear() + 1);
        } else {
            recordCalendarVO.setYear(request.getYear());
        }

        if (request.getTurnType() == 1) {
            if (request.getMonth() == 1) {
                recordCalendarVO.setMonth(12);
            } else {
                recordCalendarVO.setMonth(request.getMonth() - 1);
            }

        } else if (request.getTurnType() == 2) {
            if (request.getMonth() == 12) {
                recordCalendarVO.setMonth(1);
            } else {
                recordCalendarVO.setMonth(request.getMonth() + 1);
            }
        }

        return Result.success(recordCalendarVO);
    }

    private List<GenerateUserSignRecordVO> getUserSignRecordVOList(List<GenerateUserSignRecordBO> recordTurnPageBOList) {
        List<GenerateUserSignRecordVO> recordTurnPageVOList = PojoUtils.map(recordTurnPageBOList, GenerateUserSignRecordVO.class);
        recordTurnPageVOList.forEach(userSignRecordTurnPageVO -> {
            Integer continueSignIntegral = userSignRecordTurnPageVO.getContinueSignIntegral() != null ? userSignRecordTurnPageVO.getContinueSignIntegral() : 0;
            Integer signIntegral = userSignRecordTurnPageVO.getSignIntegral() != null ? userSignRecordTurnPageVO.getSignIntegral() : 0;
            userSignRecordTurnPageVO.setSumIntegral(signIntegral + continueSignIntegral);
        });
        return recordTurnPageVOList;
    }

}
