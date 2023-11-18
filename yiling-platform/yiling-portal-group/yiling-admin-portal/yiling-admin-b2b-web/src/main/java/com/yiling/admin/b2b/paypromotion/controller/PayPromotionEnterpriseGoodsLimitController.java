package com.yiling.admin.b2b.paypromotion.controller;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.strategy.form.AddStrategyEnterpriseGoodsLimitForm;
import com.yiling.admin.b2b.strategy.form.DeleteStrategyEnterpriseGoodsLimitForm;
import com.yiling.admin.b2b.strategy.form.QueryStrategyEnterpriseGoodsLimitPageForm;
import com.yiling.admin.b2b.strategy.vo.StrategyActivityGoodsPageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.marketing.paypromotion.api.PayPromotionActivityApi;
import com.yiling.marketing.paypromotion.dto.PayPromotionActivityDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionEnterpriseGoodsLimitDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionSellerLimitActivityDTO;
import com.yiling.marketing.strategy.api.StrategyActivityApi;
import com.yiling.marketing.strategy.api.StrategyEnterpriseGoodsApi;
import com.yiling.marketing.strategy.api.StrategySellerApi;
import com.yiling.marketing.strategy.dto.StrategyActivityDTO;
import com.yiling.marketing.strategy.dto.StrategyEnterpriseGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.StrategySellerLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyEnterpriseGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyEnterpriseGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyEnterpriseGoodsLimitPageRequest;
import com.yiling.marketing.strategy.enums.StrategyConditionSellerTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 支付促销店铺SKU 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Api(tags = "支付促销-店铺SKU")
@RestController
@RequestMapping("/payPromption/limit/enterpriseGoods")
public class PayPromotionEnterpriseGoodsLimitController extends BaseController {

    @DubboReference
    StrategyActivityApi strategyActivityApi;

    @DubboReference
    StrategyEnterpriseGoodsApi strategyEnterpriseGoodsApi;

    @DubboReference
    StrategySellerApi strategySellerApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    B2bGoodsApi b2bGoodsApi;

    @DubboReference
    InventoryApi inventoryApi;

    @DubboReference
    PayPromotionActivityApi payPromotionActivityApi;

    @ApiOperation(value = "支付促销指定店铺SKU-已添加店铺SKU列表查询-运营后台")
    @PostMapping("/pageList")
    public Result<Page<StrategyActivityGoodsPageVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryStrategyEnterpriseGoodsLimitPageForm form) {
        QueryStrategyEnterpriseGoodsLimitPageRequest request = PojoUtils.map(form, QueryStrategyEnterpriseGoodsLimitPageRequest.class);
        Page<PayPromotionEnterpriseGoodsLimitDTO> dtoPage = strategyEnterpriseGoodsApi.pageListForPayPromotion(request);
        Page<StrategyActivityGoodsPageVO> voPage = PojoUtils.map(dtoPage, StrategyActivityGoodsPageVO.class);
        if (CollUtil.isEmpty(voPage.getRecords())) {
            return Result.success(voPage);
        }
        List<Long> goodsIdList = voPage.getRecords().stream().map(StrategyActivityGoodsPageVO::getGoodsId).collect(Collectors.toList());
        Map<Long, Long> inventoryMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);
        Map<Long, Long> yiLingMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList, enterpriseApi.listSubEids(Constants.YILING_EID));
        List<GoodsInfoDTO> goodsInfoDTOList = b2bGoodsApi.batchQueryInfo(goodsIdList);
        Map<Long, GoodsInfoDTO> goodsInfoMap = goodsInfoDTOList.stream().collect(Collectors.toMap(GoodsInfoDTO::getId, e -> e, (k1, k2) -> k1));
        for (StrategyActivityGoodsPageVO record : voPage.getRecords()) {
            GoodsInfoDTO goodsDTO = goodsInfoMap.get(record.getGoodsId());
            record.setGoodsStatus(goodsDTO.getGoodsStatus());
            record.setId(record.getMarketingPayId());
            record.setEname(goodsDTO.getEname());
            record.setGoodsName(goodsDTO.getName());
            record.setManufacturer(goodsDTO.getManufacturer());
            record.setGoodsType(goodsDTO.getGoodsType());
            record.setSellSpecifications(goodsDTO.getSellSpecifications());
            record.setPrice(goodsDTO.getPrice());
            record.setSellUnit(goodsDTO.getSellUnit());
            record.setYilingGoodsFlag(2);
            if (yiLingMap.get(record.getGoodsId()) != null && yiLingMap.get(record.getGoodsId()) > 0) {
                record.setYilingGoodsFlag(1);
            }
            // 商品库存
            Long inventory = inventoryMap.get(record.getGoodsId());
            if (ObjectUtil.isNull(inventory) || inventory < 0) {
                inventory = 0L;
            }
            record.setGoodsInventory(inventory);
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "支付促销指定店铺SKU-添加店铺SKU-运营后台")
    @PostMapping("/add")
    public Result<Object> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddStrategyEnterpriseGoodsLimitForm form) {
        AddStrategyEnterpriseGoodsLimitRequest request = PojoUtils.map(form, AddStrategyEnterpriseGoodsLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        if (Objects.isNull(form.getGoodsId()) && CollUtil.isEmpty(form.getGoodsIdList())) {
            PayPromotionActivityDTO strategyActivityDTO = payPromotionActivityApi.getById(form.getMarketingStrategyId());
             StrategyConditionSellerTypeEnum conditionSellerTypeEnum = StrategyConditionSellerTypeEnum.getByType(strategyActivityDTO.getConditionSellerType());
            if (conditionSellerTypeEnum == StrategyConditionSellerTypeEnum.ASSIGN) {
                List<PayPromotionSellerLimitActivityDTO> sellerLimitDTOList = payPromotionActivityApi.listSellerByActivityId(form.getMarketingStrategyId());
                List<Long> sellerEidList = sellerLimitDTOList.stream().map(PayPromotionSellerLimitActivityDTO::getEid).collect(Collectors.toList());
                if (CollUtil.isEmpty(sellerLimitDTOList)) {
                    return Result.failed("此次查询的店铺SKU未选中活动商家");
                } else if (Objects.isNull(request.getEidPage())) {
                    request.setSellerEidList(sellerEidList);
                } else if (!sellerEidList.contains(request.getEidPage())) {
                    return Result.failed("此次查询的用户不是活动选中商家");
                }
            } else {
                if (Objects.isNull(request.getGoodsIdPage()) && StringUtils.isBlank(request.getGoodsNamePage()) && StringUtils.isBlank(request.getEnamePage()) && (Objects.isNull(request.getYilingGoodsFlag()) || 0 == request.getYilingGoodsFlag())) {
                    return Result.failed("此次查询数据量过于庞大，不允许添加");
                }
            }
        }
        boolean isSuccess = strategyEnterpriseGoodsApi.addForPayPromotion(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("添加失败");
    }

    @ApiOperation(value = "支付促销指定店铺SKU-删除店铺SKU-运营后台")
    @PostMapping("/delete")
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeleteStrategyEnterpriseGoodsLimitForm form) {
        DeleteStrategyEnterpriseGoodsLimitRequest request = PojoUtils.map(form, DeleteStrategyEnterpriseGoodsLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyEnterpriseGoodsApi.deleteForPromotion(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }
}
