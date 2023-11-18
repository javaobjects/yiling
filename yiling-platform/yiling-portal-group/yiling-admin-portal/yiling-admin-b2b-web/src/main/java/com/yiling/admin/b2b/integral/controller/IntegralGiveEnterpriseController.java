package com.yiling.admin.b2b.integral.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.b2b.integral.form.AddIntegralGiveEnterpriseForm;
import com.yiling.admin.b2b.integral.form.DeleteIntegralGiveEnterpriseForm;
import com.yiling.admin.b2b.integral.form.QueryIntegralEnterprisePageListForm;
import com.yiling.admin.b2b.integral.form.QueryIntegralGiveEnterprisePageForm;
import com.yiling.admin.b2b.integral.vo.IntegralGiveEnterpriseVO;
import com.yiling.admin.b2b.strategy.vo.EnterpriseVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.integral.api.IntegralGiveEnterpriseApi;
import com.yiling.user.integral.dto.IntegralOrderGiveEnterpriseDTO;
import com.yiling.user.integral.dto.request.AddIntegralGiveEnterpriseRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGiveEnterpriseRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveEnterprisePageRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 订单送积分-指定客户 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-04
 */
@Api(tags = "订单送积分-指定客户接口")
@RestController
@RequestMapping("integralGiveEnterprise")
public class IntegralGiveEnterpriseController extends BaseController {

    @DubboReference
    IntegralGiveEnterpriseApi integralGiveEnterpriseApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    @ApiOperation(value = "订单送积分-选择需添加客户列表查询")
    @PostMapping("/page")
    public Result<Page<EnterpriseVO>> page(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid QueryIntegralEnterprisePageListForm form) {
        QueryEnterprisePageListRequest request = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        request.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
        request.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        List<Integer> notInTypeList = ListUtil.toList(EnterpriseTypeEnum.INDUSTRY.getCode(), EnterpriseTypeEnum.BUSINESS.getCode());
        request.setNotInTypeList(notInTypeList);

        Page<EnterpriseDTO> page = enterpriseApi.pageList(request);
        return Result.success(PojoUtils.map(page, EnterpriseVO.class));
    }

    @ApiOperation(value = "订单送积分-已添加客户列表查询")
    @PostMapping("/pageList")
    public Result<Page<IntegralGiveEnterpriseVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody QueryIntegralGiveEnterprisePageForm form) {
        QueryIntegralGiveEnterprisePageRequest request = PojoUtils.map(form, QueryIntegralGiveEnterprisePageRequest.class);
        Page<IntegralOrderGiveEnterpriseDTO> dtoPage = integralGiveEnterpriseApi.pageList(request);
        Page<IntegralGiveEnterpriseVO> voPage = PojoUtils.map(dtoPage, IntegralGiveEnterpriseVO.class);
        if (CollUtil.isNotEmpty(voPage.getRecords())) {
            List<Long> eidList = voPage.getRecords().stream().map(IntegralGiveEnterpriseVO::getEid).distinct().collect(Collectors.toList());
            Map<Long, String> map = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(BaseDTO::getId, EnterpriseDTO::getName));
            voPage.getRecords().forEach(integralGiveEnterpriseVO -> integralGiveEnterpriseVO.setEname(map.get(integralGiveEnterpriseVO.getEid())));
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "订单送积分-添加客户")
    @PostMapping("/add")
    @Log(title = "订单送积分-添加客户", businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> add(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid AddIntegralGiveEnterpriseForm form) {
        AddIntegralGiveEnterpriseRequest request = PojoUtils.map(form, AddIntegralGiveEnterpriseRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(integralGiveEnterpriseApi.add(request));
    }

    @ApiOperation(value = "订单送积分-删除客户")
    @PostMapping("/delete")
    @Log(title = "订单送积分-删除客户", businessType = BusinessTypeEnum.DELETE)
    public Result<Object> delete(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid DeleteIntegralGiveEnterpriseForm form) {
        DeleteIntegralGiveEnterpriseRequest request = PojoUtils.map(form, DeleteIntegralGiveEnterpriseRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        return Result.success(integralGiveEnterpriseApi.delete(request));
    }
}
