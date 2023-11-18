package com.yiling.sales.assistant.app.cart.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.GoodsSkuStandardBasicDTO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.agreement.enums.GoodsLimitStatusEnum;
import com.yiling.mall.cart.api.CartApi;
import com.yiling.mall.cart.dto.CartDTO;
import com.yiling.mall.cart.dto.request.AddToCartRequest;
import com.yiling.mall.cart.dto.request.BatchAddToCartRequest;
import com.yiling.mall.cart.dto.request.ListCartRequest;
import com.yiling.mall.cart.dto.request.RemoveCartGoodsRequest;
import com.yiling.mall.cart.dto.request.SelectCartGoodsRequest;
import com.yiling.mall.cart.dto.request.UpdateCartGoodsQuantityRequest;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;
import com.yiling.mall.cart.enums.CartGoodsStatusEnum;
import com.yiling.mall.cart.enums.CartIncludeEnum;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionAppListDTO;
import com.yiling.marketing.promotion.dto.request.PromotionAppCartGoods;
import com.yiling.marketing.promotion.dto.request.PromotionAppCartRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.sales.assistant.app.cart.form.AddToCartForm;
import com.yiling.sales.assistant.app.cart.form.RemoveCartGoodsForm;
import com.yiling.sales.assistant.app.cart.form.SelectCartGoodsForm;
import com.yiling.sales.assistant.app.cart.form.UpdateCartGoodsQuantityForm;
import com.yiling.sales.assistant.app.cart.util.SimpleGoodInfoUtils;
import com.yiling.sales.assistant.app.cart.util.StockUtils;
import com.yiling.sales.assistant.app.cart.vo.CartChooseCustomerVO;
import com.yiling.sales.assistant.app.cart.vo.CartDistributorVO;
import com.yiling.sales.assistant.app.cart.vo.CartGoodsNumVO;
import com.yiling.sales.assistant.app.cart.vo.CartGoodsVO;
import com.yiling.sales.assistant.app.cart.vo.CartListVO;
import com.yiling.sales.assistant.task.api.TaskApi;
import com.yiling.sales.assistant.task.dto.request.QueryTaskGoodsMatchRequest;
import com.yiling.sales.assistant.task.dto.request.TaskGoodsMatchListDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 进货单相关接口
 *
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.sales.assistant.app.order.controller
 * @date: 2021/9/14
 */
