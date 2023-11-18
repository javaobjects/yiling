package com.yiling.sales.assistant.app.mr.version.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.basic.version.api.AppVersionApi;
import com.yiling.basic.version.dto.AppVersionDTO;
import com.yiling.basic.version.enums.AppChannelEnum;
import com.yiling.basic.version.enums.AppTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.VersionUtils;
import com.yiling.sales.assistant.app.mr.version.vo.AppVersionVO;
import com.yiling.sales.assistant.app.mr.version.vo.LastVersionVO;
import com.yiling.sales.assistant.app.mr.version.vo.VersionVO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * APP版本表 前端控制器
 *
 * @author: yong.zhang
 * @date: 2022/12/26 0026
 */
@RestController
@RequestMapping("/version")
@Api(tags = "医药代表端APP的版本模块")
@Slf4j
public class VersionController extends BaseController {

    @DubboReference
    AppVersionApi appVersionApi;


    @ApiOperation(value = "ios查询app最新版本信息，app更新使用")
    @GetMapping("/ios/newVersion")
    public Result<VersionVO> ios(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("channel") String channel, @RequestParam("version") String version) {
        if (StringUtils.isEmpty(channel) || StringUtils.isEmpty(version)) {
            return Result.failed("请求数据不完整");
        }
        AppChannelEnum appChannelEnum = AppChannelEnum.getByCode(channel);
        if (null == appChannelEnum) {
            return Result.failed("查询版本请求有误");
        }
        AppVersionDTO appVersionDTO = appVersionApi.querySaNews(AppTypeEnum.IOS.getCode(), appChannelEnum);
        if (null != appVersionDTO && VersionUtils.compareVersion(appVersionDTO.getVersion(), version) > 0) {
            VersionVO versionVo = PojoUtils.map(appVersionDTO, VersionVO.class);
            return Result.success(versionVo);
        } else {
            return Result.success();
        }
    }

    @ApiOperation(value = "安卓查询app最新版本信息，app更新使用")
    @GetMapping("/android/newVersion")
    public Result<VersionVO> android(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("channel") String channel, @RequestParam("version") String version) {
        if (StringUtils.isEmpty(channel) || StringUtils.isEmpty(version)) {
            return Result.failed("请求数据不完整");
        }
        AppChannelEnum appChannelEnum = AppChannelEnum.getByCode(channel);
        if (null == appChannelEnum) {
            return Result.failed("查询版本请求有误");
        }
        AppVersionDTO appVersionDTO = appVersionApi.querySaNews(AppTypeEnum.ANDROID.getCode(), appChannelEnum);
        if (null != appVersionDTO && VersionUtils.compareVersion(appVersionDTO.getVersion(), version) > 0) {
            VersionVO versionVo = PojoUtils.map(appVersionDTO, VersionVO.class);
            return Result.success(versionVo);
        } else {
            return Result.success();
        }
    }

    @ApiOperation(value = "H5获取医药代表端最新版本信息")
    @GetMapping("/getLastVersion")
    public Result<LastVersionVO> getLastVersion() {
        LastVersionVO vo = new LastVersionVO();
        AppVersionDTO iosVersionDTO = appVersionApi.querySaNews(AppTypeEnum.IOS.getCode(), AppChannelEnum.SA_MR);
        if (null != iosVersionDTO) {
            iosVersionDTO.setAppType(AppTypeEnum.IOS.getCode());
            AppVersionVO iosVO = PojoUtils.map(iosVersionDTO, AppVersionVO.class);
            vo.setIosVersion(iosVO);
        }
        AppVersionDTO androidVersionDTO = appVersionApi.querySaNews(AppTypeEnum.ANDROID.getCode(), AppChannelEnum.SA_MR);
        if (null != androidVersionDTO) {
            androidVersionDTO.setAppType(AppTypeEnum.ANDROID.getCode());
            AppVersionVO androidVO = PojoUtils.map(androidVersionDTO, AppVersionVO.class);
            vo.setAndroidVersion(androidVO);
        }
        return Result.success(vo);
    }
}
