package com.yiling.f2b.web.cart.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.yiling.common.web.util.StockUtils;
import com.yiling.f2b.web.cart.form.AddToCartForm;
import com.yiling.f2b.web.cart.form.BatchAddToCartForm;
import com.yiling.f2b.web.cart.form.RemoveCartGoodsForm;
import com.yiling.f2b.web.cart.form.SelectCartGoodsForm;
import com.yiling.f2b.web.cart.form.UpdateCartGoodsQuantityForm;
import com.yiling.f2b.web.cart.vo.CartDistributorVO;
import com.yiling.f2b.web.cart.vo.CartGoodsNumVO;
import com.yiling.f2b.web.cart.vo.CartGoodsVO;
import com.yiling.f2b.web.cart.vo.CartListVO;
import com.yiling.f2b.web.common.utils.VoUtils;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.GoodsSkuStandardBasicDTO;
import com.yiling.goods.medicine.enums.GoodsSkuStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.mall.cart.api.CartApi;
import com.yiling.mall.cart.dto.CartDTO;
import com.yiling.mall.cart.dto.request.AddToCartRequest;
import com.yiling.mall.cart.dto.request.BatchAddToCartRequest;
import com.yiling.mall.cart.dto.request.RemoveCartGoodsRequest;
import com.yiling.mall.cart.dto.request.SelectCartGoodsRequest;
import com.yiling.mall.cart.dto.request.UpdateCartGoodsQuantityRequest;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;
import com.yiling.mall.cart.enums.CartGoodsStatusEnum;
import com.yiling.mall.cart.enums.CartIncludeEnum;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 购物车 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/6/18
 */
@RestController
@RequestMapping("/cart")
@Api(tags = "购物车模块接口")
@Slf4j
public class CartController extends BaseController {
    @DubboReference
    CartApi                      cartApi;
    @DubboReference
    EnterpriseApi                enterpriseApi;
    @DubboReference
    GoodsApi                     goodsApi;
    @DubboReference
    GoodsPriceApi                goodsPriceApi;
    @Autowired
    VoUtils                      voUtils;
    @Autowired
    private RedisDistributedLock redisDistributedLock;

    @ApiOperation(value = "获取购物车商品数量")
    @GetMapping("/getCartGoodsNum")
    public Result<CartGoodsNumVO> getCartGoodsNum(@CurrentUser CurrentStaffInfo staffInfo) {
        if (staffInfo == null) {
            return Result.success(new CartGoodsNumVO(0));
        }
        Integer cartGoodsNum = cartApi.getCartGoodsNum(staffInfo.getCurrentEid(), PlatformEnum.POP, CartGoodsSourceEnum.POP);
        return Result.success(new CartGoodsNumVO(cartGoodsNum));
    }

    @ApiOperation(value = "添加商品到购物车")
    @PostMapping("/add")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<BoolObject> add(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AddToCartForm form) {
        AddToCartRequest request = PojoUtils.map(form, AddToCartRequest.class);
        request.setBuyerEid(staffInfo.getCurrentEid());
        request.setPlatformEnum(PlatformEnum.POP);
        request.setGoodsSourceEnum(CartGoodsSourceEnum.POP);
        request.setOpUserId(staffInfo.getCurrentUserId());
        String lockName = RedisKey.generate("cart",PlatformEnum.POP.getCode().toString(),"add",staffInfo.getCurrentEid().toString(),form.getGoodsSkuId().toString());
        String lockId = "";
        boolean result = false;
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            result = cartApi.addToCart(request);
        } catch (InterruptedException e) {
            return Result.failed("系统繁忙，请稍后操作!");
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "批量添加商品到购物车（快速采购）")
    @PostMapping("/batchAdd")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<BoolObject> batchAdd(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid BatchAddToCartForm form) {
        BatchAddToCartRequest request = PojoUtils.map(form, BatchAddToCartRequest.class);
        request.setBuyerEid(staffInfo.getCurrentEid());
        request.setPlatformEnum(PlatformEnum.POP);
        request.setGoodsSourceEnum(CartGoodsSourceEnum.POP);
        request.setOpUserId(staffInfo.getCurrentUserId());

        // 所有GoodsSkuID集合
        List<Long> allGoodsSkuIds = request.getQuickPurchaseInfoList().stream().map(BatchAddToCartRequest.QuickPurchaseInfoDTO::getGoodsSkuId).distinct().collect(Collectors.toList());

        if (allGoodsSkuIds.size() != request.getQuickPurchaseInfoList().size()) {

            return Result.failed("列表中存在同商品同配送商情况，为防止误操作请维护成一条后再提交");
        }

        boolean result = cartApi.batchAddToCart(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "获取购物车列表")
    @PostMapping("/list")
    @UserAccessAuthentication
    public Result<CartListVO> list(@CurrentUser CurrentStaffInfo staffInfo) {
        List<CartDTO> cartDTOList = cartApi.listByBuyerEid(staffInfo.getCurrentEid(), PlatformEnum.POP, CartGoodsSourceEnum.POP, CartIncludeEnum.ALL);
        if (CollUtil.isEmpty(cartDTOList)) {
            return Result.success(CartListVO.empty());
        }
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;

        // 配送商字典
        List<Long> distributorEids = cartDTOList.stream().map(CartDTO::getDistributorEid).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> distributorDTOList = enterpriseApi.listByIds(distributorEids);
        Map<Long, EnterpriseDTO> distributorDTOMap = distributorDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, Function.identity()));
        // 配送商商品字典
        List<Long> allSkuGoodsIds = cartDTOList.stream().map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
        List<GoodsSkuStandardBasicDTO> allDistributorGoodsDTOList = goodsApi.batchQueryStandardGoodsBasicBySkuIds(allSkuGoodsIds);

