package com.yiling.f2b.admin.agreement.controller;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.f2b.admin.agreement.form.AgreementGoodsDetailsPageForm;
import com.yiling.f2b.admin.agreement.form.QueryAgreementGoodsPageForm;
import com.yiling.f2b.admin.agreement.vo.AgreementGoodsVO;
import com.yiling.f2b.admin.goods.vo.GoodsDisableVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.agreement.dto.AgreementGoodsDTO;
import com.yiling.user.agreement.dto.request.AgreementGoodsDetailsPageRequest;
import com.yiling.user.agreement.dto.request.QueryAgreementGoodsPageRequest;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 协议商品表 前端控制器
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-06-03
 */
@RestController
@RequestMapping("/agreement")
@Api(tags = "协议商品模块")
public class AgreementGoodsController extends BaseController {

    @DubboReference
    AgreementGoodsApi agreementGoodsApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    PopGoodsApi popGoodsApi;

    @ApiOperation(value = "pop后台补充协议商品弹框", httpMethod = "POST")
    @PostMapping("/goods/list")
    public Result<Page<AgreementGoodsVO>> goodsList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryAgreementGoodsPageForm form) {
        Page<AgreementGoodsDTO> page = agreementGoodsApi.agreementGoodsListPage(PojoUtils.map(form, QueryAgreementGoodsPageRequest.class));
        List<Long> goodsIds = page.getRecords().stream().map(AgreementGoodsDTO::getGoodsId).collect(Collectors.toList());
		Page<AgreementGoodsVO> pageVO = PojoUtils.map(page, AgreementGoodsVO.class);
		//如果协议商品为空
		if (CollUtil.isEmpty(goodsIds)){
			return Result.success(pageVO);
		}

		List<GoodsDTO> list = goodsApi.batchQueryInfo(goodsIds);
		Map<Long, GoodsDTO> map = list.stream().collect(Collectors.toMap(GoodsDTO::getId, e -> e));

        List<AgreementGoodsDTO> agreementGoodsList = Collections.emptyList();
        if (form.getTempAgreementId()!=null&&form.getTempAgreementId() != 0) {
            agreementGoodsList = agreementGoodsApi.getAgreementGoodsAgreementIdByList(form.getTempAgreementId());
        }

        List<Long> agreementGoodsIdList = agreementGoodsList.stream().map(AgreementGoodsDTO::getGoodsId).collect(Collectors.toList());

        pageVO.getRecords().forEach(e -> {
            e.setPrice(map.get(e.getGoodsId()).getPrice());
            GoodsDisableVO goodsDisableVO = new GoodsDisableVO();
            //判断渠道商品是否已经被选了
            if (CollectionUtils.isNotEmpty(agreementGoodsIdList) && agreementGoodsIdList.contains(e.getGoodsId())) {
                goodsDisableVO.setAgreementDisable(true);
                goodsDisableVO.setAgreementDesc("已被占用");
            }
            e.setGoodsDisableVO(goodsDisableVO);
        });
        return Result.success(pageVO);
    }

    @ApiOperation(value = "pop后台协议详情获取商品信息", httpMethod = "POST")
    @PostMapping("/goods/get/info")
    public Result<Page<AgreementGoodsVO>> getAgreementGoodsByAgreementIdPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid AgreementGoodsDetailsPageForm form){
        AgreementGoodsDetailsPageRequest request = PojoUtils.map(form, AgreementGoodsDetailsPageRequest.class);
        Page<AgreementGoodsDTO> goods = agreementGoodsApi.getAgreementGoodsByAgreementIdPage(request);
        List<Long> goodsIds = goods.getRecords().stream().map(AgreementGoodsDTO::getGoodsId).collect(Collectors.toList());
		Page<AgreementGoodsVO> pageVO = PojoUtils.map(goods, AgreementGoodsVO.class);
		if (CollUtil.isEmpty(goodsIds)){
			return Result.success(pageVO);
		}
		List<GoodsInfoDTO> list = popGoodsApi.batchQueryInfo(goodsIds);
        if(CollectionUtil.isNotEmpty(list)){
            Map<Long, GoodsInfoDTO> map = list.stream().collect(Collectors.toMap(GoodsInfoDTO::getId, e -> e));
            pageVO.getRecords().forEach(e -> {
                e.setPrice(map.get(e.getGoodsId()).getPrice());
            });
        }

        return Result.success(pageVO);
    }

}
