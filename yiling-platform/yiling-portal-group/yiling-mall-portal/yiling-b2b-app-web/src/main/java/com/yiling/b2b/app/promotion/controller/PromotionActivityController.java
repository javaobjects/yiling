package com.yiling.b2b.app.promotion.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.app.goods.utils.GoodsAssemblyUtils;
import com.yiling.b2b.app.goods.vo.GoodsSkuVO;
import com.yiling.b2b.app.promotion.form.ActivityGoodsPageForm;
import com.yiling.b2b.app.promotion.form.MyAppointSearchForm;
import com.yiling.b2b.app.promotion.form.SpecialActivitiAppointmentAddForm;

import com.yiling.b2b.app.promotion.form.SpecialActivitiySearchForm;
import com.yiling.b2b.app.promotion.vo.ActivityCentertVO;
import com.yiling.b2b.app.promotion.vo.ActivityGoodsInfoVO;
import com.yiling.b2b.app.promotion.vo.CombinationPackageVO;
import com.yiling.b2b.app.promotion.vo.GoodsListItemVO;
import com.yiling.b2b.app.promotion.vo.SpecialActivityAppointmentItemVO;
import com.yiling.b2b.app.promotion.vo.SpecialActivityAppointmentVO;
import com.yiling.b2b.app.promotion.vo.SpecialActivityInfoVO;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.common.web.goods.vo.GoodsItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.marketing.common.util.PromotionAreaUtil;
import com.yiling.marketing.promotion.dto.PromotionSecKillSpecialDTO;
import com.yiling.marketing.promotion.enums.PromotionPermittedTypeEnum;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.bo.CombinationBuyNumberBO;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.AvtivityCenterDTO;
import com.yiling.marketing.promotion.dto.PromotionCheckContextDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsLimitDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityAppointmentDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityAppointmentItemDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityGoodsInfoDTO;
import com.yiling.marketing.promotion.dto.SpecialAvtivityInfoDTO;
import com.yiling.marketing.promotion.dto.request.ActivityGoodsPageRequest;
import com.yiling.marketing.promotion.dto.request.PromotionAppRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityAppointmentAddRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityInfoRequest;
import com.yiling.marketing.promotion.dto.request.SpecialActivityPageRequest;
import com.yiling.marketing.promotion.enums.PromotionTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseApplyApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 促销活动主表 前端控制器
 * </p>
 *
 * @author: yong.zhang
 * @date: 2021/11/27
 */
@Slf4j
@Api(tags = "促销活动管理接口")
@RestController
@RequestMapping("/promotion/activity")
public class PromotionActivityController extends BaseController {
    @DubboReference
    PromotionActivityApi promotionActivityApi;
    @DubboReference
    GoodsApi             goodsApi;
    @DubboReference
    GoodsPriceApi        goodsPriceApi;
    @Autowired
    PictureUrlUtils      pictureUrlUtils;
    @Autowired
    GoodsAssemblyUtils   goodsAssemblyUtils;
    @DubboReference
    EnterpriseApi        enterpriseApi;
    @DubboReference
    MemberApi            memberApi;
    @DubboReference
    EnterprisePurchaseApplyApi purchaseApplyApi;
    @DubboReference
    OrderPromotionActivityApi orderPromotionActivityApi;


