package com.yiling.f2b.web.mall.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.common.web.goods.vo.GoodsItemVO;
import com.yiling.f2b.web.common.utils.GoodsAssemblyUtils;
import com.yiling.f2b.web.common.utils.VoUtils;
import com.yiling.f2b.web.mall.vo.BannerVO;
import com.yiling.f2b.web.mall.vo.CategoryVO;
import com.yiling.f2b.web.mall.vo.GoodsHotWordsVO;
import com.yiling.f2b.web.mall.vo.MallBannerVO;
import com.yiling.f2b.web.mall.vo.MallHeadVO;
import com.yiling.f2b.web.mall.vo.NavigationInfoVO;
import com.yiling.f2b.web.mall.vo.NoticeInfoVO;
import com.yiling.f2b.web.mall.vo.RecommendGoodsVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.enums.GoodsSourceEnum;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.mall.banner.api.BannerApi;
import com.yiling.mall.banner.dto.BannerDTO;
import com.yiling.mall.cart.api.CartApi;
import com.yiling.mall.cart.enums.CartGoodsSourceEnum;
import com.yiling.mall.category.api.CategoryApi;
import com.yiling.mall.category.api.CategoryGoodsApi;
import com.yiling.mall.category.dto.HomeCategoryDTO;
import com.yiling.mall.category.dto.HomeCategoryGoodsDTO;
import com.yiling.mall.hotwords.api.GoodsHotWordsApi;
import com.yiling.mall.hotwords.dto.GoodsHotWordsAvailableDTO;
import com.yiling.mall.navigation.api.NavigationInfoApi;
import com.yiling.mall.navigation.dto.NavigationInfoFrontDTO;
import com.yiling.mall.notice.api.NoticeInfoApi;
import com.yiling.mall.notice.dto.NoticeInfoDTO;
import com.yiling.mall.recommend.api.RecommendGoodsApi;
import com.yiling.mall.recommend.dto.RecommendGoodsDTO;
import com.yiling.order.order.api.OrderApi;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.request.AgreementPurchaseGoodsRequest;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/6/16
 */
@Slf4j
@RestController
@RequestMapping("/mall")
@Api(tags = "商城首页接口")
public class IndexController extends BaseController {

    @DubboReference
    GoodsHotWordsApi goodsHotWordsApi;

    @DubboReference
    NavigationInfoApi navigationInfoApi;

    @DubboReference
    NoticeInfoApi noticeInfoApi;

    @DubboReference
    RecommendGoodsApi recommendGoodsApi;

    @DubboReference
    BannerApi bannerApi;

    @DubboReference
    AgreementBusinessApi agreementBusinessApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    PopGoodsApi popGoodsApi;

    @DubboReference
    CategoryApi categoryApi;

    @DubboReference
    CategoryGoodsApi categoryGoodsApi;

    @DubboReference
    AgreementGoodsApi agreementGoodsApi;

    @DubboReference
    CartApi cartApi;

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    ProcurementRelationGoodsApi procurementRelationGoodsApi;

    @Autowired
    VoUtils voUtils;

    @Autowired
    GoodsAssemblyUtils goodsAssemblyUtils;

    /**
     * 商城首页的头部信息接口，包含内容：
     * 1、页面顶部购物车商品件数
     * 2、以岭logo
     * 3、搜索框下面的搜索热词
     * 4、自定义导航
     *
     * @param staffInfo
     * @return
     */
    @ApiOperation(value = "商城页面头部信息接口")
    @GetMapping(value = "/head")
    public Result<MallHeadVO> head(@CurrentUser CurrentStaffInfo staffInfo) {
        MallHeadVO mallHeadVO = new MallHeadVO();
        // 获取购物车商品件数
        if (staffInfo != null) {
            mallHeadVO.setCartGoodsNum(cartApi.getCartGoodsNum(staffInfo.getCurrentEid(), PlatformEnum.POP, CartGoodsSourceEnum.POP));
        } else {
            mallHeadVO.setCartGoodsNum(0);
        }

        // 获取logo
        {
            mallHeadVO.setYilingLogo("前端写死");
        }

        // 获取搜索热词列表
        {
            Integer limit = 5;
            List<GoodsHotWordsAvailableDTO> searchHotWordDTOList = goodsHotWordsApi.getAvailableGoodsHotWords(-1);
            /*List<GoodsHotWordsVO> goodsHotWordVOList = new ArrayList<>(limit);
            for (GoodsHotWordsAvailableDTO goodsHotWordsAvailableDTO : searchHotWordDTOList) {
                if (goodsHotWordVOList.size() <= limit) {
                    PopGoodsDTO popGoodsDTO = popGoodsApi.getPopGoodsByGoodsId(goodsHotWordsAvailableDTO.getGoodsId());
                    if (popGoodsDTO != null && popGoodsDTO.getGoodsStatus() != 1) {
                        continue;
                    }
                    GoodsHotWordsVO goodsHotWordsVO = PojoUtils.map(goodsHotWordsAvailableDTO, GoodsHotWordsVO.class);
                    goodsHotWordVOList.add(goodsHotWordsVO);
                }
            }*/
            mallHeadVO.setGoodsHotWordsList(PojoUtils.map(searchHotWordDTOList, GoodsHotWordsVO.class));
        }

        // 获取自定义导航列表
        {
            Integer limit = 3;
            List<NavigationInfoFrontDTO> navigationDTOList = navigationInfoApi.getNavigationInfoFront(limit);
            List<NavigationInfoVO> navigationVOList = PojoUtils.map(navigationDTOList, NavigationInfoVO.class);
            mallHeadVO.setNavigationInfoList(navigationVOList);
        }
        return Result.success(mallHeadVO);
    }


