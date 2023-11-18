package com.yiling.admin.system.system.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yiling.admin.system.system.vo.IPInfoVO;
import com.yiling.basic.location.api.IPLocationApi;
import com.yiling.basic.location.bo.IPLocationBO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试 Controller
 *
 * @author: xuan.zhou
 * @date: 2022/10/19
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "测试模块接口")
public class TestController extends BaseController {

    @DubboReference
    IPLocationApi ipLocationApi;

    @Autowired
    private HttpServletRequest request;

    @ApiOperation(value = "获取请求IP信息")
    @GetMapping("/getIPInfo")
    public Result<IPInfoVO> getIPInfo(@CurrentUser CurrentAdminInfo adminInfo) {
        String ip = IPUtils.getIp(request);
        IPLocationBO ipLocationBO = ipLocationApi.query(ip);

        IPInfoVO ipInfoVO = new IPInfoVO();
        ipInfoVO.setIp(ip);
        ipInfoVO.setLocation(PojoUtils.map(ipLocationBO, IPInfoVO.Location.class));

        return Result.success(ipInfoVO);
    }

    @ApiOperation(value = "获取请求信息")
    @GetMapping("/getRequestInfo")
    public Result getRequestInfo(@CurrentUser CurrentAdminInfo adminInfo) {
        String ip = IPUtils.getIp(request);

        Map<String, Object> data = new HashMap<>();
        data.put("ip", ip);

        String userAgentStr = request.getHeader("user-agent");
        data.put("userAgent", userAgentStr);
        UserAgent userAgent = UserAgentUtil.parse(userAgentStr);
        data.put("userAgentInfo", JSONUtil.toJsonStr(userAgent));

        return Result.success(data);
    }
}