    @ApiOperation(value = "查询此满赠活动的商品信息")
    @PostMapping("/queryGoodsById")
    @Log(title = "查询此满赠活动的商品信息", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<GoodsListItemVO>> queryById(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody ActivityGoodsPageForm form) {
        Page<GoodsListItemVO> page = new Page<>();
        ActivityGoodsPageRequest request = PojoUtils.map(form, ActivityGoodsPageRequest.class);
        // 促销活动商品表
        Page<PromotionGoodsLimitDTO> dtoPage = promotionActivityApi.pageGoodsByActivityId(request);
        List<PromotionGoodsLimitDTO> dtoList = dtoPage.getRecords();

        if (CollUtil.isEmpty(dtoList)) {
            return Result.success(page);
        }

        List<Long> goodsIdList = dtoList.stream().map(PromotionGoodsLimitDTO::getGoodsId).collect(Collectors.toList());

        // 获取商品参与的促销活动
        PromotionAppRequest promotionRequest = PromotionAppRequest.builder().build();
        promotionRequest.setBuyerEid(staffInfo.getCurrentEid());
        promotionRequest.setGoodsIdList(goodsIdList);
        promotionRequest.setPlatform(PlatformEnum.B2B.getCode());
        List<PromotionGoodsLimitDTO> goodsLimitList = promotionActivityApi.queryGoodsPromotionInfo(promotionRequest);
        Map<Long, List<PromotionGoodsLimitDTO>> goodsLimitMap = goodsLimitList.stream().collect(Collectors.groupingBy(PromotionGoodsLimitDTO::getGoodsId));


        List<GoodsSkuDTO> goodsSkuDTOList = goodsApi.getGoodsSkuByGoodsIds(goodsIdList);
        Map<Long, List<GoodsSkuDTO>> goodsSkuMap = goodsSkuDTOList.stream().collect(Collectors.groupingBy(GoodsSkuDTO::getGoodsId));
        List<GoodsListItemVO> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(goodsIdList)) {
            List<GoodsItemVO> goodsItemVOList = goodsAssemblyUtils.assembly(goodsIdList, staffInfo.getCurrentEid());
            goodsItemVOList.forEach(e -> {
                GoodsListItemVO goodsListItemVO = PojoUtils.map(e, GoodsListItemVO.class);
                goodsListItemVO.setGoodsSkuList(PojoUtils.map(goodsSkuMap.get(e.getGoodsInfo().getId()), GoodsSkuVO.class));
                List<PromotionGoodsLimitDTO> promotionList = goodsLimitMap.get(e.getGoodsInfo().getId());
                if (CollUtil.isNotEmpty(promotionList)) {
                    goodsListItemVO.setHasSecKill(promotionList.stream().anyMatch(item -> PromotionTypeEnum.isSecKill(item.getType())));
                    goodsListItemVO.setHasSpecial(promotionList.stream().anyMatch(item -> PromotionTypeEnum.isSpecialPrice(item.getType())));
                }
                list.add(goodsListItemVO);
            });
        }
        page.setRecords(list);
        page.setCurrent(dtoPage.getCurrent());
        page.setSize(dtoPage.getSize());
        page.setTotal(dtoPage.getTotal());
        return Result.success(page);
    }


