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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.admin.hmc.enterprise.form.EnterpriseAccountPageForm;
import com.yiling.admin.hmc.enterprise.form.EnterpriseAccountSaveForm;
import com.yiling.admin.hmc.enterprise.form.EnterprisePageForm;
import com.yiling.admin.hmc.enterprise.vo.EnterpriseAccountDetailVO;
import com.yiling.admin.hmc.enterprise.vo.EnterpriseAccountVO;
import com.yiling.admin.hmc.enterprise.vo.EnterpriseVO;
import com.yiling.admin.hmc.enterprise.vo.GoodsVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsHmcApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.hmc.enterprise.api.EnterpriseAccountApi;
import com.yiling.hmc.enterprise.dto.EnterpriseAccountDTO;
import com.yiling.hmc.enterprise.dto.request.EnterpriseAccountPageRequest;
import com.yiling.hmc.enterprise.dto.request.EnterpriseAccountSaveRequest;
import com.yiling.hmc.goods.api.GoodsApi;
import com.yiling.hmc.goods.dto.HmcGoodsDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 保险药品商家结算账号表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-24
 */
@Api(tags = "保险药品商家结算账号接口")
@RestController
@RequestMapping("/enterprise/account")
public class EnterpriseAccountController extends BaseController {

    @DubboReference
    EnterpriseAccountApi enterpriseAccountApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    GoodsHmcApi goodsHmcApi;

    @ApiOperation(value = "保险药品商家结算账号信息分页查询")
    @PostMapping("/pageList")
    public Result<Page<EnterpriseAccountVO>> pageList(@RequestBody EnterpriseAccountPageForm form) {
        EnterpriseAccountPageRequest request = PojoUtils.map(form, EnterpriseAccountPageRequest.class);
        Page<EnterpriseAccountDTO> dtoPage = enterpriseAccountApi.pageList(request);
        Page<EnterpriseAccountVO> voPage = PojoUtils.map(dtoPage, EnterpriseAccountVO.class);
        return Result.success(voPage);
    }

    @ApiOperation(value = "通过id查询保险药品商家结算账号信息")
    @GetMapping("/queryById")
    public Result<EnterpriseAccountVO> queryById(@RequestParam("id") @ApiParam("id") Long id) {
        EnterpriseAccountDTO dto = enterpriseAccountApi.queryById(id);
        EnterpriseAccountVO enterpriseAccountVO = PojoUtils.map(dto, EnterpriseAccountVO.class);
        return Result.success(enterpriseAccountVO);
    }

    @ApiOperation(value = "通过eid查询保险药品商家结算账号信息")
    @GetMapping("/queryByEid")
    public Result<EnterpriseAccountVO> queryByEid(@RequestParam("eid") @ApiParam("eid") Long eid) {
        EnterpriseAccountDTO dto = enterpriseAccountApi.queryByEid(eid);
        EnterpriseAccountVO enterpriseAccountVO = PojoUtils.map(dto, EnterpriseAccountVO.class);
        return Result.success(enterpriseAccountVO);
    }

    @ApiOperation(value = "通过id查询保险药品商家结算账号信息和结算设置信息")
    @GetMapping("/queryDetailById")
    public Result<EnterpriseAccountDetailVO> queryDetailById(@RequestParam("id") @ApiParam("id") Long id) {
        // 结算账户
        EnterpriseAccountDTO dto = enterpriseAccountApi.queryById(id);
        EnterpriseAccountDetailVO detailVO = PojoUtils.map(dto, EnterpriseAccountDetailVO.class);
        if (null == detailVO) {
            return Result.failed("此保险药品商家结算账号不存在");
        }

        // 药品结算信息
        List<HmcGoodsDTO> dtoList = goodsApi.listByEid(dto.getEid());
        List<GoodsVO> voList = PojoUtils.map(dtoList, GoodsVO.class);
        List<Long> goodsIdList = voList.stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
        List<GoodsDTO> goodsInfoList = goodsHmcApi.batchQueryGoodsInfo(goodsIdList);
        Map<Long, GoodsDTO> dtoMap = goodsInfoList.stream().collect(Collectors.toMap(GoodsDTO::getId, e -> e, (k1, k2) -> k1));
        voList.forEach(e -> {
            GoodsDTO goodsDTO = dtoMap.get(e.getGoodsId());
            e.setSellSpecifications(goodsDTO.getSellSpecifications());
        });
        detailVO.setEnterpriseCommissionList(voList);
        return Result.success(detailVO);
    }

    @ApiOperation(value = "药品商家提成设置-选择商家")
    @PostMapping("/pageEnterprise")
    public Result<Page<EnterpriseVO>> pageEnterprise(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody EnterprisePageForm form) {
        QueryEnterprisePageListRequest request = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        List<Integer> typeList = Lists.newArrayList();
        typeList.add(EnterpriseTypeEnum.CHAIN_BASE.getCode());
        typeList.add(EnterpriseTypeEnum.HOSPITAL.getCode());
        typeList.add(EnterpriseTypeEnum.CLINIC.getCode());
        typeList.add(EnterpriseTypeEnum.CHAIN_DIRECT.getCode());
        typeList.add(EnterpriseTypeEnum.CHAIN_JOIN.getCode());
        typeList.add(EnterpriseTypeEnum.PHARMACY.getCode());
        //        request.setHmcType(EnterpriseHmcTypeEnum.MEDICINE_INSURANCE_CHECK.getCode());
        request.setInTypeList(typeList);
        if (StringUtils.isNotBlank(form.getEname())) {
            request.setName(form.getEname());
        }
        request.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        Page<EnterpriseDTO> pageList = enterpriseApi.pageList(request);
        Page<EnterpriseVO> voPage = PojoUtils.map(pageList, EnterpriseVO.class);
        return Result.success(voPage);
    }

    @ApiOperation(value = "保险药品商家结算账号信息新增和修改")
    @PostMapping("/save")
    public Result saveEnterpriseAccount(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid EnterpriseAccountSaveForm form) {
        EnterpriseAccountSaveRequest request = PojoUtils.map(form, EnterpriseAccountSaveRequest.class);
        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        // 1.校验一个商家只能有一个结算账户
        EnterpriseAccountDTO enterpriseAccountDTO = enterpriseAccountApi.queryByEid(request.getEid());
        boolean isRepeat = (null == request.getId() && null != enterpriseAccountDTO) || (null != request.getId() && !request.getId().equals(enterpriseAccountDTO.getId()));
        if (isRepeat) {
            return Result.failed("一个商家只能有一个结算账户");
        }

        // 2.账户的新增和修改
        boolean isSuccess = enterpriseAccountApi.saveEnterpriseAccount(request);
        if (isSuccess) {
            return Result.success();
        } else {
            return Result.failed("保存出现问题");
        }
    }
}
