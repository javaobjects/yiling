package com.yiling.admin.hmc.goods.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.goods.form.AddGoodsControlForm;
import com.yiling.admin.hmc.goods.form.HmcGoodsPageForm;
import com.yiling.admin.hmc.goods.form.StandardSpecificationPageForm;
import com.yiling.admin.hmc.goods.form.UpdateGoodsControlForm;
import com.yiling.admin.hmc.goods.vo.GoodsControlVO;
import com.yiling.admin.hmc.goods.vo.StandardGoodsSpecificationVO;
import com.yiling.admin.hmc.insurance.vo.GoodsDisableVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.request.StandardSpecificationPageRequest;
import com.yiling.hmc.control.api.GoodsPurchaseControlApi;
import com.yiling.hmc.goods.api.GoodsControlApi;
import com.yiling.hmc.goods.bo.GoodsControlBO;
import com.yiling.hmc.goods.dto.request.GoodsControlPageRequest;
import com.yiling.hmc.goods.dto.request.GoodsControlSaveRequest;
import com.yiling.hmc.insurance.api.InsuranceDetailApi;
import com.yiling.hmc.insurance.dto.InsuranceDetailDTO;
import com.yiling.hmc.insurance.enums.InsuranceStatusEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/3/31
 */
@RestController
@Api(tags = "保险商品管理模块")
@RequestMapping("/control/goods")
@Slf4j
public class GoodsControlController extends BaseController {

    @DubboReference
    GoodsControlApi goodsControlApi;

    @DubboReference
    GoodsHmcApi goodsHmcApi;

    @DubboReference
    GoodsPurchaseControlApi goodsPurchaseControlApi;

    @DubboReference
    InsuranceDetailApi insuranceDetailApi;


    @ApiOperation(value = "选择商品")
    @GetMapping("queryStandardSpecificationPage")
    public Result<Page<StandardGoodsSpecificationVO>> queryStandardSpecificationPage(StandardSpecificationPageForm standardSpecificationPageForm) {
        StandardSpecificationPageRequest request = new StandardSpecificationPageRequest();
        PojoUtils.map(standardSpecificationPageForm, request);
        Page<StandardGoodsSpecificationDTO> specificationDTOPage = goodsHmcApi.queryStandardSpecificationPage(request);
        Page<StandardGoodsSpecificationVO> specificationVOPage = PojoUtils.map(specificationDTOPage, StandardGoodsSpecificationVO.class);
        return Result.success(specificationVOPage);
    }

    @ApiOperation(value = "添加")
    @PostMapping("/add")
    public Result<Boolean> add(@CurrentUser CurrentAdminInfo currentAdminInfo, @Valid @RequestBody AddGoodsControlForm addGoodsControlForm) {
        GoodsControlSaveRequest request = new GoodsControlSaveRequest();
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        PojoUtils.map(addGoodsControlForm, request);
        goodsControlApi.saveOrUpdateGoodsControl(request);
        return Result.success(true);
    }

    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public Result<Boolean> update(@CurrentUser CurrentAdminInfo currentAdminInfo, @Valid @RequestBody UpdateGoodsControlForm updateGoodsControlForm) {
        GoodsControlSaveRequest request = new GoodsControlSaveRequest();
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        PojoUtils.map(updateGoodsControlForm, request);
        goodsControlApi.saveOrUpdateGoodsControl(request);
        return Result.success(true);
    }

    @ApiOperation(value = "列表")
    @GetMapping("queryPage")
    public Result<Page<GoodsControlVO>> queryPage(HmcGoodsPageForm hmcGoodsPageForm) {
        GoodsControlPageRequest request = new GoodsControlPageRequest();
        PojoUtils.map(hmcGoodsPageForm, request);
        Page<GoodsControlBO> goodsControlBOPage = goodsControlApi.pageList(request);
        if (goodsControlBOPage.getTotal() == 0) {
            return Result.success(hmcGoodsPageForm.getPage());
        }

        Page<GoodsControlVO> goodsControlVOPage = PojoUtils.map(goodsControlBOPage, GoodsControlVO.class);
        goodsControlVOPage.getRecords().forEach(goodsControlVO -> {
            goodsControlVO.setControlStatus(goodsPurchaseControlApi.getByGoodControlId(goodsControlVO.getId()));
        });
        return Result.success(goodsControlVOPage);
    }

    @ApiOperation(value = "药品列表(保险公司里面新增保险里面使用)")
    @GetMapping("pageInsuranceCompanyGoods")
    public Result<Page<GoodsControlVO>> pageInsuranceCompanyGoods(HmcGoodsPageForm form) {
        GoodsControlPageRequest request = PojoUtils.map(form, GoodsControlPageRequest.class);
        Page<GoodsControlBO> boPage = goodsControlApi.pageList(request);

        Page<GoodsControlVO> voPage = PojoUtils.map(boPage, GoodsControlVO.class);
        if (boPage.getTotal() == 0) {
            return Result.success(voPage);
        }

        List<Long> controlIdList = voPage.getRecords().stream().map(GoodsControlVO::getId).collect(Collectors.toList());
        List<InsuranceDetailDTO> insuranceDetailList = insuranceDetailApi.listByControlIdAndCompanyAndInsuranceStatus(controlIdList, form.getInsuranceCompanyId(), InsuranceStatusEnum.ENABLE.getCode());
        List<Long> idList = insuranceDetailList.stream().map(InsuranceDetailDTO::getControlId).collect(Collectors.toList());
        voPage.getRecords().forEach(e -> {
            if (null != form.getInsuranceCompanyId()) {
                GoodsDisableVO disableVO = new GoodsDisableVO();
                disableVO.setIsAllowSelect(0);
                // 如果保险公司已经存在此商品的启用保险
                if (idList.contains(e.getId())) {
                    disableVO.setIsAllowSelect(1);
                }
                e.setGoodsDisableVO(disableVO);
            }
        });
        return Result.success(voPage);
    }

    @ApiOperation(value = "药品列表-关联保险的药品去重(商家提成设置里面使用)")
    @GetMapping("pageInsuranceGoods")
    public Result<Page<GoodsControlVO>> pageInsuranceGoods(HmcGoodsPageForm hmcGoodsPageForm) {
        GoodsControlPageRequest request = new GoodsControlPageRequest();
        PojoUtils.map(hmcGoodsPageForm, request);
        Page<GoodsControlBO> boPage = goodsControlApi.queryInsuranceGoodsControlPageList(request);
        Page<GoodsControlVO> voPage = PojoUtils.map(boPage, GoodsControlVO.class);
        if (CollUtil.isEmpty(boPage.getRecords())) {
            return Result.success(voPage);
        }

        voPage.getRecords().forEach(goodsControlVO -> {
            goodsControlVO.setControlStatus(goodsPurchaseControlApi.getByGoodControlId(goodsControlVO.getId()));
        });
        return Result.success(voPage);
    }
}