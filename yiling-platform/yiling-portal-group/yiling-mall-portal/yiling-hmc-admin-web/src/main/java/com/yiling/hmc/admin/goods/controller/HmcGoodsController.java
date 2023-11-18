package com.yiling.hmc.admin.goods.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.yiling.hmc.admin.goods.form.UpdateGoodsStockAndPriceForm;
import com.yiling.hmc.admin.goods.vo.IHGoodsInfoVO;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.ih.patient.dto.HmcGetGoodsInfoDTO;
import com.yiling.ih.patient.dto.request.HmcGetIHGoodsInfoRequest;
import com.yiling.ih.patient.dto.request.HmcUpdateGoodsPriceAndPriceRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.hmc.admin.goods.form.QueryGoodsPageListForm;
import com.yiling.hmc.admin.goods.form.UpdateInventoryForm;
import com.yiling.hmc.admin.goods.vo.HmcGoodsVO;
import com.yiling.hmc.goods.bo.HmcGoodsBO;
import com.yiling.hmc.goods.dto.request.HmcGoodsPageRequest;
import com.yiling.hmc.wechat.api.InsuranceFetchPlanDetailApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gxl
 * @date 2022-03-30
 */
@RestController
@Api(tags = "商品管理模块")
@RequestMapping("/hmc/goods")
@Slf4j
public class HmcGoodsController extends BaseController {


    @DubboReference
    com.yiling.hmc.goods.api.GoodsApi hmcGoodsApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    GoodsHmcApi goodsHmcApi;

    @DubboReference
    InsuranceFetchPlanDetailApi insuranceFetchPlanDetailApi;

    @DubboReference
    HmcDiagnosisApi diagnosisApi;

    @ApiOperation(value = "商家商品列表", httpMethod = "GET")
    @GetMapping("/list")
    public Result<Page<HmcGoodsVO>> list(@CurrentUser CurrentStaffInfo staffInfo, @Valid QueryGoodsPageListForm form) {
        HmcGoodsPageRequest request = PojoUtils.map(form, HmcGoodsPageRequest.class);
        request.setEid(staffInfo.getCurrentEid());
        Page<HmcGoodsBO> page = hmcGoodsApi.pageListByEid(request);
        Page<HmcGoodsVO> newPage = PojoUtils.map(page, HmcGoodsVO.class);
        if (page.getTotal() == 0) {
            return Result.success(form.getPage());
        }
        List<HmcGoodsBO> goodsBOS = page.getRecords();
        List<Long> goodsIds = goodsBOS.stream().map(HmcGoodsBO::getGoodsId).distinct().collect(Collectors.toList());
        List<GoodsSkuDTO> inventory = goodsHmcApi.getGoodsSkuByGids(goodsIds);

        // 获取IH配送商商品id
        Map<Integer, HmcGetGoodsInfoDTO> promotionMap = Maps.newHashMap();
        List<Long> ihPharmacyGoodsIds = goodsBOS.stream().filter(item -> item.getIhEid() > 0 && item.getIhPharmacyGoodsId() > 0).map(HmcGoodsBO::getIhPharmacyGoodsId).distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(ihPharmacyGoodsIds)) {
            HmcGetIHGoodsInfoRequest getIHGoodsInfoRequest = new HmcGetIHGoodsInfoRequest();
            getIHGoodsInfoRequest.setIds(ihPharmacyGoodsIds);
            List<HmcGetGoodsInfoDTO> ihPharmacyGoodsInfo = diagnosisApi.getIHPharmacyGoodsInfo(getIHGoodsInfoRequest);
            promotionMap.putAll(ihPharmacyGoodsInfo.stream().collect(Collectors.toMap(HmcGetGoodsInfoDTO::getId, o -> o, (k1, k2) -> k1)));
        }

        Map<Long, GoodsSkuDTO> map = inventory.stream().collect(Collectors.toMap(GoodsSkuDTO::getGoodsId, Function.identity()));
        Map<Long, Long> goodsCountMap = insuranceFetchPlanDetailApi.queryGoodsCount(goodsIds);
        newPage.getRecords().forEach(hmcGoodsVO -> {
            hmcGoodsVO.setQty(map.get(hmcGoodsVO.getGoodsId()).getQty());
            hmcGoodsVO.setFrozenQty(goodsCountMap.getOrDefault(hmcGoodsVO.getGoodsId(), 0L));
            hmcGoodsVO.setSkuId(hmcGoodsVO.getSellSpecificationsId());

            IHGoodsInfoVO ihGoodsInfoVO = new IHGoodsInfoVO();
            if (promotionMap.containsKey(hmcGoodsVO.getIhPharmacyGoodsId().intValue())) {
                ihGoodsInfoVO = PojoUtils.map(promotionMap.get(hmcGoodsVO.getIhPharmacyGoodsId().intValue()), IHGoodsInfoVO.class);
            }
            hmcGoodsVO.setIhGoodsInfoVO(ihGoodsInfoVO);
        });
        return Result.success(newPage);
    }

    @ApiOperation(value = "修改药+险库存", httpMethod = "POST")
    @PostMapping("/updateGoodsInventoryBySku")
    public Result<Boolean> updateStock(@RequestBody UpdateInventoryForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        goodsHmcApi.updateGoodsInventoryBySku(form.getSkuId(), form.getInventoryQty(), staffInfo.getCurrentUserId());
        return Result.success(true);
    }

    @ApiOperation(value = "修改价格库存信息", httpMethod = "POST")
    @PostMapping("/updateGoodsPriceAndStock")
    public Result<Boolean> updateGoodsPriceAndStock(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody UpdateGoodsStockAndPriceForm form) {
        HmcUpdateGoodsPriceAndPriceRequest request = PojoUtils.map(form, HmcUpdateGoodsPriceAndPriceRequest.class);
        boolean result = diagnosisApi.updateGoodsPriceAndStock(request);
        return Result.success(result);
    }

}
