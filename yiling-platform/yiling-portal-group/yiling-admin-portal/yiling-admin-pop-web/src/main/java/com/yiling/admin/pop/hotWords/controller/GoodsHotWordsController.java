package com.yiling.admin.pop.hotWords.controller;

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
import com.yiling.admin.pop.hotWords.form.QueryGoodsHotWordsPageForm;
import com.yiling.admin.pop.hotWords.form.SaveGoodsHotWordsForm;
import com.yiling.admin.pop.hotWords.form.UpdateGoodsHotWordsForm;
import com.yiling.admin.pop.hotWords.vo.GoodsHotWordsDetailsVO;
import com.yiling.admin.pop.hotWords.vo.GoodsHotWordsVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.mall.hotwords.api.GoodsHotWordsApi;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 热词管理
 * </p>
 *
 * @author:wei.wang
 * @date:2021/6/15
 */
@RestController
@Api(tags = "热词管理")
@RequestMapping("/goods/hot/words")
public class GoodsHotWordsController extends BaseController {

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    GoodsHotWordsApi goodsHotWordsApi;

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @ApiOperation(value = "管理后台获取热词详情列表")
    @PostMapping("/get")
    public Result<Page<GoodsHotWordsVO>> getGoodsHotWordsPage(@RequestBody @Valid QueryGoodsHotWordsPageForm form) {
        com.yiling.mall.hotwords.dto.request.QueryGoodsHotWordsPageRequest request = PojoUtils.map(form, com.yiling.mall.hotwords.dto.request.QueryGoodsHotWordsPageRequest.class);
        request.setEid(Constants.YILING_EID);
        Page<com.yiling.mall.hotwords.dto.GoodsHotWordsDTO> wordsPage = goodsHotWordsApi.getGoodsHotWordsPage(request);
        Page<GoodsHotWordsVO> map = PojoUtils.map(wordsPage, GoodsHotWordsVO.class);
        return Result.success(map);
    }

    @ApiOperation(value = "管理后台获取热词详细信息")
    @GetMapping("/get/details")
    public Result<GoodsHotWordsDetailsVO> getGoodsHotWordsDetails(@RequestParam("id") Long id) {
        com.yiling.mall.hotwords.dto.GoodsHotWordsDTO goodsHotWordsDetails = goodsHotWordsApi.getGoodsHotWordsDetails(id);
        GoodsHotWordsDetailsVO result = PojoUtils.map(goodsHotWordsDetails, GoodsHotWordsDetailsVO.class);
        /*GoodsDTO goods = goodsApi.queryInfo(goodsHotWordsDetails.getGoodsId());
        if (result != null && goods != null) {
            result.setGoodsName(goods.getName())
                    .setPic(pictureUrlUtils.getGoodsPicUrl(goods.getPic())).setSellSpecifications(goods.getSellSpecifications())
                    .setLicenseNo(goods.getLicenseNo()).setUnit(goods.getUnit()).setPrice(goods.getPrice())
                    .setManufacturer(goods.getManufacturer());
        }*/
        return Result.success(result);
    }

    @ApiOperation(value = "管理后台修改热词信息")
    @PostMapping("/update")
    public Result<BoolObject> updateGoodsHotWordsById (@RequestBody @Valid UpdateGoodsHotWordsForm form){
        return Result.success(new BoolObject(goodsHotWordsApi.updateGoodsHotWordsById(PojoUtils.map(form, com.yiling.mall.hotwords.dto.request.UpdateGoodsHotWordsRequest.class))));
    }

    @ApiOperation(value = "管理后台保存热词信息")
    @PostMapping("/save")
    public Result<BoolObject> saveGoodsHotWords(@RequestBody @Valid SaveGoodsHotWordsForm form, @CurrentUser CurrentAdminInfo staffInfo){
        com.yiling.mall.hotwords.dto.request.SaveGoodsHotWordsRequest request = PojoUtils.map(form, com.yiling.mall.hotwords.dto.request.SaveGoodsHotWordsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        Boolean result = goodsHotWordsApi.saveGoodsHotWords(request);
        return Result.success(new BoolObject(result));
    }
}
