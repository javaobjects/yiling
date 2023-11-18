package com.yiling.admin.b2b.presale.controller;


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
import com.yiling.admin.b2b.presale.form.AddPresalePromoterMemberLimitForm;
import com.yiling.admin.b2b.presale.form.DeletePresalePromoterMemberLimitForm;
import com.yiling.admin.b2b.presale.vo.PresalePromoterMemberLimitVO;
import com.yiling.admin.b2b.strategy.form.AddStrategyPromoterMemberLimitForm;
import com.yiling.admin.b2b.strategy.form.DeleteStrategyPromoterMemberLimitForm;
import com.yiling.admin.b2b.strategy.form.QueryPresalePromoterMemberLimitPageForm;
import com.yiling.admin.b2b.strategy.form.QueryStrategyPromoterMemberLimitPageForm;
import com.yiling.admin.b2b.strategy.vo.StrategyPromoterMemberLimitVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.strategy.api.StrategyPromoterMemberApi;
import com.yiling.marketing.strategy.dto.PresalePromoterMemberLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyPromoterMemberLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddPresalePromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyPromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresalePromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyPromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresalePromoterMemberLimitPageRequest;
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
 * 预售活动推广方会员表 前端控制器
 * </p>
 *
 * @author zhangy
 * @date 2022-08-29
 */
@Api(tags = "预售活动-推广方会员")
@RestController
@RequestMapping("/presalePromoter/limit/promoter/member")
public class PresalePromoterMemberLimitController extends BaseController {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    StrategyPromoterMemberApi strategyPromoterMemberApi;

    @ApiOperation(value = "预售活动指定推广方会员-已添加推广方会员商家列表查询-运营后台")
    @PostMapping("/pageList")
    public Result<Page<PresalePromoterMemberLimitVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryPresalePromoterMemberLimitPageForm form) {
        QueryPresalePromoterMemberLimitPageRequest request = PojoUtils.map(form, QueryPresalePromoterMemberLimitPageRequest.class);
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

        Page<PresalePromoterMemberLimitDTO> dtoPage = strategyPromoterMemberApi.pageListForPresale(request);
        Page<PresalePromoterMemberLimitVO> voPage = PojoUtils.map(dtoPage, PresalePromoterMemberLimitVO.class);
        for (PresalePromoterMemberLimitVO promoterMemberLimitVO : voPage.getRecords()) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(promoterMemberLimitVO.getEid());
            promoterMemberLimitVO.setEname(enterpriseDTO.getName());
            promoterMemberLimitVO.setAddress(enterpriseDTO.getProvinceName() + enterpriseDTO.getCityName() + enterpriseDTO.getRegionName());
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "预售活动指定推广方会员商家-添加推广方会员商家-运营后台")
    @PostMapping("/add")
    public Result<Object> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddPresalePromoterMemberLimitForm form) {
        AddPresalePromoterMemberLimitRequest request = PojoUtils.map(form, AddPresalePromoterMemberLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyPromoterMemberApi.addForPresale(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("添加失败");
    }

    @ApiOperation(value = "预售活动指定推广方会员商家-删除商家-运营后台")
    @PostMapping("/delete")
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeletePresalePromoterMemberLimitForm form) {
        DeletePresalePromoterMemberLimitRequest request = PojoUtils.map(form, DeletePresalePromoterMemberLimitRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = strategyPromoterMemberApi.deleteForPresale(request);
        if (isSuccess) {
            return Result.success();
        }
        return Result.failed("删除失败");
    }
}
