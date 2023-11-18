package com.yiling.b2b.app.common.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.b2b.app.common.form.HotGoodsPageForm;
import com.yiling.b2b.app.common.vo.HotGoodsVO;
import com.yiling.b2b.app.goods.utils.GoodsAssemblyUtils;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.common.web.goods.vo.GoodsPriceVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.StandardGoodsBasicDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.QuerySaleSpecificationPageListRequest;
import com.yiling.goods.standard.dto.StandardGoodsBasicInfoDTO;
import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCanBuyEidRequest;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 精选药品查询
 *
 * @author: yong.zhang
 * @date: 2021/10/29
 */
@Slf4j
@Api(tags = "精选药品接口")
@RestController
@RequestMapping("/hotGoods")
public class HotGoodsController extends BaseController {
    @DubboReference
    B2bGoodsApi b2bGoodsApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    GoodsPriceApi goodsPriceApi;
    @DubboReference
    ShopApi shopApi;
    @DubboReference
    CustomerApi customerApi;
    @Autowired
    PictureUrlUtils pictureUrlUtils;
    @Autowired
    GoodsAssemblyUtils goodsAssemblyUtils;

    @ApiOperation(value = "app首页精选药品查询")
    @PostMapping("/pageHotGoods")
    public Result<Page<HotGoodsVO>> query(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody HotGoodsPageForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByParentId(Constants.YILING_EID);
        List<Long> eidList = enterpriseDTOList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
        //获取销售商品ID
        List<Long> eids = shopApi.getSellEidByEidSaleArea(staffInfo.getCurrentEid());
        if (CollUtil.isEmpty(eids)) {
            return Result.success(new Page<>());
        }
        //查询以岭的商品规格Id
        List<GoodsDTO> list = new ArrayList<>();
        for (Long eid : eidList) {
            list.addAll(goodsApi.getGoodsListByEid(eid));
        }

        List<Long> sellSpecificationsIdList = list.stream().map(e -> e.getSellSpecificationsId()).collect(Collectors.toList());
        //获取商业公司销售以岭品规Id集合
        List<Long> sellSpecificationsIds = b2bGoodsApi.getIdsBySpecificationsIdsAndIncludeEidsAndCustomerEid(sellSpecificationsIdList, eids, staffInfo.getCurrentEid());
        if(CollectionUtil.isEmpty(sellSpecificationsIds)){
            return Result.success(new Page<>());
        }
        request.setIncludeSellSpecificationsIds(sellSpecificationsIds);
        request.setEidList(eidList);
        Page<GoodsListItemBO> goodsListItemBOPage = b2bGoodsApi.queryB2bGoodsPageList(request);
        Page<HotGoodsVO> goodsVOPage = PojoUtils.map(goodsListItemBOPage, HotGoodsVO.class);
        log.info("pageHotGoods-[goodsVOPage]:{},form:{}", JSONUtil.toJsonStr(goodsVOPage),JSONUtil.toJsonStr(form));
        //若无分页数据则不执行后续逻辑
        if(CollectionUtil.isEmpty(goodsListItemBOPage.getRecords())){
            return Result.success(goodsVOPage);
        }
        List<HotGoodsVO> goodsVOPageRecords = goodsVOPage.getRecords();
        List<Long> goodsIdList = goodsVOPageRecords.stream().map(HotGoodsVO::getId).collect(Collectors.toList());
        List<StandardGoodsBasicDTO> goodsBasicDTOS = goodsApi.batchQueryStandardGoodsBasic(goodsIdList);
        Map<Long, StandardGoodsBasicDTO> goodsBasicDTOMap = goodsBasicDTOS.stream().collect(Collectors.toMap(StandardGoodsBasicDTO::getId, o -> o, (k1, k2) -> k1));

        List<Long> specificationIdList = goodsVOPageRecords.stream().map(HotGoodsVO::getSellSpecificationsId).collect(Collectors.toList());
        Map<Long, List<Long>> sellSpecificationMap = b2bGoodsApi.getSellerGoodsIdsBySellSpecificationsIds(specificationIdList, eids);

        //获取所有商品
        List<Long> allGoodId = sellSpecificationMap.values().stream().collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
        //获取商品定价系统
        QueryGoodsPriceRequest queryGoodsPriceRequest = new QueryGoodsPriceRequest();
        queryGoodsPriceRequest.setCustomerEid(staffInfo.getCurrentEid());
        queryGoodsPriceRequest.setGoodsIds(allGoodId);
        Map<Long, GoodsPriceDTO> allPriceMap = goodsPriceApi.queryB2bGoodsPriceInfoMap(queryGoodsPriceRequest);
        goodsVOPageRecords.forEach(hotGoodsVO -> {
            BigDecimal minPrice = BigDecimal.ZERO;
            //规格id拿到商品id
            List<Long> goodsIds = sellSpecificationMap.getOrDefault(hotGoodsVO.getSellSpecificationsId(), ListUtil.empty());
            //商品id获取商品价格
            List<GoodsPriceDTO> priceList = goodsIds.stream().filter(goodsId-> ObjectUtil.isNotNull(allPriceMap.get(goodsId))).map(id -> {
                return allPriceMap.get(id);
            }).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(priceList)) {
                for (GoodsPriceDTO goodsPriceDTO : priceList) {
                    if(goodsPriceDTO.getIsShow()) {
                        if (minPrice.compareTo(BigDecimal.ZERO) == 0) {
                            minPrice = goodsPriceDTO.getBuyPrice();
                        } else if (minPrice.compareTo(goodsPriceDTO.getBuyPrice()) > 0) {
                            minPrice = goodsPriceDTO.getBuyPrice();
                        }
                    }
                }
            }
            hotGoodsVO.setPrice(minPrice);
            GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
            goodsPriceVO.setBuyPrice(minPrice);
            if (minPrice.compareTo(BigDecimal.ZERO) == 0) {
                goodsPriceVO.setIsShow(false);
            } else {
                goodsPriceVO.setIsShow(true);
            }
            hotGoodsVO.setGoodsPriceVO(goodsPriceVO);
            hotGoodsVO.setSellerCount(sellSpecificationMap.getOrDefault(hotGoodsVO.getSellSpecificationsId(), ListUtil.empty()).size());

            StandardGoodsBasicDTO standardGoodsBasicDTO = goodsBasicDTOMap.get(hotGoodsVO.getId());
            if (null != standardGoodsBasicDTO) {
                StandardGoodsBasicInfoDTO standardGoods = standardGoodsBasicDTO.getStandardGoods();
                hotGoodsVO.setName(standardGoods.getName());
                hotGoodsVO.setManufacturer(standardGoods.getManufacturer());
            }
            hotGoodsVO.setPic(pictureUrlUtils.getGoodsPicUrl(hotGoodsVO.getPic()));
        });
        return Result.success(goodsVOPage);
    }

    @ApiOperation(value = "app首页瀑布流商品")
    @PostMapping("/waterfallGoods")
    public Result<Page<HotGoodsVO>> waterfallGoods(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody HotGoodsPageForm form){
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;
        //获取销售区域内eid
        List<Long> eids = shopApi.getSellEidByEidSaleArea(buyerEid);
        if (CollUtil.isEmpty(eids)) {
            return Result.success(new Page<>());
        }
        //建采商家
        QueryCanBuyEidRequest enterpriseRequest = new QueryCanBuyEidRequest();
        enterpriseRequest.setCustomerEid(buyerEid);
        enterpriseRequest.setLine(EnterpriseCustomerLineEnum.B2B.getCode());
        List<Long> purchaseEids = customerApi.getEidListByCustomerEid(enterpriseRequest);
        QuerySaleSpecificationPageListRequest request = PojoUtils.map(form, QuerySaleSpecificationPageListRequest.class);
        request.setSellerEids(eids);
        //非以岭商品
        request.setPurchaseEids(purchaseEids);
        DateTime yesterday = DateUtil.yesterday();
        //查询前30天销量
        request.setSaleTimStart(DateUtil.beginOfDay(DateUtil.offsetDay(yesterday,-29)));
        request.setSaleTimEnd(DateUtil.endOfDay(yesterday));
        Page<GoodsListItemBO> page = b2bGoodsApi.queryWaterfallSpecificationPage(request);
        if(CollectionUtil.isNotEmpty(page.getRecords())){
            //穿插一个以岭品
            request.setSize(1);
            request.setYlFlag(1);
            Page<GoodsListItemBO> ylPage = b2bGoodsApi.queryWaterfallSpecificationPage(request);
            page.getRecords().addAll(ylPage.getRecords());
        }
        Page<HotGoodsVO> goodsVOPage = PojoUtils.map(page, HotGoodsVO.class);
        if(CollectionUtil.isNotEmpty(goodsVOPage.getRecords())){
            List<Long> specIds = goodsVOPage.getRecords().stream().map(HotGoodsVO::getSellSpecificationsId).collect(Collectors.toList());
            //每个规格最低价
            Map<Long, BigDecimal> priceMap = b2bGoodsApi.getMinPriceBySpecificationsIds(specIds);
            //每个规格的售卖商家
            Map<Long, List<Long>> sellerEidMap = b2bGoodsApi.getSellerEidsBySellSpecificationsIds(specIds,eids);
            goodsVOPage.getRecords().forEach(hotGoodsVO -> {
                BigDecimal minPrice = priceMap.getOrDefault(hotGoodsVO.getSellSpecificationsId(),BigDecimal.ZERO);
                GoodsPriceVO goodsPriceVO = new GoodsPriceVO();
                goodsPriceVO.setMinPrice(minPrice);
                if (minPrice.compareTo(BigDecimal.ZERO) == 0) {
                    goodsPriceVO.setIsShow(false);
                } else {
                    goodsPriceVO.setIsShow(true);
                }
                hotGoodsVO.setGoodsPriceVO(goodsPriceVO);
                List<Long> sellerEids = sellerEidMap.getOrDefault(hotGoodsVO.getId(), ListUtil.empty());
                hotGoodsVO.setSellerCount(sellerEids.size());
                //在售卖商家内取建采商家
                List<Long> purchaseEidsBySeller = ListUtil.toList(CollectionUtil.intersection(purchaseEids, sellerEids));
                hotGoodsVO.setPurchaseCount(purchaseEidsBySeller.size());
                hotGoodsVO.setPic(pictureUrlUtils.getGoodsPicUrl(hotGoodsVO.getPic()));
            });
        }

        return Result.success(goodsVOPage);
    }
}
