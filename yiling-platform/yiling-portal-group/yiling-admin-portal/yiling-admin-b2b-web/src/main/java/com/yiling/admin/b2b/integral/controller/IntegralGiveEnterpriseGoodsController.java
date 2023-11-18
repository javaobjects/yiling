package com.yiling.admin.b2b.integral.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.integral.form.AddIntegralGiveEnterpriseGoodsForm;
import com.yiling.admin.b2b.integral.form.DeleteIntegralGiveEnterpriseGoodsForm;
import com.yiling.admin.b2b.integral.form.QueryIntegralEnterpriseGoodsPageForm;
import com.yiling.admin.b2b.integral.vo.IntegralGiveGoodsPageVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.marketing.integral.api.IntegralGiveEnterpriseGoodsApi;
import com.yiling.marketing.integral.dto.request.AddIntegralGiveEnterpriseGoodsRequest;
import com.yiling.marketing.integral.dto.request.DeleteIntegralGiveEnterpriseGoodsRequest;
import com.yiling.marketing.integral.dto.request.QueryIntegralEnterpriseGoodsPageRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseByNameRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.integral.api.IntegralGiveRuleApi;
import com.yiling.user.integral.api.IntegralGiveSellerApi;
import com.yiling.user.integral.bo.IntegralGiveRuleDetailBO;
import com.yiling.user.integral.dto.IntegralGiveRuleDTO;
import com.yiling.user.integral.dto.IntegralOrderEnterpriseGoodsDTO;
import com.yiling.user.integral.dto.IntegralOrderSellerDTO;
import com.yiling.user.integral.enums.IntegralMerchantScopeEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 订单送积分-店铺SKU 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-04
 */
@Api(tags = "订单送积分-店铺SKU接口")
@RestController
@RequestMapping("integralGiveEnterpriseGoods")
public class IntegralGiveEnterpriseGoodsController extends BaseController {

    @DubboReference
    IntegralGiveEnterpriseGoodsApi giveEnterpriseGoodsApi;
    @DubboReference
    IntegralGiveSellerApi giveSellerApi;
    @DubboReference
    IntegralGiveRuleApi integralGiveRuleApi;
    @DubboReference
    GoodsApi goodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    B2bGoodsApi b2bGoodsApi;
    @DubboReference
    InventoryApi inventoryApi;

    @ApiOperation(value = "订单送积分-店铺SKU-待添加店铺SKU分页列表查询")
    @PostMapping("/list")
    public Result<Page<IntegralGiveGoodsPageVO>> list(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryIntegralEnterpriseGoodsPageForm form) {
        QueryIntegralEnterpriseGoodsPageRequest request = PojoUtils.map(form, QueryIntegralEnterpriseGoodsPageRequest.class);
        IntegralMerchantScopeEnum merchantScopeEnum = IntegralMerchantScopeEnum.getByCode(request.getConditionSellerType());
        // 查询商品信息
        List<Long> sellerEidList = ListUtil.toList();
        if (merchantScopeEnum == IntegralMerchantScopeEnum.ASSIGN) {
            List<IntegralOrderSellerDTO> sellerDTOList = giveSellerApi.listSellerByGiveRuleId(form.getGiveRuleId());
            sellerEidList = sellerDTOList.stream().map(IntegralOrderSellerDTO::getEid).collect(Collectors.toList());
        }

        if (StrUtil.isNotEmpty(request.getEname())) {
            QueryEnterpriseByNameRequest byNameRequest = new QueryEnterpriseByNameRequest();
            byNameRequest.setName(request.getEname());
            byNameRequest.setTypeList(ListUtil.toList(EnterpriseTypeEnum.BUSINESS.getCode()));
            List<EnterpriseDTO> enterpriseListByName = enterpriseApi.getEnterpriseListByName(byNameRequest);
            List<Long> eidList = enterpriseListByName.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(sellerEidList)) {
                //  求交集
                Collection<Long> intersection = CollUtil.intersection(eidList, sellerEidList);
                sellerEidList = new ArrayList<>(intersection);
                if (CollUtil.isEmpty(intersection)) {
                    return Result.success(request.getPage());
                }
            } else {
                sellerEidList = eidList;
            }
        }

