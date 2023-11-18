package com.yiling.admin.pop.recommend.controller;


import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.common.utils.PictureUrlUtils;
import com.yiling.admin.pop.recommend.form.QueryRecommendGoodsPageListForm;
import com.yiling.admin.pop.recommend.vo.RecommendGoodsVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.mall.recommend.api.RecommendGoodsApi;
import com.yiling.mall.recommend.dto.RecommendGoodsDTO;
import com.yiling.mall.recommend.dto.request.QueryRecommendGoodsPageListRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 推荐商品表 前端控制器
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@RestController
@RequestMapping("/recommend/goods")
@Api(tags = "推荐位商品模块接口")
public class RecommendGoodsController extends BaseController {
    
    @DubboReference
    RecommendGoodsApi recommendGoodsApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    PopGoodsApi popGoodsApi;

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @ApiOperation(value = "运营后台获取推荐位商品列表")
    @PostMapping("/pageList")
    public Result<Page<RecommendGoodsVO>> pageList(@RequestBody QueryRecommendGoodsPageListForm form) {
        QueryRecommendGoodsPageListRequest request = PojoUtils.map(form, QueryRecommendGoodsPageListRequest.class);
        Page<RecommendGoodsDTO> page = recommendGoodsApi.pageList(request);
        Page<RecommendGoodsVO> recommendGoodsVOPage = PojoUtils.map(page, RecommendGoodsVO.class);
        // 处理商品列表信息
        List<RecommendGoodsVO> recommendGoodsVOList = new ArrayList<>();
        page.getRecords().forEach(e -> {
            GoodsInfoDTO goodsInfoDTO = popGoodsApi.queryInfo(e.getGoodsId());
            RecommendGoodsVO vo = PojoUtils.map(e, RecommendGoodsVO.class);
            if(goodsInfoDTO != null){
                vo.setGoodsName(goodsInfoDTO.getName());
                vo.setSpecifications(goodsInfoDTO.getSellSpecifications());
                vo.setUnit(goodsInfoDTO.getUnit());
                vo.setLicenseNo(goodsInfoDTO.getLicenseNo());
                vo.setManufacturer(goodsInfoDTO.getManufacturer());
                vo.setPrice(goodsInfoDTO.getPrice());
                vo.setPic(pictureUrlUtils.getGoodsPicUrl(goodsInfoDTO.getPic()));
            }
            recommendGoodsVOList.add(vo);
        });
        return Result.success(recommendGoodsVOPage.setRecords(recommendGoodsVOList));
    }
}