        if (allDistributorGoodsDTOList.size() != allSkuGoodsIds.size()) {

            log.error("{}:商品信息查询不全!",allSkuGoodsIds);
        }

        Map<Long, GoodsSkuStandardBasicDTO> allDistributorGoodsDTOMap = allDistributorGoodsDTOList.stream().collect(Collectors.toMap(GoodsSkuStandardBasicDTO::getId, Function.identity()));
        cartDTOList.forEach(cartDTO -> {
            Long distributorGoodsId = Optional.ofNullable(allDistributorGoodsDTOMap.get(cartDTO.getGoodsSkuId())).map(t -> t.getGoodsId()).orElse(cartDTO.getDistributorGoodsId());
            cartDTO.setDistributorGoodsId(distributorGoodsId);
        });

        //商品信息
        List<Long> distributorGoodsIds = cartDTOList.stream().map(e -> e.getDistributorGoodsId()).distinct().collect(Collectors.toList());

        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(staffInfo.getCurrentEid());
        queryGoodsPriceRequest.setGoodsIds(distributorGoodsIds);
        // 商品价格字典
        Map<Long, BigDecimal> goodsPriceMap = goodsPriceApi.queryGoodsPriceMap(queryGoodsPriceRequest);
        // 需要取消选中的购物车ID集合
        List<Long> cancelSelectCartIds = CollUtil.newArrayList();
        List<CartDistributorVO> cartDistributorVOList = CollUtil.newArrayList();
        Map<Long, List<CartDTO>> distributorCartDTOMap = cartDTOList.stream().collect(Collectors.groupingBy(CartDTO::getDistributorEid));
        for (Long distributorEid : distributorCartDTOMap.keySet()) {
            List<CartDTO> distributorCartDTOList = distributorCartDTOMap.get(distributorEid);
            List<Long> goodSkuIds = distributorCartDTOList.stream().map(CartDTO::getGoodsSkuId).distinct().collect(Collectors.toList());
            List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByIds(goodSkuIds);
            Map<Long,GoodsSkuDTO> skuResultMap = goodsSkuDTOList.stream().collect(Collectors.toMap(GoodsSkuDTO::getId,Function.identity()));
            List<CartGoodsVO> cartGoodsVOList = CollUtil.newArrayList();
            // 处理多个sku时校验库存
            Map<Long,Integer> skuQuantityMap = Maps.newHashMap();
            distributorCartDTOList.forEach(cartInfo -> {
                GoodsSkuDTO skuDTO = skuResultMap.get(cartInfo.getGoodsSkuId());
                CartGoodsVO cartGoodsVO = new CartGoodsVO();
                // 商品信息
                GoodsSkuStandardBasicDTO standardGoodsBasicDTO = allDistributorGoodsDTOMap.get(cartInfo.getGoodsSkuId());
                Integer skuNumber = skuQuantityMap.getOrDefault(cartInfo.getGoodsSkuId(),0);
                // 拷贝商品标准库信息
                PojoUtils.map(voUtils.toSimpleGoodsVO(standardGoodsBasicDTO.getStandardGoodsBasic()), cartGoodsVO);
                cartGoodsVO.setCartId(cartInfo.getId());
                cartGoodsVO.setDistributorGoodsId(standardGoodsBasicDTO.getGoodsId());
                cartGoodsVO.setBigPackage(standardGoodsBasicDTO.getPackageNumber().intValue());
                cartGoodsVO.setPrice(goodsPriceMap.getOrDefault(standardGoodsBasicDTO.getGoodsId(),standardGoodsBasicDTO.getStandardGoodsBasic().getPrice()));
                cartGoodsVO.setGoodsRemark(standardGoodsBasicDTO.getRemark());
                cartGoodsVO.setPrice(goodsPriceMap.getOrDefault(standardGoodsBasicDTO.getGoodsId(),standardGoodsBasicDTO.getStandardGoodsBasic().getPrice()));
                cartGoodsVO.setQuantity(cartInfo.getQuantity());
                cartGoodsVO.setSelectEnabled(true);
                cartGoodsVO.setOverSoldType(skuDTO != null ? skuDTO.getOverSoldType() : 0);
                cartGoodsVO.setSelected(cartInfo.getSelectedFlag() == 1);
                cartGoodsVO.setStatus(CartGoodsStatusEnum.NORMAL.getCode());
                cartGoodsVO.setStatusText(CartGoodsStatusEnum.NORMAL.getName());
                Long stockNum = skuDTO != null ? skuDTO.getQty() - skuDTO.getFrozenQty() - skuNumber : 0L;

                if (CompareUtil.compare(stockNum,0l) < 0) {
                    stockNum = 0l;
                } else {
                    Long rate = stockNum / skuDTO.getPackageNumber();
                    stockNum = rate * skuDTO.getPackageNumber();
                }
                cartGoodsVO.setStockNum(stockNum);
                cartGoodsVO.setStockText(StockUtils.getStockText(stockNum));
                if (!GoodsSkuStatusEnum.NORMAL.getCode().equals(standardGoodsBasicDTO.getStatus())
                    || GoodsStatusEnum.getByCode(standardGoodsBasicDTO.getStandardGoodsBasic().getAuditStatus()) !=  GoodsStatusEnum.AUDIT_PASS){
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelectEnabled(false);
                    cartGoodsVO.setSelected(false);
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.INVALID.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.INVALID.getName());
                } else if (GoodsStatusEnum.getByCode(standardGoodsBasicDTO.getStandardGoodsBasic().getGoodsStatus()) != GoodsStatusEnum.UP_SHELF) {
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelectEnabled(false);
                    cartGoodsVO.setSelected(false);
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.UN_SHELF.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.UN_SHELF.getName());
                } else if (stockNum == 0L && ObjectUtil.equal(0,skuDTO.getOverSoldType())) {
                    // 如果原来是选中状态，现在就要取消选中
                    if (cartGoodsVO.getSelected()) {
                        cancelSelectCartIds.add(cartGoodsVO.getCartId());
                    }
                    cartGoodsVO.setSelectEnabled(false);
                    cartGoodsVO.setSelected(false);
                    cartGoodsVO.setStatus(CartGoodsStatusEnum.NO_STOCK.getCode());
                    cartGoodsVO.setStatusText(CartGoodsStatusEnum.NO_STOCK.getName());
                } else if (stockNum < cartGoodsVO.getQuantity() && ObjectUtil.equal(0,cartGoodsVO.getOverSoldType())) {
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
                    skuQuantityMap.put(cartInfo.getGoodsSkuId(),skuNumber + cartGoodsVO.getQuantity());
                }
            });

            CartDistributorVO cartDistributorVO = new CartDistributorVO();
            cartDistributorVO.setDistributorEid(distributorEid);
            cartDistributorVO.setDistributorName(distributorDTOMap.get(distributorEid).getName());
            Optional optional = cartGoodsVOList.stream().filter(e -> e.getSelected()).findAny();
            if (optional.isPresent()) {
                cartDistributorVO.setSelectedGoodsNum(cartGoodsVOList.stream().filter(e -> e.getSelected()).mapToLong(CartGoodsVO::getQuantity).sum());
                cartDistributorVO.setSelectedGoodsAmount(cartGoodsVOList.stream().filter(e -> e.getSelected()).map(CartGoodsVO::getAmount).reduce(BigDecimal::add).get());
            } else {
                cartDistributorVO.setSelectedGoodsNum(0L);
                cartDistributorVO.setSelectedGoodsAmount(BigDecimal.ZERO);
            }
            cartDistributorVO.setCartGoodsList(cartGoodsVOList);
            cartDistributorVO.setSelectEnabled(cartGoodsVOList.stream().filter(e -> e.getSelectEnabled()).findAny().isPresent());
            cartDistributorVO.setSelected(!cartGoodsVOList.stream().filter(e -> !e.getSelected()).findAny().isPresent());

            cartDistributorVOList.add(cartDistributorVO);
        }

        CartListVO cartListVO = new CartListVO();
        cartListVO.setCartDistributorList(cartDistributorVOList);
        cartListVO.setSelectedGoodsNum(cartDistributorVOList.stream().mapToLong(CartDistributorVO::getSelectedGoodsNum).sum());
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

    @ApiOperation(value = "移除购物车商品")
    @PostMapping("/remove")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CartListVO> remove(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid RemoveCartGoodsForm form) {
        RemoveCartGoodsRequest request = PojoUtils.map(form, RemoveCartGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        cartApi.removeCartGoods(request);
        return this.list(staffInfo);
    }

    @ApiOperation(value = "修改购物车商品数量")
    @PostMapping("/updateQuantity")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CartListVO> updateQuantity(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateCartGoodsQuantityForm form) {
        UpdateCartGoodsQuantityRequest request = PojoUtils.map(form, UpdateCartGoodsQuantityRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        cartApi.updateCartGoodsQuantity(request);
        return this.list(staffInfo);
    }

    @ApiOperation(value = "勾选购物车商品")
    @PostMapping("/select")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<CartListVO> select(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SelectCartGoodsForm form) {
        SelectCartGoodsRequest request = PojoUtils.map(form, SelectCartGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        cartApi.selectCartGoods(request);
        return this.list(staffInfo);
    }
}
