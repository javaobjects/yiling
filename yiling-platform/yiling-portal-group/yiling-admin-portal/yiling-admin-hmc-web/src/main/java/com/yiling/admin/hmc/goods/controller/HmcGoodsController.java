package com.yiling.admin.hmc.goods.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.yiling.admin.hmc.goods.vo.IHGoodsInfoVO;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.ih.patient.dto.HmcGetGoodsInfoDTO;
import com.yiling.ih.patient.dto.request.HmcGetIHGoodsInfoRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.common.utils.PictureUrlUtils;
import com.yiling.admin.hmc.goods.form.QueryEnterpriseGoodsPageListForm;
import com.yiling.admin.hmc.goods.form.QueryGoodsPageListForm;
import com.yiling.admin.hmc.goods.vo.EnterpriseGoodsListVO;
import com.yiling.admin.hmc.goods.vo.HmcGoodsVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.hmc.goods.api.GoodsApi;
import com.yiling.hmc.goods.bo.EnterpriseGoodsCountBO;
import com.yiling.hmc.goods.bo.HmcGoodsBO;
import com.yiling.hmc.goods.dto.request.HmcGoodsPageRequest;
import com.yiling.hmc.wechat.api.InsuranceFetchPlanDetailApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

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

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    GoodsHmcApi goodsHmcApi;

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    InsuranceFetchPlanDetailApi insuranceFetchPlanDetailApi;

    @DubboReference
    HmcDiagnosisApi diagnosisApi;

    @ApiOperation(value = "商家列表查询", httpMethod = "GET")
    @GetMapping("/enterpriseList")
    public Result<Page<EnterpriseGoodsListVO>> enterpriseList(@Valid QueryEnterpriseGoodsPageListForm form) {
        QueryEnterprisePageListRequest request = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        //request.setMallFlag(1);
        // gxl 显示的为已经开通药+险业务项，且开通了销售与兑付的企业进行显示
        request.setHmcType(2);
        Page<EnterpriseDTO> page = enterpriseApi.pageList(request);
        if(page.getTotal()==0){
            return Result.success(form.getPage());
        }
        Page<EnterpriseGoodsListVO> pageVO = PojoUtils.map(page, EnterpriseGoodsListVO.class);
        List<Long> longs = page.getRecords().stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
        List<EnterpriseGoodsCountBO> enterpriseGoodsCountBOS = goodsApi.countGoodsByEids(longs);
        Map<Long, EnterpriseGoodsCountBO> map = enterpriseGoodsCountBOS.stream().collect(Collectors.toMap(EnterpriseGoodsCountBO::getEid, Function.identity()));
        pageVO.getRecords().forEach(e -> {
            //数量统计
            EnterpriseGoodsCountBO enterpriseGoodsCountBO = map.get(e.getId());
            e.setUpShelfCount(enterpriseGoodsCountBO == null ? 0L : enterpriseGoodsCountBO.getUpGoodsCount());
            e.setUnShelfCount(enterpriseGoodsCountBO == null ? 0L : enterpriseGoodsCountBO.getUnGoodsCount());
            e.setGoodsCount(enterpriseGoodsCountBO == null ? 0L : enterpriseGoodsCountBO.getGoodsCount());
        });
        return Result.success(pageVO);
    }


    @ApiOperation(value = "商家商品列表", httpMethod = "GET")
    @GetMapping("/list")
    public Result<Page<HmcGoodsVO>> list(@CurrentUser CurrentAdminInfo adminInfo, @Valid QueryGoodsPageListForm form) {
        HmcGoodsPageRequest request = PojoUtils.map(form, HmcGoodsPageRequest.class);
        request.setEid(form.getEid());
        Page<HmcGoodsBO> page = goodsApi.pageListByEid(request);
        if(page.getTotal()==0){
            return Result.success(form.getPage());
        }
        List<HmcGoodsBO> goodsBOS = page.getRecords();
        List<Long> goodsIds = goodsBOS.stream().map(HmcGoodsBO::getGoodsId).distinct().collect(Collectors.toList());
        List<GoodsSkuDTO> goodsSkuList = goodsHmcApi.getGoodsSkuByGids(goodsIds);

        // 获取IH配送商商品id
        Map<Integer, HmcGetGoodsInfoDTO> promotionMap= Maps.newHashMap();
        List<Long> ihPharmacyGoodsIds = goodsBOS.stream().filter(item->item.getIhEid()>0 && item.getIhPharmacyGoodsId()>0).map(HmcGoodsBO::getIhPharmacyGoodsId).distinct().collect(Collectors.toList());
        if(CollUtil.isNotEmpty(ihPharmacyGoodsIds)) {
            HmcGetIHGoodsInfoRequest getIHGoodsInfoRequest = new HmcGetIHGoodsInfoRequest();
            getIHGoodsInfoRequest.setIds(ihPharmacyGoodsIds);
            List<HmcGetGoodsInfoDTO> ihPharmacyGoodsInfo = diagnosisApi.getIHPharmacyGoodsInfo(getIHGoodsInfoRequest);
            promotionMap.putAll(ihPharmacyGoodsInfo.stream().collect(Collectors.toMap(HmcGetGoodsInfoDTO::getId, o -> o, (k1, k2) -> k1)));
        }

        Map<Long, GoodsSkuDTO> map = goodsSkuList.stream().collect(Collectors.toMap(GoodsSkuDTO::getGoodsId, Function.identity()));
        Page<HmcGoodsVO> newPage = PojoUtils.map(page,HmcGoodsVO.class);
        Map<Long, Long> goodsCountMap = insuranceFetchPlanDetailApi.queryGoodsCount(goodsIds);
        newPage.getRecords().forEach(hmcGoodsVO -> {
            Long qty = map.get(hmcGoodsVO.getGoodsId()).getQty();
            hmcGoodsVO.setQty(qty);
            //药加险所需库存
            hmcGoodsVO.setFrozenQty(goodsCountMap.getOrDefault(hmcGoodsVO.getGoodsId(),0L));
            hmcGoodsVO.setSkuId(hmcGoodsVO.getSellSpecificationsId());

            IHGoodsInfoVO ihGoodsInfoVO = new IHGoodsInfoVO();
            if (promotionMap.containsKey(hmcGoodsVO.getIhPharmacyGoodsId().intValue())) {
                ihGoodsInfoVO = PojoUtils.map(promotionMap.get(hmcGoodsVO.getIhPharmacyGoodsId().intValue()), IHGoodsInfoVO.class);
            }
            hmcGoodsVO.setIhGoodsInfoVO(ihGoodsInfoVO);
        });
        return Result.success(newPage);
    }


}
