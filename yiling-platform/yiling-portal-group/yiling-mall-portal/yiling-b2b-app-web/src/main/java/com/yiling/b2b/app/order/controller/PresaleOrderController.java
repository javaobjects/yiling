package com.yiling.b2b.app.order.controller;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yiling.b2b.app.cart.util.SimpleGoodInfoUtils;
import com.yiling.b2b.app.deliveryAddress.vo.DeliveryAddressVO;
import com.yiling.b2b.app.order.form.PreSaleOrderQuickBuyForm;
import com.yiling.b2b.app.order.form.PresalOrderSubmitForm;
import com.yiling.b2b.app.order.vo.OrderDistributorVO;
import com.yiling.b2b.app.order.vo.OrderGoodsVO;
import com.yiling.b2b.app.order.vo.OrderSettlementPageVO;
import com.yiling.b2b.app.order.vo.PresaleActivityInfoVO;
import com.yiling.b2b.app.order.vo.SubmitResultVO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.GoodsSkuInfoDTO;
import com.yiling.goods.medicine.dto.GoodsSkuStandardBasicDTO;
import com.yiling.goods.medicine.enums.GoodsErrorCode;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.agreement.enums.GoodsLimitStatusEnum;
import com.yiling.mall.cart.dto.CartDTO;
import com.yiling.mall.cart.dto.QuickBuyGoodsDTO;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.dto.request.PresaleOrderSubmitRequest;
import com.yiling.marketing.common.enums.CouponPlatformTypeEnum;
import com.yiling.marketing.presale.api.PresaleActivityApi;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.marketing.presale.dto.request.QueryPresaleInfoRequest;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PreSaleActivityTypeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.user.enterprise.api.DeliveryAddressApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.DeliveryAddressDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryDeliveryAddressRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.PaymentMethodDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/** 预售订单模块
 * @author zhigang.guo
 * @date: 2022/10/12
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Api(tags = "预售订单模块")
public class PresaleOrderController extends BaseController {
    @DubboReference
    OrderProcessApi orderProcessApi;
    @DubboReference
    GoodsApi        goodsApi;
    @DubboReference
    EnterpriseApi   enterpriseApi;
    @DubboReference
    AgreementBusinessApi agreementBusinessApi;
    @DubboReference
    DeliveryAddressApi deliveryAddressApi;
    @DubboReference
    PresaleActivityApi presaleActivityApi;
    @Autowired
    PictureUrlUtils pictureUrlUtils;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisService redisService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 校验赠品数量是否满足购买规格包装
     * @param skuIdNumberMap
     */
    private void checkPackageNumber(Map<Long,Integer> skuIdNumberMap) {

        List<GoodsSkuDTO> skuDTOList =  goodsApi.getGoodsSkuByIds(new ArrayList(skuIdNumberMap.keySet()));
        if (CollectionUtil.isEmpty(skuDTOList)) {
            throw new BusinessException(OrderErrorCode.SKU_NOT_EXIST);
        }
        Map<Long,GoodsSkuDTO> skuDTOMap =  skuDTOList.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, Function.identity()));
        for (Long skuId :skuIdNumberMap.keySet()) {
            GoodsSkuDTO skuDTO = skuDTOMap.get(skuId);
            if (skuDTO == null) {
                throw new BusinessException(OrderErrorCode.SKU_NOT_EXIST);
            }
            if (!GoodsSkuStatusEnum.NORMAL.getCode().equals(skuDTO.getStatus())){
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_DISABLE);
            }
            Integer number = skuIdNumberMap.get(skuId);
            if (number % skuDTO.getPackageNumber() != 0) {
                throw new BusinessException(OrderErrorCode.SKU_PACKAGE_NUMBER_EXIST);
            }
        }
    }

    /**
     * 校验商品状态
     */
    private void validateGoodsStatus(List<Long> GoodsSkuIds) {

        List<GoodsSkuInfoDTO> allGoodsDTOList = goodsApi.batchQueryInfoBySkuIds(GoodsSkuIds);

        if (GoodsSkuIds.size() != allGoodsDTOList.size()) {

            throw new BusinessException(GoodsErrorCode.REMOVED);
        }

        Map<Long, GoodsSkuInfoDTO> allGoodsDTOMap = allGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuInfoDTO::getId, Function.identity()));
        for (Long goodsSkuIdsId : GoodsSkuIds) {
            GoodsSkuInfoDTO goodsDTO = allGoodsDTOMap.get(goodsSkuIdsId);
            if (GoodsStatusEnum.getByCode(goodsDTO.getGoodsInfo().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF) {
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_OFF_SHELF);
            }
            if (GoodsSkuStatusEnum.DISABLE.getCode().equals(goodsDTO.getStatus())){
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_DISABLE);
            }
            // 校验是否审核通过
            if (!GoodsStatusEnum.AUDIT_PASS.getCode().equals(goodsDTO.getGoodsInfo().getAuditStatus())){
                throw new BusinessException(OrderErrorCode.SUBMIT_GOODS_DISABLE);
            }
        }
    }

    /**
     * 构建快速购买订单数据
     * @param userData
     * @return
     */
    private QuickBuyGoodsDTO buildQuickBuyGood(String userData) {

        userData = userData.substring(1, userData.length() - 1);
        //去除转义符
        userData = userData.replaceAll("\\\\", "");
        // 获取预售订单信息
        QuickBuyGoodsDTO quickBuyGoodsDTO = JSONUtil.toBean(userData, QuickBuyGoodsDTO.class);

        return quickBuyGoodsDTO;
    }


    @ApiOperation(value = "购物车\"支付定金\"生成结算页信息")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    @RequestMapping(path = "/presale/verification", method = { org.springframework.web.bind.annotation.RequestMethod.POST, RequestMethod.GET })
    public Result<Void> verification(@CurrentUser CurrentStaffInfo staffInfo,@RequestBody @Valid PreSaleOrderQuickBuyForm form) {

        // 验证商品基本信息
        this.validateGoodsStatus(Collections.singletonList(form.getGoodsSkuId()));
        // 验证是否满足
        this.checkPackageNumber(MapUtil.of(form.getGoodsSkuId(),form.getQuantity()));
        // 校验管控商品
        this.checkB2BGoodsByGids(staffInfo.getCurrentEid(), Collections.singletonList(form.getDistributorGoodsId()));

        QuickBuyGoodsDTO quickBuyGoodsDTO = PojoUtils.map(form,QuickBuyGoodsDTO.class);
        quickBuyGoodsDTO.setPromotionActivityType(PromotionActivityTypeEnum.PRESALE.getCode());
        quickBuyGoodsDTO.setPlatformType(PlatformEnum.B2B.getCode());
        quickBuyGoodsDTO.setPromotionActivityId(form.getActivityId());

        // 校验预售活动数据正确性
        Result<Void> checkPresaleActivity = this.checkPresaleActivity(staffInfo.getCurrentEid(), quickBuyGoodsDTO);

        if (HttpStatus.HTTP_OK != checkPresaleActivity.getCode()) {

            return checkPresaleActivity;
        }

        String userSessionId = StringUtils.join("order:presale:",staffInfo.getCurrentEid());
        // 设置用户会话信息
        redisService.set(userSessionId,JSONUtil.toJsonStr(quickBuyGoodsDTO),60 * 60 * 8 );

        return Result.success();
    }


    /**
     * 查询商品预售活动信息
     * @param buyerEid 买家Id
     * @param distributorGoodsIds 商品Id
     * @param presaleActivityId 预售活动Id
     * @return
     */
    private List<PresaleActivityGoodsDTO> selectGoodsPresaleActivityInfo(Long buyerEid,List<Long> distributorGoodsIds,Long presaleActivityId) {

        QueryPresaleInfoRequest queryPresaleInfoRequest = new QueryPresaleInfoRequest();
        queryPresaleInfoRequest.setPlatformSelected(CouponPlatformTypeEnum.B2B.getCode());
        queryPresaleInfoRequest.setBuyEid(buyerEid);
        queryPresaleInfoRequest.setPresaleId(presaleActivityId);
        queryPresaleInfoRequest.setGoodsId(distributorGoodsIds);

        List<PresaleActivityGoodsDTO> presaleActivityGoodsDTOList = presaleActivityApi.getPresaleInfoByGoodsIdAndBuyEid(queryPresaleInfoRequest);

        if (log.isDebugEnabled()) {

            log.debug("..调用营销接口:getPresaleInfoByGoodsIdAndBuyEid...入参:{},返回参数:{}",queryPresaleInfoRequest,presaleActivityGoodsDTOList);
        }

        if (CollectionUtil.isEmpty(presaleActivityGoodsDTOList)) {

            throw new BusinessException(OrderErrorCode.SUBMIT_PRESALE_ORDER_ERROR);
        }

        return presaleActivityGoodsDTOList;

    }

    /**
     * 校验预售活动信息
     * @param buyerEid 买家Id
     * @param quickBuyGoodsDTO 快速购买商品信息
     * @return
     */
    private Result<Void> checkPresaleActivity(Long buyerEid,QuickBuyGoodsDTO quickBuyGoodsDTO) {

        List<PresaleActivityGoodsDTO> presaleActivityGoodsDTOList = this.selectGoodsPresaleActivityInfo(buyerEid, Collections.singletonList(quickBuyGoodsDTO.getDistributorGoodsId()), quickBuyGoodsDTO.getPromotionActivityId());

        // 目前预售只有一个商品
        PresaleActivityGoodsDTO presaleActivityGoodsDTO = presaleActivityGoodsDTOList.stream().findFirst().get();

        if (CompareUtil.compare(presaleActivityGoodsDTO.getMinNum(),0) != 0 &&  CompareUtil.compare(quickBuyGoodsDTO.getQuantity(),presaleActivityGoodsDTO.getMinNum()) < 0 ) {

            String tips = MessageFormat.format("最小预定量为{0},请重新修改!",presaleActivityGoodsDTO.getMinNum());

            return Result.failed(tips);
        }

        // 商品总计可购买最大数量
        Integer availableAllNum =  NumberUtil.sub(presaleActivityGoodsDTO.getAllNum(),presaleActivityGoodsDTO.getAllHasBuyNum()).intValue();
        // 每个人最大可够买数量
        Integer availableMaxNum =  NumberUtil.sub(presaleActivityGoodsDTO.getMaxNum(),presaleActivityGoodsDTO.getCurrentHasBuyNum()).intValue();

        if (CompareUtil.compare(presaleActivityGoodsDTO.getMaxNum(),0) != 0 && CompareUtil.compare(quickBuyGoodsDTO.getQuantity(),availableMaxNum) > 0) {

            String tips = MessageFormat.format("当前客户最多还可以预定{0},请调整商品购买数量!",(availableMaxNum < 0) ? 0 : availableMaxNum);

            return Result.failed(tips);
        }

        if (CompareUtil.compare(presaleActivityGoodsDTO.getAllNum(),0) != 0 && CompareUtil.compare(quickBuyGoodsDTO.getQuantity(),availableAllNum) > 0) {

            String tips = MessageFormat.format("当前活动最多还可以预定{0},请调整商品购买数量!",(availableAllNum < 0) ? 0 : availableAllNum);

            return Result.failed(tips);
        }

        quickBuyGoodsDTO.setPresaleActivityType(presaleActivityGoodsDTO.getPresaleType());

        return Result.success();

    }


    /**
     * 校验可控商品
     *
     * @param buyerEid
     * @param gidList
     */
    private void checkB2BGoodsByGids(Long buyerEid, List<Long> gidList) {
        log.info("checkB2BGoodsByGids request->{}", JSON.toJSON(gidList));
        Map<Long, Integer> goodsListResult = agreementBusinessApi.getB2bGoodsLimitByGids(gidList, buyerEid);
        log.info("checkB2BGoodsByGids result->{}", JSON.toJSON(goodsListResult));
        // 控销商品无法结算
        if (MapUtil.isEmpty(goodsListResult)) {
            throw new BusinessException(OrderErrorCode.LIMIT_GOODS_SALE_ERROR);
        }
        // 是否管控商品
        if (goodsListResult.values().stream().anyMatch(value -> GoodsLimitStatusEnum.CONTROL_GOODS == GoodsLimitStatusEnum.getByCode(value))) {
            throw new BusinessException(OrderErrorCode.LIMIT_GOODS_SALE_ERROR);
        }
        // 是否建立采购关系
        if (goodsListResult.values().stream().anyMatch(value -> GoodsLimitStatusEnum.NOT_RELATION_SHIP == GoodsLimitStatusEnum.getByCode(value))) {
            throw new BusinessException(OrderErrorCode.NOT_RELATION_SHIP_ERROR);
        }
    }


    /**
     * 查询企业收货地址信息，
     *
     * @param eid 企业ID
     * @return
     */
    private DeliveryAddressVO selectDeliveryAddressInfoList(Long eid) {

        QueryDeliveryAddressRequest deliveryAddressRequest = new QueryDeliveryAddressRequest();
        deliveryAddressRequest.setEid(eid);

        List<DeliveryAddressDTO> deliverAddressList = deliveryAddressApi.selectDeliveryAddressList(deliveryAddressRequest);

        if (CollUtil.isEmpty(deliverAddressList)) {

            return new DeliveryAddressVO();
        }
        List<DeliveryAddressDTO> collect = deliverAddressList.stream().filter(e -> e.getDefaultFlag() == 1).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(collect)) {
            return new DeliveryAddressVO();
        }

        return PojoUtils.map(collect.get(0), DeliveryAddressVO.class);
    }


    /**
     * 设置订单支付方式
     *
     * @param paymentMethodDTOList
     * @param orderDistributorVO
     */
    private void setPaymentMethod(List<PaymentMethodDTO> paymentMethodDTOList, Long choosePaymentMethodId, OrderDistributorVO orderDistributorVO) {
        orderDistributorVO.setPaymentMethodList(ListUtil.empty());
        List<OrderDistributorVO.PaymentMethodVO> paymentMethodVOList = Lists.newArrayList();
        for (PaymentMethodDTO e : paymentMethodDTOList) {
            OrderDistributorVO.PaymentMethodVO paymentMethodVO = new OrderDistributorVO.PaymentMethodVO();
            paymentMethodVO.setId(e.getCode());
            paymentMethodVO.setName(e.getName());
            paymentMethodVO.setEnabled(1 == e.getStatus());
            paymentMethodVO.setSelected(false);
            // 如果有企业方式，按照用户选择的企业方式
            if (choosePaymentMethodId != null && choosePaymentMethodId.equals(e.getCode().longValue())) {
                paymentMethodVO.setSelected(true);
                paymentMethodVOList.add(paymentMethodVO);
                continue;
            }
            // 默认在线支付
            if (choosePaymentMethodId == null && PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(e.getCode())) {
                paymentMethodVO.setSelected(true);
                paymentMethodVOList.add(paymentMethodVO);
                continue;
            }
            paymentMethodVOList.add(paymentMethodVO);
        }
        // 如果企业不存线上支付方式,从新设置一个线下默认方式
        if (!paymentMethodVOList.stream().filter(t -> t.getSelected()).findFirst().isPresent()) {
            paymentMethodVOList.stream().findFirst().ifPresent(e -> e.setSelected(true));
        }
        orderDistributorVO.setPaymentMethodList(paymentMethodVOList);
    }


    /**
     * @param distributorEid 配送商ID
     * @param distributorName 配送商名称
     * @param allDistributorGoodsDTOMap 商品库map
     * @param paymentMethodDTOList 支付集合
     * @param shopCustomerCouponId 店铺优惠劵ID
     * @param pictureMap 图片地址
     * @param yilingSubEids 以岭企业ID
     * @param goodsPriceMap 价格
     * @return
     */
    private OrderDistributorVO buildOrderDistributorVo(
            Long buyerEid,
            Long distributorEid,
            String distributorName,
            CartDTO cartInfo,
            Map<Long, GoodsSkuStandardBasicDTO> allDistributorGoodsDTOMap,
            List<PaymentMethodDTO> paymentMethodDTOList,
            Map<Long, String> pictureMap,
            List<Long> yilingSubEids,
            Integer getPaymentMethod,
            Long shopCustomerCouponId,
            String buyerMessage,
            Map<Long,PresaleActivityGoodsDTO> presaleActivityGoodsDTOMap
    ) {

        PresaleActivityGoodsDTO presaleActivityGoodsDTO = presaleActivityGoodsDTOMap.get(cartInfo.getDistributorGoodsId());

        // 预售默认只支持在线支付
        PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
        paymentMethodDTO.setCode(4l);
        paymentMethodDTO.setName("在线支付");
        paymentMethodDTO.setStatus(1);
        paymentMethodDTO.setType(1);

        paymentMethodDTOList.add(paymentMethodDTO);

        OrderDistributorVO orderDistributorVO = new OrderDistributorVO();
        // 设置支付方式
        this.setPaymentMethod(paymentMethodDTOList, Optional.ofNullable(getPaymentMethod).map(e -> e.longValue()).orElse(null), orderDistributorVO);
        List<OrderGoodsVO> orderGoodsVOList = CollUtil.newArrayList();
        BigDecimal amount = BigDecimal.ZERO;
        GoodsSkuStandardBasicDTO standardGoodsBasicDTO = allDistributorGoodsDTOMap.get(cartInfo.getGoodsSkuId());
        OrderGoodsVO orderGoodsVO = new OrderGoodsVO();
        // 拷贝商品标准库信息
        PojoUtils.map(SimpleGoodInfoUtils.toSimpleGoodsVO(standardGoodsBasicDTO.getStandardGoodsBasic()), orderGoodsVO);
        orderGoodsVO.setGoodsSkuId(cartInfo.getGoodsSkuId());
        //设置配送商商品ID
        orderGoodsVO.setGoodsId(cartInfo.getDistributorGoodsId());
        orderGoodsVO.setGoodPic(pictureUrlUtils.getGoodsPicUrl(pictureMap.get(cartInfo.getGoodsId())));
        orderGoodsVO.setGoodsQuantity(cartInfo.getQuantity());
        orderGoodsVO.setCartId(cartInfo.getId());
        // 促销活动类型
        orderGoodsVO.setPromotionActivityType(cartInfo.getPromotionActivityType() == null ? PromotionActivityTypeEnum.NORMAL.getCode() : cartInfo.getPromotionActivityType());
        // 预售商品金额
        orderGoodsVO.setPrice(presaleActivityGoodsDTO.getPresaleAmount());
        orderGoodsVO.setAmount(NumberUtil.round(NumberUtil.mul(orderGoodsVO.getPrice(), BigDecimal.valueOf(cartInfo.getQuantity())), 2));

        amount = amount.add(orderGoodsVO.getAmount());
        orderGoodsVOList.add(orderGoodsVO);
        orderDistributorVO.setDistributorEid(distributorEid);
        orderDistributorVO.setDistributorName(distributorName);
        orderDistributorVO.setOrderGoodsList(orderGoodsVOList);
        // 设置优惠小计金额
        orderDistributorVO.setPresaleDiscountAmount(this.calculateDistributorPreSaleDiscountAmount(orderDistributorVO,presaleActivityGoodsDTOMap));
        // 计算配送商的定金金额
        orderDistributorVO.setDepositAmount(this.calculateDistributorPreSaleDepositAmount(orderDistributorVO,presaleActivityGoodsDTOMap));
        orderDistributorVO.setGoodsSpeciesNum(orderGoodsVOList.stream().count());
        orderDistributorVO.setGoodsNum(orderGoodsVOList.stream().mapToLong(OrderGoodsVO::getGoodsQuantity).sum());
        orderDistributorVO.setTotalAmount(amount);
        orderDistributorVO.setFreightAmount(BigDecimal.ZERO);
        orderDistributorVO.setPaymentAmount(NumberUtil.sub(orderDistributorVO.getTotalAmount(), orderDistributorVO.getFreightAmount(),orderDistributorVO.getPresaleDiscountAmount()));
        orderDistributorVO.setYilingFlag(yilingSubEids.contains(distributorEid));
        orderDistributorVO.setShopCouponCount(0);
        orderDistributorVO.setIsUseShopCoupon(false);
        orderDistributorVO.setCustomerShopCouponId(shopCustomerCouponId);
        orderDistributorVO.setShopCouponActivity(Collections.emptyList());
        orderDistributorVO.setBuyerMessage(buyerMessage);

        return orderDistributorVO;
    }


    /**
     * 计算整个配送商的商品小计金额
     * @param orderDistributorVO
     * @param presaleActivityGoodsDTOMap
     * @return
     */
    private BigDecimal calculateDistributorPreSaleDiscountAmount(OrderDistributorVO orderDistributorVO, Map<Long,PresaleActivityGoodsDTO> presaleActivityGoodsDTOMap) {

        return  orderDistributorVO
                .getOrderGoodsList()
                .stream()
                .map(t -> this.calculateGoodsPresaleDiscountAmount(t,presaleActivityGoodsDTOMap.get(t.getGoodsId())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * 计算整个配送商的商品小计金额
     * @param orderDistributorVO
     * @param presaleActivityGoodsDTOMap
     * @return
     */
    private BigDecimal calculateDistributorPreSaleDepositAmount(OrderDistributorVO orderDistributorVO, Map<Long,PresaleActivityGoodsDTO> presaleActivityGoodsDTOMap) {

        return  orderDistributorVO
                .getOrderGoodsList()
                .stream()
                .map(t -> this.calculateGoodsDepositAmount(t,presaleActivityGoodsDTOMap.get(t.getGoodsId())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }


    /**
     * 计算每个明细的商品优惠金额
     * @param orderGoodsVO
     * @param presaleActivityGoodsDTO
     * @return
     */
    private BigDecimal calculateGoodsPresaleDiscountAmount(OrderGoodsVO orderGoodsVO,PresaleActivityGoodsDTO presaleActivityGoodsDTO){

        if (PromotionActivityTypeEnum.PRESALE != PromotionActivityTypeEnum.getByCode(orderGoodsVO.getPromotionActivityType())) {
            return BigDecimal.ZERO;
        }

        // 全款预售无优惠金额
        if (PreSaleActivityTypeEnum.FULL == PreSaleActivityTypeEnum.getByCode(presaleActivityGoodsDTO.getPresaleType())) {

            return BigDecimal.ZERO;
        }

        // 商品没有设置具体的优惠金额
        if (CompareUtil.compare(0,presaleActivityGoodsDTO.getGoodsPresaleType()) == 0) {

            return BigDecimal.ZERO;
        }

        // 尾款立减
        if (CompareUtil.compare(2,presaleActivityGoodsDTO.getGoodsPresaleType()) == 0) {

            return NumberUtil.round(NumberUtil.mul(presaleActivityGoodsDTO.getFinalPayDiscountAmount(),orderGoodsVO.getGoodsQuantity()),2);
        }

        // 定金膨胀
        if (CompareUtil.compare(1,presaleActivityGoodsDTO.getGoodsPresaleType()) == 0) {

            // 定金金额
            BigDecimal depositAmount = NumberUtil.round(NumberUtil.mul(orderGoodsVO.getAmount(),NumberUtil.div(presaleActivityGoodsDTO.getDepositRatio(),100)),2);
            // 定金膨胀金额
            BigDecimal expansionAmount = NumberUtil.round(NumberUtil.mul(depositAmount,presaleActivityGoodsDTO.getExpansionMultiplier()),2);

            // 实际优惠金额
            BigDecimal diffDiscountAmount = NumberUtil.sub(expansionAmount,depositAmount);

            if (log.isDebugEnabled()) {
                log.debug("商品{},定金膨胀计算值定金为:{},定金膨胀金额:{},实际优惠金额{}",orderGoodsVO.getGoodsId(),depositAmount,expansionAmount,diffDiscountAmount);
            }

            return diffDiscountAmount;
        }

        return BigDecimal.ZERO;
    }


    /**
     * 计算每个明细的商品定金金额
     * @param orderGoodsVO
     * @param presaleActivityGoodsDTO
     * @return
     */
    private BigDecimal calculateGoodsDepositAmount(OrderGoodsVO orderGoodsVO,PresaleActivityGoodsDTO presaleActivityGoodsDTO){

        if (PromotionActivityTypeEnum.PRESALE != PromotionActivityTypeEnum.getByCode(orderGoodsVO.getPromotionActivityType())) {

            return orderGoodsVO.getAmount();
        }

        // 全款预售
        if (PreSaleActivityTypeEnum.FULL == PreSaleActivityTypeEnum.getByCode(presaleActivityGoodsDTO.getPresaleType())) {

            return orderGoodsVO.getAmount();
        }

        // 定金金额
        BigDecimal depositAmount = NumberUtil.round(NumberUtil.mul(orderGoodsVO.getAmount(),NumberUtil.div(presaleActivityGoodsDTO.getDepositRatio(),100)),2);

        return depositAmount;
    }



    @ApiOperation(value = "\"预售订单完善\"补充留言信息,支付方式")
    @ApiResponses({
            @ApiResponse(code = 10350, message = "预售快速加购商品信息失效"),
    })
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    @RequestMapping(path = "/presale/perfect/", method = { org.springframework.web.bind.annotation.RequestMethod.POST, RequestMethod.GET })
    public Result<OrderSettlementPageVO> perfect(@CurrentUser CurrentStaffInfo staffInfo) {

        String userSessionId = "order:presale:" + staffInfo.getCurrentEid();
        String userData = stringRedisTemplate.opsForValue().get(userSessionId);

        if (StringUtils.isBlank(userData)) {

            throw new BusinessException(OrderErrorCode.SUBMIT_PRESALE_ORDER_ERROR);
        }

        QuickBuyGoodsDTO quickBuyGoodsDTO = buildQuickBuyGood(userData);
        List<GoodsSkuStandardBasicDTO> allDistributorGoodsDTOList = goodsApi.batchQueryStandardGoodsBasicBySkuIds(Collections.singletonList(quickBuyGoodsDTO.getGoodsSkuId()));

        if (CollectionUtil.isEmpty(allDistributorGoodsDTOList)) {

            throw new BusinessException(OrderErrorCode.SUBMIT_PRESALE_ORDER_ERROR);
        }

        Optional<GoodsSkuStandardBasicDTO> optional = allDistributorGoodsDTOList.stream().filter(e -> GoodsStatusEnum.getByCode(e.getStandardGoodsBasic().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF).findAny();

        if (optional.isPresent()) {
            return Result.failed(OrderErrorCode.SUBMIT_GOODS_OFF_SHELF);
        }

        Optional<GoodsSkuStandardBasicDTO> checkDisable = allDistributorGoodsDTOList.stream().filter(e -> !GoodsSkuStatusEnum.NORMAL.getCode().equals(e.getStatus())).findAny();

        if (checkDisable.isPresent()) {
            return Result.failed(OrderErrorCode.SUBMIT_GOODS_DISABLE);
        }
        // 商品信息字典
        Map<Long, GoodsSkuStandardBasicDTO> allDistributorGoodsDTOMap = allDistributorGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuStandardBasicDTO::getId, Function.identity()));

        // 转换最新商品Id
        GoodsSkuStandardBasicDTO goodsSkuStandardBasicDTO = allDistributorGoodsDTOMap.get(quickBuyGoodsDTO.getGoodsSkuId());
        quickBuyGoodsDTO.setDistributorGoodsId(goodsSkuStandardBasicDTO.getGoodsId());

        List<Long> distributorGoodsIds = Collections.singletonList(quickBuyGoodsDTO.getDistributorGoodsId());
        List<Long> goodsIds = Collections.singletonList(quickBuyGoodsDTO.getGoodsId());

        // 配送商字典
        List<EnterpriseDTO> distributorDTOList = enterpriseApi.listByIds(Collections.singletonList(quickBuyGoodsDTO.getDistributorEid()));
        Map<Long, EnterpriseDTO> distributorDTOMap = distributorDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        // 以岭企业列表
        List<Long> yilingSubEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        // 图片地址
        Map<Long, String> pictureMap = goodsApi.getPictureUrlMapByGoodsIds(goodsIds);

        List<PresaleActivityGoodsDTO> presaleActivityGoodsDTOList = this.selectGoodsPresaleActivityInfo(staffInfo.getCurrentEid(), distributorGoodsIds, quickBuyGoodsDTO.getPromotionActivityId());
        Map<Long,PresaleActivityGoodsDTO> presaleActivityGoodsDTOMap = presaleActivityGoodsDTOList.stream().collect(Collectors.toMap(PresaleActivityGoodsDTO::getGoodsId,Function.identity()));

        List<OrderDistributorVO> orderDistributorVOList = Lists.newArrayListWithExpectedSize(1);
        String distributorName = distributorDTOMap.get(quickBuyGoodsDTO.getDistributorEid()).getName();

        CartDTO cartDTO = new CartDTO();
        cartDTO.setBuyerEid(staffInfo.getCurrentEid());
        cartDTO.setSellerEid(quickBuyGoodsDTO.getDistributorEid());
        cartDTO.setDistributorEid(quickBuyGoodsDTO.getDistributorEid());
        cartDTO.setDistributorGoodsId(quickBuyGoodsDTO.getDistributorGoodsId());
        cartDTO.setGoodsSkuId(quickBuyGoodsDTO.getGoodsSkuId());
        cartDTO.setGoodsId(quickBuyGoodsDTO.getGoodsId());
        cartDTO.setQuantity(quickBuyGoodsDTO.getQuantity());
        cartDTO.setPromotionActivityType(quickBuyGoodsDTO.getPromotionActivityType());
        cartDTO.setPromotionActivityId(quickBuyGoodsDTO.getPromotionActivityId());
        cartDTO.setSelectedFlag(1);
        cartDTO.setPlatformType(quickBuyGoodsDTO.getPlatformType());
        cartDTO.setGoodSource(CartGoodsSourceEnum.B2B.getCode());

        OrderDistributorVO vo = this.buildOrderDistributorVo(staffInfo.getCurrentEid(), quickBuyGoodsDTO.getDistributorEid(), distributorName, cartDTO, allDistributorGoodsDTOMap, new ArrayList<>(), pictureMap, yilingSubEids,null, null, "",presaleActivityGoodsDTOMap);
        orderDistributorVOList.add(vo);

        OrderSettlementPageVO pageVO = new OrderSettlementPageVO();
        pageVO.setPlatfromCouponActivity(Collections.emptyList());
        pageVO.setPlatformCouponCount(0);
        pageVO.setIsUsePlatformCoupon(false);
        pageVO.setPlatformCouponDiscountAmount(BigDecimal.ZERO);
        pageVO.setShopCouponDiscountAmount(BigDecimal.ZERO);
        pageVO.setPlatfromCouponActivity(Collections.emptyList());
        pageVO.setCustomerPlatformCouponId(0l);
        pageVO.setIsHasPresaleActivity(true);
        // 设置预售优惠金额
        pageVO.setPresaleDiscountAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getPresaleDiscountAmount).reduce(BigDecimal::add).get());

        pageVO.setOrderDistributorList(orderDistributorVOList);
        pageVO.setGoodsSpeciesNum(orderDistributorVOList.stream().mapToLong(OrderDistributorVO::getGoodsSpeciesNum).sum());
        pageVO.setGoodsNum(orderDistributorVOList.stream().mapToLong(OrderDistributorVO::getGoodsNum).sum());
        pageVO.setTotalAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getTotalAmount).reduce(BigDecimal::add).get());
        pageVO.setFreightAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getFreightAmount).reduce(BigDecimal::add).get());
        // 设置为定金金额
        pageVO.setPaymentAmount(orderDistributorVOList.stream().map(OrderDistributorVO::getDepositAmount).reduce(BigDecimal::add).get());
        // 设置预售活动信息
        this.bulidPresaleActivityInfoVO(pageVO,presaleActivityGoodsDTOList);

        pageVO.setDeliveryAddressVO(this.selectDeliveryAddressInfoList(staffInfo.getCurrentEid()));

        return Result.success(pageVO);
    }


    /**
     *  设置预售活动信息
     * @param vo
     * @param presaleActivityGoodsDTOList
     */
    private void bulidPresaleActivityInfoVO(OrderSettlementPageVO vo,List<PresaleActivityGoodsDTO> presaleActivityGoodsDTOList) {
        PresaleActivityGoodsDTO presaleActivityGoodsDTO = presaleActivityGoodsDTOList.stream().findFirst().get();
        OrderGoodsVO orderGoodsVO =  vo.getOrderDistributorList().stream().findFirst().get().getOrderGoodsList().stream().findFirst().get();
        BigDecimal finalPayDiscountAmount = NumberUtil.round(NumberUtil.mul(presaleActivityGoodsDTO.getFinalPayDiscountAmount(),orderGoodsVO.getGoodsQuantity()),2);
        PresaleActivityInfoVO infoVO = new PresaleActivityInfoVO();
        infoVO.setPresaleType(presaleActivityGoodsDTO.getPresaleType());
        infoVO.setFinalPayBeginTime(presaleActivityGoodsDTO.getFinalPayBeginTime());
        infoVO.setFinalPayEndTime(presaleActivityGoodsDTO.getFinalPayEndTime());
        infoVO.setGoodsPresaleType(presaleActivityGoodsDTO.getGoodsPresaleType());
        infoVO.setExpansionMultiplier(presaleActivityGoodsDTO.getExpansionMultiplier());
        infoVO.setFinalPayDiscountAmount(finalPayDiscountAmount);
        // 定金
        infoVO.setDepositAmount(vo.getPaymentAmount());
        // 尾款
        infoVO.setBalanceAmount(NumberUtil.sub(vo.getTotalAmount(),infoVO.getDepositAmount(),vo.getPresaleDiscountAmount()));

        vo.setPresaleActivityInfoVO(infoVO);

    }


    @ApiOperation(value = "预售订单下单提交")
    @ApiResponses({
            @ApiResponse(code = 10350, message = "预售快速加购商品信息失效"),
    })
    @PostMapping("/presale/submit")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<SubmitResultVO> submit(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid PresalOrderSubmitForm form) {
        UserAgent userAgent = UserAgentUtil.parse(httpServletRequest.getHeader("User-Agent"));

        // 预售目前只有一个商品下单,暂时取第一个,后续扩展
        PresalOrderSubmitForm.DistributorPreOrderForm distributorPreOrderForm = form.getDistributorPreOrderList().stream().findFirst().get();

        PresaleOrderSubmitRequest request = PojoUtils.map(form,PresaleOrderSubmitRequest.class);
        request.setBuyerEid(staffInfo.getCurrentEid());
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        request.setOrderTypeEnum(OrderTypeEnum.B2B);
        request.setOrderSourceEnum(OrderSourceEnum.B2B_APP);
        request.setBuyerMessage(distributorPreOrderForm.getBuyerMessage());
        request.setPaymentMethod(distributorPreOrderForm.getPaymentMethod());
        request.setDistributorEid(distributorPreOrderForm.getDistributorEid());
        request.setIpAddress(IPUtils.getIp(httpServletRequest));
        request.setUserAgent(JSONObject.toJSONString(userAgent));

        OrderSubmitBO submitBO = orderProcessApi.preSaleOrderSubmit(request);

        // 在线支付订单(包含部分支付和未支付的)
        List<OrderDTO> onlineOrderList = submitBO.getOrderDTOList().stream()
                .filter(e -> PaymentStatusEnum.getByCode(e.getPaymentStatus()) == PaymentStatusEnum.UNPAID || // 未支付
                        PaymentStatusEnum.getByCode(e.getPaymentStatus()) == PaymentStatusEnum.PARTPAID )  // 部分支付
                .filter(e ->  PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue())) // 在线订单
                .collect(Collectors.toList());

        Boolean hasOnlinePay = false;
        BigDecimal payMoney = BigDecimal.ZERO;

        if (CollectionUtil.isNotEmpty(onlineOrderList)) {
            hasOnlinePay = true;
            payMoney = onlineOrderList.stream().map(OrderDTO::getPaymentAmount).reduce(BigDecimal::add).get();
        }

        List<String> orderNos = submitBO.getOrderDTOList().stream().map(OrderDTO::getOrderNo).collect(Collectors.toList());
        SubmitResultVO resultVO = SubmitResultVO.builder().orderCodeList(orderNos).payMoney(payMoney).hasOnlinePay(hasOnlinePay).payId(submitBO.getPayId()).build();

        return Result.success(resultVO);
    }

}
