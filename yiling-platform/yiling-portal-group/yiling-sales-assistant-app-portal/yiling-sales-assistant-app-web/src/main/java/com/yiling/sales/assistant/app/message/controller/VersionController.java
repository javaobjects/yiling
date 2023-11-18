package com.yiling.sales.assistant.app.message.controller;

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
import com.yiling.sales.assistant.app.message.vo.AppVersionVO;
import com.yiling.sales.assistant.app.message.vo.LastVersionVO;
import com.yiling.sales.assistant.app.message.vo.SaVersionVO;
import com.yiling.user.system.bo.CurrentStaffInfo;

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
@Api(tags = "销售助手APP的版本模块")
@Slf4j
public class VersionController extends BaseController {
    @DubboReference
    AppVersionApi appVersionApi;

    @ApiOperation(value = "ios查询app最新版本信息，app更新使用")
    @GetMapping("/ios/newVersion")
    public Result<SaVersionVO> ios(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("channel") String channel, @RequestParam("version") String version) {
        if (StringUtils.isEmpty(channel) || StringUtils.isEmpty(version)) {
            return Result.failed("请求数据不完整");
        }
        AppChannelEnum appChannelEnum = AppChannelEnum.getByCode(channel);
        if (null == appChannelEnum) {
            return Result.failed("查询版本请求有误");
        }
        AppVersionDTO saVersionDTO = appVersionApi.querySaNews(AppTypeEnum.IOS.getCode(), appChannelEnum);
        if (null != saVersionDTO && VersionUtils.compareVersion(saVersionDTO.getVersion(), version) > 0) {
            SaVersionVO saVersionVo = PojoUtils.map(saVersionDTO, SaVersionVO.class);
            return Result.success(saVersionVo);
        } else {
            return Result.success();
        }
    }

    @ApiOperation(value = "安卓查询app最新版本信息，app更新使用")
    @GetMapping("/android/newVersion")
    public Result<SaVersionVO> android(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("channel") String channel, @RequestParam("version") String version) {
        if (StringUtils.isEmpty(channel) || StringUtils.isEmpty(version)) {
            return Result.failed("请求数据不完整");
        }
        AppChannelEnum appChannelEnum = AppChannelEnum.getByCode(channel);
        if (null == appChannelEnum) {
            return Result.failed("查询版本请求有误");
        }
        AppVersionDTO saVersionDTO = appVersionApi.querySaNews(AppTypeEnum.ANDROID.getCode(), appChannelEnum);
        if (null != saVersionDTO && VersionUtils.compareVersion(saVersionDTO.getVersion(), version) > 0) {
            SaVersionVO saVersionVo = PojoUtils.map(saVersionDTO, SaVersionVO.class);
            return Result.success(saVersionVo);
        } else {
            return Result.success();
        }
    }

    @ApiOperation(value = "H5获取销售助手最新版本信息")
    @GetMapping("/getLastVersion")
    public Result<LastVersionVO> getLastVersion() {
        LastVersionVO vo = new LastVersionVO();
        AppVersionDTO iosVersionDTO = appVersionApi.querySaNews(AppTypeEnum.IOS.getCode(), AppChannelEnum.SA);
        if (null != iosVersionDTO) {
            iosVersionDTO.setAppType(AppTypeEnum.IOS.getCode());
            AppVersionVO iosVO = PojoUtils.map(iosVersionDTO, AppVersionVO.class);
            vo.setIosVersion(iosVO);
        }
        AppVersionDTO androidVersionDTO = appVersionApi.querySaNews(AppTypeEnum.ANDROID.getCode(), AppChannelEnum.SA);
        if (null != androidVersionDTO) {
            androidVersionDTO.setAppType(AppTypeEnum.ANDROID.getCode());
            AppVersionVO androidVO = PojoUtils.map(androidVersionDTO, AppVersionVO.class);
            vo.setAndroidVersion(androidVO);
        }
        return Result.success(vo);
    }
}