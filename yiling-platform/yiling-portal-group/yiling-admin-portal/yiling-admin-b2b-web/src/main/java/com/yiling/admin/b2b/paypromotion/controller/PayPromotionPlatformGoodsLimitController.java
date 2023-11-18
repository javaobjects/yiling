package com.yiling.admin.b2b.paypromotion.controller;


import java.util.ArrayList;
import java.util.Objects;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.strategy.form.AddStrategyPlatformGoodsLimitForm;
import com.yiling.admin.b2b.strategy.form.DeleteStrategyPlatformGoodsLimitForm;
import com.yiling.admin.b2b.strategy.form.QueryStrategyPlatformGoodsLimitPageForm;
import com.yiling.admin.b2b.strategy.vo.StrategyPlatformGoodsLimitVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.bo.StandardSpecificationGoodsInfoBO;
import com.yiling.goods.standard.dto.StandardGoodsDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.marketing.paypromotion.dto.PayPromotionPlatformGoodsLimitDTO;
import com.yiling.marketing.strategy.api.StrategyPlatformGoodsApi;
import com.yiling.marketing.strategy.dto.StrategyPlatformGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyPlatformGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyPlatformGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyPlatformGoodsLimitPageRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 支付促销平台SKU 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Api(tags = "支付促销-平台SKU")
@RestController
@RequestMapping("/payPromotion/limit/platformGoods")
public class PayPromotionPlatformGoodsLimitController extends BaseController {

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @DubboReference
    StrategyPlatformGoodsApi strategyPlatformGoodsApi;

    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;


    @ApiOperation(value = "支付促销指定平台SKU-删除平台SKU-运营后台")
    @PostMapping("/delete")
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeleteStrategyPlatformGoodsLimitForm form) {
        DeleteStrategyPlatformGoodsLimitRequest request = PojoUtils.map(form, DeleteStrategyPlatformGoodsLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyPlatformGoodsApi.deleteForPayPromotionPlatformSku(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }


    @ApiOperation(value = "支付促销指定平台SKU-添加平台SKU-运营后台")
    @PostMapping("/add")
    public Result<Object> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddStrategyPlatformGoodsLimitForm form) {
        AddStrategyPlatformGoodsLimitRequest request = PojoUtils.map(form, AddStrategyPlatformGoodsLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyPlatformGoodsApi.addForPayPromotion(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("添加失败");
    }

    @ApiOperation(value = "支付促销指定平台SKU-已添加平台SKU列表查询-运营后台")
    @PostMapping("/pageList")
    public Result<Page<StrategyPlatformGoodsLimitVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryStrategyPlatformGoodsLimitPageForm form) {
        QueryStrategyPlatformGoodsLimitPageRequest request = PojoUtils.map(form, QueryStrategyPlatformGoodsLimitPageRequest.class);
        Page<PayPromotionPlatformGoodsLimitDTO> dtoPage = strategyPlatformGoodsApi.pageListFroPayPromotion(request);
        Page<StrategyPlatformGoodsLimitVO> voPage = PojoUtils.map(dtoPage, StrategyPlatformGoodsLimitVO.class);

        for (StrategyPlatformGoodsLimitVO platformGoodsLimitVO : voPage.getRecords()) {
            platformGoodsLimitVO.setMarketingStrategyId(platformGoodsLimitVO.getMarketingPayId());
            StandardGoodsSpecificationDTO standardGoodsSpecificationDTO = standardGoodsSpecificationApi.getStandardGoodsSpecification(platformGoodsLimitVO.getSellSpecificationsId());
            if (Objects.nonNull(standardGoodsSpecificationDTO)) {
                platformGoodsLimitVO.setName(standardGoodsSpecificationDTO.getName());
                platformGoodsLimitVO.setManufacturer(standardGoodsSpecificationDTO.getManufacturer());
                platformGoodsLimitVO.setSellSpecifications(standardGoodsSpecificationDTO.getSellSpecifications());
            }
            StandardGoodsDTO standardGoodsDTO = standardGoodsApi.getOneById(platformGoodsLimitVO.getStandardId());
            if (Objects.nonNull(standardGoodsDTO)) {
                // 以岭标识 0:非以岭  1：以岭
                platformGoodsLimitVO.setYlFlag(standardGoodsDTO.getYlFlag());
                platformGoodsLimitVO.setGoodsType(standardGoodsDTO.getGoodsType());
            }
        }
        return Result.success(voPage);
    }

}
