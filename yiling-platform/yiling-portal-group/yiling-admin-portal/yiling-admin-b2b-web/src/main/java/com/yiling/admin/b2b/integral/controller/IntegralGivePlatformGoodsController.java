package com.yiling.admin.b2b.integral.controller;

import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.integral.form.AddIntegralGivePlatformGoodsForm;
import com.yiling.admin.b2b.integral.form.DeleteIntegralGivePlatformGoodsForm;
import com.yiling.admin.b2b.integral.form.QueryIntegralGivePlatformGoodsPageForm;
import com.yiling.admin.b2b.integral.vo.IntegralGivePlatformGoodsVO;
import com.yiling.admin.b2b.strategy.form.AddStrategyPlatformGoodsLimitForm;
import com.yiling.admin.b2b.strategy.form.DeleteStrategyPlatformGoodsLimitForm;
import com.yiling.admin.b2b.strategy.vo.StrategyPlatformGoodsLimitVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.bo.StandardSpecificationGoodsInfoBO;
import com.yiling.goods.standard.dto.StandardGoodsDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.marketing.integral.api.IntegralGivePlatformGoodsApi;
import com.yiling.marketing.strategy.api.StrategyPlatformGoodsApi;
import com.yiling.marketing.strategy.dto.StrategyPlatformGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyPlatformGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyPlatformGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyPlatformGoodsLimitPageRequest;
import com.yiling.user.integral.dto.IntegralOrderPlatformGoodsDTO;
import com.yiling.user.integral.dto.request.AddIntegralGivePlatformGoodsRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGivePlatformGoodsRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGivePlatformGoodsPageRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 订单送积分-平台SKU接口 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-04
 */
@Api(tags = "订单送积分-平台SKU接口")
@RestController
@RequestMapping("integralGivePlatformGoods")
public class IntegralGivePlatformGoodsController extends BaseController {

    @DubboReference
    StandardGoodsApi standardGoodsApi;
    @DubboReference
    IntegralGivePlatformGoodsApi givePlatformGoodsApi;
    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;

    @ApiOperation(value = "订单送积分-平台SKU接口分页列表查询")
    @PostMapping("/list")
    public Result<Page<StandardSpecificationGoodsInfoBO>> list(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryIntegralGivePlatformGoodsPageForm form) {
        StandardSpecificationPageRequest pageRequest = PojoUtils.map(form, StandardSpecificationPageRequest.class);
        if (Objects.nonNull(form.getSellSpecificationsId()) && form.getSellSpecificationsId() != 0) {
            pageRequest.setSpecIdList(ListUtil.toList(form.getSellSpecificationsId()));
        }
        if (StrUtil.isNotEmpty(form.getGoodsName())) {
            pageRequest.setName(form.getGoodsName());
        }
        // 以岭品 0-全部 1-是 2-否
        //0：非以岭  1：以岭
        if (form.getIsYiLing() == 1) {
            pageRequest.setYlFlag(1);
        } else if (form.getIsYiLing() == 2) {
            pageRequest.setYlFlag(0);
        }
        Page<StandardSpecificationGoodsInfoBO> specificationGoodsInfoPage = standardGoodsSpecificationApi.getSpecificationGoodsInfoPage(pageRequest);
        return Result.success(specificationGoodsInfoPage);
    }

    @ApiOperation(value = "订单送积分-已添加平台SKU分页列表查询")
    @PostMapping("/pageList")
    public Result<Page<IntegralGivePlatformGoodsVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryIntegralGivePlatformGoodsPageForm form) {
        QueryIntegralGivePlatformGoodsPageRequest request = PojoUtils.map(form, QueryIntegralGivePlatformGoodsPageRequest.class);
        Page<IntegralOrderPlatformGoodsDTO> dtoPage = givePlatformGoodsApi.pageList(request);
        Page<IntegralGivePlatformGoodsVO> voPage = PojoUtils.map(dtoPage, IntegralGivePlatformGoodsVO.class);

        for (IntegralGivePlatformGoodsVO platformGoodsVO : voPage.getRecords()) {
            StandardGoodsSpecificationDTO standardGoodsSpecificationDTO = standardGoodsSpecificationApi.getStandardGoodsSpecification(platformGoodsVO.getSellSpecificationsId());
            if (Objects.nonNull(standardGoodsSpecificationDTO)) {
                platformGoodsVO.setName(standardGoodsSpecificationDTO.getName());
                platformGoodsVO.setManufacturer(standardGoodsSpecificationDTO.getManufacturer());
                platformGoodsVO.setSellSpecifications(standardGoodsSpecificationDTO.getSellSpecifications());
            }
            StandardGoodsDTO standardGoodsDTO = Optional.ofNullable(standardGoodsApi.getOneById(platformGoodsVO.getStandardId())).orElse(new StandardGoodsDTO());
            // 以岭标识 0:非以岭  1：以岭
            platformGoodsVO.setYlFlag(standardGoodsDTO.getYlFlag());
            platformGoodsVO.setGoodsType(standardGoodsDTO.getGoodsType());
        }

        return Result.success(voPage);
    }

    @ApiOperation(value = "订单送积分-添加平台SKU")
    @PostMapping("/add")
    @Log(title = "订单送积分-添加平台SKU", businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddIntegralGivePlatformGoodsForm form) {
        AddIntegralGivePlatformGoodsRequest request = PojoUtils.map(form, AddIntegralGivePlatformGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(givePlatformGoodsApi.add(request));
    }

    @ApiOperation(value = "订单送积分-删除平台SKU")
    @PostMapping("/delete")
    @Log(title = "订单送积分-删除平台SKU", businessType = BusinessTypeEnum.DELETE)
    public Result<Boolean> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeleteIntegralGivePlatformGoodsForm form) {
        DeleteIntegralGivePlatformGoodsRequest request = PojoUtils.map(form, DeleteIntegralGivePlatformGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(givePlatformGoodsApi.delete(request));
    }
}
