package com.yiling.f2b.admin.enterprise.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.f2b.admin.enterprise.form.AddPurchaseRelationForm;
import com.yiling.f2b.admin.enterprise.form.QueryCanPurchaseEnterprisePageListForm;
import com.yiling.f2b.admin.enterprise.form.QueryCountSellerChannelForm;
import com.yiling.f2b.admin.enterprise.form.QueryPurchaseRelationPageListForm;
import com.yiling.f2b.admin.enterprise.form.RemovePurchaseRelationForm;
import com.yiling.f2b.admin.enterprise.vo.CanPurchaseRelationVO;
import com.yiling.f2b.admin.enterprise.vo.CountSellerChannelVO;
import com.yiling.f2b.admin.enterprise.vo.PurchaseRelationVO;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.user.enterprise.api.EnterprisePurchaseRelationApi;
import com.yiling.user.enterprise.dto.CountSellerChannelDTO;
import com.yiling.user.enterprise.dto.EnterprisePurchaseRelationDTO;
import com.yiling.user.enterprise.dto.request.QueryPurchaseRelationPageListRequest;
import com.yiling.user.enterprise.dto.request.RemovePurchaseRelationFormRequest;
import com.yiling.user.enterprise.dto.request.SavePurchaseRelationFormRequest;

import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 企业采购关系模块 Controller
 *
 * @author: yuecheng.chen
 * @date: 2021/6/7
 */
@RestController
@RequestMapping("/purchase/relation")
@Api(tags = "企业采购关系模块")
@Slf4j
public class EnterprisePurchaseRelationController {

    @DubboReference
    EnterprisePurchaseRelationApi enterprisePurchaseRelationApi;

    @ApiOperation(value = "查询渠道商采购关系信息分页列表")
    @PostMapping("/pageList")
    public Result<Page<PurchaseRelationVO>> pageList(@RequestBody QueryPurchaseRelationPageListForm form) {
        QueryPurchaseRelationPageListRequest request = PojoUtils.map(form, QueryPurchaseRelationPageListRequest.class);
        Page<EnterprisePurchaseRelationDTO> page = enterprisePurchaseRelationApi.pageList(request);
        List<EnterprisePurchaseRelationDTO> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(request.getPage());
        }
        List<PurchaseRelationVO> list = Lists.newArrayList();
        records.forEach(e -> {
            PurchaseRelationVO item = new PurchaseRelationVO();
            // 属性拷贝
            PojoUtils.map(e, item);
            item.setEid(e.getSellerEid());
            list.add(item);
        });
        Page<PurchaseRelationVO> pageVO = PojoUtils.map(page, PurchaseRelationVO.class);
        pageVO.setRecords(list);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "查询渠道商可供采购企业分页列表")
    @PostMapping("/canPurchaseEnterprisePageList")
    public Result<Page<CanPurchaseRelationVO>> canPurchaseEnterprisePageList(@RequestBody QueryCanPurchaseEnterprisePageListForm form) {
        QueryPurchaseRelationPageListRequest request = PojoUtils.map(form, QueryPurchaseRelationPageListRequest.class);
        Page<EnterprisePurchaseRelationDTO> page = enterprisePurchaseRelationApi.canPurchaseEnterprisePageList(request);
        List<EnterprisePurchaseRelationDTO> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(request.getPage());
        }

        List<CanPurchaseRelationVO> list = records.stream().map(e -> {
            CanPurchaseRelationVO item = PojoUtils.map(e, CanPurchaseRelationVO.class);
            item.setEid(e.getSellerEid());
            return item;
        }).collect(Collectors.toList());

        Page<CanPurchaseRelationVO> pageVO = PojoUtils.map(page, CanPurchaseRelationVO.class);
        pageVO.setRecords(list);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "添加采购关系")
    @PostMapping("/addPurchaseRelation")
    @Log(title = "添加采购关系", businessType = BusinessTypeEnum.INSERT)
    public Result<BoolObject> addPurchaseRelation (@RequestBody @Valid AddPurchaseRelationForm form) {
        SavePurchaseRelationFormRequest request = PojoUtils.map(form, SavePurchaseRelationFormRequest.class);
        return Result.success(new BoolObject(enterprisePurchaseRelationApi.addPurchaseRelation(request)));
    }

    @ApiOperation(value = "移除采购关系")
    @PostMapping("/removePurchaseRelation")
    @Log(title = "移除采购关系", businessType = BusinessTypeEnum.DELETE)
    public Result<BoolObject> removePurchaseRelation(@RequestBody @Valid RemovePurchaseRelationForm form) {
        RemovePurchaseRelationFormRequest request = PojoUtils.map(form, RemovePurchaseRelationFormRequest.class);
        return Result.success(new BoolObject(enterprisePurchaseRelationApi.removePurchaseRelation(request)));
    }

    @ApiOperation(value = "统计渠道商采购销购销售渠道商的类型及个数")
    @PostMapping("/countSellerChannel")
    public Result<CountSellerChannelVO> countSellerChannel(@RequestBody QueryCountSellerChannelForm form) {
        QueryPurchaseRelationPageListRequest request = PojoUtils.map(form, QueryPurchaseRelationPageListRequest.class);
        CountSellerChannelDTO countSellerChannelDTO = enterprisePurchaseRelationApi.countSellerChannel(request);
        return Result.success(PojoUtils.map(countSellerChannelDTO, CountSellerChannelVO.class));
    }

}