    @ApiOperation(value = "查询商家下面的单个组合包信息-组合包详情")
    @PostMapping("/queryCombinationInfo")
    public Result<CombinationPackageVO> queryCombinationInfo(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody ActivityGoodsPageForm form) {
        // 计算最大购买数量，套数。比如库存100个，。一套5个，最多购买20套。参数是买家id和活动id
        Result<CollectionObject<CombinationPackageVO>> collectionObjectResult = queryCombinationPackageByEnterpriseId(staffInfo, form);
        CollectionObject<CombinationPackageVO> combinationPackage = collectionObjectResult.getData();
        ArrayList<CombinationPackageVO> combinationPackagelist = (ArrayList) combinationPackage.getList();
        if (CollectionUtils.isEmpty(combinationPackagelist)) {
            return Result.success(new CombinationPackageVO());
        }
        // 获取最大购买数量
        CombinationPackageVO combinationPackageVO = combinationPackagelist.get(0);
        List<GoodsSkuDTO> goodsSkuByIds = combinationPackageVO.getGoodsSkuDTOS();
        Map<Long, GoodsSkuDTO> goodsSkuDTOMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
        List<PromotionGoodsLimitDTO> goodsLimitDTOS = combinationPackageVO.getGoodsLimitDTOS();
        List<BigDecimal> canBuyNums = new ArrayList<>();
        goodsLimitDTOS.forEach(item -> {
            Integer allowBuyCount = item.getAllowBuyCount();
            GoodsSkuDTO goodsSkuDTO = goodsSkuDTOMap.get(item.getGoodsSkuId());
            if ((goodsSkuDTO != null) && (goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty()) != 0) {
                long AvailableNum = goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty();
                BigDecimal canBuyNum = NumberUtil.round(NumberUtil.div(AvailableNum, allowBuyCount.longValue()), 0);
                canBuyNums.add(canBuyNum);
            } else {
                canBuyNums.add(new BigDecimal(0));
            }
        });
        BigDecimal bigDecimal = canBuyNums.stream().min(Comparator.comparing(x -> x)).orElse(new BigDecimal(0));
        combinationPackageVO.setMaxBuyNum(bigDecimal.intValue());
        // 获取组合包的历史记录
        CombinationBuyNumberBO combinationBuyNumberBO = orderPromotionActivityApi.sumCombinationActivityNumber(staffInfo.getCurrentEid(), form.getId());
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
        }else {
            Long sumQty = combinationBuyNumberBO.getSumQty();
            int combinationCanBuyNum = (combinationPackageVO.getTotalNum() - sumQty.intValue())<0?0:combinationPackageVO.getTotalNum() - sumQty.intValue();
            combinationPackageVO.setSurplusBuyNum(combinationCanBuyNum);
        }
        return Result.success(combinationPackageVO);
    }

    @ApiOperation(value = "查询商家下面的组合包信息列表")
    @PostMapping("/queryCombinationPackageByEnterpriseId")
    public Result<CollectionObject<CombinationPackageVO>> queryCombinationPackageByEnterpriseId(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody ActivityGoodsPageForm form) {
        ActivityGoodsPageRequest request = PojoUtils.map(form, ActivityGoodsPageRequest.class);
        request.setType(PromotionTypeEnum.COMBINATION_PACKAGE.getType().toString());
        List<PromotionGoodsLimitDTO> promotionGoodsLimitPage = promotionActivityApi.pagePromotionByShopId(request);
        if (CollectionUtils.isEmpty(promotionGoodsLimitPage)) {
            return Result.success(new CollectionObject(new ArrayList<CombinationPackageVO>()));
        }
        List<CombinationPackageVO> combinationPackageVOS = new ArrayList<>();
        List<Long> goodsIdList = new ArrayList<>();
        List<Long> goodsSukIdsList = new ArrayList<>();
        List<Long> promotionActivityIds = new ArrayList<>();
        promotionGoodsLimitPage.forEach(item -> {
            promotionActivityIds.add(item.getPromotionActivityId());
            List<PromotionGoodsLimitDTO> goodsLimitDTOS = item.getGoodsLimitDTOS();
            List<Long> goods = goodsLimitDTOS.stream().map(PromotionGoodsLimitDTO::getGoodsId).collect(Collectors.toList());
            goodsIdList.addAll(goods);
            List<Long> goodsSukIds = goodsLimitDTOS.stream().map(PromotionGoodsLimitDTO::getGoodsSkuId).collect(Collectors.toList());
            goodsSukIdsList.addAll(goodsSukIds);
        });
        QueryGoodsPriceRequest queryPriceRequest = new QueryGoodsPriceRequest();
        queryPriceRequest.setCustomerEid(staffInfo.getCurrentEid());
        queryPriceRequest.setGoodsIds(goodsIdList);
        // 通过买家id校验终端，区域，企业，企业类型，区分是否可用。通过sku的库存，判断是否有货
        EnterpriseDTO enterprise = enterpriseApi.getById(staffInfo.getCurrentEid());
        CurrentMemberForMarketingDTO member = memberApi.getCurrentMemberForMarketing(staffInfo.getCurrentEid());
        // 计算组合包价格
        Map<Long, GoodsPriceDTO> goodsPriceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryPriceRequest);
        List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSukIdsList);
        Map<Long, GoodsSkuDTO> skuGoodsMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(goodsSkuByIds)) {
            skuGoodsMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
        }
        Map<Long, GoodsSkuDTO> finalSkuGoodsMap = skuGoodsMap;
        promotionGoodsLimitPage.forEach(item -> {
            PromotionCheckContextDTO contextDTO = PromotionCheckContextDTO.builder().build();
            contextDTO.setEnterprise(enterprise);
            contextDTO.setMember(member);
            contextDTO.setPromotionActivityId(item.getPromotionActivityId());
            PromotionSecKillSpecialDTO secKillSpecialDTOS = item.getSecKillSpecialDTO();
            contextDTO.setSecKillSpecialDTO(secKillSpecialDTOS);
            Boolean available = promotionActivityApi.promotionIsAvailableByContext(contextDTO);
            // 如果不可用，不显示给前端。如果可用判断是否有货,无货也展示.这是b2b接口所以只展示1-B2B的组合包。2-销售助手的不展示
            if (available && item.getPlatformSelected().contains("1")) {
                CombinationPackageVO goodsListItemVO = PojoUtils.map(item, CombinationPackageVO.class);
                goodsListItemVO.setInStock(1);
                goodsListItemVO.setGoodsSkuDTOS(goodsSkuByIds);
                List<PromotionGoodsLimitDTO> goodsLimitDTOS = goodsListItemVO.getGoodsLimitDTOS();
                boolean zeroQty = goodsLimitDTOS.stream().anyMatch(e -> {
                    GoodsSkuDTO goodsSkuDTO = finalSkuGoodsMap.get(e.getGoodsSkuId());
                    return (goodsSkuDTO == null) || goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty() == 0;
                });
                if (zeroQty) {
                    goodsListItemVO.setInStock(0);
                }
                List<Long> goodsIds = new ArrayList<>();
                List<Integer> allowBuyCount = new ArrayList<>();
                List<BigDecimal> sellingPrice = new ArrayList<>();
                List<BigDecimal> combinationPackagePrice = new ArrayList<>();
                goodsLimitDTOS.forEach(goodsLimit -> {
                    goodsIds.add(goodsLimit.getGoodsId());
                    allowBuyCount.add(goodsLimit.getAllowBuyCount());
                    GoodsPriceDTO goodsPriceDTO = goodsPriceMap.get(goodsLimit.getGoodsId());
                    BigDecimal singleGoodsPrice = NumberUtil.round(NumberUtil.mul(goodsPriceDTO.getLinePrice(), goodsLimit.getAllowBuyCount()), 2);
                    BigDecimal promotionGoodsPrice = NumberUtil.round(NumberUtil.mul(goodsLimit.getPromotionPrice(), goodsLimit.getAllowBuyCount()), 2);
                    sellingPrice.add(singleGoodsPrice);
                    combinationPackagePrice.add(promotionGoodsPrice);
                });
                goodsListItemVO.setGoodsIds(goodsIds);
                goodsListItemVO.setAllowBuyCount(allowBuyCount);
                goodsListItemVO.setSellingPrice(sellingPrice.stream().reduce(BigDecimal::add).get());
                goodsListItemVO.setCombinationPackagePrice(combinationPackagePrice.stream().reduce(BigDecimal::add).get());
                goodsListItemVO.setCombinationDiscountPrice(NumberUtil.round(NumberUtil.sub(goodsListItemVO.getSellingPrice(), goodsListItemVO.getCombinationPackagePrice()), 2));
                combinationPackageVOS.add(goodsListItemVO);
            }
        });
        return Result.success(new CollectionObject(combinationPackageVOS));
    }


    @ApiOperation(value = "组合包活动搜索")
    @PostMapping("/package/search")
    public Result<CollectionObject<CombinationPackageVO>> combinationPackageActivitySearch(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody ActivityGoodsPageForm form) {
        PromotionAppRequest request = PojoUtils.map(form, PromotionAppRequest.class);
        if (form.getShopEid() == null) {
            return Result.failed("店铺企业Eid为空!");
        }
        request.setPromotionActivityId(form.getId());
        request.setBuyerEid(staffInfo.getCurrentEid());
        request.setEIdList(ListUtil.toList(form.getShopEid()));
        request.setPlatform(PlatformEnum.B2B.getCode());
        request.setTypeList(ListUtil.toList(PromotionTypeEnum.COMBINATION_PACKAGE.getType()));
        // 表示通过商品名称搜索
        request.setSelectType(2);
        List<PromotionGoodsLimitDTO> promotionGoodsLimitPage = promotionActivityApi.queryGoodsPromotionInfo(request);
        if (CollectionUtils.isEmpty(promotionGoodsLimitPage)) {
            return Result.success(new CollectionObject(new ArrayList<CombinationPackageVO>()));
        }
        // 去除重复商品数据
        promotionGoodsLimitPage = promotionGoodsLimitPage.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PromotionGoodsLimitDTO::getPromotionActivityId))), ArrayList::new));
        List<CombinationPackageVO> combinationPackageVOS = new ArrayList<>();
        List<Long> goodsIdList = new ArrayList<>();
        List<Long> promotionActivityIds = new ArrayList<>();
        promotionGoodsLimitPage.forEach(item -> {
            promotionActivityIds.add(item.getPromotionActivityId());
            List<PromotionGoodsLimitDTO> goodsLimitDTOS = item.getGoodsLimitDTOS();
            List<Long> goods = goodsLimitDTOS.stream().map(PromotionGoodsLimitDTO::getGoodsId).collect(Collectors.toList());
            goodsIdList.addAll(goods);
        });
        QueryGoodsPriceRequest queryPriceRequest = new QueryGoodsPriceRequest();
        queryPriceRequest.setCustomerEid(staffInfo.getCurrentEid());
        queryPriceRequest.setGoodsIds(goodsIdList);
        // 通过买家id校验终端，区域，企业，企业类型，区分是否可用。通过sku的库存，判断是否有货
        EnterpriseDTO enterprise = enterpriseApi.getById(staffInfo.getCurrentEid());
        CurrentMemberForMarketingDTO member = memberApi.getCurrentMemberForMarketing(staffInfo.getCurrentEid());
        // 查询店铺建采数据
        Map<Long, Integer> purchaseApplyStatus = purchaseApplyApi.getPurchaseApplyStatus(ListUtil.toList(form.getShopEid()), staffInfo.getCurrentEid());
        // 计算组合包价格
        Map<Long, GoodsPriceDTO> goodsPriceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryPriceRequest);
        promotionGoodsLimitPage.forEach(item -> {
            PromotionCheckContextDTO contextDTO = PromotionCheckContextDTO.builder().build();
            contextDTO.setEnterprise(enterprise);
            contextDTO.setMember(member);
            contextDTO.setPromotionActivityId(item.getPromotionActivityId());
            Boolean available = promotionActivityApi.promotionIsAvailable(contextDTO);
            // 如果不可用，不显示给前端。如果可用判断是否有货,无货也展示.这是b2b接口所以只展示1-B2B的组合包。2-销售助手的不展示
            if (available) {
                CombinationPackageVO goodsListItemVO = PojoUtils.map(item, CombinationPackageVO.class);
                goodsListItemVO.setInStock(1);
                List<PromotionGoodsLimitDTO> goodsLimitDTOS = goodsListItemVO.getGoodsLimitDTOS();
                List<Long> goodsSukIds = goodsLimitDTOS.stream().map(PromotionGoodsLimitDTO::getGoodsSkuId).collect(Collectors.toList());
                List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSukIds);
                Map<Long, GoodsSkuDTO> skuGoodsMap = new HashMap<>();
                if (CollectionUtil.isNotEmpty(goodsSkuByIds)) {
                    skuGoodsMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
                }
                Map<Long, GoodsSkuDTO> finalSkuGoodsMap = skuGoodsMap;
                boolean zeroQty = goodsLimitDTOS.stream().anyMatch(e -> {
                    GoodsSkuDTO goodsSkuDTO = finalSkuGoodsMap.get(e.getGoodsSkuId());
                    return (goodsSkuDTO == null) || goodsSkuDTO.getQty() - goodsSkuDTO.getFrozenQty() == 0;
                });
                if (zeroQty) {
                    goodsListItemVO.setInStock(0);
                }
                List<Long> goodsIds = new ArrayList<>();
                List<BigDecimal> sellingPrice = new ArrayList<>();
                List<BigDecimal> combinationPackagePrice = new ArrayList<>();
                goodsLimitDTOS.forEach(goodsLimit -> {
                    goodsIds.add(goodsLimit.getGoodsId());
                    GoodsPriceDTO goodsPriceDTO = goodsPriceMap.get(goodsLimit.getGoodsId());
                    BigDecimal singleGoodsPrice = NumberUtil.round(NumberUtil.mul(goodsPriceDTO.getLinePrice(), goodsLimit.getAllowBuyCount()), 2);
                    BigDecimal promotionGoodsPrice = NumberUtil.round(NumberUtil.mul(goodsLimit.getPromotionPrice(), goodsLimit.getAllowBuyCount()), 2);
                    sellingPrice.add(singleGoodsPrice);
                    combinationPackagePrice.add(promotionGoodsPrice);
                });
                goodsListItemVO.setGoodsIds(goodsIds);
                goodsListItemVO.setSellingPrice(sellingPrice.stream().reduce(BigDecimal::add).get());
                goodsListItemVO.setCombinationPackagePrice(combinationPackagePrice.stream().reduce(BigDecimal::add).get());
                goodsListItemVO.setCombinationDiscountPrice(NumberUtil.round(NumberUtil.sub(goodsListItemVO.getSellingPrice(), goodsListItemVO.getCombinationPackagePrice()), 2));
                goodsListItemVO.setStatus(purchaseApplyStatus.get(item.getEid()) == 3 ? 1 : 2);

                combinationPackageVOS.add(goodsListItemVO);
            }
        });
        return Result.success(new CollectionObject(combinationPackageVOS));

    }

    @ApiOperation(value = "新增预约")
    @PostMapping("/appointment/add")
    public Result appointmentAdd(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody SpecialActivitiAppointmentAddForm appointmentAddForm) {
        appointmentAddForm.setAppointmentUserId(staffInfo.getCurrentUserId());
        appointmentAddForm.setAppointmentUserEid(staffInfo.getCurrentEid());
        SpecialActivityAppointmentAddRequest appointmentAddRequest = PojoUtils.map(appointmentAddForm, SpecialActivityAppointmentAddRequest.class);
        Boolean result = promotionActivityApi.appointmentAdd(appointmentAddRequest);
        return Result.success(result);
    }

    @ApiOperation(value = "我的预约管理")
    @PostMapping("/myAppointment")
    public Result<Page<SpecialActivityAppointmentItemVO>> queryMyAppointment(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody MyAppointSearchForm myAppointSearchForm) {
        SpecialActivityPageRequest request = PojoUtils.map(myAppointSearchForm, SpecialActivityPageRequest.class);
        Page<SpecialAvtivityAppointmentItemDTO> appointmentDTO = promotionActivityApi.queryMyAppointment(staffInfo.getCurrentEid(), request);
        return Result.success(PojoUtils.map(appointmentDTO, SpecialActivityAppointmentItemVO.class));
    }

    @ApiOperation(value = "我的预约数量")
    @PostMapping("/myAppointmentCount")
    public Result<SpecialActivityAppointmentItemVO> myAppointmentCount(@CurrentUser CurrentStaffInfo staffInfo) {
        SpecialAvtivityAppointmentItemDTO appointmentDTO = promotionActivityApi.myAppointmentCount(staffInfo.getCurrentEid());
        return Result.success(PojoUtils.map(appointmentDTO, SpecialActivityAppointmentItemVO.class));
    }

    @ApiOperation(value = "活动中心列表接口")
    @PostMapping("/activityCenter")
    public Result<ActivityCentertVO> activityCenter(@CurrentUser CurrentStaffInfo staffInfo) {
        AvtivityCenterDTO appointmentDTO = promotionActivityApi.activityCenter(staffInfo.getCurrentEid());
        return Result.success(PojoUtils.map(appointmentDTO, ActivityCentertVO.class));
    }

    @ApiOperation(value = "专场活动列表接口")
    @PostMapping("/specialActivityInfo")
    public Result<CollectionObject<SpecialActivityInfoVO>> specialActivityInfo(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody SpecialActivitiySearchForm form) {
        SpecialActivityInfoRequest request = PojoUtils.map(form, SpecialActivityInfoRequest.class);
        request.setCurrentEid(staffInfo.getCurrentEid());
        request.setUserId(staffInfo.getCurrentUserId());
        List<SpecialAvtivityInfoDTO> appointmentDTO = promotionActivityApi.specialActivityInfo(request);
        CollectionObject collectionObject = new CollectionObject(appointmentDTO);
        return Result.success(collectionObject);
    }

    @ApiOperation(value = "某个营销活动下的商品信息")
    @PostMapping("/getGoodsInfoByActivityId")
    public Result<CollectionObject<ActivityGoodsInfoVO>> getGoodsInfoByActivityId(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody SpecialActivitiySearchForm form) {
        SpecialActivityInfoRequest request = PojoUtils.map(form, SpecialActivityInfoRequest.class);
        request.setCurrentEid(staffInfo.getCurrentEid());
        request.setUserId(staffInfo.getCurrentUserId());
        List<SpecialAvtivityGoodsInfoDTO> appointmentDTO = promotionActivityApi.getGoodsInfoByActivityId(request);
        if (CollectionUtils.isNotEmpty(appointmentDTO) && request.getType() != 4) {
            List<Long> goodsIds = appointmentDTO.stream().map(SpecialAvtivityGoodsInfoDTO::getGoodsId).collect(Collectors.toList());
            List<StandardGoodsBasicDTO> standardGoodsBasicDTOList = goodsApi.batchQueryStandardGoodsBasic(goodsIds);
            Map<Long, StandardGoodsBasicDTO> standardGoodsBasicDTOMap = standardGoodsBasicDTOList.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, Function.identity()));
            appointmentDTO.forEach(item -> {
                StandardGoodsBasicDTO standardGoodsBasicDTO = standardGoodsBasicDTOMap.get(item.getGoodsId());
                item.setPic(pictureUrlUtils.getGoodsPicUrl(standardGoodsBasicDTO.getPic()));
                item.setShopId(request.getEid());
            });
            List<ActivityGoodsInfoVO> goodsInfoVOS = PojoUtils.map(appointmentDTO, ActivityGoodsInfoVO.class);
            return Result.success(new CollectionObject(goodsInfoVOS));
        }
        if (CollectionUtils.isNotEmpty(appointmentDTO) && request.getType() == 4) {
            // 如果组合包只有一个信息
            List<SpecialAvtivityGoodsInfoDTO> goodsLimitDTOS = appointmentDTO.get(0).getGoodsInfoDTOS();
            List<Long> goodsIdList = goodsLimitDTOS.stream().map(SpecialAvtivityGoodsInfoDTO::getGoodsId).collect(Collectors.toList());
            QueryGoodsPriceRequest queryPriceRequest = new QueryGoodsPriceRequest();
            queryPriceRequest.setCustomerEid(staffInfo.getCurrentEid());
            queryPriceRequest.setGoodsIds(goodsIdList);
            // 计算组合包价格
            Map<Long, GoodsPriceDTO> goodsPriceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryPriceRequest);
            List<BigDecimal> sellingPrice = new ArrayList<>();
            goodsLimitDTOS.forEach(goodsLimit -> {
                GoodsPriceDTO goodsPriceDTO = goodsPriceMap.get(goodsLimit.getGoodsId());
                BigDecimal singleGoodsPrice = NumberUtil.round(NumberUtil.mul(goodsPriceDTO.getLinePrice(), goodsLimit.getAllowBuyCount()), 2);
                sellingPrice.add(singleGoodsPrice);
            });
            List<ActivityGoodsInfoVO> goodsInfoVOS = PojoUtils.map(appointmentDTO, ActivityGoodsInfoVO.class);
            ActivityGoodsInfoVO activityGoodsInfoVO = goodsInfoVOS.get(0);
            activityGoodsInfoVO.setSellingPrice(sellingPrice.stream().reduce(BigDecimal::add).get());
            activityGoodsInfoVO.setEid(request.getEid());
            activityGoodsInfoVO.setPromotionActivityId(request.getPromotionActivityId());
            return Result.success(new CollectionObject(goodsInfoVOS));
        }
        return Result.success(new CollectionObject(appointmentDTO));
    }
    /**
     * 允许购买企业类型校验
     *
     * @param context
     * @return
     */
    public boolean permittedEnterpriseTypeCheck(PromotionCheckContextDTO context) {
        if (Objects.isNull(context)) {
            return false;
        }

        if (Objects.isNull(context.getSecKillSpecialDTO())) {
            return false;
        }

        Integer type = context.getEnterprise().getType();

        // 企业类型 1-全部，2-部分
        Integer permittedEnterpriseType = context.getSecKillSpecialDTO().getPermittedEnterpriseType();

        // 企业类型json
        String permittedEnterpriseDetail = context.getSecKillSpecialDTO().getPermittedEnterpriseDetail();

        if (PromotionPermittedTypeEnum.ALL.getType().equals(permittedEnterpriseType)) {
            return true;
        }
        if (PromotionPermittedTypeEnum.PART.getType().equals(permittedEnterpriseType) && StringUtils.isNotBlank(permittedEnterpriseDetail)) {
            List<Integer> integers = JSONObject.parseArray(permittedEnterpriseDetail, Integer.class);
            if (CollUtil.isNotEmpty(integers) && integers.contains(type)) {
                return true;
            }
        }
        log.info("[permittedEnterpriseTypeCheck]购买企业类型校验不通过，参数：{}", context);
        return false;
    }

    /**
     * 允许购买区域校验
     *
     * @param context
     * @return
     */
    public boolean permittedAreaTypeCheck(PromotionCheckContextDTO context) {
        if (Objects.isNull(context)) {
            return false;
        }

        if (Objects.isNull(context.getSecKillSpecialDTO())) {
            return false;
        }

        // 允许购买区域 1-全部，2-部分
        Integer permittedAreaType = context.getSecKillSpecialDTO().getPermittedAreaType();

        // 允许购买区域明细json
        String permittedAreaDetail = context.getSecKillSpecialDTO().getPermittedAreaDetail();

        // 所属区域编码
        String regionCode = context.getEnterprise().getRegionCode();

        if (PromotionPermittedTypeEnum.ALL.getType().equals(permittedAreaType)) {
            return true;
        }
        if (PromotionPermittedTypeEnum.PART.getType().equals(permittedAreaType) && StringUtils.isNotBlank(permittedAreaDetail)) {
            boolean checkResult = PromotionAreaUtil.check(permittedAreaDetail, regionCode);
            log.info("[terminalTypeCheck]购买区域校验结果，PromotionAreaUtil.check：{}，参数：{}", checkResult, context);
            return checkResult;

        }
        log.info("[terminalTypeCheck]购买区域校验不通过，参数：{}", context);
        return false;
    }
}