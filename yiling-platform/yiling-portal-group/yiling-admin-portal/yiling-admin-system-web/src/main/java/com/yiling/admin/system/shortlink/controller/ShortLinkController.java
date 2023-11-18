package com.yiling.admin.system.shortlink.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.basic.shortlink.api.ShortLinkApi;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 短链接 Controller
 *
 * @author: lun.yu
 * @date: 2022/01/13
 */
@Slf4j
@RestController
@RequestMapping("/shortLink")
@Api(tags = "短链接接口")
public class ShortLinkController extends BaseController {

    @DubboReference
    ShortLinkApi shortLinkApi;

    @ApiOperation(value = "生成短链接")
    @GetMapping("/generatorShortUrl")
    public Result<String> getDictTypePage(@CurrentUser CurrentAdminInfo adminInfo , @ApiParam("长地址(原链接)") @RequestParam("longUrl") String longUrl) {
        String shortUrl = shortLinkApi.generatorShortLink(longUrl);
        return Result.success(shortUrl);
    }

}