        QueryGoodsPageListRequest goodsRequest = new QueryGoodsPageListRequest();
        if (CollUtil.isNotEmpty(sellerEidList)) {
            goodsRequest.setEidList(sellerEidList);
        }
        goodsRequest.setCurrent(request.getCurrent());
        goodsRequest.setSize(request.getSize());
        goodsRequest.setGoodsStatus(request.getGoodsStatus());
        if (request.getYilingGoodsFlag() != 0) {
            goodsRequest.setIncludeEids(enterpriseApi.listSubEids(Constants.YILING_EID));
        }
        goodsRequest.setYilingGoodsFlag(request.getYilingGoodsFlag());
        if (Objects.nonNull(request.getGoodsId()) && request.getGoodsId() != 0) {
            goodsRequest.setGoodsId(request.getGoodsId());
        }
        if (StrUtil.isNotEmpty(request.getGoodsName())) {
            goodsRequest.setName(request.getGoodsName());
        }
        Page<GoodsListItemBO> goodsPage = b2bGoodsApi.queryB2bGoodsPageList(goodsRequest);
        Page<IntegralGiveGoodsPageVO> voPage = PojoUtils.map(goodsPage, IntegralGiveGoodsPageVO.class);
        if (CollUtil.isEmpty(goodsPage.getRecords())) {
            return Result.success(voPage);
        }
        List<IntegralGiveGoodsPageVO> list = new ArrayList<>();

