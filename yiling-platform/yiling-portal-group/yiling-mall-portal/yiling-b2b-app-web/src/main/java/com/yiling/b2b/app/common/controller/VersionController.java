package com.yiling.b2b.app.common.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.b2b.app.common.vo.AppVersionVO;
import com.yiling.b2b.app.common.vo.B2BVersionVO;
import com.yiling.b2b.app.common.vo.LastVersionVO;
import com.yiling.basic.version.api.AppVersionApi;
import com.yiling.basic.version.dto.AppVersionDTO;
import com.yiling.basic.version.enums.AppChannelEnum;
import com.yiling.basic.version.enums.AppTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.util.VersionUtils;
import com.yiling.user.system.bo.CurrentStaffInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2021/10/29
 */
@Slf4j
@Api(tags = "版本管理接口")
@RestController
@RequestMapping("/version")
public class VersionController extends BaseController {
    @DubboReference
    AppVersionApi appVersionApi;

    @ApiOperation(value = "ios查询app最新版本信息，app更新使用")
    @GetMapping("/ios/newVersion")
    public Result<B2BVersionVO> ios(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("channel") String channel, @RequestParam("version") String version) {
        if (StringUtils.isEmpty(channel) || StringUtils.isEmpty(version)) {
            return Result.failed("请求数据不完整");
        }
        AppChannelEnum appChannelEnum = AppChannelEnum.getByCode(channel);
        if (null == appChannelEnum) {
            return Result.failed("查询版本请求有误");
        }
        AppVersionDTO saVersionDTO = appVersionApi.queryB2BNews(AppTypeEnum.IOS.getCode(), appChannelEnum);
        if (null != saVersionDTO && VersionUtils.compareVersion(saVersionDTO.getVersion(), version) > 0) {
            B2BVersionVO saVersionVo = PojoUtils.map(saVersionDTO, B2BVersionVO.class);
            return Result.success(saVersionVo);
        } else {
            return Result.success();
        }
    }

    @ApiOperation(value = "安卓查询app最新版本信息，app更新使用")
    @GetMapping("/android/newVersion")
    public Result<B2BVersionVO> android(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("channel") String channel, @RequestParam("version") String version) {
        if (StringUtils.isEmpty(channel) || StringUtils.isEmpty(version)) {
            return Result.failed("请求数据不完整");
        }
        AppChannelEnum appChannelEnum = AppChannelEnum.getByCode(channel);
        if (null == appChannelEnum) {
            return Result.failed("查询版本请求有误");
        }
        AppVersionDTO saVersionDTO = appVersionApi.queryB2BNews(AppTypeEnum.ANDROID.getCode(), appChannelEnum);
        if (null != saVersionDTO && VersionUtils.compareVersion(saVersionDTO.getVersion(), version) > 0) {
            B2BVersionVO saVersionVo = PojoUtils.map(saVersionDTO, B2BVersionVO.class);
            return Result.success(saVersionVo);
        } else {
            return Result.success();
        }
    }

    @ApiOperation(value = "H5获取B2B最新版本信息")
    @GetMapping("/getLastVersion")
    public Result<LastVersionVO> getLastVersion() {
        LastVersionVO vo = new LastVersionVO();
        AppVersionDTO iosVersionDTO = appVersionApi.queryB2BNews(AppTypeEnum.IOS.getCode(), AppChannelEnum.B2B);
        if (null != iosVersionDTO) {
            iosVersionDTO.setAppType(AppTypeEnum.IOS.getCode());
            AppVersionVO iosVO = PojoUtils.map(iosVersionDTO, AppVersionVO.class);
            vo.setIosVersion(iosVO);
        }
        AppVersionDTO androidVersionDTO = appVersionApi.queryB2BNews(AppTypeEnum.ANDROID.getCode(), AppChannelEnum.B2B);
        if (null != androidVersionDTO) {
            androidVersionDTO.setAppType(AppTypeEnum.ANDROID.getCode());
            AppVersionVO androidVO = PojoUtils.map(androidVersionDTO, AppVersionVO.class);
            vo.setAndroidVersion(androidVO);
        }
        return Result.success(vo);
    }
}
