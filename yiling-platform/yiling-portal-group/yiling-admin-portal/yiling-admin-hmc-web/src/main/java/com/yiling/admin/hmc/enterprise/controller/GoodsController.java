package com.yiling.admin.hmc.enterprise.controller;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.hmc.enterprise.form.GoodsSaveListForm;
import com.yiling.admin.hmc.enterprise.vo.GoodsListVO;
import com.yiling.admin.hmc.enterprise.vo.GoodsVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.hmc.enterprise.dto.request.GoodsSaveListRequest;
import com.yiling.hmc.goods.api.GoodsApi;
import com.yiling.hmc.goods.dto.HmcGoodsDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * C端保险药品商家提成设置表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-24
 */
@Api(tags = "保险药品商家提成设置接口")
@Slf4j
@RestController
@RequestMapping("/goods/goods")
public class GoodsController extends BaseController {

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    GoodsHmcApi goodsHmcApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @ApiOperation(value = "通过企业id查询C端保险药品商家提成设置信息")
    @GetMapping("/queryByEId")
    public Result<GoodsListVO> queryById(@RequestParam("eid") @ApiParam("企业id") Long eid) {
        GoodsListVO voResult = new GoodsListVO();
        List<HmcGoodsDTO> dtoList = goodsApi.listByEid(eid);
        List<GoodsVO> voList = PojoUtils.map(dtoList, GoodsVO.class);
        if (CollUtil.isEmpty(voList)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eid);
            voResult.setEid(eid);
            voResult.setEname(enterpriseDTO.getName());
            return Result.success(voResult);
        }

        List<Long> goodsIdList = voList.stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
        List<GoodsDTO> goodsInfoList = goodsHmcApi.batchQueryGoodsInfo(goodsIdList);
        Map<Long, GoodsDTO> dtoMap = goodsInfoList.stream().collect(Collectors.toMap(GoodsDTO::getId, e -> e, (k1, k2) -> k1));
        voList.forEach(e -> {
            GoodsDTO goodsDTO = dtoMap.get(e.getGoodsId());
            e.setSellSpecifications(goodsDTO.getSellSpecifications());
        });

        voResult.setEid(eid);
        if (StringUtils.isNotBlank(voList.get(0).getEname())) {
            voResult.setEname(voList.get(0).getEname());
        } else {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eid);
            voResult.setEname(enterpriseDTO.getName());
        }
        voResult.setEnterpriseCommissionList(voList);
        log.info("/goods/goods/queryByEId , respond:[{}]", JSONUtil.toJsonStr(voResult));
        return Result.success(voResult);
    }

    @ApiOperation(value = "C端保险药品商家提成设置信息新增和修改")
    @PostMapping("/save")
    public Result saveEnterpriseAccount(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid GoodsSaveListForm form) {
        GoodsSaveListRequest request = PojoUtils.map(form, GoodsSaveListRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        boolean isSuccess = goodsApi.saveGoodsList(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("保存出现问题");
        }
    }
}
