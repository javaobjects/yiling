package com.yiling.admin.b2b.strategy.controller;


import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.strategy.form.AddStrategySellerLimitForm;
import com.yiling.admin.b2b.strategy.form.DeleteStrategySellerLimitForm;
import com.yiling.admin.b2b.strategy.form.QueryStrategySellerLimitPageForm;
import com.yiling.admin.b2b.strategy.vo.StrategySellerLimitVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.strategy.api.StrategySellerApi;
import com.yiling.marketing.strategy.dto.StrategySellerLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategySellerLimitPageRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterpriseListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 策略满赠商家表 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Api(tags = "策略满赠-商家")
@RestController
@RequestMapping("/strategy/limit/seller")
public class StrategySellerLimitController extends BaseController {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    StrategySellerApi strategySellerApi;

    @ApiOperation(value = "策略满赠指定商家-已添加商家列表查询-运营后台")
    @PostMapping("/pageList")
    public Result<Page<StrategySellerLimitVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryStrategySellerLimitPageForm form) {
        QueryStrategySellerLimitPageRequest request = PojoUtils.map(form, QueryStrategySellerLimitPageRequest.class);
        if (StringUtils.isNotEmpty(form.getProvinceCode()) || StringUtils.isNotEmpty(form.getCityCode()) || StringUtils.isNotEmpty(form.getRegionCode())) {
            QueryEnterpriseListRequest eidRequest = new QueryEnterpriseListRequest();
            eidRequest.setCityCode(form.getCityCode());
            eidRequest.setRegionCode(form.getRegionCode());
            eidRequest.setProvinceCode(form.getProvinceCode());
            eidRequest.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
            eidRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.queryListByArea(eidRequest);
            if (CollectionUtils.isNotEmpty(enterpriseDTOList)) {
                List<Long> eidList = enterpriseDTOList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
                request.setEidList(eidList);
            }
        }

        Page<StrategySellerLimitDTO> dtoPage = strategySellerApi.pageList(request);
        Page<StrategySellerLimitVO> voPage = PojoUtils.map(dtoPage, StrategySellerLimitVO.class);
        for (StrategySellerLimitVO sellerLimitVO : voPage.getRecords()) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(sellerLimitVO.getEid());
            sellerLimitVO.setEname(enterpriseDTO.getName());
            sellerLimitVO.setAddress(enterpriseDTO.getProvinceName() + enterpriseDTO.getCityName() + enterpriseDTO.getRegionName());
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "策略满赠指定商家-添加商家-运营后台")
    @PostMapping("/add")
    public Result<Object> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddStrategySellerLimitForm form) {
        AddStrategySellerLimitRequest request = PojoUtils.map(form, AddStrategySellerLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategySellerApi.add(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("添加失败");
    }

    @ApiOperation(value = "策略满赠指定商家-删除商家-运营后台")
    @PostMapping("/delete")
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeleteStrategySellerLimitForm form) {
        DeleteStrategySellerLimitRequest request = PojoUtils.map(form, DeleteStrategySellerLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategySellerApi.delete(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }
}
