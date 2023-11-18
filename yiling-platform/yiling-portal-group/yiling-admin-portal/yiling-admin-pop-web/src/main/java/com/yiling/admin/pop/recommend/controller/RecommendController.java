package com.yiling.admin.pop.recommend.controller;


import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.pop.recommend.form.QueryRecommendPageListForm;
import com.yiling.admin.pop.recommend.form.SaveRecommendForm;
import com.yiling.admin.pop.recommend.form.UpdateRecommendForm;
import com.yiling.admin.pop.recommend.form.UpdateRecommendStatusForm;
import com.yiling.admin.pop.recommend.vo.RecommendVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.mall.recommend.api.RecommendApi;
import com.yiling.mall.recommend.dto.RecommendDTO;
import com.yiling.mall.recommend.dto.request.QueryRecommendPageListRequest;
import com.yiling.mall.recommend.dto.request.SaveRecommendRequest;
import com.yiling.mall.recommend.dto.request.UpdateRecommendRequest;
import com.yiling.mall.recommend.dto.request.UpdateRecommendStatusRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 推荐表 前端控制器
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-15
 */
@RestController
@RequestMapping("/recommend")
@Api(tags = "推荐位模块接口")
public class RecommendController extends BaseController {

    @DubboReference
    RecommendApi recommendApi;

    @ApiOperation(value = "运营后台获取推荐位列表")
    @PostMapping("/pageList")
    public Result<Page<RecommendVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo , @RequestBody QueryRecommendPageListForm form) {
        QueryRecommendPageListRequest request = PojoUtils.map(form, QueryRecommendPageListRequest.class);
        request.setEid(Constants.YILING_EID);
        Page<RecommendDTO> page = recommendApi.pageList(request);

        Page<RecommendVO> pageVO = PojoUtils.map(page, RecommendVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "运营后台获取推荐商品详细信息")
    @GetMapping("/get")
    public Result<RecommendVO> getRecommendDetail(@RequestParam("id") Long id) {
        RecommendDTO dto = recommendApi.get(id);
        return Result.success(PojoUtils.map(dto, RecommendVO.class));
    }

    @ApiOperation(value = "运营后台保存推荐位信息")
    @PostMapping("/save")
    public Result<BoolObject> saveRecommend(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid SaveRecommendForm form) {
        SaveRecommendRequest request = PojoUtils.map(form, SaveRecommendRequest.class);
        request.setEid(Constants.YILING_EID);
        Boolean flag = recommendApi.createRecommend(request);

        return Result.success(new BoolObject(flag));
    }

    @ApiOperation(value = "运营后台编辑推荐位信息")
    @PostMapping("/update")
    public Result<BoolObject> updateRecommend(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid UpdateRecommendForm form) {
        UpdateRecommendRequest request = PojoUtils.map(form, UpdateRecommendRequest.class);
        request.setEid(Constants.YILING_EID);
        Boolean flag = recommendApi.updateRecommend(request);

        return Result.success(new BoolObject(flag));
    }

    @ApiOperation(value = "运营后台编辑推荐位状态")
    @PostMapping("/update/status")
    public Result<BoolObject> updateRecommend(@Valid @RequestBody UpdateRecommendStatusForm form) {
        UpdateRecommendStatusRequest request = PojoUtils.map(form, UpdateRecommendStatusRequest.class);
        Boolean flag = recommendApi.updateRecommendStatus(request);

        return Result.success(new BoolObject(flag));
    }


}