        // 查询库存
        List<Long> goodsIdList = goodsPage.getRecords().stream().map(GoodsListItemBO::getId).collect(Collectors.toList());
        Map<Long, Long> inventoryMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);
        Map<Long, Long> goodsMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList, enterpriseApi.listSubEids(Constants.YILING_EID));

        for (GoodsListItemBO goods : goodsPage.getRecords()) {
            IntegralGiveGoodsPageVO goodsPageVO = new IntegralGiveGoodsPageVO();
            goodsPageVO.setGoodsStatus(goods.getGoodsStatus());
            goodsPageVO.setEid(goods.getEid());
            if (goodsMap.get(goods.getId()) != null && goodsMap.get(goods.getId()) > 0) {
                goodsPageVO.setYilingGoodsFlag(1);
            } else {
                goodsPageVO.setYilingGoodsFlag(2);
            }
            goodsPageVO.setEname(goods.getEname());
            goodsPageVO.setGoodsId(goods.getId());
            goodsPageVO.setGoodsName(goods.getName());
            goodsPageVO.setManufacturer(goods.getManufacturer());
            goodsPageVO.setGoodsType(goods.getGoodsType());
            goodsPageVO.setSellSpecifications(goods.getSellSpecifications());
            goodsPageVO.setPrice(goods.getPrice());
            goodsPageVO.setSellUnit(goods.getSellUnit());
            // 商品库存
            Long inventory = inventoryMap.get(goods.getId());
            if (ObjectUtil.isNull(inventory) || inventory < 0) {
                inventory = 0L;
            }
            goodsPageVO.setGoodsInventory(inventory);

            list.add(goodsPageVO);
        }
        voPage.setRecords(list);
        return Result.success(voPage);
    }


    @ApiOperation(value = "订单送积分-店铺SKU-已添加店铺SKU分页列表查询")
    @PostMapping("/pageList")
    public Result<Page<IntegralGiveGoodsPageVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryIntegralEnterpriseGoodsPageForm form) {
        QueryIntegralEnterpriseGoodsPageRequest request = PojoUtils.map(form, QueryIntegralEnterpriseGoodsPageRequest.class);
        Page<IntegralOrderEnterpriseGoodsDTO> dtoPage = giveEnterpriseGoodsApi.pageList(request);
        Page<IntegralGiveGoodsPageVO> voPage = PojoUtils.map(dtoPage, IntegralGiveGoodsPageVO.class);
        if (CollUtil.isEmpty(voPage.getRecords())) {
            return Result.success(voPage);
        }
        List<Long> goodsIdList = voPage.getRecords().stream().map(IntegralGiveGoodsPageVO::getGoodsId).collect(Collectors.toList());
        Map<Long, Long> inventoryMap = inventoryApi.getAvailableQtyByGoodsIds(goodsIdList);
        Map<Long, Long> yiLingMap = goodsApi.getYilingGoodsIdByGoodsIdAndYilingEids(goodsIdList, enterpriseApi.listSubEids(Constants.YILING_EID));
        List<GoodsInfoDTO> goodsInfoDTOList = b2bGoodsApi.batchQueryInfo(goodsIdList);
        Map<Long, GoodsInfoDTO> goodsInfoMap = goodsInfoDTOList.stream().collect(Collectors.toMap(GoodsInfoDTO::getId, Function.identity(), (k1, k2) -> k1));

        for (IntegralGiveGoodsPageVO record : voPage.getRecords()) {
            GoodsInfoDTO goodsDTO = goodsInfoMap.get(record.getGoodsId());
            record.setGoodsStatus(goodsDTO.getGoodsStatus());
            record.setEname(goodsDTO.getEname());
            record.setGoodsName(goodsDTO.getName());
            record.setManufacturer(goodsDTO.getManufacturer());
            record.setGoodsType(goodsDTO.getGoodsType());
            record.setSellSpecifications(goodsDTO.getSellSpecifications());
            record.setPrice(goodsDTO.getPrice());
            record.setSellUnit(goodsDTO.getSellUnit());
            if (yiLingMap.get(record.getGoodsId()) != null && yiLingMap.get(record.getGoodsId()) > 0) {
                record.setYilingGoodsFlag(1);
            } else {
                record.setYilingGoodsFlag(2);
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

    @ApiOperation(value = "订单送积分-店铺SKU-添加店铺SKU")
    @PostMapping("/add")
    @Log(title = "订单送积分-店铺SKU-添加店铺SKU", businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddIntegralGiveEnterpriseGoodsForm form) {
        AddIntegralGiveEnterpriseGoodsRequest request = PojoUtils.map(form, AddIntegralGiveEnterpriseGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        if (Objects.isNull(form.getGoodsId()) && CollUtil.isEmpty(form.getGoodsIdList())) {

            if (IntegralMerchantScopeEnum.getByCode(form.getMerchantScope()) == IntegralMerchantScopeEnum.ASSIGN) {
                List<IntegralOrderSellerDTO> sellerDTOList = giveSellerApi.listSellerByGiveRuleId(form.getGiveRuleId());
                List<Long> sellerEidList = sellerDTOList.stream().map(IntegralOrderSellerDTO::getEid).collect(Collectors.toList());

                if (CollUtil.isEmpty(sellerDTOList)) {
                    return Result.failed("此次查询的店铺SKU未选中商家");
                } else if (Objects.isNull(request.getEidPage())) {
                    request.setSellerEidList(sellerEidList);
                } else if (!sellerEidList.contains(request.getEidPage())) {
                    return Result.failed("此次查询的用户不是规则选中商家");
                }

            } else {
                if (Objects.isNull(request.getGoodsIdPage()) && StringUtils.isBlank(request.getGoodsNamePage()) && StringUtils.isBlank(request.getEnamePage())
                        && (Objects.isNull(request.getYilingGoodsFlag()) || request.getYilingGoodsFlag() == 0)) {
                    return Result.failed("此次查询数据量过于庞大，不允许添加");
                }
            }
        }

        return Result.success(giveEnterpriseGoodsApi.add(request));
    }

    @ApiOperation(value = "订单送积分-店铺SKU-删除店铺SKU")
    @PostMapping("/delete")
    @Log(title = "订单送积分-店铺SKU-删除店铺SKU", businessType = BusinessTypeEnum.DELETE)
    public Result<Boolean> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeleteIntegralGiveEnterpriseGoodsForm form) {
        DeleteIntegralGiveEnterpriseGoodsRequest request = PojoUtils.map(form, DeleteIntegralGiveEnterpriseGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(giveEnterpriseGoodsApi.delete(request));
    }
}
