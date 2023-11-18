package com.yiling.activity.web.shortlink.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.basic.shortlink.api.ShortLinkApi;
import com.yiling.framework.common.base.BaseController;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 销售助手团队短信邀请 短地址 Controller
 *
 * @author: lun.yu
 * @date: 2022/01/13
 */
@Slf4j
@RestController
@RequestMapping("")
@Api(tags = "短链接接口")
public class ShortLinkController extends BaseController {

    @DubboReference
    ShortLinkApi shortLinkApi;

    @GetMapping(value = "/{urlKey:[a-z0-9A-Z]+}")
    public void invite(@PathVariable("urlKey") String urlKey, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String longUrl = shortLinkApi.getByKey(urlKey);
        if (StrUtil.isEmpty(longUrl)) {
            response.sendError(404,"没有找到");
        } else {
            response.sendRedirect(longUrl);
        }
    }

}
