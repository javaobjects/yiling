package com.yiling.admin.pop.category.controller;


import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.pop.category.form.QueryCategoryGoodsPageListForm;
import com.yiling.admin.pop.category.vo.CategoryGoodsVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.mall.category.api.CategoryGoodsApi;
import com.yiling.mall.category.dto.CategoryGoodsDTO;
import com.yiling.mall.category.dto.request.QueryCategoryGoodsPageListRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 分类商品表 前端控制器
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-16
 */
@RestController
@RequestMapping("/categor/goods")
@Api(tags = "首页分类商品模块接口")
public class CategoryGoodsController extends BaseController {
    
    @DubboReference
    CategoryGoodsApi categoryGoodsApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    PopGoodsApi popGoodsApi;

    @ApiOperation(value = "运营后台获取分类商品列表")
    @PostMapping("/pageList")
    public Result<Page<CategoryGoodsVO>> pageList(@RequestBody @Valid QueryCategoryGoodsPageListForm form) {
        QueryCategoryGoodsPageListRequest request = PojoUtils.map(form, QueryCategoryGoodsPageListRequest.class);
        Page<CategoryGoodsDTO> page = categoryGoodsApi.pageList(request);
        Page<CategoryGoodsVO> categoryGoodsVoPage = PojoUtils.map(page, CategoryGoodsVO.class);
        // 处理商品列表信息
        List<CategoryGoodsVO> categoryGoodsVOList = new ArrayList<>();
        page.getRecords().forEach(e -> {
            GoodsInfoDTO goodsInfoDTO = popGoodsApi.queryInfo(e.getGoodsId());
            CategoryGoodsVO vo = PojoUtils.map(e, CategoryGoodsVO.class);

            PojoUtils.map(goodsInfoDTO, vo);
            vo.setGoodsName(goodsInfoDTO.getName());
            if (StringUtils.isNotBlank(goodsInfoDTO.getSellSpecifications()) && StringUtils.isNotBlank(goodsInfoDTO.getUnit())) {
                vo.setSpecifications(goodsInfoDTO.getSellSpecifications().concat(goodsInfoDTO.getUnit()));
            }

            categoryGoodsVOList.add(vo);
        });
        return Result.success(categoryGoodsVoPage.setRecords(categoryGoodsVOList));
    }
}
