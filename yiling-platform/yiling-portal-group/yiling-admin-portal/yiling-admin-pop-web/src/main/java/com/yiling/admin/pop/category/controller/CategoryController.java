package com.yiling.admin.pop.category.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.common.utils.PictureUrlUtils;
import com.yiling.admin.pop.category.form.QueryCategoryPageListForm;
import com.yiling.admin.pop.category.form.SaveCategoryForm;
import com.yiling.admin.pop.category.form.UpdateCategoryForm;
import com.yiling.admin.pop.category.vo.CategoryGoodsDetailVO;
import com.yiling.admin.pop.category.vo.CategoryVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.mall.category.api.CategoryApi;
import com.yiling.mall.category.api.CategoryGoodsApi;
import com.yiling.mall.category.dto.CategoryDTO;
import com.yiling.mall.category.dto.HomeCategoryGoodsDTO;
import com.yiling.mall.category.dto.request.QueryCategoryPageListRequest;
import com.yiling.mall.category.dto.request.SaveCategoryRequest;
import com.yiling.mall.category.dto.request.UpdateCategoryRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 商品分类表 前端控制器
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-16
 */
@RestController
@RequestMapping("/category")
@Api(tags = "首页分类模块接口")
public class CategoryController extends BaseController {
    
    @DubboReference
    CategoryApi      categoryApi;
    @DubboReference
    CategoryGoodsApi categoryGoodsApi;
    @DubboReference
    GoodsApi         goodsApi;
    @DubboReference
    PopGoodsApi      popGoodsApi;
    @Autowired
    PictureUrlUtils  pictureUrlUtils;


    @ApiOperation(value = "运营后台获取商品分类列表")
    @PostMapping("/pageList")
    public Result<Page<CategoryVO>> pageList(@RequestBody QueryCategoryPageListForm form,@CurrentUser CurrentAdminInfo staffInfo) {
        QueryCategoryPageListRequest request = PojoUtils.map(form, QueryCategoryPageListRequest.class);
        request.setEid(Constants.YILING_EID);
        Page<CategoryDTO> page = categoryApi.pageList(request);
        Page<CategoryVO> pageVO = PojoUtils.map(page, CategoryVO.class);

        List<Long> categoryIds = page.getRecords().stream().map(CategoryDTO::getId).collect(Collectors.toList());
        Map<Long, Long> groupCategoryGoodsNumMap = categoryGoodsApi.countCategoryGoods(categoryIds);
        pageVO.getRecords().forEach(e -> e.setGoodsNum(groupCategoryGoodsNumMap.getOrDefault(e.getId(), 0L)));

        return Result.success(pageVO);
    }

    @ApiOperation(value = "运营后台获取分类商品详细信息")
    @GetMapping("/get")
    public Result<CategoryVO> getCategoryDetail(@RequestParam("id") Long id) {
        CategoryDTO dto = categoryApi.get(id);
        CategoryVO categoryVO = PojoUtils.map(dto, CategoryVO.class);

        List<HomeCategoryGoodsDTO> categoryGoodsList = categoryGoodsApi.getCategoryGoodsByCategoryId(id);
        List<Long> goodsIdList = categoryGoodsList.stream().map(HomeCategoryGoodsDTO::getGoodsId).collect(Collectors.toList());
        Map<Long, Integer> categorySortMap = categoryGoodsList.stream().collect(
                Collectors.toMap(HomeCategoryGoodsDTO::getGoodsId, HomeCategoryGoodsDTO::getSort, (k1, k2) -> k2));

        List<CategoryGoodsDetailVO> categoryGoodsVOList = PojoUtils.map(goodsApi.batchQueryInfo(goodsIdList), CategoryGoodsDetailVO.class);
        categoryGoodsVOList.forEach(categoryGoodsDetailVO -> {
            categoryGoodsDetailVO.setPic(pictureUrlUtils.getGoodsPicUrl(categoryGoodsDetailVO.getPic()));
            categoryGoodsDetailVO.setSort(categorySortMap.get(categoryGoodsDetailVO.getId()));
        });

        categoryGoodsVOList = categoryGoodsVOList.stream().sorted(Comparator.comparing(CategoryGoodsDetailVO::getSort)).collect(Collectors.toList());
        categoryVO.setCategoryGoodsList(categoryGoodsVOList);
        return Result.success(categoryVO);

    }

    @ApiOperation(value = "运营后台保存商品分类信息")
    @PostMapping("/save")
    public Result<BoolObject> saveCategory(@Valid @RequestBody SaveCategoryForm form, @CurrentUser CurrentAdminInfo staffInfo) {
        SaveCategoryRequest request = PojoUtils.map(form, SaveCategoryRequest.class);
        request.setEid(Constants.YILING_EID);
        boolean result = categoryApi.createCategory(request);

        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "运营后台编辑商品分类信息")
    @PostMapping("/update")
    public Result<BoolObject> updateCategory(@Valid @RequestBody UpdateCategoryForm form,@CurrentUser CurrentAdminInfo staffInfo) {
        UpdateCategoryRequest request = PojoUtils.map(form, UpdateCategoryRequest.class);
        request.setEid(Constants.YILING_EID);
        boolean result = categoryApi.updateCategory(request);

        return Result.success(new BoolObject(result));
    }
}