@RestController
@RequestMapping("/cart")
@Api(tags = "进货单接口")
@Slf4j
public class SaCartController extends BaseController {
    @DubboReference
    CartApi cartApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    PopGoodsApi popGoodsApi;
    @DubboReference
    GoodsPriceApi goodsPriceApi;
    @DubboReference
    InventoryApi inventoryApi;
    @DubboReference
    ShopApi shopApi;
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    OrderDetailApi orderDetailApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    AgreementBusinessApi agreementBusinessApi;
    @DubboReference
    TaskApi taskApi;
    @Autowired
    private FileService fileService;
    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @ApiOperation(value = "获取购物车中商品数量")
    @GetMapping("/getCartGoodsNum")
    @UserAccessAuthentication
    public Result<CartGoodsNumVO> getCartGoodsNum(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "选择客户ID", required = true) @RequestParam(value = "customerEid", required = true) Long customerEid) {
        if (staffInfo == null) {
            return Result.success(new CartGoodsNumVO(0));
        }
        CartGoodsSourceEnum sourceEnum = staffInfo.getYilingFlag() ? CartGoodsSourceEnum.POP : CartGoodsSourceEnum.B2B;
        ListCartRequest request = new ListCartRequest();
        request.setBuyerEid(customerEid);
        request.setPlatformEnum(PlatformEnum.SALES_ASSIST);
        request.setGoodsSourceEnum(sourceEnum);
        request.setCreateUser(staffInfo.getCurrentUserId());

        Integer cartNum = cartApi.getCartGoodsNumByCreateUser(request);

        return Result.success(new CartGoodsNumVO(cartNum));
    }

    @ApiOperation(value = "添加商品到购物车")
    @PostMapping("/add")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<Void> add(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddToCartForm form) {
        AddToCartRequest request = PojoUtils.map(form, AddToCartRequest.class);
        request.setBuyerEid(form.getCustomerEid());
        CartGoodsSourceEnum goodsSourceEnum = staffInfo.getYilingFlag() ? CartGoodsSourceEnum.POP : CartGoodsSourceEnum.B2B;
        request.setGoodsSourceEnum(goodsSourceEnum);
        request.setPlatformEnum(PlatformEnum.SALES_ASSIST);
        request.setOpUserId(staffInfo.getCurrentUserId());
        String lockName = RedisKey.generate("cart", PlatformEnum.SALES_ASSIST.getCode().toString(), "add", form.getCustomerEid().toString(), form.getGoodsSkuId().toString());
        String lockId = null;
        boolean result;
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 60, TimeUnit.SECONDS);
            result = cartApi.addToCart(request);
        } finally {
            redisDistributedLock.releaseLock(lockName,lockId);
        }
        if (result) {
            return Result.success();
        } else {
            return Result.failed("添加失败!");
        }
    }


    /**
     * 构建配送商商品集合
     *
     * @param distributorEid 配送商Id
     * @param distributorName 配送商名称
     * @param cartGoodsVOList 商品结果集合
     * @return
     */
    private CartDistributorVO buildCartDistributorVo(Long distributorEid, String distributorName, List<CartGoodsVO> cartGoodsVOList, ShopDTO shopOne, List<PromotionAppListDTO> promotionAppList) {

        CartDistributorVO cartDistributorVO = new CartDistributorVO();
        cartDistributorVO.setDistributorEid(distributorEid);
        cartDistributorVO.setDistributorName(distributorName);
        Optional optional = cartGoodsVOList.stream().filter(e -> e.getSelected()).findAny();
        if (optional.isPresent()) {
            cartDistributorVO.setSelectedGoodsNum(cartGoodsVOList.stream().filter(e -> e.getSelected()).count());
            cartDistributorVO.setSelectedGoodsAmount(cartGoodsVOList.stream().filter(e -> e.getSelected()).map(CartGoodsVO::getAmount).reduce(BigDecimal::add).get());
        } else {
            cartDistributorVO.setSelectedGoodsNum(0L);
            cartDistributorVO.setSelectedGoodsAmount(BigDecimal.ZERO);
        }
        cartDistributorVO.setIsMatchShopDistribute(false);
        //设置起配金额
        if (shopOne == null) {
            cartDistributorVO.setStartPassAmount(BigDecimal.ZERO);
            cartDistributorVO.setPassRemainAmount(BigDecimal.ZERO);
        } else {
            cartDistributorVO.setStartPassAmount(shopOne.getStartAmount());
            cartDistributorVO.setPassRemainAmount(shopOne.getStartAmount().compareTo(cartDistributorVO.getSelectedGoodsAmount()) > 0 ? shopOne.getStartAmount().subtract(cartDistributorVO.getSelectedGoodsAmount()) : BigDecimal.ZERO);
        }

        if (CompareUtil.compare(cartDistributorVO.getPassRemainAmount(), BigDecimal.ZERO) == 0) {
            cartDistributorVO.setIsMatchShopDistribute(true);
        }

        cartDistributorVO.setCartGoodsList(cartGoodsVOList);
        cartDistributorVO.setSelectEnabled(cartGoodsVOList.stream().filter(e -> e.getSelectEnabled()).findAny().isPresent());
        cartDistributorVO.setSelected(!cartGoodsVOList.stream().filter(e -> !e.getSelected()).findAny().isPresent());
        cartDistributorVO.setHasGift(false);
        // 是否有参入任务
        cartDistributorVO.setIsHasTask(cartGoodsVOList.stream().filter(e -> e.getIsHasTask()).findAny().isPresent());

        List<CartDistributorVO.PromotionActivity> promotionList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(promotionAppList)) {
            Map<Long, List<PromotionAppListDTO>> promotionAppListMap = promotionAppList.stream().collect(Collectors.groupingBy(PromotionAppListDTO::getPromotionActivityId));
            for (Long promotionActivityId : promotionAppListMap.keySet()) {
                List<PromotionAppListDTO> promotionAppListDTOS = promotionAppListMap.get(promotionActivityId);
                PromotionAppListDTO one = promotionAppListDTOS.get(0);
                CartDistributorVO.PromotionActivity vo = new CartDistributorVO.PromotionActivity();
                vo.setActivityId(promotionActivityId);
                vo.setGiftName(one.getGiftName());
                vo.setGiftNum(1);
                vo.setPictureUrl(fileService.getUrl(one.getPictureUrl(), FileTypeEnum.MARKETING_GOODS_GIFT_PICTURE));
                vo.setGiftAmountLimit(one.getGiftAmountLimit());
                vo.setActivityGoodList(one.getGoodsIdList());
                vo.setPassRemainGifAmount(one.getDiff());
                vo.setIsMatchGift(true);
                if (CompareUtil.compare(one.getDiff(), BigDecimal.ZERO) > 0) {
                    vo.setIsMatchGift(false);
                }
                promotionList.add(vo);
            }
        }
        cartDistributorVO.setPromotionActivityList(promotionList);

        return cartDistributorVO;

    }


    /**
     * 查询配送商是否参与
     *
     * @param userId 用户Id
     * @param distributorEids 配送商Ids
     * @return
     */
    private Map<Long, TaskGoodsMatchListDTO> selectDistributorTaskInfo(Long userId, List<Long> distributorEids) {

        List<QueryTaskGoodsMatchRequest> queryTaskGoodsMatchRequestList = distributorEids.stream().map(t -> {

            QueryTaskGoodsMatchRequest queryTaskGoodsMatchRequest = new QueryTaskGoodsMatchRequest();
            queryTaskGoodsMatchRequest.setUserId(userId);
            queryTaskGoodsMatchRequest.setEid(t);
            return queryTaskGoodsMatchRequest;
        }).collect(Collectors.toList());

        // 查询当前用户参与的任务品
        List<TaskGoodsMatchListDTO> taskGoodsMatchDTOS = taskApi.queryTaskGoodsList(queryTaskGoodsMatchRequestList);

        if (log.isDebugEnabled()) {
            log.debug("调用任务接口:queryTaskGoodsList..入参:{},返回参数:{}", queryTaskGoodsMatchRequestList, taskGoodsMatchDTOS);
        }

        if (CollectionUtil.isEmpty(taskGoodsMatchDTOS)) {

            return MapUtil.empty();
        }

        return taskGoodsMatchDTOS.stream().collect(Collectors.toMap(t -> t.getEid(), Function.identity(), (u, u1) -> u));
    }

    /**
     * b2b购物车列表
     *
     * @param staffInfo
     * @param customerEid
     * @return
     */
    private Result<CartListVO> b2bCartList(CurrentStaffInfo staffInfo, Long customerEid) {

        ListCartRequest ListRequest = new ListCartRequest();
        ListRequest.setBuyerEid(customerEid);
        ListRequest.setPlatformEnum(PlatformEnum.SALES_ASSIST);
        ListRequest.setGoodsSourceEnum(CartGoodsSourceEnum.B2B);
        ListRequest.setCreateUser(staffInfo.getCurrentUserId());

        List<CartDTO> cartDTOList = cartApi.listByCreateUser(ListRequest);

        if (CollUtil.isEmpty(cartDTOList)) {
            return Result.success(CartListVO.empty());
        }
        // 配送商字典
        List<Long> distributorEids = cartDTOList.stream().map(CartDTO::getDistributorEid).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> distributorDTOList = enterpriseApi.listByIds(distributorEids);
        Map<Long, EnterpriseDTO> distributorDTOMap = distributorDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        // 配送商商品字典
        List<Long> allGoodsSkuIds = cartDTOList.stream().map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
        List<GoodsSkuStandardBasicDTO> allDistributorGoodsDTOList = goodsApi.batchQueryStandardGoodsBasicBySkuIds(allGoodsSkuIds);
        Map<Long, GoodsSkuStandardBasicDTO> allDistributorGoodsDTOMap = allDistributorGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuStandardBasicDTO::getId, Function.identity()));
        if (allDistributorGoodsDTOList.size() != allGoodsSkuIds.size()) {
            log.error("{}:商品信息查询不全!",allGoodsSkuIds);
        }
        // 获取最新的商品Id
        cartDTOList.forEach(cartDTO -> {
            Long distributorGoodsId = Optional.ofNullable(allDistributorGoodsDTOMap.get(cartDTO.getGoodsSkuId())).map(t -> t.getGoodsId()).orElse(cartDTO.getDistributorGoodsId());
            cartDTO.setDistributorGoodsId(distributorGoodsId);
        });

        //满配信息
        List<ShopDTO> shopList = shopApi.listShopByEidList(distributorEids);
        Map<Long, ShopDTO> shopMap = shopList.stream().collect(Collectors.toMap(ShopDTO::getShopEid, o -> o, (k1, k2) -> k1));


        //商品信息
        List<Long> goodsIds = cartDTOList.stream().map(e -> e.getDistributorGoodsId()).distinct().collect(Collectors.toList());
        QueryGoodsPriceRequest request = new QueryGoodsPriceRequest();
        request.setCustomerEid(customerEid);
        request.setGoodsIds(goodsIds);
        Map<Long, GoodsPriceDTO> goodsPriceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(request);
        // 查询商品品控信息
        Map<Long, Integer> goodsListResult = agreementBusinessApi.getB2bGoodsLimitByGids(goodsIds, customerEid);

        // 需要取消选中的购物车ID集合
        List<Long> cancelSelectCartIds = CollUtil.newArrayList();
        List<CartDistributorVO> cartDistributorVOList = CollUtil.newArrayList();
        Map<Long, List<CartDTO>> distributorCartDTOMap = cartDTOList.stream().collect(Collectors.groupingBy(CartDTO::getDistributorEid));

        // 查询当前用户参与的任务品
        Map<Long, TaskGoodsMatchListDTO> taskGoodsMatchMap = this.selectDistributorTaskInfo(staffInfo.getCurrentUserId(), distributorEids);

        //赠品活动信息
        List<PromotionAppCartGoods> requestList = new ArrayList<>();
        Map<Long, List<CartGoodsVO>> cartDistributorMap = Maps.newHashMap();
        for (Long distributorEid : distributorCartDTOMap.keySet()) {
            List<CartDTO> distributorCartDTOList = distributorCartDTOMap.get(distributorEid);
            List<Long> goodsSkuIds = distributorCartDTOList.stream().map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
            List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByIds(goodsSkuIds);
            Map<Long, GoodsSkuDTO> goodSkuDtoMap = goodsSkuDTOList.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, Function.identity()));
            List<CartGoodsVO> cartGoodsVOList = CollUtil.newArrayList();
            // 处理多个sku时校验库存
            Map<Long, Integer> skuQuantityMap = Maps.newHashMap();
            distributorCartDTOList.forEach(cartInfo -> {
                GoodsSkuDTO goodsSkuDTO = goodSkuDtoMap.get(cartInfo.getGoodsSkuId());
                CartGoodsVO cartGoodsVO = new CartGoodsVO();
                Integer skuNumber = skuQuantityMap.getOrDefault(cartInfo.getGoodsSkuId(), 0);
                // 商品信息
                GoodsSkuStandardBasicDTO standardGoodsBasicDTO = allDistributorGoodsDTOMap.get(cartInfo.getGoodsSkuId());
                // 拷贝商品标准库信息
                PojoUtils.map(SimpleGoodInfoUtils.toSimpleGoodsVO(standardGoodsBasicDTO.getStandardGoodsBasic()), cartGoodsVO);
                cartGoodsVO.setCartId(cartInfo.getId());
                cartGoodsVO.setDistributorGoodsId(cartInfo.getDistributorGoodsId());
                cartGoodsVO.setBigPackage(standardGoodsBasicDTO.getPackageNumber().intValue());
                cartGoodsVO.setPackageNumber(standardGoodsBasicDTO.getPackageNumber());
                cartGoodsVO.setPrice(goodsPriceMap.getOrDefault(cartInfo.getDistributorGoodsId(), new GoodsPriceDTO().setLinePrice(BigDecimal.ZERO)).getLinePrice());

                cartGoodsVO.setQuantity(cartInfo.getQuantity());
                cartGoodsVO.setSelectEnabled(true);
                cartGoodsVO.setSelected(cartInfo.getSelectedFlag() == 1);
                cartGoodsVO.setStatus(CartGoodsStatusEnum.NORMAL.getCode());
                cartGoodsVO.setStatusText(CartGoodsStatusEnum.NORMAL.getName());
                cartGoodsVO.setRemark(goodsSkuDTO != null ? goodsSkuDTO.getRemark() : "");
                cartGoodsVO.setOverSoldType(goodsSkuDTO != null ? goodsSkuDTO.getOverSoldType() : 0);
                // 任务标识
                cartGoodsVO.setIsHasTask(this.matchTask(taskGoodsMatchMap.get(cartInfo.getDistributorEid()), cartInfo.getGoodsId()));

                Long stockNum = goodsSkuDTO != null && CompareUtil.compare(goodsSkuDTO.getQty(), goodsSkuDTO.getFrozenQty()) > 0 ? goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty() - skuNumber : 0L;
                if (CompareUtil.compare(stockNum, 0l) < 0) {
                    stockNum = 0l;
                } else {
                    Long rate = stockNum / goodsSkuDTO.getPackageNumber();
                    stockNum = rate * goodsSkuDTO.getPackageNumber();
                }
                cartGoodsVO.setStockText(StockUtils.getStockText(stockNum));
                cartGoodsVO.setStockNum(stockNum);
                Integer goodsLimitStatus = goodsListResult.get(cartInfo.getDistributorGoodsId());
                // 异常优先级，失效->控销->下架->无货
                if (goodsSkuDTO != null && !GoodsSkuStatusEnum.NORMAL.getCode().equals(goodsSkuDTO.getStatus())) {
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.INVALID.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.INVALID.getName());
                    cartGoodsVO.setSelectEnabled(false);
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelected(false);
                } else if (goodsLimitStatus != null && (GoodsLimitStatusEnum.CONTROL_GOODS == GoodsLimitStatusEnum.getByCode(goodsLimitStatus) || GoodsLimitStatusEnum.INVALID_GOODS == GoodsLimitStatusEnum.getByCode(goodsLimitStatus))) {
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.INVALID.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.INVALID.getName());
                    cartGoodsVO.setSelectEnabled(false);
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelected(false);
                } else if (GoodsStatusEnum.getByCode(standardGoodsBasicDTO.getStandardGoodsBasic().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF) {
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelectEnabled(false);
                    cartGoodsVO.setSelected(false);
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.UN_SHELF.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.UN_SHELF.getName());
                } else if (stockNum == 0L) {
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelectEnabled(false);
                    cartGoodsVO.setSelected(false);
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.NO_STOCK.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.NO_STOCK.getName());
                } else if (stockNum < cartGoodsVO.getQuantity()) {
                    // 当库存数量大于0，且小于购买数量时，修改购物车商品数量为库存数量
                    cartGoodsVO.setQuantity(stockNum.intValue());
                    if (GoodsStatusEnum.getByCode(standardGoodsBasicDTO.getStandardGoodsBasic().getGoodsStatus()) == GoodsStatusEnum.UP_SHELF) {
                        UpdateCartGoodsQuantityRequest updateCartGoodsQuantityRequest = new UpdateCartGoodsQuantityRequest();
                        updateCartGoodsQuantityRequest.setId(cartGoodsVO.getCartId());
                        updateCartGoodsQuantityRequest.setQuantity(cartGoodsVO.getQuantity());
                        updateCartGoodsQuantityRequest.setOpUserId(staffInfo.getCurrentUserId());
                        cartApi.updateCartGoodsQuantity(updateCartGoodsQuantityRequest);
                    }
                }
                cartGoodsVO.setAmount(NumberUtil.round(NumberUtil.mul(cartGoodsVO.getPrice(), cartGoodsVO.getQuantity()), 2));
                cartGoodsVOList.add(cartGoodsVO);
                if (cartGoodsVO.getSelected()) {
                    skuQuantityMap.put(cartInfo.getGoodsSkuId(), skuNumber + cartGoodsVO.getQuantity());
                }

            });

            Map<Long, BigDecimal> goodsAmountMap = cartGoodsVOList.stream().filter(t -> t.getSelected()).collect(Collectors.groupingBy(CartGoodsVO::getDistributorGoodsId, Collectors.reducing(BigDecimal.ZERO, CartGoodsVO::getAmount, BigDecimal::add)));
            List<Long> distributorGoodsIds = cartGoodsVOList.stream().map(t -> t.getDistributorGoodsId()).distinct().collect(Collectors.toList());
            for (Long distributorGoodsId : distributorGoodsIds) {
                PromotionAppCartGoods cartGoods = new PromotionAppCartGoods();
                cartGoods.setGoodsId(distributorGoodsId);
                cartGoods.setAmount(goodsAmountMap.getOrDefault(distributorGoodsId, BigDecimal.ZERO));
                requestList.add(cartGoods);
            }
            cartDistributorMap.put(distributorEid, cartGoodsVOList);
        }
        PromotionAppCartRequest cartRequest = new PromotionAppCartRequest();
        cartRequest.setPlatform(PlatformEnum.SALES_ASSIST.getCode());
        cartRequest.setCartGoodsList(requestList);
        List<PromotionAppListDTO> promotionAppListList = promotionActivityApi.queryAppCartPromotion(cartRequest);
        log.info("queryAppGoodsListPromotion..request->{}.....result ->{}", JSON.toJSON(cartRequest), JSON.toJSON(promotionAppListList));
        Map<Long, List<PromotionAppListDTO>> promotionMap = promotionAppListList.stream().collect(Collectors.groupingBy(PromotionAppListDTO::getEid));
        cartDistributorMap.keySet().stream().forEach(distributorEid -> {
            //满配金额
            ShopDTO shopOne = shopMap.get(distributorEid);
            //赠品活动信息
            List<PromotionAppListDTO> promotionAppList = promotionMap.get(distributorEid);
            CartDistributorVO cartDistributorVO = this.buildCartDistributorVo(distributorEid, distributorDTOMap.get(distributorEid).getName(), cartDistributorMap.get(distributorEid), shopOne, promotionAppList);
            cartDistributorVOList.add(cartDistributorVO);
        });

        CartListVO cartListVO = new CartListVO();
        cartListVO.setOrderType(OrderTypeEnum.B2B.getCode());
        cartListVO.setCartDistributorList(cartDistributorVOList);
        cartListVO.setSelectedGoodsNum(cartDistributorVOList.stream().mapToLong(CartDistributorVO::getSelectedGoodsNum).sum());
        cartListVO.setSelectedGoodsAmount(cartDistributorVOList.stream().map(CartDistributorVO::getSelectedGoodsAmount).reduce(BigDecimal::add).get());
        cartListVO.setSelectEnabled(cartDistributorVOList.stream().filter(e -> e.getSelectEnabled()).findAny().isPresent());
        cartListVO.setSelected(!cartDistributorVOList.stream().filter(e -> !e.getSelected()).findAny().isPresent());
        cartListVO.setCartGoodsNum(Long.valueOf(cartDTOList.size()));
        if (CollUtil.isEmpty(cancelSelectCartIds)) {
            return Result.success(cartListVO);
        }
        SelectCartGoodsRequest selectCartGoodsRequest = new SelectCartGoodsRequest();
        selectCartGoodsRequest.setIds(cancelSelectCartIds);
        selectCartGoodsRequest.setSelected(false);
        selectCartGoodsRequest.setOpUserId(staffInfo.getCurrentUserId());
        cartApi.selectCartGoods(selectCartGoodsRequest);

        return Result.success(cartListVO);
    }


    /**
     * POP订单购物车
     *
     * @param staffInfo
     * @param customerEid
     * @return
     */
    private Result<CartListVO> popCartList(CurrentStaffInfo staffInfo, Long customerEid) {

        ListCartRequest request = new ListCartRequest();
        request.setBuyerEid(customerEid);
        request.setPlatformEnum(PlatformEnum.SALES_ASSIST);
        request.setGoodsSourceEnum(CartGoodsSourceEnum.POP);
        request.setCreateUser(staffInfo.getCurrentUserId());
        List<CartDTO> cartDTOList = cartApi.listByCreateUser(request);

        if (CollUtil.isEmpty(cartDTOList)) {

            return Result.success(CartListVO.empty());
        }
        // 配送商字典
        List<Long> distributorEids = cartDTOList.stream().map(CartDTO::getDistributorEid).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> distributorDTOList = enterpriseApi.listByIds(distributorEids);
        Map<Long, EnterpriseDTO> distributorDTOMap = distributorDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));

        // 配送商商品字典
        List<Long> allGoodsSkuIds = cartDTOList.stream().map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
        List<GoodsSkuStandardBasicDTO> allDistributorGoodsDTOList = goodsApi.batchQueryStandardGoodsBasicBySkuIds(allGoodsSkuIds);
        Map<Long, GoodsSkuStandardBasicDTO> allDistributorGoodsDTOMap = allDistributorGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuStandardBasicDTO::getId, Function.identity()));


        if (allDistributorGoodsDTOList.size() != allGoodsSkuIds.size()) {
            log.error("{}:商品信息查询不全!",allGoodsSkuIds);
        }

        // 获取最新的商品Id
        cartDTOList.forEach(cartDTO -> {
            Long distributorGoodsId = Optional.ofNullable(allDistributorGoodsDTOMap.get(cartDTO.getGoodsSkuId())).map(t -> t.getGoodsId()).orElse(cartDTO.getDistributorGoodsId());
            cartDTO.setDistributorGoodsId(distributorGoodsId);
        });

        //商品信息
        List<Long> distributorGoodsIds = cartDTOList.stream().map(e -> e.getDistributorGoodsId()).distinct().collect(Collectors.toList());

        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(customerEid);
        queryGoodsPriceRequest.setGoodsIds(distributorGoodsIds);
        // 商品价格字典
        Map<Long, BigDecimal> goodsPriceMap = goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);
        // 需要取消选中的购物车ID集合
        List<Long> cancelSelectCartIds = CollUtil.newArrayList();
        List<CartDistributorVO> cartDistributorVOList = CollUtil.newArrayList();
        Map<Long, List<CartDTO>> distributorCartDTOMap = cartDTOList.stream().collect(Collectors.groupingBy(CartDTO::getDistributorEid));
        for (Long distributorEid : distributorCartDTOMap.keySet()) {
            List<CartDTO> distributorCartDTOList = distributorCartDTOMap.get(distributorEid);
            List<Long> skuGoodIds = distributorCartDTOList.stream().map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
            Map<Long, InventoryDTO> distributorGoodsInventoryDTOMap = inventoryApi.getMapBySkuIds(skuGoodIds);
            List<CartGoodsVO> cartGoodsVOList = CollUtil.newArrayList();
            // 处理多个sku时校验库存
            Map<Long, Integer> skuQuantityMap = Maps.newHashMap();
            for (CartDTO cartInfo : distributorCartDTOList) {
                CartGoodsVO cartGoodsVO = new CartGoodsVO();
                InventoryDTO inventoryDTO = distributorGoodsInventoryDTOMap.get(cartInfo.getGoodsSkuId());
                Integer skuNumber = skuQuantityMap.getOrDefault(cartInfo.getGoodsSkuId(), 0);
                // 商品信息
                GoodsSkuStandardBasicDTO goodsSkuStandardBasicDTO = allDistributorGoodsDTOMap.get(cartInfo.getGoodsSkuId());
                // 拷贝商品标准库信息
                StandardGoodsBasicDTO standardGoodsBasicDTO = goodsSkuStandardBasicDTO.getStandardGoodsBasic();
                PojoUtils.map(SimpleGoodInfoUtils.toSimpleGoodsVO(standardGoodsBasicDTO), cartGoodsVO);
                cartGoodsVO.setCartId(cartInfo.getId());
                cartGoodsVO.setDistributorGoodsId(cartInfo.getDistributorGoodsId());
                cartGoodsVO.setPackageNumber(goodsSkuStandardBasicDTO.getPackageNumber());
                cartGoodsVO.setBigPackage(standardGoodsBasicDTO.getBigPackage());
                cartGoodsVO.setPrice(goodsPriceMap.getOrDefault(cartInfo.getDistributorGoodsId(), standardGoodsBasicDTO.getPrice()));
                cartGoodsVO.setQuantity(cartInfo.getQuantity());
                cartGoodsVO.setAmount(NumberUtil.round(NumberUtil.mul(cartGoodsVO.getPrice(), cartGoodsVO.getQuantity()), 2));
                cartGoodsVO.setSelectEnabled(true);
                cartGoodsVO.setSelected(cartInfo.getSelectedFlag() == 1);
                cartGoodsVO.setStatus(CartGoodsStatusEnum.NORMAL.getCode());
                cartGoodsVO.setStatusText(CartGoodsStatusEnum.NORMAL.getName());
                cartGoodsVO.setOverSoldType(inventoryDTO != null ? inventoryDTO.getOverSoldType() : 0);
                cartGoodsVO.setIsHasTask(false);
                PopGoodsDTO popGoodsDTO = popGoodsApi.getPopGoodsByGoodsId(standardGoodsBasicDTO.getId());
                Integer goodsStatus = popGoodsDTO.getGoodsStatus();
                if (GoodsStatusEnum.getByCode(goodsStatus) != GoodsStatusEnum.UP_SHELF) {
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelectEnabled(false);
                    cartGoodsVO.setSelected(false);
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.UN_SHELF.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.UN_SHELF.getName());
                }
                Long stockNum = inventoryDTO != null && CompareUtil.compare(inventoryDTO.getQty(), inventoryDTO.getFrozenQty()) > 0 ? inventoryDTO.getQty() - inventoryDTO.getFrozenQty() - skuNumber : 0L;

                if (CompareUtil.compare(stockNum, 0l) < 0) {
                    stockNum = 0l;
                } else {
                    Long rate = stockNum / goodsSkuStandardBasicDTO.getPackageNumber();
                    stockNum = rate * goodsSkuStandardBasicDTO.getPackageNumber();
                }
                cartGoodsVO.setStockNum(stockNum);
                cartGoodsVO.setStockText(StockUtils.getStockText(stockNum));
                // 异常优先级，失效->控销->下架->无货
                if (goodsSkuStandardBasicDTO != null && !GoodsSkuStatusEnum.NORMAL.getCode().equals(goodsSkuStandardBasicDTO.getStatus()) || GoodsStatusEnum.getByCode(goodsSkuStandardBasicDTO.getStandardGoodsBasic().getAuditStatus()) != GoodsStatusEnum.AUDIT_PASS) {
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelectEnabled(false);
                    cartGoodsVO.setSelected(false);
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.INVALID.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.INVALID.getName());
                } else if (GoodsStatusEnum.getByCode(goodsStatus) != GoodsStatusEnum.UP_SHELF) {
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelectEnabled(false);
                    cartGoodsVO.setSelected(false);
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.UN_SHELF.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.UN_SHELF.getName());
                } else if (stockNum == 0L && ObjectUtil.equal(0, inventoryDTO.getOverSoldType())) {
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelectEnabled(false);
                    cartGoodsVO.setSelected(false);
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.NO_STOCK.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.NO_STOCK.getName());
                } else if (stockNum < cartGoodsVO.getQuantity() && ObjectUtil.equal(0, cartGoodsVO.getOverSoldType())) {
                    // 当库存数量大于0，且小于购买数量时，修改购物车商品数量为库存数量
                    cartGoodsVO.setQuantity(stockNum.intValue());
                    if (GoodsStatusEnum.getByCode(goodsStatus) == GoodsStatusEnum.UP_SHELF) {
                        UpdateCartGoodsQuantityRequest updateCartGoodsQuantityRequest = new UpdateCartGoodsQuantityRequest();
                        updateCartGoodsQuantityRequest.setId(cartGoodsVO.getCartId());
                        updateCartGoodsQuantityRequest.setQuantity(cartGoodsVO.getQuantity());
                        updateCartGoodsQuantityRequest.setOpUserId(staffInfo.getCurrentUserId());
                        cartApi.updateCartGoodsQuantity(updateCartGoodsQuantityRequest);
                    }
                }
                cartGoodsVOList.add(cartGoodsVO);
                if (cartGoodsVO.getSelected()) {
                    skuQuantityMap.put(cartInfo.getGoodsSkuId(), skuNumber + cartGoodsVO.getQuantity());
                }
            }
            CartDistributorVO cartDistributorVO = new CartDistributorVO();
            cartDistributorVO.setDistributorEid(distributorEid);
            cartDistributorVO.setDistributorName(distributorDTOMap.get(distributorEid).getName());
            Optional optional = cartGoodsVOList.stream().filter(e -> e.getSelected()).findAny();
            if (optional.isPresent()) {
                cartDistributorVO.setSelectedGoodsNum(cartGoodsVOList.stream().filter(e -> e.getSelected()).count());
                cartDistributorVO.setSelectedGoodsAmount(cartGoodsVOList.stream().filter(e -> e.getSelected()).map(CartGoodsVO::getAmount).reduce(BigDecimal::add).get());
            } else {
                cartDistributorVO.setSelectedGoodsNum(0L);
                cartDistributorVO.setSelectedGoodsAmount(BigDecimal.ZERO);
            }
            cartDistributorVO.setCartGoodsList(cartGoodsVOList);
            cartDistributorVO.setSelectEnabled(cartGoodsVOList.stream().filter(e -> e.getSelectEnabled()).findAny().isPresent());
            cartDistributorVO.setSelected(!cartGoodsVOList.stream().filter(e -> !e.getSelected()).findAny().isPresent());
            // 是否有参入任务
            cartDistributorVO.setIsHasTask(cartGoodsVOList.stream().filter(e -> e.getIsHasTask()).findAny().isPresent());

            cartDistributorVOList.add(cartDistributorVO);
        }

        CartListVO cartListVO = new CartListVO();
        cartListVO.setOrderType(OrderTypeEnum.POP.getCode());
        cartListVO.setCartDistributorList(cartDistributorVOList);
        cartListVO.setSelectedGoodsNum(cartDistributorVOList.stream().mapToLong(CartDistributorVO::getSelectedGoodsNum).sum());
        cartListVO.setCartGoodsNum(Long.valueOf(cartDTOList.size()));
        cartListVO.setSelectedGoodsAmount(cartDistributorVOList.stream().map(CartDistributorVO::getSelectedGoodsAmount).reduce(BigDecimal::add).get());
        cartListVO.setSelectEnabled(cartDistributorVOList.stream().filter(e -> e.getSelectEnabled()).findAny().isPresent());
        cartListVO.setSelected(!cartDistributorVOList.stream().filter(e -> !e.getSelected()).findAny().isPresent());

        if (CollUtil.isNotEmpty(cancelSelectCartIds)) {
            SelectCartGoodsRequest selectCartGoodsRequest = new SelectCartGoodsRequest();
            selectCartGoodsRequest.setIds(cancelSelectCartIds);
            selectCartGoodsRequest.setSelected(false);
            selectCartGoodsRequest.setOpUserId(staffInfo.getCurrentUserId());
            cartApi.selectCartGoods(selectCartGoodsRequest);
        }

        return Result.success(cartListVO);

    }

    /**
     * 匹配商品是否参与运河
     *
     * @param goodsMatchListDTO 任务信息
     * @param goodsId 匹配商品Id
     * @return
     */
    private Boolean matchTask(TaskGoodsMatchListDTO goodsMatchListDTO, Long goodsId) {

        Boolean isHasTask = Optional.ofNullable(goodsMatchListDTO).map(t -> {
            if (CollectionUtil.isEmpty(t.getGoodsMatchList())) {
                return false;
            }
            List<Long> taskGoodIds = t.getGoodsMatchList().stream().map(z -> z.getGoodsId()).collect(Collectors.toList());
            if (taskGoodIds.contains(goodsId)) {
                return true;
            }
            return false;
        }).orElse(false);

        return isHasTask;
    }

    @ApiOperation(value = "获取购物车列表")
    @PostMapping("/list")
    @UserAccessAuthentication
    public Result<CartListVO> list(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "选择客户ID", required = true) @RequestParam(value = "customerEid", required = true) Long customerEid) {

        if (!staffInfo.getYilingFlag()) {
            return this.b2bCartList(staffInfo, customerEid);
        } else {
            return this.popCartList(staffInfo, customerEid);
        }

    }

    @ApiOperation(value = "移除购物车商品")
    @PostMapping("/remove")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CartListVO> remove(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid RemoveCartGoodsForm form) {
        RemoveCartGoodsRequest request = PojoUtils.map(form, RemoveCartGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        cartApi.removeCartGoods(request);

        return this.list(staffInfo, form.getCustomerEid());
    }

    @ApiOperation(value = "根据客户ID删除进货单数据")
    @PostMapping("/delete/customerEid")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<Void> removeByCustomerEid(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "选择客户ID", required = true) @RequestParam(value = "customerEid", required = true) Long customerEid) {
        CartGoodsSourceEnum goodsSourceEnum = staffInfo.getYilingFlag() ? CartGoodsSourceEnum.POP : CartGoodsSourceEnum.B2B;
        List<CartDTO> cartDTOList = cartApi.listByBuyerEid(customerEid, PlatformEnum.SALES_ASSIST, goodsSourceEnum, CartIncludeEnum.ALL);
        if (CollectionUtil.isEmpty(cartDTOList)) {
            return Result.success();
        }
        List<Long> cartIdList = cartDTOList.stream().map(t -> t.getId()).collect(Collectors.toList());
        RemoveCartGoodsRequest request = new RemoveCartGoodsRequest();
        request.setIds(cartIdList);
        Boolean result = cartApi.removeCartGoods(request);
        if (result) {
            return Result.success();
        }
        return Result.failed("删除客户进货单失败!");
    }

    @ApiOperation(value = "修改购物车商品数量")
    @PostMapping("/updateQuantity")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CartListVO> updateQuantity(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateCartGoodsQuantityForm form) {
        UpdateCartGoodsQuantityRequest request = PojoUtils.map(form, UpdateCartGoodsQuantityRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        cartApi.updateCartGoodsQuantity(request);
        return this.list(staffInfo, form.getCustomerEid());
    }

    @ApiOperation(value = "勾选购物车商品")
    @PostMapping("/select")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CartListVO> select(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SelectCartGoodsForm form) {
        SelectCartGoodsRequest request = PojoUtils.map(form, SelectCartGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        cartApi.selectCartGoods(request);
        return this.list(staffInfo, form.getCustomerEid());
    }


    @ApiOperation(value = "草稿箱选择客户")
    @PostMapping("/choose/customer")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CartChooseCustomerVO> chooseCustomerDraftBox(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "客户企业ID", required = true) @RequestParam("customerEid") Long customerEid) {

        CartGoodsSourceEnum goodsSourceEnum = staffInfo.getYilingFlag() ? CartGoodsSourceEnum.POP : CartGoodsSourceEnum.B2B;
        List<CartDTO> cartDTOList = cartApi.listByBuyerEid(customerEid, PlatformEnum.SALES_ASSIST, goodsSourceEnum, CartIncludeEnum.ALL);

        if (CollectionUtil.isEmpty(cartDTOList)) {
            return Result.failed("当前客户,草稿箱无数据!");
        }

        CartChooseCustomerVO customerVO = new CartChooseCustomerVO();

        List<Long> distributorEids = cartDTOList.stream().map(e -> e.getDistributorEid()).collect(Collectors.toList());

        if (distributorEids.size() > 1) {
            customerVO.setIsSkipCart(true);
        } else {
            customerVO.setIsSkipCart(false);
        }

        customerVO.setCusomterEid(customerEid);
        customerVO.setDistributorEids(distributorEids);

        return Result.success(customerVO);
    }


    @ApiOperation(value = "再来一单")
    @PostMapping("/history/add")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<Void> historyAdd(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "订单ID", required = true) @RequestParam("orderId") Long orderId) {
        List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailInfo(orderId);
        if (CollectionUtil.isEmpty(orderDetailDTOList)) {
            return Result.failed("未查询到订单信息!");
        }
        OrderDTO orderDTO = orderApi.getOrderInfo(orderId);
        BatchAddToCartRequest request = new BatchAddToCartRequest();
        request.setBuyerEid(orderDTO.getBuyerEid());
        request.setPlatformEnum(PlatformEnum.SALES_ASSIST);
        request.setGoodsSourceEnum(OrderTypeEnum.POP == OrderTypeEnum.getByCode(orderDTO.getOrderType()) ? CartGoodsSourceEnum.POP : CartGoodsSourceEnum.B2B);
        request.setOpUserId(staffInfo.getCurrentUserId());
        List<BatchAddToCartRequest.QuickPurchaseInfoDTO> quickPurchaseInfoList = orderDetailDTOList.stream().map(t -> {
            BatchAddToCartRequest.QuickPurchaseInfoDTO quickPurchaseInfoDTO = new BatchAddToCartRequest.QuickPurchaseInfoDTO();
            quickPurchaseInfoDTO.setGoodsId(t.getGoodsId());
            quickPurchaseInfoDTO.setDistributorGoodsId(t.getDistributorGoodsId());
            quickPurchaseInfoDTO.setDistributorEid(orderDTO.getDistributorEid());
            quickPurchaseInfoDTO.setGoodsSkuId(t.getGoodsSkuId());
            quickPurchaseInfoDTO.setQuantity(t.getGoodsQuantity());
            return quickPurchaseInfoDTO;
        }).collect(Collectors.toList());

        request.setQuickPurchaseInfoList(quickPurchaseInfoList);
        Boolean result = cartApi.batchAddToCart(request);

        if (result) {
            return Result.success();
        }

        return Result.failed("再来一单添加失败!");

    }

}
