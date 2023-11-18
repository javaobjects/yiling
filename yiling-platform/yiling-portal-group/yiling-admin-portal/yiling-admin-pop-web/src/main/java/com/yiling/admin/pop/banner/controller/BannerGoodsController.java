package com.yiling.admin.pop.banner.controller;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.pop.banner.form.QueryBannerGoodsPageListForm;
import com.yiling.admin.pop.banner.vo.BannerGoodsVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.mall.banner.api.BannerGoodsApi;
import com.yiling.mall.banner.dto.BannerGoodsDTO;
import com.yiling.mall.banner.dto.request.QueryBannerGoodsPageListRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * banner_goods表 前端控制器
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@RestController
@RequestMapping("/banner/goods")
@Api(tags = "banner商品模块接口")
public class BannerGoodsController extends BaseController {

    @DubboReference
    BannerGoodsApi bannerGoodsApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    PopGoodsApi popGoodsApi;

    @ApiOperation(value = "运营后台获取banner商品列表")
    @PostMapping("/pageList")
    public Result<Page<BannerGoodsVO>> pageList(@RequestBody QueryBannerGoodsPageListForm form) {
        QueryBannerGoodsPageListRequest request = PojoUtils.map(form, QueryBannerGoodsPageListRequest.class);
        Page<BannerGoodsDTO> page = bannerGoodsApi.pageList(request);
        Page<BannerGoodsVO> bannerGoodsVOPage = PojoUtils.map(page, BannerGoodsVO.class);
        // 处理商品列表信息
        List<BannerGoodsVO> bannerGoodsVOList = new ArrayList<>();
        page.getRecords().forEach(e -> {
            GoodsInfoDTO goodsInfoDTO = popGoodsApi.queryInfo(e.getGoodsId());
            BannerGoodsVO vo = PojoUtils.map(e, BannerGoodsVO.class);
            if (StringUtils.isNotBlank(goodsInfoDTO.getName())) {
                vo.setGoodsName(goodsInfoDTO.getName());
            }
            if (StringUtils.isNotBlank(goodsInfoDTO.getSellSpecifications()) && StringUtils.isNotBlank(goodsInfoDTO.getUnit())) {
                vo.setSpecifications(goodsInfoDTO.getSellSpecifications().concat(goodsInfoDTO.getUnit()));
            }
            if (StringUtils.isNotBlank(goodsInfoDTO.getLicenseNo())) {
                vo.setLicenseNo(goodsInfoDTO.getLicenseNo());
            }
            if (StringUtils.isNotBlank(goodsInfoDTO.getManufacturer())) {
                vo.setManufacturer(goodsInfoDTO.getManufacturer());
            }
            if (goodsInfoDTO.getPrice() != null) {
                vo.setPrice(goodsInfoDTO.getPrice());
            }
            if (StringUtils.isNotBlank(goodsInfoDTO.getPic())) {
                vo.setPic(goodsInfoDTO.getPic());
            }
            bannerGoodsVOList.add(vo);
        });
        return Result.success(bannerGoodsVOPage.setRecords(bannerGoodsVOList));
    }

}
