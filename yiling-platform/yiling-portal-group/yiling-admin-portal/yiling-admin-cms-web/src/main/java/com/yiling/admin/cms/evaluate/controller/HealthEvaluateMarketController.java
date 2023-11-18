package com.yiling.admin.cms.evaluate.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.yiling.admin.cms.evaluate.form.DelHealthEvaluateMarketForm;
import com.yiling.admin.cms.evaluate.form.GetEvaluateMarketForm;
import com.yiling.admin.cms.evaluate.form.HealthEvaluateMarketForm;
import com.yiling.admin.cms.evaluate.vo.HealthEvaluateGoodsInfoVO;
import com.yiling.admin.cms.evaluate.vo.HealthEvaluateMarketVO;
import com.yiling.cms.evaluate.api.HealthEvaluateMarketApi;
import com.yiling.cms.evaluate.dto.HealthEvaluateMarketDTO;
import com.yiling.cms.evaluate.dto.request.DelHealthEvaluateMarketRequest;
import com.yiling.cms.evaluate.dto.request.HealthEvaluateMarketRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.user.system.bo.CurrentAdminInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 健康测评营销 前端控制器
 *
 * @author fan.shen
 * @date 2022-12-09
 */
@Api(tags = "测评营销")
@RestController
@RequestMapping("/cms/healthEvaluateMarket")
public class HealthEvaluateMarketController extends BaseController {

    @DubboReference
    HealthEvaluateMarketApi marketApi;

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @Autowired
    FileService fileService;

    @Log(title = "营销设置", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("营销设置")
    @PostMapping("marketSet")
    public Result<Boolean> marketSet(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid HealthEvaluateMarketForm form) {
        HealthEvaluateMarketRequest request = PojoUtils.map(form, HealthEvaluateMarketRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        return Result.success(marketApi.marketSet(request));
    }

    @ApiOperation("获取营销设置")
    @PostMapping("getMarketSet")
    public Result<HealthEvaluateMarketVO> getMarketListByEvaluateId(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid GetEvaluateMarketForm form) {
        HealthEvaluateMarketDTO marketList = marketApi.getMarketListByEvaluateId(form.getId());
        HealthEvaluateMarketVO marketVO = PojoUtils.map(marketList, HealthEvaluateMarketVO.class);
        if (CollUtil.isNotEmpty(marketVO.getAdviceList())) {
            marketVO.getAdviceList().forEach(item -> {
                if (StrUtil.isNotBlank(item.getPic())) {
                    item.setPicUrl(fileService.getUrl(item.getPic(), FileTypeEnum.HEALTH_EVALUATE));
                }
            });
        }
        if (CollUtil.isNotEmpty(marketVO.getPromoteList())) {
            marketVO.getPromoteList().forEach(item -> {
                if (StrUtil.isNotBlank(item.getPic())) {
                    item.setPicUrl(fileService.getUrl(item.getPic(), FileTypeEnum.HEALTH_EVALUATE));
                }
            });
        }
        if (CollUtil.isNotEmpty(marketVO.getGoodsList())) {
            marketVO.getGoodsList().forEach(goods -> {
                StandardGoodsAllInfoDTO standardGoodsById = standardGoodsApi.getStandardGoodsById(goods.getStandardId());
                goods.setName(standardGoodsById.getBaseInfo().getName());
            });
        }
        return Result.success(marketVO);
    }

    @ApiOperation("删除测评营销")
    @PostMapping("delMarket")
    public Result<Boolean> delMarket(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid DelHealthEvaluateMarketForm form) {
        DelHealthEvaluateMarketRequest request = PojoUtils.map(form, DelHealthEvaluateMarketRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        return Result.success(marketApi.delMarket(request));
    }

    @ApiOperation("获取药品信息")
    @GetMapping("getGoodsInfoByStandardId")
    public Result<HealthEvaluateGoodsInfoVO> getGoodsInfoByStandardId(@RequestParam Long standardId) {
        StandardGoodsAllInfoDTO standardGoodsById = standardGoodsApi.getStandardGoodsById(standardId);
        if(Objects.isNull(standardGoodsById)) {
            return Result.failed("药品ID不存在");
        }
        String indications = standardGoodsById.getGoodsInstructionsInfo().getIndications();
        String name = standardGoodsById.getBaseInfo().getName();
        HealthEvaluateGoodsInfoVO goodsInfoVO = new HealthEvaluateGoodsInfoVO();
        goodsInfoVO.setIndications(indications);
        goodsInfoVO.setName(name);
        return Result.success(goodsInfoVO);
    }


}