    /**
     * 商城页Banner区域接口，包含内容：
     * 1、商城Banner列表
     * 2、公告信息
     * 3、智能推荐
     * 4、商品分类名
     *
     * @param staffInfo
     * @return
     */
    @ApiOperation(value = "商城首页Banner区域接口")
    @GetMapping(value = "/banner")
    public Result<MallBannerVO> banner(@CurrentUser CurrentStaffInfo staffInfo) {
        MallBannerVO mallBannerVO = new MallBannerVO();

        // 获取公告列表
        {
            Integer limit = 5;
            List<NoticeInfoDTO> noticeInfoList = noticeInfoApi.getAvailableNoticeInfoList(limit);
            List<NoticeInfoVO> noticeInfoVOList = PojoUtils.map(noticeInfoList, NoticeInfoVO.class);
            mallBannerVO.setNoticeInfoList(noticeInfoVOList);
        }

        // 获取banner信息
        {
            Integer limit = 5;
            List<BannerDTO> bannerList = bannerApi.getAvailableBannerList(limit);
            List<BannerVO> bannerVOList = PojoUtils.map(bannerList, BannerVO.class);
            mallBannerVO.setBannerList(bannerVOList);
        }

        //智能推荐
        {
            Integer limit = 10;
            RecommendGoodsVO recommendGoods = new RecommendGoodsVO();
            List<RecommendGoodsDTO> recommendGoodsDTOList = recommendGoodsApi.getStaffRecommendGoodsList(limit);
            List<Long> goodsIds = recommendGoodsDTOList.stream().map(RecommendGoodsDTO::getGoodsId).distinct().collect(Collectors.toList());

            List<Long> showGoodsIds = new ArrayList<>(limit);
            //获取3个月内的订单
            if (staffInfo != null) {
                List<Long> ylOrderGoodsIds = orderApi.getOrderGoodsByEid(staffInfo.getCurrentEid());
                ylOrderGoodsIds = CollectionUtil.isEmpty(ylOrderGoodsIds) ? Collections.emptyList()  : ylOrderGoodsIds.stream().distinct().collect(Collectors.toList());
                //查询以岭的商品ID
//                AgreementPurchaseGoodsRequest request = new AgreementPurchaseGoodsRequest();
//                request.setPurchaseEid(staffInfo.getCurrentEid());
//                List<AgreementGoodsDTO> agreementGoodsDTOList = agreementGoodsApi.getYearPurchaseGoodsList(request);
//                List<Long> ylGoodsIds = agreementGoodsDTOList.stream().map(e -> e.getGoodsId()).collect(Collectors.toList());
                List<Long> ylGoodsIds = procurementRelationGoodsApi.getYlGoodsIdByYlGoodsIds(ylOrderGoodsIds, staffInfo.getCurrentEid());
                ylOrderGoodsIds.forEach(e -> {
                    if (ylGoodsIds.contains(e) && showGoodsIds.size() <= limit) {
                        showGoodsIds.add(e);
                    }
                });
            }
            //去除配置商品里的订单商品
            goodsIds = goodsIds.stream().filter(id -> !showGoodsIds.contains(id)).collect(Collectors.toList());
            if (showGoodsIds.size() <= limit) {
                goodsIds.forEach(e -> {
                    if (showGoodsIds.size() <= limit) {
                        showGoodsIds.add(e);
                    }
                });
            }
            log.info("staffInfo:{}",staffInfo);
            List<GoodsItemVO> goodsList = goodsAssemblyUtils.assembly(showGoodsIds, staffInfo != null ? staffInfo.getCurrentEid() : 0L);
            recommendGoods.setGoodsList(goodsList);
            mallBannerVO.setRecommendGoods(recommendGoods);
        }

        //获取分类名称
        {
            Integer limit = 8;
            List<HomeCategoryDTO> categoryDTOList = categoryApi.getCategoryList(limit);
            List<CategoryVO> categoryList = PojoUtils.map(categoryDTOList, CategoryVO.class);
            mallBannerVO.setCategoryList(categoryList);
        }
        return Result.success(mallBannerVO);
    }

    @ApiOperation(value = "商城首页分类商品显示")
    @GetMapping(value = "/category")
    public Result<CollectionObject<GoodsItemVO>> category(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("categoryId") Long categoryId) {
        Long buyerEid = staffInfo != null ? staffInfo.getCurrentEid() : 0L;
        List<HomeCategoryGoodsDTO> homeCategoryGoodsDTOList = categoryGoodsApi.getCategoryGoodsByCategoryId(categoryId);
        List<Long> goodsIds = homeCategoryGoodsDTOList.stream().map(HomeCategoryGoodsDTO::getGoodsId).collect(Collectors.toList());
        List<GoodsItemVO> goodsList = goodsAssemblyUtils.assembly(goodsIds, buyerEid);
        return Result.success(new CollectionObject<>(goodsList));
    }
}
