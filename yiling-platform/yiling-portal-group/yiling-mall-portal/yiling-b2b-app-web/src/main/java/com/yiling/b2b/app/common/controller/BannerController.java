package com.yiling.b2b.app.common.controller;


import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.b2b.app.common.form.AppBannerForm;
import com.yiling.b2b.app.common.vo.B2bAppBannerVO;
import com.yiling.b2b.app.common.vo.B2bAppVajraPositionVO;
import com.yiling.b2b.app.common.vo.BannerAndVajraVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.banner.api.BannerApi;
import com.yiling.mall.banner.api.VajraPositionApi;
import com.yiling.mall.banner.dto.B2bAppBannerDTO;
import com.yiling.mall.banner.dto.B2bAppVajraPositionDTO;
import com.yiling.mall.banner.enums.B2BBannerLinkTypeEnum;
import com.yiling.mall.banner.enums.B2BVajraPositionLinkTypeEnum;
import com.yiling.mall.banner.enums.BannerUsageScenarioEnum;
import com.yiling.sales.assistant.message.enums.SourceEnum;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Banner 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2021-10-25
 */
@Slf4j
@Api(tags = "Banner管理接口")
@RestController
@RequestMapping("/banner")
public class BannerController extends BaseController {

    @DubboReference
    BannerApi bannerApi;

    @DubboReference
    VajraPositionApi vajraPositionApi;

    @DubboReference
    ShopApi shopApi;

    @ApiOperation(value = "app查询展示banner")
    @ApiImplicitParams({ @ApiImplicitParam(name = "count", value = "需要banner的数量", required = true) })
    @GetMapping("/listByCondition")
    public Result<BannerAndVajraVO> listByCondition(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "count") Integer count) {
        BannerAndVajraVO vo = new BannerAndVajraVO();
        //使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner
        // 主banner
        List<B2bAppBannerDTO> listDTOMain = bannerApi.listByScenarioAndSource(1, SourceEnum.B2B.getCode(), count);
        log.info("主banner数据为:[{}]", listDTOMain);
        List<B2bAppBannerVO> listVOMain = PojoUtils.map(listDTOMain, B2bAppBannerVO.class);
        for (B2bAppBannerVO e : listVOMain) {

            operateBanner(e);

            if (5 == e.getLinkType()) {
                if (null != e.getSellerEid()) {
                    ShopDTO shop = shopApi.getShop(e.getSellerEid());
                    if (null != shop) {
                        e.setSellerEid(shop.getId());
                    } else {
                        e.setSellerEid(0L);
                    }
                } else {
                    e.setSellerEid(0L);
                }
            }
        }
        vo.setMainBannerVOList(listVOMain);

        // 副banner
        List<B2bAppBannerDTO> listDTOSecond = bannerApi.listByScenarioAndSource(2, SourceEnum.B2B.getCode(), count);
        List<B2bAppBannerVO> listVOSecond = PojoUtils.map(listDTOSecond, B2bAppBannerVO.class);
        for (B2bAppBannerVO e : listVOSecond) {

            operateBanner(e);

            if (5 == e.getLinkType()) {
                if (null != e.getSellerEid()) {
                    ShopDTO shop = shopApi.getShop(e.getSellerEid());
                    if (null != shop) {
                        e.setSellerEid(shop.getId());
                    } else {
                        e.setSellerEid(0L);
                    }
                } else {
                    e.setSellerEid(0L);
                }
            }
        }
        vo.setSecondBannerVOList(listVOSecond);

        // 金刚位
        List<B2bAppVajraPositionDTO> listDTO = vajraPositionApi.listAll(SourceEnum.B2B.getCode());
        List<B2bAppVajraPositionVO> listVO = PojoUtils.map(listDTO, B2bAppVajraPositionVO.class);
        for (B2bAppVajraPositionVO e : listVO) {

            operateVajraPosition(e);

            if (5 == e.getLinkType()) {
                if (null != e.getSellerEid()) {
                    ShopDTO shop = shopApi.getShop(e.getSellerEid());
                    if (null != shop) {
                        e.setSellerEid(shop.getId());
                    } else {
                        e.setSellerEid(0L);
                    }
                } else {
                    e.setSellerEid(0L);
                }
            }
        }
        vo.setVajraPositionVOList(listVO);

        return Result.success(vo);
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

    /**
     * 金刚位的优化兼容
     *
     * @param b2bAppVajraPositionVO 返回给前端的数据实体
     */
    private void operateVajraPosition(B2bAppVajraPositionVO b2bAppVajraPositionVO) {
        B2BVajraPositionLinkTypeEnum vajraPositionLinkTypeEnum = B2BVajraPositionLinkTypeEnum.getByCode(b2bAppVajraPositionVO.getLinkType());
        if (vajraPositionLinkTypeEnum == B2BVajraPositionLinkTypeEnum.SEARCH) {
            b2bAppVajraPositionVO.setSearchKeywords(b2bAppVajraPositionVO.getActivityLinks());
        } else if (vajraPositionLinkTypeEnum == B2BVajraPositionLinkTypeEnum.GOODS) {
            b2bAppVajraPositionVO.setGoodsId(StringUtils.isNotEmpty(b2bAppVajraPositionVO.getActivityLinks()) ? Long.parseLong(b2bAppVajraPositionVO.getActivityLinks()) : 0L);
        } else if (vajraPositionLinkTypeEnum == B2BVajraPositionLinkTypeEnum.SHOP) {
            b2bAppVajraPositionVO.setSellerEid(StringUtils.isNotEmpty(b2bAppVajraPositionVO.getActivityLinks()) ? Long.parseLong(b2bAppVajraPositionVO.getActivityLinks()) : 0L);
        }
    }

    @ApiOperation(value = "app查询展示店铺banner")
    @PostMapping("/listByUsageScenario")
    public Result<List<B2bAppBannerVO>> listByUsageScenario(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AppBannerForm form) {
        List<B2bAppBannerDTO> listBanner = bannerApi.listByScenarioAndSourceAndEid(BannerUsageScenarioEnum.ENTERPRISE.getCode(), SourceEnum.B2B.getCode(), form.getEid(), form.getCount());
        List<B2bAppBannerVO> listVO = PojoUtils.map(listBanner, B2bAppBannerVO.class);
        for (B2bAppBannerVO e : listVO) {

            operateBanner(e);

            if (5 == e.getLinkType()) {
                if (null != e.getSellerEid()) {
                    ShopDTO shop = shopApi.getShop(e.getSellerEid());
                    if (null != shop) {
                        e.setSellerEid(shop.getId());
                    } else {
                        e.setSellerEid(0L);
                    }
                } else {
                    e.setSellerEid(0L);
                }
            }
        }

        return Result.success(listVO);
    }
}
