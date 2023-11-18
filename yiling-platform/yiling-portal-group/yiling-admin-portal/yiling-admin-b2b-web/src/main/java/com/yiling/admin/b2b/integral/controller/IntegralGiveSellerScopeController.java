package com.yiling.admin.b2b.integral.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.integral.form.AddIntegralOrderSellerForm;
import com.yiling.admin.b2b.integral.form.DeleteIntegralGiveSellerForm;
import com.yiling.admin.b2b.integral.form.QueryGiveIntegralSellerScopePageForm;
import com.yiling.admin.b2b.integral.vo.EnterpriseTagOptionVO;
import com.yiling.admin.b2b.integral.vo.IntegralGiveSellerVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterpriseTagApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseTagDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.integral.api.IntegralGiveSellerApi;
import com.yiling.user.integral.dto.IntegralOrderSellerDTO;
import com.yiling.user.integral.dto.request.AddIntegralOrderSellerRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveSellerRequest;
import com.yiling.user.integral.dto.request.QueryGiveIntegralSellerPageRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单送积分-商家范围 Controller
 *
 * @author: lun.yu
 * @date: 2023-01-03
 */
@Slf4j
@RestController
@RequestMapping("/integralGiveSellerScope")
@Api(tags = "订单送积分-商家范围接口")
public class IntegralGiveSellerScopeController extends BaseController {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    IntegralGiveSellerApi integralGiveSellerApi;
    @DubboReference
    EnterpriseTagApi enterpriseTagApi;

    @ApiOperation(value = "订单送积分-指定商家-已添加商家分页列表查询")
    @PostMapping("/pageList")
    public Result<Page<IntegralGiveSellerVO>> pageList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryGiveIntegralSellerScopePageForm form) {
        QueryGiveIntegralSellerPageRequest request = PojoUtils.map(form, QueryGiveIntegralSellerPageRequest.class);
        List<Long> eidListByTagIdList = ListUtil.toList();
        if (CollUtil.isNotEmpty(form.getTagIds())) {
            eidListByTagIdList = enterpriseTagApi.getEidListByTagIdList(form.getTagIds());
        }

        if (StringUtils.isNotEmpty(form.getProvinceCode()) || StringUtils.isNotEmpty(form.getCityCode()) || StringUtils.isNotEmpty(form.getRegionCode())
                || StrUtil.isNotEmpty(form.getEname()) || CollUtil.isNotEmpty(form.getTagIds())) {
            QueryEnterpriseListRequest eidRequest = PojoUtils.map(form, QueryEnterpriseListRequest.class);
            eidRequest.setName(form.getEname());
            eidRequest.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
            eidRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            if (CollUtil.isNotEmpty(eidListByTagIdList)) {
                eidRequest.setIdList(eidListByTagIdList);
            }
            List<Long> eidList = enterpriseApi.queryListByArea(eidRequest).stream().map(BaseDTO::getId).distinct().collect(Collectors.toList());
            request.setEidList(eidList);
        }

        Page<IntegralOrderSellerDTO> dtoPage = integralGiveSellerApi.pageList(request);
        Page<IntegralGiveSellerVO> voPage = PojoUtils.map(dtoPage, IntegralGiveSellerVO.class);

        List<Long> eidList = voPage.getRecords().stream().map(IntegralGiveSellerVO::getEid).distinct().collect(Collectors.toList());
        Map<Long, EnterpriseDTO> eidMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(eidList)) {
            eidMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity()));
        }
        Map<Long, EnterpriseDTO> finalEidMap = eidMap;
        voPage.getRecords().forEach(integralGiveSellerVO -> {
            EnterpriseDTO enterpriseDTO = finalEidMap.get(integralGiveSellerVO.getEid());
            integralGiveSellerVO.setEname(enterpriseDTO.getName());
            integralGiveSellerVO.setAddress(enterpriseDTO.getProvinceName() + enterpriseDTO.getCityName() + enterpriseDTO.getRegionName());
        });

        return Result.success(voPage);
    }

    @ApiOperation(value = "订单送积分-指定商家-添加商家")
    @PostMapping("/add")
    @Log(title = "订单送积分-指定商家-添加商家", businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddIntegralOrderSellerForm form) {
        AddIntegralOrderSellerRequest request = PojoUtils.map(form, AddIntegralOrderSellerRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(integralGiveSellerApi.add(request));
    }

    @ApiOperation(value = "订单送积分-指定商家-删除商家")
    @PostMapping("/delete")
    @Log(title = "订单送积分-指定商家-删除商家", businessType = BusinessTypeEnum.DELETE)
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeleteIntegralGiveSellerForm form) {
        DeleteIntegralGiveSellerRequest request = PojoUtils.map(form, DeleteIntegralGiveSellerRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return  Result.success(integralGiveSellerApi.delete(request));
    }

    @ApiOperation(value = "获取企业标签选择项列表")
    @GetMapping("/selectTags")
    public Result<CollectionObject<EnterpriseTagOptionVO>> selectTags(@CurrentUser CurrentAdminInfo staffInfo) {
        List<EnterpriseTagDTO> list = enterpriseTagApi.listAll(EnableStatusEnum.ENABLED);
        List<EnterpriseTagOptionVO> voList = PojoUtils.map(list, EnterpriseTagOptionVO.class);
        return Result.success(new CollectionObject<>(voList));
    }

}
