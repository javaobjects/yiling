package com.yiling.admin.sales.assistant.version.controller;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.sales.assistant.version.form.VersionAddForm;
import com.yiling.admin.sales.assistant.version.form.VersionPageForm;
import com.yiling.admin.sales.assistant.version.vo.AppInfoVO;
import com.yiling.admin.sales.assistant.version.vo.AppVersionVO;
import com.yiling.basic.version.api.AppVersionApi;
import com.yiling.basic.version.dto.AppInfoDTO;
import com.yiling.basic.version.dto.AppVersionPageDTO;
import com.yiling.basic.version.dto.request.VersionAddRequest;
import com.yiling.basic.version.dto.request.VersionPageRequest;
import com.yiling.basic.version.enums.AppChannelEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * APP版本表 前端控制器
 * </p>
 *
 * @author yong.zhang
 * @date 2021-09-14
 */
@RestController
@RequestMapping("/version")
@Api(tags = "销售助手运营后台的版本模块")
@Slf4j
public class VersionController extends BaseController {
    @DubboReference
    AppVersionApi appVersionApi;

    @ApiOperation(value = "选择app-运营后台")
    @GetMapping("/listAppInfo")
    public Result<List<AppInfoVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo) {
        List<AppInfoDTO> appInfoDTOList = appVersionApi.listAppInfoByChannelCode(AppChannelEnum.SA);
        List<AppInfoVO> voList = PojoUtils.map(appInfoDTOList, AppInfoVO.class);
        voList.forEach(appInfoVO -> appInfoVO.setChannelCode(AppChannelEnum.SA.getCode()));
        return Result.success(voList);
    }

    @ApiOperation(value = "新增和编辑版本")
    @PostMapping("/save")
    @Log(title = "新增和编辑版本", businessType = BusinessTypeEnum.INSERT)
    public Result<Object> saveVersion(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody @Valid VersionAddForm form) {
        VersionAddRequest request = PojoUtils.map(form, VersionAddRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        if (!request.getChannelCode().equals(AppChannelEnum.SA.getCode())) {
            return Result.failed("选择的app信息有误");
        }
        boolean isSuccess = appVersionApi.saveVersion(request);
        if (isSuccess) {
            return Result.success("操作成功");
        } else {
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "分页列表查询版本-运营后台")
    @PostMapping("/pageList")
    public Result<Page<AppVersionVO>> pageList(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody VersionPageForm form) {
        VersionPageRequest request = PojoUtils.map(form, VersionPageRequest.class);
        request.setChannelCode(AppChannelEnum.SA.getCode());
        Page<AppVersionPageDTO> pageDTO = appVersionApi.pageList(request);
        Page<AppVersionVO> pageVO = PojoUtils.map(pageDTO, AppVersionVO.class);
        int kb = 1024;
        for (AppVersionVO appVersionVO : pageVO.getRecords()) {
            Long packageSize = appVersionVO.getPackageSize();
            long size = packageSize * kb;
            appVersionVO.setPackageSize(size);
        }
        return Result.success(pageVO);
    }

    //    @ApiOperation(value = "APP版本表发布")
    //    @PostMapping("/create")
    //    public Result<Object> create(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody SaVersionAddForm form) {
    //        SaVersionAddRequest request = PojoUtils.map(form, SaVersionAddRequest.class);
    //        request.setOpUserId(staffInfo.getCurrentUserId());
    //        request.setOpTime(new Date());
    //        boolean res = saVersionApi.saCreate(request);
    //        if (res) {
    //            return Result.success("APP版本表发布成功！");
    //        } else {
    //            return Result.failed("APP版本表发布失败！");
    //        }
    //    }
    //
    //    @ApiOperation(value = "APP版本列表")
    //    @PostMapping("/listPage")
    //    public Result<Page<SaVersionVO>> listPage(@CurrentUser CurrentAdminInfo staffInfo, @RequestBody SaVersionPageInfoForm form) {
    //        Page<SaVersionDTO> saVersionDTOPage = saVersionApi.listSaVersion(form.getCurrent(), form.getSize());
    //        Page<SaVersionVO> pageVO = PojoUtils.map(saVersionDTOPage, SaVersionVO.class);
    //        pageVO.getRecords().stream().forEach(saVersionVO -> {
    //            UserDTO userDTO = userApi.getById(saVersionVO.getCreateUser());
    //            saVersionVO.setCreateUserName(userDTO.getName());
    //        });
    //        return Result.success(pageVO);
    //    }
}