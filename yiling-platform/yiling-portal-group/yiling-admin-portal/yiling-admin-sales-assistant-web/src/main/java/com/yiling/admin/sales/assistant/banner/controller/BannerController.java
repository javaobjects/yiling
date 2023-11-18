package com.yiling.admin.sales.assistant.banner.controller;

import java.util.Date;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.sales.assistant.banner.form.SaAppBannerDeleteForm;
import com.yiling.admin.sales.assistant.banner.form.SaAppBannerPageForm;
import com.yiling.admin.sales.assistant.banner.form.SaAppBannerSaveForm;
import com.yiling.admin.sales.assistant.banner.form.SaAppBannerStatusForm;
import com.yiling.admin.sales.assistant.banner.form.SaAppBannerWeightForm;
import com.yiling.admin.sales.assistant.banner.vo.SaAppBannerVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sales.assistant.banner.api.BannerApi;
import com.yiling.sales.assistant.banner.dto.BannerDTO;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerDeleteRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerPageRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerSaveRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerStatusRequest;
import com.yiling.sales.assistant.banner.dto.request.SaAppBannerWeightRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.Admin;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 销售助手banner表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2021-09-14
 */
@RestController
@RequestMapping("/banner")
@Api(tags = "销售助手App维护banner版本模块")
@Slf4j
public class BannerController extends BaseController {

    @DubboReference
    AdminApi  adminApi;
    @DubboReference
    BannerApi     saBannerApi;
    @DubboReference
    UserApi       userApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    @ApiOperation(value = "销售助手新增和编辑banner信息")
    @PostMapping("/save")
    @Log(title = "新增和编辑Banner信息", businessType = BusinessTypeEnum.INSERT)
    public Result<Object> saveSaAppBanner(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid SaAppBannerSaveForm form) {
        SaAppBannerSaveRequest request = PojoUtils.map(form, SaAppBannerSaveRequest.class);
        if (null == request.getId()) {
            Date startTime = request.getStartTime();
            if (startTime.getTime() < DateUtil.beginOfDay(new Date()).getTime()) {
                return Result.failed("投放开始时间不应可选当天之前");
            }
            Date stopTime = request.getStopTime();
            if (startTime.getTime() > stopTime.getTime()) {
                return Result.failed("投放开始时间不应大于投放结束时间");
            }
        }
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = saBannerApi.saveSaAppBanner(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "销售助手中banner排序，权重数字修改")
    @PostMapping("/editWeight")
    @Log(title = "编辑Banner权重", businessType = BusinessTypeEnum.UPDATE)
    public Result<Object> editSaAppBannerWeight(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid SaAppBannerWeightForm form) {
        SaAppBannerWeightRequest request = PojoUtils.map(form, SaAppBannerWeightRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = saBannerApi.editSaAppBannerWeight(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "销售助手中banner的停用")
    @PostMapping("/editStatus")
    @Log(title = "编辑Banner状态", businessType = BusinessTypeEnum.UPDATE)
    public Result<Object> editSaAppBannerStatus(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid SaAppBannerStatusForm form) {
        SaAppBannerStatusRequest request = PojoUtils.map(form, SaAppBannerStatusRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = saBannerApi.editSaAppBannerStatus(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "删除销售助手中banner")
    @PostMapping("/delete")
    @Log(title = "删除Banner", businessType = BusinessTypeEnum.DELETE)
    public Result<Object> deleteSaAppBanner(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid SaAppBannerDeleteForm form) {
        SaAppBannerDeleteRequest request = PojoUtils.map(form, SaAppBannerDeleteRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        boolean isSuccess = saBannerApi.deleteSaAppBanner(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "分页列表查询销售助手中banner-运营后台")
    @PostMapping("/pageList")
    public Result<Page<SaAppBannerVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody SaAppBannerPageForm form) {
        SaAppBannerPageRequest request = PojoUtils.map(form, SaAppBannerPageRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        Page<BannerDTO> pageDTO = saBannerApi.pageSaList(request);
        Page<SaAppBannerVO> pageVO = PojoUtils.map(pageDTO, SaAppBannerVO.class);
        pageVO.getRecords().forEach(b2bAppBannerVO -> {
            Admin createUser = adminApi.getById(b2bAppBannerVO.getCreateUser());
            b2bAppBannerVO.setCreateUserName(null != createUser ? createUser.getName() : "---");

            Admin updateUser = adminApi.getById(b2bAppBannerVO.getUpdateUser());
            b2bAppBannerVO.setUpdateUserName(null != updateUser ? updateUser.getName() : "---");
        });
        return Result.success(pageVO);
    }

    @ApiOperation(value = "通过id查询banner")
    @GetMapping("getById")
    public Result<SaAppBannerVO> queryById(@CurrentUser CurrentAdminInfo staffInfo, @RequestParam(value = "id") Long id) {
        BannerDTO bannerDTO = saBannerApi.queryById(id);
        SaAppBannerVO b2bAppBannerVO = PojoUtils.map(bannerDTO, SaAppBannerVO.class);
        return Result.success(b2bAppBannerVO);
    }
}
