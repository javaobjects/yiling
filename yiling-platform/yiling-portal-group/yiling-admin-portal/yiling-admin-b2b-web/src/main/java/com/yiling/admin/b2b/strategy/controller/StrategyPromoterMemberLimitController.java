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
import com.yiling.admin.b2b.strategy.form.AddStrategyPromoterMemberLimitForm;
import com.yiling.admin.b2b.strategy.form.DeleteStrategyPromoterMemberLimitForm;
import com.yiling.admin.b2b.strategy.form.QueryStrategyPromoterMemberLimitPageForm;
import com.yiling.admin.b2b.strategy.vo.StrategyPromoterMemberLimitVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.strategy.api.StrategyPromoterMemberApi;
import com.yiling.marketing.strategy.dto.StrategyPromoterMemberLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyPromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyPromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyPromoterMemberLimitPageRequest;
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
 * 策略满赠推广方会员表 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-08-29
 */
@Api(tags = "策略满赠-推广方会员")
@RestController
@RequestMapping("/strategy/limit/promoter/member")
public class StrategyPromoterMemberLimitController extends BaseController {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    StrategyPromoterMemberApi strategyPromoterMemberApi;

    @ApiOperation(value = "策略满赠指定推广方会员-已添加推广方会员商家列表查询-运营后台")
    @PostMapping("/pageList")
    public Result<Page<StrategyPromoterMemberLimitVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryStrategyPromoterMemberLimitPageForm form) {
        QueryStrategyPromoterMemberLimitPageRequest request = PojoUtils.map(form, QueryStrategyPromoterMemberLimitPageRequest.class);
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

        Page<StrategyPromoterMemberLimitDTO> dtoPage = strategyPromoterMemberApi.pageList(request);
        Page<StrategyPromoterMemberLimitVO> voPage = PojoUtils.map(dtoPage, StrategyPromoterMemberLimitVO.class);
        for (StrategyPromoterMemberLimitVO promoterMemberLimitVO : voPage.getRecords()) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(promoterMemberLimitVO.getEid());
            promoterMemberLimitVO.setEname(enterpriseDTO.getName());
            promoterMemberLimitVO.setAddress(enterpriseDTO.getProvinceName() + enterpriseDTO.getCityName() + enterpriseDTO.getRegionName());
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "策略满赠指定推广方会员商家-添加推广方会员商家-运营后台")
    @PostMapping("/add")
    public Result<Object> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddStrategyPromoterMemberLimitForm form) {
        AddStrategyPromoterMemberLimitRequest request = PojoUtils.map(form, AddStrategyPromoterMemberLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyPromoterMemberApi.add(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("添加失败");
    }

    @ApiOperation(value = "策略满赠指定推广方会员商家-删除商家-运营后台")
    @PostMapping("/delete")
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeleteStrategyPromoterMemberLimitForm form) {
        DeleteStrategyPromoterMemberLimitRequest request = PojoUtils.map(form, DeleteStrategyPromoterMemberLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyPromoterMemberApi.delete(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }
}
