package com.yiling.admin.cms.goods.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.cms.goods.form.QueryGoodsPageForm;
import com.yiling.admin.cms.goods.vo.GoodsInfoVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.goods.standard.dto.request.StandardGoodsInfoRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: gxl
 * @date: 2022/6/2
 */
@Api(tags = "文献或者文章选择关联标准库商品")
@RestController
@RequestMapping("/cms/goods")
public class GoodsController extends BaseController {
    @DubboReference
    private StandardGoodsApi standardGoodsApi;

    @ApiOperation(value = "查询商品")
    @GetMapping("queryGoods")
    public Result<Page<GoodsInfoVO>> queryGoods(QueryGoodsPageForm form){
        StandardGoodsInfoRequest request = new StandardGoodsInfoRequest();
        request.setSize(form.getSize()).setCurrent(form.getCurrent());
        request.setName(form.getGoodsName());
        Page<StandardGoodsInfoDTO> standardGoodsInfo = standardGoodsApi.getStandardGoodsInfo(request);
        Page<GoodsInfoVO> result = PojoUtils.map(standardGoodsInfo,GoodsInfoVO.class);
        return Result.success(result);
    }
}